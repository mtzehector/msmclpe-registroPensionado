/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.baseback.service.BaseCRUDService;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.registropensionadoback.entity.McltPersona;
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadoback.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadoback.model.TokenRegistroUsusario;
import mx.gob.imss.dpes.registropensionadoback.repository.ObtenerInfoTokenSpecification;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mx.gob.imss.dpes.registropensionadoback.repository.TokenRegistroUsuarioRepository;

/**
 *
 * @author edgar.arenas
 */
@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class TokenRegistroUsuarioPersistenceService extends BaseCRUDService<McltTokenRegistroUsuario, McltTokenRegistroUsuario, Long, Long> {

    @Autowired
    private TokenRegistroUsuarioRepository repository;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private PensionadoService pensionadoService;

    public Message<McltTokenRegistroUsuario> execute(Message<McltTokenRegistroUsuario> request) throws BusinessException {

        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute: {0}", request.getPayload());

        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute: {0}", request.getPayload().getTokenRegistroUsuario());

        // VALIDA QUE ES USUARIO NUEVO CON TOKEN VENCIDO PARA NO CONSULTAR DATOS DE USUARIO Y PENSIONADO INEXISTENTES.
        if (request.getPayload().getTokenRegistroUsuario() != null) {
            TokenRegistroUsusario tokenRegistroUsusario = request.getPayload().getTokenRegistroUsuario();
            log.log(Level.INFO, "REACTIVAR TOKEN: {0}", tokenRegistroUsusario.getToken());

            request.getPayload().setId(tokenRegistroUsusario.getId());
            request.getPayload().setToken(tokenRegistroUsusario.getToken());
            request.getPayload().setNumTelefono(tokenRegistroUsusario.getNumTelefono());
            request.getPayload().setNss(tokenRegistroUsusario.getNss());
            request.getPayload().setCurp(tokenRegistroUsusario.getCurp());

            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 3);
            dt = c.getTime();

            request.getPayload().setVigenciaToken(dt);
            request.getPayload().setCvePerfil(tokenRegistroUsusario.getCvePerfil());
            request.getPayload().setMatriculaTrabajadorImss(tokenRegistroUsusario.getMatriculaTrabajadorImss());
            request.getPayload().setDelegacionTrabajadorImss(tokenRegistroUsusario.getDelegacionTrabajadorImss());
            request.getPayload().setRfc(tokenRegistroUsusario.getRfc());
            request.getPayload().setRegistroPatronal(tokenRegistroUsusario.getRegistroPatronal());
            request.getPayload().setNumEmpleado(tokenRegistroUsusario.getNumEmpleado());

            if (tokenRegistroUsusario.getCveEntidadFinanciera() != null) {
                request.getPayload().setCveEntidadFinanciera(tokenRegistroUsusario.getCveEntidadFinanciera().toString());
            }

            request.getPayload().setOtrosDatosJson(tokenRegistroUsusario.getOtrosDatosJson());
            request.getPayload().setFirmaCartaRecibo(tokenRegistroUsusario.getFirmaCartaRecibo() == null ? 0L :
                    tokenRegistroUsusario.getFirmaCartaRecibo());
        }

        if(request.getPayload().getFirmaCartaRecibo() == null)
            request.getPayload().setFirmaCartaRecibo(0L);

        request.getPayload().setNomUsuario(request.getPayload().getCorreo());
        request.getPayload().setIndActivo(1);
        request.getPayload().setIndTokenConfirmado(0);

        if (request.getPayload().getCurp() == null || request.getPayload().getCurp().isEmpty()) {
            // VALIDA QUE ES USUARIO NUEVO CON TOKEN VENCIDO PARA NO CONSULTAR DATOS DE USUARIO Y PENSIONADO INEXISTENTES.
            if (request.getPayload().getTokenRegistroUsuario() == null) {

                log.log(Level.INFO, ">>>>>> usuarioService: {0}", request.getPayload().getCorreo());
                McltUsuario usuario = usuarioService.validarUsuario(request.getPayload().getCorreo());
                log.log(Level.INFO, ">>>>>> pensionadoService: {0}", usuario.getIdPersona());
                McltPersona pensionado = pensionadoService.obtenerDatosPensionado(usuario.getIdPersona());
                request.getPayload().setCurp(pensionado.getCveCurp());
                request.getPayload().setNss(String.valueOf(pensionado.getNumNss()));
                request.getPayload().setNumTelefono(pensionado.getTelCelular());

            }
        }

        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute BACK Persiste Token Service {0}", request);
        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute request.getPayload().getVigenciaToken() = {0}", request.getPayload().getVigenciaToken());

        McltTokenRegistroUsuario saved = save(request.getPayload());

        Date vigencia = saved.getVigenciaToken();
        Date now = new Date();
        Long vigenciaMillis = vigencia.getTime();
        Long difference = vigenciaMillis - now.getTime();
        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute vigencia= {0}", vigencia);
        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute now= {0}", now);
        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute vigenciaMillis= {0}", vigenciaMillis);
        log.log(Level.INFO, ">>>>>> TokenRegistroUsuarioPersistenceService execute difference(m)= {0}", difference / 1000 / 60);
        
        
        log.log(Level.INFO, ">>>>>> Token saved: \n {0}", saved);
        return new Message<>(saved);
    }

    public McltTokenRegistroUsuario consultaToken(String token) throws BusinessException {
        try {
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new ObtenerInfoTokenSpecification(token));
            return findOne(constraints);
        } catch(Exception e) {
            log.log(Level.SEVERE, "TokenRegistroUsuarioPersistenceService.consultaToken - " +
                    "token = [" + token + "]", e);
            throw new InvalidTokenException(InvalidTokenException.ERROR_DE_LECTURA_DE_BD);
        }
    }

    public void actualizarIndicadorToken(McltTokenRegistroUsuario token) throws BusinessException {
        try {
            if (!(token != null && token.getId() != null))
                throw new InvalidTokenException(InvalidTokenException.KEY);

            token.setIndActivo(0);
            token.setIndTokenConfirmado(1);
            token.setFirmaCartaRecibo(token.getFirmaCartaRecibo() == null ?
                0L : token.getFirmaCartaRecibo());

            save(token);
        } catch (BusinessException be) {
            log.log(Level.SEVERE, "ERROR TokenRegistroUsuarioPersistenceService.actualizarIndicadorToken - token = [" +
                    token + "]", be);
            throw be;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR TokenRegistroUsuarioPersistenceService.actualizarIndicadorToken - token = [" +
                    token + "]", e);
            throw new InvalidTokenException(InvalidTokenException.ERROR_DE_ESCRITURA_DE_BD);
        }
    }

    @Override
    public JpaSpecificationExecutor<McltTokenRegistroUsuario> getRepository() {
        return repository;
    }

    @Override
    public JpaRepository<McltTokenRegistroUsuario, Long> getJpaRepository() {
        return repository;
    }
}

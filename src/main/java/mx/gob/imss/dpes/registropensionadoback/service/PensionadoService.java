/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.service;

import java.util.logging.Level;
import java.util.Collection;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import java.util.ArrayList;

import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadoback.exception.PersonaException;
import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import mx.gob.imss.dpes.registropensionadoback.repository.ObtenerInfoTokenSpecification;
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.baseback.service.BaseCRUDService;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.registropensionadoback.entity.McltPersona;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;
import mx.gob.imss.dpes.registropensionadoback.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.PersonaByCorreoSpecification;
import mx.gob.imss.dpes.registropensionadoback.repository.PersonaPorCurpSpecification;
import mx.gob.imss.dpes.registropensionadoback.repository.PersonaRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author edgar.arenas
 */
@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class PensionadoService extends BaseCRUDService<McltPersona, McltPersona, Long, Long> {

    @Autowired
    private PersonaRepository repository;

    @Inject
    private UsuarioService registrarUsuario;

    @Inject
    private PerfilUsuarioService registrarPerfil;

    @Inject
    private TokenRegistroUsuarioPersistenceService token;

    @Inject
    private ValidarTokenService validarTokenService;

    public McltPersona registrarPensionado(RegistroRequest request) throws BusinessException {
        try {
            McltPersona persona = null;
            Long cvePerfil = request.getInfoToken().getCvePerfil();

            if (cvePerfil != null && (cvePerfil == 1 || cvePerfil == 2 || cvePerfil == 3 || cvePerfil == 4)) {

                boolean guardarPersona = false;
                persona = obtienePersonaPorCurp(request.getInfoToken().getCurp());
                if (persona == null) {
                    guardarPersona = true;
                    persona = new McltPersona();
                    persona.setNombre(request.getNombre());
                    persona.setPrimerApellido(request.getPrimerApellido());
                    persona.setSegundoApellido(request.getSegundoApellido());
                    persona.setCveCurp(request.getInfoToken().getCurp());
                    persona.setFecNacimiento(request.getFecNacimiento());
                    persona.setIndRegistrado(1);
                    persona.setNumNss(request.getInfoToken().getNss());
                    persona.setTelCelular(request.getInfoToken().getNumTelefono());
                    persona.setCorreoElectronico(request.getInfoToken().getCorreo());
                    persona.setRfc(request.getInfoToken().getRfc());
                    persona.setRegistroPatronal(request.getInfoToken().getRegistroPatronal());
                    persona.setNumEmpleado(request.getInfoToken().getNumEmpleado());
                    persona.setCveSexo(request.getCveSexo());
                }

                if (cvePerfil == 4)
                    persona.setCveEntidadFinanciera(request.getInfoToken().getCveEntidadFinanciera().longValue());

                if (guardarPersona)
                    persona = insertaRegistroPersona(persona);

            } else {
                //Registramos datos personales del pensionado
                persona = new McltPersona();
                persona.setCorreoElectronico(request.getInfoToken().getCorreo());
                persona.setNombre(request.getNombre());
                persona.setPrimerApellido(request.getPrimerApellido());
                persona.setSegundoApellido(request.getSegundoApellido());
                persona.setCveCurp(request.getInfoToken().getCurp());
                persona.setFecNacimiento(request.getFecNacimiento());
                persona.setIndRegistrado(1);

                if (request.getInfoToken().getCvePerfil().intValue() != 2) {
                    persona.setNumNss(request.getInfoToken().getNss());
                    persona.setTelCelular(request.getInfoToken().getNumTelefono());
                }
                log.log(Level.INFO, ">>>>>>>>>>>>>Entro registrarPensionado {0}", persona);
                persona = insertaRegistroPersona(persona);
            }

            if (persona.getCveUsuario() != null)
                throw new PersonaException(PersonaException.ERROR_EXISTE_RELACION_CON_USUARIO);

            McltTokenRegistroUsuario mcltToken = null;
            McltUsuario usuario = null;

            //Registramos datos de loggin del usuario (pensionado)
            if (cvePerfil != 2) {
                usuario = registrarUsuario.registrarUsuario(persona.getId(), request);
            } else {
                usuario = registrarUsuario.validaCreaUsuario(request, persona.getId());
            }

            // Consultamos Perfil en el Token para registrarlo.
            mcltToken = token.consultaToken(request.getInfoToken().getToken());

            //Registramos el id de usuario en la tabla perfiles
            registrarPerfil.registrarPerfil(usuario.getId(), mcltToken.getCvePerfil(), mcltToken.getFirmaCartaRecibo());

            //Actualizamos el idUsuario en Persona
            actualizarIdUsuarioPersona(persona, usuario.getId());

            if (mcltToken.getCvePerfil().intValue() == 6) // Operador IMSS
                actualizarMatriculaYDelegacion(persona, mcltToken, usuario.getId());

            //Actualizamos el indicador en la tabla token
            token.actualizarIndicadorToken(mcltToken);

            return persona;
        } catch(BusinessException be) {
            log.log(Level.SEVERE, "PensionadoService.registrarPensionado - " +
                    "request = [" + request + "]", be);;
            throw be;
        } catch(Exception e) {
            log.log(Level.SEVERE, "PensionadoService.registrarPensionado - " +
                            "request = [" + request + "]", e);
        }

        throw new PersonaException(PersonaException.ERROR_DESCONOCIDO_EN_EL_SERVICIO);
    }

    public McltPersona actualizarIdUsuarioPersona(McltPersona persona, Long idUsuario) throws BusinessException {
        try {
            persona.setCveUsuario(idUsuario);
            return save(persona);
        } catch(Exception e) {
            log.log(Level.SEVERE, "PensionadoService.actualizarIdUsuarioPersona - " +
                            "persona = [" + persona + "], idUsuario = [" + idUsuario + "]", e);
            throw new PersonaException(PersonaException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }

    public McltPersona actualizarMatriculaYDelegacion(McltPersona persona, McltTokenRegistroUsuario token, Long idUsuario) throws BusinessException {

        persona.setDelegacionTrabajadorImss(token.getDelegacionTrabajadorImss());
        persona.setMatriculaTrabajadorImss(token.getMatriculaTrabajadorImss());
        log.log(Level.INFO, ">>>>>>>>>>>>>BACK actualizarIdUsuarioPersona {0}", persona);

        McltPersona saved = save(persona);

        return saved;
    }

    public McltPersona obtenerDatosPensionado(Long idUusario) throws BusinessException {

        McltPersona pensionado = findOne(idUusario);
        log.log(Level.INFO, ">>>>>>>>>>>>>BACK obtenerDatosPensionado {0}", pensionado);
        return pensionado;
    }

    public McltPersona obtienePersonaPorCorreo(String correo) throws BusinessException {
        try {
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new PersonaByCorreoSpecification(correo));
            McltPersona mcltPersona = findOne(constraints);
            return mcltPersona;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "PensionadoService.obtienePersonaPorCorreo - correo = [" + correo + "]", e);
            throw new PersonaException(PersonaException.ERROR_DE_LECTURA_DE_BD);
        }
    }

    public McltPersona obtienePersonaPorCurp(String curp) throws BusinessException {
        try {
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new PersonaPorCurpSpecification(curp));
            McltPersona mcltUsuario = findOne(constraints);
            return mcltUsuario;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                "PensionadoService.obtienePersonaPorCurpYCorreoElectronico - curp = [" + curp + "]", e);
            throw new PersonaException(PersonaException.ERROR_DE_LECTURA_DE_BD);
        }
    }

    public McltPersona insertaRegistroPersona(McltPersona persona) throws BusinessException {
        try {
            return save(persona);
        } catch(Exception e) {
            log.log(Level.SEVERE, "PensionadoService.insertaRegistroPersona - " +
                            "persona = [" + persona + "]", e);
            throw new PersonaException(PersonaException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }

    @Override
    public JpaSpecificationExecutor<McltPersona> getRepository() {
        return repository;
    }

    @Override
    public JpaRepository<McltPersona, Long> getJpaRepository() {
        return repository;
    }

}

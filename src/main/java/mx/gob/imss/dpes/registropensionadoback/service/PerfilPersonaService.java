package mx.gob.imss.dpes.registropensionadoback.service;

//import mx.gob.imss.dpes.common.enums.PerfilUsuarioEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
//import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadoback.entity.PerfilPersona;
import mx.gob.imss.dpes.registropensionadoback.exception.TokenLoginException;
import mx.gob.imss.dpes.registropensionadoback.model.LoginRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.PerfilPersonaRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class PerfilPersonaService extends ServiceDefinition<LoginRequest, LoginRequest> {

    @Autowired
    private PerfilPersonaRepository perfilPersonaRepository;

    @Inject
    @ConfigProperty(name = "leyenda.login.perfil")
    private String leyendaLogInPerfil;

    @Override
    public Message<LoginRequest> execute(Message<LoginRequest> loginRequestMessage) throws BusinessException {

        try {
            PerfilPersona perfilPersona = perfilPersonaRepository.
                buscarPerfilPersonaPorEmail(loginRequestMessage.getPayload().getUsername());

            if(!(perfilPersona != null && perfilPersona.getCvePerfil() != null))
                throw new TokenLoginException(TokenLoginException.ERROR_OBTENER_PERFIL_USUARIO);

            //if(
            //    ((long) perfilPersona.getCvePerfil()) == PerfilUsuarioEnum.PENSIONADO.toValue()
            //)
            //    throw new VariableMessageException(leyendaLogInPerfil);

            loginRequestMessage.getPayload().setPerfilPersona(perfilPersona);
            return loginRequestMessage;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "PerfilPersonaService.execute [" +
                ((loginRequestMessage != null && loginRequestMessage.getPayload() != null) ?
                    loginRequestMessage.getPayload().toStringLog() : null) + "]", ex);
        }

        throw new TokenLoginException(TokenLoginException.ERROR_OBTENER_PERFIL_USUARIO);
    }

    public Boolean validaMensaje(String tokenSeguridad, String email, Long sesion) throws BusinessException {

        try {
            if(
                !(
                    tokenSeguridad != null && !tokenSeguridad.trim().isEmpty() &&
                    email != null && !email.trim().isEmpty() &&
                    sesion != null && sesion > 0
                )
            )
                return false;

            PerfilPersona perfilPersona = perfilPersonaRepository.
                buscarPerfilPersonaPorToken(tokenSeguridad);

            return perfilPersona != null &&
                    email.equals(perfilPersona.getNomUsuario()) &&
                    sesion.equals(perfilPersona.getSesion());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.log(Level.SEVERE,
                "ERROR PerfilPersonaService.validaMensaje - tokenSeguridad = [" +
                tokenSeguridad + "], email = [" + email + "], sesion = [" + sesion + ")", e);
        }

        return false;
    }

}

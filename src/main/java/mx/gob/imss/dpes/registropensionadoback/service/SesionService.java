package mx.gob.imss.dpes.registropensionadoback.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadoback.exception.TokenLoginException;
import mx.gob.imss.dpes.registropensionadoback.model.LoginRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.TokenRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class SesionService extends ServiceDefinition<LoginRequest, LoginRequest> {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Message<LoginRequest> execute(Message<LoginRequest> loginRequestMessage) throws BusinessException {

        try {
            Long sesion = tokenRepository.obtenerSesion();

            if (!(sesion != null && sesion > 0L))
                throw new TokenLoginException(TokenLoginException.ERROR_DE_SESION);

            loginRequestMessage.getPayload().setSesion(sesion);
            return loginRequestMessage;
        } 
        catch (Exception ex) {
            log.log(Level.SEVERE, "SesionService.execute [" + ((loginRequestMessage != null &&
                loginRequestMessage.getPayload() != null) ?
                    loginRequestMessage.getPayload().toStringLog() : null) + "]", ex);
        }

        throw new TokenLoginException(TokenLoginException.ERROR_DE_SESION);
    }

}

package mx.gob.imss.dpes.registropensionadoback.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadoback.entity.McltToken;
import mx.gob.imss.dpes.registropensionadoback.exception.TokenLoginException;
import mx.gob.imss.dpes.registropensionadoback.model.LoginRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.TokenRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class TokenService extends ServiceDefinition<LoginRequest, LoginRequest> {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Message<LoginRequest> execute(Message<LoginRequest> loginRequestMessage) throws BusinessException {

        Response response = null;

        try {
            if (!(loginRequestMessage != null && loginRequestMessage.getPayload() != null &&
                    loginRequestMessage.getPayload().getResponse() != null))
                return loginRequestMessage;

            LoginRequest loginRequest = loginRequestMessage.getPayload();

            McltToken mcltToken = new McltToken();
            mcltToken.setSesion(loginRequest.getSesion());
            mcltToken.setNomUsuario(loginRequest.getUsername());
            mcltToken.setToken(loginRequest.getResponse() != null ? loginRequest.getResponse().getAccessToken() : "");
            mcltToken.setCvePersona(loginRequest.getPerfilPersona() != null ?
                    loginRequest.getPerfilPersona().getCvePersona() : 0L);
            mcltToken.setCvePerfil(loginRequest.getPerfilPersona() != null ?
                    loginRequest.getPerfilPersona().getCvePerfil() : 0);
            mcltToken.setAltaRegistro(loginRequest.getBitacoraInterfaz().getAltaRegistro());

            tokenRepository.save(mcltToken);

            return loginRequestMessage;
        }
        catch (Exception ex) {
            log.log(Level.SEVERE, "TokenService.execute [" + ((loginRequestMessage != null &&
                loginRequestMessage.getPayload() != null) ?
                    loginRequestMessage.getPayload().toStringLog() : null) + "]", ex);
        }

        throw new TokenLoginException(TokenLoginException.ERROR_DE_ESCRITURA_DE_BD);
    }

}

package mx.gob.imss.dpes.registropensionadoback.service;

import mx.gob.imss.dpes.common.enums.TipoServicioEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.registropensionadoback.exception.TokenLoginException;
import mx.gob.imss.dpes.registropensionadoback.model.LoginRequest;
import mx.gob.imss.dpes.registropensionadoback.model.LoginResponse;
import mx.gob.imss.dpes.registropensionadoback.restclient.NetIQClient;
import mx.gob.imss.dpes.registropensionadoback.restclient.SSLUtil;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Date;
import java.util.logging.Level;

@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class TokenSeguridadService extends ServiceDefinition<LoginRequest, LoginRequest> {

    @Inject
    @RestClient
    NetIQClient netIQClient;

    @Inject
    @ConfigProperty(name = "netiq.grant.type")
    private String grantType;

    @Inject
    @ConfigProperty(name = "netiq.client.id")
    private String clientId;

    @Inject
    @ConfigProperty(name = "netiq.client.secret")
    private String clientSecret;

    @Inject
    @ConfigProperty(name = "netiq.scope.pensionado")
    private String scopePensionado;

    @Inject
    @ConfigProperty(name = "netiq.scope.ef.administrador.sc")
    private String scopeAdministradorEFSC;

    @Inject
    @ConfigProperty(name = "netiq.scope.ef.administrador")
    private String scopeAdministradorEF;

    @Inject
    @ConfigProperty(name = "netiq.scope.ef.promotor")
    private String scopePromotorEF;

    @Inject
    @ConfigProperty(name = "netiq.scope.ef.operador")
    private String scopeOperadorEF;

    @Inject
    @ConfigProperty(name = "netiq.scope.imss.administrador")
    private String scopeAdministradorIMSS;

    @Inject
    @ConfigProperty(name = "netiq.scope.imss.operador")
    private String scopeOperadorIMSS;

    @Inject
    @ConfigProperty(name = "netiq.no.scope")
    private String noScope;

    @Inject
    @ConfigProperty(name = "mx.gob.imss.dpes.registropensionadoback.restclient.NetIQClient/mp-rest/url")
    private String url;

    @Override
    public Message<LoginRequest> execute(Message<LoginRequest> loginRequestMessage) throws BusinessException {

        Response response = null;

        String scope = null;
        Long tiempoInicial = 0L;
        Long tiempoEjecucion = 0L;
        LoginRequest loginRequest =null;
        boolean ejecucionExitosaNetIQ = false;

        try {
            loginRequest = loginRequestMessage.getPayload();

            SSLUtil.disableSSLValidation();

            tiempoInicial = new Date().getTime();

            scope = obtenerScope(loginRequest.getPerfilPersona().getCvePerfil(),
                loginRequest.getPerfilPersona().getConvenio());

            response =
                netIQClient.obtainTokenNetIQ(
                    grantType,
                    clientId,
                    clientSecret,
                    scope,
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                );
            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            ejecucionExitosaNetIQ = true;

            loginRequest.setResponse(response.readEntity(LoginResponse.class));
        } 
        catch (Exception ex) {
            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            log.log(Level.SEVERE, "TokenService.execute [" + ((loginRequestMessage != null &&
                loginRequestMessage.getPayload() != null) ?
                    loginRequestMessage.getPayload().toStringLog() : null) + "]", ex);
            //throw new TokenLoginException(TokenLoginException.ERROR_OBTENER_TOKEN);
        } finally {
            BitacoraInterfaz bitacoraInterfaz = new BitacoraInterfaz();
            bitacoraInterfaz.setIdTipoServicio(TipoServicioEnum.NET_IQ.getId());
            bitacoraInterfaz.setExito(ejecucionExitosaNetIQ ? 1 : 0);
            bitacoraInterfaz.setSesion(loginRequest.getSesion());
            bitacoraInterfaz.setEndpoint(url);
            bitacoraInterfaz.setDescRequest("grant_type=" + grantType + "&client_id=" + clientId +
                    "&client_secret=" + clientSecret + "&scope=" + scope + "&username=" + loginRequest.getUsername());
            bitacoraInterfaz.setReponseEndpoint(ejecucionExitosaNetIQ ? loginRequest.getResponse().toString() : null);
            bitacoraInterfaz.setNumTiempoResp(tiempoEjecucion);
            bitacoraInterfaz.setAltaRegistro(tiempoInicial != null ? new Date(tiempoInicial) : new Date());

            loginRequest.setBitacoraInterfaz(bitacoraInterfaz);
        }

        return new Message<LoginRequest> (loginRequest);
    }

    private String obtenerScope(Integer cvePerfil, Integer convenio) {
        String scope = null;
        switch(cvePerfil) {
            case 1:
                scope = scopePensionado;
                break;
            case 2:
                scope = convenio == 0 ? scopeAdministradorEFSC : scopeAdministradorEF;
                break;
            case 3:
                scope = scopePromotorEF;
                break;
            case 4:
                scope = scopeOperadorEF;
                break;
            case 5:
                scope = scopeAdministradorIMSS;
                break;
            case 6:
                scope = scopeOperadorIMSS;
                break;
            default:
                scope = noScope;
                break;
        }
        return scope;
    }

}

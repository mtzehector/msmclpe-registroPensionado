package mx.gob.imss.dpes.registropensionadoback.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PartialContentFlowException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidHorario;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadoback.exception.TokenLoginException;
import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import mx.gob.imss.dpes.registropensionadoback.model.LoginRequest;
import mx.gob.imss.dpes.registropensionadoback.model.LoginResponse;
import mx.gob.imss.dpes.registropensionadoback.service.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.logging.Level;

@ApplicationScoped
@Path("/token")
public class TokenEndPoint extends BaseGUIEndPoint<LoginRequest, LoginRequest, LoginRequest> {
    @Inject
    private SesionService sesionService;
    @Inject
    private TokenSeguridadService tokenSeguridadService;
    @Inject
    private TokenService tokenService;
    @Inject
    private BitacoraInterfazService bitacoraInterfazService;
    @Inject
    private PerfilPersonaService perfilPersonaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(LoginRequest loginRequest) {
        Response response = null;
        Message<LoginRequest> loginRequestMessage = null;

        try {
            if(!(loginRequest != null && loginRequest.getUsername() != null && loginRequest.getPassword() != null))
                return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                        new UsuarioException(UsuarioException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                        null));

            this.validaHorario();

            ServiceDefinition[] steps = {perfilPersonaService, sesionService, tokenSeguridadService, tokenService};

            loginRequestMessage = perfilPersonaService.executeSteps(steps, new Message<LoginRequest>(loginRequest));

            loginRequest = loginRequestMessage.getPayload();

            if(loginRequest.getResponse() == null)
                throw new InvalidTokenException(TokenLoginException.ERROR_DE_CREDENCIALES);
            else {
                loginRequest.getResponse().setSession(loginRequest.getSesion());
                response = toResponse(new Message< LoginResponse >(loginRequestMessage.getPayload().getResponse()));
            }

        } catch (VariableMessageException e) {
            response = toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException be) {
            response = toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "TokenEndPoint.getToken loginRequest = [" +
                    ((loginRequest != null) ? loginRequest.toStringLog() : null) + "]", e);

            response = toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new TokenLoginException(TokenLoginException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null));
        } finally {
            if(loginRequestMessage != null && loginRequestMessage.getPayload() != null &&
                    loginRequestMessage.getPayload().getBitacoraInterfaz() != null) {
                BitacoraInterfaz bitacoraInterfaz = loginRequestMessage.getPayload().getBitacoraInterfaz();

                try {
                    bitacoraInterfazService.execute(new Message<>(bitacoraInterfaz));
                } catch (BusinessException bei) {
                    //Este escenario nunca debería pasar
                    log.log(Level.WARNING,
                            "ERROR TokenEndPoint.getToken error al guardar la bitacoraInterfaz = [" +
                                    bitacoraInterfaz + "]", bei);
                }
            }
        }

        return response;
    }

    private Calendar getBeginTime(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private Calendar getEndTime(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar;
    }

    private void validaHorario() throws InvalidHorario {
        Calendar cal = Calendar.getInstance();
        long tiempoActual = cal.getTimeInMillis();
        if(
                !(
                        cal.after(this.getBeginTime(tiempoActual)) &&
                                cal.before(this.getEndTime(tiempoActual))
                )
        )
            throw new InvalidHorario();
    }
}

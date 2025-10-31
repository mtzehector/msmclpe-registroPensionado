/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.endpoint;


import java.util.Date;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.enums.TipoServicioEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.interfaces.userdata.model.UserData;
import mx.gob.imss.dpes.interfaces.userdata.model.UserRequest;
import mx.gob.imss.dpes.registropensionadoback.exception.UserOrPwdInvalidException;

import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import mx.gob.imss.dpes.registropensionadoback.service.BitacoraInterfazService;
import mx.gob.imss.dpes.registropensionadoback.service.PerfilPersonaService;
import mx.gob.imss.dpes.registropensionadoback.service.UserDataByCurpAndNssService;
import mx.gob.imss.dpes.registropensionadoback.service.UserDataService;

/**
 *
 * @author gabriel.rios
 */
@ApplicationScoped
@Path("/user-data")
public class UserDataEndPoint extends BaseGUIEndPoint<UserRequest, UserRequest, UserRequest> {

    @Inject
    private UserDataService userDataService;

    @Inject
    private UserDataByCurpAndNssService byCurpAndNssService;

    @Inject
    private BitacoraInterfazService bitacoraInterfazService;

    @Inject
    private PerfilPersonaService perfilPersonaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserData(@Context HttpHeaders headers, UserRequest request) {

        Response response = null;
        UserData userData = null;
        boolean exito = false;
        boolean almacenaBitacora = false;
        Long tiempoInicial = 0L;
        Long tiempoEjecucion = 0L;

        try {
            if(
                !(request != null && request.getUserName() != null && request.getSesion() != null) ||
                !perfilPersonaService.validaMensaje(this.obtenerTokenSeguridad(headers.getRequestHeaders()),
                    request.getUserName(), request.getSesion())
            )
                return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                        new UsuarioException(UsuarioException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                        null));

            almacenaBitacora = true;

            tiempoInicial = new Date().getTime();
            userData = userDataService.findUserData(request.getUserName());
            tiempoEjecucion = new Date().getTime() - tiempoInicial;

            if (userData == null)
                throw new UserOrPwdInvalidException();

            exito = true;

            response = toResponse(new Message<>(userData));
        } catch (BusinessException be) {
            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            response = toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            log.log(Level.SEVERE, "ERROR UserDataEndPoint.getUserData request = [" + request + "]", e);
            response = toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new UsuarioException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null));
        } finally {
            if (almacenaBitacora) {
                BitacoraInterfaz bitacoraInterfaz = new BitacoraInterfaz();
                bitacoraInterfaz.setIdTipoServicio(TipoServicioEnum.SIPRE2.getId());
                bitacoraInterfaz.setExito(exito ? 1 : 0);
                bitacoraInterfaz.setSesion(request.getSesion());
                bitacoraInterfaz.setEndpoint("/registroPensionadoBack/webresources/user-data");
                bitacoraInterfaz.setDescRequest("{\"userName\":\"" + request.getUserName() +
                        "\", \"sesion\": " + request.getSesion() + "}");
                bitacoraInterfaz.setReponseEndpoint(exito ? userData.toString() : null);
                bitacoraInterfaz.setNumTiempoResp(tiempoEjecucion);
                bitacoraInterfaz.setAltaRegistro(new Date());

                try {
                    bitacoraInterfazService.execute(new Message<>(bitacoraInterfaz));
                } catch (BusinessException bei) {
                    //Este escenario nunca debería pasar
                    log.log(Level.WARNING,
                            "ERROR UserDataEndPoint.getUserData error al guardar la bitacoraInterfaz = [" +
                                    bitacoraInterfaz + "]", bei);
                }
            }
        }

        return response;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/byCurpAndNss")
    public Response getUserDataByCurpAndNss(UserRequest request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>UserDataEndPoint getUserDataByCurpAndNss request= {0}", request);
        UserData execute = byCurpAndNssService.findUserData(request.getCurp(),request.getNss());
        log.log(Level.INFO, ">>>>>>>>>UserDataEndPoint getUserDataByCurpAndNss response= {0}", execute);
        if (execute == null) {
            throw new UserOrPwdInvalidException();
        }

        return toResponse(new Message<>(execute));
    }
}

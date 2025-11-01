/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.DatabaseException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.registropensionadoback.assembler.UsuarioAssembler;
import mx.gob.imss.dpes.registropensionadoback.entity.*;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidCorreoTokenException;
import mx.gob.imss.dpes.registropensionadoback.exception.PersonaException;
import mx.gob.imss.dpes.registropensionadoback.exception.UserOrPwdInvalidException;
import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import mx.gob.imss.dpes.registropensionadoback.model.PregistroRequest;
import mx.gob.imss.dpes.registropensionadoback.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadoback.model.UsuarioRQ;
import mx.gob.imss.dpes.registropensionadoback.service.*;

/**
 *
 * @author edgar.arenas
 */
@ApplicationScoped
@Path("/registroPensionado")
public class RegistroPensionadoEndPoint extends BaseGUIEndPoint<McltTokenRegistroUsuario, McltTokenRegistroUsuario, McltTokenRegistroUsuario> {

    @Inject
    private TokenRegistroUsuarioPersistenceService tokenRegistroUsuarioPersistenceService;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private ValidarTokenService validarTokenService;

    @Inject
    private PensionadoService registroService;

    @Inject
    private LogginService logginService;

    @Inject
    UsuarioJpaService usuarioJpaService;
    
    @Inject
    ObtieneTokenCreacionPorEmailService valCorreoTokenService;

    @Inject
    private UsuarioAssembler usuarioAssembler;

    @Inject
    private PerfilUsuarioService perfilUsuarioService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response create(McltTokenRegistroUsuario mcltRegistro) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint Entro createEndPoint {0}", mcltRegistro);
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint mcltRegistro.getVigenciaToken= ''{0}''", mcltRegistro.getVigenciaToken());
        Message<McltTokenRegistroUsuario> execute = tokenRegistroUsuarioPersistenceService.execute(new Message<McltTokenRegistroUsuario>(mcltRegistro));

        return toResponse(execute);
    }

    @POST
    @Path("/usuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Integer validaUsuario(PregistroRequest request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint BACK valida Usuario EndPoint {0}", request);
        Integer usuario = 0;
        McltUsuario execute = null;
        try {
            execute = usuarioService.validarUsuario(request.getCorreo());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
        if (execute != null) {
            usuario = 1;
            log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint BACK entro al if {0}", usuario);
            return usuario;
        }
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint BACK No entro al if {0}", usuario);
        return usuario;
    }

    @POST
    @Path("/tokenInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenInfoToken(PregistroRequest request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint Entro obtenInfoToken ENDPOINT {0}", request);
        Message<McltTokenRegistroUsuario> execute = validarTokenService.execute(new Message<>(request));

        return toResponse(execute);
    }

    @POST
    @Path("/registro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response persisteUsuario(RegistroRequest request) {
        try {
            //log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint  BACK persisteUsuario EndPoint {0}", request);
            McltPersona execute = registroService.registrarPensionado(request);
            //log.log(Level.INFO, "McltPersona execute JGV {0}", execute);
            return toResponse(new Message<>(execute));
        } catch (BusinessException be) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR RegistroPensionadoEndPoint.persisteUsuario - request = [" +
                    request + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new UsuarioException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @POST
    @Path("/actualizar/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPassword(RegistroRequest request) {
        try {
            //log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint Empieza actualizarPassword {0}", request);
            McltUsuario mcltUsuario = usuarioService.actualizarPassword(request);
            tokenRegistroUsuarioPersistenceService.actualizarIndicadorToken(
                tokenRegistroUsuarioPersistenceService.consultaToken(request.getInfoToken().getToken()));
            //log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint Termino actualizarPassword {0}", request);
            return toResponse(new Message<>(mcltUsuario));
        } catch (BusinessException be) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR RegistroPensionadoEndPoint.actualizarPassword - request = [" +
                    request + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new UsuarioException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @POST
    @Path("/acceder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loggeo(McltUsuario1 request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint BACK loggeo endpoint {0}", request);
        McltUsuario1 execute = logginService.loggeo(request);
        log.log(Level.INFO, ">>>>>>>>>registroPensionadoBack RegistroPensionadoEndPoint  execute= {0}", execute);
        if (execute == null) {
            throw new UserOrPwdInvalidException();
        }

        return toResponse(new Message<>(execute));
    }

    @POST
    @Path("/updateIndActivo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateIndActivo(UsuarioRQ request) throws BusinessException {
        log.log(Level.INFO, "Actualizar IndActivo {0}", request);
        usuarioJpaService.updateIndActivo(request.getNomUsuario(), request.getIndActivo());
        log.log(Level.INFO, "Actualizó IndActivo {0}", request);

        return Response.accepted().build();

    }

    @PUT
    @Path("/updateCorreo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCorreoUsuario(UsuarioRQ request) throws BusinessException {
        log.log(Level.INFO, "Actualizar NomUsuario {0}", request);
        usuarioJpaService.updateNomUsuario(request.getNomUsuario(), request.getNomUsuarioOld() );
        log.log(Level.INFO, "Actualizó NomUsuario {0}", request);

        return Response.accepted().build();

    }
    
    @GET
    @Path("/tokenCreacion/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTokenCreacionPorCorreo(@PathParam("email") String email) {
        try {
            PregistroRequest registroRequest = new PregistroRequest();
            registroRequest.setCorreo(email);

            Message<McltTokenRegistroUsuario> execute = valCorreoTokenService.execute(new Message<>(registroRequest));

            if (execute != null)
                return toResponse(execute);
            else
                return Response.noContent().build();

        } catch (BusinessException be) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR RegistroPensionadoEndPoint.correoTokenVencido - email = [" +
                    email + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new InvalidCorreoTokenException(InvalidCorreoTokenException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @GET
    @Path("/{idPersona}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioByCvePersona(@PathParam("idPersona") Long cvePersona) throws BusinessException {
        try {
        	McltUsuario usuario = usuarioService.obtenerUsuarioByCvePersona(cvePersona);
            return toResponse(new Message<>(usuarioAssembler.assemble(usuario)));
        }catch (BusinessException e){
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR RegistroPensionadoEndPoint.getUsuarioByCvePersona - cvePersona = [" +
                    cvePersona + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new UsuarioException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }
    
    @GET
    @Path("/usuario/{idPersona}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioByIDPersona(@PathParam("idPersona") Long cvePersona) throws BusinessException {
        try {
        	McltUsuario usuario = usuarioService.obtenerUsuarioByCvePersona(cvePersona);
            return toResponse(new Message<>(usuario));
        }catch (BusinessException e){
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR RegistroPensionadoEndPoint.getUsuarioByCvePersona - cvePersona = [" +
                    cvePersona + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new UsuarioException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @POST
    @Path("/registro/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response persistePensionadoPorEmail(@PathParam("email") String correo) {
        try {
            McltUsuario usuario = usuarioService.validarUsuario(correo);
            if (usuario != null)
                throw new UsuarioException(UsuarioException.ERROR_EMAIL_EXISTE);

            McltPersona persona = registroService.obtienePersonaPorCorreo(correo);
            if (!(persona != null && persona.getCveCurp() != null))
                throw new PersonaException(PersonaException.ERROR_EMAIL_NO_EXISTE_O_CURP_ES_NULO);

            usuario = usuarioService.registrarUsuarioPensionado(correo, persona.getId());
            if (!(usuario != null && usuario.getId() != null))
                throw new UsuarioException(UsuarioException.ERROR_AL_RECUPERAR_ID_DE_USUARIO);

            persona.setIndRegistrado(1);
            registroService.actualizarIdUsuarioPersona(persona, usuario.getId());
            perfilUsuarioService.registrarPerfil(usuario.getId(), 1L, 0L);

            return Response.ok().build();
        } catch (BusinessException e){
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e){
            log.log(Level.SEVERE,
                    "RegistroPensionadoEndPoint.getUsuarioByCvePersona - correo = [" + correo + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new UsuarioException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @GET
    @Path("/persona/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtienePersonaPorCorreo(@PathParam("email") String correo) {
        try {
            McltPersona persona = registroService.obtienePersonaPorCorreo(correo);

            if (persona == null)
                return Response.noContent().build();
            else
                return Response.ok(persona).build();

        } catch (BusinessException e){
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e){
            log.log(Level.SEVERE,
                    "RegistroPensionadoEndPoint.obtienePersonaPorCorreo - correo = [" + correo + "]", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new PersonaException(UsuarioException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }
}

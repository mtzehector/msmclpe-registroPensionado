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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.registropensionadoback.entity.McltPerfilUsuario;
import mx.gob.imss.dpes.registropensionadoback.exception.UserOrPwdInvalidException;
import mx.gob.imss.dpes.registropensionadoback.service.PerfilUsuarioService;


/**
 *
 * @author edgar.arenas
 */
@ApplicationScoped
@Path("/perfilPersona")
public class PerfilEndPoint extends BaseGUIEndPoint<McltPerfilUsuario, McltPerfilUsuario, McltPerfilUsuario>{
    
    @Inject
    private PerfilUsuarioService perfilService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerfil(McltPerfilUsuario request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>BACK getPerfil endpoint {0}", request);
        McltPerfilUsuario execute = perfilService.getPerfil(request);
        log.log(Level.INFO, ">>>>>>>>>BACK getPerfil endpoint response= {0}", execute);
        if(execute==null){
             throw new UserOrPwdInvalidException();
      }
            
        return toResponse(new Message<>(execute));
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{usuario}")
    public Response getPerfilUsuario(@PathParam("usuario") Long usuario) throws BusinessException {
        McltPerfilUsuario execute = perfilService.getPerfilUsuario(usuario);
        if(execute==null){
             throw new UserOrPwdInvalidException();
      }
            
        return toResponse(new Message<>(execute));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{usuario}/{firma}")
    public Response updatePerfil(@PathParam("usuario") Long usuario, @PathParam("firma") Long firma) throws BusinessException {
        McltPerfilUsuario execute = perfilService.actualizarPerfil(usuario,firma);
        if(execute==null){
             throw new UserOrPwdInvalidException();
      }
            
        return toResponse(new Message<>(execute));
    }
     
}

package mx.gob.imss.dpes.registropensionadoback.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@RegisterRestClient
public interface NetIQClient {

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public Response obtainTokenNetIQ(
      @FormParam("grant_type") String grantType,
      @FormParam("client_id") String clientId,
      @FormParam("client_secret") String clientSecret,
      @FormParam("scope") String scope,
      @FormParam("username") String username,
      @FormParam("password") String password
  );
}

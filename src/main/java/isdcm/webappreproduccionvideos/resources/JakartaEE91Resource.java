package isdcm.webappreproduccionvideos.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author 
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
    
    /**
     * Sample of GET method
     * @param info
     * @param fecha
     * @return 
     */
    @Path("getInfo")
    @GET    
    @Produces("text/html")
    public String getInfo (@QueryParam("info") String info, 
                            @QueryParam("fecha") String fecha) {
        
        return "<html><head></head> <body> Informaci&oacute;n recibida " + info + " en fecha " + fecha + " </body></html>";
    }

    /**
     * Sample of POST method
     * 
     * @param info
     * @param fecha
     * @return 
     */
    @Path("postInfo")   
    @POST    
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String postInfo (  @FormParam("info") String info, 
                              @FormParam("fecha") String fecha) 
    {                
        return "<html><head></head> <body> Informaci&oacute;n recibida " + info + " en fecha " + fecha + " </body></html>";
    }
}

package si.fri.project.image_catalog.api.v1.resource;


import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/catalog")
public class ImageCatalogResource {

    @GET
    public Response getComments() {
        return Response.ok("CATALOG API").build();
    }
}

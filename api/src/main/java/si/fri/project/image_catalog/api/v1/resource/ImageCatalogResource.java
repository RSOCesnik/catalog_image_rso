package si.fri.project.image_catalog.api.v1.resource;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import si.fri.project.image_catalog.models.ImageEntity;
import si.fri.project.image_catalog.services.ImageBean;
@ApplicationScoped
@Path("/catalog")
public class ImageCatalogResource {
    @Context
    private UriInfo uriInfo;

    @Inject
    private ImageBean imageBean;

//    @GET
//    public Response getComments() {
//        return Response.ok("CATALOG API").build();
//    }
    @GET
    public Response getPhotos() {
        List<ImageEntity> photos = imageBean.getPhotos(uriInfo);

        return Response.ok(photos).build();
    }
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response printInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("opis_projekta", "S pomocjo nasega projekta, lahko dodajate slike, jih gledate ali komentirate");
        return Response.ok(map).build();
    }

}

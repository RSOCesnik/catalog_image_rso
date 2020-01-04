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

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.project.image_catalog.models.ImageEntity;
import si.fri.project.image_catalog.services.ImageBean;
@Log
@ApplicationScoped
@Path("/catalog")
public class ImageCatalogResource {
    @Context
    private UriInfo uriInfo;

    @Inject
    private ImageBean imageBean;

    @GET
    public Response getPhotos() {
        List<ImageEntity> photos = imageBean.getPhotos(uriInfo);

        return Response.ok(photos).build();
    }

}

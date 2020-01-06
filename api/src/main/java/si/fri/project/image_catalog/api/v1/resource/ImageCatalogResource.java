package si.fri.project.image_catalog.api.v1.resource;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import si.fri.project.image_catalog.models.CommentDto;
import si.fri.project.image_catalog.models.ImageEntity;
import si.fri.project.image_catalog.models.ImagePlusComment;
import si.fri.project.image_catalog.services.ImageBean;

@Log
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/catalog")
public class ImageCatalogResource {
    @Context
    private UriInfo uriInfo;

    @Inject
    private ImageBean imageBean;

    @Metered
    @GET
    public Response getPhotos() {
        List<ImageEntity> photos = imageBean.getPhotos(uriInfo);

        return Response.ok(photos).build();
    }

    @GET
    @Path("/{photoId}")
    @Counted(name = "demo_counter", monotonic = true)
    public Response getPhoto(@PathParam("photoId")Integer photoId) {
        ImageEntity photoandcomments = imageBean.getPhoto(photoId);
        if(photoandcomments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(photoandcomments).build();
    }

    @DELETE
    @Path("{photoId}")
    public Response deleteCustomer(@PathParam("photoId") Integer photoId) {

        boolean deleted = imageBean.deletePhoto(photoId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/external/{photoId}")
    public Response getExternalData(@PathParam("photoId")Integer photoId) {
        ImageEntity photo = imageBean.getPhoto(photoId);
        String descriptionData = imageBean.getDescriptionLang(photo.getDescription());
        if(descriptionData == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(descriptionData).build();
    }


    @GET
    @Path("/info")
    public Response getInfo() {

        return Response.ok("INFO").build();
    }


}

package si.fri.project.image_catalog.api.v1.resource;


import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/catalog")
public class ImageCatalogResource {

    @GET
    public Response getComments() {
        return Response.ok("CATALOG API").build();
    }

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response printInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> clani = new ArrayList<String>();
        clani.add("bc1610");

        List<String> mikrostoritve = new ArrayList<String>();
        mikrostoritve.add("http://35.205.131.132:8081/v1/uploads");
        mikrostoritve.add("http://35.195.128.97:8082/v1/comments");

        List<String> github = new ArrayList<String>();
        github.add("https://github.com/RSOCesnik/upload_image_rso");
        github.add("https://github.com/RSOCesnik/catalog_image_rso");
        github.add("https://github.com/RSOCesnik/comment_image_rso");


        List<String> travis = new ArrayList<String>();
        travis.add("https://travis-ci.org/RSOCesnik/catalog_image_rso");
        travis.add("https://travis-ci.org/RSOCesnik/comment_image_rso");
        travis.add("https://travis-ci.org/RSOCesnik/upload_image_rso");

        List<String> dockerhub = new ArrayList<String>();
        dockerhub.add("https://hub.docker.com/repository/docker/bcesnik/rso-image-catalog");
        dockerhub.add("https://hub.docker.com/repository/docker/bcesnik/rso-image-comments");
        dockerhub.add("https://hub.docker.com/repository/docker/bcesnik/rso-image-uploader");


        map.put("clani", clani);
        map.put("opis_projekta", "S pomocjo nasega projekta, lahko dodajate slike, jih gledate ali komentirate");
        map.put("mikrostoritve", mikrostoritve);
        map.put("github", github);
        map.put("travis", travis);
        map.put("dockerhub", dockerhub);
        return Response.ok(map).build();
    }

}

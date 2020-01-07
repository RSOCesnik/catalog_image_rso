package si.fri.project.image_catalog.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.json.JSONObject;
import si.fri.project.image_catalog.models.CommentDto;
import si.fri.project.image_catalog.models.ImageEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import si.fri.project.image_catalog.models.ImagePlusComment;
import si.fri.project.image_catalog.services.AppProperties;
@ApplicationScoped
public class ImageBean {
//    private Logger log = Logger.getLogger(PhotoBean.class.getName());

    @Inject
    private EntityManager em;
    private Logger log = Logger.getLogger(ImageBean.class.getName());

    @Inject
    private AppProperties appProperties;

    private Client httpClient;

    @Inject
    @DiscoverService("image-comments-service")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<ImageEntity> getPhotos(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();
        try {
            return JPAUtils.queryEntities(em, ImageEntity.class, queryParameters);

        } catch (WebApplicationException | ProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public List<CommentDto> getComments(Integer photoId) {

        if (baseUrl.isPresent()) {

            log.info("Calling comments service: getting comments for picture " + photoId+".");

            try {
                return httpClient
                        .target(baseUrl.get() + "/v1/comments/")
                        .request().get(new GenericType<List<CommentDto>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;

    }

    public List<CommentDto> getCommentsAll() {

        if (baseUrl.isPresent()) {

            log.info("Calling Images service: getting all images.");

            try {
                return httpClient
                        .target(baseUrl.get() + "/v1/comments")
                        .request().get(new GenericType<List<CommentDto>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;

    }

    @CircuitBreaker(requestVolumeThreshold = 3)
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getDescriptionLangback")
    public String getDescriptionLang(String description) {
        log.info(String.valueOf(appProperties.isExternalServicesEnabled()));
        if(appProperties.isExternalServicesEnabled()) {
            try {
                HttpResponse<String> response = Unirest.post("https://google-translate1.p.rapidapi.com/language/translate/v2/detect")
                        .header("x-rapidapi-host", "google-translate1.p.rapidapi.com")
                        .header("x-rapidapi-key", "cdcd0362b8msh238c8ef2c593523p155b83jsn26b296767ef4")
                        .header("content-type", "application/x-www-form-urlencoded")
                        .body("q=" + URLEncoder.encode(description, "UTF-8"))
                        .asString();

                log.severe(response.getBody());
                return response.getBody();
            } catch (WebApplicationException | ProcessingException | UnirestException | UnsupportedEncodingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }else{
            return null;
        }
    }

    private String getDescriptionLangback(String description) {
        return "";
    }


    public ImageEntity getPhoto(Integer photoId) {
        ImageEntity photo = em.find(ImageEntity.class, photoId);
        if(photo == null) throw new NotFoundException();
        return photo;
    }

    public boolean deletePhoto(Integer photoId) {
        ImageEntity photo = em.find(ImageEntity.class, photoId);

        if(photo != null) {
            try {
                beginTx();
                em.remove(photo);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }

        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}

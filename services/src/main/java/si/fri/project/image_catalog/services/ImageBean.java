package si.fri.project.image_catalog.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
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
        if(appProperties.isExternalServicesEnabled()) {
            try {
                return JPAUtils.queryEntities(em, ImageEntity.class, queryParameters);

            } catch (WebApplicationException | ProcessingException e) {
                throw new InternalServerErrorException(e);
            }
        }
        return null;

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

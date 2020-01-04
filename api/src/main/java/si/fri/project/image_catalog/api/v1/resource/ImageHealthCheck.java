package si.fri.project.image_catalog.api.v1.resource;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import si.fri.project.image_catalog.services.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class ImageHealthCheck implements HealthCheck {
    @Inject
    private AppProperties appProperties;

    @Override
    public HealthCheckResponse call() {
        if(appProperties.isHealthy()) {
            return HealthCheckResponse.named(ImageHealthCheck.class.getSimpleName()).up().build();
        }
        return HealthCheckResponse.named(ImageHealthCheck.class.getSimpleName()).down().build();
    }
}
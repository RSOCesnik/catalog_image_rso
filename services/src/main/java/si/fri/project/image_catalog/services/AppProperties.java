package si.fri.project.image_catalog.services;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("app-properties")
public class AppProperties {

    @ConfigValue(value = "external-services-enabled", watch = true)
    private boolean externalServicesEnabled;

    @ConfigValue(value = "config-services-enabled", watch = true)
    private boolean commentsServicesEnabled;

    @ConfigValue(value = "service.healthy", watch = true)
    private boolean healthy;

    public boolean isExternalServicesEnabled() {
        return externalServicesEnabled;
    }

    public boolean isCommentsServicesEnabled() {
        return commentsServicesEnabled;
    }

    public void setExternalServicesEnabled(boolean externalServicesEnabled) {
        this.externalServicesEnabled = externalServicesEnabled;
    }
    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}

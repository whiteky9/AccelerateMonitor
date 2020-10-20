package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;

public interface ProductInterface {

    void addJenkinsServer(Jenkins jenkins);
    void addGithub(Github github);
}

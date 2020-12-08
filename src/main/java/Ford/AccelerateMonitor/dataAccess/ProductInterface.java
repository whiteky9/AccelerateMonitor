package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;

/**
 * Interface to allow easy implementation of additional data sources.
 * in order to use a different database, create a corresponding data access class
 * which inheirits from this interface
 */
public interface ProductInterface {

    void addJenkinsServer(Jenkins jenkins);
    void addGithub(Github github);

    Object getGithubProduct(String name);
    Object getJenkinsProduct(String name);

    void deleteProducts(String name);
}

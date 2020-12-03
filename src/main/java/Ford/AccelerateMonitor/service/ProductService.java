package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.ProductInterface;
import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductInterface productInterface;

    public ProductService(@Qualifier("productDataAccess") ProductInterface productInterface){ this.productInterface = productInterface; }

    public void addJenkinsServer(Jenkins jenkins){ productInterface.addJenkinsServer(jenkins); }

    public void addGithub(Github github){ productInterface.addGithub(github); }

    public Object getGithubProduct(String name) { return productInterface.getGithubProduct(name); }

    public Object getJenkinsProduct(String name) { return productInterface.getJenkinsProduct(name); }

}

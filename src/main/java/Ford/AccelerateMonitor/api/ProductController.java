package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.product.Jenkins;
import Ford.AccelerateMonitor.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){ this.productService = productService; }

    @PostMapping("/addJenkinsProduct")
    public void addJenkinsServer(@RequestBody Jenkins jenkins){ productService.addJenkinsServer(jenkins); }

    //@PostMapping("/addGithubProduct")
    //public void addGithub(@RequestBody )
}

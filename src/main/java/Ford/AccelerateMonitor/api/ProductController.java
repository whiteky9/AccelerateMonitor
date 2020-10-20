package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;
import Ford.AccelerateMonitor.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RequestMapping("/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){ this.productService = productService; }

    @PostMapping("/addJenkinsProduct")
    public void registerJenkins(@RequestBody Jenkins jenkins) throws ParseException {
        jenkins.getAllBuildLogs();
        productService.addJenkinsServer(jenkins);
    }

    @PostMapping("/addGithubProduct")
    public void registerGithub(@RequestBody Github github) throws IOException, InterruptedException, ParseException {
        github.getInitialCommitData();
        productService.addGithub(github);
    }
}

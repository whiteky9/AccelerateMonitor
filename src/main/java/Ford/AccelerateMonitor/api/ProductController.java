package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;
import Ford.AccelerateMonitor.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/", "http://35.9.22.89:8888/", "http://35.9.22.63:8888/", "http://accmonitor.com/"})
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
    public String registerGithub(@RequestBody Github github) throws IOException, InterruptedException, ParseException {
        String status = github.getAllCommitData();
        if(status.equals("Success"))
            productService.addGithub(github);
        return status;
    }

    @GetMapping("/getGithubProduct/{name}")
    public Object getGithubProduct(@PathVariable("name") String name){ return productService.getGithubProduct(name); }

    @GetMapping("/getJenkinsProduct/{name}")
    public Object getJenkinsProduct(@PathVariable("name") String name){ return productService.getJenkinsProduct(name); }

}

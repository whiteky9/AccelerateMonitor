package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.product.Github;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/"})
@RequestMapping("/gitStats")
@RestController
public class GitHubController {

    // for testing purposes - gets the data for first commit
    @GetMapping("/firstCommit")
    public Map<String, String> getStats() throws IOException, InterruptedException, ParseException, JSONException {
        Github githubInstance = new Github("https://api.github.com/repos/", "anoopkhera", "mytoken");

        Map<String, String> item = githubInstance.getData(githubInstance.getUrl());

        return item;

    }
}

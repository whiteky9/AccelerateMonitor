package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.GitHub;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/", "http://35.9.22.89:8888/", "http://35.9.22.63:8888/", "http://accmonitor.com/"})
@RequestMapping("/gitStats")
@RestController
public class GitHubController {

    // url used to test
    public static final String API_URL = "https://api.github.com/repos/anoopkhera/cse498-frontend/commits";

    //get the first commit
    @GetMapping("/firstCommit")
    public Map<String, String> getStats(@RequestParam(value = "urlAPI") String urlAPI) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(urlAPI))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        List<GitHub> commits = mapper.readValue(response.body(), new TypeReference<List<GitHub>>() {});


        GitHub last = commits.get(commits.size() - 1);

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("node_id", last.getNode_id());
        String date = last.getCommit().path("author").path("date").toString();
        String author = last.getCommit().path("author").path("name").toString();
        dataMap.put("date", date);
        dataMap.put("name", author);

        return dataMap;
    };
}

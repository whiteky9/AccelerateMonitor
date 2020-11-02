package Ford.AccelerateMonitor.product;

import Ford.AccelerateMonitor.SpringContext;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Commit;
import Ford.AccelerateMonitor.model.GitHub;
import Ford.AccelerateMonitor.service.RecordsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Github extends Product{

    public Github(String name, String url, String userName, String projectName, String token){
        super(name);
        this.url = url;
        this.userName = userName;
        this.projectName = projectName;
        this.token = token;
    }

    public Github(){
        super();
        url = null;
        userName = null;
        projectName = null;
        token = null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private RecordsService getRecordsService() { return SpringContext.getBean(RecordsService.class); }

    public String constructApiUrl(String URL){
        String ApiUrl = "https://api.github.com/repos" + URL.substring(URL.indexOf("github.com") + 10) + "/commits?per_page=100&page=1";
        return ApiUrl;
    }

    /*private String getAuthString(){
        return token;
    }

    private JsonNode retreiveData(String url) {
        return super.retreiveData(constructApiUrl(url), getAuthString());
    }*/

    public void getAllCommitData() throws IOException, InterruptedException, ParseException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        String apiUrl = constructApiUrl(this.url);
        boolean[] complete = {false};
        int c = 1;
        while(!complete[0]) {

            request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .header("Authorization", "token a889deec8bcac779300ed12252c0a32978731162")
                    .uri(URI.create(apiUrl))
                    .build();
            //}

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();

            List<GitHub> commits = mapper.readValue(response.body(), new TypeReference<List<GitHub>>() {
            });
            System.out.println(commits.size());

            for (int i = 0; i < commits.size(); i++) {
                GitHub last = commits.get(i);
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("node_id", last.getNode_id());
                String date = last.getCommit().path("author").path("date").asText();
                String author = last.getCommit().path("author").path("name").asText();
                String sha = last.getSha();
                dataMap.put("date", date);
                dataMap.put("name", author);

                Record record = new Commit(date, this.projectName, author, sha);

                getRecordsService().addRecord(record);
            }
            c += 1;
            if(commits.size() < 100)
                complete[0] = true;
            else
                apiUrl = apiUrl.substring(0,apiUrl.length()-digits(c)) + c;
        }
    }

    private int digits(int i){
        int c = 0;
        while(i != 0){
            i /= 10;
            c += 1;
        }
        return c;
    }

    private String url;
    private String userName;
    private String projectName;
    private String token;
}

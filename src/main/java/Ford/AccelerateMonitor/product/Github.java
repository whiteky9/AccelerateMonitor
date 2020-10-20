package Ford.AccelerateMonitor.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Github {

    public Github(
            String url,
            String userName, String token) {
        //super(jobName);
        this.url = url;
        this.userName = userName;
        this.token = token;
    }

    private String url = "https://api.github.com/repos/";

    private String userName;

    private String token;

    private String projectName = "hello-world"; // get from project instance

    private String getCommitUrl()
    {
        return url+userName+"/"+projectName+"/commits";
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getData(String url) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(getCommitUrl()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // get JSON response
        String json = response.body();

        JsonNode node = JsonNodeFactory.instance.objectNode();

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to `JsonNode`
            node = mapper.readTree(json);

        } catch (Exception e){
            e.printStackTrace();
        }


        JsonNode firstNode = node.get(node.size() - 1);

        String commitID = firstNode.get("sha").toString().substring(1,41);

        String date[] = firstNode.get("commit").get("author").get("date").toString().substring(1,11).split("-");
        String formatted = date[1] + " " + date[2] + " " + date[0];


        Map<String, String> newMap = new HashMap<>();

        newMap.put("date", formatted);
        newMap.put("commitID", commitID);
        newMap.put("projectName", projectName);
        //System.out.println(newMap);

        return newMap;
    }

}



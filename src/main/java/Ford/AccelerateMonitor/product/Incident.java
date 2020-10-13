package Ford.AccelerateMonitor.product;

import Ford.AccelerateMonitor.SpringContext;
import Ford.AccelerateMonitor.model.IncidentRecord;
import Ford.AccelerateMonitor.service.JenkinsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;

public class Incident extends TimerTask {

    // Parameterized Constructor
    // to assign the values
    // to the properties of
    // the entity
    public Incident(
            String appUrl)
    {

        this.appUrl = appUrl;

    }

    private String appUrl;

    public String getHealthUrl()
    {
        return appUrl+"/actuator/health";
    }

    public JenkinsService getJenkinsService(){ return SpringContext.getBean(JenkinsService.class); }

    public String retreiveHealthStatus() {

        // create request
        HttpEntity request = new HttpEntity(new HttpHeaders());

        // make a request
        ResponseEntity<String> response = new RestTemplate().exchange(appUrl, HttpMethod.GET, request, String.class);

        // get JSON response
        String json = response.getBody();

        JsonNode node;
        String status = "Not Available";

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to `JsonNode`
            node = mapper.readTree(json);

            status = node.get("status").asText();
        } catch (Exception e){
            e.printStackTrace();
        }

        return status;

    }

    public void run()
    {
        String ping = "Before Ping";
        int code = 0;

        try
        {
            URL url = new URL(appUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();

            code = connection.getResponseCode();

            if (code == 200)
            {
                String status = this.retreiveHealthStatus();
                if (status == "DOWN")
                {
                    // CREATE INCIDENT
                    //Record record = new IncidentRecord(projectName, date, "Down");
                    //getJenkinsService().addRecord(record);
                }
                ping = status;
            }
            else
            {
                // CREATE INCIDENT
                //Record record = new IncidentRecord(projectName, date, "Restored");
                //getJenkinsService().addRecord(record);
                ping = "Not Available";
            }


        } catch (Exception e)
        {
            // CREATE INCIDENT
            ping = "Bad Domain: "+ e.getMessage();
        }

        System.out.print(ping + '\n');

    }

}

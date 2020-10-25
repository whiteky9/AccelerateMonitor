package Ford.AccelerateMonitor.product;

import Ford.AccelerateMonitor.SpringContext;
import Ford.AccelerateMonitor.model.IncidentRecord;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.service.RecordsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimerTask;

public class Incident extends TimerTask {

    // Parameterized Constructor
    // to assign the values
    // to the properties of
    // the entity
    public Incident(
            String appUrl, String projectName)
    {

        this.appUrl = appUrl;
        this.projectName = projectName;

    }

    private String appUrl;

    private String projectName;

    private boolean isIncident = false;

    private void createIncident(String status) throws ParseException {
        Date date= new Date();
        ZonedDateTime dateTime = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.of("EST5EDT"));
        String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        Record record = new IncidentRecord(projectName, formatted, status);
        getRecordsService().addRecord(record);
    }

    public String getHealthUrl()
    {
        return appUrl+"/actuator/health";
    }

    public RecordsService getRecordsService(){ return SpringContext.getBean(RecordsService.class); }

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

    // If user registers incidents with application URL
    // Run following lines of code:
    // Incident incident = new Incident("http://localhost:8888/actuator/health", "test-project");
    // Timer timer = new Timer();
    // timer.schedule(incident, 0, 10000);
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
                if (!isIncident && status.equals("DOWN"))
                {
                    isIncident = true;
                    // CREATE DOWN RECORD
                    createIncident("Down");
                } else if (isIncident && status.equals("UP")) {
                    // CREATE RESTORED RECORD
                    isIncident = false;
                    createIncident("Restored");
                }
                ping = status;
            }
            else if (!isIncident)
            {
                isIncident = true;
                // CREATE DOWN RECORD
                createIncident("Down");
                ping = "Not Available";
            }


        } catch (Exception e)
        {
            if (!isIncident)
            {
                isIncident = true;
                // CREATE DOWN RECORD
                try {
                    createIncident("Down");
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                ping = "Bad Domain: "+ e.getMessage();
            }

        }

        System.out.print(ping + '\n');

    }

}

package Ford.AccelerateMonitor.product;

import Ford.AccelerateMonitor.SpringContext;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.service.JenkinsService;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Jenkins extends Product {

    // Parameterized Constructor
    // to assign the values
    // to the properties of
    // the entity
    /** Parameterized Constructor
     * To assign the values to the properties of the entity
     * @param url Jenkins server url
     * @param jobName name of Jenkins job
     * @param userName Jenkins username for authentication
     * @param token auth token generated from Jenkins
     * */
    public Jenkins(
            String url, String jobName,
            String userName, String token) {
        super(jobName);
        this.url = url;
        this.userName = userName;
        this.token = token;
    }

    private String url;

    private String userName;

    private String token;

    private String env = "PROD"; // UNDER ASSUMPTION EACH PIPELINE IS FOR SPECIFIC ENVIRONMENT

    private String projectName = "jenkins-test"; // WILL GET THIS INFORMATION FROM PROJECT INSTANCE

    private String getJobUrl()
    {
        return url+"/job/"+name+"/";
    }

    private JenkinsService getJenkinsService()
    {
        return SpringContext.getBean(JenkinsService.class);
    }

    /**
     * Method to create authentication string
     * @returns base64 String version of auth token
     * */
    private String getAuthString()
    {
        String authStr = userName+":"+token;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        return base64Creds;
    }

    /**
     * Method to retrieve data from Jenkins Remote Access API
     * @param url Url to send GET request to
     * @return JsonNode of retrieved data
     * */
    private JsonNode retreiveData(String url) {
        return super.retreiveData(url, getAuthString());
    }

    /** START OF PUBLIC METHODS */

    /**
     * Retrieves job data from Jenkins API using retrieveData function
     * Grabs list of all builds and creates record for each build
     * @return String list of all build logs - will change later
     * */
    public void getAllBuildLogs() throws ParseException {
        String url = getJobUrl()+"api/json";
        JsonNode jsonNode = retreiveData(url);

        List<String> allBuilds = new ArrayList<>();

        JsonNode buildsArray = jsonNode.path("builds");
        for ( JsonNode build : buildsArray )
        {
            String buildUrl = getJobUrl()+build.get("number").asText()+"/api/json";
            JsonNode buildData = retreiveData(buildUrl);

            // GET COMMIT ID
            // ONLY FINDS FIRST COMMIT ID LISTED
            // SKIP BUILD IF NO COMMIT ID
            if (buildData.findValue("SHA1") == null){
                continue;
            }
            String commitID = buildData.findValue("SHA1").asText();

            // GET BUILD RESULT
            String result = buildData.get("result").asText();

            // GET BUILD TIMESTAMP
            String epochTimeString = buildData.get("timestamp").asText();
            ZonedDateTime dateTime = Instant.ofEpochMilli(Long.parseLong(epochTimeString))
                    .atZone(ZoneId.of("EST5EDT"));
            String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

            // GET DEPLOY BOOL - not implemented

            // CREATE RECORD
            Record record = new Record(projectName, commitID, formatted, "true", result, env);
            getJenkinsService().addRecord(record);


        }

    }



}


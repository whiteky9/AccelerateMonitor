package Ford.AccelerateMonitor.product;

import Ford.AccelerateMonitor.SpringContext;
import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.service.RecordsService;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

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
     * @param projectName project name registered in portal
     * @param env environment associated with this job
     * */
    public Jenkins(
            String url, String jobName,
            String userName, String token,
            String projectName, String env) {
        super(jobName);
        this.url = url;
        this.userName = userName;
        this.token = token;
        this.projectName = projectName;
        this.env = env;
    }

    /** Default Constructor
     * For constructing via rest calls
     */
    public Jenkins(){
        super();
        this.url = null;
        this.userName = null;
        this.token = null;
        this.env = "PROD";
        this.projectName = null;
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

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private String url;

    private String userName;

    private String token;

    private String env;

    private String projectName;

    /**
     * Method to create Jenkins job specific URL
     * @returns String job specific URL
     * */
    private String getJobUrl()
    {
        return url+"/job/"+name+"/";
    }

    /**
     * Allows class to connect to database
     * @returns RecordsService
     * */
    private RecordsService getRecordsService()
    {
        return SpringContext.getBean(RecordsService.class);
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


    /**
     * Retrieves job data from Jenkins API using retrieveData function
     * Grabs list of all builds and creates record for each build
     * @return String list of all build logs - will change later
     * */
    public void getAllBuildLogs() throws ParseException {
        String url = getJobUrl()+"api/json";
        JsonNode jsonNode = retreiveData(url);

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

            // GET DEPLOY BOOL
            Boolean deployBool = false;
            JsonNode paramArray = buildData.findValue("parameters");

            if (paramArray != null) {
                for (final JsonNode paramObj : paramArray) {
                    JsonNode nameNode = paramObj.get("name");
                    if (nameNode.asText().equals("Deploy")) {
                        deployBool = paramObj.get("value").asBoolean();
                    }

                }
            }

            // CREATE RECORD
            Record record = new Build(projectName, formatted, commitID, result, deployBool, env);
            getRecordsService().addRecord(record);


        }

    }



}


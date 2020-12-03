package Ford.AccelerateMonitor.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    public Request(String statRequested, String targetTeam, String targetProject, String startDate,String endDate) throws ParseException {
        this.statRequested = statRequested;
        this.targetTeam = targetTeam;
        this.targetProject = targetProject;
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.startDate = sdf.parse(startDate);
        this.endDate = sdf.parse(endDate);
    }

    public Request(){
        this.statRequested = null;
        this.targetTeam = null;
        this.startDate = null;
        this.endDate = new Date(System.currentTimeMillis());
    }

    public Request(String body) throws JSONException, ParseException {
        JSONObject bodyJSON = new JSONObject(body);
        JSONObject queryResult = bodyJSON.getJSONObject("queryResult");
        JSONObject parameters = queryResult.getJSONObject("parameters");
        String statRequested = parameters.get("statrequested").toString();
        String targetTeamArray = parameters.get("targetteam").toString();
        String targetTeam = targetTeamArray.substring(2, targetTeamArray.length()-2);
        targetTeam = targetTeam.substring(0,1).toUpperCase() + targetTeam.substring(1);
        String targetProject = parameters.get("targetProject").toString();
        JSONArray datePeriod = parameters.getJSONArray("date-period");
        JSONObject datePeriodObject = (JSONObject) datePeriod.get(0);
        String startDateString = datePeriodObject.get("startDate").toString();
        this.setStatRequested(statRequested);
        if(targetTeam.equals(""))
            this.setTargetTeam(null);
        else
            this.setTargetTeam(targetTeam);
        if(targetProject.equals(""))
            this.setTargetProject(null);
        else
            this.setTargetProject(targetProject);
        DateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = source.parse(startDateString);
        DateFormat dest = new SimpleDateFormat("MM dd yyyy");
        startDateString = dest.format(date);
        
        this.setStartDate(startDateString);
        this.endDate = new Date(System.currentTimeMillis());
    }


    public String getStatRequested() {
        return statRequested;
    }

    public void setStatRequested(String statRequested) {
        this.statRequested = statRequested;
    }

    public String getTargetTeam() {
        return targetTeam;
    }

    public void setTargetTeam(String targetTeam) {
        this.targetTeam = targetTeam;
    }

    public String getTargetProject() { return targetProject; }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.startDate = sdf.parse(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.endDate = sdf.parse(endDate);
    }

    public void setEndDateSame(String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyyHH:mm:ss");
        this.endDate = sdf.parse(endDate + "23:59:59");
    }

    private String statRequested;
    private String targetTeam;
    private String targetProject;
    private Date startDate;
    private Date endDate;
}

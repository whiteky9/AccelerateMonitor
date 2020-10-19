package Ford.AccelerateMonitor.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    public Request(String statRequested, String targetTeam, String targetProject, String startDate/*,String endDate*/) throws ParseException {
        this.statRequested = statRequested;
        this.targetTeam = targetTeam;
        this.targetProject = targetProject;
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.startDate = sdf.parse(startDate);
        //this.endDate = endDate;//TODO add later?
    }

    public Request(){
        this.statRequested = null;
        this.targetTeam = null;
        this.startDate = null;
        //this.endDate = null;
    }

    public Request(String body) throws JSONException, ParseException {
        JSONObject bodyJSON = new JSONObject(body);
        JSONObject queryResult = bodyJSON.getJSONObject("queryResult");
        JSONObject parameters = queryResult.getJSONObject("parameters");
        String statRequested = parameters.get("statRequested").toString();
        String targetTeam = parameters.get("targetTeam").toString();
        String targetProject = parameters.get("targetProject").toString();
        JSONObject datePeriod = parameters.getJSONObject("date-period");
        String startDateString = datePeriod.get("startDate").toString();
        this.setStatRequested(statRequested);
        this.setTargetTeam(targetTeam);
        this.setTargetProjects(targetProject);
        DateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = source.parse(startDateString);
        DateFormat dest = new SimpleDateFormat("MM dd yyyy");
        startDateString = dest.format(date);
        this.setStartDate(startDateString);
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

    public void setTargetProjects(String targetProject) {
        this.targetProject = targetProject;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.startDate = sdf.parse(startDate);
    }

    /*public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.endDate = sdf.parse(endDate);
    }*/

    private String statRequested;
    private String targetTeam;
    private String targetProject;
    private Date startDate;
    //private Date endDate;
}

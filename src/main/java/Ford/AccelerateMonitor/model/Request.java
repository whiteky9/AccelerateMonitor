package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    public Request(@JsonProperty("statRequested") String statRequested,
                   @JsonProperty("targetTeam") String targetTeam,
                   @JsonProperty("targetProject") String targetProject,
                   @JsonProperty("startDate") String startDate/*,
                   @JsonProperty("endDate") String endDate*/) throws ParseException {
        this.statRequested = statRequested;
        this.targetTeam = targetTeam;
        this.targetProject = targetProject;//TODO if null get based on targetTeam
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        this.startDate = sdf.parse(startDate);
        //this.endDate = endDate;
    }

    public Request(){
        this.statRequested = null;
        this.targetTeam = null;
        this.targetProject = null;
        this.startDate = null;
        //this.endDate = null;
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

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /*public String getEndDate() {
        return endDate;
    }*/

    /*public void setEndDate(String endDate) {
        this.endDate = endDate;
    }*///later maybe?

    private String statRequested;
    private String targetTeam;
    private String targetProject;
    private Date startDate;
    //private String endDate;
}

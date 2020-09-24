package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    public Request(@JsonProperty("statRequested") String statRequested,
                   @JsonProperty("targetTeam") String targetTeam,
                   @JsonProperty("targetProject") String targetProject,
                   @JsonProperty("startDate") String startDate,
                   @JsonProperty("endDate") String endDate){
        this.statRequested = statRequested;
        this.targetTeam = targetTeam;
        this.targetProject = targetProject;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Request(){
        this.statRequested = null;
        this.targetTeam = null;
        this.targetProject = null;
        this.startDate = null;
        this.endDate = null;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private String statRequested;
    private String targetTeam;
    private String targetProject;
    private String startDate;
    private String endDate;
}

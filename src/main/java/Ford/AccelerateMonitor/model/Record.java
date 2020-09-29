package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Record {

    public Record(@JsonProperty("projectName") String projectname,
                  @JsonProperty("commitId") String commitId,
                  @JsonProperty("date") String date,
                  @JsonProperty("deployment") String deployment,
                  @JsonProperty("status") String status,
                  @JsonProperty("env") String env){
        this.projectName = projectname;
        this.commitId = commitId;
        this.date = date;
        this.deployment = deployment;
        this.status = status;
        this.env = env;
    }

    public Record(){
        this.projectName = null;
        this.commitId = null;
        this.date = null;
        this.deployment = null;
        this.status = null;
        this.env = null;

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    private String projectName;
    private String commitId;
    private String date;
    private String deployment;
    private String status;
    private String env;

}

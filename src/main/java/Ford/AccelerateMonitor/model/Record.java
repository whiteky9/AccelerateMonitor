package Ford.AccelerateMonitor.model;

public class Record {

    public Record(String projectName, String commitID, String date,Boolean deployment, String status, String env) {
        this.projectName = projectName;
        this.commitID = commitID;
        this.date = date;
        this.deployment = deployment;
        this.status = status;
        this.env = env;
    }

    public Record(){
        this.projectName = null;
        this.commitID = null;
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

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getDeployment() {
        return deployment;
    }

    public void setDeployment(Boolean deployment) {
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
    private String commitID;
    private String date;
    private Boolean deployment;
    private String status;
    private String env;

}

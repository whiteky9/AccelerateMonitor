package Ford.AccelerateMonitor.model;

public class Build extends Record{

    public Build(String projectName, String date, String commitID, String status, Boolean deployment, String env){
        super(projectName, date);
        this.commitID = commitID;
        this.status = status;
        this.deployment = deployment;
        this.env = env;
    }
    public Build(){
        super();
        this.commitID = null;
        this.status = null;
        this.env = null;
        this.deployment = null;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeployment() {
        return deployment;
    }

    public void setDeployment(Boolean deployment) {
        this.deployment = deployment;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    private String commitID;
    private String status;
    private String env;
    Boolean deployment;
}

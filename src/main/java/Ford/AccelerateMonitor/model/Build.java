package Ford.AccelerateMonitor.model;

/**
 * Used to store information from a jenkins build
 */
public class Build extends Record{

    public Build(String projectName, String date, String commitID, String status, Boolean deployment, String env,
                 Boolean notification){
        super(projectName, date);
        this.commitID = commitID;
        this.status = status;
        this.deployment = deployment;
        this.env = env;
        this.notification = notification;

    }

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
        this.notification = null;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    @Override
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

    public Boolean getNotification() { return notification; }

    public void setNotification(Boolean notification) { this.notification = notification; }

    private String commitID;
    private String status;
    private String env;
    private Boolean deployment;
    private Boolean notification;
}

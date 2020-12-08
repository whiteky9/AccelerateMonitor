package Ford.AccelerateMonitor.model;

/**
 * abstract record class
 */
public abstract class Record {

    public Record(String projectName,String date) {
        this.date = date;
        this.projectName = projectName;
    }

    public Record(){
        this.projectName = null;
        this.date = null;
    }

    public String getProjectName() { return projectName; }

    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDate() { return date; }

    public void setDate(String date) {this.date = date; }

    public String getStatus() {return null;};

    public String getUser() {return null;};

    public String getSha() {return null;};

    public String getCommitID() {return null;};

    private String date;
    private String projectName;
}

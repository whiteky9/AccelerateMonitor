package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.*;

/*
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IncidentRecord.class, name = "Incident")
})*/
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

    public abstract String getStatus() ;
/*
    public abstract String getCommitID();
    public abstract void setCommitID(String commitID);
    public abstract Boolean getDeployment();
    public abstract void setDeployment(Boolean deployment) ;
    public abstract String getStatus() ;
    public abstract void setStatus(String status) ;
    public abstract String getEnv();
    public abstract void setEnv(String env);

    public String getType();
    public void setType(String type) ;
*/
    private String date;
    private String projectName;
    //private String type;
}

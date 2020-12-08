package Ford.AccelerateMonitor.model;

/**
 * Used to store information on service failure
 */
public class IncidentRecord extends Record{

    public IncidentRecord(String projectName,String date, String status){
        super(projectName,date);
        this.status = status;
    }

    public IncidentRecord(){
        super();
        this.status = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
}

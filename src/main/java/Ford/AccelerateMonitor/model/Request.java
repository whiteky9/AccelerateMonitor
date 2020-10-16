package Ford.AccelerateMonitor.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    public Request(String statRequested, String targetTeam, String targetProject, String startDate/*,String endDate*/) throws ParseException {
        this.statRequested = statRequested;
        this.targetTeam = targetTeam;
        this.targetProject = targetProject;//TODO if null get based on targetTeam
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");//TODO fix this
        this.startDate = sdf.parse(startDate);
        //this.endDate = endDate;//TODO add later?
    }

    public Request(){
        this.statRequested = null;
        this.targetTeam = null;
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

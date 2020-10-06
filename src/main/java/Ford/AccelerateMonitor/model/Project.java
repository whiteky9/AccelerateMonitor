package Ford.AccelerateMonitor.model;

public class Project {
    public Project(){
        this.name = null;
        this.team = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    private String name;
    private String team;
}

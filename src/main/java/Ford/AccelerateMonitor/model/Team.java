package Ford.AccelerateMonitor.model;

import java.util.Map;

public class Team {
    public Team(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProjects() {
        return projects;
    }

    public void setProjects(Map<String, Object> projects) {
        this.projects = projects;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }

    private String name;
    private Map<String,Object> projects;
    private Map<String,Object> members;
}

package Ford.AccelerateMonitor.model;

import java.util.Map;
import java.util.UUID;

/**
 * Used to store information of registered teams
 */
public class Team {
    public Team(){
        this.name = null;
        this.id = UUID.randomUUID().toString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String id;
    private Map<String,Object> projects;
    private Map<String,Object> members;
}

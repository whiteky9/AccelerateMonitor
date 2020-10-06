package Ford.AccelerateMonitor.model;

import java.util.Map;
import java.util.UUID;

public class Member {

    public Member(){
        this.firstName = null;
        this.lastName = null;
        this.password = null;
        this.email = null;
        this.id= UUID.randomUUID().toString();
        this.role=null;
        this.teams = null;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getTeams(){ return teams;}

    public void setTeams(Map<String, Object> teams){
        this.teams = teams;
    }

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String id;
    private Map<String, Object> teams;
}
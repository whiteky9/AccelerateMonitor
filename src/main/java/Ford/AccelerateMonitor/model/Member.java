package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Member {
    public Member(@JsonProperty("name") String name, @JsonProperty("role") String role, @JsonProperty("id") Integer id) {
        this.name = name;
        this.role = role;
        this.id = id;
    }

    public Member(){
        this.id= null;
        this.name=null;
        this.role=null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String name;
    public String role;
    public Integer id;
}
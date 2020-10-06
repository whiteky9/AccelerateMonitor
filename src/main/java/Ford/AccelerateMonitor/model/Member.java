package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Member {
    public Member(String name, String role, Integer id, Map<String,Object> teams) {
        this.name = name;
        this.role = role;
        this.id = id;
        this.teams = teams;
    }

    public Member(){
        this.id= null;
        this.name=null;
        this.role=null;
        this.teams = null;
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

    public Map<String, Object> getTeams(){ return teams;}

    public void setTeams(Map<String, Object> teams){
        this.teams = teams;
    }

    private String name;
    private String role;
    private Integer id;
    private Map<String, Object> teams;
}
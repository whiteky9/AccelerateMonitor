package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Team;

import java.util.List;

/**
 * Interface to allow easy implementation of additional data sources.
 * in order to use a different database, create a corresponding data access class
 * which inheirits from this interface
 */
public interface TeamInterface {
    void insertTeam(Team team);
    List<Team> getAllTeams();
    Team getTeamById(String id);
    Team getTeamByName(String name);
    void deleteTeam(String id);
    void updateTeam(String id, Team team);
    void addMember(String id, String member);
    void removeMember(String id, String member);
    void addProject(String id, String project);
    void removeProject(String id, String project);
}

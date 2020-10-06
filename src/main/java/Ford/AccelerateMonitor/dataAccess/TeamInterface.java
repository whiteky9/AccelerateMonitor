package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Team;

import java.util.List;

public interface TeamInterface {
    void insertTeam(Team team);
    List<Team> getAllTeams();
    Team getTeam(String id);
    void deleteTeam(String id);
    void updateTeam(String id, Team team);
}

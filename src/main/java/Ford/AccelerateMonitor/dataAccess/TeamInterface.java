package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Team;

import java.util.List;

public interface TeamInterface {
    void insertTeam(Team team);
    List<Team> getAllTeams();
    Team getTeam(String name);
    void deleteTeam(String name);
    void updateTeam(String name, Team team);
}

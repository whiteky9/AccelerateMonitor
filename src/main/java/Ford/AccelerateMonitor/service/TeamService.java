package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.TeamInterface;
import Ford.AccelerateMonitor.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamInterface teamInterface;

    @Autowired
    public TeamService(@Qualifier("teamDataAccess") TeamInterface teamInterface){ this.teamInterface = teamInterface; }

    public void addTeam(Team team){ teamInterface.insertTeam(team); }

    public List<Team> getAllTeams(){
        return teamInterface.getAllTeams();
    }

    public Team getTeam(String name) { return teamInterface.getTeam(name); }

    public void deleteTeam(String name){ teamInterface.deleteTeam(name); }

    public void updateTeam(String name, Team team){ teamInterface.updateTeam(name, team); }
}

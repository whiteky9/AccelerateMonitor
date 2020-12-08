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

    public Team getTeamById(String id) { return teamInterface.getTeamById(id); }

    public Team getTeamByName(String name) { return teamInterface.getTeamByName(name); }

    public void deleteTeam(String id){ teamInterface.deleteTeam(id); }

    public void updateTeam(String id, Team team){ teamInterface.updateTeam(id, team); }

    public void addMember(String id, String member){ teamInterface.addMember(id, member); }

    public void removeMember(String id, String member){ teamInterface.removeMember(id, member); }

    public void addProject(String id, String project){ teamInterface.addProject(id, project); }

    public void removeProject(String id, String project){ teamInterface.removeProject(id, project); }

}

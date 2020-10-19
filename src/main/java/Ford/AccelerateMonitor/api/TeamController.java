package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Team;
import Ford.AccelerateMonitor.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/"})
@RequestMapping("teams")
@RestController
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @PostMapping("/addTeam")
    public void addTeam(@RequestBody Team team) { teamService.addTeam(team); }


    @GetMapping("/getAllTeams")
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping(path = "/getTeam/{id}")
    public Team getTeamById(@PathVariable("id") String id){ return teamService.getTeamById(id); }

    @GetMapping(path = "/getTeam/{name}")
    public Team getTeamByName(@PathVariable("name") String name){ return teamService.getTeamByName(name); }

    @DeleteMapping(path = "/deleteTeam/{id}")
    public void deleteTeam(@PathVariable("id") String id){ teamService.deleteTeam(id); }

    @PutMapping(path = "/updateTeam/{id}")
    public void updateTeam(@PathVariable("id") String id, @RequestBody Team team) { teamService.updateTeam(id, team);}

    @PutMapping(path = "/addMember/{id}")
    public void addMember(@PathVariable("id") String id, @RequestBody Map<String,String> member){ teamService.addMember(id, member.get("member")); }

    @PutMapping(path = "/removeMember/{id}")
    public void removeMember(@PathVariable("id") String id, @RequestBody Map<String,String> member){ teamService.removeMember(id, member.get("member")); }

    @PutMapping(path = "/addProject/{id}")
    public void addProject(@PathVariable("id") String id, @RequestBody Map<String,String> project){ teamService.addProject(id, project.get("project")); }

    @PutMapping(path = "/removeProject/{id}")
    public void removeProject(@PathVariable("id") String id, @RequestBody Map<String,String> project){ teamService.removeProject(id, project.get("project")); }
}

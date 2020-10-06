package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Team;
import Ford.AccelerateMonitor.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Team getTeam(@PathVariable("id") String id){ return teamService.getTeam(id); }

    @DeleteMapping(path = "/deleteTeam/{id}")
    public void deleteTeam(@PathVariable("id") String id){ teamService.deleteTeam(id); }

    @PutMapping(path = "/updateTeam/{id}")
    public void updateTeam(@PathVariable("id") String id, @RequestBody Team team) { teamService.updateTeam(id, team);}
}

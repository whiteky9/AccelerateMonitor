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

    @GetMapping(path = "/getTeam/{name}")
    public Team getTeam(@PathVariable("name") String name){ return teamService.getTeam(name); }

    @DeleteMapping(path = "/deleteTeam/{name}")
    public void deleteTeam(@PathVariable("name") String name){ teamService.deleteTeam(name); }

    @PutMapping(path = "/updateTeam/{name}")
    public void updateTeam(@PathVariable("name") String name, @RequestBody Team team) { teamService.updateTeam(name, team);}
}

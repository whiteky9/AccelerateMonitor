package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Project;
import Ford.AccelerateMonitor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("projects")
@RestController
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/addProject")
    public void addProject(@RequestBody Project project) { projectService.addProject(project); }

    @GetMapping("/getAllProjects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping(path = "/getProject/{name}")
    public Project getProject(@PathVariable("name") String name){ return projectService.getProject(name); }

    @DeleteMapping(path = "/deleteProject/{name}")
    public void deleteProject(@PathVariable("name") String name){ projectService.deleteProject(name); }

    @PutMapping(path = "/updateProject/{name}")
    public void updateProject(@PathVariable("name") String name, @RequestBody Project project) { projectService.updateProject(name, project);}
}

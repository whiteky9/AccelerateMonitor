package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Project;
import Ford.AccelerateMonitor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "http://35.9.22.64:8888/")
@RequestMapping("projects")
@RestController
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    //http://35.9.22.64:8888/
    @CrossOrigin(origins = "http://35.9.22.64:8888/")
    @PostMapping("/addProject")
    public void addProject(@RequestBody Project project) { projectService.addProject(project); }

    @CrossOrigin(origins = "http://35.9.22.64:8888/")
    @GetMapping("/getAllProjects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @CrossOrigin(origins = "http://35.9.22.64:8888/")
    @GetMapping(path = "/getProject/{id}")
    public Project getProject(@PathVariable("id") String id){ return projectService.getProject(id); }

    @CrossOrigin(origins = "http://35.9.22.64:8888/")
    @DeleteMapping(path = "/deleteProject/{id}")
    public void deleteProject(@PathVariable("id") String id){ projectService.deleteProject(id); }

    @CrossOrigin(origins = "http://35.9.22.64:8888/")
    @PutMapping(path = "/updateProject/{id}")
    public void updateProject(@PathVariable("id") String id, @RequestBody Project project) { projectService.updateProject(id, project);}
}

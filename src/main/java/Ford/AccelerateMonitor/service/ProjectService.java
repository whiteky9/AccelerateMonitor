package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.ProjectInterface;
import Ford.AccelerateMonitor.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectInterface projectInterface;

    @Autowired
    public ProjectService(@Qualifier("projectDataAccess") ProjectInterface projectInterface){ this.projectInterface = projectInterface; }

    public void addProject(Project project){ projectInterface.insertProject(project); }

    public List<Project> getAllProjects(){
        return projectInterface.getAllProjects();
    }

    public Project getProject(String id) { return projectInterface.getProject(id); }

    public void deleteProject(String id){ projectInterface.deleteProject(id); }

    public void updateProject(String id, Project project){ projectInterface.updateProject(id, project); }
}

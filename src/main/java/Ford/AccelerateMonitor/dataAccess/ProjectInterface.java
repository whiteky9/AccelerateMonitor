package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Project;

import java.util.List;

public interface ProjectInterface {
    void insertProject(Project project);
    List<Project> getAllProjects();
    Project getProject(String id);
    void deleteProject(String id);
    void updateProject(String id, Project project);
}

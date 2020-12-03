package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Project;

import java.util.List;

public interface ProjectInterface {
    void insertProject(Project project);
    List<Project> getAllProjects();
    Project getProjectById(String id);
    Project getProjectByName(String name);
    void deleteProject(String id);
    void updateProject(String id, Project project);
}

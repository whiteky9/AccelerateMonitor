package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Project;

import java.util.List;

public interface ProjectInterface {
    void insertProject(Project project);
    List<Project> getAllProjects();
    Project getProject(String name);
    void deleteProject(String name);
    void updateProject(String name, Project project);
}

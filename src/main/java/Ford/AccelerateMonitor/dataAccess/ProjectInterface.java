package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Project;

import java.util.List;

/**
 * Interface to allow easy implementation of additional data sources.
 * in order to use a different database, create a corresponding data access class
 * which inheirits from this interface
 */
public interface ProjectInterface {
    void insertProject(Project project);
    List<Project> getAllProjects();
    Project getProjectById(String id);
    Project getProjectByName(String name);
    void deleteProject(String id);
    void updateProject(String id, Project project);
}

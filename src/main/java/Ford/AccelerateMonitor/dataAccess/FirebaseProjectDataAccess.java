package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Project;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("projectDataAccess")
public class FirebaseProjectDataAccess implements ProjectInterface{
    @Autowired
    public FirebaseProjectDataAccess() throws IOException {
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json"); // replace this with the filename for the service key

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com") // replace this with firebase url
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseProjectDatabase");
    }
    @Override
    public void insertProject(Project project){
        //creates reference to project list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference projectsRef = dataRef.child("projects");

        //pushes provided project into the database
        Map<String, Object> newProject = new HashMap<>();
        newProject.put(project.getId(), project);
        projectsRef.updateChildrenAsync(newProject);
    }

    @Override
    public List<Project> getAllProjects(){
        //creates reference to project list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getProjectsRef = DB.getReference("projects");
        List<Project> projects = new ArrayList<>();
        final boolean[] complete = {false};

        getProjectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    projects.add(child.getValue(Project.class));
                }
                complete[0] = true;
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //waits for listeners to update projects
        while(!complete[0]){}
        return projects;
    }

    @Override
    public Project getProjectById(String id){
        //creates reference to project list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getProjectRef = DB.getReference("projects");
        List<Project> projects = new ArrayList<>();
        final boolean[] complete = {false};

        getProjectRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                projects.add(dataSnapshot.getValue(Project.class));
                complete[0] = true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        return projects.get(0);
    }

    @Override
    public Project getProjectByName(String name){
        //creates reference to project list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getProjectRef = DB.getReference("projects");
        List<Project> projects = new ArrayList<>();
        final boolean[] complete = {false};

        getProjectRef.orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                projects.add(dataSnapshot.getValue(Project.class));
                complete[0] = true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        return projects.get(0);
    }

    @Override
    public void deleteProject(String id){
        //creates reference to project list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference projectsRef = DB.getReference("projects");
        Map<String, Object> deleteUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};
        //
        projectsRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keys.add(dataSnapshot.getKey());
                complete[0] = true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        deleteUserById.put(keys.get(0), null);
        projectsRef.updateChildrenAsync(deleteUserById);
    }

    @Override
    public void updateProject(String id, Project project){
        //creates reference to project list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference projectsRef = DB.getReference("projects");
        Map<String, Project> updateUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};

        projectsRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keys.add(dataSnapshot.getKey());
                complete[0] = true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //
        while(!complete[0]){}
        updateUserById.put(keys.get(0), project);
        projectsRef.setValueAsync(updateUserById);
    }

    final private FirebaseApp app;
}

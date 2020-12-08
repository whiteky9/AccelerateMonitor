package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Member;
import Ford.AccelerateMonitor.model.Team;
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

@Repository("teamDataAccess")
public class FirebaseTeamDataAccess implements TeamInterface{
    @Autowired
    public FirebaseTeamDataAccess() throws IOException {
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json"); // replace this with the filename for the service key

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com") // replace this with firebase url
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseTeamDatabase");
    }

    @Override
    public void insertTeam(Team team){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference teamsRef = dataRef.child("teams");

        //pushes provided team into the database
        Map<String, Object> newTeam = new HashMap<>();
        newTeam.put(team.getId(), team);
        teamsRef.updateChildrenAsync(newTeam);
    }

    @Override
    public List<Team> getAllTeams(){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getTeamsRef = DB.getReference("teams");
        List<Team> teams = new ArrayList<>();
        final boolean[] complete = {false};

        getTeamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    teams.add(child.getValue(Team.class));
                }
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //waits for listeners to update teams
        while(!complete[0]){}
        return teams;
    }

    @Override
    public Team getTeamById(String id){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getTeamRef = DB.getReference("teams");
        List<Team> teams = new ArrayList<>();
        final boolean[] complete = {false};

        getTeamRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                teams.add(dataSnapshot.getValue(Team.class));
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
        return teams.get(0);
    }

    @Override
    public Team getTeamByName(String name){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getTeamRef = DB.getReference("teams");
        List<Team> teams = new ArrayList<>();
        final boolean[] complete = {false};

        getTeamRef.orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                teams.add(dataSnapshot.getValue(Team.class));
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
        return teams.get(0);
    }



    @Override
    public void deleteTeam(String id){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference teamsRef = DB.getReference("teams");
        Map<String, Object> deleteUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};
        //
        teamsRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
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
        teamsRef.updateChildrenAsync(deleteUserById);
    }

    @Override
    public void updateTeam(String id, Team team){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference teamsRef = DB.getReference("teams");
        Map<String, Team> updateUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};

        teamsRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
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
        updateUserById.put(keys.get(0), team);
        teamsRef.setValueAsync(updateUserById);
    }

    @Override
    public void addMember(String id, String member){
        Team team = getTeamById(id);
        Map<String, Object> members = team.getMembers();
        members.put(member,true);
        team.setMembers(members);
        updateTeam(id, team);
    }

    @Override
    public void removeMember(String id, String member){
        Team team = getTeamById(id);
        Map<String, Object> members = team.getMembers();
        members.put(member,null);
        team.setMembers(members);
        updateTeam(id, team);
    }

    @Override
    public void addProject(String id, String project){
        Team team = getTeamById(id);
        Map<String, Object> projects = team.getProjects();
        projects.put(project,true);
        team.setProjects(projects);
        updateTeam(id, team);
    }

    @Override
    public void removeProject(String id, String project){
        Team team = getTeamById(id);
        Map<String, Object> projects = team.getProjects();
        projects.put(project,null);
        team.setProjects(projects);
        updateTeam(id, team);
    }

    final private FirebaseApp app;
}

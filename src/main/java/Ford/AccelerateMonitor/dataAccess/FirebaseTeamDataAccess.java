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
                new FileInputStream("auth\\cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
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
        DatabaseReference newTeamRef = teamsRef.push();
        newTeamRef.setValueAsync(team);
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
    public Team getTeam(String name){
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
    public void deleteTeam(String name){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference teamsRef = DB.getReference("teams");
        Map<String, Object> deleteUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};
        //
        teamsRef.orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {
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
    public void updateTeam(String name, Team team){
        //creates reference to team list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference teamsRef = DB.getReference("teams");
        Map<String, Team> updateUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};

        teamsRef.orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {
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

    final private FirebaseApp app;
}

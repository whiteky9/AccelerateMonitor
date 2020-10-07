package Ford.AccelerateMonitor.dataAccess;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import Ford.AccelerateMonitor.model.Member;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import java.io.FileInputStream;
import java.io.IOException;

//Data Access class for final version of database
@Repository("memberDataAccess")
public class FirebaseMemberDataAccess implements MemberInterface {
    @Autowired
    public FirebaseMemberDataAccess() throws IOException {
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("auth\\cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseMemberDatabase");
    }

    //
    // adds a member into the database
    //
    @Override
    public void insertMember(Member member){
        //creates reference to member list in database

        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference membersRef = dataRef.child("members");


        //pushes provided member into the database
        Map<String, Object> newMember = new HashMap<>();
        newMember.put(member.getId(), member);
		membersRef.updateChildrenAsync(newMember);
    }

    //
    // gets a list of all members from the database
    //
    @Override
    public List<Member> getAllMembers(){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getMembersRef = DB.getReference("members");
        List<Member> members = new ArrayList<>();
        final boolean[] complete = {false};

        getMembersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    members.add(child.getValue(Member.class));
                }
                complete[0] = true;
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //waits for listeners to update members
        while(!complete[0]){}
        return members;
    }
    //
    // gets member by id
    //
    @Override
    public Member getMember(String id){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getMemberRef = DB.getReference("members");
        List<Member> members = new ArrayList<>();
        final boolean[] complete = {false};

        getMemberRef.orderByChild("id").equalTo(String.valueOf(id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                members.add(dataSnapshot.getValue(Member.class));
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
        return members.get(0);
    }

    //
    // removes member from database
    //
    @Override
    public void deleteMember(String id){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference membersRef = DB.getReference("members");
        Map<String, Object> deleteUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};
        //
        membersRef.orderByChild("id").equalTo(String.valueOf(id)).addChildEventListener(new ChildEventListener() {
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
        membersRef.updateChildrenAsync(deleteUserById);
    }

    @Override
    public void updateMember(String id, Member member){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference membersRef = DB.getReference("members");
        Map<String, Member> updateUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};

        membersRef.orderByChild("id").equalTo(String.valueOf(id)).addChildEventListener(new ChildEventListener() {
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
        updateUserById.put(keys.get(0), member);
        membersRef.setValueAsync(updateUserById);
    }

    @Override
    public void addTeam(String id, String team){
        Member member = getMember(id);
        Map<String, Object> teams = member.getTeams();
        teams.put(team,true);
        member.setTeams(teams);
        updateMember(id, member);
    }

    @Override
    public void removeTeam(String id, String team){
        Member member = getMember(id);
        Map<String, Object> teams = member.getTeams();
        teams.put(team,null);
        member.setTeams(teams);
        updateMember(id, member);
    }

    final private FirebaseApp app;
}

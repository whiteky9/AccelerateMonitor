package Ford.AccelerateMonitor.dataAccess;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import Ford.AccelerateMonitor.model.Member;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

//Data Access class for final version of database
@Repository("DataAccess")
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
        this.app = FirebaseApp.initializeApp(options, "FirebaseDatabase");
    }

    //
    // adds a member into the database
    //
    @Override
    public int insertMember(Member member){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference membersRef = dataRef.child("members");

        //pushes provided member into the database
        DatabaseReference newMemberRef = membersRef.push();
		newMemberRef.setValueAsync(member);

		return 0;
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
    public Member getMember(int id){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getMemberRef = DB.getReference("members");
        List<Member> members = new ArrayList<>();
        final boolean[] complete = {false};

        getMemberRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
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
    public void deleteMember(int id){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference membersRef = DB.getReference("members");
        Map<String, Object> deleteUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};
        //
        membersRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
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
        deleteUserById.put(keys.get(0), null);
        membersRef.updateChildrenAsync(deleteUserById);
    }

    @Override
    public void updateMember(int id, Member member){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference membersRef = DB.getReference("members");
        Map<String, Member> updateUserById = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final boolean[] complete = {false};

        //
        membersRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
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

    final private FirebaseApp app;
}

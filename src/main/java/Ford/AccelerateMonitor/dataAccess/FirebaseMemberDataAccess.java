package Ford.AccelerateMonitor.dataAccess;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import Ford.AccelerateMonitor.model.Member;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import java.io.FileInputStream;
import java.io.IOException;

@Repository("DataAccess")
public class FirebaseMemberDataAccess implements MemberInterface {
    @Autowired
    public FirebaseMemberDataAccess() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("auth\\cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
                .build();

        this.app = FirebaseApp.initializeApp(options, "FirebaseDatabase");
    }

    @Override
    public int insertMember(Member member){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference membersRef = dataRef.child("members");

        DatabaseReference newMemberRef = membersRef.push();
		newMemberRef.setValueAsync(member);

		return 0;
    }

    @Override
    public List<Member> getAllMembers(){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference getMembersRef = DB.getReference("members");
        List<Member> members = new ArrayList<>();

        getMembersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                members.add(member);
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
        return members;
    }

    private FirebaseApp app;
}

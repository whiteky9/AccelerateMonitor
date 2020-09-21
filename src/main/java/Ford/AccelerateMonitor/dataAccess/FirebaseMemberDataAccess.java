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

        getMembersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    members.add(child.getValue(Member.class));
                }
                System.out.println(members.size() + "i");
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //waits for listeners to update members
        while(members.size()==0){}
        return members;
    }

    final private FirebaseApp app;
}

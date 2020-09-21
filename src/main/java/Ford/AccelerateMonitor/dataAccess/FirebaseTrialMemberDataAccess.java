package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Member;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//Data access class for experimental or testing purposes
@Repository("trialDataAccess")
public class FirebaseTrialMemberDataAccess implements MemberInterface {

    @Autowired
    public FirebaseTrialMemberDataAccess() throws IOException {
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("auth\\ford-501d7-firebase-adminsdk-svb09-9d40c15937.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ford-501d7.firebaseio.com/")
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseTrialDatabase");
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

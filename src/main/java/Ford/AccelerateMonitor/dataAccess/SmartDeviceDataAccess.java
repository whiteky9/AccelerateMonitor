package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
import Ford.AccelerateMonitor.model.Team;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository("SmartDeviceDataAccess")
public class SmartDeviceDataAccess implements SmartDeviceInterface{

    @Autowired
    public SmartDeviceDataAccess() throws IOException{
        FileInputStream serviceAccount =
                new FileInputStream("auth\\cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseSmartDeviceDatabase");
    }

    @Override
    public List<Record> getLeadTimeRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }

    @Override
    public List<Record> getMTTRRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }

    @Override
    public List<Record> getDeploymentFrequencyRecords(Request request){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        List<Record> records = new ArrayList<>();
        // query by project
        if(request.getTargetProject() != null){
            records = getDFRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam() != null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            for(int i=0; i<projects.size(); i++){
                request.setTargetProjects(projects.get(i));
                records = getDFRecordsByProject(records, request, DB);
            }
            request.setTargetProjects(null);
        }
        else{
            // error
        }
        return records;
    }

    @Override
    public List<Record> getChangeFailPercentageRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }

    final private FirebaseApp app;

    //
    // helper functions
    //
    private List<Record> getDFRecordsByProject(List<Record> records, Request request, FirebaseDatabase DB){
        DatabaseReference recordsRef = DB.getReference("records");
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        final Boolean[] complete = {false};
        recordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Record record = child.getValue(Record.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && record.getDeployment()) {
                        records.add(record);
                    }
                }
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        return records;
    }

    private List<String> getProjectNamesByTeamName(Request request, FirebaseDatabase DB){
        final Team[] team = {null};
        final Boolean[] complete = {false};
        DatabaseReference teamsRef = DB.getReference("teams");
        // query project list
        teamsRef.orderByChild("name").equalTo(request.getTargetTeam()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                team[0] = dataSnapshot.getValue(Team.class);
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
        Set<String> p = team[0].getProjects().keySet();
        List<String> projects = new ArrayList<>(p);
        return projects;
    }
}


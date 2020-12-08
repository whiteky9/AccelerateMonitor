package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.model.Commit;
import Ford.AccelerateMonitor.model.IncidentRecord;
import Ford.AccelerateMonitor.model.Record;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository("recordsDataAccess")
public class RecordsDataAccess implements RecordsInterface {

    public RecordsDataAccess() throws IOException{
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json"); // replace this with the filename for the service key

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com") // replace this with firebase url
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "RecordsFirebaseDatabase");
    }

    @Override
    public void addRecord(Record record) throws ParseException {
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference recordsRef = dataRef.child("records/");
        if(record.getClass() == (new Build()).getClass()) {
            DatabaseReference buildsRef = recordsRef.child("builds/");
            Map<String, Object> newBuild = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            String key = sdf.parse(record.getDate()).getTime()+":"+record.getProjectName();
            newBuild.put(key, record);
            buildsRef.updateChildrenAsync(newBuild);
        }
        else if(record.getClass() == (new IncidentRecord()).getClass()){
            DatabaseReference incidentsRef = recordsRef.child("incidents/");
            Map<String, Object> newIncident = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            String key = sdf.parse(record.getDate()).getTime()+":"+record.getProjectName();
            newIncident.put(key, record);
            incidentsRef.updateChildrenAsync(newIncident);
        }
        else if(record.getClass() == (new Commit()).getClass()){
            DatabaseReference commitsRef = recordsRef.child("commits/");
            Map<String, Object> newCommit = new HashMap<>();
            String key = record.getSha();
            newCommit.put(key, record);
            commitsRef.updateChildrenAsync(newCommit);
        }
        else {
            //error
        }
    }

    public void deleteRecords(String name){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference buildsRef = DB.getReference("records/builds");
        Map<String, Object> deleteBuilds = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final Boolean[] complete = {false,false,false};
        buildsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Build record = child.getValue(Build.class);
                    if (record.getProjectName().equals(name))
                        keys.add(child.getKey());
                }
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        for(int i=0; i<keys.size();i++){
            deleteBuilds.put(keys.get(i), null);
        }
        buildsRef.updateChildrenAsync(deleteBuilds);


        // commits
        DatabaseReference commitsRef = DB.getReference("records/commits");
        Map<String, Object> deleteCommits = new HashMap<>();
        keys.clear();
        commitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Commit record = child.getValue(Commit.class);
                    System.out.println("here");
                    if (record.getProjectName().equals(null)){
                        keys.add(child.getKey());
                    }
                    if (record.getProjectName().equals(name))
                        keys.add(child.getKey());

                }
                complete[1] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[1]){}
        for(int i=0; i<keys.size();i++){
            deleteCommits.put(keys.get(i), null);
        }
        commitsRef.updateChildrenAsync(deleteCommits);

        // incidents
        DatabaseReference incidentsRef = DB.getReference("records/incidents");
        Map<String, Object> deleteIncidents = new HashMap<>();
        keys.clear();
        incidentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    IncidentRecord record = child.getValue(IncidentRecord.class);
                    if (record.getProjectName().equals(name))
                        keys.add(child.getKey());
                }
                complete[2] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[2]){}
        for(int i=0; i<keys.size();i++){
            deleteIncidents.put(keys.get(i), null);
        }
        incidentsRef.updateChildrenAsync(deleteIncidents);
    }



    final private FirebaseApp app;
}

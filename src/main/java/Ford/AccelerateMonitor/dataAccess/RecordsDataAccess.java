package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.model.Commit;
import Ford.AccelerateMonitor.model.IncidentRecord;
import Ford.AccelerateMonitor.model.Record;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Repository("recordsDataAccess")
public class RecordsDataAccess implements RecordsInterface {

    public RecordsDataAccess() throws IOException{
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
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

            if(record.getStatus().equals("FAILURE")){
                // TODO send build failure notification
            }
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String key = sdf.parse(record.getDate()).getTime()+":"+record.getProjectName();
            newCommit.put(key, record);
            commitsRef.updateChildrenAsync(newCommit);
        }
        else {
            //error
        }
    }

    final private FirebaseApp app;
}

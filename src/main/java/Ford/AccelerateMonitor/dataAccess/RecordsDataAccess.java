package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Build;
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
    public void addRecord(Record record){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference recordsRef = dataRef.child("records/");
        if(record.getClass() == (new Build()).getClass()) {
            DatabaseReference buildsRef = recordsRef.child("builds/");
            DatabaseReference newRecordRef = buildsRef.push();
            newRecordRef.setValueAsync(record);

            if(record.getStatus().equals("FAILURE")){
                // send build failure notification
            }
        }
        else if(record.getClass() == (new IncidentRecord()).getClass()){
            DatabaseReference incidentsRef = recordsRef.child("incidents/");
            DatabaseReference newRecordRef = incidentsRef.push();
            newRecordRef.setValueAsync(record);
        }
        else {
            //error
        }
    }

    final private FirebaseApp app;
}

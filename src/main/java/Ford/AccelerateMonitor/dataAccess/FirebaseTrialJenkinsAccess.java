package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;

@Repository("trialJenkinsDataAccess")
public class FirebaseTrialJenkinsAccess  implements JenkinsInterface {

    public FirebaseTrialJenkinsAccess () throws IOException{
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("auth\\ford-501d7-firebase-adminsdk-svb09-9d40c15937.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ford-501d7.firebaseio.com/")
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "JenkinsFirebaseTrialDatabase");
    }

    @Override
    public void addRecord(Record record){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference recordsRef = dataRef.child("records");

        DatabaseReference newRecordRef = recordsRef.push();
        newRecordRef.setValueAsync(record);
    }

    final private FirebaseApp app;
}



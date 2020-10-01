package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
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
    public List<Record> getDeploymentFrequencyRecords(Request request) throws ParseException {
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference recordsRef = DB.getReference("records");
        List<Record> records = new ArrayList<>();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date requestDate = request.getStartDate();

        recordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Record record = child.getValue(Record.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && record.getDeployment() && record.getProjectName().equals(request.getTargetProject()))
                        records.add(record);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(records.size()==0){}

        return records;
    }

    @Override
    public List<Record> getChangeFailPercentageRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }
    /*
    * get record querying
    *
    * implement DB reference/snapshot
    *
    * dataraf.addChildListener()
    * on ChildAdded{
    *
    * }
    *
    * */
    final private FirebaseApp app;
}

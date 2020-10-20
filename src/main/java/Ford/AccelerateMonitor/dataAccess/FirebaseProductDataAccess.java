package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;

@Repository("productDataAccess")
public class FirebaseProductDataAccess implements ProductInterface{
    @Autowired
    public FirebaseProductDataAccess() throws IOException{
        //creates connection to database
        FileInputStream serviceAccount =
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseProductDatabase");
    }

    @Override
    public void addJenkinsServer(Jenkins jenkins){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference jenkinsRef = dataRef.child("products/jenkins");

        //pushes provided member into the database
        DatabaseReference newRecordRef = jenkinsRef.push();
        newRecordRef.setValueAsync(jenkins);
    }

    @Override
    public void addGithub(Github github){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference githubRef = dataRef.child("products/github");

        //pushes provided member into the database
        DatabaseReference newRecordRef = githubRef.push();
        newRecordRef.setValueAsync(github);
    }

    final private FirebaseApp app;
}

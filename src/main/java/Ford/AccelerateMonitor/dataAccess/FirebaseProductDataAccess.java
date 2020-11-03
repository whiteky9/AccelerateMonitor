package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.product.Github;
import Ford.AccelerateMonitor.product.Jenkins;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> newJenkins = new HashMap<>();
        String key = jenkins.getProjectName() + ':' + jenkins.getName();
        newJenkins.put(key,jenkins);
        jenkinsRef.setValueAsync(newJenkins);
    }

    @Override
    public void addGithub(Github github){
        //creates reference to member list in database
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference githubRef = dataRef.child("products/github");

        //pushes provided member into the database
        Map<String, Object> newGithub = new HashMap<>();
        String key = github.getProjectName() + ':' + github.getName();
        newGithub.put(key,github);
        githubRef.updateChildrenAsync(newGithub);
    }

    @Override
    public Object getGithubProduct(String name) {
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference githubRef = dataRef.child("products/github");

        List<Object> products = new ArrayList<>();
        final boolean[] complete = {false};

        githubRef.orderByChild("projectName").equalTo(String.valueOf(name)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> gitProd = (Map<String, Object>) dataSnapshot.getValue();
                List<Object> git = new ArrayList<Object>(gitProd.values());

                products.add(git.get(0));
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        while(!complete[0]){}

        return products.get(0);
    }

    @Override
    public Object getJenkinsProduct(String name) {
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        DatabaseReference dataRef = DB.getReference();
        DatabaseReference jenkinsRef = dataRef.child("products/jenkins");

        List<Object> products = new ArrayList<>();
        final boolean[] complete = {false};

        jenkinsRef.orderByChild("projectName").equalTo(String.valueOf(name)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> jenkinsProd = (Map<String, Object>) dataSnapshot.getValue();
                List<Object> jenkins = new ArrayList<Object>(jenkinsProd.values());

                products.add(jenkins.get(0));
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        while(!complete[0]){}

        return products.get(0);
    }

    final private FirebaseApp app;
}

package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.*;
import Ford.AccelerateMonitor.model.Record;
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
import java.util.*;

@Repository("SmartDeviceDataAccess")
public class SmartDeviceDataAccess implements SmartDeviceInterface{

    @Autowired
    public SmartDeviceDataAccess() throws IOException{
        FileInputStream serviceAccount =
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json"); // replace this with the filename for the service key

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com") // replace this with firebase url
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseSmartDeviceDatabase");
    }

    @Override
    public Map<Commit,Build> getLeadTimeRecords(Request request) {
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        Map<Commit,Build> records = new HashMap<>();
        if(request.getTargetProject() != null){
            records = getLeadTimeRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam() != null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            if (projects == null)
                records = null;
            else {
                for (int i = 0; i < projects.size(); i++) {
                    request.setTargetProject(projects.get(i));
                    records = getLeadTimeRecordsByProject(records, request, DB);
                }
            }
            request.setTargetProject(null);

        }
        return records;
    }

    @Override
    public List<Record> getMTTRRecords(Request request){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        List<Record> records = new ArrayList<>();
        // query by project
        if(request.getTargetProject() != null){
            records = getMTTRRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam() != null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            if (projects == null)
                records = null;
            else {
                for (int i = 0; i < projects.size(); i++) {
                    request.setTargetProject(projects.get(i));
                    records = getMTTRRecordsByProject(records, request, DB);
                }
            }
            request.setTargetProject(null);
        }
        return records;
    }

    @Override
    public List<Record> getDeploymentFrequencyRecords(Request request){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        List<Record> records = new ArrayList<>();
        // query by project
        if(request.getTargetProject()!= null){
            records = getDFRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam()!= null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            if (projects == null)
                records = null;
            else {
                for (int i = 0; i < projects.size(); i++) {
                    request.setTargetProject(projects.get(i));
                    records = getDFRecordsByProject(records, request, DB);
                }
            }
            request.setTargetProject(null);
        }
        else{
            // error
        }
        return records;
    }

    @Override
    public List<Record> getChangeFailPercentageRecords(Request request){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        List<Record> records = new ArrayList<>();
        // query by project
        if(request.getTargetProject()!= null){
            records = getCFRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam()!= null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            if (projects == null)
                records = null;
            else {
                for (int i = 0; i < projects.size(); i++) {
                    request.setTargetProject(projects.get(i));
                    records = getCFRecordsByProject(records, request, DB);
                }
            }
            request.setTargetProject(null);
        }
        else{
            // error
        }
        return records;
    }

    @Override
    public List<Record> getBuildRecords(Request request){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        List<Record> records = new ArrayList<>();
        // query by project
        if(request.getTargetProject() != null){
            records = getBuildRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam() != null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            if (projects == null)
                records = null;
            else {
                for (int i = 0; i < projects.size(); i++) {
                    request.setTargetProject(projects.get(i));
                    records = getBuildRecordsByProject(records, request, DB);
                }
            }
            request.setTargetProject(null);
        }
        else{
            // error
        }
        return records;
    }

    @Override
    public List<Record> getCommitRecords(Request request){
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        List<Record> records = new ArrayList<>();
        // query by project
        if(request.getTargetProject() != null){
            records = getCommitRecordsByProject(records, request, DB);
        }
        // query by team
        else if(request.getTargetTeam() != null){
            List<String> projects = getProjectNamesByTeamName(request, DB);
            if (projects == null)
                records = null;
            else {
                for (int i = 0; i < projects.size(); i++) {
                    request.setTargetProject(projects.get(i));
                    records = getCommitRecordsByProject(records, request, DB);
                }
            }
            request.setTargetProject(null);
        }
        else{
            // error
        }
        return records;
    }

    //
    // helper functions
    //
    // Gets list of incident records
    private Map<Commit,Build> getLeadTimeRecordsByProject(Map<Commit,Build> records, Request request, FirebaseDatabase DB){
        DatabaseReference commitsRef = DB.getReference("records/commits");
        DatabaseReference buildsRef = DB.getReference("records/builds");
        final Boolean[] complete = {false};
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        // find relevant commit records
        commitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Commit record = child.getValue(Commit.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf1.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && request.getTargetProject().equals(record.getProjectName())) {
                        records.put(record, null);
                    }
                }
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        complete[0] = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Map<String, Build> builds = new HashMap<>();
        // find relevant build records
        buildsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Build build = child.getValue(Build.class);
                    Date buildDate = null;
                    Date firstDate = null;
                    try {
                        buildDate = sdf.parse(build.getDate());
                    } catch (ParseException e) {

                    }
                    // if the build was successfully deployed to production, and is in the requested date range...
                    if (buildDate.after(requestDate) && buildDate.before(request.getEndDate()) && request.getTargetProject().equals(build.getProjectName()) && build.getDeployment() && build.getStatus().equals("SUCCESS") && build.getEnv().equalsIgnoreCase("PROD")) {
                        for(Commit commit : records.keySet()){
                            // ...and if the sha matches one of the commits...
                            if (commit.getSha().equals(build.getCommitID())) {
                                // ... and there are no other corresponding builds...
                                if (records.get(commit) == null)
                                    records.put(commit, build);
                                /// ...or the other build occured after this one, then store the build in the map.
                                else{
                                    try {
                                        firstDate = sdf.parse(records.get(commit).getDate());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if(buildDate.before(firstDate))
                                        records.put(commit,build);
                                }
                            }
                        }
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

    private List<Record> getMTTRRecordsByProject(List<Record> records, Request request, FirebaseDatabase DB){
        DatabaseReference incidentsRef = DB.getReference("records/incidents");
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        final Boolean[] complete = {false};
        incidentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    IncidentRecord record = child.getValue(IncidentRecord.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && request.getTargetProject().equals(record.getProjectName()))
                        records.add(record);
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

    // Gets list of Deployment records
    private List<Record> getDFRecordsByProject(List<Record> records, Request request, FirebaseDatabase DB){
        DatabaseReference buildsRef = DB.getReference("records/builds");
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        final Boolean[] complete = {false};
        buildsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Build record = child.getValue(Build.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && record.getDeployment() && request.getTargetProject().equals(record.getProjectName())) {
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

    // Gets Change Fail records and puts them into list passed in
    private List<Record> getCFRecordsByProject(List<Record> records, Request request, FirebaseDatabase DB){
        DatabaseReference buildsRef = DB.getReference("records/builds");
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        final Boolean[] complete = {false};
        buildsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Build record = child.getValue(Build.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && record.getDeployment() && request.getTargetProject().equals(record.getProjectName())) {
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

    //
    List<Record> getBuildRecordsByProject(List<Record> records, Request request, FirebaseDatabase DB){
        DatabaseReference buildsRef = DB.getReference("records/builds");
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        final Boolean[] complete = {false};
        buildsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Build record = child.getValue(Build.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && request.getTargetProject().equalsIgnoreCase(record.getProjectName())) {
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

    //
    List<Record> getCommitRecordsByProject(List<Record> records, Request request, FirebaseDatabase DB){
        DatabaseReference commitsRef = DB.getReference("records/commits");
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        final Boolean[] complete = {false};
        commitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Commit record = child.getValue(Commit.class);
                    Date recordDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    if(recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && request.getTargetProject().equalsIgnoreCase(record.getProjectName())) {
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

    // if the team name is requested, this function uses this name to obtain relevant projects
    private List<String> getProjectNamesByTeamName(Request request, FirebaseDatabase DB){
        final Team[] teams = {null};
        final Boolean[] complete = {false};
        DatabaseReference teamsRef = DB.getReference("teams");
        // query project list
        teamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams[0] = null;
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Team team = child.getValue(Team.class);
                    if (team.getName().equals(request.getTargetTeam())){
                        teams[0] = team;
                        complete[0] = true;
                    }
                }
                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        while(!complete[0]){}
        List<String> projects = null;
        if(teams[0] != null){
            Set<String> p = teams[0].getProjects().keySet();
            projects = new ArrayList<>(p);
        }
        return projects;
    }

    final private FirebaseApp app;
}


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
                new FileInputStream("cse498-capstone-firebase-adminsdk-4g11i-67fbf0b50a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cse498-capstone.firebaseio.com")
                .build();
        //instantiates firebase app
        this.app = FirebaseApp.initializeApp(options, "FirebaseSmartDeviceDatabase");
    }

    @Override
    public Map<Build,List<Commit>> getLeadTimeRecords(Request request) {
        FirebaseDatabase DB = FirebaseDatabase.getInstance(app);
        Map<Build, List<Commit>> records = new HashMap<>();
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

    //
    // helper functions
    //
    // Gets list of incident records
    private Map<Build,List<Commit>> getLeadTimeRecordsByProject(Map<Build,List<Commit>> records, Request request, FirebaseDatabase DB){
        DatabaseReference commitsRef = DB.getReference("records/commits");
        DatabaseReference buildsRef = DB.getReference("records/builds");
        final Boolean[] complete = {false};
        Date requestDate = request.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Map<String, Build> builds = new HashMap<>();
        // find relevant build records
        buildsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Build record = child.getValue(Build.class);
                    Date recordDate = null;
                    Date firstDate = null;
                    try {
                        recordDate = sdf.parse(record.getDate());
                    } catch (ParseException e) {

                    }
                    // if the build was successfully deployed to production, and is in the requested date range...
                    if (recordDate.after(requestDate) && recordDate.before(request.getEndDate()) && request.getTargetProject().equals(record.getProjectName()) && record.getDeployment() && record.getStatus().equals("SUCCESS") && record.getEnv().equalsIgnoreCase("PROD")) {
                        // ...and if the sha is unique...
                        if (!builds.containsKey(record.getCommitID())) {
                            builds.put(record.getCommitID(), record);
                        }
                        else{
                            try {
                                firstDate = sdf.parse(builds.get(record.getCommitID()).getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // ...or occurred before the previously recorded build, then store it.
                            if(recordDate.before(firstDate)){
                                builds.put(record.getCommitID(), record);
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
        complete[0] = false;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for(Build build : builds.values()){
            records.put(build, new ArrayList<>());
        }
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
                        for(Build build : records.keySet()){
                            if(build.getCommitID().equals(record.getSha()))
                                records.get(build).add(record);
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
                    if(recordDate.after(requestDate) && request.getTargetProject().equals(record.getProjectName()))
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


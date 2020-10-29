package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.api.GoogleController;
import Ford.AccelerateMonitor.dataAccess.SmartDeviceInterface;
import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.model.Commit;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
import com.google.actions.api.DialogflowApp;
import com.google.api.services.dialogflow_fulfillment.v2.model.WebhookResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.response.ResponseBuilder;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class SmartDeviceService extends DialogflowApp {
    static Integer[] DAYSINMONTHS = new Integer[]{31,29,31,30,31,30,31,31,30,31,30,31};

    private final SmartDeviceInterface smartDeviceInterface;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(GoogleController.class);

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }
    //TODO review calculation methods

    public String getAccelerateStatString(Request request) throws ParseException {
        List<Record> records;
        Map<Commit,Build> leadTimeRecords;
        String out = "Stat not recognized";
        DecimalFormat df = new DecimalFormat("0.00");
        if(request.getStatRequested().equalsIgnoreCase("Lead Time")){
            leadTimeRecords = smartDeviceInterface.getLeadTimeRecords(request);
            if (leadTimeRecords == null)
                out = "Team Does Not Exist.";
            else {
                SimpleDateFormat buildSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                SimpleDateFormat commitSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                float buildTime;
                float commitTime;
                float totalLeadTime = 0;
                int c = 0;
                for(Commit commit: leadTimeRecords.keySet()){
                    Build build = leadTimeRecords.get(commit);
                    if(build == null)
                        continue; // commits have not been deployed to production yet
                    commitTime = commitSdf.parse(commit.getDate()).getTime();
                    buildTime = buildSdf.parse(build.getDate()).getTime();
                    totalLeadTime += buildTime - commitTime;
                    c += 1;
                }
                float averageLeadTime = totalLeadTime / c / (1000 * 60 * 60);
                out = "Average Lead Time since " + request.getStartDate() + " is: " + averageLeadTime + " hours.";
            }
        }
        if(request.getStatRequested().equalsIgnoreCase("mean time to restore")){
            records = smartDeviceInterface.getMTTRRecords(request);

            if (records.isEmpty())
                out = "Team or Project Does Not Exist.";
            else {
                // Match up records into pairs
                int pairCount = 0;
                Long sum = new Long(0);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

                HashMap<String, Record> tempMap = new HashMap<>();
                Iterator<Record> recordIterator = records.iterator();
                Record temp;

                while (recordIterator.hasNext()) {

                    // Temporarily store record
                    temp = recordIterator.next();

                    // Assuming incident records are in order of time stamp (down record before restored record)
                    // Assuming when asking for MTTR by team, there may be more than one project
                    // Does NOT consider if there is a restored record with no matching down record
                    // Add every Down record to temporary hash map with project name as key
                    // Next Restored record with the same project name is a match
                    // For each pair calculate time to restore and add to sum
                    if (temp.getStatus().equals("Down")) {
                        tempMap.put(temp.getProjectName(), temp);
                    } else if (temp.getStatus().equals("Restored")) {

                        LocalDateTime downDate = LocalDateTime.parse(temp.getDate(), formatter);
                        LocalDateTime restoredDate = LocalDateTime.parse(tempMap.get(temp.getProjectName()).getDate(), formatter);

                        Duration duration = Duration.between(restoredDate, downDate);
                        Long durationSeconds = duration.getSeconds();
                        sum += durationSeconds;

                        pairCount++;

                        tempMap.remove(temp.getProjectName());
                    }
                }
                if (pairCount == 0) {
                    out = "No incident pairs.";
                } else {
                    float mttr = sum/pairCount/60;
                    out =  pairCount + " incident record pair(s). MTTR is: "+ (int)mttr + " minutes";
                }

            }

        }
        if(request.getStatRequested().equalsIgnoreCase("deployment frequency")){
            records = smartDeviceInterface.getDeploymentFrequencyRecords(request);
            if (records == null)
                out = "Team Does Not Exist.";
            else {
                int deploys = records.size();
                Date current = new Date(System.currentTimeMillis());
                float days = (current.getTime() - request.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
                out = deploys + " deploy(s) " + " in " + (int) days + " days. Deployment Frequency is: " + df.format(deploys / days) + " " + " deploys per day";
            }
        }
        if(request.getStatRequested().equalsIgnoreCase("Change Fail Percentage")){
            records = smartDeviceInterface.getChangeFailPercentageRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equalsIgnoreCase("Builds Executed")){
            records = smartDeviceInterface.getBuildRecords(request);
            if (records == null)
                out = "Team Does Not Exist.";
            else {
                int deploys = records.size();
                out = deploys + " build(s) since " + request.getStartDate().toString() + ".";
            }
        }
        if(request.getStatRequested().equalsIgnoreCase("Commits")){
            records = smartDeviceInterface.getCommitRecords(request);
            if (records == null)
                out = "Team Does Not Exist.";
            else {
                int commits = records.size();
                out = commits + " commit(s) since " + request.getStartDate().toString() + ".";
            }
        }


        return out;
    }

    public List<Integer> getAccelerateStatList(Request request) throws ParseException {
        List<Integer> ints = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.getStartDate());
        Integer month = calendar.get(Calendar.MONTH);
        Integer year = calendar.get(Calendar.YEAR);

        for(int i=0; i<DAYSINMONTHS[month]; i++){
            request.setStartDate(String.format("%02d",month+1) + " " + String.format("%02d",i+1) + " " + year);
            request.setEndDateSame(String.format("%02d",month+1) + " " + String.format("%02d",i+1) + " " + year);
            /*if(request.getStatRequested().equals("Lead Time"))
                ints.add(smartDeviceInterface.getLeadTimeRecords(request).size());
            else if(request.getStatRequested().equals("Mean Time To Restore"))                  not yet implemented
                ints.add(smartDeviceInterface.getMTTRRecords(request).size());
            else*/ if(request.getStatRequested().equals("Deployment Frequency"))
                ints.add(smartDeviceInterface.getDeploymentFrequencyRecords(request).size());
            /*else if(request.getStatRequested().equals("Change Fail Percentage"))
                ints.add(smartDeviceInterface.getChangeFailPercentageRecords(request).size());*/
            else if(request.getStatRequested().equals("Builds Executed"))
                ints.add((smartDeviceInterface.getBuildRecords(request)).size());
            else if(request.getStatRequested().equals("Commits"))
                ints.add(smartDeviceInterface.getCommitRecords(request).size());

            else
                ints.add(-1);
        }

        return ints;
    }

    public WebhookResponse getAccelerateStatWebhook(Request request) throws ParseException {
        String out = getAccelerateStatString(request);

        ResponseBuilder responseBuilder = new ResponseBuilder().add(out);
        ActionResponse response = responseBuilder.build();
        return response.getWebhookResponse();
    }
}

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

    private final SmartDeviceInterface smartDeviceInterface;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(GoogleController.class);

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }
    //TODO review calculation methods

    public String getAccelerateStatString(Request request) throws ParseException {
        List<Record> records;
        Map<Build, List<Commit>> leadTimeRecords;
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
                for(Build build : leadTimeRecords.keySet()){
                    buildTime = buildSdf.parse(build.getDate()).getTime();
                    for(Commit commit : leadTimeRecords.get(build)){
                        commitTime = commitSdf.parse(commit.getDate()).getTime();
                        totalLeadTime += buildTime - commitTime;
                        c += 1;
                    }
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

        return out;
    }

    public WebhookResponse getAccelerateStatWebhook(Request request) throws ParseException {
        String out = getAccelerateStatString(request);

        ResponseBuilder responseBuilder = new ResponseBuilder().add(out);
        ActionResponse response = responseBuilder.build();
        return response.getWebhookResponse();
    }
}

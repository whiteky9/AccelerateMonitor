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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * service class which performs calculations to determine accelerate statistics using records obtained from
 * smartDeviceDataAccess
 */
@Service
public class SmartDeviceService extends DialogflowApp {

    static Integer[] DAYSINMONTHS = new Integer[]{31,29,31,30,31,30,31,31,30,31,30,31};

    private final SmartDeviceInterface smartDeviceInterface;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(GoogleController.class);

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }

    /**
     * given a single accelerate stat request, returns a response in string form
     * @param request google assistant request
     * @return a string to me returned to google, formatted to be comprehensible when spoken  by google assistant
     */
    public String getAccelerateStatString(Request request) throws ParseException, IOException, InterruptedException {
        List<Record> records;
        Map<Commit,Build> leadTimeRecords;
        String out = "Stat not recognized";
        DecimalFormat df = new DecimalFormat("0.00");

        // requested stat is lead time
        if(request.getStatRequested().equalsIgnoreCase("Lead Time")){
            leadTimeRecords = smartDeviceInterface.getLeadTimeRecords(request);
            if (leadTimeRecords == null)
                out = "Team Does Not Exist.";
            else {
                long averageLeadTime = leadTime(leadTimeRecords);
                final long[] timeMS = {averageLeadTime};
                String hour = dateStringFormatter(timeMS);
                String minute = dateStringFormatter(timeMS);
                if(hour.equals(" 0 minutes"))
                    out = "No commit/build record pairs found for this team.";
                else
                    out = "Average Lead Time since " + request.getStartDate() + " is:" + hour + minute;
            }
        }

        // requested stat is mean time to restore
        if(request.getStatRequested().equalsIgnoreCase("mean time to restore")){
            records = smartDeviceInterface.getMTTRRecords(request);
            if(records == null)
                out = "Team Does Not Exist.";
            else if (records.isEmpty())
                out = "No Records Found for the Given Team/Project.";
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
                    // Add every Down record to temporary hash map with project name as key
                    // Next Restored record with the same project name is a match
                    // For each pair calculate time to restore and add to sum
                    if (temp.getStatus().equals("Down")) {
                        tempMap.put(temp.getProjectName(), temp);
                    } else if (temp.getStatus().equals("Restored") && tempMap.containsKey(temp.getProjectName())) {

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

        // requested stat is deployment frequency
        if(request.getStatRequested().equalsIgnoreCase("deployment frequency")){
            records = smartDeviceInterface.getDeploymentFrequencyRecords(request);
            if (records == null)
                out = "Team Does Not Exist.";
            else {
                int deploys = records.size();
                Date current = new Date(System.currentTimeMillis());
                final long[] timeMS = {current.getTime() - request.getStartDate().getTime()};
                long totalTimeMS = current.getTime() - request.getStartDate().getTime();
                // variables are named day and hour for readibility.
                // however it is also possible that the most applicable units of time are hours and minutes.
                String day = dateStringFormatter(timeMS);
                String hour = dateStringFormatter(timeMS);

                out = deploys + " deploy(s) " + " in" + day + hour + ".";
                if(day.contains("day"))
                    out += " Deployment Frequency is: " + df.format((float)deploys /(float) (totalTimeMS/(1000 * 60 * 60 * 24))) + " deploys per day";
                else if(day.contains("hour"))
                    out += " Deployment Frequency is: " + df.format((float)deploys / (float)(totalTimeMS/(1000 * 60 * 60))) + " deploys per hour";
                // if the desired period of time is less than one hour, calculating deployment frequency specifically is unnecessary.
                // in this case the value returned is simply the number of deployments and over however many minutes.
            }
        }

        // requested statistic is change/fail percentage
        if(request.getStatRequested().equalsIgnoreCase("Change Fail Percentage")){
            records = smartDeviceInterface.getChangeFailPercentageRecords(request);


            //calculate and set to out
            if (records == null)
                out = "Team Does Not Exist.";
            else if(((Double)changeFail(records)).isNaN())
                out = "No records found for this project or team.";
            else
                out = "Change Fail Percentage = " + df.format(changeFail(records));
        }

        // requested statistic is builds executed
        if(request.getStatRequested().equalsIgnoreCase("Builds Executed")){
            records = smartDeviceInterface.getBuildRecords(request);
            if (records == null)
                out = "Team Does Not Exist.";
            else {
                int deploys = records.size();
                out = deploys + " build(s) since " + request.getStartDate().toString() + ".";
            }
        }

        // requested statistic is total commits
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

    /**
     * intended for displaying stats on the web portal.
     * @param request google assistant request
     * @return a list of floats
     */
    public List<Float> getAccelerateStatList(Request request) throws ParseException, IOException, InterruptedException {
        List<Float> ints = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.getStartDate());
        Integer month = calendar.get(Calendar.MONTH);
        Integer year = calendar.get(Calendar.YEAR);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        DecimalFormat df = new DecimalFormat("0.00");

        if(request.getStatRequested().equals("Deployment Frequency") || request.getStatRequested().equals("Builds Executed") || request.getStatRequested().equals("Commits")) {
            for (int i = 0; i < DAYSINMONTHS[month]; i++) {
                request.setStartDate(String.format("%02d", month + 1) + " " + String.format("%02d", i + 1) + " " + year);
                request.setEndDateSame(String.format("%02d", month + 1) + " " + String.format("%02d", i + 1) + " " + year);
                if (request.getStatRequested().equals("Deployment Frequency"))
                    ints.add((float) smartDeviceInterface.getDeploymentFrequencyRecords(request).size());
                else if (request.getStatRequested().equals("Builds Executed"))
                    ints.add((float) (smartDeviceInterface.getBuildRecords(request)).size());
                else if (request.getStatRequested().equals("Commits"))
                    ints.add((float) smartDeviceInterface.getCommitRecords(request).size());

            }
        }

        else if(request.getStatRequested().equals("Mean Time To Restore") ||  request.getStatRequested().equals("Change Fail Percentage" ) || request.getStatRequested().equals("Lead Time")) {
            request.setStartDate(String.format("%02d",month+1) + " " + String.format("%02d",day) + " " + year);

            if ((DAYSINMONTHS[month] - day) <= 6) {
                day += DAYSINMONTHS[month] - day;
            }
            else if (request.getStartDate().getDay() != 0) {
                day += 6 - request.getStartDate().getDay();
            }
            else {
                day += 6;
            }

            request.setEndDateSame(String.format("%02d",month+1) + " " + String.format("%02d",day) + " " + year);
            if (request.getStatRequested().equals("Lead Time"))
                ints.add((float) (leadTime(smartDeviceInterface.getLeadTimeRecords(request))/(1000 * 60 * 60)));
            else if (request.getStatRequested().equals("Mean Time To Restore"))
                ints.add(mttr(smartDeviceInterface.getMTTRRecords(request)));
            else if (request.getStatRequested().equals("Change Fail Percentage")) {
                ints.add((float) changeFail(smartDeviceInterface.getChangeFailPercentageRecords(request)));
            }
        }
        else
            ints.add(null);

        return ints;
    }

    public WebhookResponse getAccelerateStatWebhook(Request request) throws ParseException, IOException, InterruptedException {
        String out = getAccelerateStatString(request);

        ResponseBuilder responseBuilder = new ResponseBuilder().add(out);
        ActionResponse response = responseBuilder.build();
        return response.getWebhookResponse();
    }

    /**
     * Statistic calculations helper functions
     */

    /**
     * calculates lead time
     * @param leadTimeRecords
     * @return long representing lead time of the records provided
     */
    private long leadTime(Map<Commit,Build> leadTimeRecords) throws ParseException {
        SimpleDateFormat buildSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        SimpleDateFormat commitSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
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
        return (long) (totalLeadTime / c);
    }

    /**
     * calculates mean time to restore
     * @param records list of records with which to perform calculations
     * @return float representing mean time to restore of the records provided
     */
    private float mttr(List<Record> records){
        // Match up records into pairs
        int pairCount = 0;
        Long sum = new Long(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        HashMap<String, Record> tempMap = new HashMap<>();
        Iterator<Record> recordIterator = records.iterator();
        Record temp;
        float mttr;

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
        if(pairCount == 0)
            mttr = 0;
        else
            mttr = sum/pairCount/60;
        return mttr;
    }

    /**
     * calculates change/fail percentage
     * @param records
     * @return double representing change/fail percentage of the records provided
     */
    private double changeFail(List<Record> records) {

        double successCommits = 0;
        double repeats = 0;
        double failed = 0;

        List<String> commitIDs = new ArrayList<>();
        Map<String, Integer> history = new HashMap<>();

        for (Record record : records) {
            commitIDs.add(record.getCommitID());
            if (record.getStatus().equals("SUCCESS")) {
                successCommits += 1;
            }
        }

        for (int x = 0; x < commitIDs.size(); x++) {
            if (!history.containsKey(commitIDs.get(x))) {
                history.put(commitIDs.get(x), 1);
            }
            else {
                repeats += 1;
                int first = commitIDs.indexOf(commitIDs.get(x));
                failed += (x - first - 1);
            }
        }
        return failed / (successCommits - repeats);
    }

    /**
     * for converting time in ms into a neatly formatted string. if used x times in a row on the same variable it
     * provides x or less subsequent units of time, ie. 3 hours 26 min
     * @param timeMS a list containing one long representing the time in ms. timeMS is replaced by a long representing
     * MS in next smallest unit of time, if applicable
     * @return formatted date string
     */
    private String dateStringFormatter(long[] timeMS){
        String out = "";
        // if largest complete unit is days, add that many days to string
        if(timeMS[0]/(1000 * 60 * 60 * 24) >= 1) {
            out = " " + out + timeMS[0] / (1000 * 60 * 60 * 24) + " day";
            if(timeMS[0]/(1000 * 60 * 60 * 24) > 1)
                out += "s";
            timeMS[0] = timeMS[0] % (1000 * 60 * 60 * 24);
        }
        // if the largest complete unit of time is hours, adds that many hours to the string
        else if(timeMS[0]/(1000 * 60 * 60) >= 1) {
            out = " " + out + timeMS[0] / (1000 * 60 * 60) + " hour";
            if(timeMS[0]/(1000 * 60 * 60) > 1)
                out += "s";
            timeMS[0] = timeMS[0] % (1000 * 60 * 60);
        }
        // if the largest complete unit of time is minutes, adds that many minutes to the string
        else if (timeMS[0]/(1000 * 60) >= 1) {
            out = " " + out + (int) (timeMS[0] / (1000 * 60)) + " minute";
            if(timeMS[0]/(1000 * 60) > 1)
                out += "s";
            timeMS[0] = -1; // negative value ensures that number of minutes wont be added to the output twice
        }
        // if smallest unit of time is < 1 min but > 0 MS,
        else if(timeMS[0]/(1000 * 60)<1 && timeMS[0]>=0){
            out = out + " 0 minutes";
            timeMS[0] = -1; // negative value ensures that number of minutes wont be added to the output twice
        }
        return out;
    }
}
package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.api.GoogleController;
import Ford.AccelerateMonitor.dataAccess.SmartDeviceInterface;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
import com.google.actions.api.DialogflowApp;
import com.google.api.services.dialogflow_fulfillment.v2.model.WebhookResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.response.ResponseBuilder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.StrictMath.round;

@Service
public class SmartDeviceService extends DialogflowApp {

    private final SmartDeviceInterface smartDeviceInterface;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(GoogleController.class);

    public Request extractValues(Request request, String body) throws JSONException, ParseException {
        JSONObject bodyJSON = new JSONObject(body);
        JSONObject queryResult = bodyJSON.getJSONObject("queryResult");
        JSONObject parameters = queryResult.getJSONObject("parameters");
        String statRequested = parameters.get("statRequested").toString();
        String targetTeam = parameters.get("targetTeam").toString();
        String targetProject = parameters.get("targetProject").toString();
        JSONObject datePeriod = parameters.getJSONObject("date-period");
        String startDateString = datePeriod.get("startDate").toString();
        request.setStatRequested(statRequested);
        request.setTargetTeam(targetTeam);
        request.setTargetProjects(targetProject);
        DateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = source.parse(startDateString);
        DateFormat dest = new SimpleDateFormat("MM dd yyyy");
        startDateString = dest.format(date);
        request.setStartDate(startDateString); //date: simple date object, pass in string mm dd yyyy
        return request;

    }

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }
//TODO review calculation methods
    public WebhookResponse getAccelerateStat(Request request) throws ParseException {
        List<Record> records;
        String out = "The mean time to restore is:";
        DecimalFormat df = new DecimalFormat("0.00");
        if(request.getStatRequested().equals("Lead Time")){
            records = smartDeviceInterface.getLeadTimeRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equals("MTTR")){
            records = smartDeviceInterface.getMTTRRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equals("deployment frequency")){
            records = smartDeviceInterface.getDeploymentFrequencyRecords(request);
            int deploys = records.size();
            Date current = new Date(System.currentTimeMillis());
            float days = (current.getTime() - request.getStartDate().getTime())/(1000*60*60*24);
            out = deploys +" deploy(s) " + " in " + days + " days. Deployment Frequency is: " + df.format(deploys/days) +" " + " deploys per day";
            System.out.println(out);
        }
        if(request.getStatRequested().equals("Change Fail Percentage")){
            records = smartDeviceInterface.getChangeFailPercentageRecords(request);
            //calculate and set to out
        }
        ResponseBuilder responseBuilder = new ResponseBuilder().add(out);
        ActionResponse response = responseBuilder.build();
        logger.info(response.toJson());
        return response.getWebhookResponse();
    }
}

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
        if(request.getStatRequested().equals("Builds Executed")){
            records = smartDeviceInterface.getBuildRecords(request);
            int deploys = records.size();
            out = deploys +" build(s) since " + request.getStartDate().toString() + ".";
        }
        return out;
        ResponseBuilder responseBuilder = new ResponseBuilder().add(out);
        ActionResponse response = responseBuilder.build();
        return response.getWebhookResponse();
    }
}

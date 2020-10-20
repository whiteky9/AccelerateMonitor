package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.api.GoogleController;
import Ford.AccelerateMonitor.dataAccess.SmartDeviceInterface;
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
import java.util.Date;
import java.util.List;


@Service
public class SmartDeviceService extends DialogflowApp {

    private final SmartDeviceInterface smartDeviceInterface;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(GoogleController.class);

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }
    //TODO review calculation methods

    public String getAccelerateStatString(Request request) throws ParseException {
        List<Record> records;
        String out = "Stat not recognized";
        DecimalFormat df = new DecimalFormat("0.00");
        if(request.getStatRequested().equalsIgnoreCase("Lead Time")){
            records = smartDeviceInterface.getLeadTimeRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equalsIgnoreCase("mean time to restore")){
            records = smartDeviceInterface.getMTTRRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equalsIgnoreCase("deployment frequency")){
            records = smartDeviceInterface.getDeploymentFrequencyRecords(request);
            if (records == null){
                out = "Team Does Not Exist.";
            }
            else {
                int deploys = records.size();
                Date current = new Date(System.currentTimeMillis());
                float days = (current.getTime() - request.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
                out = deploys + " deploy(s) " + " in " + (int) days + " days. Deployment Frequency is: " + df.format(deploys / days) + " " + " deploys per day";
                System.out.println(out);
            }
        }
        if(request.getStatRequested().equalsIgnoreCase("Change Fail Percentage")){
            records = smartDeviceInterface.getChangeFailPercentageRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equalsIgnoreCase("Builds Executed")){
            records = smartDeviceInterface.getBuildRecords(request);
            if (records == null){
                out = "Team Does Not Exist.";
            }
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

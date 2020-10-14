package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.SmartDeviceInterface;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class SmartDeviceService {

    private final SmartDeviceInterface smartDeviceInterface;

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }
//TODO review calculation methods
    public String getAccelerateStat(Request request) throws ParseException {
        List<Record> records;
        String out = "The mean time to restore is:";
        if(request.getStatRequested().equals("Lead Time")){
            records = smartDeviceInterface.getLeadTimeRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equals("MTTR")){
            records = smartDeviceInterface.getMTTRRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested().equals("Deployment Frequency")){
            records = smartDeviceInterface.getDeploymentFrequencyRecords(request);
            int deploys = records.size();
            Date current = new Date(System.currentTimeMillis());
            float days = (current.getTime() - request.getStartDate().getTime())/(1000*60*60*24);
            out = deploys +" deploy(s) " + " in " + days + " days. Deployment Frequency is: " + deploys/days +" " + " deploys per day";
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
    }
}

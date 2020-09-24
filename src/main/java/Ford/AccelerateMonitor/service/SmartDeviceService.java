package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.SmartDeviceInterface;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmartDeviceService {

    private final SmartDeviceInterface smartDeviceInterface;

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }

    public String getAccelerateStat(Request request){
        List<Record> records = new ArrayList<>();
        String out = "The mean time to restore is:";
        if(request.getStatRequested() == "Lead Time"){
            records = smartDeviceInterface.getLeadTimeRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested() == "MTTR"){
            records = smartDeviceInterface.getMTTRRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested() == "Deployment Frequency"){
            records = smartDeviceInterface.getDeploymentFrequencyRecords(request);
            //calculate and set to out
        }
        if(request.getStatRequested() == "Change Fail Percentage"){
            records = smartDeviceInterface.getChangeFailPercentageRecords(request);
            //calculate and set to out
        }
        return out;
    }
}

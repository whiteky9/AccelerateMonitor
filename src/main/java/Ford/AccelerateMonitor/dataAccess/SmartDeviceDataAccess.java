package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("SmartDeviceDataAccess")
public class SmartDeviceDataAccess implements SmartDeviceInterface{

    @Override
    public List<Record> getLeadTimeRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }

    @Override
    public List<Record> getMTTRRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }

    @Override
    public List<Record> getDeploymentFrequencyRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }

    @Override
    public List<Record> getChangeFailPercentageRecords(Request request){
        List<Record> records = new ArrayList<>();

        return records;
    }
    /*
    * get record querying
    *
    * implement DB reference/snapshot
    *
    * dataraf.addChildListener()
    * on ChildAdded{
    *
    * }
    *
    * */
}

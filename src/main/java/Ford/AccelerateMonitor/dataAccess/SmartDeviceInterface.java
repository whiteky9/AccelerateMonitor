package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.model.Commit;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

// Interface to allow easy implementation of additional data sources.
public interface SmartDeviceInterface {
    Map<Build,List<Commit>> getLeadTimeRecords(Request request);
    List<Record> getMTTRRecords(Request request);
    List<Record> getDeploymentFrequencyRecords(Request request) throws ParseException;
    List<Record> getChangeFailPercentageRecords(Request request);
    List<Record> getBuildRecords(Request request);
}

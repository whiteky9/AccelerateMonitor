package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;

import java.util.List;

// Interface to allow easy implementation of additional data sources.
public interface SmartDeviceInterface {
    List<Record> getLeadTimeRecords(Request request);
    List<Record> getMTTRRecords(Request request);
    List<Record> getDeploymentFrequencyRecords(Request request);
    List<Record> getChangeFailPercentageRecords(Request request);
}

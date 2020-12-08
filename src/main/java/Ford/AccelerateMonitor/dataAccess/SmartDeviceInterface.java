package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.model.Commit;
import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.model.Request;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

// Interface to allow easy implementation of additional data sources.
public interface SmartDeviceInterface {
    Map<Commit,Build> getLeadTimeRecords(Request request) throws InterruptedException, ParseException, IOException;
    List<Record> getMTTRRecords(Request request);
    List<Record> getDeploymentFrequencyRecords(Request request) throws ParseException;
    List<Record> getChangeFailPercentageRecords(Request request);
    List<Record> getBuildRecords(Request request);
    List<Record> getCommitRecords(Request request) throws InterruptedException, ParseException, IOException;
}

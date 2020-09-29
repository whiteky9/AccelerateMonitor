package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;
// Interface to allow easy implementation of additional data sources.
public interface JenkinsInterface {

    void addRecord(Record record);
}

package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;

import java.text.ParseException;

// Interface to allow easy implementation of additional data sources.
public interface RecordsInterface {

    void addRecord(Record record) throws ParseException;
}

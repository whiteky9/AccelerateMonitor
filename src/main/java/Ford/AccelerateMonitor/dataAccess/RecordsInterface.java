package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Record;

import java.text.ParseException;

/**
 * Interface to allow easy implementation of additional data sources.
 * in order to use a different database, create a corresponding data access class
 * which inheirits from this interface
 */
public interface RecordsInterface {

    void addRecord(Record record) throws ParseException;
    void deleteRecords(String name);
}

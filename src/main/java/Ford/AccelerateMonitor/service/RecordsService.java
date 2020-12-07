package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.dataAccess.RecordsInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class RecordsService {

    private final RecordsInterface recordsInterface;

    public RecordsService(@Qualifier("recordsDataAccess") RecordsInterface recordsInterface){ this.recordsInterface = recordsInterface; }

    public void addRecord(Record record) throws ParseException { recordsInterface.addRecord(record); }

    public void deleteRecords(String name) {recordsInterface.deleteRecords(name); }
}

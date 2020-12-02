package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/", "http://35.9.22.89:8888/", "http://35.9.22.63:8888/", "http://accmonitor.com"})
@RequestMapping("records")
@RestController
public class JenkinsController {

    private final RecordsService recordsService;

    @Autowired
    public JenkinsController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }

    @PostMapping("/addJenkinsRecord")
    public void addRecord(@RequestBody Build build) throws ParseException { recordsService.addRecord(build); }
}

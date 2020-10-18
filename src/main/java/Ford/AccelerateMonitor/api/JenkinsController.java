package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Record;
import Ford.AccelerateMonitor.service.JenkinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/"})
@RequestMapping("records")
@RestController
public class JenkinsController {

    private final JenkinsService jenkinsService;

    @Autowired
    public JenkinsController(JenkinsService jenkinsService) {
        this.jenkinsService = jenkinsService;
    }

    @PostMapping("/addRecord")
    public void addRecord(@RequestBody Record record){ jenkinsService.addRecord(record
    ); }
}

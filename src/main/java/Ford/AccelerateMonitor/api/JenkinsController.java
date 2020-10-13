package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("records")
@RestController
public class JenkinsController {

    private final RecordsService recordsService;

    @Autowired
    public JenkinsController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }

    @PostMapping("/addJenkinsRecord")
    public void addRecord(@RequestBody Build build){ recordsService.addRecord(build); }
}

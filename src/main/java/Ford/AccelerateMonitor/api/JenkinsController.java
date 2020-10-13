package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Build;
import Ford.AccelerateMonitor.service.JenkinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("records")
@RestController
public class JenkinsController {

    private final JenkinsService jenkinsService;

    @Autowired
    public JenkinsController(JenkinsService jenkinsService) {
        this.jenkinsService = jenkinsService;
    }

    @PostMapping("/addJenkinsRecord")
    public void addRecord(@RequestBody Build build){ jenkinsService.addRecord(build); }
}

package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.service.JenkinsService;
import Ford.AccelerateMonitor.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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

        //TODO
        //post rest putting information in the database
}

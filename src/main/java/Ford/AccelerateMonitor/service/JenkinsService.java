package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.JenkinsInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JenkinsService {

    private final JenkinsInterface jenkinsInterface;

    public JenkinsService(@Qualifier("jenkinsDataAccess") JenkinsInterface jenkinsInterface){ this.jenkinsInterface = jenkinsInterface; }

}

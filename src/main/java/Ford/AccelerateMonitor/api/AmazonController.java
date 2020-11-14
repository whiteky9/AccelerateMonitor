package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.service.SmartDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/", "http://35.9.22.89:8888/", "http://35.9.22.63:8888/", "http://accmonitor.com/"})
@RequestMapping("records")
@RestController
public class AmazonController {

    private final SmartDeviceService smartDeviceService;

    @Autowired
    public AmazonController(SmartDeviceService smartDeviceService) {
        this.smartDeviceService = smartDeviceService;
    }

    //getmapping
    //getdatafromfirebase
}

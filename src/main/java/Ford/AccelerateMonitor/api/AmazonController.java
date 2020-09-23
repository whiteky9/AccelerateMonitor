package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.service.SmartDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

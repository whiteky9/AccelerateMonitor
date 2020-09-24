package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Request;
import Ford.AccelerateMonitor.service.SmartDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("records")
@RestController
public class GoogleController {

    private final SmartDeviceService smartDeviceService;

    @Autowired
    public GoogleController(SmartDeviceService smartDeviceService) {
        this.smartDeviceService = smartDeviceService;
    }

    //needs to receive json of request. possibly make a post request? or get request with body?
    @GetMapping(path = "/accelerate")// needs to be fixed
    public String getAccelerateStat(@RequestBody Request request){ return smartDeviceService.getAccelerateStat(request); }

}

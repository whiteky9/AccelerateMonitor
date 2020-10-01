package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Request;
import Ford.AccelerateMonitor.service.SmartDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping("records")
@RestController
public class GoogleController {

    private final SmartDeviceService smartDeviceService;

    @Autowired
    public GoogleController(SmartDeviceService smartDeviceService) {
        this.smartDeviceService = smartDeviceService;
    }

    @GetMapping(path = "/accelerate")
    public String getAccelerateStat(@RequestBody Request request) throws ParseException { return smartDeviceService.getAccelerateStat(request); }

}

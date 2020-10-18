package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Request;
import Ford.AccelerateMonitor.service.SmartDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/"})
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

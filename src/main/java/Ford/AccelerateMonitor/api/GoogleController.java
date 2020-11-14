package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.model.Request;
import Ford.AccelerateMonitor.service.SmartDeviceService;
import com.google.api.services.dialogflow_fulfillment.v2.model.WebhookResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/", "http://35.9.22.89:8888/", "http://35.9.22.63:8888/", "http://accmonitor.com/"})
@RequestMapping("records")
@RestController
public class GoogleController {

    private final SmartDeviceService smartDeviceService;
    private Logger logger = LoggerFactory.getLogger(GoogleController.class);

    @Autowired
    public GoogleController(SmartDeviceService smartDeviceService) {
        this.smartDeviceService = smartDeviceService;
    }

    @PostMapping
    public WebhookResponse getAccelerateStatWebhook(HttpServletRequest http_request, HttpServletResponse response) throws IOException, JSONException, ParseException {
        String body = http_request.getReader().lines().collect(Collectors.joining());
        logger.info(new JSONObject(body).toString(4));
        Request request = new Request(body);
        return smartDeviceService.getAccelerateStatWebhook(request);
    }

    @PostMapping("/accelerate")
    public String getAccelerateRest(@RequestBody Request request) throws ParseException { return smartDeviceService.getAccelerateStatString(request); }

    @PostMapping("/display")
    public List<Float> getAccelerateDisplay(@RequestBody Request request) throws ParseException { return smartDeviceService.getAccelerateStatList(request); }
}

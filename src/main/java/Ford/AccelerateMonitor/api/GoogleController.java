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
import java.util.stream.Collectors;

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
    public WebhookResponse getAccelerateStat(HttpServletRequest http_request, HttpServletResponse response) throws IOException, JSONException, ParseException {
        String body = http_request.getReader().lines().collect(Collectors.joining());
        logger.info(new JSONObject(body).toString(4));
        Request request = new Request();
        smartDeviceService.extractValues(request, body);
        return smartDeviceService.getAccelerateStat(request);
    }

}

package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.dispatcher.NotificationDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("notify")
@RestController
public class BuildFailureController {

    private final NotificationDispatcher notificationDispatcher;

    @Autowired
    public BuildFailureController(NotificationDispatcher notificationDispatcher){ this.notificationDispatcher = notificationDispatcher; }

    @PostMapping("/Failure")
    public void sendBuildFailureNotification(@RequestBody Map<String, Object> requestBody){ notificationDispatcher.sendBuildFailureNotification(requestBody); }
}

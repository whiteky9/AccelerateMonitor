package Ford.AccelerateMonitor.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class NotificationDispatcher {

    @Autowired
    public NotificationDispatcher(){

    }

    public void sendBuildFailureNotification(Map<String,Object> message){
        //TODO
    }

}

package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.dataAccess.SmartDeviceInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SmartDeviceService {

    private final SmartDeviceInterface smartDeviceInterface;

    public SmartDeviceService(@Qualifier("SmartDeviceDataAccess") SmartDeviceInterface smartDeviceInterface){ this.smartDeviceInterface = smartDeviceInterface; }


}

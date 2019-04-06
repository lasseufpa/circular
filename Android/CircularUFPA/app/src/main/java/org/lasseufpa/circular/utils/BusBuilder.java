package org.lasseufpa.circular.utils;

import org.lasseufpa.circular.domain.Bus;
import org.osmdroid.util.GeoPoint;

public class BusBuilder {

    public Bus busBuilder(String message, String busName) throws IllegalAccessException {
        if (isMessageValid()){
            String messages[] = message.split(",");

            float signalQuality = Float.parseFloat(messages[0]);
            String dateTime = messages[1];
            String temperature = messages[2];
            GeoPoint latLog = new GeoPoint(Double.parseDouble(messages[3]), Double.parseDouble(messages[4]));
            double speed = Double.parseDouble(messages[5]);

            return new Bus(latLog, busName, speed, signalQuality, temperature, dateTime);

        } else throw new IllegalAccessException();
    }

    public Boolean isMessageValid(){
        // TODO: add verification logic
        return true;
    }


}

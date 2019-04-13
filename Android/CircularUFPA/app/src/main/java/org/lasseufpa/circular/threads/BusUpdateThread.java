package org.lasseufpa.circular.threads;

import android.util.Log;

import org.lasseufpa.circular.data.BusList;
import org.lasseufpa.circular.domain.Bus;
import org.osmdroid.util.GeoPoint;

import java.util.Arrays;

public class BusUpdateThread {

    public Boolean control = false;
    private Bus bus;

    public void MessageArrived(String message, String busName) {
        //  Message Format:
        // <Qualidade do sinal>,<Temperatura>,<UTC date & Time>,<Latitude>,<Longitude>,<Velocidade>,<Curso>

        String[] data = message.split(",");

        GeoPoint latLog = new GeoPoint(Double.parseDouble(data[3]), Double.parseDouble(data[4]));
        Bus bus = new Bus(latLog, busName);

        BusList busList = new BusList();
        busList.saveBus(bus);

        Log.d("MQTT", String.valueOf(busList.size()));
    }
}

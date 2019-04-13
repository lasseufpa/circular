package org.lasseufpa.circular.data;

import org.lasseufpa.circular.domain.Bus;

import java.util.ArrayList;
import java.util.Calendar;

public class BusList {

    private ArrayList<Bus> busList;
    private long timeToObsolete = 30000;
    private long timeToErase = 60000;
    private Calendar lastUpdate = Calendar.getInstance();


    public BusList(){
        busList = new ArrayList<>();
    }

    public void saveBus(Bus bus){
        lastUpdate = Calendar.getInstance();
        bus.setRecivedTime(Calendar.getInstance());

        for (Bus currentBus : busList){
            if (currentBus.getName().equals(bus.getName())){
                bus.setLatLog(bus.getLatLog());
                bus.setRecivedTime(bus.getRecivedTime());
                return;
            } else {
                busList.add(bus);
            }
        }
    }

    public ArrayList<Bus> getBusList() {
        return busList;
    }

    public int size(){
        return busList.size();
    }
}

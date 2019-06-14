package org.lasseufpa.circular.domain;

import org.osmdroid.util.GeoPoint;

public class Stop {

    private GeoPoint latLog;
    private String name;
    private String description;
    private int idStop;
    private boolean isBusHere;

    public Stop(int idStop, String name, String description, GeoPoint latLog){
        this.idStop = idStop;
        this.name = name;
        this.description = description;
        this.latLog = latLog;
    }

    public GeoPoint getLatLog() { return latLog; }
    public void setLatLog(GeoPoint latLog) { this.latLog = latLog; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getIdStop() { return idStop; }
    public void setIdStop(int idStop) { this.idStop = idStop; }

    public boolean isBusHere() { return isBusHere; }
    public void setBusHere(boolean busHere) { isBusHere = busHere; }
}

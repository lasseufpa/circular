package org.lasseufpa.circular.domain;

import org.osmdroid.api.Marker;
import org.osmdroid.util.GeoPoint;

import java.util.Calendar;

public class Bus {
    private GeoPoint latLog;
    private Marker marker;
    private String name;
    private Calendar recivedTime;

    private Route route;
    private double speed;
    private float signalQuality;
    private float temperature;
    private String dateTime;
    private Boolean isObsolet;
    private int direction;

    public Bus(GeoPoint latLog, String name) {
        this.latLog = latLog;
        this.name = name;
    }

    public void setLatLog(GeoPoint latLog){ this.latLog = latLog; }
    public GeoPoint getLatLog(){ return latLog; }

    public void setMarker(Marker marker){ this.marker = marker; }
    public Marker getMarket(){ return this.marker; }

    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }

    public void setRoute(Route route){ this.route = route; }
    public Route getRoute(){ return route; }

    public void setSpeed(double speed){ this.speed = speed; }
    public Double getSpeed(){ return speed; }

    public void setSignalQuality(float signalQuality){ this.signalQuality = signalQuality; }
    public Float getSignalQuality(){ return signalQuality; }

    public void setTemperature(float temperature){ this.temperature = temperature; }
    public Float getTemperature(){ return temperature; }

    public void setDateTime(String temperature){ this.dateTime = dateTime; }
    public String getDateTime(){ return dateTime; }

    public void setIsObsolet(Boolean isObsolet){ this.isObsolet = isObsolet; }
    public Boolean getIsObsolet(){ return isObsolet; }

    public void setDirection(int direction) { this.direction = direction; }
    public int getDirection() { return direction; }

    public void setRecivedTime(Calendar recivedTime) { this.recivedTime = recivedTime; }
    public Calendar getRecivedTime() { return recivedTime; }
}

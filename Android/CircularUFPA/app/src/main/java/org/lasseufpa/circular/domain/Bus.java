package org.lasseufpa.circular.domain;

import org.osmdroid.api.Marker;
import org.osmdroid.util.GeoPoint;

public class Bus {
    private GeoPoint latLog;
    private Marker marker;
    private String name;

    private Route route;
    private double speed;
    private float signalQuality;
    private float temperature;
    private String dateTime;
    private Boolean isObsolet;


    public Bus(GeoPoint latLog, String name, double speed, float signalQuality, String temperature, String dateTime){
        this.latLog = latLog;
        this.name = name;
    }

    public void setLatLog(double latitude){ this.latLog = latLog; }
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

}

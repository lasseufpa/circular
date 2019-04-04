package org.lasseufpa.circular.domain;

import org.osmdroid.api.Marker;

public class Bus {
    private double  latitude;
    private double  longitude;
    private Marker  marker;
    private String  name;

    private Route   route;
    private double  speed;
    private float   signalQuality;
    private float   temperature;
    private String  date;
    private String  time;
    private Boolean isObsolet;


    public Bus(double latitude, double longitude, Marker marker, String name){
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = marker;
        this.name = name;
    }

    public void setLatitude(double latitude){ this.latitude = latitude; }
    public Double getLatitude(){ return latitude; }

    public void setLongitude(double longitude){ this.longitude = longitude; }
    public Double getLongitude(){ return longitude; }

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

    public void setDate(String temperature){ this.date = date; }
    public String getDate(){ return date; }

    public void setTime(String time){ this.time = time; }
    public String getTime(){ return time; }

    public void setIsObsolet(Boolean isObsolet){ this.isObsolet = isObsolet; }
    public Boolean getIsObsolet(){ return isObsolet; }

}

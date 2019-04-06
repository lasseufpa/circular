package org.lasseufpa.circular.domain;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Route {

    private String name;
    private ArrayList<GeoPoint> routePoints;

    public Route(String name, ArrayList<GeoPoint> routePoints){
        this.name = name;
        this.routePoints = routePoints;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setRoutePoints(ArrayList<GeoPoint> routePoints) { this.routePoints = routePoints; }
    public ArrayList<GeoPoint> getRoutePoints() { return routePoints; }
}

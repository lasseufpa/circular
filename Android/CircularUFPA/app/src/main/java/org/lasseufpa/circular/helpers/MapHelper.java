package org.lasseufpa.circular.helpers;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.lasseufpa.circular.R;
import org.lasseufpa.circular.utils.UfpaStopList;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


public class MapHelper {

    public ArrayList<OverlayItem> addStopOverlay (String[][] stopsList, Drawable marker){
        ArrayList<OverlayItem> StopOverlayList = new ArrayList<OverlayItem>();

        for (int i=0; i<=stopsList.length - 1; i++){
            OverlayItem item = new OverlayItem(UfpaStopList.STOPS[i][2], UfpaStopList.STOPS[i][3], new GeoPoint(Double.parseDouble(UfpaStopList.STOPS[i][0]),Double.parseDouble(UfpaStopList.STOPS[i][1])));
            item.setMarker(marker);
            StopOverlayList.add(item);
        }

        return StopOverlayList;
    }

    public void addBusIcon (MapView map, Marker busMarker, GeoPoint latLog){
        busMarker.setPosition(latLog);
        map.getOverlays().add(busMarker);
    }

    public void removeBusIcon (MapView map, Marker busMarker){
        map.getOverlays().remove(busMarker);
    }
}
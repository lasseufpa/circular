package org.lasseufpa.circular;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.lasseufpa.circular.Domain.Circular;
import org.lasseufpa.circular.Domain.Parada;

import java.util.ArrayList;

public class CircularMapFragment extends Fragment implements OnMapReadyCallback,RepositorioCircular.RepositorioCircularChangeListener {

    //TASKS
    // - atualizar pontos de parada
    // - atualizar circulares
    // - traçar rota

    //Objeto Google Map
    private GoogleMap mMap;

    //mapview
    private MapView map;

    //repositório de circulares no mapa
    public static final RepositorioCircular repositorioCirculares = new RepositorioCircular();

    //lista de circulares
    ArrayList<Circular> circulares;
    //lista de maradoresCirculares
    ArrayList<Marker> circularesMarkers;

    Context contexto;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contexto = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circulares = repositorioCirculares.getCircularList();
        circularesMarkers = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        repositorioCirculares.setRepositorioCircularChangeListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();

        Log.i("circularMapFragment","Onresume chamado");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        map.onDestroy();
    }

    @Override
    public void onPause() {

        super.onPause();
        map.onPause();
        Log.i("circularMapFragment","Onspause chamado");

        //salav estado do mapa
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps,null);



        // obtém o SupportMapFragment e recebe uma notificação caso o mapa esteja pronto paras ser utilizado
        map = (MapView) view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);


        map.onResume();


        map.getMapAsync(this);

        Log.i("circularMapFragment","OncreateView chamado");


        return view;
    }



    @Override
    public void onStop() {
        super.onStop();
        repositorioCirculares.removeRepositorioCircularChangeListener(this);
        map.onStop();
        circularesMarkers.clear();
        Log.i("circularMapFragment","Onstop chamado");
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng place = new LatLng(-1.472465599146933,-48.45721595321921);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);


        //salvar estado do mapa

        //atualiza posição dos circulares no mapa
        updateCircularPosition();
        Log.i("circularMapFragment","OnmapReady chamado");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }






    /**
     * updateCircularPosition()
     * Atualiza os pontos do circular no mapa
     *
     */

    private void updateCircularPosition(){
        Log.i("circularMapFragment","UpdateCircularPosition chamado");

        ArrayList<Marker> ToRemoveList = new ArrayList<>();
        boolean found = false;

        //atualizando marcadores existentes
        for (Marker m : circularesMarkers) {

            for (Circular currentC : circulares) {

                if (m.getTitle().equals(currentC.getNome())) {
                    m.setPosition(currentC.getPosition());
                    found = true;
                    break;
                }
            }

            if (!found) {
                m.remove();
            } else {
                found = false;
            }
        }

        //adicionando novos
        for (Circular currentC : circulares) {

            for (Marker m : circularesMarkers) {

                if (m.getTitle().equals(currentC.getNome())) {
                    found = true;
                    break;
                }

            }

            if (!found) {
                addCircular(currentC);
            } else {
                found = false;
            }

        }

    }

    /* old
    private void updateCircularPosition(){

        Log.i("circularMapFragment","UpdateCircularPosition chamado");

        for (Marker m : circularesMarkers) {
            m.remove();
        }
        circularesMarkers.clear();
        //repete para cada circular na lista
        for (Circular currentC : circulares) {
            addCircular(currentC);
        }
    } */


    //redimenciona os icones do mapa para tamanhos personalizados
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private void addCircular(Circular C) {

        Log.i("circularMapFragment","addCircular chamado");
            //se o circular não tem marcador - cria um marcador
            Marker circular = mMap.addMarker(new MarkerOptions()
                    .position(C.getPosition())
                    .title(C.getNome())
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circ", 100, 100)))
            );
            circularesMarkers.add(circulares.indexOf(C),circular);
            //verifica se a informação é antiga para marcar cinza
            if (C.isObsolet()) {
                circular.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circg", 100, 100)));
            }
    }

    private void traceRoute(ArrayList<LatLng> pontos) {
        Log.i("rota","a rota foi adicionada ao mapa");
        mMap.addPolyline(new PolylineOptions().addAll(pontos).width(5).color(Color.rgb(255,153,153)));
    }

    private void setStops(ArrayList<Parada> pontos) {
        for (Parada ponto : pontos) {
            Marker pontoParada = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(ponto.getX(),ponto.getY()))
                    .title(ponto.getTitle())
                    .snippet(ponto.getDescription())
                    .anchor(0.5f,0.5f)
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("stoppoint", 50, 50))));
        }
    }

    private void UpdateStopPoints () {

    }


    @Override
    public void onRepositorioCircularChanged() {
        Log.i("circularMapFragment","OrepositorioCircularChanged chamado");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

               updateCircularPosition();

            }
        });
    }

    public void circularMArkerFocus(final String CircularName, Activity context) {

    }
}

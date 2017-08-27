package org.lasseufpa.circular;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import org.lasseufpa.circular.Domain.StopPoint;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        repositorioCirculares.setRepositorioCircularChangeListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // mapUpdateService = new CircularPositionUpdater(getActivity().getApplicationContext(),maphandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        map.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
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



        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        repositorioCirculares.removeRepositorioCircularChangeListener(this);
        map.onStop();
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

        //repete para cada circular na lista
        for (Circular currentC : circulares) {
            addCircular(currentC);
        }
    }


    //redimenciona os icones do mapa para tamanhos personalizados
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private void addCircular(Circular C) {
        if (C.getMarcador() == null) {
            //se o circular não tem marcador - cria um marcador
            Marker circular = mMap.addMarker(new MarkerOptions()
                    .position(C.getPosition())
                    .title(C.getNome())
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circ", 100, 100)))
            );
            C.setMarcador(circular);
        } else {
            //se o circular já tem marcador atualizar posição
            C.getMarcador().setPosition(C.getPosition());
        }

        //verifica se a informação é antiga para apagar
        if (C.isErase()) {
            //remove marcador do mapa
            C.getMarcador().remove();
            //remove da lista
            //CircularPositionUpdater.repositorioCirculares.getCircularList()
                  //  .remove(CircularPositionUpdater.repositorioCirculares.getCircularcopyList().indexOf(C));
        } else {

            //verifica se a informação é antiga para marcar cinza
            if (C.isObsolet()) {
                C.getMarcador().setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circg", 100, 100)));

            }
            //verifica se a informação foi renovada
            if (!C.isObsolet()) {
                C.getMarcador().setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circ", 100, 100)));


            }
        }
    }

    private void traceRoute(ArrayList<LatLng> pontos) {
        Log.i("rota","a rota foi adicionada ao mapa");
        mMap.addPolyline(new PolylineOptions().addAll(pontos).width(5).color(Color.rgb(255,153,153)));
    }

    private void setStops(ArrayList<StopPoint> pontos) {
        for (StopPoint ponto : pontos) {
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

               updateCircularPosition();

            }
        });
    }
}

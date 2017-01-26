package org.lasseufpa.circular;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Objeto Google Map
    private GoogleMap mMap;

    //Objetos Textview para atualização de status do mapa
    private TextView status;
    private TextView viewMessage;

    //variável do serviço de atualização do mapa
    private MapUpdateService mapUpdateService;


    //handler para capturar mensagens de MapUpdateService
    private Handler maphandler = new MapHandler();

    //variaveis de mensagens
    public static final int MESSAGE_LOG             = 1;
    public static final int UPDATE_CIRCULAR         = 2;
    public static final int UPDATE_STOPPOINT        = 3;
    public static final int CONECTIVITY_STATEMENT   = 4;
    public static final int ERR_CONECTION_TIMEOUT   = 5;
    public static final int TRACE_ROUTE             = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        status = (TextView) findViewById(R.id.status);
        viewMessage = (TextView) findViewById(R.id.message);

        mapUpdateService = new MapUpdateService(getApplicationContext(),maphandler);


        // obtém o SupportMapFragment e recebe uma notificação caso o mapa esteja pronto paras ser utilizado
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        pauseMapService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ReloadMapService();
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

        mapUpdateService.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void pauseMapService() {
       mapUpdateService.pause();

    }

    private void stopService() {
        mapUpdateService.stop();
    }

    private void ReloadMapService() {
        mapUpdateService.start();

    }


    private void updateConectivity (boolean connectivity) {

        if (connectivity) {
            status.setText("Conectado");
            status.setTextColor(Color.GREEN);
        } else {
            status.setText("Desconectado");
            status.setTextColor(Color.RED);
        }
    }


    /**
     * updateCircularPosition()
     * Atualiza os pontos do circular no mapa
     *
     */
    private void updateCircularPosition(){
        //atualiza a lista de circulares
        MapUpdateService.repositorioCirculares.UpdateCircularList();

        //captura a lista de circulares do repositório
        ArrayList<Circular> circulares =  MapUpdateService.repositorioCirculares.getCircularcopyList();

        //repete para cada circular na lista
        for (Circular currentC : circulares) {
            addCircular(currentC);
        }



    }


    //redimenciona os icones do mapa para tamanhos personalizados
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
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
            MapUpdateService.repositorioCirculares.getCircularList()
                    .remove(MapUpdateService.repositorioCirculares.getCircularcopyList().indexOf(C));
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


    //classe anonima para capturar mensagens
    private class MapHandler extends Handler {



        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == MESSAGE_LOG) {
                //mensagem de log
                viewMessage.setText(msg.getData().getString("message"));
            }

            if (msg.what == UPDATE_CIRCULAR) { // 1 - posiciona um circular no mapa
               updateCircularPosition();
            }

            if (msg.what == UPDATE_STOPPOINT) { //2 - posiciona ponto de parada no mapa

            }

            if (msg.what == CONECTIVITY_STATEMENT) {
                boolean con_status = msg.getData().getBoolean("conectivity");
                updateConectivity(con_status);
            }

            if (msg.what == ERR_CONECTION_TIMEOUT) {

            }

            if (msg.what == TRACE_ROUTE) {

            }



        }
    }



}

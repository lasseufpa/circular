/*

package org.lasseufpa.circular;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.lasseufpa.circular.Domain.RouteCircular1;
import org.lasseufpa.circular.Domain.StopPoint;
import org.lasseufpa.circular.Domain.StopPoints;

import java.util.ArrayList;
import java.util.Calendar;



public class CircularPositionUpdater implements Runnable {


    private final int timeUpdate = 100;            //tempo de atualização em milisegundos
    private final int timeOutConnection = 30000;   //tempo de fim de espera de conexão em milisegundos
    private boolean serviceOn = false;             //flag de inicialização do serviço
    private Calendar lastUpdate;                   //ultima atualização

    private Handler mapHandler;
    private MqttHandler mqttHandler;

    //repositório de circulares no mapa
    public static final RepositorioCircular repositorioCirculares = new RepositorioCircular();

    //objeto de comunicação MQTT
    private  MqttConnect mqttconnect;
    private Context contexto; //contexto



    public CircularPositionUpdater(Context context, Handler mapHandlergot) {
        contexto = context;
        mapHandler = mapHandlergot;
        mqttHandler = new MqttHandler();
        mqttconnect = new MqttConnect(context,mqttHandler);
        lastUpdate = Calendar.getInstance();
    }


    /**
     * Iniciar o processo de atualização do mapa

    public void start() {
        serviceOn = true;
        Thread t = new Thread(this);
        t.start();
        Log.i("mensg","thread CircularPositionUpdater Criado e inciado");
    }

    public void pause() {
        serviceOn = false;
    }
    /**
     * Parar o processo de atualização do mapa

    public void stop() {
        mqttconnect.Disconnect();
        serviceOn = false;
    }


    /**
     * Iniciar o processo de comunicação com o broker MQTT

    private void startMqttConnect() {
     mqttconnect = new MqttConnect(contexto,mqttHandler);
        Thread t = new Thread() {
            public void run() {
                mqttconnect.doConnect();
            }
        };

        t.start();
    }







    private boolean isUpdated() {
        //pergunta para o repositorio se o mapa está atualizado
        return repositorioCirculares.isUpdated(lastUpdate);
    }





    /**
     * Traça a rota do circular no mapa

    private void traceRoute () {

        ArrayList<LatLng> pontos= new ArrayList<>();

        for (int i=0; i<RouteCircular1.NROUTEPOINTS; i++) {
            pontos.add(new LatLng(RouteCircular1.POINTS[i][1], RouteCircular1.POINTS[i][0]));
        }
        pontos.add(new LatLng(RouteCircular1.POINTS[0][1], RouteCircular1.POINTS[0][0]));

        sendRoute(pontos);

     //   Polyline line = mMap.addPolyline(new PolylineOptions().addAll(pontos).width(5).color(Color.rgb(255,153,153)));

    }

    private void sendRoute(ArrayList<LatLng> pontos) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putSerializable("pontos",pontos);
        msg.setData(b);
        msg.what = CircularMapFragment.TRACE_ROUTE;
        mapHandler.sendMessage(msg);
    }

    private void sendStopPoints(ArrayList<StopPoint> pontos) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putSerializable("pontos",pontos);
        msg.setData(b);
        msg.what = CircularMapFragment.UPDATE_STOPPOINT;
        mapHandler.sendMessage(msg);

    }



    /**
     * Processo de execução principal, atualização do mapa de localização de circulares


    public run() {

        int sumTime = 0; //variavel para armazenar a soma de tempo

        //processamentos necessários ao inciar activity
        if (!isMqttConnected())  startMqttConnect(); //iniciar conexão mqtt


       // traceRoute();   //traçar rota do circular

        setStopPoints();

        //enquanto o serviço estiver ligado
        while (serviceOn) {

            //se há conexão com o broker
            if (isMqttConnected()){
                sumTime=0;                                  //zerar o contador de tempo sem conexão
                repositorioCirculares.UpdateCircularList(); //atualizar a lista de circulares
                                                            //se o repositorio não está atualizado com o mapa
             if (!isUpdated()) {
                 updateMap();                               //atualiza o mapa
             }

            } else {                              //se não está conectado
                sumTime = sumTime + timeUpdate;   //realiza a soma do tempo aproximado
                if (sumTime>timeOutConnection) {  //se o tempo passado passar do limite especificado
                    timeOutConnection();          //realizar operações de tempo encerrado de conexão
                    stop();                       //finalizar atividade
                }
            }


            try {
                Thread.sleep(timeUpdate);        //esperar 100 milisegundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    //transferir para uma classe especifica
    private void setStopPoints() {
        ArrayList<StopPoint> pontos = new ArrayList<>();

        for (int i = 0; i<StopPoints.N_STOP_POINTS; i++) {
            StopPoint stop = new StopPoint();
            stop.setX(StopPoints.POINTS[i][0]);
            stop.setY(StopPoints.POINTS[i][1]);
            stop.setTitle(StopPoints.NAME_DESCRIPTION[i][0]);
            stop.setDescription(StopPoints.NAME_DESCRIPTION[i][1]);
            pontos.add(stop);
        }

        sendStopPoints(pontos);
    }



    public void publishMessage(String message) {
        Message mensagem = new Message();
        mensagem.what = CircularMapFragment.MESSAGE_LOG;
        Bundle b = new Bundle();
        b.putString("message",message);
        mensagem.setData(b);
        mapHandler.sendMessage(mensagem);
    }



    //classe anonima para capturar mensagens
    private class MqttHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 11) {
                publishMessage(msg.getData().getString("message"));
            }




        }
    }


}
*/
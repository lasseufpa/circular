package org.lasseufpa.circular;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.lasseufpa.circular.Domain.Circular;

import java.util.Calendar;

/**
 *
 *    ALPHAMAGE APPS TECHNOLOGY
 *    Desenvolvimento de aplicações com qualidade
 *
 */

public class CircularUpdateService extends Service implements Runnable,MqttCallbackExtended,IMqttActionListener {

    private boolean serviceOn = false;

    //recuperando instancia do repositorio
    private RepositorioCircular repositorio = CircularMapFragment.repositorioCirculares;

    //objeto de conexão MQTT
    private  MQTTconect mqttconnect;

    //criador de circulares interpretando suas mensagens
    private CircularBuilderGSM ciruclarBuilder;
    //Ultima atualização
    private Calendar lastUpdate = Calendar.getInstance();
    //tempo de atualização em milisegundos
    private final int timeUpdate = 500;

    PendingIntent p;


    /**
     * Executado na criação do objeto CircularUpdateService
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mqttconnect = new MQTTconect(getApplicationContext(),this);
        ciruclarBuilder = new CircularBuilderGSM();
        p = PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(),MainActivity.class),0);


    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        UpdateNotification("Serviço iniciado");
        startMqttConnect();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void run() {
        int i=0;
        String c = "-____";
        //enquanto o serviço estiver ligado
        while (serviceOn) {
            //se houver conexão com o mqttServer
            if (isMqttConnected()) {

                try {
                    repositorio.UpdateCircularList();
                } catch (Exception e) {
                    e.printStackTrace();
                    stopSelf();
                }



                //UpdateNotification(c);
                i++;
                switch (i) {
                    case 1: c="-____"; break;
                    case 2: c="_-___"; break;
                    case 3: c="__-__"; break;
                    case 4: c="___-_"; break;
                    case 5: c="____-"; break;
                    case 6: i=0; break;
                }


                UpdateNotification("Conectado ao Servidor " + c);

                try {
                    Thread.sleep(timeUpdate);        //esperar 100 milisegundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } else {
                serviceOn = false;
            }
        }
    }

    private boolean isUpdated() {
        //pergunta para o repositorio se o mapa está atualizado
        return repositorio.isUpdated(lastUpdate);
    }

    public void stop() {
        mqttconnect.Disconnect();
        serviceOn = false;
    }

    private void tryToReconnect() {
        UpdateNotification("Reiniciando conexão com o servidor");
        try {
            mqttconnect.doConnect(this);
        } catch (Throwable throwable) {
            UpdateNotification("Falha na conexão");
            throwable.printStackTrace();
        }
    }

    /**
     * Iniciar o processo de comunicação com o broker MQTT
     */
    private void startMqttConnect() {
        UpdateNotification("Iniciando conexão com o servidor");
        try {
            mqttconnect.doConnect(this);
        } catch (Throwable throwable) {
            UpdateNotification("Falha na conexão");
            throwable.printStackTrace();
        }
    }

    private boolean isMqttConnected() {
        return mqttconnect.isconnected();
    }

    @Override
    public void onDestroy() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //conexão com o MQTT perdida
    @Override
    public void connectionLost(Throwable cause) {
        UpdateNotification("Conexão perdida");
;   }

    //mensagem recebida do broker
    @Override
    public void messageArrived(String topic, MqttMessage message) {

        String topicos[] = topic.split("/");

        if (topicos.length==5 ) {

            String info = topicos[3];
            String nomeCircular = topicos[4];

            switch (info) {
                case "loc" :
                    Circular c = ciruclarBuilder.CircularBuild(message.toString(),nomeCircular);
                    repositorio.saveCircular(c);
                    //UpdateNotification("Localização de " + nomeCircular + " recebida");
                    break;
                default:
                    //UpdateNotification("mensagem não reconhecida");
                    break;
            }
        } else {
            //UpdateNotification("mensagem não reconhecida");
        }

    }



    //Entrega completa
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    //conexão completa
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        UpdateNotification("Conectado ao Servidor");
        serviceOn = true;
        new Thread(this).start();

    }


    private void UpdateNotification (String messageGet) {

        int mId = 1;
        String message[] = {"",""};
        message[0] = messageGet;
        message[1] = repositorio.activeCircularesNumber() + " Circulares ativos";



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_informacoes)
                        .setContentTitle("CircularGPS service - Ativado")
                        .setContentText(messageGet).setOngoing(true)
                        ;

        if (isMqttConnected()) {

            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            style.setBigContentTitle("CircularGPS service - Ativado");
            for (String line : message) {
                style.addLine(line);
            }
            mBuilder.setStyle(style);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

    }

    /**
     * sucesso na conexão: sobreescrever
     * @param asyncActionToken
     */
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        mqttconnect.subscribeToTopic();
    }

    /**
     * falha na conexão com o servidor MQTT
     * @param asyncActionToken
     * @param exception
     */
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        UpdateNotification("Falha na conexão com o servidor");

    }


}




 package org.lasseufpa.circular;

/**
 * Created by alberto on 18/11/2016.
 */


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;   //Classe cliente MQTT android
import org.eclipse.paho.client.mqttv3.IMqttActionListener;   //Classe para Escuta de eventos de conexão MQTT
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttException;



public class MqttConnect  {


    private MqttAndroidClient mqttAndroidClient;         //objeto Cliente MQTT do Android
    private String serverAndress = "tcp://192.168.1.79";  //variavel para armazenar o endereço do servidor
    private String clientID;                             //armazena o identificador do cliente MQTT
    private String username = "alberto";                 //armazena o nome de usuário
    private String password = "null";                    //armazena a senha de conexão do broker
    private String publishTopic = "CircularUFPA";       //Tópico para o cliente subescrever no servidor
    private CircularBuilder circularBuilder= new CircularBuilder();
    private Context contexto;
    private Handler handler;


    public MqttConnect(Context contexto, Handler handler) {

        this.contexto = contexto;
        this.handler = handler;
    }

    public boolean isconnected () {
        return mqttAndroidClient.isConnected();
    }

    public void doConnect() {

        publishMessage("Tentando estabelecer conexão com o servidor");
        clientID = MqttClient.generateClientId();    //gera um código randomico que serve como identificação do cliente

        try {
            //cria um objeto MQTTClient android entregando como parametro o endereço o servidor e o id do cliente
            mqttAndroidClient = new MqttAndroidClient(contexto, serverAndress, clientID);
            //configura um objeto CallBack (objeto de chamada caso haja alteração)
            mqttAndroidClient.setCallback(new MqttCallback() {

                //@Override
              //  public void connectComplete(boolean reconnect, String serverURI) {
              //       publishMessage("conexão completa");
              //  }

                //chamado quando há perda da conexão
                @Override
                public void connectionLost(Throwable cause) {
                    publishMessage("Conexão com o servidor perdida");

                }

                //chamado quando chega uma mensagem
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    checkMessage(message.toString());

                }



                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }


            });

            IMqttToken token = mqttAndroidClient.connect(new MqttConnectOptions());            //realiza a conexão com o broker
            token.setActionCallback(new IMqttActionListener() {

                //chamado quando a conexão é estabelecida
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    publishMessage("conectado com sucesso ao servidor");
                    subscribeToTopic();
                }

                //chamado quando há falha na conexão
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    publishMessage("falha na conexão");
                }
            });


        } catch (MqttException e) {
            publishMessage("ERRO");

        }




    }



    private void subscribeToTopic() {
        publishMessage("subescrevendo");
        if (mqttAndroidClient.isConnected()) {
            try {
                IMqttToken subToken = mqttAndroidClient.subscribe(publishTopic, 1);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // A mensagem foi publicada com sucesso
                        publishMessage("Conexão establecida");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // The subscription could not be performed, maybe the user was not
                        // authorized to subscribe on the specified topic e.g. using wildcards
                        publishMessage("Falha na conexão");

                    }
                });

            } catch (MqttException e) {
                e.printStackTrace();
            }

        } else {
            publishMessage("Erro na conexão com o servidor");
        }
    }




    //mensagem recebida do proker, checa a mensagem
    private void checkMessage(String s) {

        Circular c;
        try {
            c = circularBuilder.CircularBuild(s);
            MapsActivity.repositorioCirculares.salvarCircular(c);
            publishMessage(c.getNome()+"salvo");
        } catch (IllegalArgumentException e) {
            publishMessage("Mensagem de Localização Inválida");
        }


    }



    public void publishMessage(final String message) {

        Bundle b = new Bundle();

        b.putString("message",message);
        Message msg = new Message();
        msg.what = 1;
        msg.setData(b);
        handler.sendMessage(msg);


    }




    //getters and setters methods

    public void setServerAndress(String adress) {
        this.serverAndress = adress;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerAndress() {
        return serverAndress;
    }

    public String getClientID() {
        return clientID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPublishTopic() {
        return publishTopic;
    }

    public void setPublishTopic(String publishTopic) {
        this.publishTopic = publishTopic;
    }


}

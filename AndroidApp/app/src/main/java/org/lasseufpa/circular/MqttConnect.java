package org.lasseufpa.circular;

/**
 * Created by alberto on 18/11/2016.
 */


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.eclipse.paho.android.service.MqttAndroidClient;   //Classe cliente MQTT android
import org.eclipse.paho.client.mqttv3.IMqttActionListener;   //Classe para Escuta de eventos de conexão MQTT
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.lasseufpa.circular.Domain.Circular;


public class MqttConnect  {


    private MqttAndroidClient mqttAndroidClient;                //objeto Cliente MQTT do Android
    private String serverAndress = "tcp://iot.eclipse.org";     //variavel para armazenar o endereço do servidor
    private String clientID;                                    //armazena o identificador do cliente MQTT
    private String username = "alberto";                        //armazena o nome de usuário
    private String password = "null";                           //armazena a senha de conexão do broker
    private String publishTopic = "/ufpa/circular/loc/";           //Tópico para o cliente subescrever no servidor
    private CircularBuilderGSM circularBuilder = new CircularBuilderGSM(); //Objeto que tranforma uma mensagem String em um objeto circular
    private Context contexto;
    private Handler handler;


    public MqttConnect(Context contexto, Handler hand) {

        this.contexto = contexto;
        this.handler = hand;
    }

    public boolean isconnected () {
        boolean retorno;
        try {
            retorno = mqttAndroidClient.isConnected();
        }
        catch (Exception e) {
            e.printStackTrace();
            retorno = false;
        }
        return retorno;
    }

    public void doConnect() {

        publishMessage("Tentando estabelecer conexão com o servidor");
        clientID = MqttClient.generateClientId()+"circularUFPAapp";    //gera um código randomico que serve como identificação do cliente

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

            IMqttToken token = mqttAndroidClient.connect();            //realiza a conexão com o broker
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
                    exception.printStackTrace();
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
                        publishMessage("Falha na conexão ao subescrever");

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
            CircularPositionUpdater.repositorioCirculares.saveCircular(c);
            publishMessage("localização de "+c.getNome()+" Recebida");
        } catch (IllegalArgumentException e) {
            publishMessage("Mensagem de Localização Inválida");
            e.printStackTrace();
        }


    }

    public void Disconnect() {
        if (isconnected()) {
            try {
                mqttAndroidClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }



    private void publishMessage(final String message) {

        Bundle b = new Bundle();
        b.putString("message",message);
        Message msg = new Message();
        msg.what = 11;
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
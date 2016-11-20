 package org.lasseufpa.circular;

/**
 * Created by alberto on 18/11/2016.
 */

/*
import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;   //Classe cliente MQTT android
import org.eclipse.paho.client.mqttv3.IMqttActionListener;   //Classe para Escuta de eventos de conexão MQTT
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttException;



public class MqttConnect implements Runnable {


    private MqttAndroidClient mqttAndroidClient;         //objeto Cliente MQTT do Android
    private String serverAndress = "mqtt://localhost/";  //variavel para armazenar o endereço do servidor
    private String clientID;                             //armazena o identificador do cliente MQTT
    private String username = "alberto";                 //armazena o nome de usuário
    private String password = "null";
    private String publishTopic = "mqttmessenger";       //Tópico para o cliente subescrever no servidor


    public MqttConnect(String serverAdress, String clientID) {

    }

    public void doConnect(Context contexto) {

        clientID = MqttClient.generateClientId();               //gera um código randomico que serve como identificação do cliente

        try {
            //cria um objeto MQTTClient android entregando como parametro o endereço o servidor e o id do cliente
            mqttAndroidClient = new MqttAndroidClient(contexto, serverAndress, clientID);
            //configura um objeto CallBack (objeto de chamada caso haja alteração)
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {

                }

                //chamado quando há perda da conexão
                @Override
                public void connectionLost(Throwable cause) {
                    publishMessage("Desconectado!");

                }

                //chamado quando chega uma mensagem
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    checkMessage(message.getPayload().toString());
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
                    publishMessage("conectado ao servidor");
                    subscribeToTopic();
                }

                //chamado quando há falha na conexão
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    publishMessage("falha na conexão");
                }
            });


        } catch (MqttException e) {

        }




    }



    private void subscribeToTopic() {
        if (mqttAndroidClient.isConnected()) {
            try {
                IMqttToken subToken = mqttAndroidClient.subscribe(publishTopic, 1);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // A mensagem foi publicada com sucesso
                        publishMessage("Subscrito com sucesso");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // The subscription could not be performed, maybe the user was not
                        // authorized to subscribe on the specified topic e.g. using wildcards
                        publishMessage("Falha ao subescrever ao topico");

                    }
                });

            } catch (MqttException e) {
                e.printStackTrace();
            }

        } else {
            publishMessage("Não da pra entrar em uma conversa, o servidor não está conectado");
        }
    }





    private void checkMessage(String s) {
    }

    //faz o onibus andar uma casa
    public void GoTheBus() {


    }

    public void publishMessage(String Message) {


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

    @Override
    public void run() {

    }
}
*/
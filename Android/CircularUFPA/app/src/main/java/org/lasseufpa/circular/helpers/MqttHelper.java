package org.lasseufpa.circular.helpers;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHelper implements MqttCallback {

    private MqttClient mqttClient;                             // Objeto cliente MQTT
    private String SERVER = "tcp://iot.eclipse.org";           // Endereço do servidor MQTT (broker)
    private String CLIENT_ID;                                  // Identificador do cliente MQTT
    private String USERNAME = "circularUFPA";                  // Nome do usuário
    private String PASSWORD = "null";                          // Senha
    private String SUBSCRIBE_TOPICS = "/ufpa/circular/#";      // Tópicos para subscrever

    public MqttHelper(Context context) {
        CLIENT_ID = MqttClient.generateClientId() + "androidapp";

        try {
            mqttClient = new MqttClient(SERVER, CLIENT_ID, new MemoryPersistence());
            mqttClient.setCallback(this);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(USERNAME);
        mqttConnectOptions.setPassword(PASSWORD.toCharArray());

        try {

            mqttClient.connect(mqttConnectOptions);
            subscribeToTopic();

        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    private void subscribeToTopic() {
        try {
            mqttClient.subscribe(SUBSCRIBE_TOPICS, 0);

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.w("MQTT", message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public Boolean isConnected() { return mqttClient.isConnected();}
}

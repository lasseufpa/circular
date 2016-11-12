package com.example.alberto.mqttmessenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.eclipse.paho.android.service.MqttAndroidClient;   //Classe cliente MQTT android
import org.eclipse.paho.client.mqttv3.IMqttActionListener;   //Classe para Escuta de eventos de conexão MQTT
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    /*
                   VARIÁVEIS DO CLIENTE MQTT
     */

    private MqttAndroidClient mqttAndroidClient;         //objeto Cliente MQTT do Android
    private String serverAndress = "mqtt://localhost/";  //variavel para armazenar o endereço do servidor
    private String clientID;                             //armazena o identificador do cliente MQTT
    private String username = "alberto";                 //armazena o nome de usuário
    private String publishTopic = "mqttmessenger";       //Tópico para o cliente subescrever no servidor

    /*
                   VARIÁVEIS DO LAYOUT
     */

    ArrayAdapter<String> adapterStr;                     //adaptador String para a ListView do layout
    EditText serverUri;                                  //EditText para receber o endereço do servidor Broker MQTT
    EditText mensagem;                                   //EditText para receber a mensagem
    EditText userNameEdit;                               //EditText para receber o nome de usuário
    Button connectButton;                                //Botão para conectar ao servidor
    Button sendButton;                                   //Botão para enviar mensagens
    ListView mensagensLista;                             //List View para exibir as mensagens na tela


    /*
    MÉTODO ONCREATE: É CHAMADO NO INICIO DE UMA ACTIVITY E QUANDO HÁ MUDANÇAS DE CONTEXTO
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);               //chamando o método da superclasse
        setContentView(R.layout.activity_main);           //definindo o arquivo XML que define a interface gráfica da activity

        //recupera objetos View da interface com o método findViewById()
        //atraves do id de cada componente
        serverUri = (EditText) findViewById(R.id.serverUri);
        connectButton = (Button) findViewById(R.id.conectbutton);
        mensagem = (EditText) findViewById(R.id.EditMensagem);
        sendButton = (Button) findViewById(R.id.EnviarMensagemButton);
        mensagensLista = (ListView) findViewById(R.id.listaMensagens);
        userNameEdit = (EditText) findViewById(R.id.nomeUsuario);

        //capturando eventos do botão de conectar
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {              //quando o botão for clicado, este método é executado automaticamente
                connect();                                //inicia o processo de conexão com o servidor MQTT
            }
        });

        //recuperando o nome do usuário digitado no editext username como uma String
        username = userNameEdit.getText().toString();

        //recupera a lista de strings salva
        adapterStr = new ArrayAdapter<String>(                                           //cria um listAdapter do tipo String para ser adicionado ao ListView
                this,android.R.layout.simple_list_item_1);
        if (savedInstanceState!=null) {                                                  //se tiver algum objeto salvo na memoria
            ArrayList<String> list = savedInstanceState.getStringArrayList("lista");     //recupera lista de strings salva
            username = savedInstanceState.getString("username");                         //recupera o nome de usuário salvo

            //laço para adicionar todos os elementos da lista de Strings ao Adaptador de Strings criado
            for (int i = 0; i < list.size(); i++)
                adapterStr.add(list.get(i));

        }

        //Definindo o Adapter da ListView como o Adapter do tipo String
        mensagensLista.setAdapter(adapterStr);
        //capturar eventos do botão de enviar  mensagem
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {            //quando o botão for clicado, este método é executado automaticamente
                publishMessage();                    //Inicia o processo de publicar uma mensagem no Broker
            }
        });

       if (mqttAndroidClient!=null) {                //se o cliente MQTT já foi instanciado
           if (!mqttAndroidClient.isConnected()) connect(); //conecta se não estiver conectado
       } else connect();                             //se não tiver sido instanciado conecta

    }

    /*
    MÉTODO ONSAVEINSTANCESTATE: É CHAMANDO QUANDO HÁ UMA MUDANÇA DE CONTEXTO, A ACTITVITY PRECISA SALVAR INSTANCIAS DE OBJETOS
    IMPORTANTES PARA O SEU FUNCIONAMENTO
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);                           //Chama a implementação da superclasse
        ArrayList<String> list = new ArrayList<>();                    //cria ArrayList de Strings
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)mensagensLista.getAdapter();   //recupera o ArrayAdapter da ListView
        //laço para salvar cada String do ArrayAdapter
        for (int i = 0; i < adapter.getCount(); i++)
            list.add(adapter.getItem(i));
        outState.putStringArrayList("lista",list);
        outState.putString("username",userNameEdit.getText().toString());
    }

/*
MÉTODO ONRESTART: É CHAMADO QUANDO A ACTIVITY É PAUSADA E PRECISA REINICIAR
 */

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!mqttAndroidClient.isConnected()) {         //se o cliente não estiver conectado
            connect();                                  //conectar
        }
    }


    /*
    MÉTODO CONNECT: CONECTA O CLIENTE MQTT AO BROKER
     */
    private void connect () {
            serverAndress = serverUri.getText().toString();         //Recupera o endereço do servidor na EditText serverUri
            username = userNameEdit.getText().toString();           //Recupera o nome de usuário na EditText userNameEdit
            clientID = MqttClient.generateClientId();               //gera um código randomico que serve como identificação do cliente

        //desconecta a sessão caso haja uma conexão estabelecida
        if (mqttAndroidClient!=null) {
            if (mqttAndroidClient.isConnected()) {
                try {
                    mqttAndroidClient.unsubscribe(publishTopic);
                    mqttAndroidClient.disconnect();
                } catch (MqttException e ) {

                }

            }
        }



         try {
             //cria um objeto MQTTClient android entregando como parametro o endereço o servidor e o id do cliente
             mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), serverAndress, clientID);
             //configura um objeto CallBack (objeto de chamada caso haja alteração)
             mqttAndroidClient.setCallback(new MqttCallbackExtended() {

                 @Override
                 public void connectComplete(boolean reconnect, String serverURI) {

                 }

                 //chamado quando há perda da conexão
                 @Override
                 public void connectionLost(Throwable cause) {
                     addToHistory("Desconectado!");

                 }

                 //chamado quando chega uma mensagem
                 @Override
                 public void messageArrived(String topic, MqttMessage message) throws Exception {
                     addToHistory(new String(message.getPayload()));
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
                     addToHistory("conectado ao servidor");
                     subscribeToTopic();
                 }

                 //chamado quando há falha na conexão
                 @Override
                 public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                     addToHistory("falha na conexão");
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
                        addToHistory("Subscrito com sucesso");
                        publishMessage(username + " Está online!");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // The subscription could not be performed, maybe the user was not
                        // authorized to subscribe on the specified topic e.g. using wildcards
                        addToHistory("Falha ao subescrever ao topico");

                    }
                });

            } catch (MqttException e) {
                e.printStackTrace();
            }

        } else {
            addToHistory("Não da pra entrar em uma conversa, o servidor não está conectado");
        }
    }


    private void publishMessage () {


        if (mqttAndroidClient.isConnected()) {
            byte[] encodedPayload = new byte[0];
            String message = username + ": "+ mensagem.getText().toString();

            try {
                encodedPayload = message.getBytes("UTF-8");
                MqttMessage mess = new MqttMessage(encodedPayload);
                mqttAndroidClient.publish(publishTopic, mess);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        } else {
            addToHistory("Não da pra enviar a mensagem, não estamos conectados ao servidor");
        }
        mensagem.setText("");

    }

    private void publishMessage (String mes) {

        if (mqttAndroidClient.isConnected()) {
            byte[] encodedPayload = new byte[0];
            String message = mes;
            try {
                encodedPayload = message.getBytes("UTF-8");
                MqttMessage mess = new MqttMessage(encodedPayload);
                mqttAndroidClient.publish(publishTopic, mess);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        } else {
            addToHistory("Não da pra enviar a mensagem, não estamos conectados ao servidor");
        }

    }

    private void addToHistory(String message) {

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mensagensLista.getAdapter();
        adapter.add(message);
        adapter.notifyDataSetChanged();

    }


}

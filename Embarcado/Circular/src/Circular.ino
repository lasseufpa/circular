

/**************************************************************
 * Circular UFPA code for the embedded system to sent GNSS data
 * over MQTT to the Circular tracking sistem
 *
 * It also sends some enviromental data, collected from the
 * campus by the buses.
 *
 * This code was based on
 * TinyGSM MQTT cliente example: https://github.com/vshymanskyy/TinyGSM/blob/master/examples/MqttClient/MqttClient.ino
 * SIM808 GNSS commands: https://www.elecrow.com/download/SIM800%20Series_GNSS_Application%20Note%20V1.00.pdf
 * Bald Engineer millis tutorial: https://www.baldengineer.com/millis-tutorial.html
 **************************************************************
 * Authors:
 * LASSE – Núcleo de Pesquisa e Desenvolvimento em Telecomunicações, Automação e Eletrônica (UFPA)
 * Felipe Bastos 
 * updated by: Gabriel Couto, Marcos Lude, Yuri Silva
 *
 *
 *
 */

 
#define TINY_GSM_MODEM_SIM808
#include <TinyGsmClient.h> //Library for GPRS(GSM) connection
#include <PubSubClient.h> //Library for MQTT protocol
//#define broker "iot.eclipse.org"

#define D9 5                // Pino que possui funções de gerenciamento de energia (sair do sleep)
#define ledNetwork 2
#define ledInternet 3
#define ledMqtt 4
#define RX_pin 10
#define TX_pin 11


//Variables to storage the GNSS data
String data;
char data2[70];


// Your GPRS credentials
// Leave empty, if missing user or pass
const char apn[]  = "timbrasil.br";
const char user[] = "tim";
const char pass[] = "tim";

// Use Hardware Serial on Mega, Leonardo, Micro
//#define Serial1 SerialAT

// or Software Serial on Uno, Nano
#include <SoftwareSerial.h>
SoftwareSerial SerialAT(RX_pin, TX_pin); // RX, TX

TinyGsm modem(SerialAT);
TinyGsmClient client(modem);
PubSubClient mqtt(client);

// #### === MQTT parameters === ####
const char* broker = "iot.eclipse.org";         //Broker adress
const int broker_port = 1883;                   //Broker port adress
const char* client_id = "circular01";           //MQTT client ID
const char* mqtt_user = "";                     //MQTT broker user
const char* mqtt_pass = "";                     //MQTT broker password

//  == MQTT topics ==
const char* topicInit = "GsmClientTest/init"; // topic to send the alive menssage
// /ufpa/circular/loc/XX(number of the bus)
const char* topicLocation = "/ufpa/circular/loc/01";  // MQTT topic to publish the current GNSS data

//Control variables
boolean flagNetwork = 0;
boolean flagInternet = 0;
boolean flagMqtt = 0;

//Timing variables
unsigned long t = 0;
unsigned long lastReconnectAttempt = 0;
unsigned long lastGNSS = 0;

//Functions intervals
unsigned int intervalReconnect = 10000;     //10 seconds
unsigned int intervalGNSS = 2000;           //2 seconds (send GNSS data every 2 seconds)

void modemRestart();
void connectNetwork();
void connectInternet();
boolean mqttConnect();
void checkConnection();
void GNSSRequest();
void getStatus();
void mqttCallback(char* topic, byte* payload, unsigned int len);

void setup()
{
  pinMode(D9,OUTPUT);
  pinMode(ledNetwork, OUTPUT);
  pinMode(ledInternet, OUTPUT);
  pinMode(ledMqtt, OUTPUT);

  digitalWrite(D9,HIGH);            //deve ser mantido no HIGH
  digitalWrite(ledNetwork, LOW);
  digitalWrite(ledInternet, LOW);
  digitalWrite(ledMqtt, LOW);
  
  
  // Set console baud rate
  Serial.begin(38400);
  delay(10);

  // Set GSM module baud rate
  SerialAT.begin(38400);
  delay(10);

  // Init Connections
  modemRestart();
  connectNetwork();
  connectInternet();

  // MQTT Broker setup
  mqtt.setServer(broker, broker_port);
  Serial.println("set");
  mqtt.setCallback(mqttCallback);
  Serial.println("calback");
}

void loop()
{
  // Check Connections
  checkConnection();                   //Futuramente fazer uma interrupção ou usar o millis()

  // if Network is desconnected, then..
  if(!flagNetwork)
  {
    getStatus();       //Print status
    //modemRestart();
    connectNetwork(); //Connect to Network
  }

  // if Ineternet is desconnected, then..
  if(!flagInternet)
  {
    getStatus();       //Print status
    connectInternet();//Connect to Internet
  }

  //if MQTT is connected
  if (flagMqtt) // then...
  {
    mqtt.loop();    //
    GNSSRequest();  //Send location
  }
  // else
  else
  {
     // Try to reconnect every 10 seconds
      t = millis();
      if (t - lastReconnectAttempt > intervalReconnect)
      {
       getStatus();       //Print status
       lastReconnectAttempt = t;
       if (mqttConnect()) {
          lastReconnectAttempt = 0;
        }
     }
   } 
}

void modemRestart()
{
  Serial.print("Initializing modem...");
  modem.restart();
  Serial.println("OK");
}

void connectNetwork()
{
  Serial.print("Waiting for network...");
  if (!modem.waitForNetwork())
  {
    Serial.println(" fail");
    while (true);
  }
  Serial.println(" OK");
  flagNetwork = 1;
  digitalWrite(ledNetwork, HIGH);
}

void connectInternet()
{
  Serial.print("Connecting to ");
  Serial.print(apn);
  if (!modem.gprsConnect(apn, user, pass))
  {
    Serial.println(" fail");
    while (true);
  }
  Serial.println(" OK");
  flagInternet = 1;
  digitalWrite(ledInternet, HIGH);
}

//Function that gets and and publish on the MQTT network the GNSS data
void GNSSRequest()
{
  t = millis();
  if (t - lastGNSS > intervalGNSS)
  {
    //Request GNSS data
    SerialAT.write("AT+CGNSINF\r\n");
    //Save GNSS data at data variable (string)
    data=SerialAT.readString();
    //Print and transmit GNSS data
    //Data structure: +CGNSINF: <GNSS run status>,<Fix status>,<UTC date & Time>,<Latitude>,<Longitude>,<MSL Altitude>,<Speed Over Ground>,<Course Over Ground>,<Fix Mode>,<Reserved1>,<HDOP>,<PDOP>,<VDOP>,<Reserved2>,<GNSS Satellites in View>,<GNSS Satellites Used>,<GLONASS Satellites Used>,<Reserved3>,<C/N0 max>,<HPA>,<VPA>
    Serial.println(data);
    //MQTT labrary only sends chars
    data.toCharArray(data2, 68);
    mqtt.publish(topicLocation, data2);

    lastGNSS = millis();
  }
}

boolean mqttConnect()
{
  Serial.print("Connecting to ");
  Serial.print(broker);
  if (!mqtt.connect(client_id, mqtt_user, mqtt_pass))
  {
    Serial.println(" fail");
    return false;
  }
  Serial.println(" OK");
  mqtt.publish(topicInit, "GsmClientTest started");

  //Turn GNSS on
  SerialAT.write("AT+CGPSPWR=1\r\n");

  return mqtt.connected();
}

// Function to show module status
void getStatus()
{
  Serial.println(modem.getGSMDateTime(DATE_FULL));
  if(modem.isNetworkConnected())
  {
    Serial.println("Conectado a Network");
    Serial.print("Signal Quality: ");
    Serial.println(modem.getSignalQuality());
  }
  else
  {
    Serial.println("Desconectado a Network");
  }
  if(modem.isGprsConnected())
  {
    Serial.println("Conectado a Internet");
  }
  else
  {
    Serial.println("Desconectado a Internet");
  }
}

// Function to check connections
void checkConnection()
{
  flagNetwork  = 0;
  flagInternet = 0;
  flagMqtt     = 0;
  
  //Check Connection with Network
  if(modem.isNetworkConnected())
  {
    flagNetwork  = 1;
    digitalWrite(ledNetwork, HIGH);
  }
  else
  {
    flagNetwork  = 0;
    digitalWrite(ledNetwork, LOW);
  }
  
  //Check Connection with Internet
  if(modem.isGprsConnected())
  {
    flagInternet = 1;
    digitalWrite(ledInternet, HIGH);
  }
  else
  {
    flagInternet = 0;
    digitalWrite(ledInternet, LOW);
  }

  //Check Connection with MQTT Broker
  if(mqtt.connected())
  {
    flagMqtt = 1;
    digitalWrite(ledMqtt, HIGH);
  }
  else
  {
    flagMqtt = 0;
    digitalWrite(ledMqtt, LOW);
  }
}
// Function to receive the income data from MQTT subscribed topics
void mqttCallback(char* topic, byte* payload, unsigned int len)
{
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("]: ");
  Serial.write(payload, len);
  Serial.println();
}

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
 *
 * Felipe Bastos
 *
 *
 *
 *
 *
 **************************************************************/

// Select your modem:
#define TINY_GSM_MODEM_SIM800
//#define TINY_GSM_MODEM_SIM900
//#define TINY_GSM_MODEM_A6
//#define TINY_GSM_MODEM_M590

#include <TinyGsmClient.h> //Library for GPRS(GSM) connection
#include <PubSubClient.h> //Livrery for MQTT protocol

//Variables to storage the GNSS data
String data;
char data2[70];


// Your GPRS credentials
// Leave empty, if missing user or pass
const char apn[]  = "timbrasil.br";
const char user[] = "tim";
const char pass[] = "tim";

// Use Hardware Serial on Mega, Leonardo, Micro
#define SerialAT Serial1

// or Software Serial on Uno, Nano
//#include <SoftwareSerial.h>
//SoftwareSerial SerialAT(2, 3); // RX, TX

TinyGsm modem(SerialAT);
TinyGsmClient client(modem);
PubSubClient mqtt(client);

const char* broker = "iot.eclipse.org"; //Broker adress
const char* topicInit = "GsmClientTest/init";

// MQTT topic to publish the current GNSS data
// /circularufpa/location/XX(number of the bus)
const char* topicLocation = "/circularufpa/location/01";

// MQTT topics to publish the current enviromental data
// /circularufpa/enviroment/data(temp, humidity...)/XX(number of the bus)
const char* topicTemp = "/circularufpa/enviroment/temp/01";

//Timing variables
unsigned long t = 0;
unsigned long lastReconnectAttempt = 0;
unsigned long lastGNSS = 0;
unsigned long lastSensor = 0;

int intervalReconnect = 10000; //10 seconds
int intervalGNSS = 2000; //2 seconds
int intervalSensor = 60000; //1 minute


void setup()
{
  // Set console baud rate
  Serial.begin(115200);
  delay(10);

  // Set GSM module baud rate
  SerialAT.begin(115200);
  delay(10);

  // Restart takes quite some time
  // To skip it, call init() instead of restart()
  Serial.println("Initializing modem...");
  modem.restart();

  // Unlock your SIM card with a PIN
  //modem.simUnlock("1234");

  Serial.print("Waiting for network...");
  if (!modem.waitForNetwork())
  {
    Serial.println(" fail");
    while (true);
  }
  Serial.println(" OK");

  Serial.print("Connecting to ");
  Serial.print(apn);
  if (!modem.gprsConnect(apn, user, pass))
  {
    Serial.println(" fail");
    while (true);
  }
  Serial.println(" OK");

  // MQTT Broker setup
  mqtt.setServer(broker, 1883);
  Serial.println("set");
  mqtt.setCallback(mqttCallback);
  Serial.println("calback");
}

boolean mqttConnect()
{
  Serial.print("Connecting to ");
  Serial.print(broker);
  if (!mqtt.connect("GsmClientTest"))
  {
    Serial.println(" fail");
    return false;
  }
  Serial.println(" OK");
  mqtt.publish(topicInit, "GsmClientTest started");

  //Turn GNSS on
  Serial1.write("AT+CGPSPWR=1\r\n");

  return mqtt.connected();
}

void loop()
{

  if (mqtt.connected())
  {
    mqtt.loop();
    GNSSRequest();
    SensorRequest();

  }
  else
  {
    // Reconnect every 10 seconds
    t = millis();
    if (t - lastReconnectAttempt > intervalReconnect)
    {
      lastReconnectAttempt = t;
      if (mqttConnect()) {
        lastReconnectAttempt = 0;
      }
    }
}
}

//Function that gets and and publish on the MQTT network the GNSS data
void GNSSRequest()
{
  t = millis();
  if (t - lastGNSS > intervalGNSS)
  {
    //Request GNSS data
    Serial1.write("AT+CGNSINF\r\n");
    //Save GNSS data at data variable (string)
    data=Serial1.readString();
    //Print and transmit GNSS data
    //Data structure: +CGNSINF: <GNSS run status>,<Fix status>,<UTC date & Time>,<Latitude>,<Longitude>,<MSL Altitude>,<Speed Over Ground>,<Course Over Ground>,<Fix Mode>,<Reserved1>,<HDOP>,<PDOP>,<VDOP>,<Reserved2>,<GNSS Satellites in View>,<GNSS Satellites Used>,<GLONASS Satellites Used>,<Reserved3>,<C/N0 max>,<HPA>,<VPA>
    Serial.println(data);
    //MQTT lobrary only sends chars
    data.toCharArray(data2, 68);
    mqtt.publish(topicLocation, data2);

    lastGNSS = millis();
  }
}

void SensorRequest()
{
  t = millis();
  if (t - lastSensor > intervalSensor)
  {
    //code here
    lastSensor = millis();
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

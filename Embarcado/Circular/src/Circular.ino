/**************************************************************
 * Circular UFPA code for the embedded system to sent GNSS data
 * over MQTT to the Circular tracking system
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
 * updated by: Gabriel Couto and Yuri Silva
 *
 *
 *
 */
#define TINY_GSM_MODEM_SIM808
#include <TinyGsmClient.h> //Library for GPRS(GSM) connection
#include <PubSubClient.h> //Library for MQTT protocol
#include <avr/wdt.h>

// --- Mapeamento de Hardware ---
#define D9 6                // Pino que possui funções de gerenciamento de energia (sair do sleep)
#define ledNetwork 2
#define ledInternet 3
#define ledMqtt 4
#define rxPin 10
#define txPin 11
#define lm35 A0

//Variables to storage the GNSS data
String data;
char   data2[70];

// Your GPRS credentials
// Leave empty, if missing user or pass
const char apn[]  = "claro.com.br";
const char user[] = "claro";
const char pass[] = "claro";

// Use Hardware Serial on Mega, Leonardo, Micro
//#define Serial1 SerialAT

// or Software Serial on Uno, Nano
#include <SoftwareSerial.h>
SoftwareSerial SerialAT(rxPin, txPin); // RX, TX

TinyGsm modem(SerialAT);
TinyGsmClient client(modem);
PubSubClient mqtt(client);

// === MQTT parameters ===
const char* broker    = "iot.eclipse.org";      //Broker adress
const int broker_port = 1883;                   //Broker port adress
const char* client_id = "circular01";           //MQTT client ID
const char* mqtt_user = "";                     //MQTT broker user
const char* mqtt_pass = "";                     //MQTT broker password

//  == MQTT topics ==
// /ufpa/circular/loc/XX(number of the bus)
const char* topicLocation = "/ufpa/circular/loc/01";    // MQTT topic to publish the current GNSS data

//Control variables
boolean waitConnection  = 0;
boolean flagNetwork     = 0;
boolean flagInternet    = 0;
boolean flagMqtt        = 0;
boolean flagLedNetwork  = 0;
boolean flagLedInternet = 0;
boolean flagLedMqtt     = 0;
boolean flagReset       = 0;
uint8_t resetNumber     = 0;

//Timing variables
unsigned long lastReconnectAttempt = 0;
unsigned long lastGNSS   = 0;
unsigned long resetADay = 0;
uint8_t counter          = 0;

//Functions intervals
unsigned const int intervalReconnect = 5000;  //5 seconds
unsigned const int intervalGNSS      = 2000;  //2 seconds (send GNSS data every 2 seconds)
void connections();
void GNSSRequest();
void failConnection();
void splitGnssData(String data_,char div);

String hora;
String latitude;
String longitude;
String velocidade;
String curso;


//Rotina de Interrupção que ocorre a cada ~ 10ms
ISR(TIMER2_OVF_vect)
{
    
    TCNT2=100;                      // Reinicializa o registrador do Timer2
    counter++;                      //incremente várialvel auxiliar
    if(counter == 50)               //Ocorre a cada 500ms
    {
      counter = 0;                  //Reiniciar a variável
        
      if(flagNetwork)           digitalWrite(ledNetwork, HIGH);
      else if (flagLedNetwork)  digitalWrite(ledNetwork, !digitalRead(ledNetwork));
      else                      digitalWrite(ledNetwork, LOW);

      if(flagInternet)          digitalWrite(ledInternet, HIGH);
      else if (flagLedInternet) digitalWrite(ledInternet, !digitalRead(ledInternet));
      else                      digitalWrite(ledInternet, LOW);

      if(flagMqtt)              digitalWrite(ledMqtt, HIGH);
      else if (flagLedMqtt)     digitalWrite(ledMqtt, !digitalRead(ledMqtt));
      else                      digitalWrite(ledMqtt, LOW);

      
      if(millis()-resetADay > 86400000) //delisgar a cada 24h
      {
        mqtt.publish("/ufpa/circular/debug/01", "Vou reiniciar, Tô trabalhando 24H");     //Mensagem de reiniciar
        flagReset = 1;
      }
      
      if(flagReset) ;                         // Se o MCU quiser reiniciar, então ... não faça nada (esperar o wdt_timer reiniciar)
      else if(!waitConnection) wdt_reset();   // Se o MCU não está testando conexão, então ... reset o wdt para NÃO reiniciar
      else                                    // Se o MCU está tentando se conectar, então ... espere 1 minuto antes de reiniciar
      {
        if(resetNumber > 0)
        {
          wdt_reset();
          resetNumber--;
        }
        else ;                                // Passou um minuto, então ... espere (Por padrão, nunca orá acontecer)
      }
    }  
}

void setup()
{
  wdt_disable();
  flagReset       = 0;
  flagNetwork     = 0;
  flagInternet    = 0;
  flagMqtt        = 0;
  flagLedNetwork  = 0;
  flagLedInternet = 0;
  flagLedMqtt     = 0;
  
  // Set console baud rate
  Serial.begin(38400);
  delay(10);

  // Set GSM module baud rate
  SerialAT.begin(38400);
  delay(10);
  
  //Definindo Pino de controle e LEDs
  pinMode(D9,OUTPUT);             
  pinMode(ledNetwork, OUTPUT);
  pinMode(ledInternet, OUTPUT);
  pinMode(ledMqtt, OUTPUT);

  digitalWrite(D9,HIGH);            //deve ser mantido no HIGH para o funcionamento do módulo
  digitalWrite(ledNetwork, LOW);    // Começando os LED's de Conexão em nível LOW
  digitalWrite(ledInternet, LOW);   //...
  digitalWrite(ledMqtt, LOW);       //...

  Serial.print("Initializing modem...");
  modem.restart();
  Serial.println("OK");

  SerialAT.println("AT+IPR=38400");             //Set baud rate to 38400
  SerialAT.write("AT+CGPSPWR=1\r\n");           // Power On GPS

  cli();           //Desabilitando interrupções globais
  TCCR2A = 0x00;   //Timer operando em modo normal
  TCCR2B = 0x07;   //Prescaler 1:1024
  TCNT2  = 100;    //10 ms overflow again
  TIMSK2 = 0x01;   //Habilita interrupção do Timer2

  wdt_enable(WDTO_2S);  //Configurando wdt_timer para estourar a cada 2S
  
  sei();               //Habilitando interrupções globais

  mqtt.setServer(broker, broker_port);           // MQTT Broker setup
  mqtt.setCallback(mqttCallback);                // Set callback (Não utilizado)
  
  connections();                   // Estabelece as conexões
  mqtt.publish("/ufpa/circular/debug/01", "Iniciei");     //Mensagem de inicio
  resetADay = millis();           //guardar o tempo que o Arduino iniciou 
}

void loop()
{
  mqtt.loop();
  connections();
  if (flagMqtt)             //if MQTT is connected, then...
  {    
    GNSSRequest();          //Send Location
  }
  if(Serial.available())    //Se Alguma mensagem foi enviada ao arduino, então ...
  {
    wdt_reset();    
    ATcommands(Serial.readString());  //Envie esse comando ao módulo
  }
}

//Fução que Estabelece e testa as conexões do Arduino
void connections()
{
  resetNumber = 120;        // Esperar um minuto antes de reiniciar
  waitConnection = 1;       // Afirma que está testando/estabelecendo conexões
  
  if(!flagNetwork)          // Se a rede não está conectada, então ... se conecte
  {
    flagLedNetwork = 1;     // Piscar o led de rede
    Serial.print("Waiting for network...");
    if (!modem.waitForNetwork())
    {
      failConnection();     // Não consegui estabelecer conexão
    }
    Serial.println("OK");
    flagNetwork = 1;        // Afirma que está conectado a rede
    flagLedNetwork = 0;     // Pare de piscar o led de rede
    SerialAT.write("AT+CGPSPWR=1\r\n");   // Ligue o GPS (Teoricamente não seria necessário...)
  }

  resetNumber = 120;        // Esperar um minuto antes de reiniciar   
  
  if(!flagInternet)         // Se a internet não está conectada, então ... se conecte
  {
    flagLedInternet = 1;     // Piscar o led de internet
    Serial.print("Connecting to ");
    Serial.print(apn);
    if (!modem.gprsConnect(apn, user, pass))
    {
     failConnection();     // Não consegui estabelecer conexão
    }
    Serial.println(" OK");
    flagInternet = 1;        // Afirma que está conectado a internet
    flagLedInternet = 0;     // Pare de piscar o led de rede
  }

  resetNumber = 120;        // Esperar um minuto antes de reiniciar 
  
  if(!flagMqtt)
  {
    if (millis() - lastReconnectAttempt > intervalReconnect)
    {
      flagLedMqtt = 1;
      Serial.print("Connecting to ");
      Serial.print(broker);
      if (!mqtt.connect(client_id, mqtt_user, mqtt_pass))
      {
       Serial.println(" fail");
      }
      else
      {
        Serial.println(" OK");
        lastReconnectAttempt = 0;
        flagMqtt    = 1;
        flagLedMqtt = 0;
      }
    }
    else lastReconnectAttempt = millis();
  }
  
  if(modem.isNetworkConnected())
  {
    flagNetwork = 1;
  }
  else
  {
    Serial.println("Rede Falhou");
    flagNetwork  = 0;
    flagInternet = 0;
    flagMqtt     = 0;
    modem.gprsDisconnect();
    mqtt.disconnect();
  }

  if(modem.isGprsConnected() && flagNetwork) flagInternet = 1;
  else
  {
    Serial.println("Internet Falhou");
    flagInternet = 0;
    flagMqtt = 0;
    modem.gprsDisconnect();
    mqtt.disconnect();
  }

  if(mqtt.connected() && flagNetwork && flagInternet) flagMqtt = 1;
  else
  {
    Serial.println("MQTT Falhou");
    mqtt.disconnect();
    flagMqtt = 0;
  } 
  waitConnection = 0;

}

//Function that gets and and publish on the MQTT network the GNSS data
void GNSSRequest()
{
  if (millis() - lastGNSS > intervalGNSS)
  {
    SerialAT.write("AT+CGNSINF\r\n");   //Request GNSS data
    data=SerialAT.readString();         //Save GNSS data at data variable (string)
    //Data structure: +CGNSINF: <GNSS run status>,<Fix status>,<UTC date & Time>,<Latitude>,<Longitude>,<MSL Altitude>,<Speed Over Ground>,<Course Over Ground>,
    //                          <Fix Mode>,<Reserved1>,<HDOP>,<PDOP>,<VDOP>,<Reserved2>,<GNSS Satellites in View>,<GNSS Satellites Used>,<GLONASS Satellites Used>,<Reserved3>,<C/N0 max>,<HPA>,<VPA>               //Print GNSS data
    //Serial.println("#############################");
    Serial.println(data);
    //Split GNSS string
    splitGnssData(data, ',');
    
    //Data structure: <Qualidade do sinal>,<Temperatura>,<UTC date & Time>,<Latitude>,<Longitude>,<Velocidade>,<Curso>
    data =  String(-(113-(int(modem.getSignalQuality())*2)));
    data += ","; 
    data += String((float(analogRead(lm35))*5/(1023))/0.01);
    data += ",";
    data += hora;
    data += ",";
    data += latitude;
    data += ",";
    data += longitude;
    data += ",";
    data += velocidade;
    data += ",";
    data += curso;
    
    //MQTT labrary only sends chars
    data.toCharArray(data2, 68);          //Converter
    mqtt.publish(topicLocation, data2);     //Publish GNSS data on Topic Location
    lastGNSS = millis();                  //Guarda o tempo que o envio foi feito

    /*
    Serial.println("INFORMAÇÕES ENVIADAS: ");
    Serial.print("Qualidade do Sinal ~: ");
    Serial.print(String(-(113-(int(modem.getSignalQuality())*2))));
    Serial.println("dBm");
    Serial.print("Temperatura ~: ");
    Serial.println((float(analogRead(lm35))*5/(1023))/0.01);
    Serial.print("Dara e Hora UTC: ");
    Serial.println(hora);
    Serial.print("Latitude: ");
    Serial.println(latitude);
    Serial.print("Longitude: ");
    Serial.println(longitude);
    Serial.print("Velocidade: ");
    Serial.println(velocidade);
    Serial.print("Curso: ");
    Serial.println(curso);
    Serial.print("Link: ");
    data = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
    Serial.println(data);
    Serial.println("#############################");
    */
  }
}

void failConnection()
{
  Serial.println(" fail");
  digitalWrite(ledNetwork, LOW);    // os 3 Leds piscando simultaneamente
  digitalWrite(ledInternet, LOW);   //...
  digitalWrite(ledMqtt, LOW);       //...
  flagLedNetwork = 1;               //...
  flagLedInternet = 1;              //...
  flagLedMqtt = 1;                  //...

  //flagReset = 1; //Arduino ira resetar nos proximos 2S.
  resetNumber = 0;
  
  Serial.println("Fail Reset");
  while(1);
  //
}

void splitGnssData(String data_,char div)
{

   uint8_t wordPosition=0;
   String arr[8];

   for (int i = 0; i < data_.length() ; ++i)
   {
       if(data_[i] == div)
       {
           ++wordPosition;
           if(wordPosition == 8) break;
       }
       else
       {
           arr[wordPosition] += data_[i];
       }
   }

   for (int i = 0; i < 8; ++i)
   {
       if(!arr[i].length())
           arr[i] = "0.000000";
   }

   hora       = arr[2];
   latitude   = arr[3];
   longitude  = arr[4];
   velocidade = arr[6];
   curso      = arr[7];
}

void ATcommands(String command)
{
  SerialAT.println(command);
  Serial.println(SerialAT.readString());
}
// Function to receive the income data from MQTT subscribed topics
void mqttCallback(char* topic, byte* payload, unsigned int len)
{
  String msg;

    if(!len) flagReset = 1;
    //obtem a string do payload recebido
    for(int i = 0; i < len; i++) 
    {
       char c = (char)payload[i];
       msg += c;
    }
    
    Serial.println(msg);
}

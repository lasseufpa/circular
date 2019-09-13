#define TINY_GSM_MODEM_SIM808
#include "TinyGsmClient.h" //GPRS(GSM) connection Library
#include "PubSubClient.h"  //MQTT protocol Library
#include <avr/wdt.h>       //Watchdog timer Library
#include <EEPROM.h>
#include "Config.h"

TinyGsm modem(SerialAT);                //TinyGSM use SerialAT to communication
TinyGsmClient client(modem);            //...
PubSubClient mqtt(client);              //Create a MQTT client

//funções

//variaveis globais
String data;
char   data2[70];

//Control variables
boolean waitConnection = 0, flagReset       = 0,
        flagNetwork    = 0, flagInternet    = 0, flagMqtt        = 0,
        flagLedNetwork = 0, flagLedInternet = 0, flagLedMqtt     = 0;
        
uint8_t resetNumber = 0, counter = 0;

//Timing variables
unsigned long lastReconnectAttempt = 0, lastPublish   = 0,
              lastConnection       = 0, resetADay     = 0,
              lastCheckSMS         = 0;

//Functions intervals
unsigned const int intervalReconnect        = 5000, attemptPublishDelay  = 2000, 
                    attempetConnectionDelay = 1000, attemptCheckSMS      = 30000;
                    
ISR(TIMER2_OVF_vect)
{
    TCNT2=100;                      //Reinicializa o registrador do Timer2
    counter++;                      //incremente várialvel auxiliar
    
    if(counter == 50)               //Ocorre a cada 500ms
    {
      counter = 0;                  //Reiniciar a variável
      ledControl(flagNetwork, flagLedNetwork, ledNetwork);    //ledControl turn ON, OFF or BLINK the LED
      ledControl(flagInternet, flagLedInternet, ledInternet); //....
      ledControl(flagMqtt, flagLedMqtt, ledMqtt);             //....
      resetControl();               //Control All Resets events
    }  
}

void setup() {
  wdt_disable();
  
  initPeripherals();
  modemSetup();
  timersSetup();
  
  wdt_enable(WDTO_2S);
  
  mqttSetup();
  connections();  
  mqttDebug("start");
  checkReset();
  checkSMS();
  deleteSms();
}

void loop() {
  if(millis() - lastConnection > attempetConnectionDelay) connections();
  if(flagMqtt && millis() - lastPublish > attemptPublishDelay) publishLocation();
  if(millis() - lastCheckSMS > attemptCheckSMS) checkSMS();
  if(Serial.available()){
    data = Serial.readString();
    Serial.println(data);
    SerialAT.println(data);
    delay(10);
    data = SerialAT.readString();
    Serial.println(data);
  }
  if(SerialAT.available()){
    Serial.println("SERIAL");
    data = SerialAT.readString();
    Serial.println(data);
    if(cut(cut(data, ':', 0), '+', 1) == "CMTI")
    {
      reciveSMS();
      deleteSms();
      lastCheckSMS = millis();
    }
    
  }
}

void checkSMS(){
  SerialAT.println("AT+CMGR=1");
  data = SerialAT.readString();
  data = cut(data, ':', 0);
  data = cut(data, '+', 1);
  if(data == "CMGR"){
    Serial.println("Tem Mensagem");
    reciveSMS();
    deleteSms();
  }
  else Serial.println("Não tem Mensagem");
  lastCheckSMS = millis();
}

void mqttSetup(){
  mqtt.setServer(broker, broker_port);           // MQTT Broker setup
  mqtt.setCallback(mqttCallback);                // Set callback (Não utilizado nesse projeto)
}

void modemSetup(){
  Serial.print("Initializing modem...");
  modem.restart();
  Serial.println("OK");
  SerialAT.println("AT+IPR=38400");             //Set baud rate to 38400
  SerialAT.write("AT+CGPSPWR=1\r\n");           // Power On GPS
  SerialAT.println("AT+CMGF=1");
  deleteSms();
}

void mqttDebug(String command){
  if(command == "start"){
    mqtt.publish(topicDebug, "Start - Circular 01");     //Mensagem de inicio
    resetADay = millis();                                //guardar o tempo que o Arduino iniciou (Este tempo será utilizado para reiniciar depois de um dia)
  }
  else if(command == "restart"){
    mqtt.publish(topicDebug, "24hr Working");     //Mensagem de reiniciar
    flagReset = 1;                                //set the Flag to reset arduino
  }
}

void publishLocation(){
  SerialAT.write("AT+CGNSINF\r\n");   //Request GNSS data
  data = SerialAT.readString();         //Save GNSS data at data variable (string)
  char div = ',';
  String arr[8];
  uint8_t wordPosition=0;
   
  for (unsigned int i = 0; i < data.length() ; ++i){
      if(data[i] == div){
          ++wordPosition;
          if(wordPosition == 8) break;
      }
      else{
        arr[wordPosition] += data[i];
      }
  }         
  for (int i = 0; i < 8; ++i) if(!arr[i].length())arr[i] = "0";
   
  data = String(-(113-(int(modem.getSignalQuality())*2))); data += ","; 
  data += String((float(analogRead(lm35))*5/(1023))/0.01); data += ",";
  data += arr[2]; data += ",";
  data += arr[3]; data += ",";
  data += arr[4]; data += ",";
  data += arr[6]; data += ",";
  data += arr[7];
  Serial.println(data);
  data.toCharArray(data2, 68);          //Converter
  data = "";
  mqtt.publish(topicLocation, data2, true);     //Publish GNSS data on Topic Location
  
  lastPublish = millis();                  //Guarda o tempo que o envio foi feito
}

String cut(String data_,char div, int position)
{
  
   uint8_t wordPosition = 0;
   String arr[8];
   for (unsigned int i = 0; i < data_.length() ; ++i)
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
   
   if(!arr[position].length()) return "0.00";
   return arr[position];
}

// ### --- Peripheral Control --- ###
void initPeripherals()
{
  // Set console baud rate
  Serial.begin(9600);
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
}

void ledControl(boolean flagService ,boolean flagLed , int ledPin)
{
  if(flagService)              digitalWrite(ledPin, HIGH);  
  else if (flagLed)     digitalWrite(ledPin, !digitalRead(ledPin));
  else                      digitalWrite(ledPin, LOW);
}

// ### --- Connection Functions --- ###
//Fução que Estabelece e testa as conexões do Arduino
void connections()
{
  resetNumber = 120;        // Esperar um minuto antes de reiniciar
  waitConnection = 1;       // Afirma que está testando/estabelecendo conexões
  
  if(!flagNetwork){
    flagLedNetwork = 1;     // Piscar o led de rede
    Serial.print("Waiting for network...");
    if (!modem.waitForNetwork()) failConnection();     // Não consegui estabelecer conexão
    Serial.println("OK");
    flagNetwork = 1;        // Afirma que está conectado a rede
    flagLedNetwork = 0;     // Pare de piscar o led de rede
    SerialAT.write("AT+CGPSPWR=1\r\n");   // Ligue o GPS (Teoricamente não seria necessário...)
  }

  resetNumber = 120;        // Esperar um minuto antes de reiniciar   
  
  if(!flagInternet){
    flagLedInternet = 1;     // Piscar o led de internet
    Serial.print("Connecting to ");
    Serial.print(apn);
    if (!modem.gprsConnect(apn, user, pass)) failConnection();
    Serial.println(" OK");
    flagInternet = 1;        // Afirma que está conectado a internet
    flagLedInternet = 0;     // Pare de piscar o led de rede
  }

  resetNumber = 120;        // Esperar um minuto antes de reiniciar 
  
  if(!flagMqtt){
    if (millis() - lastReconnectAttempt > intervalReconnect){
      flagLedMqtt = 1;
      Serial.print("Connecting to ");
      Serial.print(broker);
      if (!mqtt.connect(client_id, mqtt_user, mqtt_pass)) Serial.println(" fail");
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
  
  if(modem.isNetworkConnected()) flagNetwork = 1;
  else {
    Serial.println("Rede Falhou");
    flagNetwork  = 0;
    flagInternet = 0;
    flagMqtt     = 0;
    mqtt.disconnect();
    modem.gprsDisconnect();
  }

  if(modem.isGprsConnected() && flagNetwork) flagInternet = 1;
  else {
    Serial.println("Internet Falhou");
    flagInternet = 0;
    flagMqtt = 0;
    mqtt.disconnect();
    modem.gprsDisconnect();
  }

  if(mqtt.connected() && flagNetwork && flagInternet) flagMqtt = 1;
  else {
    Serial.println("MQTT Falhou");
    mqtt.disconnect();
    flagMqtt = 0;
  } 
  waitConnection = 0;
  lastConnection = millis();
}

void failConnection()
{
  Serial.println(" fail");
  checkSMS();
  digitalWrite(ledNetwork, LOW);    // os 3 Leds piscando simultaneamente
  digitalWrite(ledInternet, LOW);   //...
  digitalWrite(ledMqtt, LOW);       //...
  flagLedNetwork = 1;               //...
  flagLedInternet = 1;              //...
  flagLedMqtt = 1;                  //...

  //flagReset = 1; //Arduino ira resetar nos proximos 2S.
  resetNumber = 0;
  delay(2000);
  Serial.println("Fail Reset");
  while(1);
}

// ### --- Reset Control --- ###
void timersSetup(){
  cli();           //Desabilitando interrupções globais
  TCCR2A = 0x00;   //Timer operando em modo normal
  TCCR2B = 0x07;   //Prescaler 1:1024
  TCNT2  = 100;    //10 ms overflow again
  TIMSK2 = 0x01;   //Habilita interrupção do Timer2
  sei();               //Habilitando interrupções globais
}

void resetControl(){
 if(millis()-resetADay > 86400000) //delisgar a cada 24h
  {
    mqtt.publish(topicDebug, "24hr Working", true);     //Mensagem de reiniciar
    flagReset = 1;                                //set the Flag to reset arduino
  }
      
  if(flagReset) ;                         // Se for para reiniciar o arduino, então ... não faça nada (esperar o wdt_timer reiniciar depois de 2 segs)
  else if(!waitConnection) wdt_reset();   // Se o MCU não está testando conexão, então ... reset o wdt para NÃO reiniciar
  else                                    // Se o MCU está tentando se conectar, então ... espere 1 minuto
                                          // Se persistirem os sintomas o arduino deverá ser reiniciado
  {
  if(resetNumber > 0)
  {
    wdt_reset();
    resetNumber--;
  }
  else ;                                // Passou um minuto, então ... espere até reiniciar
  }
}

void mqttCallback(char* topic, byte* payload, unsigned int len)
{
  String msg;

    if(!len) flagReset = 1;
    //obtem a string do payload recebido
    for(unsigned int i = 0; i < len; i++) 
    {
       char c = (char)payload[i];
       msg += c;
    }
    
    Serial.println(msg);
}


void reciveSMS()
{
  SerialAT.println("AT+CMGR=1");
  data = SerialAT.readString();
  Serial.println(data);
  String phoneNumber = cut(cut(data, ',', 1), '"', 1);
  String sms = cut(cut(data, '#', 1), '.', 0);
  Serial.print("Comando: ");Serial.println(sms);
  Serial.print("Número");Serial.println(phoneNumber);
  if ( sms == "0.00"){
    for(int i=0; i<=200; i++){
      sms = cut(cut(data, '#', 1), '.', 0);
      if ( sms != "0.00") break;
    } 
  }
  if ( phoneNumber == "0.00"){
    for(int i=0; i<=1000; i++){
      phoneNumber = cut(cut(data, ',', 1), '"', 1);
      if ( phoneNumber != "0.00") break;
    } 
  }
  Serial.print("Comando: ");Serial.println(sms);
  Serial.print("Número");Serial.println(phoneNumber);
  if (sms == "restart"){
    modem.sendSMS(phoneNumber, "RESTART COMMAND");
    for(int i=0; i< 14; i++) EEPROM.write(i, char(phoneNumber[i]));
    for(int i=0; i< 14; i++){
      Serial.print(i);Serial.print(": ");Serial.println(char(EEPROM.read(i)));
    }
    EEPROM.write(255, 1);
    
    flagReset = 1;
  }
  else if ( sms == "debug"){
    data = "";
    data = "DEBUG MODE\n""Network: " + String(flagNetwork) + "\nGPRS: " + String(flagInternet) + "\nMQTT: " + String(flagMqtt);
    modem.sendSMS(phoneNumber, data);
  }
  //else if ( sms == "normal") modem.sendSMS(phoneNumber, "O comando NORMAL MODE foi executado com Sucesso!");
  //else if ( sms == "location") modem.sendSMS(phoneNumber, "O comando LOCATION foi executado com Sucesso!");
  //comandsSms(&phoneNumber,&sms);
  deleteSms();
}

void checkReset(){
  if(EEPROM.read(255)){
    String phoneNumber;
    for(int i=0; i<14; i++) phoneNumber+= char(EEPROM.read(i));
    EEPROM.write(255, 0);
    Serial.println(phoneNumber);
    modem.sendSMS(phoneNumber, "Restart complete");
  }
}
/*
void comandsSms(String* phoneNumber, String* sms)
{
    Serial.print("Número:");Serial.println(*phoneNumber);
    Serial.print("Comando:");Serial.println(*sms);
    if ( *sms == "restart" )
    {
      modem.sendSMS(*phoneNumber, "O comando RESTART foi executado com Sucesso!");
    }
    else if ( *sms == "debug")
    {
      Serial.println("Debug");
      modem.sendSMS(*phoneNumber, "O comando DEBUG MODE foi executado com Sucesso!");
    }
    else if ( *sms == "normal")
    {
     Serial.println("Normal");
     modem.sendSMS(*phoneNumber, "O comando NORMAL MODE foi executado com Sucesso!");
    }
    else if ( sms == "location")
    {
     Serial.println("SMS location");
     SerialAT.write("AT+CGNSINF\r\n");   //Request GNSS data
     data = SerialAT.readString();         //Save GNSS data at data variable (string)
     char div = ',';
     String arr[8];
     uint8_t wordPosition=0;
   
     for (unsigned int i = 0; i < data.length() ; ++i){
        if(data[i] == div){
             ++wordPosition;
             if(wordPosition == 8) break;
        }
        else{
          arr[wordPosition] += data[i];
        }
     }
              
     for (int i = 0; i < 8; ++i) if(!arr[i].length())arr[i] = "0";
   
     data = String(-(113-(int(modem.getSignalQuality())*2))); data += ","; 
     data += String((float(analogRead(lm35))*5/(1023))/0.01); data += ",";
     data += arr[2]; data += ",";
     data += arr[3]; data += ",";
     data += arr[4]; data += ",";
     data += arr[6]; data += ",";
     data += arr[7];
     data = data + "\n http://maps.google.com/maps?q=" + arr[3] + "," + arr[4];
     modem.sendSMS(phoneNumber, data);
    }
    else Serial.println(data);
}*/

void deleteSms()
{
  SerialAT.println("AT+CMGD=1,1");
  SerialAT.println("AT+CMGD=1,2");
  SerialAT.println("AT+CMGD=1,3");
  SerialAT.println("AT+CMGD=1,4");
}

//AT+CPMS="SM","SM","SM"; quando o retorno de CMTI é tiferente de SM

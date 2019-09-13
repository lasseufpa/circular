// --- Mapeamento de Hardware ---
#define D9 6                // Pino que possui funções de gerenciamento de energia (sair do sleep)
#define ledNetwork 2
#define ledInternet 3
#define ledMqtt 4
#define rxPin 10
#define txPin 11
#define lm35 A0

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

// === MQTT parameters ===
const char* broker    = "broker";      //Broker adress
const int broker_port = 1883;                               //Broker port adress
const char* client_id = "id";                      //MQTT client ID
const char* mqtt_user = "user";                        //MQTT broker user
const char* mqtt_pass = "passwd";                     //MQTT broker password

//  == MQTT topics ==
// /ufpa/circular/loc/XX(number of the bus)
const char* topicLocation = "/ufpa/circular/loc/01";    // MQTT topic to publish the current GNSS data
const char* topicDebug = "/ufpa/circular/debug/01";    // MQTT topic to publish Debugs

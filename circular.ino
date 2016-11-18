#include <LiquidCrystal.h>
 
LiquidCrystal lcd(30, 31, 32, 33, 34, 35); 
char dadoTwitter, dadoTwitterLCD;
int leds[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
int ledCirc1 = 27, ledCirc2 = 28;
int x = 0, t = 0;


void setup() {
  Serial.begin(9600);
  for (int cont = 1; cont < 25; cont++){
    pinMode (leds[cont], OUTPUT);
  }
  pinMode (ledCirc1, OUTPUT);
  pinMode (ledCirc2, OUTPUT);
  lcd.begin(16, 2);
  lcd.setCursor(0, 0);
  lcd.print("Interaja Conosco!");
  lcd.setCursor(0, 1); 
  lcd.print("Citações: "); 
  
}

void loop() {
  
  digitalWrite (ledCirc1, HIGH);
  digitalWrite (ledCirc2, HIGH);
  
  if (Serial.available()){
    dadoTwitter = Serial.read();
    dadoTwitterLCD = Serial.read();
      if (dadoTwitter == '1'){
        Serial.print ("#");
        Leds();
        LCD();
        dadoTwitter = 0;
      }
  }
  

}

void Leds(){
  x = x + 1;
  digitalWrite(leds[x], HIGH);
  delay (50);
  digitalWrite(leds[x], LOW);
  delay (50);
  digitalWrite(leds[x], HIGH);
  if (x==24){ // Quando chegar no número máximo de LEDs entra aqui
      for(x=25;x>=0;x--){
        digitalWrite(leds[x], LOW);
        delay (5);
        }
      for(x=0;x<=24;x++){
        digitalWrite(leds[x], HIGH);
        delay (5);
        }
      for(x=0;x<=24;x++){
        digitalWrite(leds[x], LOW);
        delay (150);
        }
      x=0;
   }
}

void LCD(){
  t = t + 1;
  lcd.setCursor(11, 1);
  lcd.print(t);
}

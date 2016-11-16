#include <LiquidCrystal.h>
 
LiquidCrystal lcd(12, 11, 5, 4, 3, 2); 
int dadoTwitter, dadoTwitterLCD;
int leds[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};


void setup() {
  Serial.begin(9600);
  for (int cont = 1; cont < 14; cont++){
    pinMode (leds[cont], OUTPUT);
  }
  lcd.begin(16, 2);
  lcd.setCursor(0, 0);
  lcd.print("Interaja Conosco!");
  lcd.setCursor(0, 1); 
  lcd.print("Citações: "); 
  
}

void loop() {
  
  
  if (Serial.available()){
    dadoTwitter = Serial.read();
    dadoTwitterLCD = Serial.read();
      if (dadoTwitter == 1){
        Leds();
        LCD();
        dadoTwitter = 0;
      }
  }
  

}

void Leds(){
  int x = x + 1;
  digitalWrite(leds[x], HIGH);
  if (x=13){ // Quando chegar no número máximo de LEDs entra aqui
      for(x=13;x>=0;x--){
        digitalWrite(leds[x], LOW);
        delay (5);
        }
      for(x=0;x<=13;x++){
        digitalWrite(leds[x], HIGH);
        delay (5);
        }
      for(x=0;x<=13;x++){
        digitalWrite(leds[x], LOW);
        delay (100);
        }
   }
}

void LCD(){
  int t = t + 1;
  lcd.setCursor(11, 1);
  lcd.print(t);
}


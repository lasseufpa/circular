package org.example.circular.checklocation;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private TextView latitude;   // Variável de classe para armazenar view de latitude
    private TextView longitude;  // Variável de classe para armazenar view de longitude
    private Button boton;        //Variável de classe para armazenar botão
    private GoogleApiClient googleApiClient; // Variável de classe para armazenar instãncia de cliente do Google API
    private Location Localiza; // Objeto do tipo Location, armazena informações de uma localização passadas pela API
    private LocationRequest locationRequest; //Objeto usafp pra fazer equisição pra uma localização
    private boolean istoggled = false;
    private boolean GPSconnect = false;
    private boolean firsttime = true;
    private MqttConnect mqttConnect;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Criando uma instancia da GoogleAPIClient
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Criando uma requisição para localização com os parametros definidos
        createLocationRequest();


        // Passando as referências dos views para as variaveis de classe
        latitude = (TextView) findViewById(R.id.latitudeView);
        longitude = (TextView) findViewById(R.id.longitudeView);
        boton = (Button) findViewById(R.id.button);

        //criando objeto mqtt Connect
        mqttConnect = new MqttConnect(this.getApplicationContext(),handler);

        //conectando
        new Thread(new Runnable() {
            @Override
            public void run() {
                mqttConnect.doConnect();
            }
        }).start();
        

    }

    protected void onRestart() {
        super.onRestart();
        // caso a pessoa saia do app com o app ainda rodando faz
        //com que ele volte a atualizar as localizações ao abrir ele de novo
        if(istoggled)
        {
            googleApiClient.connect();
        }

    }
    protected void onStart() {

        super.onStart();
    }

    protected void onStop() {
        // Desconecta dos serviços ao fechar o app
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Ao conectar aos serviços da google play services, iniciia um serviço de atualização de localização
        startLocationUpdates();
    }

    protected void createLocationRequest() {
        //cria requisição de localização
        locationRequest = new LocationRequest();
        locationRequest.setInterval(2500); // Com 5 segundos de atualização aproximadamente
        locationRequest.setFastestInterval(2500); // mas nunca mais rápido que 5 segundos
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Define a prioridade por localização de alta precisão (GPS)
    }

    protected void startLocationUpdates() {
        // testa se a permissão pra acesso aos serviços de localização foi dada pelo usário
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Exibe um toast caso a permissão para acesso aos serviços de localização não esteja dada
            Toast.makeText(this, "Abilite o acesso do app aos serviços de localização", Toast.LENGTH_LONG).show();
            return;
        }
        // Passa os parâmetros para o serviço de localização para iniciar as atualizações, a api do google,
        // uma requisição de localização e um listener de localização que a atividade implementa
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        // Cada vez que a localização é atualizada
        // a variavel Localiza rece essas informações novas de localização
        Localiza = location;
    }

    protected void updateUI(){
        // N acho que precise explicar isso, atribuo os valores de latitude e longitude aos views da tel.
        latitude.setText(String.valueOf(Localiza.getLatitude()));
        longitude.setText(String.valueOf(Localiza.getLongitude()));
        if(googleApiClient.isConnected()) {
            Toast.makeText(this, "Localização atualizada", Toast.LENGTH_LONG).show();
        }
        firsttime = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void toggle(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Exibe um toast caso a permissão para acesso aos serviços de localização não esteja dada
            Toast.makeText(this, "Abilite o acesso do app aos serviços de localização", Toast.LENGTH_LONG).show();
            return;
        }

        else {

            GPSconnect = !GPSconnect;

            if (GPSconnect) {
                // Conecta aos serviços da google play services com o cliente que criamos no onCreate()
                googleApiClient.connect();
                istoggled = true;
                boton.setText("PARAR");
                update upTodate = new update();
                upTodate.start();

            } else {
                istoggled = false;
                googleApiClient.disconnect();
                boton.setText("INICIAR");
            }

        }

    }



    private class update extends Thread {
        @Override
        public void run() {
            while (istoggled) {
                try {
                    sleep(5 * 1000); // 5 segundos de dormidencia

                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        updateUI(); // Chama a atualização dos views da interface
                    }
                });

            }

        }
    }
}

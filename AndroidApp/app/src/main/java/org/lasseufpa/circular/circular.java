package org.lasseufpa.circular;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzf;

import java.util.Random;

/**
 * Created by alberto on 17/11/2016.
 */

public class Circular {

    private LatLng position;                 //posição do circular
    private Marker marcador;                 //marcador do Maps associado
    private int velocidade;                  //velocidade do circular
    private String nome;                     //nome do circular
    private int currentcircularPoint = 0;    //ponto da rota
    private final int NCircularPoints = 439;
    public int[] stopPoints = {19,43,81,156,230,339};
    private int countStop = 0;

    public Circular (String nome, LatLng local) {
        this.nome = nome;
        this.position = local;
    }

    public Circular () {

    }





    public void setRandomPosition () {

        currentcircularPoint = new Random().nextInt(440);
        this.position = new LatLng(MapsActivity.rotaY[currentcircularPoint],MapsActivity.rotaX[currentcircularPoint]);

    }

    public void updatePosition () {



        if (countStop>0) {
            countStop--;
            return;
        } else {
            for (int i=0;i<6;i++) {
                if (currentcircularPoint==stopPoints[i]) {
                    countStop = 10;
                    i=10;
                }
            }
        }


        this.position = new LatLng(MapsActivity.rotaY[currentcircularPoint], MapsActivity.rotaX[currentcircularPoint]);
        currentcircularPoint = (currentcircularPoint + 1) % NCircularPoints;
        marcador.setPosition(this.position);
       // marcador.setTitle(""+currentcircularPoint);
    }



    public Marker getMarcador() {
        return marcador;
    }

    public void setMarcador(Marker marcador) {
        this.marcador = marcador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }


}

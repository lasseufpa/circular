package org.lasseufpa.circular;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.eclipse.paho.client.mqttv3.util.Strings;
import org.lasseufpa.circular.Domain.Circular;

import java.util.Objects;

/**
 * Created by alberto on 04/08/2017.
 */

public class CircularBuilderGSM {


    /**
     * monta objeto circular de acordo com o novo padrão de mensagem
     *
     *  +CGNSINF: 1,1,20170606213815.000,-1.464823,-48.445460,25.900,1.74,13.6,1,,4.4,4.5,1.0,,13,3,,,37,,
     */



    public Circular CircularBuild (String message,String nomeCircular) throws IllegalArgumentException {

        String Messages[] = message.split(":");
        String Data[] = Messages[1].split(",");

        Log.i("localização",Messages[0]+Data[3]+Data[4]);

        if (isValidMessage(Messages[0])) {

            String nome = "Circular " + nomeCircular;
            LatLng local = getLoc(Data);

            Log.i("localização",Messages[0]+Data[3]+Data[4]);

            return new Circular(nome,local);

        } else throw new IllegalArgumentException();



    }



    private LatLng getLoc(String[] message) {
        double x,y;
        String valuex = message[3];
        String valuey = message[4];

        //se retornar uma String vazia o numero não é valido
        if (valuex.equals("")||valuex.equals("")) {
            throw new IllegalArgumentException();
        }

        try {
            valuex = valuex.replace(",",".");
            valuey = valuey.replace(",",".");
            x=Double.parseDouble(valuex);
            y=Double.parseDouble(valuey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();

        }



    LatLng local = new LatLng(x,y);
        return local;
    }

    private boolean isValidMessage (String preamble) {
        if (preamble.equals("+CGNSINF")||preamble.equals("+CGNSINF+CGNSINF")) {
            return true;
        } else return false;

    }





}

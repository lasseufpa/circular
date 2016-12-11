package org.lasseufpa.circular;

import com.google.android.gms.maps.model.LatLng;



/**
 * Created by alberto on 24/11/2016.
 * esta classe recebe uma String e transforma em um objeto circular
 */

public class CircularBuilder {



    //Formato da mensagem { #NCircular01*#X-4.508526*#Y-56.985413516*}

    public Circular CircularBuild (String message) throws IllegalArgumentException {

        String nome = identName(message);
        LatLng local = identLoc(message);

        return new Circular(nome,local);

    }



    private String identName(String message) throws IllegalArgumentException {
        String name = getVar(message,'N');

        //se retornar uma String vazia o nome não é valido
        if (name.equals("")) {
           throw new IllegalArgumentException();
        }

        return name;
    }

    private LatLng identLoc(String message) throws IllegalArgumentException {

        double x,y;
        String valuex = getVar(message,'X');
        String valuey = getVar(message,'Y');

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


    //retorna o valor de uma tag especificada #Tag
    private String getVar(String message, char ident) {

        int n = message.length();
        int index;
        String mess = "";

        for (int i=0; i<n; i++) {

            if (message.charAt(i)=='#'&&message.charAt(i+1)==ident) {
                index = i+2;

                for (int j=index;j<n;j++) {

                    if (message.charAt(j)=='*') {
                        j=i=n;
                        break;
                    }

                    mess = mess+message.charAt(j);

                }


            }

        }

        return mess;
    }


}

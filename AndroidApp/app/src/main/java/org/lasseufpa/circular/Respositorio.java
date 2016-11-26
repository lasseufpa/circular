package org.lasseufpa.circular;

import java.util.ArrayList;

/**
 * Created by alberto on 24/11/2016.
 */

public class Respositorio {

    private  ArrayList<Circular> circularesList;


    public Respositorio () {

        circularesList = new ArrayList<>();
    }

    //salva um circular
    public void salvarCircular (Circular circ) {
        //loop para saber se o circular com o mesmo nome já está armazenado
        for (Circular currentC : circularesList)
            if (currentC.getNome().equals(circ.getNome())) {
                currentC.setPosition(circ.getPosition());
                return;
            }
        //se não encontrar nenhum adiciona na ultima posição
        circularesList.add(circ);
    }

    public ArrayList<Circular> getCircularList () {
        return circularesList;
    }

    //pega circular pelo nome
    public Circular getCircular (String nome) {
        for (Circular currentC : circularesList) {
            if (currentC.getNome().equals(nome)) {
                return currentC;
            }

        }
        return null;
    }





}

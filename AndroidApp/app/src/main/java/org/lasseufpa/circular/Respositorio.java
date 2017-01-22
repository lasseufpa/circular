package org.lasseufpa.circular;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alberto on 24/11/2016.
 */

public class Respositorio {

    private  ArrayList<Circular> circularesList;
    private long  timeToObsolete = 30000; //tempo para informação obsoleta
    private long  timeToErase = 60000;    //tempo para informação ser apagada



    public Respositorio () {

        circularesList = new ArrayList<>();
    }

    //salva um circular
    public void salvarCircular (Circular circ) {

        circ.setRecivedTime(Calendar.getInstance()); //salva o instante de captura da localização

        //loop para saber se o circular com o mesmo nome já está armazenado
        for (Circular currentC : circularesList)
            if (currentC.getNome().equals(circ.getNome())) {
                currentC.setPosition(circ.getPosition());
                currentC.setRecivedTime(circ.getRecivedTime());
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

    /**
     * Atualiza a lista de circulares e marca informações antigas
     */
    public void UpdateCircularList () {

        for (Circular currentC : circularesList) {
            long timeCurrentC = currentC.getRecivedTime().getTimeInMillis();
            long timePassed = Calendar.getInstance().getTimeInMillis() - timeCurrentC;


            if (timePassed>timeToErase) {
                currentC.setErase(true);
            } else if (timePassed>timeToObsolete) {
                currentC.setObsolet(true);
            } else {
                currentC.setObsolet(false);
            }


        }


    }

    /**
     * remove todos os marcadores da lista de circulares
     */
    public void removeAllCircularMarks() {
        for (Circular currentC : circularesList) {
            currentC.setMarcador(null);
        }

    }





}

package org.lasseufpa.circular;

import android.util.Log;

import org.lasseufpa.circular.Domain.Circular;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alberto on 24/11/2016.
 */

public class RepositorioCircular {

    private  ArrayList<Circular> circularesList;
    private ArrayList<RepositorioCircularChangeListener> listenersList;
    private long  timeToObsolete = 30000; //tempo para informação obsoleta
    private long  timeToErase = 60000;    //tempo para informação ser apagada
    private Calendar lastUpdate = Calendar.getInstance();



    public RepositorioCircular() {
        circularesList = new ArrayList<>();
        listenersList = new ArrayList<>();
        lastUpdate = Calendar.getInstance();
    }


    public boolean isUpdated(Calendar timeUpdated) {
        return (lastUpdate.getTimeInMillis()-timeUpdated.getTimeInMillis())<=0;
    }


    //salva um circular
    public void saveCircular(Circular circ) {

        Log.i("depura"," circular " + circ.getNome() +  "adicionado");


        lastUpdate = Calendar.getInstance(); //registra o tempo de atualização
        circ.setRecivedTime(Calendar.getInstance()); //salva o instante de captura da localização

        //loop para saber se o circular com o mesmo nome já está armazenado
        for (Circular currentC : circularesList)
            if (currentC.getNome().equals(circ.getNome())) {
                currentC.setPosition(circ.getPosition());
                currentC.setRecivedTime(circ.getRecivedTime());
                notifyChange();
                return;
            }
        //se não encontrar nenhum verifica se já está no limite
        if (activeCircularesNumber()<10) {
            circularesList.add(circ);
        } else {
            eraseLastCircular();
            circularesList.add(circ);
        }

        notifyChange();

    }

    public ArrayList<Circular> getCircularList () {
        return circularesList;
    }

    public ArrayList<Circular> getCircularcopyList () {
        ArrayList<Circular> circ = new ArrayList<>();
        for (Circular c : circularesList) {
            circ.add(c);
        }
        return circ;
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

        Circular circularForErase=null;

        for (Circular currentC : circularesList) {
            long timeCurrentC = currentC.getRecivedTime().getTimeInMillis();
            long timePassed = Calendar.getInstance().getTimeInMillis() - timeCurrentC;


            if (timePassed>timeToErase) {
                if (!currentC.isErase()) lastUpdate = Calendar.getInstance();
                currentC.setErase(true);
                circularForErase = currentC;


            } else if (timePassed>timeToObsolete) {

                if (!currentC.isObsolet()) {
                    lastUpdate = Calendar.getInstance();
                    notifyChange();
                }
                currentC.setObsolet(true);

            } else {
                if (currentC.isObsolet()) {
                    lastUpdate = Calendar.getInstance();
                    notifyChange();
                }
                currentC.setObsolet(false);

            }
        }

        if (circularForErase!=null) {
            circularesList.remove(circularForErase);
            notifyChange();
        }


    }

    private void eraseLastCircular() {

        Circular lastC = new Circular();
        lastC.setRecivedTime(Calendar.getInstance());
        long maxtime = 0;
        for (Circular currentC : circularesList) {
            long timeCurrentC = currentC.getRecivedTime().getTimeInMillis();
            long timePassed = Calendar.getInstance().getTimeInMillis() - timeCurrentC;

            if (timePassed>maxtime) {
                maxtime = timePassed;
                lastC = currentC;
            }
        }


        circularesList.remove(lastC);

    }

    public int activeCircularesNumber() {
        int retorno = 0;
        for (Circular currentC : circularesList) {
            retorno ++;
        }
        return retorno;
    }

    public void setRepositorioCircularChangeListener(RepositorioCircularChangeListener listener)  {
        listenersList.add(listener);
    }

    public void removeRepositorioCircularChangeListener(RepositorioCircularChangeListener listener) {
        listenersList.remove(listener);
    }

    public void notifyChange() {


        for (RepositorioCircularChangeListener listener : listenersList) {
            listener.onRepositorioCircularChanged();
            Log.i("depura"," notifiquei um listener");
        }

    }


    public interface RepositorioCircularChangeListener {
        public void onRepositorioCircularChanged();

    }



}

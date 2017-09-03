package org.lasseufpa.circular.Domain;

/**
 * Created by alberto on 03/04/2017.
 */

public class Parada {

    private double x;
    private double y;

    private String title;
    private String description;
    private final int nParada;
    private boolean IsCircularHere;

    public Parada (int nParada) {
        this.nParada = nParada;
    }

    public boolean isCircularHere() {
        return IsCircularHere;
    }

    public void setCircularHere(boolean circularHere) {
        IsCircularHere = circularHere;
    }

    public int getnParada() {
        return nParada;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

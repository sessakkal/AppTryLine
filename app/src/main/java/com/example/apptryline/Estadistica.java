package com.example.apptryline;

public class Estadistica {
    private String titulo;
    private int datoFavor;
    private int datoContra;
    private int max;
    private int progreso;

    public Estadistica() {
        // Constructor vacío necesario para Firebase
    }

    public Estadistica(String titulo, int datoFavor, int datoContra, int max, int progreso) {
        this.titulo = titulo;
        this.datoFavor = datoFavor;
        this.datoContra = datoContra;
        this.max = max;
        this.progreso = progreso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDatoFavor() {
        return datoFavor;
    }

    public void setDatoFavor(int datoFavor) {
        this.datoFavor = datoFavor;
    }

    public int getDatoContra() {
        return datoContra;
    }

    public void setDatoContra(int datoContra) {
        this.datoContra = datoContra;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }
}

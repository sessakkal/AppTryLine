package com.example.apptryline;

public class Estadistica {
    private String titulo;
    private int datoFavor;
    private int datoContra;
    private int progresoFavor;
    private int progresoContra;
    private int max;

    // Constructor sin argumentos necesario para Firebase
    public Estadistica() {
    }

    // Constructor con argumentos
    public Estadistica(String titulo, int datoFavor, int datoContra, int max, int progresoFavor, int progresoContra) {
        this.titulo = titulo;
        this.datoFavor = datoFavor;
        this.datoContra = datoContra;
        this.max = max;
        this.progresoFavor = progresoFavor;
        this.progresoContra = progresoContra;
    }

    // Getters y setters...
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

    public int getProgresoFavor() {
        return progresoFavor;
    }

    public void setProgresoFavor(int progresoFavor) {
        this.progresoFavor = progresoFavor;
    }

    public int getProgresoContra() {
        return progresoContra;
    }

    public void setProgresoContra(int progresoContra) {
        this.progresoContra = progresoContra;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // Nuevo método setProgreso
    public void setProgreso(int progresoFavor, int progresoContra) {
        this.progresoFavor = progresoFavor;
        this.progresoContra = progresoContra;
    }
}



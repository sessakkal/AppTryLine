package com.example.apptryline;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PartidoDatos {

    private String id;
    private Date fecha;
    private String horaInicio;
    private String coordenadas;
    private String ubicacionTexto;
    private String equipoLocal;
    private String equipoVisitante;
    private String resultado;
    private int melesAFavor;
    private int melesEnContra;
    private int triesAFavor;
    private int triesEnContra;

    // Constructor vacío necesario para Firebase
    public PartidoDatos() {}

    // Constructor con parámetros
    public PartidoDatos(String id, Date fecha, String horaInicio, String ubicacionTexto, String equipoLocal, String equipoVisitante, String resultado, int melesAFavor, int melesEnContra, int triesAFavor, int triesEnContra) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.ubicacionTexto = ubicacionTexto;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.resultado = resultado;
        this.melesAFavor = melesAFavor;
        this.melesEnContra = melesEnContra;
        this.triesAFavor = triesAFavor;
        this.triesEnContra = triesEnContra;
    }

    public String getDescripcion() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaString = dateFormat.format(fecha);
        return equipoLocal + " vs " + equipoVisitante + " - " + fechaString + " " + horaInicio;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getUbicacionTexto() {
        return ubicacionTexto;
    }

    public void setUbicacionTexto(String ubicacionTexto) {
        this.ubicacionTexto = ubicacionTexto;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getMelesAFavor() {
        return melesAFavor;
    }

    public void setMelesAFavor(int melesAFavor) {
        this.melesAFavor = melesAFavor;
    }

    public int getMelesEnContra() {
        return melesEnContra;
    }

    public void setMelesEnContra(int melesEnContra) {
        this.melesEnContra = melesEnContra;
    }

    public int getTriesAFavor() {
        return triesAFavor;
    }

    public void setTriesAFavor(int triesAFavor) {
        this.triesAFavor = triesAFavor;
    }

    public int getTriesEnContra() {
        return triesEnContra;
    }

    public void setTriesEnContra(int triesEnContra) {
        this.triesEnContra = triesEnContra;
    }
}

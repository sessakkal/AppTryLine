package com.example.apptryline;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PartidoDatos {

    private String id;
    private Date fecha;
    private String horaInicio;
    private String ubicacionTexto;
    private String equipoLocal;
    private String equipoVisitante;
    private String resultadolocal;
    private String resultadovisitante;

    // Constructor vacío necesario para Firebase
    public PartidoDatos() {}

    // Constructor con parámetros
    public PartidoDatos(String id, Date fecha, String horaInicio, String ubicacionTexto, String equipoLocal, String equipoVisitante, String resultadoLocal, String resultadoVisitante) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.ubicacionTexto = ubicacionTexto;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.resultadolocal = resultadoLocal;
        this.resultadovisitante = resultadoVisitante;
    }

    public String getResultadolocal() {
        return resultadolocal;
    }

    public void setResultadolocal(String resultadolocal) {
        this.resultadolocal = resultadolocal;
    }

    public String getResultadovisitante() {
        return resultadovisitante;
    }

    public void setResultadovisitante(String resultadovisitante) {
        this.resultadovisitante = resultadovisitante;
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
}
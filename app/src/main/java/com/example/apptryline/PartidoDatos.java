package com.example.apptryline;

public class PartidoDatos {
    private String id;
    private String fecha;
    private String horaInicio;
    private String coordenadas;
    private String ubicacionTexto;
    private String equipoLocal;
    private String equipoVisitante;

    public PartidoDatos() {
        // Constructor vacío requerido para Firebase
    }

    public PartidoDatos(String id, String fecha, String horaInicio, String coordenadas,
                   String ubicacionTexto, String equipoLocal, String equipoVisitante) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.coordenadas = coordenadas;
        this.ubicacionTexto = ubicacionTexto;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }
// Getters y setters
}

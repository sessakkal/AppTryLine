package com.example.apptryline;

public class PartidoDatos {
    private String nombre;
    private String fecha;
    private String horaInicio;
    private String coordenadas;
    private String ubicacionTexto;
    private String equipoLocal;
    private String equipoVisitante;

    public PartidoDatos() {
        // Constructor vacío requerido para Firebase
    }

    public PartidoDatos(String nombre, String fecha, String horaInicio, String coordenadas,
                   String ubicacionTexto, String equipoLocal, String equipoVisitante) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.coordenadas = coordenadas;
        this.ubicacionTexto = ubicacionTexto;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }

    // Getters y setters
}

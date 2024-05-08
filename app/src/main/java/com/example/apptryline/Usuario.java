package com.example.apptryline;

public class Usuario {
    private String nombreEquipo;
    private String correoElectronico;
    private String nombreUsuario;

    // Constructor vacío requerido por Firebase
    public Usuario() {
    }

    public Usuario(String nombreEquipo, String correoElectronico, String nombreUsuario) {
        this.nombreEquipo = nombreEquipo;
        this.correoElectronico = correoElectronico;
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}

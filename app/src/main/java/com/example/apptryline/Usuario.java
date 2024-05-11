package com.example.apptryline;

public class Usuario {
    private String correoElectronico;
    private String nombreUsuario;
    private String nombre;
    private boolean admin;

    // Constructor vacío requerido por Firebase
    public Usuario() {
    }

    // Constructor para inicializar correo electrónico, nombre de usuario y nombre
    public Usuario(String correoElectronico, String nombreUsuario, String nombre) {
        this.correoElectronico = correoElectronico;
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
    }

    // Getters y setters
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

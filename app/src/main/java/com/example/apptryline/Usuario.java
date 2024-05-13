// Usuario.java
package com.example.apptryline;

public class Usuario {
    private String correoElectronico;
    private String nombreUsuario;
    private String nombre;
    private boolean admin;
    private String equipoId; // ID del equipo al que pertenece el usuario

    public Usuario() {
        // Constructor vacío requerido por Firebase
    }

    public Usuario(String correoElectronico, String nombreUsuario, String nombre, boolean admin, String equipoId) {
        this.correoElectronico = correoElectronico;
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.admin = admin;
        this.equipoId = equipoId;
    }

    // Getters y setters

    // toString() para facilitar la lectura y escritura de datos en Firebase
    @Override
    public String toString() {
        return "Usuario{" +
                "correoElectronico='" + correoElectronico + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", admin=" + admin +
                ", equipoId='" + equipoId + '\'' +
                '}';
    }
}

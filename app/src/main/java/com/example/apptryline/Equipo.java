package com.example.apptryline;

import java.util.Map;

public class Equipo {
    private String adminId;
    private Map<String, Boolean> miembros;
    private String nombre;

    public Equipo() {
        // Constructor vacío requerido por Firebase
    }

    public Equipo(String adminId, Map<String, Boolean> miembros, String nombre) {
        this.adminId = adminId;
        this.miembros = miembros;
        this.nombre = nombre;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public Map<String, Boolean> getMiembros() {
        return miembros;
    }

    public void setMiembros(Map<String, Boolean> miembros) {
        this.miembros = miembros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

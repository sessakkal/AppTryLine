package com.example.apptryline;

import java.util.HashMap;
import java.util.Map;

public class Equipo {
    private String nombre;
    private String adminId;
    private Map<String, Boolean> miembros;

    public Equipo() {
        // Constructor vacío requerido por Firebase
    }

    public Equipo(String nombre, String adminId) {
        this.nombre = nombre;
        this.adminId = adminId;
        this.miembros = new HashMap<>();
        // Agregar al administrador como miembro
        this.miembros.put(adminId, true);
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
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

    // Método para agregar un nuevo miembro al equipo
    public void agregarMiembro(String memberId) {
        miembros.put(memberId, true);
    }

    // Método para remover un miembro del equipo
    public void removerMiembro(String memberId) {
        miembros.remove(memberId);
    }

    // Otros métodos útiles, como toString(), equals(), hashCode(), etc.
}

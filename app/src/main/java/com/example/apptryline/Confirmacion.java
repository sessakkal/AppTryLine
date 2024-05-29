package com.example.apptryline;

public class Confirmacion {

    private String usuario;
    private String comentario;

    public Confirmacion() {
    }

    public Confirmacion(String usuario, String comentario) {
        this.usuario = usuario;
        this.comentario = comentario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

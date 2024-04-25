package com.example.apptryline;

public class Usuarios {
    String usuario, mail;
    boolean admin;
    public Usuarios() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Usuarios(String usuario, String mail, boolean admin) {
        this.usuario = usuario;
        this.mail = mail;
        this.admin = admin;
    }
}

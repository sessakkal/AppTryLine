package com.example.apptryline;

public class ModelChat {

    private String mensaje;
    private String idUsuarioEnvia;
    private String idUsuarioRecibe;
    private String timestamp;
    private boolean mensajeVisto;

    public ModelChat() {
        // Constructor vacío necesario para la deserialización de Firebase
    }

    public ModelChat(String mensaje, String idUsuarioEnvia, String idUsuarioRecibe, String timestamp, boolean mensajeVisto) {
        this.mensaje = mensaje;
        this.idUsuarioEnvia = idUsuarioEnvia;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.timestamp = timestamp;
        this.mensajeVisto = mensajeVisto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getIdUsuarioEnvia() {
        return idUsuarioEnvia;
    }

    public void setIdUsuarioEnvia(String idUsuarioEnvia) {
        this.idUsuarioEnvia = idUsuarioEnvia;
    }

    public String getIdUsuarioRecibe() {
        return idUsuarioRecibe;
    }

    public void setIdUsuarioRecibe(String idUsuarioRecibe) {
        this.idUsuarioRecibe = idUsuarioRecibe;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMensajeVisto() {
        return mensajeVisto;
    }

    public void setMensajeVisto(boolean mensajeVisto) {
        this.mensajeVisto = mensajeVisto;
    }
}

package app.projections;

public class CorreoDto {
    private int idCorreo;
    private String correo;
    private String clienteDni;

    public CorreoDto(int idCorreo, String correo, String clienteDni) {
        this.idCorreo = idCorreo;
        this.correo = correo;
        this.clienteDni = clienteDni;
    }

    public int getIdCorreo() {
        return idCorreo;
    }

    public void setIdCorreo(int idCorreo) {
        this.idCorreo = idCorreo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClienteDni() {
        return clienteDni;
    }

    public void setClienteDni(String clienteDni) {
        this.clienteDni = clienteDni;
    }
    
}

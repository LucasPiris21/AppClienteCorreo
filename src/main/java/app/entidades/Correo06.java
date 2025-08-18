package app.entidades;
import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "correo")
public class Correo06 {
	
    public Correo06() {
        super();
    }
    
    //Mapeo de la tabla correo  //////////////////
    @Id
    @Column(name = "idCorreo", length = 6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCorreo;
    @Column(name = "correo", length = 100, unique=true, nullable=false)
    private String correo;
    
    //////////////////////////////////////////////
    //En el caso que el requerimiernto solicite la relación:
    // un cliente tiene solamente un correo
    //@OneToOne(fetch = FetchType.LAZY)
    /////////////////////////////////////////////////////
    @JoinColumn(name = "cliente06DNIfk", referencedColumnName = "dni", nullable=false)
    
    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.ALL}) //Un cliente muchos correos
    private Cliente06 cliente06=null; //Se creará la columna foránea en la tabla correo.
    //Fin mapeo //////////////////////////////////////
    
    public Correo06(String correo, Cliente06 cliente06) {
        super();
        this.correo = correo;
        this.cliente06 = cliente06; //El cliente relacionado con este correo
    }

    public void setIdCorreo(int idCorreo) {
        this.idCorreo = idCorreo;
    }

    public int getIdCorreo() {
        return idCorreo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCliente06(Cliente06 cliente06) {
        this.cliente06 = cliente06;
    }

    public Cliente06 getCliente06() {
        return cliente06;
    }

    @Override
    public String toString() {
        // TODO Implement this method
        return "id=" + idCorreo + ", Correo=" + correo + "\n";
    }
}

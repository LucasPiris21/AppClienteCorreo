package app.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
@Table(name = "cliente" )
public class Cliente06 {
    
    public Cliente06() {
        super();
    }

    public Cliente06(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    // Mapeo de la tabla cliente ///////////////
    @Column(name="DNI", nullable=false, length=10)
    @Id
    private String dni;
    @Column(name="Nombre", nullable=false, length=50)
    private String nombre;
    @Column(name="Apellido", nullable=false, length=50)
    private String apellido;
    // Fin mapeo //////////////////////////////

    @OneToMany(mappedBy = "cliente06", cascade = CascadeType.ALL)
    private List<Correo06> correos;
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getDni() {
        return dni;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getApellido() {
        return apellido;
    }
    @Override
    public String toString() {
        // TODO Implement this method
        return "id=" + dni + ", Nombre=" + nombre + ", " + apellido + "\n";
    }
}


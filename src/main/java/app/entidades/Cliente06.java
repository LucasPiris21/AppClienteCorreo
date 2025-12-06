package app.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
@Table(name = "cliente" )
public class Cliente06 {
    
    public Cliente06() {
        super();
    }

    public Cliente06(String dni, String nombre, String apellido, LocalDate fechaNacimiento, Nacionalidad nacionalidad) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
    }


    // Mapeo de la tabla cliente ///////////////
    @Column(name="DNI", nullable=false, length=10)
    @Id
    private String dni;
    @Column(name="Nombre", nullable=false, length=50)
    private String nombre;
    @Column(name="Apellido", nullable=false, length=50)
    private String apellido;
    @Column(name="fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    @JoinColumn(name = "nacionalidad", referencedColumnName = "id", nullable=false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Nacionalidad nacionalidad; 
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
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        // TODO Implement this method
        return "id=" + dni + ", Nombre=" + nombre + ", " + apellido + ", FechaNacimiento= " + fechaNacimiento + ", nacionalidad= " + nacionalidad +"\n";
    }
}


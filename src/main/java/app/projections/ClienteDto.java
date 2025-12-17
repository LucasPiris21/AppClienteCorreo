package app.projections;

import java.time.LocalDate;

public class ClienteDto {
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidadId;

    public ClienteDto(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String nacionalidadId) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidadId = nacionalidadId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidadId() {
        return nacionalidadId;
    }

    public void setNacionalidadId(String nacionalidadId) {
        this.nacionalidadId = nacionalidadId;
    }

    
    
}

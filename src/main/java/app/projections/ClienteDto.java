package app.projections;

import java.time.LocalDate;

public class ClienteDto {
    public String dni;
    public String nombre;
    public String apellido;
    public LocalDate fechaNacimiento;
    public String nacionalidadId;

    public ClienteDto(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String nacionalidadId) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidadId = nacionalidadId;
    }

    
}

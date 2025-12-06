package app.projections;

import java.time.LocalDate;

public interface ClienteProjection {
    String getDni();
    String getNombre();
    String getApellido();
    LocalDate getFechaNacimiento();
    String getNacionalidadPais();
}

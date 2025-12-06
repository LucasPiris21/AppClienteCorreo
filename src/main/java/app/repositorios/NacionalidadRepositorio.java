package app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entidades.Nacionalidad;

@Repository
public interface NacionalidadRepositorio extends JpaRepository<Nacionalidad, Integer> {
    
}

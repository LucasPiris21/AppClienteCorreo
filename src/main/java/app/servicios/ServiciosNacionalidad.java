package app.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entidades.Nacionalidad;
import app.repositorios.NacionalidadRepositorio;
import app.requerimientos.RequerimientosCRUD;

@Service
public class ServiciosNacionalidad {
    
    public ServiciosNacionalidad(){
        super();
    }

    @Autowired
    private NacionalidadRepositorio nacionalidadRepositorio;

    public Nacionalidad buscarPorId(String id) {
        int idInt = Integer.valueOf(id);
        return nacionalidadRepositorio.findById(idInt).orElse(new Nacionalidad());
    }

    public List<Nacionalidad> listarTodos() {
        return nacionalidadRepositorio.findAll();
    }

    

}

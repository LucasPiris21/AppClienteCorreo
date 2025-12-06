package app.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entidades.Nacionalidad;
import app.repositorios.NacionalidadRepositorio;
import app.requerimientos.RequerimientosCRUD;

@Service
public class ServiciosNacionalidad implements RequerimientosCRUD<Nacionalidad>{
    
    public ServiciosNacionalidad(){
        super();
    }

    @Autowired
    private NacionalidadRepositorio nacionalidadRepositorio;

    @Override
    public void actualizar(Nacionalidad unaEntidad) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Nacionalidad buscarPorId(String id) {
        int idInt = Integer.valueOf(id);
        return nacionalidadRepositorio.findById(idInt).orElse(new Nacionalidad());
    }

    @Override
    public void eliminar(Nacionalidad unaEntidad) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void eliminarPorId(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean existePorId(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void guardar(Nacionalidad unaEntidad) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Nacionalidad> listarTodos() {
        return nacionalidadRepositorio.findAll();
    }

    

}

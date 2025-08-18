package app.requerimientos;

import java.util.List;

import app.entidades.Correo06;

public interface RequerimientosFuncionesDeNegocio {
	public List<Correo06> listarPorDni(String dni);
	public List<Correo06> listarCorreosConClientes();
	public List<Correo06> listarCorreosConClientesPorDni(String dni);
}

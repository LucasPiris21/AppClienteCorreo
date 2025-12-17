package app.requerimientos;

import java.util.List;

public interface RequerimientosFuncionesDeNegocio {
	public List<?> search(String searchTerm);
	public List<?> listarCorreosConClientes();
}

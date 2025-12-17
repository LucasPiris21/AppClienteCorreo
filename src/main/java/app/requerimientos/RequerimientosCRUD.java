package app.requerimientos;

public interface RequerimientosCRUD<T, D> {
	public void guardar(D unaEntidad);
	public void actualizar(D unaEntidad);
	public T buscarPorId(String id);
	public void eliminarPorId(String id);
	public void eliminar(T unaEntidad);
	public boolean existePorId(String id);
	public java.util.List<?> listarTodos();

}

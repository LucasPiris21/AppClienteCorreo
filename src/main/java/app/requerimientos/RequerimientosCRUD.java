package app.requerimientos;

public interface RequerimientosCRUD<T> {
	public void guardar(T unaEntidad);
	public void actualizar(T unaEntidad);
	public T buscarPorId(String id);
	public void eliminarPorId(String id);
	public void eliminar(T unaEntidad);
	public boolean existePorId(String id);
	public java.util.List<T> listarTodos();

}

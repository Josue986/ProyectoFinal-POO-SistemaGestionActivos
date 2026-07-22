/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOInterface;
import Modelo.Activo;
import java.util.List;
/**
 *
 * @author jotue
 */
public interface ActivoDAO {
    boolean guardar(Activo activo);
    boolean actualizar(Activo activo);
    boolean eliminar(int idActivo);
    List<Activo> obtenerTodos();
    Activo obtenerPorId(int id);
}

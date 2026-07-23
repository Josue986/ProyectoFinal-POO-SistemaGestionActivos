/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOInterface;
import Modelo.RegMantenimiento;
import java.util.List;
/**
 *
 * @author jotue
 */
public interface RegMantenimientoDAO {
    boolean guardar(RegMantenimiento regMantenimiento);
    boolean actualizar(RegMantenimiento regMantenimiento);
    boolean eliminar(int idMantenimiento);
    List<RegMantenimiento> obtenerTodos();
    
    // Método especializado para filtrar mantenimientos por un activo específico
    List<RegMantenimiento> obtenerRegMantenimientosActivo(int idActivo);
    RegMantenimiento obtenerPorId(int idMantenimiento);
}

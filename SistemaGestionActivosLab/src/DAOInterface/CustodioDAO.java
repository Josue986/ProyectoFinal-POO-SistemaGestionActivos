/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOInterface;
import Modelo.Custodio;
import java.util.List;
/**
 *
 * @author jotue
 */
public interface CustodioDAO {
    boolean guardar(Custodio custodio);
    boolean actualizar(Custodio custodio);
    boolean eliminar(int idCustodio);
    List<Custodio> obtenerTodos();
    
    Custodio obtenerPorId(int idCustodio);
}

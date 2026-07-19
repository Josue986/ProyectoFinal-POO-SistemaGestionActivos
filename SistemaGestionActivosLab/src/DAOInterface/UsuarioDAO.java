/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOInterface;
import Modelo.Usuario;
import java.util.List;
/**
 *
 * @author jotue
 */
public interface UsuarioDAO {
    boolean guardar(Usuario usuario);
    boolean actualizar(Usuario usuario);
    boolean eliminar(int idUsuario);
    List<Usuario> obtenerTodos();
}

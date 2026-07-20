package Controlador;

import Vista.Vista;
import Modelo.*;
import DAOInterface.ActivoDAO;
import DAOInterface.RegMantenimientoDAO;
import DAOInterface.UsuarioDAO;
import DAOInterface.CustodioDAO;
import Mantenimiento.CalcularMantenimiento; // Tu interfaz de cálculo
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class InventarioControlador implements ActionListener, CalcularMantenimiento {

    private Vista vista;
    private ActivoDAO activoDAO;
    private RegMantenimientoDAO regMantenimientoDAO;
    private UsuarioDAO usuarioDAO;
    private CustodioDAO custodioDAO;

    // El constructor une la Vista y el DAO
    public InventarioControlador(Vista vista, ActivoDAO activoDAO,
            RegMantenimientoDAO regMantenimientoDAO, UsuarioDAO usuarioDAO,
            CustodioDAO custodioDAO) {
        this.vista = vista;
        this.activoDAO = activoDAO;
        this.regMantenimientoDAO = regMantenimientoDAO;
        this.usuarioDAO = usuarioDAO;
        this.custodioDAO = custodioDAO;

        // Aquí le decimos a los botones que, al ser presionados, 
        // avisen a esta clase (el controlador)
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ¿Qué botón se presionó?
        if (e.getSource() == vista.btnGuardar) {
            registrarActivo();
        } else if (e.getSource() == vista.btnEliminar) {
            eliminarActivo();
        }
    }

    public void registrarActivo() {
        var activo = vista.obtenerDatosFormulario();
        if (activo == null) {
            JOptionPane.showMessageDialog(vista, "Formulario vacío o inválido");
            return;
        }

        if (activoDAO.guardar(activo)) {
            JOptionPane.showMessageDialog(vista, "¡Activo guardado con éxito!");
            listarActivos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el activo");
        }
    }

    public void listarActivos() {
        var lista = activoDAO.obtenerTodos();
        vista.mostrarLista(lista);
    }

    public void actualizarActivo() {
        // Implementar lógica de modificación
        Activo activo = null;
        if (activo != null && activoDAO.actualizar(activo)) {
            JOptionPane.showMessageDialog(vista, "Activo actualizado con éxito");
            listarActivos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el activo");
        }
    }

    public void eliminarActivo() {
        String idSeleccionado = vista.obtenerIdSeleccionado();
        if (idSeleccionado != null) {
            int id = Integer.parseInt(idSeleccionado);
            if (activoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Activo eliminado");
                listarActivos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un activo de la tabla");
        }
    }

    public void cargarDatosParaEditarActivo() {
    }

    // Métodos para Registro de Mantenimientos
    public void registrarRegMantenimiento() {
        //var mantenimiento = vista.obtenerDatosMantenimiento(); // Ajusta al nombre real en tu Vista
        RegMantenimiento mantenimiento = null;
        if (mantenimiento == null) {
            JOptionPane.showMessageDialog(vista, "Formulario de mantenimiento vacío o inválido");
            return;
        }

        if (regMantenimientoDAO.guardar(mantenimiento)) {
            JOptionPane.showMessageDialog(vista, "¡Mantenimiento registrado con éxito!");
            listarRegMantenimientos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el mantenimiento");
        }
    }

    public void listarRegMantenimientos() {
        var lista = regMantenimientoDAO.obtenerTodos();
        //vista.mostrarListaMantenimientos(lista); // Ajusta al método de tu Vista
    }
    
    public void actualizarRegMantenimiento() {
        // TODO: var mantenimiento = vista.obtenerDatosMantenimientoModificado();
        RegMantenimiento mantenimiento = null;
        if (mantenimiento != null && regMantenimientoDAO.actualizar(mantenimiento)) {
            JOptionPane.showMessageDialog(vista, "Mantenimiento actualizado con éxito");
            listarRegMantenimientos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el mantenimiento");
        }
    }

    public void eliminarRegMantenimiento() {
        //String idSeleccionado = vista.obtenerIdMantenimientoSeleccionado();
        String idSeleccionado = null;
        if (idSeleccionado != null) {
            int id = Integer.parseInt(idSeleccionado);
            if (regMantenimientoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Registro de mantenimiento eliminado");
                listarRegMantenimientos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar el mantenimiento");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un registro de mantenimiento de la tabla");
        }
    }

    public void cargarDatosParaEditarMantenimiento() {
        // Lógica para capturar la fila seleccionada y setearla en los campos del formulario
        //var mantenimiento = vista.getMantenimientoSeleccionado();
        Object mantenimiento = null;
        if (mantenimiento != null) {
            //vista.cargarFormularioMantenimiento(mantenimiento);
        }
    }

    // Métodos para Usuarios
    public void registrarUsuario() {
        //var usuario = vista.obtenerDatosUsuario();
        Usuario usuario = null;
        if (usuario == null) {
            JOptionPane.showMessageDialog(vista, "Formulario de usuario vacío o inválido");
            return;
        }

        if (usuarioDAO.guardar(usuario)) {
            JOptionPane.showMessageDialog(vista, "¡Usuario guardado con éxito!");
            listarUsuarios();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el usuario");
        }
    }

    public void listarUsuarios() {
        var lista = usuarioDAO.obtenerTodos();
        //vista.mostrarListaUsuarios(lista);
    }

    public void actualizarUsuario() {
        //var usuario = vista.obtenerDatosUsuarioModificado(); // Debe incluir el ID
        Usuario usuario = null;
        if (usuario != null && usuarioDAO.actualizar(usuario)) {
            JOptionPane.showMessageDialog(vista, "Usuario modificado con éxito");
            listarUsuarios();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al modificar el usuario");
        }
    }

    public void eliminarUsuario() {
        //String idSeleccionado = vista.obtenerIdUsuarioSeleccionado();
        String idSeleccionado = null;
        if (idSeleccionado != null) {
            int id = Integer.parseInt(idSeleccionado);
            if (usuarioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Usuario eliminado");
                listarUsuarios();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar usuario");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla");
        }
    }

    public void cargarDatosParaEditarUsuario() {
        //var usuario = vista.getUsuarioSeleccionado();
        Usuario usuario = null;
        if (usuario != null) {
            //vista.cargarFormularioUsuario(usuario);
        }
    }

    // Métodos para Custodios
    public void registrarCustodio() {
        //var custodio = vista.obtenerDatosCustodio();
        Custodio custodio = null;
        if (custodio == null) {
            //JOptionPane.showMessageDialog(vista, "Formulario de custodio vacío o inválido");
            return;
        }

        if (custodioDAO.guardar(custodio)) {
            JOptionPane.showMessageDialog(vista, "¡Custodio guardado con éxito!");
            listarCustodios();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el custodio");
        }
    }

    public void listarCustodios() {
        var lista = custodioDAO.obtenerTodos();
        //vista.mostrarListaCustodios(lista);
    }

    public void actualizarCustodio() {
        //var custodio = vista.obtenerDatosCustodioModificado();
        Custodio custodio = null;
        if (custodio != null && custodioDAO.actualizar(custodio)) {
            JOptionPane.showMessageDialog(vista, "Custodio modificado con éxito");
            listarCustodios();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al modificar el custodio");
        }
    }

    public void eliminarCustodio() {
        //String idSeleccionado = vista.obtenerIdCustodioSeleccionado();
        String idSeleccionado = null;
        if (idSeleccionado != null) {
            int id = Integer.parseInt(idSeleccionado);
            if (custodioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Custodio eliminado");
                listarCustodios();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar custodio");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un custodio de la tabla");
        }
    }

    public void cargarDatosParaEditarCustodio() {
        //var custodio = vista.getCustodioSeleccionado();
        Custodio custodio = null;
        if (custodio != null) {
           //vista.cargarFormularioCustodio(custodio);
        }
    }

    // --- IMPLEMENTACIÓN DE LA INTERFAZ DE CÁLCULO ---
    @Override
    public double calcularCostoMantenimiento() {
        // Aquí interactúas con regMantenimientoDAO para sumar los costos de la BD
        return 0.0;
    }
    
    @Override
    public double calcularCostoMantenimientoTotal() {
        return 0.0;
    }
}

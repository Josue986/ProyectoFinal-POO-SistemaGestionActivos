package Controlador;

import Vista.Vista;
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

    public void modificarActivo() {
        // Implementar lógica de modificación
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
    }

    public void listarRegMantenimientos() {
    }

    public void eliminarRegMantenimiento() {
    }

    public void cargarDatosParaEditarMantenimiento() {
    }

    // Métodos para Usuarios
    public void registrarUsuario() {
    }

    public void listarUsuarios() {
    }

    public void modificarUsuario() {
    }

    public void eliminarUsuario() {
    }

    public void cargarDatosParaEditarUsuario() {
    }

    // Métodos para Custodios
    public void registrarCustodio() {
    }

    public void listarCustodios() {
    }

    public void modificarCustodio() {
    }

    public void eliminarCustodio() {
    }

    public void cargarDatosParaEditarCustodio() {
    }

    // --- IMPLEMENTACIÓN DE LA INTERFAZ DE CÁLCULO ---
    @Override
    public double calcularCostoMantenimiento() {
        // Aquí interactúas con regMantenimientoDAO para sumar los costos de la BD
        return 0.0;
    }

    public double calcularMantenimientoTotal() {
        return calcularCostoMantenimiento();
    }
}

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        
        // Activos
        this.vista.btnGuardarActivo.addActionListener(this);
        this.vista.btnActualizarActivo.addActionListener(this);
        this.vista.btnEliminarActivo.addActionListener(this);
        
        // Custodios
        this.vista.btnGuardarCustodio.addActionListener(this);
        this.vista.btnActualizarCustodio.addActionListener(this);
        this.vista.btnEliminarCustodio.addActionListener(this);

        // Usuarios
        this.vista.btnGuardarUsuario.addActionListener(this);
        this.vista.btnActualizarUsuario.addActionListener(this);
        this.vista.btnEliminarUsuario.addActionListener(this);

        // Mantenimientos
        this.vista.btnGuardarMantenimiento.addActionListener(this);
        this.vista.btnActualizarMantenimiento.addActionListener(this);
        this.vista.btnEliminarMantenimiento.addActionListener(this);
        this.vista.btnFiltrarMantenimientoActivo.addActionListener(this);
        
        // --- LISTENERS PARA TABLAS (Autocargar al hacer clic) ---
        this.vista.tablaActivos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.cargarFormularioActivoDesdeTabla();
            }
        });

        this.vista.tablaCustodios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.cargarFormularioCustodioDesdeTabla();
            }
        });

        this.vista.tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.cargarFormularioUsuarioDesdeTabla();
            }
        });

        this.vista.tablaMantenimientos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.cargarFormularioMantenimientoDesdeTabla();
            }
        });
        
        // Cargar listas iniciales
        listarActivos();
        listarCustodios();
        listarUsuarios();
        listarRegMantenimientos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        // --- ACTIVOS ---
        if (src == vista.btnGuardarActivo) registrarActivo();
        else if (src == vista.btnActualizarActivo) actualizarActivo();
        else if (src == vista.btnEliminarActivo) eliminarActivo();
        
        // --- CUSTODIOS ---
        else if (src == vista.btnGuardarCustodio) registrarCustodio();
        else if (src == vista.btnActualizarCustodio) actualizarCustodio();
        else if (src == vista.btnEliminarCustodio) eliminarCustodio();

        // --- USUARIOS ---
        else if (src == vista.btnGuardarUsuario) registrarUsuario();
        else if (src == vista.btnActualizarUsuario) actualizarUsuario();
        else if (src == vista.btnEliminarUsuario) eliminarUsuario();

        // --- MANTENIMIENTOS ---
        else if (src == vista.btnGuardarMantenimiento) registrarRegMantenimiento();
        else if (src == vista.btnActualizarMantenimiento) actualizarRegMantenimiento();
        else if (src == vista.btnEliminarMantenimiento) eliminarRegMantenimiento();
        else if (src == vista.btnFiltrarMantenimientoActivo) listarRegMantenimientosPorActivo();
    }

    public void registrarActivo() {
        try {
            var activo = vista.obtenerDatosFormulario();
            if (activo == null) {
                JOptionPane.showMessageDialog(vista, "Formulario vacío o inválido");
            return;
            }

            if (activoDAO.guardar(activo)) {
                JOptionPane.showMessageDialog(vista, "Activo guardado con éxito!");
                listarActivos();
                vista.limpiarFormularioActivo();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar el activo");
            }
        } catch (Exception ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(vista, "Excepción al guardar: " + ex.getMessage());
    }
    }

    public void listarActivos() {
        vista.mostrarLista(activoDAO.obtenerTodos());
    }

    public void actualizarActivo() {
        Activo activo = vista.obtenerDatosFormulario();
        if (activo != null && !vista.txtIdActivo.getText().trim().isEmpty()) {
            activo.setIdActivo(Integer.parseInt(vista.txtIdActivo.getText().trim()));
            if (activoDAO.actualizar(activo)) {
                JOptionPane.showMessageDialog(vista, "Activo actualizado con éxito");
                listarActivos();
                vista.limpiarFormularioActivo();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al actualizar o active/seleccione un registro");
    }

    public void eliminarActivo() {
        String idSeleccionado = vista.obtenerIdActivoSeleccionado();
        if (idSeleccionado != null) {
            int id = Integer.parseInt(idSeleccionado);
            if (activoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Activo eliminado");
                listarActivos();
                vista.limpiarFormularioActivo();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un activo de la tabla");
        }
    }

    // Métodos para Registro de Mantenimientos
    public void registrarRegMantenimiento() {
        RegMantenimiento mantenimiento = vista.obtenerDatosMantenimiento();
        if (mantenimiento == null) {
            JOptionPane.showMessageDialog(vista, "Formulario de mantenimiento vacío o inválido");
            return;
        }

        if (regMantenimientoDAO.guardar(mantenimiento)) {
            JOptionPane.showMessageDialog(vista, "¡Mantenimiento registrado con éxito!");
            listarRegMantenimientos();
            vista.limpiarFormularioMantenimiento();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el mantenimiento");
        }
    }

    public void listarRegMantenimientos() {
        vista.mostrarListaMantenimientos(regMantenimientoDAO.obtenerTodos());
    }

    
    public void listarRegMantenimientosPorActivo() {
        String idText = vista.txtIdActivoMantenimiento.getText().trim();
        if (!idText.isEmpty()) {
            int idActivo = Integer.parseInt(idText);       
            vista.mostrarListaMantenimientos(regMantenimientoDAO.obtenerRegMantenimientosActivo(idActivo));
        } else {
            JOptionPane.showMessageDialog(vista, "Ingrese el ID del activo en el campo correspondiente para filtrar");
        }
    }
    
    public void actualizarRegMantenimiento() {
        RegMantenimiento mantenimiento = vista.obtenerDatosMantenimiento();
        if (mantenimiento != null && !vista.txtIdMantenimiento.getText().trim().isEmpty()) {
            mantenimiento.setIdMantenimiento(Integer.parseInt(vista.txtIdMantenimiento.getText().trim()));
            if (regMantenimientoDAO.actualizar(mantenimiento)) {
                JOptionPane.showMessageDialog(vista, "Mantenimiento actualizado con éxito");
                listarRegMantenimientos();
                vista.limpiarFormularioMantenimiento();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al actualizar o seleccione un mantenimiento");
    }

    public void eliminarRegMantenimiento() {
        String idText = vista.obtenerIdMantenimientoSeleccionado();
        if (idText != null) {
            int id = Integer.parseInt(idText);
            if (regMantenimientoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Registro de mantenimiento eliminado");
                listarRegMantenimientos();
                vista.limpiarFormularioMantenimiento();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar el mantenimiento");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un mantenimiento de la tabla para eliminar");
        }
    }

    // Métodos para Usuarios
    public void registrarUsuario() {
        //var usuario = vista.obtenerDatosUsuario();
        Usuario usuario =vista.obtenerDatosUsuario();
        if (usuario == null) {
            JOptionPane.showMessageDialog(vista, "Formulario de usuario vacío o inválido");
            return;
        }

        if (usuarioDAO.guardar(usuario)) {
            JOptionPane.showMessageDialog(vista, "Usuario guardado con éxito!");
            listarUsuarios();
            vista.limpiarFormularioUsuario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el usuario");
        }
    }

    public void listarUsuarios() {
        vista.mostrarListaUsuarios(usuarioDAO.obtenerTodos());
    }

    public void actualizarUsuario() {     
        Usuario usuario = vista.obtenerDatosUsuario();
        if (usuario != null && !vista.txtIdUsuario.getText().trim().isEmpty()) {
            usuario.setIdUsuario(Integer.parseInt(vista.txtIdUsuario.getText().trim()));
            if (usuarioDAO.actualizar(usuario)) {
                JOptionPane.showMessageDialog(vista, "Usuario modificado con éxito");
                listarUsuarios();
                vista.limpiarFormularioUsuario();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al modificar el usuario");
    }

    public void eliminarUsuario() {
        String idText = vista.obtenerIdUsuarioSeleccionado();
        if (idText != null) {
            int id = Integer.parseInt(idText);
            if (usuarioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Usuario eliminado");
                listarUsuarios();
                vista.limpiarFormularioUsuario();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar usuario");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla");
        }
    }

    // Métodos para Custodios
    public void registrarCustodio() {
        //var custodio = vista.obtenerDatosCustodio();
        Custodio custodio = vista.obtenerDatosCustodio();
        if (custodio == null) {
            //JOptionPane.showMessageDialog(vista, "Formulario de custodio vacío o inválido");
            return;
        }

        if (custodioDAO.guardar(custodio)) {
            JOptionPane.showMessageDialog(vista, "Custodio guardado con éxito!");
            listarCustodios();
            vista.limpiarFormularioCustodio();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el custodio");
        }
    }

    public void listarCustodios() {
        vista.mostrarListaCustodios(custodioDAO.obtenerTodos());
    }

    public void actualizarCustodio() {
        Custodio custodio = vista.obtenerDatosCustodio();
        if (custodio != null && !vista.txtIdCustodio.getText().trim().isEmpty()) {
            custodio.setIdCustodio(Integer.parseInt(vista.txtIdCustodio.getText().trim()));
            if (custodioDAO.actualizar(custodio)) {
                JOptionPane.showMessageDialog(vista, "Custodio modificado con éxito");
                listarCustodios();
                vista.limpiarFormularioCustodio();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al modificar el custodio");
    }

    public void eliminarCustodio() {        
        String idText = vista.obtenerIdCustodioSeleccionado();
        if (idText != null) {
            int id = Integer.parseInt(idText);
            if (custodioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Custodio eliminado");
                listarCustodios();
                vista.limpiarFormularioCustodio();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar custodio");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un custodio de la tabla");
        }
    }

    // --- IMPLEMENTACIÓN DE LA INTERFAZ DE CÁLCULO ---
    @Override
    public double calcularCostoMantenimiento() {
        double costoPreventivoEstimado = 0.0;
        
        if (activoDAO != null && activoDAO.obtenerTodos() != null) {
            for (Activo activo : activoDAO.obtenerTodos()) {
                // Cálculo polimórfico según el tipo de activo
                if (activo instanceof Hardware hw) {
                    // 5% del valor de adquisición por año de uso
                    costoPreventivoEstimado += (hw.getCostoAdquisicion() * 0.05) * hw.getAnniosUso();
                } else if (activo instanceof Periferico p) {
                    // Tarifa fija de $10 anuales por desgaste
                    costoPreventivoEstimado += 10.0 * p.getAnniosUso();
                } else if (activo instanceof Licencia lic) {
                    // Costo preventivo/mantenimiento de un software es su renovación
                    costoPreventivoEstimado += lic.getCostoRenovacion();
                } 
            }
        }
        return costoPreventivoEstimado;
    }

    @Override
    public double calcularCostoMantenimientoTotal() {
        double costoTotal = 0.0;
        if (regMantenimientoDAO != null && regMantenimientoDAO.obtenerTodos() != null) {
            for (RegMantenimiento m : regMantenimientoDAO.obtenerTodos()) {
                costoTotal += m.getCostoMantenimiento();
            }
          }
          return costoTotal;  
    }
}

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        this.vista.tablaVistaGlobal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String idStr = vista.obtenerIdSeleccionadoGlobal();
                String tipoEntidad = vista.obtenerTipoSeleccionadoGlobal();

                if (idStr != null && tipoEntidad != null) {
                    int id = Integer.parseInt(idStr);

                    if (tipoEntidad.startsWith("Activo")) {
                        Activo activoCompleto = activoDAO.obtenerPorId(id);
                        vista.cargarFormularioActivoDesdeTabla(activoCompleto);
                    } else if (tipoEntidad.equals("Custodio")) {
                        Custodio custodio = custodioDAO.obtenerPorId(id);
                        if (custodio != null) {
                            vista.txtIdCustodio.setText(String.valueOf(custodio.getIdCustodio()));
                            vista.txtCedulaCustodio.setText(custodio.getCedula());
                            vista.txtNombreCustodio.setText(custodio.getNombre());
                            vista.txtApellidoCustodio.setText(custodio.getApellido());
                            vista.txtRolCustodio.setText(custodio.getRol());
                        }
                    } else if (tipoEntidad.equals("Mantenimiento")) {
                        RegMantenimiento mant = regMantenimientoDAO.obtenerPorId(id);
                        if (mant != null) {
                            vista.txtIdMantenimiento.setText(String.valueOf(mant.getIdMantenimiento()));
                            vista.txtDetallesMantenimiento.setText(mant.getDetallesMantenimiento());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            vista.txtFechaInicioMantenimiento.setText(mant.getFechaInicio() != null ? sdf.format(mant.getFechaInicio()) : "");
                            vista.txtFechaFinMantenimiento.setText(mant.getFechaFin() != null ? sdf.format(mant.getFechaFin()) : "");
                            vista.txtCostoMantenimiento.setText(String.valueOf(mant.getCostoMantenimiento()));
                            if (mant.getActivo() != null) {
                                vista.txtIdActivoMantenimiento.setText(String.valueOf(mant.getActivo().getIdActivo()));
                            }
                            if (mant.getUsuario() != null) {
                                vista.txtIdUsuarioMantenimiento.setText(String.valueOf(mant.getUsuario().getIdUsuario()));
                            }
                        }
                    }
                }
            }
        });

        // Cargar vista global inicial
        listarVistaGlobal();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // --- ACTIVOS ---
        if (src == vista.btnGuardarActivo) {
            registrarActivo();
        } else if (src == vista.btnActualizarActivo) {
            actualizarActivo();
        } else if (src == vista.btnEliminarActivo) {
            eliminarActivo();
        } // --- CUSTODIOS ---
        else if (src == vista.btnGuardarCustodio) {
            registrarCustodio();
        } else if (src == vista.btnActualizarCustodio) {
            actualizarCustodio();
        } else if (src == vista.btnEliminarCustodio) {
            eliminarCustodio();
        } // --- USUARIOS ---
        else if (src == vista.btnGuardarUsuario) {
            registrarUsuario();
        } else if (src == vista.btnActualizarUsuario) {
            actualizarUsuario();
        } else if (src == vista.btnEliminarUsuario) {
            eliminarUsuario();
        } // --- MANTENIMIENTOS ---
        else if (src == vista.btnGuardarMantenimiento) {
            registrarRegMantenimiento();
        } else if (src == vista.btnActualizarMantenimiento) {
            actualizarRegMantenimiento();
        } else if (src == vista.btnEliminarMantenimiento) {
            eliminarRegMantenimiento();
        } else if (src == vista.btnFiltrarMantenimientoActivo) {
            listarRegMantenimientosPorActivo();
        }
    }

    public void registrarActivo() {
        try {
            var activo = vista.obtenerDatosFormulario();
            if (activo == null) {
                return;
            }

            if (activoDAO.guardar(activo)) {
                JOptionPane.showMessageDialog(vista, "Activo guardado con éxito!");
                listarVistaGlobal();
                vista.limpiarFormularioActivo();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar el activo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Excepción al guardar: " + ex.getMessage());
        }
    }

    public void actualizarActivo() {
        Activo activo = vista.obtenerDatosFormulario();
        if (activo != null && !vista.txtIdActivo.getText().trim().isEmpty()) {
            activo.setIdActivo(Integer.parseInt(vista.txtIdActivo.getText().trim()));
            if (activoDAO.actualizar(activo)) {
                JOptionPane.showMessageDialog(vista, "Activo actualizado con éxito");
                listarVistaGlobal();
                vista.limpiarFormularioActivo();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al actualizar o seleccione un registro");
    }

    public void eliminarActivo() {
        String idSeleccionado = vista.obtenerIdSeleccionadoGlobal();
        String tipo = vista.obtenerTipoSeleccionadoGlobal();
        if (idSeleccionado != null && tipo != null && tipo.startsWith("Activo")) {
            int id = Integer.parseInt(idSeleccionado);
            if (activoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Activo eliminado");
                listarVistaGlobal();
                vista.limpiarFormularioActivo();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un activo válido de la tabla global");
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
            listarVistaGlobal();
            vista.limpiarFormularioMantenimiento();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el mantenimiento");
        }
    }

    public void listarVistaGlobal() {
        List<Object[]> datosGlobales = new ArrayList<>();

        if (activoDAO != null && activoDAO.obtenerTodos() != null) {
            for (Activo a : activoDAO.obtenerTodos()) {
                String custodioNom = (a.getCustodio() != null) ? a.getCustodio().getNombre() + " " + a.getCustodio().getApellido() : "Sin Custodio";
                datosGlobales.add(new Object[]{
                    a.getIdActivo(),
                    "Activo (" + a.getTipoActivo() + ")",
                    a.getNombreActivo(),
                    a.getMarca(),
                    a.getCostoAdquisicion(),
                    a.getEstadoActivo(),
                    custodioNom
                });
            }
        }

        if (custodioDAO != null && custodioDAO.obtenerTodos() != null) {
            for (Custodio c : custodioDAO.obtenerTodos()) {
                datosGlobales.add(new Object[]{
                    c.getIdCustodio(),
                    "Custodio",
                    c.getNombre() + " " + c.getApellido(),
                    "Cédula: " + c.getCedula(),
                    "N/A",
                    c.getRol(),
                    "N/A"
                });
            }
        }

        if (regMantenimientoDAO != null && regMantenimientoDAO.obtenerTodos() != null) {
            for (RegMantenimiento rm : regMantenimientoDAO.obtenerTodos()) {
                String nomActivo = (rm.getActivo() != null) ? rm.getActivo().getNombreActivo() : "N/A";
                datosGlobales.add(new Object[]{
                    rm.getIdMantenimiento(),
                    "Mantenimiento",
                    rm.getDetallesMantenimiento(),
                    "Inicio: " + rm.getFechaInicio(),
                    rm.getCostoMantenimiento(),
                    "Registrado",
                    "Activo: " + nomActivo
                });
            }
        }

        vista.mostrarVistaGlobal(datosGlobales);
    }

    public void listarRegMantenimientosPorActivo() {
        String idText = vista.txtIdActivoMantenimiento.getText().trim();
        if (!idText.isEmpty()) {
            int idActivo = Integer.parseInt(idText);       
            List<RegMantenimiento> lista = regMantenimientoDAO.obtenerRegMantenimientosActivo(idActivo);
            List<Object[]> datosFiltrados = new ArrayList<>();
            for (RegMantenimiento rm : lista) {
                String nomActivo = (rm.getActivo() != null) ? rm.getActivo().getNombreActivo() : "N/A";
                datosFiltrados.add(new Object[]{
                    rm.getIdMantenimiento(),
                    "Mantenimiento",
                    rm.getDetallesMantenimiento(),
                    "Inicio: " + rm.getFechaInicio(),
                    rm.getCostoMantenimiento(),
                    "Registrado",
                    "Activo: " + nomActivo
                });
            }
            vista.mostrarVistaGlobal(datosFiltrados);
        } else {
            JOptionPane.showMessageDialog(vista, "Ingrese el ID del activo para filtrar");
        }
    }

    public void actualizarRegMantenimiento() {
        RegMantenimiento mantenimiento = vista.obtenerDatosMantenimiento();
        if (mantenimiento != null && !vista.txtIdMantenimiento.getText().trim().isEmpty()) {
            mantenimiento.setIdMantenimiento(Integer.parseInt(vista.txtIdMantenimiento.getText().trim()));
            if (regMantenimientoDAO.actualizar(mantenimiento)) {
                JOptionPane.showMessageDialog(vista, "Mantenimiento actualizado con éxito");
                listarVistaGlobal();
                vista.limpiarFormularioMantenimiento();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al actualizar o seleccione un mantenimiento");
    }

    public void eliminarRegMantenimiento() {
        String idText = vista.obtenerIdSeleccionadoGlobal();
        String tipo = vista.obtenerTipoSeleccionadoGlobal();
        if (idText != null && tipo != null && tipo.equals("Mantenimiento")) {
            int id = Integer.parseInt(idText);
            if (regMantenimientoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Registro de mantenimiento eliminado");
                listarVistaGlobal();
                vista.limpiarFormularioMantenimiento();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar el mantenimiento");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un registro de mantenimiento de la tabla global");
        }
    }

    // Métodos para Usuarios
    public void registrarUsuario() {
        Usuario usuario = vista.obtenerDatosUsuario();
        if (usuario == null) {
            JOptionPane.showMessageDialog(vista, "Formulario de usuario vacío o inválido");
            return;
        }

        if (usuarioDAO.guardar(usuario)) {
            JOptionPane.showMessageDialog(vista, "Usuario guardado con éxito!");
            listarVistaGlobal();
            vista.limpiarFormularioUsuario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el usuario");
        }
    }

    public void actualizarUsuario() {
        Usuario usuario = vista.obtenerDatosUsuario();
        if (usuario != null && !vista.txtIdUsuario.getText().trim().isEmpty()) {
            usuario.setIdUsuario(Integer.parseInt(vista.txtIdUsuario.getText().trim()));
            if (usuarioDAO.actualizar(usuario)) {
                JOptionPane.showMessageDialog(vista, "Usuario modificado con éxito");
                listarVistaGlobal();
                vista.limpiarFormularioUsuario();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al modificar el usuario");
    }

    public void eliminarUsuario() {
        String idText = vista.obtenerIdSeleccionadoGlobal();
        if (idText != null) {
            int id = Integer.parseInt(idText);
            if (usuarioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Usuario eliminado");
                listarVistaGlobal();
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
        Custodio custodio = vista.obtenerDatosCustodio();
        if (custodio == null) {
            return;
        }

        if (custodioDAO.guardar(custodio)) {
            JOptionPane.showMessageDialog(vista, "Custodio guardado con éxito!");
            listarVistaGlobal();
            vista.limpiarFormularioCustodio();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el custodio");
        }
    }

    public void actualizarCustodio() {
        Custodio custodio = vista.obtenerDatosCustodio();
        if (custodio != null && !vista.txtIdCustodio.getText().trim().isEmpty()) {
            custodio.setIdCustodio(Integer.parseInt(vista.txtIdCustodio.getText().trim()));
            if (custodioDAO.actualizar(custodio)) {
                JOptionPane.showMessageDialog(vista, "Custodio modificado con éxito");
                listarVistaGlobal();
                vista.limpiarFormularioCustodio();
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Error al modificar el custodio");
    }

    public void eliminarCustodio() {
        String idText = vista.obtenerIdSeleccionadoGlobal();
        String tipo = vista.obtenerTipoSeleccionadoGlobal();
        if (idText != null && tipo != null && tipo.equals("Custodio")) {
            int id = Integer.parseInt(idText);
            if (custodioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Custodio eliminado");
                listarVistaGlobal();
                vista.limpiarFormularioCustodio();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar custodio");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un custodio de la tabla global");
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

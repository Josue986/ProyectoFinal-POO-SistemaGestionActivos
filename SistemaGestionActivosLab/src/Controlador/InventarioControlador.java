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
import java.util.Date;
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
                    } else if (tipoEntidad.equals("Usuario")) {
                        // Buscamos el usuario en la lista general ya que no hay método obtenerPorId en UsuarioDAO
                        Usuario usuEncontrado = null;
                        if (usuarioDAO.obtenerTodos() != null) {
                            for (Usuario uIter : usuarioDAO.obtenerTodos()) {
                                if (uIter.getIdUsuario() == id) {
                                    usuEncontrado = uIter;
                                    break;
                                }
                            }
                        }
                        
                        if (usuEncontrado != null) {
                            vista.txtIdUsuario.setText(String.valueOf(usuEncontrado.getIdUsuario()));
                            if (usuEncontrado.getCustodio() != null) {
                                vista.txtIdCustodioUsuario.setText(String.valueOf(usuEncontrado.getCustodio().getIdCustodio()));
                            } else {
                                vista.txtIdCustodioUsuario.setText("");
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

    // --- CONSTRUCTOR AUXILIAR PRIVADO PARA ACTIVOS (Evita duplicar código entre guardar y actualizar) ---
    private Activo construirActivoDesdeVista() {
        try {
            String idStr = vista.txtIdActivo.getText().trim();
            int idActivo = idStr.isEmpty() ? 0 : Integer.parseInt(idStr);

            String nombre = vista.txtNombreActivo.getText().trim();
            String marca = vista.txtMarcaActivo.getText().trim();
            String subtipo = (String) vista.cbSubtipoActivo.getSelectedItem();
            String estado = vista.txtEstadoActivo.getText().trim();

            if (nombre.isEmpty() || estado.isEmpty()) {
                JOptionPane.showMessageDialog(vista,
                        "Por favor llena los campos obligatorios: Nombre y Estado del Activo.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            String costoStr = vista.txtCostoActivo.getText().trim().replace(",", ".");
            double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);

            int anniosUso = vista.txtAnniosUsoActivo.getText().trim().isEmpty() ? 0 : Integer.parseInt(vista.txtAnniosUsoActivo.getText().trim());

            Custodio custodioAux = null;
            if (!vista.txtIdCustodioActivo.getText().trim().isEmpty()) {
                int idCustodio = Integer.parseInt(vista.txtIdCustodioActivo.getText().trim());
                custodioAux = new Custodio();
                custodioAux.setIdCustodio(idCustodio);
            }

            switch (subtipo) {
                case "CPU":
                    return new Cpu(vista.txtProcesador.getText().trim(), vista.txtRam.getText().trim(), vista.txtAlmacenamiento.getText().trim(), anniosUso, idActivo, nombre, marca, "CPU", costo, estado, custodioAux);
                case "Monitor":
                    return new Monitor(vista.txtResolucion.getText().trim(), vista.txtTasaRefresco.getText().trim(), anniosUso, vista.txtConexionMonitor.getText().trim(), idActivo, nombre, marca, "MONITOR", estado, costo, custodioAux);
                case "Mouse":
                    return new Mouse(vista.txtDpi.getText().trim(), anniosUso, vista.txtConexionMouse.getText().trim(), idActivo, nombre, marca, "MOUSE", costo, estado, custodioAux);
                case "Licencia de Software":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaExp;
                    try {
                        fechaExp = sdf.parse(vista.txtFechaExpiracion.getText().trim());
                    } catch (Exception ex) {
                        fechaExp = new Date();
                    }
                    double costoRenov = 0.0;
                    if (!vista.txtCostoRenovacion.getText().trim().isEmpty()) {
                        costoRenov = Double.parseDouble(vista.txtCostoRenovacion.getText().trim().replace(",", "."));
                    }
                    return new Licencia(fechaExp, costoRenov, idActivo, nombre, marca, "LICENCIA", estado, costo, custodioAux);
                case "Periférico Genérico":
                    return new Periferico(anniosUso, "USB / Genérico", idActivo, nombre, marca, "PERIFERICO", costo, estado, custodioAux);
                case "Hardware Genérico":
                default:
                    return new Hardware(anniosUso, idActivo, nombre, marca, "HARDWARE", costo, estado, custodioAux);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Asegúrate de ingresar valores numéricos válidos en costos, años de uso e IDs.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al construir el activo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void registrarActivo() {
        Activo activo = construirActivoDesdeVista();
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
    }

    public void actualizarActivo() {
        Activo activo = construirActivoDesdeVista();
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
        RegMantenimiento mantenimiento = obtenerDatosMantenimientoDesdeVista();
        if (mantenimiento == null) {
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

    public void actualizarRegMantenimiento() {
        RegMantenimiento mantenimiento = obtenerDatosMantenimientoDesdeVista();
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

    private RegMantenimiento obtenerDatosMantenimientoDesdeVista() {
        try {
            RegMantenimiento rm = new RegMantenimiento();
            rm.setDetallesMantenimiento(vista.txtDetallesMantenimiento.getText().trim());

            String fInicioStr = vista.txtFechaInicioMantenimiento.getText().trim();
            String fFinStr = vista.txtFechaFinMantenimiento.getText().trim();

            if (fInicioStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "La Fecha de Inicio es obligatoria.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            // Formatos flexibles para parsear fecha con o sin hora
            java.text.SimpleDateFormat[] formatos = {
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new java.text.SimpleDateFormat("yyyy-MM-dd")
            };

            Date fechaInicio = null;
            for (java.text.SimpleDateFormat sdf : formatos) {
                try {
                    sdf.setLenient(false);
                    fechaInicio = sdf.parse(fInicioStr);
                    break;
                } catch (java.text.ParseException ignored) {}
            }
            
            if (fechaInicio == null) {
                throw new java.text.ParseException("Formato de fecha de inicio inválido", 0);
            }
            rm.setFechaInicio(fechaInicio);

            if (!fFinStr.isEmpty()) {
                Date fechaFin = null;
                for (java.text.SimpleDateFormat sdf : formatos) {
                    try {
                        sdf.setLenient(false);
                        fechaFin = sdf.parse(fFinStr);
                        break;
                    } catch (java.text.ParseException ignored) {}
                }
                if (fechaFin == null) {
                    throw new java.text.ParseException("Formato de fecha fin inválido", 0);
                }
                rm.setFechaFin(fechaFin);
            } else {
                rm.setFechaFin(null);
            }

            String costoStr = vista.txtCostoMantenimiento.getText().trim().replace(",", ".");
            rm.setCostoMantenimiento(costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr));

            if (!vista.txtIdActivoMantenimiento.getText().trim().isEmpty()) {
                Hardware act = new Hardware();
                act.setIdActivo(Integer.parseInt(vista.txtIdActivoMantenimiento.getText().trim()));
                rm.setActivo(act);
            }

            if (!vista.txtIdUsuarioMantenimiento.getText().trim().isEmpty()) {
                Usuario usu = new Usuario();
                usu.setIdUsuario(Integer.parseInt(vista.txtIdUsuarioMantenimiento.getText().trim()));
                rm.setUsuario(usu);
            }

            return rm;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Error en los campos numéricos (Costo, ID Activo o ID Usuario).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "El formato de la fecha debe ser estrictamente YYYY-MM-DD (Ej: 2026-07-23).", "Error en Formato de Fecha", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Métodos para Usuarios
    public void registrarUsuario() {
        Usuario usuario = obtenerDatosUsuarioDesdeVista();
        if (usuario == null) {
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
        Usuario usuario = obtenerDatosUsuarioDesdeVista();
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

    private Usuario obtenerDatosUsuarioDesdeVista() {
        try {
            Usuario u = new Usuario();
            if (!vista.txtIdCustodioUsuario.getText().trim().isEmpty()) {
                int idCustodio = Integer.parseInt(vista.txtIdCustodioUsuario.getText().trim());
                Custodio custodioAux = new Custodio();
                custodioAux.setIdCustodio(idCustodio);
                u.setCustodio(custodioAux); // O el método correspondiente en tu modelo Usuario para asociar el custodio
            }
            return u;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al leer datos del usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Métodos para Custodios
    public void registrarCustodio() {
        Custodio custodio = obtenerDatosCustodioDesdeVista();
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
        Custodio custodio = obtenerDatosCustodioDesdeVista();
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

    private Custodio obtenerDatosCustodioDesdeVista() {
        try {
            int idCustodio = 0;
            if (!vista.txtIdCustodio.getText().trim().isEmpty()) {
                idCustodio = Integer.parseInt(vista.txtIdCustodio.getText().trim());
            }

            String cedula = vista.txtCedulaCustodio.getText().trim();
            String nombre = vista.txtNombreCustodio.getText().trim();
            String apellido = vista.txtApellidoCustodio.getText().trim();
            String rol = vista.txtRolCustodio.getText().trim();

            if (cedula.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Cédula y Nombre del custodio son obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return new Custodio(idCustodio, cedula, nombre, apellido, rol);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID del custodio debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // --- LISTADOS GLOBALES ---
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

        if (usuarioDAO != null && usuarioDAO.obtenerTodos() != null) {
            for (Usuario u : usuarioDAO.obtenerTodos()) {
                String custodioAsociado = (u.getCustodio() != null) ? u.getCustodio().getNombre() + " " + u.getCustodio().getApellido() : "Sin Custodio";
                datosGlobales.add(new Object[]{
                    u.getIdUsuario(),
                    "Usuario",
                    "Usuario del Sistema",
                    "ID Custodio: " + (u.getCustodio() != null ? u.getCustodio().getIdCustodio() : "N/A"),
                    "N/A",
                    "Activo",
                    custodioAsociado
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

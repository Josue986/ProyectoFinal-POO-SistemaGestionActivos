package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Modelo.*;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import DAOImpl.CustodioDAOImpl;
import java.awt.event.ActionListener;

public class Vista extends JFrame {

    //--- Pestaña Global (Tabla Unificada Central) ----
    public javax.swing.JTable tablaVistaGlobal;
    public javax.swing.table.DefaultTableModel modeloTablaGlobal;

    //--- Pestaña 1: ACTIVOS ----
    public JTextField txtIdActivo, txtNombreActivo, txtMarcaActivo, txtCostoActivo, txtEstadoActivo, txtIdCustodioActivo, txtAnniosUsoActivo;
    public JComboBox<String> cbCategoriaActivo, cbSubtipoActivo;

    // Campos dinámicos
    // CPU
    public JTextField txtProcesador, txtRam, txtAlmacenamiento;
    // Monitor
    public JTextField txtResolucion, txtTasaRefresco, txtConexionMonitor;
    // Mouse
    public JTextField txtDpi, txtConexionMouse;
    // Licencia
    public JTextField txtFechaExpiracion, txtCostoRenovacion;

    private CardLayout cardLayoutEspecifico;
    private JPanel panelFormularioDinamico;

    public JButton btnGuardarActivo, btnActualizarActivo, btnEliminarActivo;

    //--- Pestaña 2: CUSTODIOS ---
    public JTextField txtIdCustodio, txtCedulaCustodio, txtNombreCustodio, txtApellidoCustodio, txtRolCustodio;
    public JButton btnGuardarCustodio, btnActualizarCustodio, btnEliminarCustodio;

    //--- Pestaña 3: USUARIOS ---
    public JTextField txtIdUsuario, txtIdCustodioUsuario;
    public JButton btnGuardarUsuario, btnActualizarUsuario, btnEliminarUsuario;

    //--- Pestaña 4: MANTENIMIENTOS ---
    public JTextField txtIdMantenimiento, txtDetallesMantenimiento, txtFechaInicioMantenimiento, txtFechaFinMantenimiento, txtCostoMantenimiento, txtIdActivoMantenimiento, txtIdUsuarioMantenimiento;
    public JButton btnGuardarMantenimiento, btnActualizarMantenimiento, btnEliminarMantenimiento, btnFiltrarMantenimientoActivo;

    public Vista() {
        // Configuramos la ventana básica
        this.setTitle("Sistema de Gestión de Activos e Inventarios - UTPL");
        this.setSize(1050, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane panelPestañas = new JTabbedPane();

        panelPestañas.addTab("Vista Global / Consolidada", crearPanelVistaGlobal());
        panelPestañas.addTab("Activos", crearPanelActivos());
        panelPestañas.addTab("Custodios", crearPanelCustodios());
        panelPestañas.addTab("Usuarios", crearPanelUsuarios());
        panelPestañas.addTab("Mantenimientos", crearPanelMantenimientos());

        this.add(panelPestañas);
    }

    // PANEL VISTA GLOBAL | CONSOLIDADA
    private JPanel crearPanelVistaGlobal() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Información Consolidada de la Tabla Unificada"));
        panelSuperior.add(new JLabel("Aquí se visualizan todos los registros activos del sistema de manera centralizada."));

        modeloTablaGlobal = new DefaultTableModel(
                new String[]{"ID Registro", "Tipo de Entidad", "Nombre / Detalle Principal", "Marca / Atributo", "Costo / Valor", "Estado / Rol", "Custodio Asociado"}, 0
        );
        tablaVistaGlobal = new JTable(modeloTablaGlobal);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaVistaGlobal), BorderLayout.CENTER);

        return panel;
    }

    //  PANEL ACTIVOS | CONSTRUCCIÓN DE PANELES (UI) DINÁMICO CON CARDLAYOUT
    private JPanel crearPanelActivos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new GridLayout(8, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos Generales del Activo"));

        txtIdActivo = new JTextField();
        txtIdActivo.setEditable(false);
        txtNombreActivo = new JTextField();
        txtMarcaActivo = new JTextField();
        txtCostoActivo = new JTextField();
        txtEstadoActivo = new JTextField();
        txtIdCustodioActivo = new JTextField();
        txtAnniosUsoActivo = new JTextField("0");

        cbCategoriaActivo = new JComboBox<>(new String[]{"Hardware", "Periférico", "Licencia"});
        cbSubtipoActivo = new JComboBox<>();

        cbCategoriaActivo.addActionListener(e -> actualizarSubtipos());
        cbSubtipoActivo.addActionListener(e -> cambiarPanelEspecifico());

        panelForm.add(new JLabel("ID Activo:"));
        panelForm.add(txtIdActivo);
        panelForm.add(new JLabel("Categoría Principal:"));
        panelForm.add(cbCategoriaActivo);
        panelForm.add(new JLabel("Subtipo Específico:"));
        panelForm.add(cbSubtipoActivo);
        panelForm.add(new JLabel("Nombre Activo:"));
        panelForm.add(txtNombreActivo);
        panelForm.add(new JLabel("Marca:"));
        panelForm.add(txtMarcaActivo);
        panelForm.add(new JLabel("Costo Adquisición ($):"));
        panelForm.add(txtCostoActivo);
        panelForm.add(new JLabel("Años de Uso:"));
        panelForm.add(txtAnniosUsoActivo);
        panelForm.add(new JLabel("Estado:"));
        panelForm.add(txtEstadoActivo);

        cardLayoutEspecifico = new CardLayout();
        panelFormularioDinamico = new JPanel(cardLayoutEspecifico);
        panelFormularioDinamico.setBorder(BorderFactory.createTitledBorder("Atributos Especializados del Subtipo"));

        panelFormularioDinamico.add(crearPanelCpu(), "CPU");
        panelFormularioDinamico.add(crearPanelHardwareGen(), "HARDWARE_GENERICO");
        panelFormularioDinamico.add(crearPanelMonitor(), "MONITOR");
        panelFormularioDinamico.add(crearPanelMouse(), "MOUSE");
        panelFormularioDinamico.add(crearPanelPerifericoGen(), "PERIFERICO_GENERICO");
        panelFormularioDinamico.add(crearPanelLicencia(), "LICENCIA");

        JPanel panelCustodioAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCustodioAux.add(new JLabel("ID Custodio Asociado:"));
        panelCustodioAux.add(txtIdCustodioActivo);
        txtIdCustodioActivo.setPreferredSize(new Dimension(100, 25));
        
        JPanel panelArriba = new JPanel();
        panelArriba.setLayout(new BoxLayout(panelArriba, BoxLayout.Y_AXIS));
        panelArriba.add(panelForm);
        panelArriba.add(panelFormularioDinamico);
        panelArriba.add(panelCustodioAux); 

        JPanel panelBotones = new JPanel();
        btnGuardarActivo = new JButton("Guardar");
        btnActualizarActivo = new JButton("Actualizar");
        btnEliminarActivo = new JButton("Eliminar");

        panelBotones.add(btnGuardarActivo);
        panelBotones.add(btnActualizarActivo);
        panelBotones.add(btnEliminarActivo);
        
        panel.add(panelArriba, BorderLayout.NORTH);
        panel.add(new JPanel(), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        actualizarSubtipos();
        return panel;
    }

    // Subpaneles para CardLayout
    private JPanel crearPanelCpu() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        txtProcesador = new JTextField();
        txtRam = new JTextField();
        txtAlmacenamiento = new JTextField();

        // (Puedes dejar o quitar los setPreferredSize que pusimos antes, 
        // con el paso de abajo ya no son estrictamente necesarios)
        p.add(new JLabel("Procesador:"));
        p.add(txtProcesador);
        p.add(new JLabel("Memoria RAM:"));
        p.add(txtRam);
        p.add(new JLabel("Almacenamiento:"));
        p.add(txtAlmacenamiento);

        // --- NUEVO CÓDIGO AQUÍ ---
        // Envolvemos el grid en un nuevo panel alineado al NORTE (arriba)
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.add(p, BorderLayout.NORTH);

        // Retornamos el contenedor en lugar de 'p'
        return panelContenedor;
    }

    private JPanel crearPanelHardwareGen() {
        JPanel p = new JPanel();
        p.add(new JLabel("No se requieren datos adicionales para Hardware Genérico."));
        return p;
    }

    private JPanel crearPanelMonitor() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
    txtResolucion = new JTextField();
    txtTasaRefresco = new JTextField();
    txtConexionMonitor = new JTextField("HDMI/DisplayPort");

    p.add(new JLabel("Resolución:"));
    p.add(txtResolucion);
    p.add(new JLabel("Tasa de Refresco:"));
    p.add(txtTasaRefresco);
    p.add(new JLabel("Tipo de Conexión:"));
    p.add(txtConexionMonitor);

    JPanel panelContenedor = new JPanel(new BorderLayout());
    panelContenedor.add(p, BorderLayout.NORTH);
    
    return panelContenedor;
    }

    private JPanel crearPanelMouse() {
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        txtDpi = new JTextField();
        txtConexionMouse = new JTextField("USB/Inalámbrico");

        txtDpi.setPreferredSize(new Dimension(300, 28));
        txtDpi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        txtConexionMouse.setPreferredSize(new Dimension(300, 28));
        txtConexionMouse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        p.add(new JLabel("DPI:"));
        p.add(txtDpi);
        p.add(new JLabel("Tipo de Conexión:"));
        p.add(txtConexionMouse);
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.add(p, BorderLayout.NORTH);
        return panelContenedor;
    }

    private JPanel crearPanelPerifericoGen() {
        JPanel p = new JPanel();
        p.add(new JLabel("No se requieren datos adicionales para Periférico Genérico."));
        return p;
    }

    private JPanel crearPanelLicencia() {
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        txtFechaExpiracion = new JTextField("yyyy-MM-dd");
        txtCostoRenovacion = new JTextField("0.0");

        txtFechaExpiracion.setPreferredSize(new Dimension(300, 28));
        txtFechaExpiracion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        txtCostoRenovacion.setPreferredSize(new Dimension(300, 28));
        txtCostoRenovacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        p.add(new JLabel("Fecha Expiración:"));
        p.add(txtFechaExpiracion);
        p.add(new JLabel("Costo Renovación ($):"));
        p.add(txtCostoRenovacion);
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.add(p, BorderLayout.NORTH);
        return panelContenedor;
    }

    // Lógica de actualización de Cascada / CardLayout
    private void actualizarSubtipos() {
        cbSubtipoActivo.removeAllItems();
        String cat = (String) cbCategoriaActivo.getSelectedItem();
        if ("Hardware".equals(cat)) {
            cbSubtipoActivo.addItem("CPU");
            cbSubtipoActivo.addItem("Hardware Genérico");
        } else if ("Periférico".equals(cat)) {
            cbSubtipoActivo.addItem("Monitor");
            cbSubtipoActivo.addItem("Mouse");
            cbSubtipoActivo.addItem("Periférico Genérico");
        } else if ("Licencia".equals(cat)) {
            cbSubtipoActivo.addItem("Licencia de Software");
        }
        cambiarPanelEspecifico();
    }

    private void cambiarPanelEspecifico() {
        String subtipo = (String) cbSubtipoActivo.getSelectedItem();
        if (subtipo == null) {
            return;
        }

        switch (subtipo) {
            case "CPU":
                cardLayoutEspecifico.show(panelFormularioDinamico, "CPU");
                break;
            case "Hardware Genérico":
                cardLayoutEspecifico.show(panelFormularioDinamico, "HARDWARE_GENERICO");
                break;
            case "Monitor":
                cardLayoutEspecifico.show(panelFormularioDinamico, "MONITOR");
                break;
            case "Mouse":
                cardLayoutEspecifico.show(panelFormularioDinamico, "MOUSE");
                break;
            case "Periférico Genérico":
                cardLayoutEspecifico.show(panelFormularioDinamico, "PERIFERICO_GENERICO");
                break;
            case "Licencia de Software":
                cardLayoutEspecifico.show(panelFormularioDinamico, "LICENCIA");
                break;
        }
    }

    //  PANEL CUSTODIOS
    private JPanel crearPanelCustodios() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Custodio"));

        txtIdCustodio = new JTextField();
        txtIdCustodio.setEditable(false);
        txtCedulaCustodio = new JTextField();
        txtNombreCustodio = new JTextField();
        txtApellidoCustodio = new JTextField();
        txtRolCustodio = new JTextField();

        panelForm.add(new JLabel("ID Custodio:"));
        panelForm.add(txtIdCustodio);
        panelForm.add(new JLabel("Cédula:"));
        panelForm.add(txtCedulaCustodio);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombreCustodio);
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellidoCustodio);
        panelForm.add(new JLabel("Rol:"));
        panelForm.add(txtRolCustodio);

        JPanel panelContenedorForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelForm.setPreferredSize(new Dimension(600, 220));
        panelContenedorForm.add(panelForm);

        JPanel panelBotones = new JPanel();
        btnGuardarCustodio = new JButton("Guardar");
        btnActualizarCustodio = new JButton("Actualizar");
        btnEliminarCustodio = new JButton("Eliminar");

        panelBotones.add(btnGuardarCustodio);
        panelBotones.add(btnActualizarCustodio);
        panelBotones.add(btnEliminarCustodio);

        panel.add(panelContenedorForm, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    //  PANEL USUARIOS
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        txtIdUsuario = new JTextField();
        txtIdUsuario.setEditable(false);
        txtIdCustodioUsuario = new JTextField();

        panelForm.add(new JLabel("ID Usuario:"));
        panelForm.add(txtIdUsuario);
        panelForm.add(new JLabel("ID Custodio Asociado:"));
        panelForm.add(txtIdCustodioUsuario);

        JPanel panelContenedorForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelForm.setPreferredSize(new Dimension(600, 120));
        panelContenedorForm.add(panelForm);

        JPanel panelBotones = new JPanel();
        btnGuardarUsuario = new JButton("Guardar");
        btnActualizarUsuario = new JButton("Actualizar");
        btnEliminarUsuario = new JButton("Eliminar");

        panelBotones.add(btnGuardarUsuario);
        panelBotones.add(btnActualizarUsuario);
        panelBotones.add(btnEliminarUsuario);

        panel.add(panelContenedorForm, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    //  PANEL MANTENIMIENTOS
    private JPanel crearPanelMantenimientos() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registro de Mantenimiento"));

        txtIdMantenimiento = new JTextField();
        txtIdMantenimiento.setEditable(false);
        txtDetallesMantenimiento = new JTextField();
        txtFechaInicioMantenimiento = new JTextField();
        txtFechaFinMantenimiento = new JTextField();
        txtCostoMantenimiento = new JTextField();
        txtIdActivoMantenimiento = new JTextField();
        txtIdUsuarioMantenimiento = new JTextField();

        panelForm.add(new JLabel("ID Mantenimiento:"));
        panelForm.add(txtIdMantenimiento);
        panelForm.add(new JLabel("Detalles:"));
        panelForm.add(txtDetallesMantenimiento);
        panelForm.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        panelForm.add(txtFechaInicioMantenimiento);
        panelForm.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        panelForm.add(txtFechaFinMantenimiento);
        panelForm.add(new JLabel("Costo ($):"));
        panelForm.add(txtCostoMantenimiento);
        panelForm.add(new JLabel("ID Activo:"));
        panelForm.add(txtIdActivoMantenimiento);
        panelForm.add(new JLabel("ID Usuario:"));
        panelForm.add(txtIdUsuarioMantenimiento);

        JPanel panelContenedorForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelForm.setPreferredSize(new Dimension(650, 280));
        panelContenedorForm.add(panelForm);

        JPanel panelBotones = new JPanel();
        btnGuardarMantenimiento = new JButton("Guardar");
        btnActualizarMantenimiento = new JButton("Actualizar");
        btnEliminarMantenimiento = new JButton("Eliminar");
        btnFiltrarMantenimientoActivo = new JButton("Filtrar por Activo");

        panelBotones.add(btnGuardarMantenimiento);
        panelBotones.add(btnActualizarMantenimiento);
        panelBotones.add(btnEliminarMantenimiento);
        panelBotones.add(btnFiltrarMantenimientoActivo);

        panel.add(panelContenedorForm, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    // MÉTODOS DE MAPEO POLIMÓRFICO
    // Método para poblar la tabla global desde el controlador
    public void mostrarVistaGlobal(List<Object[]> registros) {
        modeloTablaGlobal.setRowCount(0);
        if (registros != null) {
            for (Object[] fila : registros) {
                modeloTablaGlobal.addRow(fila);
            }
        }
    }

    // --- ACTIVOS ---
    public Activo obtenerDatosFormulario() {
        try {
            int idActivo = 0;
            if (!txtIdActivo.getText().trim().isEmpty()) {
                idActivo = Integer.parseInt(txtIdActivo.getText().trim());
            }

            String nombre = txtNombreActivo.getText().trim();
            String marca = txtMarcaActivo.getText().trim();
            String subtipo = (String) cbSubtipoActivo.getSelectedItem();
            String estado = txtEstadoActivo.getText().trim();

            if (nombre.isEmpty() || estado.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor llena los campos obligatorios: Nombre y Estado del Activo.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            String costoStr = txtCostoActivo.getText().trim().replace(",", ".");
            double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);

            int anniosUso = txtAnniosUsoActivo.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAnniosUsoActivo.getText().trim());

            Custodio custodioAux = null;
            if (!txtIdCustodioActivo.getText().trim().isEmpty()) {
                try {
                    int idCustodio = Integer.parseInt(txtIdCustodioActivo.getText().trim());
                    CustodioDAOImpl custodioDao = new CustodioDAOImpl();
                    custodioAux = custodioDao.obtenerPorId(idCustodio);

                    if (custodioAux == null) {
                        JOptionPane.showMessageDialog(this, "El ID de custodio ingresado no existe en la base de datos.", "Custodio no encontrado", JOptionPane.WARNING_MESSAGE);
                        return null;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "El ID del custodio debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            switch (subtipo) {
                case "CPU":
                    return new Cpu(txtProcesador.getText().trim(), txtRam.getText().trim(), txtAlmacenamiento.getText().trim(), anniosUso, idActivo, nombre, marca, "CPU", costo, estado, custodioAux);
                case "Monitor":
                    return new Monitor(txtResolucion.getText().trim(), txtTasaRefresco.getText().trim(), anniosUso, txtConexionMonitor.getText().trim(), idActivo, nombre, marca, "MONITOR", estado, costo, custodioAux);
                case "Mouse":
                    return new Mouse(txtDpi.getText().trim(), anniosUso, txtConexionMouse.getText().trim(), idActivo, nombre, marca, "MOUSE", costo, estado, custodioAux);
                case "Licencia de Software":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaExp;
                    try {
                        fechaExp = sdf.parse(txtFechaExpiracion.getText().trim());
                    } catch (Exception ex) {
                        fechaExp = new Date();
                    }
                    double costoRenov = 0.0;
                    if (!txtCostoRenovacion.getText().trim().isEmpty()) {
                        costoRenov = Double.parseDouble(txtCostoRenovacion.getText().trim().replace(",", "."));
                    }
                    return new Licencia(fechaExp, costoRenov, idActivo, nombre, marca, "LICENCIA", estado, costo, custodioAux);
                case "Periférico Genérico":
                    return new Periferico(anniosUso, "USB / Genérico", idActivo, nombre, marca, "PERIFERICO", costo, estado, custodioAux);
                case "Hardware Genérico":
                default:
                    return new Hardware(anniosUso, idActivo, nombre, marca, "HARDWARE", costo, estado, custodioAux);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Asegúrate de ingresar valores numéricos válidos en costos, años de uso e IDs.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al construir el activo:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // --- MÉTODOS DE LIMPIEZA DE FORMULARIOS ---
    public void limpiarFormularioActivo() {
        txtIdActivo.setText("");
        txtNombreActivo.setText("");
        txtMarcaActivo.setText("");
        txtCostoActivo.setText("");
        txtEstadoActivo.setText("");
        txtAnniosUsoActivo.setText("0");
        txtIdCustodioActivo.setText("");
        txtProcesador.setText("");
        txtRam.setText("");
        txtAlmacenamiento.setText("");
        txtResolucion.setText("");
        txtTasaRefresco.setText("");
        txtConexionMonitor.setText("HDMI/DisplayPort");
        txtDpi.setText("");
        txtConexionMouse.setText("USB/Inalámbrico");
        txtFechaExpiracion.setText("yyyy-MM-dd");
        txtCostoRenovacion.setText("0.0");
        tablaVistaGlobal.clearSelection();
    }

    public void limpiarFormularioCustodio() {
        txtIdCustodio.setText("");
        txtCedulaCustodio.setText("");
        txtNombreCustodio.setText("");
        txtApellidoCustodio.setText("");
        txtRolCustodio.setText("");
        tablaVistaGlobal.clearSelection();
    }

    public void limpiarFormularioUsuario() {
        txtIdUsuario.setText("");
        txtIdCustodioUsuario.setText("");
        tablaVistaGlobal.clearSelection();
    }

    public void limpiarFormularioMantenimiento() {
        txtIdMantenimiento.setText("");
        txtDetallesMantenimiento.setText("");
        txtFechaInicioMantenimiento.setText("");
        txtFechaFinMantenimiento.setText("");
        txtCostoMantenimiento.setText("");
        txtIdActivoMantenimiento.setText("");
        txtIdUsuarioMantenimiento.setText("");
        tablaVistaGlobal.clearSelection();
    }

    public void cargarFormularioActivoDesdeTabla(Activo activoSeleccionado) {
        if (activoSeleccionado == null) {
            return;
        }

        txtIdActivo.setText(String.valueOf(activoSeleccionado.getIdActivo()));
        txtNombreActivo.setText(activoSeleccionado.getNombreActivo());
        txtMarcaActivo.setText(activoSeleccionado.getMarca());
        txtCostoActivo.setText(String.valueOf(activoSeleccionado.getCostoAdquisicion()));
        txtEstadoActivo.setText(activoSeleccionado.getEstadoActivo());

        if (activoSeleccionado instanceof Hardware) {
            txtAnniosUsoActivo.setText(String.valueOf(((Hardware) activoSeleccionado).getAnniosUso()));
        } else if (activoSeleccionado instanceof Periferico) {
            txtAnniosUsoActivo.setText(String.valueOf(((Periferico) activoSeleccionado).getAnniosUso()));
        } else {
            txtAnniosUsoActivo.setText("0");
        }

        if (activoSeleccionado.getCustodio() != null) {
            txtIdCustodioActivo.setText(String.valueOf(activoSeleccionado.getCustodio().getIdCustodio()));
        } else {
            txtIdCustodioActivo.setText("");
        }

        String tipo = activoSeleccionado.getTipoActivo();
        if (tipo != null) {
            ActionListener[] listenersCategoria = cbCategoriaActivo.getActionListeners();
            ActionListener[] listenersSubtipo = cbSubtipoActivo.getActionListeners();

            for (ActionListener al : listenersCategoria) {
                cbCategoriaActivo.removeActionListener(al);
            }
            for (ActionListener al : listenersSubtipo) {
                cbSubtipoActivo.removeActionListener(al);
            }

            if (tipo.equals("CPU") || tipo.equals("HARDWARE")) {
                cbCategoriaActivo.setSelectedItem("Hardware");
                actualizarSubtiposManual("Hardware");
                cbSubtipoActivo.setSelectedItem(tipo.equals("CPU") ? "CPU" : "Hardware Genérico");
            } else if (tipo.equals("MONITOR") || tipo.equals("MOUSE") || tipo.equals("PERIFERICO")) {
                cbCategoriaActivo.setSelectedItem("Periférico");
                actualizarSubtiposManual("Periférico");
                if (tipo.equals("MONITOR")) {
                    cbSubtipoActivo.setSelectedItem("Monitor");
                } else if (tipo.equals("MOUSE")) {
                    cbSubtipoActivo.setSelectedItem("Mouse");
                } else {
                    cbSubtipoActivo.setSelectedItem("Periférico Genérico");
                }
            } else if (tipo.equals("LICENCIA")) {
                cbCategoriaActivo.setSelectedItem("Licencia");
                actualizarSubtiposManual("Licencia");
                cbSubtipoActivo.setSelectedItem("Licencia de Software");
            }

            for (ActionListener al : listenersCategoria) {
                cbCategoriaActivo.addActionListener(al);
            }
            for (ActionListener al : listenersSubtipo) {
                cbSubtipoActivo.addActionListener(al);
            }
        }

        cambiarPanelEspecifico();

        if (activoSeleccionado instanceof Cpu) {
            Cpu cpu = (Cpu) activoSeleccionado;
            if (txtProcesador != null) {
                txtProcesador.setText(cpu.getProcesador() != null ? cpu.getProcesador() : "");
            }
            if (txtRam != null) {
                txtRam.setText(cpu.getMemoriaRAM() != null ? cpu.getMemoriaRAM() : "");
            }
            if (txtAlmacenamiento != null) {
                txtAlmacenamiento.setText(cpu.getAlmacenamiento() != null ? cpu.getAlmacenamiento() : "");
            }
        } else if (activoSeleccionado instanceof Monitor) {
            Monitor mon = (Monitor) activoSeleccionado;
            if (txtResolucion != null) {
                txtResolucion.setText(mon.getResolucion() != null ? mon.getResolucion() : "");
            }
            if (txtTasaRefresco != null) {
                txtTasaRefresco.setText(mon.getTasaDeRefresco() != null ? mon.getTasaDeRefresco() : "");
            }
            if (txtConexionMonitor != null) {
                txtConexionMonitor.setText(mon.getTipoConexion() != null ? mon.getTipoConexion() : "");
            }
        } else if (activoSeleccionado instanceof Mouse) {
            Mouse mouse = (Mouse) activoSeleccionado;
            if (txtDpi != null) {
                txtDpi.setText(mouse.getDpi() != null ? mouse.getDpi() : "");
            }
            if (txtConexionMouse != null) {
                txtConexionMouse.setText(mouse.getTipoConexion() != null ? mouse.getTipoConexion() : "");
            }
        } else if (activoSeleccionado instanceof Licencia) {
            Licencia lic = (Licencia) activoSeleccionado;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (txtFechaExpiracion != null) {
                txtFechaExpiracion.setText(lic.getFechaExpiracion() != null ? sdf.format(lic.getFechaExpiracion()) : "");
            }
            if (txtCostoRenovacion != null) {
                txtCostoRenovacion.setText(String.valueOf(lic.getCostoRenovacion()));
            }
        }
    }

    // Método auxiliar privado para poblar los subtipos sin disparar eventos de escucha en cadena
    private void actualizarSubtiposManual(String cat) {
        cbSubtipoActivo.removeAllItems();
        if ("Hardware".equals(cat)) {
            cbSubtipoActivo.addItem("CPU");
            cbSubtipoActivo.addItem("Hardware Genérico");
        } else if ("Periférico".equals(cat)) {
            cbSubtipoActivo.addItem("Monitor");
            cbSubtipoActivo.addItem("Mouse");
            cbSubtipoActivo.addItem("Periférico Genérico");
        } else if ("Licencia".equals(cat)) {
            cbSubtipoActivo.addItem("Licencia de Software");
        }
    }

    // --- CUSTODIO ---
    public Custodio obtenerDatosCustodio() {
        try {
            int idCustodio = 0;
            if (!txtIdCustodio.getText().trim().isEmpty()) {
                idCustodio = Integer.parseInt(txtIdCustodio.getText().trim());
            }

            String cedula = txtCedulaCustodio.getText().trim();
            String nombre = txtNombreCustodio.getText().trim();
            String apellido = txtApellidoCustodio.getText().trim();
            String rol = txtRolCustodio.getText().trim();

            if (cedula.isEmpty() || nombre.isEmpty()) {
                return null;
            }

            return new Custodio(idCustodio, cedula, nombre, apellido, rol);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El ID del custodio debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // --- USUARIO ---
    public Usuario obtenerDatosUsuario() {
        try {
            int idCustodio = Integer.parseInt(txtIdCustodioUsuario.getText().trim());
            Custodio c = new Custodio();
            c.setIdCustodio(idCustodio);
            return new Usuario(0, c);
        } catch (Exception e) {
            return null;
        }
    }

    // --- MANTENIMIENTOS ---
    public RegMantenimiento obtenerDatosMantenimiento() {
        try {
            String detalles = txtDetallesMantenimiento.getText().trim();
            String fInicioStr = txtFechaInicioMantenimiento.getText().trim();
            String fFinStr = txtFechaFinMantenimiento.getText().trim();

            if (detalles.isEmpty() || fInicioStr.isEmpty() || txtIdActivoMantenimiento.getText().trim().isEmpty() || txtIdUsuarioMantenimiento.getText().trim().isEmpty()) {
                return null;
            }

            String costoStr = txtCostoMantenimiento.getText().trim().replace(",", ".");
            double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);

            int idActivo = Integer.parseInt(txtIdActivoMantenimiento.getText().trim());
            Activo activoAux = new Hardware(0, idActivo, "", "", "Hardware", 0.0, "", null);

            int idUsuario = Integer.parseInt(txtIdUsuarioMantenimiento.getText().trim());
            Usuario usuarioAux = new Usuario(idUsuario, null);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fInicio = sdf.parse(fInicioStr);
            Date fFin = fFinStr.isEmpty() ? null : sdf.parse(fFinStr);

            return new RegMantenimiento(0, detalles, fInicio, fFin, costo, activoAux, usuarioAux);
        } catch (Exception e) {
            System.err.println("Error en obtenerDatosMantenimiento: " + e.getMessage());
            return null;
        }
    }

    // OBTENCIÓN DE SELECCIÓN DESDE LA TABLA GLOBAL
    public String obtenerIdSeleccionadoGlobal() {
        int fila = tablaVistaGlobal.getSelectedRow();
        return (fila != -1) ? tablaVistaGlobal.getValueAt(fila, 0).toString() : null;
    }

    public String obtenerTipoSeleccionadoGlobal() {
        int fila = tablaVistaGlobal.getSelectedRow();
        return (fila != -1) ? tablaVistaGlobal.getValueAt(fila, 1).toString() : null;
    }
}

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
    public JTable tablaActivos;
    private DefaultTableModel modeloTablaActivos;

    //--- Pestaña 2: CUSTODIOS ---
    public JTextField txtIdCustodio, txtCedulaCustodio, txtNombreCustodio, txtApellidoCustodio, txtRolCustodio;
    public JButton btnGuardarCustodio, btnActualizarCustodio, btnEliminarCustodio;
    public JTable tablaCustodios;
    private DefaultTableModel modeloTablaCustodios;

    //--- Pestaña 3: USUARIOS ---
    public JTextField txtIdUsuario, txtIdCustodioUsuario;
    public JButton btnGuardarUsuario, btnActualizarUsuario, btnEliminarUsuario;
    public JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;

    //--- Pestaña 4: MANTENIMIENTOS ---
    public JTextField txtIdMantenimiento, txtDetallesMantenimiento, txtFechaInicioMantenimiento, txtFechaFinMantenimiento, txtCostoMantenimiento, txtIdActivoMantenimiento, txtIdUsuarioMantenimiento;
    public JButton btnGuardarMantenimiento, btnActualizarMantenimiento, btnEliminarMantenimiento, btnFiltrarMantenimientoActivo;
    public JTable tablaMantenimientos;
    private DefaultTableModel modeloTablaMantenimientos;

    public Vista() {
        // Configuramos la ventana básica
        this.setTitle("Sistema de Gestión de Activos e Inventarios - UTPL");
        this.setSize(1050, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane panelPestañas = new JTabbedPane();

        panelPestañas.addTab("Activos", crearPanelActivos());
        panelPestañas.addTab("Custodios", crearPanelCustodios());
        panelPestañas.addTab("Usuarios", crearPanelUsuarios());
        panelPestañas.addTab("Mantenimientos", crearPanelMantenimientos());

        this.add(panelPestañas);
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

        // Selección multinivel / cascada
        cbCategoriaActivo = new JComboBox<>(new String[]{"Hardware", "Periférico", "Licencia"});
        cbSubtipoActivo = new JComboBox<>();

        // Eventos para actualizar la cascada y la vista dinámica
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

        // Paneles Dinámicos (CardLayout)
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

        JPanel panelFormularioCompleto = new JPanel(new BorderLayout());
        panelFormularioCompleto.add(panelForm, BorderLayout.NORTH);
        panelFormularioCompleto.add(panelFormularioDinamico, BorderLayout.CENTER);
        panelFormularioCompleto.add(panelCustodioAux, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel();
        btnGuardarActivo = new JButton("Guardar");
        btnActualizarActivo = new JButton("Actualizar");
        btnEliminarActivo = new JButton("Eliminar");

        panelBotones.add(btnGuardarActivo);
        panelBotones.add(btnActualizarActivo);
        panelBotones.add(btnEliminarActivo);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormularioCompleto, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaActivos = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Marca", "Tipo", "Costo Adquisición", "Estado", "Custodio"}, 0
        );
        tablaActivos = new JTable(modeloTablaActivos);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaActivos), BorderLayout.CENTER);

        actualizarSubtipos();

        return panel;
    }

    // Subpaneles para CardLayout
    private JPanel crearPanelCpu() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        txtProcesador = new JTextField();
        txtRam = new JTextField();
        txtAlmacenamiento = new JTextField();
        p.add(new JLabel("Procesador:"));
        p.add(txtProcesador);
        p.add(new JLabel("Memoria RAM:"));
        p.add(txtRam);
        p.add(new JLabel("Almacenamiento:"));
        p.add(txtAlmacenamiento);
        return p;
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
        return p;
    }

    private JPanel crearPanelMouse() {
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        txtDpi = new JTextField();
        txtConexionMouse = new JTextField("USB/Inalámbrico");
        p.add(new JLabel("DPI:"));
        p.add(txtDpi);
        p.add(new JLabel("Tipo de Conexión:"));
        p.add(txtConexionMouse);
        return p;
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
        p.add(new JLabel("Fecha Expiración:"));
        p.add(txtFechaExpiracion);
        p.add(new JLabel("Costo Renovación ($):"));
        p.add(txtCostoRenovacion);
        return p;
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

        JPanel panelBotones = new JPanel();
        btnGuardarCustodio = new JButton("Guardar");
        btnActualizarCustodio = new JButton("Actualizar");
        btnEliminarCustodio = new JButton("Eliminar");

        panelBotones.add(btnGuardarCustodio);
        panelBotones.add(btnActualizarCustodio);
        panelBotones.add(btnEliminarCustodio);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaCustodios = new DefaultTableModel(
                new String[]{"ID", "Cédula", "Nombre", "Apellido", "Rol"}, 0
        );
        tablaCustodios = new JTable(modeloTablaCustodios);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCustodios), BorderLayout.CENTER);

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

        JPanel panelBotones = new JPanel();
        btnGuardarUsuario = new JButton("Guardar");
        btnActualizarUsuario = new JButton("Actualizar");
        btnEliminarUsuario = new JButton("Eliminar");

        panelBotones.add(btnGuardarUsuario);
        panelBotones.add(btnActualizarUsuario);
        panelBotones.add(btnEliminarUsuario);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaUsuarios = new DefaultTableModel(
                new String[]{"ID Usuario", "ID Custodio", "Nombre Custodio"}, 0
        );
        tablaUsuarios = new JTable(modeloTablaUsuarios);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

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

        JPanel panelBotones = new JPanel();
        btnGuardarMantenimiento = new JButton("Guardar");
        btnActualizarMantenimiento = new JButton("Actualizar");
        btnEliminarMantenimiento = new JButton("Eliminar");
        btnFiltrarMantenimientoActivo = new JButton("Filtrar por Activo");

        panelBotones.add(btnGuardarMantenimiento);
        panelBotones.add(btnActualizarMantenimiento);
        panelBotones.add(btnEliminarMantenimiento);
        panelBotones.add(btnFiltrarMantenimientoActivo);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaMantenimientos = new DefaultTableModel(
                new String[]{"ID", "Detalles", "Fecha Inicio", "Fecha Fin", "Costo", "Activo", "Usuario"}, 0
        );
        tablaMantenimientos = new JTable(modeloTablaMantenimientos);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMantenimientos), BorderLayout.CENTER);

        return panel;
    }

    // MÉTODOS DE MAPEO POLIMÓRFICO
    // --- ACTIVOS ---
    public void mostrarLista(List<Activo> activos) {
        modeloTablaActivos.setRowCount(0);
        if (activos != null) {
            for (Activo a : activos) {
                String nombreCustodio = (a.getCustodio() != null) ? a.getCustodio().getNombre() + " " + a.getCustodio().getApellido() : "Sin Custodio";
                modeloTablaActivos.addRow(new Object[]{
                    a.getIdActivo(), // Clave primaria
                    a.getNombreActivo(),
                    a.getMarca(),
                    a.getTipoActivo(),
                    a.getCostoAdquisicion(),
                    a.getEstadoActivo(),
                    nombreCustodio
                });
            }
        }
    }

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

            // Correcion de formato numérico por estabilidad (remueve comas y espacios extra)
            String costoStr = txtCostoActivo.getText().trim().replace(",", ".");
            double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);

            int anniosUso = txtAnniosUsoActivo.getText().trim().isEmpty()
                    ? 0
                    : Integer.parseInt(txtAnniosUsoActivo.getText().trim());

            Custodio custodioAux = null;
            if (!txtIdCustodioActivo.getText().trim().isEmpty()) {
                try {
                    int idCustodio = Integer.parseInt(txtIdCustodioActivo.getText().trim());

                    // LLAMAS AL DAO DE CUSTODIO PARA OBTENER EL OBJETO COMPLETO
                    CustodioDAOImpl custodioDao = new CustodioDAOImpl(); // O la instancia que uses en tu proyecto
                    custodioAux = custodioDao.obtenerPorId(idCustodio);

                    if (custodioAux == null) {
                        JOptionPane.showMessageDialog(this,
                                "El ID de custodio ingresado no existe en la base de datos.",
                                "Custodio no encontrado", JOptionPane.WARNING_MESSAGE);
                        return null; // Detiene el guardado si el custodio no es válido
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "El ID del custodio debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            switch (subtipo) {
                case "CPU":
                    return new Cpu(
                            txtProcesador.getText().trim(),
                            txtRam.getText().trim(),
                            txtAlmacenamiento.getText().trim(),
                            anniosUso, idActivo, nombre, marca, "CPU",
                            costo,
                            estado,
                            custodioAux
                    );
                case "Monitor":
                    return new Monitor(
                            txtResolucion.getText().trim(),
                            txtTasaRefresco.getText().trim(),
                            anniosUso,
                            txtConexionMonitor.getText().trim(),
                            idActivo, nombre, marca, "MONITOR",
                            estado,
                            costo,
                            custodioAux
                    );

                case "Mouse":
                    return new Mouse(
                            txtDpi.getText().trim(),
                            anniosUso, txtConexionMouse.getText().trim(),
                            idActivo, nombre, marca, "MOUSE", costo, estado, custodioAux
                    );

                case "Licencia de Software":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaExp = null;
                    try {
                        fechaExp = sdf.parse(txtFechaExpiracion.getText().trim());
                    } catch (Exception ex) {
                        fechaExp = new Date();
                    }
                    double costoRenov = 0.0;
                    if (!txtCostoRenovacion.getText().trim().isEmpty()) {
                        costoRenov = Double.parseDouble(txtCostoRenovacion.getText().trim().replace(",", "."));
                    }
                    return new Licencia(
                            fechaExp,
                            costoRenov,
                            idActivo,
                            nombre, marca,
                            "LICENCIA",
                            estado, costo, custodioAux
                    );

                case "Periférico Genérico":
                    return new Periferico(anniosUso, "USB / Genérico", idActivo, nombre, marca, "PERIFERICO", costo, estado, custodioAux);

                case "Hardware Genérico":
                default:
                    return new Hardware(anniosUso, idActivo, nombre, marca, "HARDWARE", costo, estado, custodioAux);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Asegúrate de ingresar valores numéricos válidos en costos, años de uso e IDs.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al construir el activo:" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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

        // Campos dinámicos
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

        tablaActivos.clearSelection();
    }

    public void limpiarFormularioCustodio() {
        txtIdCustodio.setText("");
        txtCedulaCustodio.setText("");
        txtNombreCustodio.setText("");
        txtApellidoCustodio.setText("");
        txtRolCustodio.setText("");
        tablaCustodios.clearSelection();
    }

    public void limpiarFormularioUsuario() {
        txtIdUsuario.setText("");
        txtIdCustodioUsuario.setText("");
        tablaUsuarios.clearSelection();
    }

    public void limpiarFormularioMantenimiento() {
        txtIdMantenimiento.setText("");
        txtDetallesMantenimiento.setText("");
        txtFechaInicioMantenimiento.setText("");
        txtFechaFinMantenimiento.setText("");
        txtCostoMantenimiento.setText("");
        txtIdActivoMantenimiento.setText("");
        txtIdUsuarioMantenimiento.setText("");
        tablaMantenimientos.clearSelection();
    }

    public void cargarFormularioActivoDesdeTabla(Activo activoSeleccionado) {
        if (activoSeleccionado == null) return;

        // 1. Cargar datos generales comunes
        txtIdActivo.setText(String.valueOf(activoSeleccionado.getIdActivo()));
        txtNombreActivo.setText(activoSeleccionado.getNombreActivo());
        txtMarcaActivo.setText(activoSeleccionado.getMarca());
        txtCostoActivo.setText(String.valueOf(activoSeleccionado.getCostoAdquisicion()));
        txtEstadoActivo.setText(activoSeleccionado.getEstadoActivo());

        // Cargar años de uso de forma segura (verificando clases hijas que implementan anniosUso)
        if (activoSeleccionado instanceof Hardware) {
            txtAnniosUsoActivo.setText(String.valueOf(((Hardware) activoSeleccionado).getAnniosUso()));
        } else if (activoSeleccionado instanceof Periferico) {
            txtAnniosUsoActivo.setText(String.valueOf(((Periferico) activoSeleccionado).getAnniosUso()));
        } else {
            txtAnniosUsoActivo.setText("0"); // Licencias u otros tipos que no posean años de uso
        }

        // Cargar Custodio si existe
        if (activoSeleccionado.getCustodio() != null) {
            txtIdCustodioActivo.setText(String.valueOf(activoSeleccionado.getCustodio().getIdCustodio()));
        } else {
            txtIdCustodioActivo.setText("");
        }

        // 2. Establecer categoría y subtipo evitando la propagación de eventos indeseados
        String tipo = activoSeleccionado.getTipoActivo();
        if (tipo != null) {
            ActionListener[] listenersCategoria = cbCategoriaActivo.getActionListeners();
            ActionListener[] listenersSubtipo = cbSubtipoActivo.getActionListeners();
            
            for (ActionListener al : listenersCategoria) cbCategoriaActivo.removeActionListener(al);
            for (ActionListener al : listenersSubtipo) cbSubtipoActivo.removeActionListener(al);

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

            for (ActionListener al : listenersCategoria) cbCategoriaActivo.addActionListener(al);
            for (ActionListener al : listenersSubtipo) cbSubtipoActivo.addActionListener(al);
        }

        // Forzar actualización visual del panel específico del CardLayout
        cambiarPanelEspecifico();

        // 3. Cargar los datos específicos mediante polimorfismo seguro (con nombres de getters corregidos)
        if (activoSeleccionado instanceof Cpu) {
            Cpu cpu = (Cpu) activoSeleccionado;
            if (txtProcesador != null) txtProcesador.setText(cpu.getProcesador() != null ? cpu.getProcesador() : "");
            if (txtRam != null) txtRam.setText(cpu.getMemoriaRAM() != null ? cpu.getMemoriaRAM() : "");
            if (txtAlmacenamiento != null) txtAlmacenamiento.setText(cpu.getAlmacenamiento() != null ? cpu.getAlmacenamiento() : "");
        } else if (activoSeleccionado instanceof Monitor) {
            Monitor mon = (Monitor) activoSeleccionado;
            if (txtResolucion != null) txtResolucion.setText(mon.getResolucion() != null ? mon.getResolucion() : "");
            if (txtTasaRefresco != null) txtTasaRefresco.setText(mon.getTasaDeRefresco() != null ? mon.getTasaDeRefresco() : "");
            if (txtConexionMonitor != null) txtConexionMonitor.setText(mon.getTipoConexion() != null ? mon.getTipoConexion() : "");
        } else if (activoSeleccionado instanceof Mouse) {
            Mouse mouse = (Mouse) activoSeleccionado;
            if (txtDpi != null) txtDpi.setText(mouse.getDpi() != null ? mouse.getDpi() : "");
            if (txtConexionMouse != null) txtConexionMouse.setText(mouse.getTipoConexion() != null ? mouse.getTipoConexion() : "");
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

    // --- ACTIVOS ---
    public void cargarFormularioCustodioDesdeTabla() {
        int fila = tablaCustodios.getSelectedRow();
        if (fila != -1) {
            txtIdCustodio.setText(tablaCustodios.getValueAt(fila, 0).toString());
            txtCedulaCustodio.setText(tablaCustodios.getValueAt(fila, 1).toString());
            txtNombreCustodio.setText(tablaCustodios.getValueAt(fila, 2).toString());
            txtApellidoCustodio.setText(tablaCustodios.getValueAt(fila, 3).toString());
            txtRolCustodio.setText(tablaCustodios.getValueAt(fila, 4).toString());
        }
    }

    public String obtenerIdActivoSeleccionado() {
        int fila = tablaActivos.getSelectedRow();
        return (fila != -1) ? tablaActivos.getValueAt(fila, 0).toString() : null;
    }

    // --- CUSTODIOS ---
    public String obtenerIdCustodioSeleccionado() {
        int fila = tablaCustodios.getSelectedRow();
        return (fila != -1) ? tablaCustodios.getValueAt(fila, 0).toString() : null;
    }

    public void mostrarListaCustodios(List<Custodio> custodios) {
        modeloTablaCustodios.setRowCount(0);
        if (custodios != null) {
            for (Custodio c : custodios) {
                modeloTablaCustodios.addRow(new Object[]{
                    c.getIdCustodio(), // Clave primaria
                    c.getCedula(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getRol()
                });
            }
        }
    }

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
            JOptionPane.showMessageDialog(this, "El ID del custodio debe ser un número entero válido.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // --- USUARIOS ---
    public void cargarFormularioUsuarioDesdeTabla() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila != -1) {
            txtIdUsuario.setText(tablaUsuarios.getValueAt(fila, 0) != null ? tablaUsuarios.getValueAt(fila, 0).toString() : "");
            txtIdCustodioUsuario.setText(tablaUsuarios.getValueAt(fila, 1) != null ? tablaUsuarios.getValueAt(fila, 1).toString() : "");
        }
    }

    public String obtenerIdUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        return (fila != -1) ? tablaUsuarios.getValueAt(fila, 0).toString() : null;
    }

    public void mostrarListaUsuarios(List<Usuario> usuarios) {
        modeloTablaUsuarios.setRowCount(0);
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                String nombreCustodio = (u.getCustodio() != null) ? u.getCustodio().getNombre() + " " + u.getCustodio().getApellido() : "N/A";
                int idCustodio = (u.getCustodio() != null) ? u.getCustodio().getIdCustodio() : 0;

                modeloTablaUsuarios.addRow(new Object[]{
                    u.getIdUsuario(), // Clave primaria
                    idCustodio, // Clave foranea
                    nombreCustodio
                });
            }
        }
    }

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
    public void cargarFormularioMantenimientoDesdeTabla() {
        int fila = tablaMantenimientos.getSelectedRow();
        if (fila != -1) {
            txtIdMantenimiento.setText(tablaMantenimientos.getValueAt(fila, 0).toString());
            txtDetallesMantenimiento.setText(tablaMantenimientos.getValueAt(fila, 1).toString());

            Object fInicio = tablaMantenimientos.getValueAt(fila, 2);
            txtFechaInicioMantenimiento.setText(fInicio != null ? fInicio.toString() : "");

            Object fFin = tablaMantenimientos.getValueAt(fila, 3);
            txtFechaFinMantenimiento.setText(fFin != null ? fFin.toString() : "");

            txtCostoMantenimiento.setText(tablaMantenimientos.getValueAt(fila, 4).toString());
        }
    }

    public String obtenerIdMantenimientoSeleccionado() {
        int fila = tablaMantenimientos.getSelectedRow();
        return (fila != -1) ? tablaMantenimientos.getValueAt(fila, 0).toString() : null;
    }

    public void mostrarListaMantenimientos(List<RegMantenimiento> mantenimientos) {
        modeloTablaMantenimientos.setRowCount(0);
        if (mantenimientos != null) {
            for (RegMantenimiento rm : mantenimientos) {
                String nombreActivo = (rm.getActivo() != null) ? rm.getActivo().getNombreActivo() : "N/A";
                String idUsuario = (rm.getUsuario() != null) ? String.valueOf(rm.getUsuario().getIdUsuario()) : "N/A";

                modeloTablaMantenimientos.addRow(new Object[]{
                    rm.getIdMantenimiento(), // Clave primaria
                    rm.getDetallesMantenimiento(),
                    rm.getFechaInicio(),
                    rm.getFechaFin(),
                    rm.getCostoMantenimiento(),
                    nombreActivo,
                    idUsuario
                });
            }
        }
    }

    public RegMantenimiento obtenerDatosMantenimiento() {
        try {
            String detalles = txtDetallesMantenimiento.getText().trim();
            String fInicioStr = txtFechaInicioMantenimiento.getText().trim();
            String fFinStr = txtFechaFinMantenimiento.getText().trim();

            if (detalles.isEmpty() || fInicioStr.isEmpty()
                    || txtIdActivoMantenimiento.getText().trim().isEmpty()
                    || txtIdUsuarioMantenimiento.getText().trim().isEmpty()) {
                return null;
            }

            String costoStr = txtCostoMantenimiento.getText().trim().replace(",", ".");
            double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);

            int idActivo = Integer.parseInt(txtIdActivoMantenimiento.getText().trim());
            // Firma exacta: (anniosUso = 0, idActivo = idActivo, nombre, marca, tipo, costo, estado, custodio)
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
}

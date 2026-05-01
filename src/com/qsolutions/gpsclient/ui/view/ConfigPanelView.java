package com.qsolutions.gpsclient.ui.view;

import com.qsolutions.gpsclient.config.FleetConfig;
import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.service.GpsSoapService;
import com.qsolutions.gpsclient.ui.validator.CoordinateValidator;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.tempuri.Protocolo;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

/**
 * Configuration panel for a fleet unit.
 *
 * <p>Allows the operator to select a unit, edit its schedule and GPS
 * coordinates, and trigger a test pulse.  Fields validate in real-time:
 * a green border indicates a valid value, red indicates an invalid one.</p>
 *
 * <p>Selecting a unit from the combo box pre-populates all fields with
 * that unit's default values.</p>
 */
public class ConfigPanelView {

    // -------------------------------------------------------------------------
    // Instance fields (need cross-listener access)
    // -------------------------------------------------------------------------

    private static final GpsSoapService gpsService = new GpsSoapService();

    private final VBox root;
    private final EventLogView eventLog;

    private ComboBox<String> comboUnidad;
    private TextField tfInicio;
    private TextField tfFin;
    private TextField tfLat;
    private TextField tfLon;
    private Button btnCancelar;
    private Button btnTest;
    private Button btnGuardar;

    private Label labelTipoHorario;

    private java.util.function.Consumer<Unidad> onUnidadSeleccionada;

    /**
     * Constructs the configuration panel, assembles all form sections, and
     * attaches validation and action listeners.
     *
     * @param eventLog the shared event log panel; must not be {@code null}
     */
    public ConfigPanelView(EventLogView eventLog) {
        this.eventLog = eventLog;
        root = new VBox(16);
        root.setPadding(new Insets(16));

        labelTipoHorario = new Label("");
        labelTipoHorario.setVisible(false);
        labelTipoHorario.setManaged(false);
        labelTipoHorario.getStyleClass().add("badge-schedule");

        root.getChildren().addAll(
                buildHeader(),
                new Separator(),
                buildUnitSelector(),
                labelTipoHorario,
                buildScheduleSection(),
                buildCoordinatesSection(),
                buildActions()
        );

        attachListeners();
    }

    /**
     * Returns the root {@link VBox} node ready to be placed into a parent layout.
     *
     * @return the assembled configuration panel root
     */
    public VBox getRoot() {
        return root;
    }

    public void seleccionarUnidad(Unidad u) {
        comboUnidad.setValue(u.getNumUnidad());
    }

    public void setOnUnidadSeleccionada(java.util.function.Consumer<Unidad> handler) {
        this.onUnidadSeleccionada = handler;
    }

    // -------------------------------------------------------------------------
    // Section builders
    // -------------------------------------------------------------------------

    private VBox buildHeader() {
        Label title = new Label("Panel de Control");
        title.getStyleClass().add("label-title");

        Label subtitle = new Label("Configura los parámetros de la unidad seleccionada");
        subtitle.getStyleClass().add("label-secondary");

        return new VBox(4, title, subtitle);
    }

    private VBox buildUnitSelector() {
        Label lbl = new Label("Unidad");
        lbl.getStyleClass().add("label-secondary");

        comboUnidad = new ComboBox<>(FXCollections.observableArrayList(
                "Peugeot", "Kangoo", "Tr-02", "Attitude", "Sentra"
        ));
        comboUnidad.setPromptText("Selecciona una unidad");
        comboUnidad.setMaxWidth(Double.MAX_VALUE);

        return new VBox(6, lbl, comboUnidad);
    }

    private VBox buildScheduleSection() {
        Label sectionTitle = new Label("Horario de operación");
        sectionTitle.getStyleClass().add("label-subtitle");

        tfInicio = createField("06:00");
        tfFin    = createField("16:00");

        VBox vboxInicio = labeledField("Inicio", tfInicio);
        VBox vboxFin    = labeledField("Fin",    tfFin);
        HBox.setHgrow(vboxInicio, Priority.ALWAYS);
        HBox.setHgrow(vboxFin,    Priority.ALWAYS);

        return new VBox(8, sectionTitle, new HBox(12, vboxInicio, vboxFin));
    }

    private VBox buildCoordinatesSection() {
        Label sectionTitle = new Label("Coordenadas GPS");
        sectionTitle.getStyleClass().add("label-subtitle");

        tfLat = createField("-17.0526");
        tfLon = createField("-101.2345");

        VBox vboxLat = labeledField("Latitud",  tfLat);
        VBox vboxLon = labeledField("Longitud", tfLon);
        HBox.setHgrow(vboxLat, Priority.ALWAYS);
        HBox.setHgrow(vboxLon, Priority.ALWAYS);

        Label hint = new Label("Rangos válidos: -90/+90 lat, -180/+180 lon");
        hint.getStyleClass().add("label-secondary");
        hint.setStyle("-fx-font-size: 11px;");

        return new VBox(8, sectionTitle, new HBox(12, vboxLat, vboxLon), hint);
    }

    private HBox buildActions() {
        btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-secondary");

        btnTest = new Button("Test pulso");
        btnTest.getStyleClass().add("btn-success");

        btnGuardar = new Button("Guardar");
        btnGuardar.getStyleClass().add("btn-primary");

        HBox actions = new HBox(8, btnCancelar, btnTest, btnGuardar);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(8, 0, 0, 0));
        return actions;
    }

    // -------------------------------------------------------------------------
    // Listeners
    // -------------------------------------------------------------------------

    private void attachListeners() {
        attachValidation(tfInicio, CoordinateValidator::isValidTime);
        attachValidation(tfFin,    CoordinateValidator::isValidTime);
        attachValidation(tfLat,    CoordinateValidator::isValidLatitude);
        attachValidation(tfLon,    CoordinateValidator::isValidLongitude);

        comboUnidad.valueProperty().addListener((obs, oldVal, newVal) -> {
            Unidad nuevaUnidad = newVal != null ? FleetConfig.getByName(newVal) : null;

            if (nuevaUnidad == null) {
                labelTipoHorario.setVisible(false);
                labelTipoHorario.setManaged(false);
                tfInicio.setDisable(false);
                tfFin.setDisable(false);
                return;
            }

            labelTipoHorario.setVisible(true);
            labelTipoHorario.setManaged(true);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

            if (nuevaUnidad.isHorarioFijo()) {
                labelTipoHorario.setText("● Horario fijo (Manager) — no editable");
                labelTipoHorario.getStyleClass().setAll("badge-schedule", "badge-schedule-fixed");
                tfInicio.setText(nuevaUnidad.getHoraInicio().format(fmt));
                tfFin.setText(nuevaUnidad.getHoraFin().format(fmt));
                tfInicio.setDisable(true);
                tfFin.setDisable(true);
            } else {
                labelTipoHorario.setText("● Horario manual (Operador) — editable");
                labelTipoHorario.getStyleClass().setAll("badge-schedule", "badge-schedule-manual");
                tfInicio.setText(nuevaUnidad.getHoraInicio() != null ? nuevaUnidad.getHoraInicio().format(fmt) : "");
                tfFin.setText(nuevaUnidad.getHoraFin() != null ? nuevaUnidad.getHoraFin().format(fmt) : "");
                tfInicio.setDisable(false);
                tfFin.setDisable(false);
            }

            tfLat.setText(nuevaUnidad.getLatitud() != null ? nuevaUnidad.getLatitud().toString() : "");
            tfLon.setText(nuevaUnidad.getLongitud() != null ? nuevaUnidad.getLongitud().toString() : "");

            if (onUnidadSeleccionada != null) {
                onUnidadSeleccionada.accept(nuevaUnidad);
            }
        });

        btnGuardar.setOnAction(e -> {
            String nombre = comboUnidad.getValue();
            if (nombre == null) {
                System.out.println("[Guardar] No hay unidad seleccionada");
                if (eventLog != null) {
                    eventLog.agregarEvento("Sistema", "⚠ Aviso", "Selecciona una unidad antes de guardar");
                }
                return;
            }
            Unidad sel = FleetConfig.getByName(nombre);
            if (sel == null) return;

            StringBuilder cambios = new StringBuilder();
            boolean huboError = false;

            // 1. Guardar coordenadas SIEMPRE (independiente de horario fijo o manual)
            try {
                if (tfLat.getText() != null && !tfLat.getText().trim().isEmpty()) {
                    BigDecimal lat = new BigDecimal(tfLat.getText().trim());
                    sel.setLatitud(lat);
                    cambios.append("lat=").append(lat).append(" ");
                }
            } catch (NumberFormatException ex) {
                System.out.println("[Guardar] Error parseando latitud: " + ex.getMessage());
                huboError = true;
            }

            try {
                if (tfLon.getText() != null && !tfLon.getText().trim().isEmpty()) {
                    BigDecimal lon = new BigDecimal(tfLon.getText().trim());
                    sel.setLongitud(lon);
                    cambios.append("lon=").append(lon).append(" ");
                }
            } catch (NumberFormatException ex) {
                System.out.println("[Guardar] Error parseando longitud: " + ex.getMessage());
                huboError = true;
            }

            // 2. Solo guardar horarios si NO es horario fijo
            if (!sel.isHorarioFijo()) {
                try {
                    if (tfInicio.getText() != null && !tfInicio.getText().trim().isEmpty()) {
                        LocalTime ini = LocalTime.parse(tfInicio.getText().trim());
                        sel.setHoraInicio(ini);
                        cambios.append("inicio=").append(ini).append(" ");
                    }
                    if (tfFin.getText() != null && !tfFin.getText().trim().isEmpty()) {
                        LocalTime fin = LocalTime.parse(tfFin.getText().trim());
                        sel.setHoraFin(fin);
                        cambios.append("fin=").append(fin).append(" ");
                    }
                } catch (Exception ex) {
                    System.out.println("[Guardar] Error parseando horario: " + ex.getMessage());
                    huboError = true;
                }
            }

            // 3. Activar la unidad
            sel.setActiva(true);

            // 4. Log en consola para diagnóstico
            System.out.println("[Guardar] Unidad final: " + sel);
            System.out.println("[Guardar] Lat actual: " + sel.getLatitud() + " | Lon actual: " + sel.getLongitud());

            // 5. Feedback en UI
            if (eventLog != null) {
                if (huboError) {
                    eventLog.agregarEvento(sel.getNumUnidad(), "⚠ Guardado parcial",
                            "Algunos campos no se pudieron parsear");
                } else {
                    eventLog.agregarEvento(sel.getNumUnidad(), "✓ Guardado",
                            "Configuración actualizada: " + cambios.toString().trim());
                }
            }
        });

        btnCancelar.setOnAction(e -> {
            comboUnidad.setValue(null);
            tfInicio.clear();
            tfFin.clear();
            tfLat.clear();
            tfLon.clear();
            tfInicio.setDisable(false);
            tfFin.setDisable(false);
            labelTipoHorario.setVisible(false);
            labelTipoHorario.setManaged(false);
        });

        btnTest.setOnAction(e -> {
            String nombreUnidad = comboUnidad.getValue();
            if (nombreUnidad == null) {
                eventLog.agregarEvento("Panel", "✗ Error", "Sin unidad seleccionada");
                return;
            }

            Unidad unidadSel = FleetConfig.getByName(nombreUnidad);
            if (unidadSel == null) {
                eventLog.agregarEvento("Panel", "✗ Error", "Unidad no encontrada");
                return;
            }

            if (unidadSel.getLatitud() == null || unidadSel.getLongitud() == null) {
                eventLog.agregarEvento(nombreUnidad, "✗ Error", "Sin coordenadas configuradas");
                return;
            }

            btnTest.setText("Enviando...");
            btnTest.setDisable(true);

            Task<Protocolo> task = new Task<>() {
                @Override
                protected Protocolo call() {
                    return gpsService.enviarPulsacion(unidadSel);
                }
            };

            task.setOnSucceeded(ev -> {
                Protocolo respuesta = task.getValue();
                btnTest.setText("Test pulso");
                btnTest.setDisable(false);

                if (respuesta != null && respuesta.isProcessed()) {
                    eventLog.agregarEvento(
                            nombreUnidad,
                            "✓ Enviado",
                            "GPS pulse: " + unidadSel.getLatitud() + ", " + unidadSel.getLongitud()
                    );
                } else if (respuesta != null) {
                    eventLog.agregarEvento(
                            nombreUnidad,
                            "⚠ Rechazado",
                            respuesta.getMessage() != null ? respuesta.getMessage() : "Sin mensaje del servidor"
                    );
                } else {
                    eventLog.agregarEvento(
                            nombreUnidad,
                            "✗ Error",
                            "Conexión fallida con QSolutions"
                    );
                }
            });

            task.setOnFailed(ev -> {
                btnTest.setText("Test pulso");
                btnTest.setDisable(false);
                Throwable ex = task.getException();
                eventLog.agregarEvento(
                        nombreUnidad,
                        "✗ Error",
                        ex != null ? ex.getMessage() : "Error desconocido"
                );
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void attachValidation(TextField field, Predicate<String> validator) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            field.getStyleClass().removeAll("text-field-valid", "text-field-invalid");
            if (newVal == null || newVal.isEmpty()) return;
            field.getStyleClass().add(validator.test(newVal) ? "text-field-valid" : "text-field-invalid");
        });
    }

    private boolean allFieldsValid() {
        return CoordinateValidator.isValidTime(tfInicio.getText())
            && CoordinateValidator.isValidTime(tfFin.getText())
            && CoordinateValidator.isValidLatitude(tfLat.getText())
            && CoordinateValidator.isValidLongitude(tfLon.getText());
    }

    private TextField createField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setMaxWidth(Double.MAX_VALUE);
        return field;
    }

    private VBox labeledField(String labelText, TextField field) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("label-secondary");
        return new VBox(4, lbl, field);
    }
}

package com.qsolutions.gpsclient.ui.view;

import com.qsolutions.gpsclient.ui.validator.CoordinateValidator;
import javafx.collections.FXCollections;
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

import java.util.Map;
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

    private final VBox root;

    private ComboBox<String> comboUnidad;
    private TextField tfInicio;
    private TextField tfFin;
    private TextField tfLat;
    private TextField tfLon;
    private Button btnCancelar;
    private Button btnTest;
    private Button btnGuardar;

    // Unit presets: name → [inicio, fin, lat, lon]
    private static final Map<String, String[]> PRESETS = Map.of(
        "Peugeot",  new String[]{"06:00", "16:00", "-17.0526", "-101.2345"},
        "Kangoo",   new String[]{"07:00", "17:00", "-17.0612", "-101.2400"},
        "Tr-02",    new String[]{"07:00", "17:00", "-17.0589", "-101.2378"},
        "Attitude", new String[]{"Manual", "Manual", "", ""},
        "Sentra",   new String[]{"Manual", "Manual", "", ""}
    );

    /**
     * Constructs the configuration panel, assembles all form sections, and
     * attaches validation and action listeners.
     */
    public ConfigPanelView() {
        root = new VBox(16);
        root.setPadding(new Insets(16));

        root.getChildren().addAll(
                buildHeader(),
                new Separator(),
                buildUnitSelector(),
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
            if (newVal == null) return;
            String[] preset = PRESETS.get(newVal);
            if (preset == null) return;
            tfInicio.setText(preset[0]);
            tfFin.setText(preset[1]);
            tfLat.setText(preset[2]);
            tfLon.setText(preset[3]);
        });

        btnGuardar.setOnAction(e -> {
            if (!allFieldsValid()) return;
            System.out.println("Guardando: "
                    + comboUnidad.getValue() + " "
                    + tfInicio.getText() + " "
                    + tfFin.getText() + " "
                    + tfLat.getText() + " "
                    + tfLon.getText());
        });

        btnCancelar.setOnAction(e -> {
            comboUnidad.setValue(null);
            tfInicio.clear();
            tfFin.clear();
            tfLat.clear();
            tfLon.clear();
        });

        btnTest.setOnAction(e -> {
            String unidad = comboUnidad.getValue();
            System.out.println("Test pulso para: " + (unidad != null ? unidad : "(sin unidad)"));
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

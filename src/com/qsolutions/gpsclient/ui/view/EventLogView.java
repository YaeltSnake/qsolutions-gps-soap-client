package com.qsolutions.gpsclient.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Event log panel showing a live table of GPS pulse activity.
 *
 * <p>Displays the most recent pulse events from all fleet units.  New events
 * can be pushed at runtime via {@link #agregarEvento(EventoLog)}.</p>
 */
public class EventLogView {

    private final VBox root;
    private final ObservableList<EventoLog> items = FXCollections.observableArrayList();

    /**
     * Constructs the event log panel and pre-loads five sample rows.
     */
    public EventLogView() {
        TableView<EventoLog> table = buildTable();

        root = new VBox(8, buildHeader(), table);
        root.getStyleClass().add("surface-pane");
        root.setPadding(new Insets(16));
        root.setPrefHeight(240);
        VBox.setVgrow(table, Priority.ALWAYS);

        preloadSampleData();
    }

    /**
     * Returns the root {@link VBox} node ready to be placed into a parent layout.
     *
     * @return the assembled event log panel root
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * Inserts a new event at the top of the table so the most recent pulse is
     * always visible without scrolling.
     *
     * @param evento the event to add; must not be {@code null}
     */
    public void agregarEvento(EventoLog evento) {
        items.add(0, evento);
    }

    // -------------------------------------------------------------------------
    // Builders
    // -------------------------------------------------------------------------

    private HBox buildHeader() {
        Label title = new Label("Eventos");
        title.getStyleClass().add("label-subtitle");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label subtitle = new Label("Últimas pulsaciones");
        subtitle.getStyleClass().add("label-secondary");

        HBox header = new HBox(12, title, spacer, subtitle);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private TableView<EventoLog> buildTable() {
        TableColumn<EventoLog, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colHora.setPrefWidth(100);

        TableColumn<EventoLog, String> colUnidad = new TableColumn<>("Unidad");
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colUnidad.setPrefWidth(120);

        TableColumn<EventoLog, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(100);

        TableColumn<EventoLog, String> colDetalles = new TableColumn<>("Detalles");
        colDetalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        colDetalles.setMaxWidth(Double.MAX_VALUE);

        TableView<EventoLog> table = new TableView<>(items);
        table.getColumns().addAll(colHora, colUnidad, colEstado, colDetalles);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label placeholder = new Label("Sin eventos registrados aún");
        placeholder.getStyleClass().add("label-secondary");
        table.setPlaceholder(placeholder);

        return table;
    }

    private void preloadSampleData() {
        items.addAll(
            new EventoLog("12:32:15", "Peugeot",  "✓ Enviado",      "GPS pulse: -17.05, -101.23"),
            new EventoLog("12:31:20", "Kangoo",   "✓ Enviado",      "GPS pulse: -17.06, -101.24"),
            new EventoLog("12:30:45", "Tr-02",    "⚠ Reintentando", "Timeout: 5000ms"),
            new EventoLog("12:29:30", "Attitude", "✓ Enviado",      "GPS pulse: -17.04, -101.22"),
            new EventoLog("12:28:15", "Sentra",   "✗ Error",        "Connection refused"),
            new EventoLog("12:27:00", "Peugeot",  "✓ Enviado",      "GPS pulse: -17.05, -101.23"),
            new EventoLog("12:25:45", "Kangoo",   "✓ Enviado",      "GPS pulse: -17.06, -101.24"),
            new EventoLog("12:24:30", "Tr-02",    "✓ Enviado",      "GPS pulse: -17.05, -101.23"),
            new EventoLog("12:23:15", "Attitude", "✓ Enviado",      "GPS pulse: -17.04, -101.22"),
            new EventoLog("12:22:00", "Sentra",   "⚠ Reintentando", "Timeout: 5000ms")
        );
    }

    // -------------------------------------------------------------------------
    // Inner model class
    // -------------------------------------------------------------------------

    /**
     * Immutable record of a single GPS pulse event displayed in the log table.
     */
    public static class EventoLog {

        private final String hora;
        private final String unidad;
        private final String estado;
        private final String detalles;

        /**
         * Creates a new event log entry.
         *
         * @param hora     time of the pulse (e.g. {@code "12:32:15"})
         * @param unidad   unit name (e.g. {@code "Peugeot"})
         * @param estado   result status (e.g. {@code "✓ Enviado"})
         * @param detalles detail message or error description
         */
        public EventoLog(String hora, String unidad, String estado, String detalles) {
            this.hora     = hora;
            this.unidad   = unidad;
            this.estado   = estado;
            this.detalles = detalles;
        }

        /** @return pulse timestamp */
        public String getHora()     { return hora; }

        /** @return unit display name */
        public String getUnidad()   { return unidad; }

        /** @return result status string */
        public String getEstado()   { return estado; }

        /** @return detail message */
        public String getDetalles() { return detalles; }
    }
}

package com.qsolutions.gpsclient.ui.view;

import javafx.application.Platform;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Event log panel showing a live table of GPS pulse activity.
 *
 * <p>Displays the most recent pulse events from all fleet units.  New events
 * can be pushed at runtime via {@link #agregarEvento(String, String, String)}.</p>
 */
public class EventLogView {

    private final VBox root;
    private final ObservableList<EventoLog> eventos = FXCollections.observableArrayList();

    /**
     * Constructs the event log panel and pre-loads ten sample rows.
     */
    public EventLogView() {
        TableView<EventoLog> table = buildTable();

        root = new VBox(8, buildHeader(), table);
        root.getStyleClass().add("surface-pane");
        root.setPadding(new Insets(16));
        root.setPrefHeight(240);
        VBox.setVgrow(table, Priority.ALWAYS);

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
     * Inserts a new event at the top of the table, thread-safe.
     * Trims the list to a maximum of 50 entries.
     *
     * @param unidad   unit name
     * @param estado   result status (e.g. {@code "✓ Enviado"})
     * @param detalles detail message or error description
     */
    public void agregarEvento(String unidad, String estado, String detalles) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        EventoLog evento = new EventoLog(hora, unidad, estado, detalles);
        Platform.runLater(() -> {
            eventos.add(0, evento);
            while (eventos.size() > 50) {
                eventos.remove(eventos.size() - 1);
            }
        });
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

        TableView<EventoLog> table = new TableView<>(eventos);
        table.getColumns().addAll(colHora, colUnidad, colEstado, colDetalles);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label placeholder = new Label("Sin eventos registrados aún");
        placeholder.getStyleClass().add("label-secondary");
        table.setPlaceholder(placeholder);

        return table;
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

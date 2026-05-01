package com.qsolutions.gpsclient.ui.view;

import com.qsolutions.gpsclient.ui.animation.CardAnimations;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Reusable card component representing a single fleet unit.
 *
 * <p>Displays the unit name, schedule, last-pulse timestamp, next-pulse
 * countdown with a progress bar, and quick-action buttons.  The card
 * applies {@code card-active} styling when the unit is active, and
 * {@code card-secondary} when inactive.</p>
 *
 * <p>Active cards fade in on construction and run a live countdown timer
 * on the progress bar.</p>
 */
public class UnidadCardView {

    private final VBox root;

    // Promoted to instance fields so the countdown timer can update them
    private Label labelSegundos;
    private ProgressBar progressBar;

    /**
     * Constructs a card for the given fleet unit.
     *
     * @param nombreUnidad  display name of the unit (e.g. {@code "Peugeot"})
     * @param horarioInicio schedule start time or {@code "Manual"}
     * @param horarioFin    schedule end time or {@code "Manual"}
     * @param tipoHorario   schedule type label (e.g. {@code "Manager"})
     * @param activa        {@code true} if the unit is currently active
     */
    public UnidadCardView(String nombreUnidad,
                          String horarioInicio,
                          String horarioFin,
                          String tipoHorario,
                          boolean activa) {
        root = buildCard(nombreUnidad, horarioInicio, horarioFin, tipoHorario, activa);

        if (activa) {
            CardAnimations.fadeIn(root, 400);
            Timeline timer = CardAnimations.createCountdownTimer(labelSegundos, progressBar, 900);
            timer.play();
        }
    }

    /**
     * Returns the root {@link VBox} node ready to be added to a parent layout.
     *
     * @return the assembled card node
     */
    public VBox getRoot() {
        return root;
    }

    // -------------------------------------------------------------------------
    // Private builder
    // -------------------------------------------------------------------------

    private VBox buildCard(String nombre,
                           String inicio,
                           String fin,
                           String tipo,
                           boolean activa) {
        VBox card = new VBox(8);
        card.getStyleClass().addAll("card", activa ? "card-active" : "card-secondary");
        card.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(
                buildHeader(nombre, activa),
                buildScheduleLabel(inicio, fin, tipo),
                new Separator(),
                buildLastPulse(),
                buildCountdown(),
                buildActions()
        );

        return card;
    }

    private HBox buildHeader(String nombre, boolean activa) {
        Label lblNombre = new Label(nombre);
        lblNombre.getStyleClass().add("label-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblEstado = new Label(activa ? "● Activa" : "○ Inactiva");
        lblEstado.getStyleClass().add(activa ? "label-success" : "label-secondary");

        HBox header = new HBox(8, lblNombre, spacer, lblEstado);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private Label buildScheduleLabel(String inicio, String fin, String tipo) {
        boolean manual = "Manual".equalsIgnoreCase(inicio);
        String texto = manual
                ? "Manual · " + tipo
                : inicio + " — " + fin + " · " + tipo;

        Label lbl = new Label(texto);
        lbl.getStyleClass().add("label-secondary");
        return lbl;
    }

    private VBox buildLastPulse() {
        Label header = new Label("Último pulso");
        header.getStyleClass().add("label-secondary");
        header.setStyle("-fx-font-size: 11px;");

        Label timestamp = new Label("hace 3 min");
        timestamp.getStyleClass().add("label-title");
        timestamp.setStyle("-fx-font-size: 12px; -fx-font-weight: normal;");

        return new VBox(4, header, timestamp);
    }

    private VBox buildCountdown() {
        Label header = new Label("Próximo pulso");
        header.getStyleClass().add("label-secondary");
        header.setStyle("-fx-font-size: 11px;");

        // Assigned to instance fields so the timeline can update them
        labelSegundos = new Label("en 900 seg");
        labelSegundos.setStyle("-fx-text-fill: -color-text-primary; -fx-font-size: 12px;");

        progressBar = new ProgressBar(1.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().addAll("progress-bar", "progress-bar-success");
        VBox.setMargin(progressBar, new Insets(2, 0, 0, 0));

        return new VBox(4, header, labelSegundos, progressBar);
    }

    private HBox buildActions() {
        Button btnEditar = new Button("Editar");
        btnEditar.getStyleClass().add("btn-secondary");
        btnEditar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnEditar, Priority.ALWAYS);

        Button btnTest = new Button("Test pulso");
        btnTest.getStyleClass().add("btn-success");
        btnTest.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnTest, Priority.ALWAYS);

        return new HBox(8, btnEditar, btnTest);
    }
}

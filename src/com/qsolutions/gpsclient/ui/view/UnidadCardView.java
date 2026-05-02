package com.qsolutions.gpsclient.ui.view;

import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.ui.animation.CardAnimations;
import com.qsolutions.gpsclient.ui.scheduler.UnitScheduler;
import com.qsolutions.gpsclient.ui.scheduler.UnitSchedulerRegistry;
import javafx.application.Platform;
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

import java.time.format.DateTimeFormatter;

/**
 * Reactive card representing a single fleet unit and its scheduler state.
 *
 * <p>Owns no scheduling logic — delegates to {@link UnitScheduler} via
 * {@link UnitSchedulerRegistry}. Re-renders automatically when the
 * unit's schedule state, coordinates, or activation change.</p>
 */
public class UnidadCardView {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final Unidad unidad;
    private final EventLogView eventLog;
    private final UnitScheduler scheduler;
    private final VBox root;

    private Label labelEstado;
    private Label labelCountdown;
    private Label labelPulsos;
    private ProgressBar progressBar;
    private Button btnIniciarDetener;
    private Button btnForzarPulso;
    private Button btnEditar;

    private Runnable onEditRequest;

    public UnidadCardView(Unidad unidad, EventLogView eventLog) {
        this.unidad   = unidad;
        this.eventLog = eventLog;
        this.scheduler = UnitSchedulerRegistry.getInstance().getOrCreate(unidad, eventLog);
        this.root     = buildCard();

        attachReactiveListeners();
        attachSchedulerCallbacks();
        refrescarEstadoVisual();
    }

    public Unidad getUnidad() { return unidad; }
    public VBox getRoot() { return root; }

    public void setSeleccionado(boolean sel) {
        if (sel) {
            if (!root.getStyleClass().contains("card-selected")) {
                root.getStyleClass().add("card-selected");
            }
        } else {
            root.getStyleClass().remove("card-selected");
        }
    }

    public void setOnEditRequest(Runnable handler) {
        this.onEditRequest = handler;
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    private VBox buildCard() {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(
            buildHeader(),
            buildScheduleLabel(),
            new Separator(),
            buildCountdownSection(),
            buildPulsosSection(),
            buildActionsRow1(),
            buildActionsRow2()
        );
        return card;
    }

    private HBox buildHeader() {
        Label lblNombre = new Label(unidad.getNumUnidad());
        lblNombre.getStyleClass().add("label-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        labelEstado = new Label("");
        HBox header = new HBox(8, lblNombre, spacer, labelEstado);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private Label buildScheduleLabel() {
        String tipo = unidad.isHorarioFijo() ? "Manager" : "Operador";
        String texto = unidad.isHorarioFijo()
            ? unidad.getHoraInicio().format(TIME_FMT) + " — "
              + unidad.getHoraFin().format(TIME_FMT) + " · " + tipo
            : "Manual · " + tipo;
        Label lbl = new Label(texto);
        lbl.getStyleClass().add("label-secondary");
        return lbl;
    }

    private VBox buildCountdownSection() {
        Label header = new Label("Próximo pulso");
        header.getStyleClass().add("label-secondary");
        header.setStyle("-fx-font-size: 11px;");

        labelCountdown = new Label("—");
        labelCountdown.setStyle("-fx-text-fill: -color-text-tertiary; -fx-font-size: 14px; -fx-font-weight: 500;");

        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().addAll("progress-bar", "progress-bar-success");
        VBox.setMargin(progressBar, new Insets(2, 0, 0, 0));

        return new VBox(4, header, labelCountdown, progressBar);
    }

    private VBox buildPulsosSection() {
        Label header = new Label("Pulsos enviados");
        header.getStyleClass().add("label-secondary");
        header.setStyle("-fx-font-size: 11px;");

        labelPulsos = new Label("0 / —");
        labelPulsos.setStyle("-fx-text-fill: -color-text-primary; -fx-font-size: 12px;");

        return new VBox(4, header, labelPulsos);
    }

    private HBox buildActionsRow1() {
        btnEditar = new Button("Editar");
        btnEditar.getStyleClass().add("btn-secondary");
        btnEditar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnEditar, Priority.ALWAYS);
        btnEditar.setOnAction(e -> { if (onEditRequest != null) onEditRequest.run(); });

        btnForzarPulso = new Button("Forzar pulso");
        btnForzarPulso.getStyleClass().add("btn-secondary");
        btnForzarPulso.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnForzarPulso, Priority.ALWAYS);
        btnForzarPulso.setOnAction(e -> scheduler.forcePulse());
        btnForzarPulso.setDisable(true);

        return new HBox(8, btnEditar, btnForzarPulso);
    }

    private HBox buildActionsRow2() {
        btnIniciarDetener = new Button("Iniciar ronda");
        btnIniciarDetener.getStyleClass().add("btn-success");
        btnIniciarDetener.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnIniciarDetener, Priority.ALWAYS);
        btnIniciarDetener.setDisable(true);

        btnIniciarDetener.setOnAction(e -> {
            if (scheduler.getState() == UnitScheduler.State.RUNNING) {
                scheduler.stop();
            } else {
                scheduler.start();
            }
        });

        return new HBox(8, btnIniciarDetener);
    }

    // -------------------------------------------------------------------------
    // Reactive bindings
    // -------------------------------------------------------------------------

    private void attachReactiveListeners() {
        unidad.activaProperty().addListener((obs, o, n) -> refrescarEstadoVisual());
        unidad.latitudProperty().addListener((obs, o, n) -> refrescarEstadoVisual());
        unidad.longitudProperty().addListener((obs, o, n) -> refrescarEstadoVisual());
    }

    private void attachSchedulerCallbacks() {
        scheduler.setOnTick(() -> Platform.runLater(this::refrescarCountdown));
        scheduler.setOnPulseSent(() -> Platform.runLater(this::refrescarPulsos));
        scheduler.setOnStateChange(() -> Platform.runLater(this::refrescarEstadoVisual));
    }

    // -------------------------------------------------------------------------
    // Visual refresh
    // -------------------------------------------------------------------------

    private boolean isConfigurada() {
        return unidad.getLatitud() != null && unidad.getLongitud() != null;
    }

    private void refrescarEstadoVisual() {
        UnitScheduler.State state = scheduler.getState();

        root.getStyleClass().removeAll("card-active", "card-secondary", "card-warning");

        if (!isConfigurada()) {
            root.getStyleClass().add("card-warning");
            labelEstado.setText("⚠ Sin configurar");
            labelEstado.getStyleClass().setAll("label-warning");
            btnIniciarDetener.setDisable(true);
            btnIniciarDetener.setText("Iniciar ronda");
            btnIniciarDetener.getStyleClass().setAll("btn-success");
            btnForzarPulso.setDisable(true);
            labelCountdown.setText("—");
            labelCountdown.setStyle("-fx-text-fill: -color-text-tertiary; -fx-font-size: 14px; -fx-font-weight: 500;");
            progressBar.setProgress(0);
            labelPulsos.setText("0 / —");
            return;
        }

        switch (state) {
            case RUNNING:
                root.getStyleClass().add("card-active");
                labelEstado.setText("● En operación");
                labelEstado.getStyleClass().setAll("label-success");
                btnIniciarDetener.setDisable(false);
                btnIniciarDetener.setText("Detener ronda");
                btnIniciarDetener.getStyleClass().setAll("btn-danger");
                btnForzarPulso.setDisable(false);
                refrescarCountdown();
                refrescarPulsos();
                break;
            case EXPIRED:
                root.getStyleClass().add("card-secondary");
                labelEstado.setText("⏱ Sesión finalizada");
                labelEstado.getStyleClass().setAll("label-secondary");
                btnIniciarDetener.setDisable(true);
                btnIniciarDetener.setText("Iniciar ronda");
                btnIniciarDetener.getStyleClass().setAll("btn-success");
                btnForzarPulso.setDisable(true);
                labelCountdown.setText("—");
                refrescarPulsos();
                break;
            case STOPPED:
                root.getStyleClass().add("card-secondary");
                labelEstado.setText("⏹ Detenida");
                labelEstado.getStyleClass().setAll("label-secondary");
                btnIniciarDetener.setDisable(false);
                btnIniciarDetener.setText("Iniciar ronda");
                btnIniciarDetener.getStyleClass().setAll("btn-success");
                btnForzarPulso.setDisable(true);
                labelCountdown.setText("—");
                refrescarPulsos();
                break;
            case IDLE:
            default:
                root.getStyleClass().add("card-secondary");
                labelEstado.setText("○ Lista");
                labelEstado.getStyleClass().setAll("label-secondary");
                btnIniciarDetener.setDisable(false);
                btnIniciarDetener.setText("Iniciar ronda");
                btnIniciarDetener.getStyleClass().setAll("btn-success");
                btnForzarPulso.setDisable(true);
                labelCountdown.setText("—");
                progressBar.setProgress(0);
                labelPulsos.setText("0 / —");
                break;
        }
    }

    private void refrescarCountdown() {
        int segundos = scheduler.getSegundosRestantes();
        int minutos = segundos / 60;
        int seg = segundos % 60;
        labelCountdown.setText(String.format("%02d:%02d", minutos, seg));
        labelCountdown.setStyle("-fx-text-fill: -color-text-primary; -fx-font-size: 14px; -fx-font-weight: 500;");
        progressBar.setProgress((double) segundos / 900);
    }

    private void refrescarPulsos() {
        int enviados = scheduler.getPulsosEnviados();
        int total = scheduler.getPulsosTotalesEstimados();
        if (total > 0) {
            labelPulsos.setText(enviados + " / " + total);
        } else {
            labelPulsos.setText(enviados + " enviados");
        }
    }
}

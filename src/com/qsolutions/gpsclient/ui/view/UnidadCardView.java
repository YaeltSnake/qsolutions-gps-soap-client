package com.qsolutions.gpsclient.ui.view;

import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.service.GpsSoapService;
import com.qsolutions.gpsclient.ui.animation.CardAnimations;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
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
import org.tempuri.Protocolo;

import java.time.format.DateTimeFormatter;

/**
 * Reusable card component representing a single fleet unit.
 *
 * <p>Three visual states driven by {@link #isOperacional()}:</p>
 * <ul>
 *   <li>Operational (active + coords set) — green border, countdown running.</li>
 *   <li>Unconfigured (no coords) — orange border, countdown paused.</li>
 *   <li>Inactive (coords set but not active) — grey border, countdown paused.</li>
 * </ul>
 */
public class UnidadCardView {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final GpsSoapService gpsService = new GpsSoapService();

    private final Unidad       unidad;
    private final EventLogView eventLog;
    private final VBox         root;

    private Label       labelEstado;
    private Label       labelSegundos;
    private ProgressBar progressBar;
    private Button      btnTestPulso;

    private boolean  seleccionado  = false;
    private Runnable onEditRequest;

    /**
     * Constructs a card bound to the given {@link Unidad}.
     *
     * @param unidad   the fleet unit to display; must not be {@code null}
     * @param eventLog the shared event log panel; must not be {@code null}
     */
    public UnidadCardView(Unidad unidad, EventLogView eventLog) {
        this.unidad   = unidad;
        this.eventLog = eventLog;
        this.root     = buildCard();

        attachActivaListener();

        if (isOperacional()) {
            CardAnimations.fadeIn(root, 400);
            Timeline timer = CardAnimations.createCountdownTimer(labelSegundos, progressBar, 900);
            timer.play();
        } else {
            labelSegundos.setText("—");
            labelSegundos.setStyle("-fx-text-fill: -color-text-tertiary;");
            progressBar.setProgress(0);
            progressBar.setStyle("-fx-accent: -color-text-tertiary;");
        }
    }

    /** @return the model object backing this card */
    public Unidad getUnidad() { return unidad; }

    /** @return the root {@link VBox} ready to be placed in a parent layout */
    public VBox getRoot() { return root; }

    public void setSeleccionado(boolean sel) {
        this.seleccionado = sel;
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
        String initialState = isOperacional() ? "card-active"
                : (unidad.getLatitud() == null || unidad.getLongitud() == null) ? "card-warning"
                : "card-secondary";
        card.getStyleClass().addAll("card", initialState);
        card.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(
                buildHeader(),
                buildScheduleLabel(),
                new Separator(),
                buildLastPulse(),
                buildCountdown(),
                buildActions()
        );
        return card;
    }

    private HBox buildHeader() {
        Label lblNombre = new Label(unidad.getNumUnidad());
        lblNombre.getStyleClass().add("label-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String textoEstado;
        String claseEstado;
        if (isOperacional()) {
            textoEstado = "● Activa";
            claseEstado = "label-success";
        } else if (unidad.getLatitud() == null || unidad.getLongitud() == null) {
            textoEstado = "⚠ Sin configurar";
            claseEstado = "label-warning";
        } else {
            textoEstado = "○ Inactiva";
            claseEstado = "label-secondary";
        }
        labelEstado = new Label(textoEstado);
        labelEstado.getStyleClass().add(claseEstado);

        HBox header = new HBox(8, lblNombre, spacer, labelEstado);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private Label buildScheduleLabel() {
        String tipo = unidad.isHorarioFijo() ? "Manager" : "Operador";
        String texto;
        if (unidad.isHorarioFijo()) {
            texto = unidad.getHoraInicio().format(TIME_FMT)
                  + " — "
                  + unidad.getHoraFin().format(TIME_FMT)
                  + " · " + tipo;
        } else {
            texto = "Manual · " + tipo;
        }
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
        btnEditar.setOnAction(e -> { if (onEditRequest != null) onEditRequest.run(); });

        btnTestPulso = new Button("Test pulso");
        btnTestPulso.getStyleClass().add("btn-success");
        btnTestPulso.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnTestPulso, Priority.ALWAYS);

        btnTestPulso.setOnAction(e -> {
            System.out.println("[Test Pulso Card] Unidad: " + unidad.getNumUnidad()
                    + " | Lat: " + unidad.getLatitud()
                    + " | Lon: " + unidad.getLongitud()
                    + " | Activa: " + unidad.isActiva());

            if (unidad.getLatitud() == null || unidad.getLongitud() == null) {
                eventLog.agregarEvento(unidad.getNumUnidad(), "✗ Error",
                        "Sin coordenadas configuradas");
                return;
            }

            btnTestPulso.setText("Enviando...");
            btnTestPulso.setDisable(true);

            Task<Protocolo> task = new Task<>() {
                @Override
                protected Protocolo call() {
                    return gpsService.enviarPulsacion(unidad);
                }
            };

            task.setOnSucceeded(ev -> {
                Protocolo respuesta = task.getValue();
                btnTestPulso.setText("Test pulso");
                btnTestPulso.setDisable(false);

                if (respuesta != null && respuesta.isProcessed()) {
                    eventLog.agregarEvento(
                            unidad.getNumUnidad(),
                            "✓ Enviado",
                            "GPS pulse: " + unidad.getLatitud() + ", " + unidad.getLongitud()
                    );
                } else if (respuesta != null) {
                    eventLog.agregarEvento(
                            unidad.getNumUnidad(),
                            "⚠ Rechazado",
                            respuesta.getMessage() != null ? respuesta.getMessage() : "Sin mensaje del servidor"
                    );
                } else {
                    eventLog.agregarEvento(
                            unidad.getNumUnidad(),
                            "✗ Error",
                            "Conexión fallida con QSolutions"
                    );
                }
            });

            task.setOnFailed(ev -> {
                btnTestPulso.setText("Test pulso");
                btnTestPulso.setDisable(false);
                Throwable ex = task.getException();
                eventLog.agregarEvento(
                        unidad.getNumUnidad(),
                        "✗ Error",
                        ex != null ? ex.getMessage() : "Error desconocido"
                );
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        return new HBox(8, btnEditar, btnTestPulso);
    }

    // -------------------------------------------------------------------------
    // State helpers
    // -------------------------------------------------------------------------

    private boolean isOperacional() {
        return unidad.isActiva()
                && unidad.getLatitud() != null
                && unidad.getLongitud() != null;
    }

    private void attachActivaListener() {
        unidad.activaProperty().addListener((obs, oldVal, newVal) -> refrescarEstado());
        unidad.latitudProperty().addListener((obs, oldVal, newVal) -> refrescarEstado());
        unidad.longitudProperty().addListener((obs, oldVal, newVal) -> refrescarEstado());
    }

    private void refrescarEstado() {
        if (isOperacional()) {
            root.getStyleClass().remove("card-secondary");
            root.getStyleClass().remove("card-warning");
            if (!root.getStyleClass().contains("card-active")) root.getStyleClass().add("card-active");
            labelEstado.setText("● Activa");
            labelEstado.getStyleClass().setAll("label-success");
        } else if (unidad.getLatitud() == null || unidad.getLongitud() == null) {
            root.getStyleClass().remove("card-active");
            root.getStyleClass().remove("card-secondary");
            if (!root.getStyleClass().contains("card-warning")) root.getStyleClass().add("card-warning");
            labelEstado.setText("⚠ Sin configurar");
            labelEstado.getStyleClass().setAll("label-warning");
        } else {
            root.getStyleClass().remove("card-active");
            root.getStyleClass().remove("card-warning");
            if (!root.getStyleClass().contains("card-secondary")) root.getStyleClass().add("card-secondary");
            labelEstado.setText("○ Inactiva");
            labelEstado.getStyleClass().setAll("label-secondary");
        }
    }
}

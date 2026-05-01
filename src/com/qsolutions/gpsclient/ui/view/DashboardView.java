package com.qsolutions.gpsclient.ui.view;

import com.qsolutions.gpsclient.config.FleetConfig;
import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.ui.animation.CardAnimations;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Root view of the Fleet Tracker dashboard.
 *
 * <p>Organises the main window into four regions:</p>
 * <ul>
 *   <li><b>Top</b> — application header with title and active-unit count.</li>
 *   <li><b>Left</b> — scrollable panel listing {@link UnidadCardView} cards.</li>
 *   <li><b>Center</b> — control panel that will host map/detail views.</li>
 *   <li><b>Bottom</b> — event log for pulse activity.</li>
 * </ul>
 */
public class DashboardView {

    private final BorderPane root;
    private Label labelContador;

    private final List<UnidadCardView> cardViews = new ArrayList<>();
    private ConfigPanelView configPanel;
    private EventLogView eventLogView;

    /**
     * Constructs the dashboard and assembles all layout regions.
     */
    public DashboardView() {
        eventLogView = new EventLogView();
        root = new BorderPane();
        root.setTop(buildHeader());
        root.setLeft(buildUnitPanel());
        root.setCenter(buildCenterPanel());
        root.setBottom(buildEventLog());
    }

    /**
     * Returns the root {@link BorderPane} that can be placed directly into a
     * {@link javafx.scene.Scene}.
     *
     * @return the fully assembled dashboard root pane
     */
    public BorderPane getRoot() {
        return root;
    }

    public EventLogView getEventLog() {
        return eventLogView;
    }

    // -------------------------------------------------------------------------
    // Region builders
    // -------------------------------------------------------------------------

    private VBox buildHeader() {
        Label title = new Label("QSolutions Fleet Tracker");
        title.getStyleClass().add("label-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        labelContador = new Label();
        labelContador.getStyleClass().add("label-secondary");

        HBox titleBar = new HBox(12, title, spacer, labelContador);

        StatisticsCardView stats = new StatisticsCardView();

        VBox topContainer = new VBox(12, titleBar, new Separator(), stats.getRoot());
        topContainer.getStyleClass().add("surface-pane");
        topContainer.setPadding(new Insets(16));
        return topContainer;
    }

    private ScrollPane buildUnitPanel() {
        VBox leftPanel = new VBox(12);
        leftPanel.setPadding(new Insets(16));

        List<Unidad> unidades = FleetConfig.getUnidades();

        for (int i = 0; i < unidades.size(); i++) {
            Unidad u = unidades.get(i);
            UnidadCardView card = new UnidadCardView(u, eventLogView);
            card.setOnEditRequest(() -> configPanel.seleccionarUnidad(u));
            cardViews.add(card);
            leftPanel.getChildren().add(card.getRoot());
            CardAnimations.fadeInWithDelay(card.getRoot(), 400, i * 100);
        }

        for (Unidad u : unidades) {
            u.activaProperty().addListener((obs, oldVal, newVal) -> actualizarContador());
            u.latitudProperty().addListener((obs, oldVal, newVal) -> actualizarContador());
            u.longitudProperty().addListener((obs, oldVal, newVal) -> actualizarContador());
        }

        actualizarContador();

        ScrollPane scroll = new ScrollPane(leftPanel);
        scroll.setPrefWidth(340);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private VBox buildCenterPanel() {
        configPanel = new ConfigPanelView(eventLogView);
        configPanel.setOnUnidadSeleccionada(unidadSel -> {
            for (UnidadCardView cv : cardViews) {
                cv.setSeleccionado(cv.getUnidad().equals(unidadSel));
            }
        });
        return configPanel.getRoot();
    }

    private VBox buildEventLog() {
        return eventLogView.getRoot();
    }

    private void actualizarContador() {
        long operacionales = FleetConfig.getUnidades().stream()
                .filter(u -> u.isActiva()
                        && u.getLatitud() != null
                        && u.getLongitud() != null)
                .count();
        labelContador.setText(operacionales + " unidades activas");
    }
}

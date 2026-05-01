package com.qsolutions.gpsclient.ui.view;

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

    /**
     * Constructs the dashboard and assembles all layout regions.
     */
    public DashboardView() {
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

    // -------------------------------------------------------------------------
    // Region builders
    // -------------------------------------------------------------------------

    private VBox buildHeader() {
        Label title = new Label("QSolutions Fleet Tracker");
        title.getStyleClass().add("label-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label unitCount = new Label("5 unidades activas");
        unitCount.getStyleClass().add("label-secondary");

        HBox titleBar = new HBox(12, title, spacer, unitCount);

        StatisticsCardView stats = new StatisticsCardView();

        VBox topContainer = new VBox(12, titleBar, new Separator(), stats.getRoot());
        topContainer.getStyleClass().add("surface-pane");
        topContainer.setPadding(new Insets(16));
        return topContainer;
    }

    private ScrollPane buildUnitPanel() {
        VBox cards = new VBox(12);
        cards.setPadding(new Insets(16));

        UnidadCardView[] unidades = {
            new UnidadCardView("Peugeot",  "06:00", "16:00", "Manager",  true),
            new UnidadCardView("Kangoo",   "07:00", "17:00", "Manager",  true),
            new UnidadCardView("Tr-02",    "07:00", "17:00", "Manager",  true),
            new UnidadCardView("Attitude", "Manual", "Manual", "Operador", false),
            new UnidadCardView("Sentra",   "Manual", "Manual", "Operador", false),
        };
        for (int i = 0; i < unidades.length; i++) {
            VBox cardRoot = unidades[i].getRoot();
            CardAnimations.fadeInWithDelay(cardRoot, 400, i * 100);
            cards.getChildren().add(cardRoot);
        }

        ScrollPane scroll = new ScrollPane(cards);
        scroll.setPrefWidth(340);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private VBox buildCenterPanel() {
        ConfigPanelView configPanel = new ConfigPanelView();
        return configPanel.getRoot();
    }

    private VBox buildEventLog() {
        EventLogView eventLog = new EventLogView();
        return eventLog.getRoot();
    }
}

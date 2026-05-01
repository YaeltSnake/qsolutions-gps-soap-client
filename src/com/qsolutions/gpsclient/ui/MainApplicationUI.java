package com.qsolutions.gpsclient.ui;

import com.qsolutions.gpsclient.ui.view.DashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Main JavaFX application entry point for QSolutions Fleet Tracker.
 *
 * <p>Bootstraps the primary window with the Soft Dark theme and acts as the
 * composition root for all UI views.  DashboardView and other panels are
 * loaded here once implemented.</p>
 */
public class MainApplicationUI extends Application {

    private static final String APP_TITLE   = "QSolutions Fleet Tracker";
    private static final double PREF_WIDTH  = 1280;
    private static final double PREF_HEIGHT = 760;
    private static final double MIN_WIDTH   = 1100;
    private static final double MIN_HEIGHT  = 700;

    private static final String CSS_PATH =
            "/com/qsolutions/gpsclient/ui/styles/application.css";

    /**
     * Initialises and shows the primary {@link Stage}.
     *
     * <p>Sets window dimensions, loads the application stylesheet, builds a
     * temporary placeholder layout, and centres the window on the primary
     * screen.</p>
     *
     * @param primaryStage the stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        DashboardView dashboard = new DashboardView();
        BorderPane root = dashboard.getRoot();

        Scene scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        applyStylesheet(scene);

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setScene(scene);

        centerOnScreen(primaryStage);

        primaryStage.show();
    }

    /**
     * Loads the application CSS from the classpath.  Logs a warning to
     * stderr if the resource cannot be found so the app still starts
     * without styles rather than crashing.
     *
     * @param scene the scene that will receive the stylesheet
     */
    private void applyStylesheet(Scene scene) {
        var resource = getClass().getResource(CSS_PATH);
        if (resource != null) {
            scene.getStylesheets().add(resource.toExternalForm());
        } else {
            System.err.println("[MainApplicationUI] CSS not found: " + CSS_PATH);
        }
    }

    /**
     * Centres the given stage on the primary screen using the screen's
     * visual bounds (excludes OS taskbars).
     *
     * @param stage the stage to centre
     */
    private void centerOnScreen(Stage stage) {
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth()  - PREF_WIDTH)  / 2);
        stage.setY((bounds.getHeight() - PREF_HEIGHT) / 2);
    }

    /**
     * Application entry point.  Delegates to {@link Application#launch} so
     * the JavaFX runtime initialises correctly regardless of the JVM version.
     *
     * @param args command-line arguments forwarded to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}

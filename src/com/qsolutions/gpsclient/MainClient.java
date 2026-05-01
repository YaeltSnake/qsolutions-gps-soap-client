package com.qsolutions.gpsclient;

import com.qsolutions.gpsclient.ui.MainApplicationUI;
import javafx.application.Application;

/**
 * Entry point for the QSolutions GPS Fleet Tracking Service.
 *
 * <p>Launches the JavaFX desktop UI which provides interactive
 * configuration, manual pulse dispatch, and live event monitoring
 * for all registered fleet units.</p>
 *
 * <p>The previous CLI-based scheduler has been replaced by the UI.
 * Background scheduling will be reintroduced in a future iteration
 * via FleetScheduler integrated with the UI lifecycle.</p>
 */
public class MainClient {

    public static void main(String[] args) {
        Application.launch(MainApplicationUI.class, args);
    }
}

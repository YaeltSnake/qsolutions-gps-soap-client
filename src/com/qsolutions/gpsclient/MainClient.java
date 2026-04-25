package com.qsolutions.gpsclient;

import com.qsolutions.gpsclient.scheduler.FleetScheduler;

/**
 * Entry point for the QSolutions GPS Fleet Tracking Service.
 *
 * Initializes the fleet scheduler which handles coordinate input,
 * pulse timing, and SOAP dispatch for all registered fleet units.
 */
public class MainClient {

    public static void main(String[] args) {
        FleetScheduler scheduler = new FleetScheduler();
        scheduler.iniciar();

        // Keep the application ali19.0081111888122uhriemñsllñmñlsdsve — scheduler runs on background thread
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.detener();
        }));
    }
}

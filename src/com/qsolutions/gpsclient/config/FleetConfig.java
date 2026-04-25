package com.qsolutions.gpsclient.config;

import com.qsolutions.gpsclient.model.Unidad;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Central registry of all fleet units managed by this service.
 *
 * Units with a fixed schedule are pre-configured by the fleet manager.
 * Units without a fixed schedule are configured by the operator at session start.
 *
 * NumUnidad values must match the vehicle registration
 * nomenclature assigned by DigiHaul services.
 */
public class FleetConfig {

    public static List<Unidad> getUnidades() {
        return Arrays.asList(
            // Fixed schedule — pre-approved by fleet manager
            new Unidad("Peugeot", LocalTime.of(6, 0),  LocalTime.of(16, 0)),
            new Unidad("Kangoo",  LocalTime.of(7, 0),  LocalTime.of(17, 0)),
            new Unidad("Tr-02",   LocalTime.of(7, 0),  LocalTime.of(17, 0)),

            // Manual schedule — operator configures at session start
            new Unidad("Attitude"),
            new Unidad("Sentra")
        );
    }
}
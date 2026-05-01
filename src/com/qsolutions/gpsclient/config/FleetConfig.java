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

    private static final List<Unidad> UNIDADES = Arrays.asList(
        new Unidad("Peugeot", LocalTime.of(6, 0),  LocalTime.of(16, 0)),
        new Unidad("Kangoo",  LocalTime.of(7, 0),  LocalTime.of(17, 0)),
        new Unidad("Tr-02",   LocalTime.of(7, 0),  LocalTime.of(17, 0)),
        new Unidad("Attitude"),
        new Unidad("Sentra")
    );

    public static List<Unidad> getUnidades() {
        return UNIDADES;
    }

    public static Unidad getByName(String numUnidad) {
        return UNIDADES.stream()
                .filter(u -> u.getNumUnidad().equals(numUnidad))
                .findFirst()
                .orElse(null);
    }

    private FleetConfig() {}
}

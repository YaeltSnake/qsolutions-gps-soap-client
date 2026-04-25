package com.qsolutions.gpsclient.config;

import com.qsolutions.gpsclient.model.Unidad;
import java.util.Arrays;
import java.util.List;

/**
 * Central registry of all fleet units managed by this service.
 *
 * NumUnidad values must match the vehicle registration
 * nomenclature assigned by DigiHaul services.
 */

public class FleetConfig {
    
    // Nuestras unidades
    public static List<Unidad> getUnidades() {
        return Arrays.asList(
            new Unidad("Peugeot"),
            new Unidad("Attitude"),
            new Unidad("Sentra"),
            new Unidad("Kangoo"),
            new Unidad("Tr-02")
        );
    }
}

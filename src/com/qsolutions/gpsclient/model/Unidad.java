package com.qsolutions.gpsclient.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Represents a fleet vehicle unit participating in GPS tracking.
 *
 * Each unit has its own operational window and active status,
 * allowing independent scheduling per vehicle.
 *
 * Phase 2: coordinates will be sourced automatically via Flespi/SinoTrack API.
 */
public class Unidad {

    private final String numUnidad;
    private BigDecimal latitud;
    private BigDecimal longitud;

    // Per-unit operational window
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Whether this unit participates in today's pulse cycle
    private boolean activa;

    public Unidad(String numUnidad) {
        this.numUnidad  = numUnidad;
        this.horaInicio = LocalTime.of(8, 0);   // default 8am
        this.horaFin    = LocalTime.of(18, 0);  // default 6pm
        this.activa     = false; // inactive by default — operator must activate explicitly
    }

    // --- Getters ---
    public String getNumUnidad()    { return numUnidad; }
    public BigDecimal getLatitud()  { return latitud; }
    public BigDecimal getLongitud() { return longitud; }
    public LocalTime getHoraInicio(){ return horaInicio; }
    public LocalTime getHoraFin()   { return horaFin; }
    public boolean isActiva()       { return activa; }

    // --- Setters ---
    public void setLatitud(BigDecimal latitud)    { this.latitud = latitud; }
    public void setLongitud(BigDecimal longitud)  { this.longitud = longitud; }
    public void setHoraInicio(LocalTime hora)     { this.horaInicio = hora; }
    public void setHoraFin(LocalTime hora)        { this.horaFin = hora; }
    public void setActiva(boolean activa)         { this.activa = activa; }

    /**
     * Returns true if the current time falls within this unit's operational window.
     */
    public boolean estaEnHorario() {
        LocalTime ahora = LocalTime.now();
        return !ahora.isBefore(horaInicio) && !ahora.isAfter(horaFin);
    }

    @Override
    public String toString() {
        return String.format("Unidad{id='%s', activa=%s, horario=%s-%s, lat=%s, lon=%s}",
                numUnidad, activa, horaInicio, horaFin, latitud, longitud);
    }
}

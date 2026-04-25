package com.qsolutions.gpsclient.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Represents a fleet vehicle unit participating in GPS tracking.
 *
 * Units can have a fixed operational window (predefined by fleet manager)
 * or a manually configured one set by the operator at session start.
 *
 * Phase 2: coordinates will be sourced automatically via Flespi/SinoTrack API.
 */
public class Unidad {

    private final String numUnidad;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean activa;
    private final boolean horarioFijo; // true = schedule set by fleet manager, not operator

    /** Constructor for units with manually configured schedule. */
    public Unidad(String numUnidad) {
        this.numUnidad   = numUnidad;
        this.horaInicio  = LocalTime.of(8, 0);
        this.horaFin     = LocalTime.of(18, 0);
        this.activa      = false;
        this.horarioFijo = false;
    }

    /** Constructor for units with a fixed predefined schedule. */
    public Unidad(String numUnidad, LocalTime horaInicio, LocalTime horaFin) {
        this.numUnidad   = numUnidad;
        this.horaInicio  = horaInicio;
        this.horaFin     = horaFin;
        this.activa      = false;
        this.horarioFijo = true;
    }

    // --- Getters ---
    public String getNumUnidad()     { return numUnidad; }
    public BigDecimal getLatitud()   { return latitud; }
    public BigDecimal getLongitud()  { return longitud; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin()    { return horaFin; }
    public boolean isActiva()        { return activa; }
    public boolean isHorarioFijo()   { return horarioFijo; }

    // --- Setters ---
    public void setLatitud(BigDecimal latitud)   { this.latitud = latitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
    public void setHoraInicio(LocalTime hora)    { this.horaInicio = hora; }
    public void setHoraFin(LocalTime hora)       { this.horaFin = hora; }
    public void setActiva(boolean activa)        { this.activa = activa; }

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

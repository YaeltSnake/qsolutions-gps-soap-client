package com.qsolutions.gpsclient.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

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
    private final boolean horarioFijo; // true = schedule set by fleet manager, not operator

    private final ObjectProperty<BigDecimal> latitud    = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> longitud   = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime>  horaInicio = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime>  horaFin    = new SimpleObjectProperty<>();
    private final BooleanProperty            activa     = new SimpleBooleanProperty(false);

    /** Constructor for units with manually configured schedule. */
    public Unidad(String numUnidad) {
        this.numUnidad   = numUnidad;
        this.horarioFijo = false;
        this.horaInicio.set(LocalTime.of(8, 0));
        this.horaFin.set(LocalTime.of(18, 0));
        this.activa.set(false);
    }

    /** Constructor for units with a fixed predefined schedule. */
    public Unidad(String numUnidad, LocalTime horaInicio, LocalTime horaFin) {
        this.numUnidad   = numUnidad;
        this.horarioFijo = true;
        this.horaInicio.set(horaInicio);
        this.horaFin.set(horaFin);
        this.activa.set(false);
    }

    // --- Getters ---
    public String     getNumUnidad()  { return numUnidad; }
    public boolean    isHorarioFijo() { return horarioFijo; }
    public BigDecimal getLatitud()    { return latitud.get(); }
    public BigDecimal getLongitud()   { return longitud.get(); }
    public LocalTime  getHoraInicio() { return horaInicio.get(); }
    public LocalTime  getHoraFin()    { return horaFin.get(); }
    public boolean    isActiva()      { return activa.get(); }

    // --- Setters ---
    public void setLatitud(BigDecimal v)   { this.latitud.set(v); }
    public void setLongitud(BigDecimal v)  { this.longitud.set(v); }
    public void setHoraInicio(LocalTime v) { this.horaInicio.set(v); }
    public void setHoraFin(LocalTime v)    { this.horaFin.set(v); }
    public void setActiva(boolean v)       { this.activa.set(v); }

    // --- JavaFX property accessors ---
    public ObjectProperty<BigDecimal> latitudProperty()    { return latitud; }
    public ObjectProperty<BigDecimal> longitudProperty()   { return longitud; }
    public ObjectProperty<LocalTime>  horaInicioProperty() { return horaInicio; }
    public ObjectProperty<LocalTime>  horaFinProperty()    { return horaFin; }
    public BooleanProperty            activaProperty()     { return activa; }

    /**
     * Returns true if the current time falls within this unit's operational window.
     */
    public boolean estaEnHorario() {
        LocalTime ahora = LocalTime.now();
        return !ahora.isBefore(horaInicio.get()) && !ahora.isAfter(horaFin.get());
    }

    @Override
    public String toString() {
        return String.format("Unidad{id='%s', activa=%s, horario=%s-%s, lat=%s, lon=%s}",
                numUnidad, activa.get(), horaInicio.get(), horaFin.get(),
                latitud.get(), longitud.get());
    }
}

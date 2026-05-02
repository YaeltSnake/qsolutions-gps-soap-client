package com.qsolutions.gpsclient.ui.scheduler;

import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.service.GpsSoapService;
import com.qsolutions.gpsclient.ui.view.EventLogView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.tempuri.Protocolo;
import java.time.LocalTime;

public class UnitScheduler {

    public enum State { IDLE, RUNNING, STOPPED, EXPIRED }

    private static final int INTERVALO_SEGUNDOS = 900;
    private static final GpsSoapService gpsService = new GpsSoapService();

    private final Unidad unidad;
    private final EventLogView eventLog;

    private Timeline pulseTimer;
    private Timeline countdownTimer;
    private int segundosRestantes = INTERVALO_SEGUNDOS;
    private int pulsosEnviados = 0;
    private int pulsosTotalesEstimados = 0;
    private State estado = State.IDLE;

    private Runnable onTick;
    private Runnable onPulseSent;
    private Runnable onStateChange;

    public UnitScheduler(Unidad unidad, EventLogView eventLog) {
        this.unidad = unidad;
        this.eventLog = eventLog;
    }

    public void start() {
        if (estado == State.RUNNING) return;
        if (unidad.getLatitud() == null || unidad.getLongitud() == null) {
            eventLog.agregarEvento(unidad.getNumUnidad(), "✗ Error",
                "No se puede iniciar ronda sin coordenadas");
            return;
        }

        if (haExpirado()) {
            eventLog.agregarEvento(unidad.getNumUnidad(), "⏱ Expirada",
                "No se puede iniciar — horario ya terminó");
            estado = State.EXPIRED;
            notifyStateChange();
            return;
        }

        pulsosTotalesEstimados = calcularPulsosRestantes();
        pulsosEnviados = 0;
        segundosRestantes = INTERVALO_SEGUNDOS;
        estado = State.RUNNING;

        enviarPulso(false);

        pulseTimer = new Timeline(new KeyFrame(Duration.seconds(INTERVALO_SEGUNDOS), e -> {
            if (haExpirado()) {
                expire();
            } else {
                enviarPulso(false);
                segundosRestantes = INTERVALO_SEGUNDOS;
            }
        }));
        pulseTimer.setCycleCount(Timeline.INDEFINITE);
        pulseTimer.play();

        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundosRestantes--;
            if (segundosRestantes < 0) segundosRestantes = INTERVALO_SEGUNDOS;
            if (onTick != null) onTick.run();

            if (haExpirado()) expire();
        }));
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();

        notifyStateChange();
        eventLog.agregarEvento(unidad.getNumUnidad(), "▶ Iniciada",
            "Ronda iniciada — pulsos cada 15 min");
    }

    public void stop() {
        if (pulseTimer != null) pulseTimer.stop();
        if (countdownTimer != null) countdownTimer.stop();
        estado = State.STOPPED;
        notifyStateChange();
        eventLog.agregarEvento(unidad.getNumUnidad(), "⏹ Detenida",
            "Ronda detenida por el operador");
    }

    private void expire() {
        if (pulseTimer != null) pulseTimer.stop();
        if (countdownTimer != null) countdownTimer.stop();
        estado = State.EXPIRED;
        notifyStateChange();
        eventLog.agregarEvento(unidad.getNumUnidad(), "⏱ Expirada",
            "Horario terminó — sesión finalizada");
    }

    public void forcePulse() {
        if (estado != State.RUNNING) return;
        enviarPulso(true);
    }

    private void enviarPulso(boolean forzado) {
        Task<Protocolo> task = new Task<>() {
            @Override
            protected Protocolo call() {
                return gpsService.enviarPulsacion(unidad);
            }
        };

        task.setOnSucceeded(ev -> {
            Protocolo respuesta = task.getValue();
            if (respuesta != null && respuesta.isProcessed()) {
                pulsosEnviados++;
                String prefijo = forzado ? "⚡ Forzado" : "✓ Enviado";
                eventLog.agregarEvento(unidad.getNumUnidad(), prefijo,
                    "GPS pulse: " + unidad.getLatitud() + ", " + unidad.getLongitud());
                if (onPulseSent != null) onPulseSent.run();
            } else {
                String msg = (respuesta != null && respuesta.getMessage() != null)
                    ? respuesta.getMessage() : "Sin respuesta del servidor";
                eventLog.agregarEvento(unidad.getNumUnidad(), "⚠ Rechazado", msg);
            }
        });

        task.setOnFailed(ev -> {
            Throwable ex = task.getException();
            eventLog.agregarEvento(unidad.getNumUnidad(), "✗ Error",
                ex != null ? ex.getMessage() : "Error desconocido");
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private boolean haExpirado() {
        if (!unidad.isHorarioFijo()) return false;
        return LocalTime.now().isAfter(unidad.getHoraFin());
    }

    private int calcularPulsosRestantes() {
        if (!unidad.isHorarioFijo()) return -1;
        long segundosHastaFin = java.time.Duration.between(
            LocalTime.now(), unidad.getHoraFin()).getSeconds();
        if (segundosHastaFin <= 0) return 0;
        return (int) (segundosHastaFin / INTERVALO_SEGUNDOS) + 1;
    }

    private void notifyStateChange() {
        if (onStateChange != null) onStateChange.run();
    }

    public State getState() { return estado; }
    public int getSegundosRestantes() { return segundosRestantes; }
    public int getPulsosEnviados() { return pulsosEnviados; }
    public int getPulsosTotalesEstimados() { return pulsosTotalesEstimados; }
    public Unidad getUnidad() { return unidad; }

    public void setOnTick(Runnable r) { this.onTick = r; }
    public void setOnPulseSent(Runnable r) { this.onPulseSent = r; }
    public void setOnStateChange(Runnable r) { this.onStateChange = r; }
}

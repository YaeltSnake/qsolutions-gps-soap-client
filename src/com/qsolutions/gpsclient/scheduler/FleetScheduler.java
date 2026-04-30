package com.qsolutions.gpsclient.scheduler;

import com.qsolutions.gpsclient.config.FleetConfig;
import com.qsolutions.gpsclient.model.Unidad;
import com.qsolutions.gpsclient.service.GpsSoapService;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Orchestrates periodic GPS pulse dispatch for all active fleet units.
 *
 * Each unit has its own operational window and active status.
 * The operator configures units individually at the start of each session.
 *
 * Pulse cycle: every 15 minutes, only for active units within their window.
 * Phase 2: coordinates sourced automatically via Flespi/SinoTrack API.
 */
public class FleetScheduler {

    private static final Logger log = LoggerFactory.getLogger(FleetScheduler.class);
    private static final int INTERVALO_MINUTOS = 15;

    private final GpsSoapService soapService;
    private final List<Unidad> todasLasUnidades;
    private final ScheduledExecutorService executor;
    private final Scanner scanner;

    public FleetScheduler() {
        this.soapService      = new GpsSoapService();
        this.todasLasUnidades = FleetConfig.getUnidades();
        this.executor         = Executors.newSingleThreadScheduledExecutor();
        this.scanner          = new Scanner(System.in);
    }

    /**
     * Starts the interactive configuration session, then launches the pulse cycle.
     */
    public void iniciar() {
        mostrarBienvenida();
        configurarUnidades();
        
        List<Unidad> activas = getUnidadesActivas();
        
        if (activas.isEmpty()) {
            log.warn("No hay unidades activas. Cerrando servicio.");
            System.out.println("\n⚠ No hay unidades activas. Cerrando servicio.");
            return;
        }

        mostrarResumenSesion(activas);
        log.info("Sesion iniciada — {} unidades activas.", activas.size());

        executor.scheduleAtFixedRate(
                this::enviarRondaDePulsaciones,
                0,
                INTERVALO_MINUTOS,
                TimeUnit.MINUTES
        );
    }

    // ---------------------------------------------------------------
    // CONFIGURACIÓN INTERACTIVA
    // ---------------------------------------------------------------

    /**
     * Walks the operator through activating and configuring each unit.
     */
    private void configurarUnidades() {
        System.out.println("\n📋 CONFIGURACIÓN DE UNIDADES");
        System.out.println("===========================================");
        System.out.println("Configura cada unidad para la sesión de hoy.");
        System.out.println("Las unidades inactivas no enviarán pulsaciones.\n");

        for (Unidad unidad : todasLasUnidades) {
            configurarUnidad(unidad);
        }
    }

    /**
     * Configures a single unit: activation, schedule, and coordinates.
     */
    private void configurarUnidad(Unidad unidad) {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│ Unidad: " + unidad.getNumUnidad()
                + (unidad.isHorarioFijo() ? " [Horario fijo]" : ""));
        System.out.println("└─────────────────────────────────────┘");

        // Step 1 — Activate or skip
        boolean activar = leerSiNo("  ¿Activar esta unidad hoy? (s/n): ");
        if (!activar) {
            System.out.println("  Unidad omitida.\n");
            return;
        }
        unidad.setActiva(true);

        // Step 2 — Operational window (skip if fixed)
        if (unidad.isHorarioFijo()) {
            System.out.println("  Horario: " + unidad.getHoraInicio()
                    + " - " + unidad.getHoraFin() + " (predefinido)");
        } else {
            System.out.println("  Horario de operacion (Enter para usar default):");
            LocalTime inicio = leerHora("  Hora inicio [08:00]: ", LocalTime.of(8, 0));
            LocalTime fin = leerHora("  Hora fin    [18:00]: ", LocalTime.of(18, 0));
            unidad.setHoraInicio(inicio);
            unidad.setHoraFin(fin);
        }

        // Step 3 — Coordinates
        System.out.println("  Coordenadas iniciales:");
        BigDecimal lat = leerCoordenada("  Latitud  (ej: 19.432608): ");
        BigDecimal lon = leerCoordenada("  Longitud (ej: -99.133209): ");
        unidad.setLatitud(lat);
        unidad.setLongitud(lon);

        System.out.println("  ✓ " + unidad.getNumUnidad()
                + " configurada - " + unidad.getHoraInicio()
                + " a " + unidad.getHoraFin() + "\n");
    }

    // ---------------------------------------------------------------
    // CICLO DE PULSACIONES
    // ---------------------------------------------------------------

    /**
     * Sends one GPS pulse per active unit that is within its operational window.
     * Units outside their window are skipped silently.
     */
    private void enviarRondaDePulsaciones() {
        LocalTime ahora = LocalTime.now();
        log.info("Iniciando ronda de pulsaciones a las {}.", ahora);

        List<Unidad> activas = getUnidadesActivas();

        if (activas.isEmpty()) {
            log.warn("No hay unidades activas. Deteniendo scheduler.");
            detener();
            return;
        }

        int enviadas = 0;
        for (Unidad unidad : activas) {
            if (unidad.estaEnHorario()) {
                soapService.enviarPulsacion(unidad);
                enviadas++;
            } else {
                log.debug("[{}] Fuera de horario ({}-{}) — omitida.",
                        unidad.getNumUnidad(), unidad.getHoraInicio(), unidad.getHoraFin());
            }
        }

        log.info("Ronda completada — {} pulsaciones enviadas. Proxima en {} min.", enviadas, INTERVALO_MINUTOS);
    }

    // ---------------------------------------------------------------
    // HELPERS DE INPUT
    // ---------------------------------------------------------------

    private boolean leerSiNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("s") || input.equals("si") || input.equals("sí")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("  ✗ Ingresa 's' para sí o 'n' para no.");
        }
    }

    private LocalTime leerHora(String prompt, LocalTime valorDefault) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return valorDefault;
        try {
            return LocalTime.parse(input);  // expects HH:mm format
        } catch (DateTimeParseException e) {
            System.out.println("  ✗ Formato inválido. Usando default: " + valorDefault);
            return valorDefault;
        }
    }

    private BigDecimal leerCoordenada(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Valor inválido. Ingresa un decimal (ej: 19.366138)");
            }
        }
    }

    // ---------------------------------------------------------------
    // HELPERS INTERNOS
    // ---------------------------------------------------------------

    private List<Unidad> getUnidadesActivas() {
        return todasLasUnidades.stream()
                .filter(Unidad::isActiva)
                .collect(Collectors.toList());
    }

    private void mostrarBienvenida() {
        System.out.println("===========================================");
        System.out.println("  QSolutions GPS Fleet Tracking Service   ");
        System.out.println("  Proveedor: Digi-Haul                    ");
        System.out.println("===========================================");
    }

    private void mostrarResumenSesion(List<Unidad> activas) {
        System.out.println("\n✓ SESIÓN INICIADA");
        System.out.println("===========================================");
        activas.forEach(u -> System.out.println(
                "  • " + u.getNumUnidad() + 
                " | " + u.getHoraInicio() + "-" + u.getHoraFin() +
                " | " + u.getLatitud() + ", " + u.getLongitud()));
        System.out.println("===========================================");
        System.out.println("Pulsaciones cada " + INTERVALO_MINUTOS + 
                " min. Ctrl+C para detener.\n");
    }

    /**
     * Gracefully shuts down the pulse scheduler.
     */
    public void detener() {
        log.info("Deteniendo servicio...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Servicio detenido correctamente.");
    }
}

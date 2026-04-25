package com.qsolutions.gpsclient.service;

import com.qsolutions.gpsclient.util.DateUtils;
import com.qsolutions.gpsclient.model.Unidad;
//import com.qsolutions.gpsclient.util.DateUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.tempuri.GPSInfo;
import org.tempuri.Protocolo;
import org.tempuri.ReceiveGPSInfo;
import org.tempuri.ReceiveGPSInfoSoap;

/**
 * Service responsible for building and dispatching GPS pulses
 * to the QSolutions SOAP endpoint.
 *
 * Each call to {@link #enviarPulsacion(Unidad)} constructs a
 * fresh GPS payload with real-time timestamps and the current
 * coordinates of the given fleet unit.
 */

public class GpsSoapService {
    
    private final String username;
    private final String password;
    private final String proveedor;

    /**
     * Loads service credentials from config.properties at startup.
     * Credentials are never hardcoded — they live outside source control.
     */
    public GpsSoapService() {
        Properties props = new Properties();
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException(
                    "[GpsSoapService] config.properties not found in classpath. " +
                    "Copy config.properties.example to config.properties and fill in your credentials."
                );
            }
            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("[GpsSoapService] Failed to load config.properties", e);
        }

        this.username = props.getProperty("qsolutions.username");
        this.password = props.getProperty("qsolutions.password");
        this.proveedor = props.getProperty("qsolutions.proveedor");
    }

    /**
     * Sends a single GPS pulse for the given unit.
     * Timestamps are generated at the moment of the call (real-time).
     *
     * @param unidad the fleet unit with current coordinates
     * @return the SOAP response from QSolutions, or null if the call failed
     */
    public Protocolo enviarPulsacion(Unidad unidad) {
        try {
            GPSInfo info = new GPSInfo();
            info.setUsername(username);
            info.setPassword(password);
            info.setProveedor(proveedor);
            info.setNumUnidad(unidad.getNumUnidad());
            info.setLatitud(unidad.getLatitud());
            info.setLongitud(unidad.getLongitud());
            info.setTrackingnumber("1");

            // Real-time timestamps — generated at the exact moment of each pulse
            info.setFechaHoraEvento(DateUtils.now());
            info.setFechaRecepcion(DateUtils.now());

            ReceiveGPSInfo service = new ReceiveGPSInfo();
            ReceiveGPSInfoSoap port = service.getReceiveGPSInfoSoap();
            Protocolo respuesta = port.receiveGPSInformationObjeto(info);

            System.out.println("[" + unidad.getNumUnidad() + "] Pulsación enviada — " +
                    "Procesado: " + respuesta.isProcessed() +
                    " | Mensaje: " + respuesta.getMessage());

            return respuesta;

        } catch (Exception e) {
            System.err.println("[" + unidad.getNumUnidad() + "] ERROR al enviar pulsación: "
                    + e.getMessage());
            return null;
        }
    }
}

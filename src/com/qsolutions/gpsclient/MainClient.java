package com.qsolutions.gpsclient;

import java.math.BigDecimal;
import java.util.Date;
import org.tempuri.GPSInfo;
import org.tempuri.ReceiveGPSInfoSoap;
import org.tempuri.Protocolo;
import org.tempuri.ReceiveGPSInfo;

public class MainClient {
    public static void main(String[] args) {
        // Importaremos las clases generadas 
        GPSInfo info = new GPSInfo();
        info.setProveedor("Digi-Haul");
        info.setLatitud( new BigDecimal("19.366138"));
        info.setLongitud(new BigDecimal("-99.180295"));
        info.setNumUnidad("CarroPrueba");
        info.setTrackingnumber("1");
        info.setFechaHoraEvento(DateUtils.buiXMLGregorianCalendar(2026, 4, 06, 15, 29, 22));
        info.setFechaRecepcion(DateUtils.buiXMLGregorianCalendar(2026, 4, 06, 15, 30, 44));
        info.setUsername("UsuarioPrueba");
        info.setPassword("passworddd");
        
        // Crear cliente del servicio
        ReceiveGPSInfo service = new ReceiveGPSInfo();
        ReceiveGPSInfoSoap port = service.getReceiveGPSInfoSoap();
        
        // Lammar al metodo SOAP
        Protocolo resp = port.receiveGPSInformationObjeto(info);
        
        // Nos mostrara la respuesta
        System.out.println("ReceptionDate: " + resp.getReceptionDate());
        System.out.println("Message: " + resp.getMessage());
        System.out.println("Processed: " + resp.isProcessed());
               
        
        
    }
    
}

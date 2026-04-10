
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GPSInformation" type="{http://tempuri.org/}ArrayOfGPSInfo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "gpsInformation"
})
@XmlRootElement(name = "ReceiveGPSInformation_Objetos")
public class ReceiveGPSInformationObjetos {

    @XmlElement(name = "GPSInformation")
    protected ArrayOfGPSInfo gpsInformation;

    /**
     * Obtiene el valor de la propiedad gpsInformation.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfGPSInfo }
     *     
     */
    public ArrayOfGPSInfo getGPSInformation() {
        return gpsInformation;
    }

    /**
     * Define el valor de la propiedad gpsInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfGPSInfo }
     *     
     */
    public void setGPSInformation(ArrayOfGPSInfo value) {
        this.gpsInformation = value;
    }

}

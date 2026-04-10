
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
 *         &lt;element name="ReceiveGPSInformation_ObjetoResult" type="{http://tempuri.org/}Protocolo" minOccurs="0"/&gt;
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
    "receiveGPSInformationObjetoResult"
})
@XmlRootElement(name = "ReceiveGPSInformation_ObjetoResponse")
public class ReceiveGPSInformationObjetoResponse {

    @XmlElement(name = "ReceiveGPSInformation_ObjetoResult")
    protected Protocolo receiveGPSInformationObjetoResult;

    /**
     * Obtiene el valor de la propiedad receiveGPSInformationObjetoResult.
     * 
     * @return
     *     possible object is
     *     {@link Protocolo }
     *     
     */
    public Protocolo getReceiveGPSInformationObjetoResult() {
        return receiveGPSInformationObjetoResult;
    }

    /**
     * Define el valor de la propiedad receiveGPSInformationObjetoResult.
     * 
     * @param value
     *     allowed object is
     *     {@link Protocolo }
     *     
     */
    public void setReceiveGPSInformationObjetoResult(Protocolo value) {
        this.receiveGPSInformationObjetoResult = value;
    }

}

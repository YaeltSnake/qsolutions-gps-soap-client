
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


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
 *         &lt;element name="ReceiveGPSInformationMassiveXMLResult" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;any processContents='lax'/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "receiveGPSInformationMassiveXMLResult"
})
@XmlRootElement(name = "ReceiveGPSInformationMassiveXMLResponse")
public class ReceiveGPSInformationMassiveXMLResponse {

    @XmlElement(name = "ReceiveGPSInformationMassiveXMLResult")
    protected ReceiveGPSInformationMassiveXMLResponse.ReceiveGPSInformationMassiveXMLResult receiveGPSInformationMassiveXMLResult;

    /**
     * Obtiene el valor de la propiedad receiveGPSInformationMassiveXMLResult.
     * 
     * @return
     *     possible object is
     *     {@link ReceiveGPSInformationMassiveXMLResponse.ReceiveGPSInformationMassiveXMLResult }
     *     
     */
    public ReceiveGPSInformationMassiveXMLResponse.ReceiveGPSInformationMassiveXMLResult getReceiveGPSInformationMassiveXMLResult() {
        return receiveGPSInformationMassiveXMLResult;
    }

    /**
     * Define el valor de la propiedad receiveGPSInformationMassiveXMLResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiveGPSInformationMassiveXMLResponse.ReceiveGPSInformationMassiveXMLResult }
     *     
     */
    public void setReceiveGPSInformationMassiveXMLResult(ReceiveGPSInformationMassiveXMLResponse.ReceiveGPSInformationMassiveXMLResult value) {
        this.receiveGPSInformationMassiveXMLResult = value;
    }


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
     *         &lt;any processContents='lax'/&gt;
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
        "any"
    })
    public static class ReceiveGPSInformationMassiveXMLResult {

        @XmlAnyElement(lax = true)
        protected Object any;

        /**
         * Obtiene el valor de la propiedad any.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     {@link Element }
         *     
         */
        public Object getAny() {
            return any;
        }

        /**
         * Define el valor de la propiedad any.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     {@link Element }
         *     
         */
        public void setAny(Object value) {
            this.any = value;
        }

    }

}

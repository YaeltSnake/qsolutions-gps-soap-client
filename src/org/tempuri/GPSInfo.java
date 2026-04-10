
package org.tempuri;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para GPSInfo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="GPSInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parametrosAdicionales" type="{http://tempuri.org/}ArrayOfParametroAdicional" minOccurs="0"/&gt;
 *         &lt;element name="Proveedor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Latitud" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="Longitud" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="NumUnidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumRemolque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Velocidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Placas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Trackingnumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ubicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FechaHoraEvento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="FechaRecepcion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BOL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GPSInfo", propOrder = {
    "parametrosAdicionales",
    "proveedor",
    "latitud",
    "longitud",
    "numUnidad",
    "numRemolque",
    "velocidad",
    "placas",
    "trackingnumber",
    "ubicacion",
    "fechaHoraEvento",
    "fechaRecepcion",
    "username",
    "password",
    "ruta",
    "bol",
    "observaciones"
})
public class GPSInfo {

    protected ArrayOfParametroAdicional parametrosAdicionales;
    @XmlElement(name = "Proveedor")
    protected String proveedor;
    @XmlElement(name = "Latitud", required = true)
    protected BigDecimal latitud;
    @XmlElement(name = "Longitud", required = true)
    protected BigDecimal longitud;
    @XmlElement(name = "NumUnidad")
    protected String numUnidad;
    @XmlElement(name = "NumRemolque")
    protected String numRemolque;
    @XmlElement(name = "Velocidad")
    protected String velocidad;
    @XmlElement(name = "Placas")
    protected String placas;
    @XmlElement(name = "Trackingnumber")
    protected String trackingnumber;
    @XmlElement(name = "Ubicacion")
    protected String ubicacion;
    @XmlElement(name = "FechaHoraEvento", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraEvento;
    @XmlElement(name = "FechaRecepcion", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaRecepcion;
    @XmlElement(name = "Username")
    protected String username;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "Ruta")
    protected String ruta;
    @XmlElement(name = "BOL")
    protected String bol;
    @XmlElement(name = "Observaciones")
    protected String observaciones;

    /**
     * Obtiene el valor de la propiedad parametrosAdicionales.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfParametroAdicional }
     *     
     */
    public ArrayOfParametroAdicional getParametrosAdicionales() {
        return parametrosAdicionales;
    }

    /**
     * Define el valor de la propiedad parametrosAdicionales.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfParametroAdicional }
     *     
     */
    public void setParametrosAdicionales(ArrayOfParametroAdicional value) {
        this.parametrosAdicionales = value;
    }

    /**
     * Obtiene el valor de la propiedad proveedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Define el valor de la propiedad proveedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProveedor(String value) {
        this.proveedor = value;
    }

    /**
     * Obtiene el valor de la propiedad latitud.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLatitud() {
        return latitud;
    }

    /**
     * Define el valor de la propiedad latitud.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLatitud(BigDecimal value) {
        this.latitud = value;
    }

    /**
     * Obtiene el valor de la propiedad longitud.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLongitud() {
        return longitud;
    }

    /**
     * Define el valor de la propiedad longitud.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLongitud(BigDecimal value) {
        this.longitud = value;
    }

    /**
     * Obtiene el valor de la propiedad numUnidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumUnidad() {
        return numUnidad;
    }

    /**
     * Define el valor de la propiedad numUnidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumUnidad(String value) {
        this.numUnidad = value;
    }

    /**
     * Obtiene el valor de la propiedad numRemolque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumRemolque() {
        return numRemolque;
    }

    /**
     * Define el valor de la propiedad numRemolque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumRemolque(String value) {
        this.numRemolque = value;
    }

    /**
     * Obtiene el valor de la propiedad velocidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVelocidad() {
        return velocidad;
    }

    /**
     * Define el valor de la propiedad velocidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVelocidad(String value) {
        this.velocidad = value;
    }

    /**
     * Obtiene el valor de la propiedad placas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlacas() {
        return placas;
    }

    /**
     * Define el valor de la propiedad placas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlacas(String value) {
        this.placas = value;
    }

    /**
     * Obtiene el valor de la propiedad trackingnumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingnumber() {
        return trackingnumber;
    }

    /**
     * Define el valor de la propiedad trackingnumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingnumber(String value) {
        this.trackingnumber = value;
    }

    /**
     * Obtiene el valor de la propiedad ubicacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Define el valor de la propiedad ubicacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUbicacion(String value) {
        this.ubicacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaHoraEvento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHoraEvento() {
        return fechaHoraEvento;
    }

    /**
     * Define el valor de la propiedad fechaHoraEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHoraEvento(XMLGregorianCalendar value) {
        this.fechaHoraEvento = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaRecepcion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaRecepcion() {
        return fechaRecepcion;
    }

    /**
     * Define el valor de la propiedad fechaRecepcion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaRecepcion(XMLGregorianCalendar value) {
        this.fechaRecepcion = value;
    }

    /**
     * Obtiene el valor de la propiedad username.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Define el valor de la propiedad username.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Obtiene el valor de la propiedad password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define el valor de la propiedad password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Obtiene el valor de la propiedad ruta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Define el valor de la propiedad ruta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuta(String value) {
        this.ruta = value;
    }

    /**
     * Obtiene el valor de la propiedad bol.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBOL() {
        return bol;
    }

    /**
     * Define el valor de la propiedad bol.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBOL(String value) {
        this.bol = value;
    }

    /**
     * Obtiene el valor de la propiedad observaciones.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Define el valor de la propiedad observaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservaciones(String value) {
        this.observaciones = value;
    }

}

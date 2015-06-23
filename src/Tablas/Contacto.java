/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Geek
 */
@Entity
@Table(name = "Contacto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contacto.findAll", query = "SELECT c FROM Contacto c"),
    @NamedQuery(name = "Contacto.findById", query = "SELECT c FROM Contacto c WHERE c.id = :id"),
    @NamedQuery(name = "Contacto.findByDireccion", query = "SELECT c FROM Contacto c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "Contacto.findByColonia", query = "SELECT c FROM Contacto c WHERE c.colonia = :colonia"),
    @NamedQuery(name = "Contacto.findByZona", query = "SELECT c FROM Contacto c WHERE c.zona = :zona"),
    @NamedQuery(name = "Contacto.findByCodigoPostal", query = "SELECT c FROM Contacto c WHERE c.codigoPostal = :codigoPostal")})
public class Contacto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "Colonia")
    private String colonia;
    @Column(name = "Zona")
    private String zona;
    @Column(name = "Codigo_Postal")
    private String codigoPostal;
    @JoinColumn(name = "Municipio_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Municipio municipioid;
    @JoinColumn(name = "Pais_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pais paisid;
    @JoinColumn(name = "Persona_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Persona personaid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactoid")
    private Collection<Correo> correoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactoid")
    private Collection<Idioma> idiomaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactoid")
    private Collection<Telefono> telefonoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactoid")
    private Collection<DatosLaborales> datosLaboralesCollection;

    public Contacto() {
    }

    public Contacto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Municipio getMunicipioid() {
        return municipioid;
    }

    public void setMunicipioid(Municipio municipioid) {
        this.municipioid = municipioid;
    }

    public Pais getPaisid() {
        return paisid;
    }

    public void setPaisid(Pais paisid) {
        this.paisid = paisid;
    }

    public Persona getPersonaid() {
        return personaid;
    }

    public void setPersonaid(Persona personaid) {
        this.personaid = personaid;
    }

    @XmlTransient
    public Collection<Correo> getCorreoCollection() {
        return correoCollection;
    }

    public void setCorreoCollection(Collection<Correo> correoCollection) {
        this.correoCollection = correoCollection;
    }

    @XmlTransient
    public Collection<Idioma> getIdiomaCollection() {
        return idiomaCollection;
    }

    public void setIdiomaCollection(Collection<Idioma> idiomaCollection) {
        this.idiomaCollection = idiomaCollection;
    }

    @XmlTransient
    public Collection<Telefono> getTelefonoCollection() {
        return telefonoCollection;
    }

    public void setTelefonoCollection(Collection<Telefono> telefonoCollection) {
        this.telefonoCollection = telefonoCollection;
    }

    @XmlTransient
    public Collection<DatosLaborales> getDatosLaboralesCollection() {
        return datosLaboralesCollection;
    }

    public void setDatosLaboralesCollection(Collection<DatosLaborales> datosLaboralesCollection) {
        this.datosLaboralesCollection = datosLaboralesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tablas.Contacto[ id=" + id + " ]";
    }
    
}

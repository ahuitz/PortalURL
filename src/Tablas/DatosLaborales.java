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
@Table(name = "Datos_Laborales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosLaborales.findAll", query = "SELECT d FROM DatosLaborales d"),
    @NamedQuery(name = "DatosLaborales.findById", query = "SELECT d FROM DatosLaborales d WHERE d.id = :id"),
    @NamedQuery(name = "DatosLaborales.findByNombreEmpresa", query = "SELECT d FROM DatosLaborales d WHERE d.nombreEmpresa = :nombreEmpresa"),
    @NamedQuery(name = "DatosLaborales.findByDireccion", query = "SELECT d FROM DatosLaborales d WHERE d.direccion = :direccion"),
    @NamedQuery(name = "DatosLaborales.findBySector", query = "SELECT d FROM DatosLaborales d WHERE d.sector = :sector"),
    @NamedQuery(name = "DatosLaborales.findByCantidadHoras", query = "SELECT d FROM DatosLaborales d WHERE d.cantidadHoras = :cantidadHoras")})
public class DatosLaborales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Nombre_Empresa")
    private String nombreEmpresa;
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "Sector")
    private String sector;
    @Column(name = "Cantidad_Horas")
    private String cantidadHoras;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datosLaboralesid")
    private Collection<Jornada> jornadaCollection;
    @JoinColumn(name = "Contacto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Contacto contactoid;
    @JoinColumn(name = "Cargo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cargo cargoid;

    public DatosLaborales() {
    }

    public DatosLaborales(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCantidadHoras() {
        return cantidadHoras;
    }

    public void setCantidadHoras(String cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    @XmlTransient
    public Collection<Jornada> getJornadaCollection() {
        return jornadaCollection;
    }

    public void setJornadaCollection(Collection<Jornada> jornadaCollection) {
        this.jornadaCollection = jornadaCollection;
    }

    public Contacto getContactoid() {
        return contactoid;
    }

    public void setContactoid(Contacto contactoid) {
        this.contactoid = contactoid;
    }

    public Cargo getCargoid() {
        return cargoid;
    }

    public void setCargoid(Cargo cargoid) {
        this.cargoid = cargoid;
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
        if (!(object instanceof DatosLaborales)) {
            return false;
        }
        DatosLaborales other = (DatosLaborales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tablas.DatosLaborales[ id=" + id + " ]";
    }
    
}

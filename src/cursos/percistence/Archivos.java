/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.percistence;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Diaz
 */
@Entity
@Table(name = "archivos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Archivos.findAll", query = "SELECT a FROM Archivos a"),
    @NamedQuery(name = "Archivos.findById", query = "SELECT a FROM Archivos a WHERE a.id = :id"),
    @NamedQuery(name = "Archivos.findByNombre", query = "SELECT a FROM Archivos a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Archivos.findByExtension", query = "SELECT a FROM Archivos a WHERE a.extension = :extension"),
    @NamedQuery(name = "Archivos.findByUrl", query = "SELECT a FROM Archivos a WHERE a.url = :url"),
    @NamedQuery(name = "Archivos.findByTama\u00f1o", query = "SELECT a FROM Archivos a WHERE a.tama\u00f1o = :tama\u00f1o")})
public class Archivos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Extension")
    private String extension;
    @Column(name = "Url")
    private String url;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Tama\u00f1o")
    private Double tamaño;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "archivosid")
    private Collection<DetalleEntrega> detalleEntregaCollection;

    public Archivos() {
    }

    public Archivos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getTamaño() {
        return tamaño;
    }

    public void setTamaño(Double tamaño) {
        this.tamaño = tamaño;
    }

    @XmlTransient
    public Collection<DetalleEntrega> getDetalleEntregaCollection() {
        return detalleEntregaCollection;
    }

    public void setDetalleEntregaCollection(Collection<DetalleEntrega> detalleEntregaCollection) {
        this.detalleEntregaCollection = detalleEntregaCollection;
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
        if (!(object instanceof Archivos)) {
            return false;
        }
        Archivos other = (Archivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.percistence.Archivos[ id=" + id + " ]";
    }
    
}

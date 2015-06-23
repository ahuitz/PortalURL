/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Geek
 */
@Entity
@Table(name = "Historial_Contrasenia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistorialContrasenia.findAll", query = "SELECT h FROM HistorialContrasenia h"),
    @NamedQuery(name = "HistorialContrasenia.findById", query = "SELECT h FROM HistorialContrasenia h WHERE h.id = :id"),
    @NamedQuery(name = "HistorialContrasenia.findByFechaCreacion", query = "SELECT h FROM HistorialContrasenia h WHERE h.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "HistorialContrasenia.findByContrasenia", query = "SELECT h FROM HistorialContrasenia h WHERE h.contrasenia = :contrasenia")})
public class HistorialContrasenia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Fecha_Creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "Contrasenia")
    private String contrasenia;
    @JoinColumn(name = "Usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioid;

    public HistorialContrasenia() {
    }

    public HistorialContrasenia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Usuario getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(Usuario usuarioid) {
        this.usuarioid = usuarioid;
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
        if (!(object instanceof HistorialContrasenia)) {
            return false;
        }
        HistorialContrasenia other = (HistorialContrasenia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tablas.HistorialContrasenia[ id=" + id + " ]";
    }
    
}

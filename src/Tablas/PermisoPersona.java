/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Geek
 */
@Entity
@Table(name = "Permiso_Persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermisoPersona.findAll", query = "SELECT p FROM PermisoPersona p"),
    @NamedQuery(name = "PermisoPersona.findById", query = "SELECT p FROM PermisoPersona p WHERE p.id = :id")})
public class PermisoPersona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "Tipo_Persona_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoPersona tipoPersonaid;
    @JoinColumn(name = "Permiso_Persona_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Permiso permisoPersonaid;

    public PermisoPersona() {
    }

    public PermisoPersona(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoPersona getTipoPersonaid() {
        return tipoPersonaid;
    }

    public void setTipoPersonaid(TipoPersona tipoPersonaid) {
        this.tipoPersonaid = tipoPersonaid;
    }

    public Permiso getPermisoPersonaid() {
        return permisoPersonaid;
    }

    public void setPermisoPersonaid(Permiso permisoPersonaid) {
        this.permisoPersonaid = permisoPersonaid;
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
        if (!(object instanceof PermisoPersona)) {
            return false;
        }
        PermisoPersona other = (PermisoPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tablas.PermisoPersona[ id=" + id + " ]";
    }
    
}

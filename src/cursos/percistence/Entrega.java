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
 * @author Diaz
 */
@Entity
@Table(name = "entrega")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entrega.findAll", query = "SELECT e FROM Entrega e"),
    @NamedQuery(name = "Entrega.findById", query = "SELECT e FROM Entrega e WHERE e.id = :id")})
public class Entrega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "Tarea_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tarea tareaid;
    @JoinColumn(name = "Usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entregaid")
    private Collection<DetalleEntrega> detalleEntregaCollection;

    public Entrega() {
    }

    public Entrega(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tarea getTareaid() {
        return tareaid;
    }

    public void setTareaid(Tarea tareaid) {
        this.tareaid = tareaid;
    }

    public Usuario getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(Usuario usuarioid) {
        this.usuarioid = usuarioid;
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
        if (!(object instanceof Entrega)) {
            return false;
        }
        Entrega other = (Entrega) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.percistence.Entrega[ id=" + id + " ]";
    }
    
}

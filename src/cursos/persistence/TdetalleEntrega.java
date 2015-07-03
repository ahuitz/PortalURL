/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.persistence;

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
 * @author Diaz
 */
@Entity
@Table(name = "tdetalle_entrega")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TdetalleEntrega.findAll", query = "SELECT t FROM TdetalleEntrega t"),
    @NamedQuery(name = "TdetalleEntrega.findByIdt", query = "SELECT t FROM TdetalleEntrega t WHERE t.idt = :idt"),
    @NamedQuery(name = "TdetalleEntrega.findByFechaEntrega", query = "SELECT t FROM TdetalleEntrega t WHERE t.fechaEntrega = :fechaEntrega")})
public class TdetalleEntrega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idt")
    private Integer idt;
    @Basic(optional = false)
    @Column(name = "Fecha_Entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @JoinColumn(name = "Archivos_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tarchivos archivosid;
    @JoinColumn(name = "Entrega_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tentrega entregaid;

    public TdetalleEntrega() {
    }

    public TdetalleEntrega(Integer idt) {
        this.idt = idt;
    }

    public TdetalleEntrega(Integer idt, Date fechaEntrega) {
        this.idt = idt;
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getIdt() {
        return idt;
    }

    public void setIdt(Integer idt) {
        this.idt = idt;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Tarchivos getArchivosid() {
        return archivosid;
    }

    public void setArchivosid(Tarchivos archivosid) {
        this.archivosid = archivosid;
    }

    public Tentrega getEntregaid() {
        return entregaid;
    }

    public void setEntregaid(Tentrega entregaid) {
        this.entregaid = entregaid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idt != null ? idt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TdetalleEntrega)) {
            return false;
        }
        TdetalleEntrega other = (TdetalleEntrega) object;
        if ((this.idt == null && other.idt != null) || (this.idt != null && !this.idt.equals(other.idt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.TdetalleEntrega[ idt=" + idt + " ]";
    }
    
}

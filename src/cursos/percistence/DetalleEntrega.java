/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.percistence;

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
@Table(name = "detalle_entrega")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleEntrega.findAll", query = "SELECT d FROM DetalleEntrega d"),
    @NamedQuery(name = "DetalleEntrega.findByIdt", query = "SELECT d FROM DetalleEntrega d WHERE d.idt = :idt"),
    @NamedQuery(name = "DetalleEntrega.findByFechaEntrega", query = "SELECT d FROM DetalleEntrega d WHERE d.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "DetalleEntrega.findByCalificacion", query = "SELECT d FROM DetalleEntrega d WHERE d.calificacion = :calificacion")})
public class DetalleEntrega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idt")
    private Integer idt;
    @Column(name = "Fecha_Entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Calificacion")
    private Double calificacion;
    @JoinColumn(name = "Archivos_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Archivos archivosid;
    @JoinColumn(name = "Entrega_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Entrega entregaid;

    public DetalleEntrega() {
    }

    public DetalleEntrega(Integer idt) {
        this.idt = idt;
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

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public Archivos getArchivosid() {
        return archivosid;
    }

    public void setArchivosid(Archivos archivosid) {
        this.archivosid = archivosid;
    }

    public Entrega getEntregaid() {
        return entregaid;
    }

    public void setEntregaid(Entrega entregaid) {
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
        if (!(object instanceof DetalleEntrega)) {
            return false;
        }
        DetalleEntrega other = (DetalleEntrega) object;
        if ((this.idt == null && other.idt != null) || (this.idt != null && !this.idt.equals(other.idt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.percistence.DetalleEntrega[ idt=" + idt + " ]";
    }
    
}

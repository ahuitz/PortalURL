/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.percistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Diaz
 */
@Entity
@Table(name = "tarea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarea.findAll", query = "SELECT t FROM Tarea t"),
    @NamedQuery(name = "Tarea.findById", query = "SELECT t FROM Tarea t WHERE t.id = :id"),
    @NamedQuery(name = "Tarea.findByTitulo", query = "SELECT t FROM Tarea t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Tarea.findByDescripcion", query = "SELECT t FROM Tarea t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tarea.findByFechaEntrega", query = "SELECT t FROM Tarea t WHERE t.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "Tarea.findByFechaPublicacion", query = "SELECT t FROM Tarea t WHERE t.fechaPublicacion = :fechaPublicacion"),
    @NamedQuery(name = "Tarea.findByValor", query = "SELECT t FROM Tarea t WHERE t.valor = :valor"),
    @NamedQuery(name = "Tarea.findByTiempoextra", query = "SELECT t FROM Tarea t WHERE t.tiempoextra = :tiempoextra")})
public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Titulo")
    private String titulo;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Fecha_Entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "Fecha_Publicacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPublicacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Valor")
    private Double valor;
    @Column(name = "Tiempo_extra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tiempoextra;
    @JoinColumn(name = "Seccion_Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SeccionCursos seccionCursoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tareaid")
    private Collection<Entrega> entregaCollection;

    public Tarea() {
    }

    public Tarea(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getTiempoextra() {
        return tiempoextra;
    }

    public void setTiempoextra(Date tiempoextra) {
        this.tiempoextra = tiempoextra;
    }

    public SeccionCursos getSeccionCursoid() {
        return seccionCursoid;
    }

    public void setSeccionCursoid(SeccionCursos seccionCursoid) {
        this.seccionCursoid = seccionCursoid;
    }

    @XmlTransient
    public Collection<Entrega> getEntregaCollection() {
        return entregaCollection;
    }

    public void setEntregaCollection(Collection<Entrega> entregaCollection) {
        this.entregaCollection = entregaCollection;
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
        if (!(object instanceof Tarea)) {
            return false;
        }
        Tarea other = (Tarea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.percistence.Tarea[ id=" + id + " ]";
    }
    
}

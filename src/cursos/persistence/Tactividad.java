/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Table(name = "tactividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tactividad.findAll", query = "SELECT t FROM Tactividad t"),
    @NamedQuery(name = "Tactividad.findById", query = "SELECT t FROM Tactividad t WHERE t.id = :id"),
    @NamedQuery(name = "Tactividad.findByTitulo", query = "SELECT t FROM Tactividad t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Tactividad.findByDescripcion", query = "SELECT t FROM Tactividad t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tactividad.findByFechaEntrega", query = "SELECT t FROM Tactividad t WHERE t.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "Tactividad.findByFechaPublicacion", query = "SELECT t FROM Tactividad t WHERE t.fechaPublicacion = :fechaPublicacion"),
    @NamedQuery(name = "Tactividad.findByValor", query = "SELECT t FROM Tactividad t WHERE t.valor = :valor"),
    @NamedQuery(name = "Tactividad.findByTiempoextra", query = "SELECT t FROM Tactividad t WHERE t.tiempoextra = :tiempoextra")})
public class Tactividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "Fecha_Entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Basic(optional = false)
    @Column(name = "Fecha_Publicacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPublicacion;
    @Basic(optional = false)
    @Column(name = "Valor")
    private double valor;
    @Basic(optional = false)
    @Column(name = "Tiempo_extra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tiempoextra;
    @JoinColumn(name = "Seccion_Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TseccionCursos seccionCursoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividadid")
    private List<Tentrega> tentregaList;

    public Tactividad() {
    }

    public Tactividad(Integer id) {
        this.id = id;
    }

    public Tactividad(Integer id, String titulo, String descripcion, Date fechaEntrega, Date fechaPublicacion, double valor, Date tiempoextra) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.fechaPublicacion = fechaPublicacion;
        this.valor = valor;
        this.tiempoextra = tiempoextra;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getTiempoextra() {
        return tiempoextra;
    }

    public void setTiempoextra(Date tiempoextra) {
        this.tiempoextra = tiempoextra;
    }

    public TseccionCursos getSeccionCursoid() {
        return seccionCursoid;
    }

    public void setSeccionCursoid(TseccionCursos seccionCursoid) {
        this.seccionCursoid = seccionCursoid;
    }

    @XmlTransient
    public List<Tentrega> getTentregaList() {
        return tentregaList;
    }

    public void setTentregaList(List<Tentrega> tentregaList) {
        this.tentregaList = tentregaList;
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
        if (!(object instanceof Tactividad)) {
            return false;
        }
        Tactividad other = (Tactividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.Tactividad[ id=" + id + " ]";
    }
    
}

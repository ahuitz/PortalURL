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
@Table(name = "seccion_cursos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeccionCursos.findAll", query = "SELECT s FROM SeccionCursos s"),
    @NamedQuery(name = "SeccionCursos.findById", query = "SELECT s FROM SeccionCursos s WHERE s.id = :id"),
    @NamedQuery(name = "SeccionCursos.findByCupo", query = "SELECT s FROM SeccionCursos s WHERE s.cupo = :cupo")})
public class SeccionCursos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Cupo")
    private Integer cupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionCursoid")
    private Collection<Recurso> recursoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionCursoid")
    private Collection<Tarea> tareaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionCursoid")
    private Collection<Asignados> asignadosCollection;
    @JoinColumn(name = "Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Curso cursoid;
    @JoinColumn(name = "Seccion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seccion seccionid;
    @JoinColumn(name = "Catedratico_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario catedraticoid;

    public SeccionCursos() {
    }

    public SeccionCursos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    @XmlTransient
    public Collection<Recurso> getRecursoCollection() {
        return recursoCollection;
    }

    public void setRecursoCollection(Collection<Recurso> recursoCollection) {
        this.recursoCollection = recursoCollection;
    }

    @XmlTransient
    public Collection<Tarea> getTareaCollection() {
        return tareaCollection;
    }

    public void setTareaCollection(Collection<Tarea> tareaCollection) {
        this.tareaCollection = tareaCollection;
    }

    @XmlTransient
    public Collection<Asignados> getAsignadosCollection() {
        return asignadosCollection;
    }

    public void setAsignadosCollection(Collection<Asignados> asignadosCollection) {
        this.asignadosCollection = asignadosCollection;
    }

    public Curso getCursoid() {
        return cursoid;
    }

    public void setCursoid(Curso cursoid) {
        this.cursoid = cursoid;
    }

    public Seccion getSeccionid() {
        return seccionid;
    }

    public void setSeccionid(Seccion seccionid) {
        this.seccionid = seccionid;
    }

    public Usuario getCatedraticoid() {
        return catedraticoid;
    }

    public void setCatedraticoid(Usuario catedraticoid) {
        this.catedraticoid = catedraticoid;
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
        if (!(object instanceof SeccionCursos)) {
            return false;
        }
        SeccionCursos other = (SeccionCursos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.percistence.SeccionCursos[ id=" + id + " ]";
    }
    
}

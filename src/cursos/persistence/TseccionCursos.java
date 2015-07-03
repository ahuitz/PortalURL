/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.persistence;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Diaz
 */
@Entity
@Table(name = "tseccion_cursos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TseccionCursos.findAll", query = "SELECT t FROM TseccionCursos t"),
    @NamedQuery(name = "TseccionCursos.findById", query = "SELECT t FROM TseccionCursos t WHERE t.id = :id"),
    @NamedQuery(name = "TseccionCursos.findByCupo", query = "SELECT t FROM TseccionCursos t WHERE t.cupo = :cupo"),
    @NamedQuery(name = "TseccionCursos.findByAula", query = "SELECT t FROM TseccionCursos t WHERE t.aula = :aula")})
public class TseccionCursos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Cupo")
    private int cupo;
    @Column(name = "aula")
    private String aula;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionCursoid")
    private List<Tactividad> tactividadList;
    @JoinColumn(name = "Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tcurso cursoid;
    @JoinColumn(name = "Seccion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tseccion seccionid;
    @JoinColumn(name = "Catedratico_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tusuario catedraticoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionCursoid")
    private List<Trecurso> trecursoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionCursoid")
    private List<Tasignados> tasignadosList;

    public TseccionCursos() {
    }

    public TseccionCursos(Integer id) {
        this.id = id;
    }

    public TseccionCursos(Integer id, int cupo) {
        this.id = id;
        this.cupo = cupo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    @XmlTransient
    public List<Tactividad> getTactividadList() {
        return tactividadList;
    }

    public void setTactividadList(List<Tactividad> tactividadList) {
        this.tactividadList = tactividadList;
    }

    public Tcurso getCursoid() {
        return cursoid;
    }

    public void setCursoid(Tcurso cursoid) {
        this.cursoid = cursoid;
    }

    public Tseccion getSeccionid() {
        return seccionid;
    }

    public void setSeccionid(Tseccion seccionid) {
        this.seccionid = seccionid;
    }

    public Tusuario getCatedraticoid() {
        return catedraticoid;
    }

    public void setCatedraticoid(Tusuario catedraticoid) {
        this.catedraticoid = catedraticoid;
    }

    @XmlTransient
    public List<Trecurso> getTrecursoList() {
        return trecursoList;
    }

    public void setTrecursoList(List<Trecurso> trecursoList) {
        this.trecursoList = trecursoList;
    }

    @XmlTransient
    public List<Tasignados> getTasignadosList() {
        return tasignadosList;
    }

    public void setTasignadosList(List<Tasignados> tasignadosList) {
        this.tasignadosList = tasignadosList;
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
        if (!(object instanceof TseccionCursos)) {
            return false;
        }
        TseccionCursos other = (TseccionCursos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.TseccionCursos[ id=" + id + " ]";
    }
    
}

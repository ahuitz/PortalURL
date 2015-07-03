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
@Table(name = "tusuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tusuario.findAll", query = "SELECT t FROM Tusuario t"),
    @NamedQuery(name = "Tusuario.findById", query = "SELECT t FROM Tusuario t WHERE t.id = :id")})
public class Tusuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catedraticoid")
    private List<TseccionCursos> tseccionCursosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioid")
    private List<Tasignados> tasignadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioid")
    private List<Tentrega> tentregaList;

    public Tusuario() {
    }

    public Tusuario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public List<TseccionCursos> getTseccionCursosList() {
        return tseccionCursosList;
    }

    public void setTseccionCursosList(List<TseccionCursos> tseccionCursosList) {
        this.tseccionCursosList = tseccionCursosList;
    }

    @XmlTransient
    public List<Tasignados> getTasignadosList() {
        return tasignadosList;
    }

    public void setTasignadosList(List<Tasignados> tasignadosList) {
        this.tasignadosList = tasignadosList;
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
        if (!(object instanceof Tusuario)) {
            return false;
        }
        Tusuario other = (Tusuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.Tusuario[ id=" + id + " ]";
    }
    
}

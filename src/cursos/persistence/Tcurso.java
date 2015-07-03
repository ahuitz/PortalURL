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
@Table(name = "tcurso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tcurso.findAll", query = "SELECT t FROM Tcurso t"),
    @NamedQuery(name = "Tcurso.findById", query = "SELECT t FROM Tcurso t WHERE t.id = :id"),
    @NamedQuery(name = "Tcurso.findByNombre", query = "SELECT t FROM Tcurso t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Tcurso.findByFacultad", query = "SELECT t FROM Tcurso t WHERE t.facultad = :facultad")})
public class Tcurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Facultad")
    private String facultad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoid")
    private List<TseccionCursos> tseccionCursosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoid")
    private List<TcursoCiclo> tcursoCicloList;

    public Tcurso() {
    }

    public Tcurso(Integer id) {
        this.id = id;
    }

    public Tcurso(Integer id, String nombre, String facultad) {
        this.id = id;
        this.nombre = nombre;
        this.facultad = facultad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    @XmlTransient
    public List<TseccionCursos> getTseccionCursosList() {
        return tseccionCursosList;
    }

    public void setTseccionCursosList(List<TseccionCursos> tseccionCursosList) {
        this.tseccionCursosList = tseccionCursosList;
    }

    @XmlTransient
    public List<TcursoCiclo> getTcursoCicloList() {
        return tcursoCicloList;
    }

    public void setTcursoCicloList(List<TcursoCiclo> tcursoCicloList) {
        this.tcursoCicloList = tcursoCicloList;
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
        if (!(object instanceof Tcurso)) {
            return false;
        }
        Tcurso other = (Tcurso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.Tcurso[ id=" + id + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.persistence;

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
 * @author Diaz
 */
@Entity
@Table(name = "tasignados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tasignados.findAll", query = "SELECT t FROM Tasignados t"),
    @NamedQuery(name = "Tasignados.findById", query = "SELECT t FROM Tasignados t WHERE t.id = :id")})
public class Tasignados implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "Seccion_Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TseccionCursos seccionCursoid;
    @JoinColumn(name = "Usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tusuario usuarioid;

    public Tasignados() {
    }

    public Tasignados(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TseccionCursos getSeccionCursoid() {
        return seccionCursoid;
    }

    public void setSeccionCursoid(TseccionCursos seccionCursoid) {
        this.seccionCursoid = seccionCursoid;
    }

    public Tusuario getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(Tusuario usuarioid) {
        this.usuarioid = usuarioid;
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
        if (!(object instanceof Tasignados)) {
            return false;
        }
        Tasignados other = (Tasignados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.Tasignados[ id=" + id + " ]";
    }
    
}

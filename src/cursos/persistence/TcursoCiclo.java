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
@Table(name = "tcurso_ciclo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcursoCiclo.findAll", query = "SELECT t FROM TcursoCiclo t"),
    @NamedQuery(name = "TcursoCiclo.findById", query = "SELECT t FROM TcursoCiclo t WHERE t.id = :id"),
    @NamedQuery(name = "TcursoCiclo.findByFecha", query = "SELECT t FROM TcursoCiclo t WHERE t.fecha = :fecha")})
public class TcursoCiclo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Fecha")
    private int fecha;
    @JoinColumn(name = "Ciclo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tciclo cicloid;
    @JoinColumn(name = "Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tcurso cursoid;

    public TcursoCiclo() {
    }

    public TcursoCiclo(Integer id) {
        this.id = id;
    }

    public TcursoCiclo(Integer id, int fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public Tciclo getCicloid() {
        return cicloid;
    }

    public void setCicloid(Tciclo cicloid) {
        this.cicloid = cicloid;
    }

    public Tcurso getCursoid() {
        return cursoid;
    }

    public void setCursoid(Tcurso cursoid) {
        this.cursoid = cursoid;
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
        if (!(object instanceof TcursoCiclo)) {
            return false;
        }
        TcursoCiclo other = (TcursoCiclo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.TcursoCiclo[ id=" + id + " ]";
    }
    
}

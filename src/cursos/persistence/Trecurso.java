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
@Table(name = "trecurso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trecurso.findAll", query = "SELECT t FROM Trecurso t"),
    @NamedQuery(name = "Trecurso.findById", query = "SELECT t FROM Trecurso t WHERE t.id = :id"),
    @NamedQuery(name = "Trecurso.findByTitulo", query = "SELECT t FROM Trecurso t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Trecurso.findByDescripcion", query = "SELECT t FROM Trecurso t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Trecurso.findByFechaPublicacion", query = "SELECT t FROM Trecurso t WHERE t.fechaPublicacion = :fechaPublicacion")})
public class Trecurso implements Serializable {
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
    @Column(name = "Fecha_Publicacion")
    private String fechaPublicacion;
    @JoinColumn(name = "Archivos_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tarchivos archivosid;
    @JoinColumn(name = "Seccion_Curso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TseccionCursos seccionCursoid;

    public Trecurso() {
    }

    public Trecurso(Integer id) {
        this.id = id;
    }

    public Trecurso(Integer id, String titulo, String descripcion, String fechaPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaPublicacion = fechaPublicacion;
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

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Tarchivos getArchivosid() {
        return archivosid;
    }

    public void setArchivosid(Tarchivos archivosid) {
        this.archivosid = archivosid;
    }

    public TseccionCursos getSeccionCursoid() {
        return seccionCursoid;
    }

    public void setSeccionCursoid(TseccionCursos seccionCursoid) {
        this.seccionCursoid = seccionCursoid;
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
        if (!(object instanceof Trecurso)) {
            return false;
        }
        Trecurso other = (Trecurso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cursos.persistence.Trecurso[ id=" + id + " ]";
    }
    
}

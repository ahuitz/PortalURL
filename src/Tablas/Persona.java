/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

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
 * @author Geek
 */
@Entity
@Table(name = "Persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findById", query = "SELECT p FROM Persona p WHERE p.id = :id"),
    @NamedQuery(name = "Persona.findByNombre", query = "SELECT p FROM Persona p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Persona.findByApellido", query = "SELECT p FROM Persona p WHERE p.apellido = :apellido"),
    @NamedQuery(name = "Persona.findBySexo", query = "SELECT p FROM Persona p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "Persona.findByDpi", query = "SELECT p FROM Persona p WHERE p.dpi = :dpi"),
    @NamedQuery(name = "Persona.findByNumeroRegisto", query = "SELECT p FROM Persona p WHERE p.numeroRegisto = :numeroRegisto"),
    @NamedQuery(name = "Persona.findByCarne", query = "SELECT p FROM Persona p WHERE p.carne = :carne"),
    @NamedQuery(name = "Persona.findByFechaNacimiento", query = "SELECT p FROM Persona p WHERE p.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Persona.findByTipoSangre", query = "SELECT p FROM Persona p WHERE p.tipoSangre = :tipoSangre"),
    @NamedQuery(name = "Persona.findByReligion", query = "SELECT p FROM Persona p WHERE p.religion = :religion"),
    @NamedQuery(name = "Persona.findByTrabaja", query = "SELECT p FROM Persona p WHERE p.trabaja = :trabaja"),
    @NamedQuery(name = "Persona.findByNit", query = "SELECT p FROM Persona p WHERE p.nit = :nit")})
public class Persona implements Serializable {
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
    @Column(name = "Apellido")
    private String apellido;
    @Column(name = "Sexo")
    private Boolean sexo;
    @Column(name = "DPI")
    private String dpi;
    @Column(name = "Numero_Registo")
    private String numeroRegisto;
    @Basic(optional = false)
    @Column(name = "Carne")
    private String carne;
    @Column(name = "Fecha_Nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "Tipo_Sangre")
    private String tipoSangre;
    @Column(name = "Religion")
    private String religion;
    @Column(name = "Trabaja")
    private Boolean trabaja;
    @Column(name = "NIT")
    private String nit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaid")
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "Tipo_Persona_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoPersona tipoPersonaid;
    @JoinColumn(name = "Estado_Civil_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoCivil estadoCivilid;
    @JoinColumn(name = "Estado_Usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoUsuario estadoUsuarioid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaid")
    private Collection<Contacto> contactoCollection;

    public Persona() {
    }

    public Persona(Integer id) {
        this.id = id;
    }

    public Persona(Integer id, String nombre, String apellido, String carne) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carne = carne;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNumeroRegisto() {
        return numeroRegisto;
    }

    public void setNumeroRegisto(String numeroRegisto) {
        this.numeroRegisto = numeroRegisto;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Boolean getTrabaja() {
        return trabaja;
    }

    public void setTrabaja(Boolean trabaja) {
        this.trabaja = trabaja;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public TipoPersona getTipoPersonaid() {
        return tipoPersonaid;
    }

    public void setTipoPersonaid(TipoPersona tipoPersonaid) {
        this.tipoPersonaid = tipoPersonaid;
    }

    public EstadoCivil getEstadoCivilid() {
        return estadoCivilid;
    }

    public void setEstadoCivilid(EstadoCivil estadoCivilid) {
        this.estadoCivilid = estadoCivilid;
    }

    public EstadoUsuario getEstadoUsuarioid() {
        return estadoUsuarioid;
    }

    public void setEstadoUsuarioid(EstadoUsuario estadoUsuarioid) {
        this.estadoUsuarioid = estadoUsuarioid;
    }

    @XmlTransient
    public Collection<Contacto> getContactoCollection() {
        return contactoCollection;
    }

    public void setContactoCollection(Collection<Contacto> contactoCollection) {
        this.contactoCollection = contactoCollection;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tablas.Persona[ id=" + id + " ]";
    }
    
}

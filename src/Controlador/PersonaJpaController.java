/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.TipoPersona;
import Tablas.EstadoCivil;
import Tablas.EstadoUsuario;
import Tablas.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import Tablas.Contacto;
import Tablas.Persona;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) {
        if (persona.getUsuarioCollection() == null) {
            persona.setUsuarioCollection(new ArrayList<Usuario>());
        }
        if (persona.getContactoCollection() == null) {
            persona.setContactoCollection(new ArrayList<Contacto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPersona tipoPersonaid = persona.getTipoPersonaid();
            if (tipoPersonaid != null) {
                tipoPersonaid = em.getReference(tipoPersonaid.getClass(), tipoPersonaid.getId());
                persona.setTipoPersonaid(tipoPersonaid);
            }
            EstadoCivil estadoCivilid = persona.getEstadoCivilid();
            if (estadoCivilid != null) {
                estadoCivilid = em.getReference(estadoCivilid.getClass(), estadoCivilid.getId());
                persona.setEstadoCivilid(estadoCivilid);
            }
            EstadoUsuario estadoUsuarioid = persona.getEstadoUsuarioid();
            if (estadoUsuarioid != null) {
                estadoUsuarioid = em.getReference(estadoUsuarioid.getClass(), estadoUsuarioid.getId());
                persona.setEstadoUsuarioid(estadoUsuarioid);
            }
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : persona.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getId());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            persona.setUsuarioCollection(attachedUsuarioCollection);
            Collection<Contacto> attachedContactoCollection = new ArrayList<Contacto>();
            for (Contacto contactoCollectionContactoToAttach : persona.getContactoCollection()) {
                contactoCollectionContactoToAttach = em.getReference(contactoCollectionContactoToAttach.getClass(), contactoCollectionContactoToAttach.getId());
                attachedContactoCollection.add(contactoCollectionContactoToAttach);
            }
            persona.setContactoCollection(attachedContactoCollection);
            em.persist(persona);
            if (tipoPersonaid != null) {
                tipoPersonaid.getPersonaCollection().add(persona);
                tipoPersonaid = em.merge(tipoPersonaid);
            }
            if (estadoCivilid != null) {
                estadoCivilid.getPersonaCollection().add(persona);
                estadoCivilid = em.merge(estadoCivilid);
            }
            if (estadoUsuarioid != null) {
                estadoUsuarioid.getPersonaCollection().add(persona);
                estadoUsuarioid = em.merge(estadoUsuarioid);
            }
            for (Usuario usuarioCollectionUsuario : persona.getUsuarioCollection()) {
                Persona oldPersonaidOfUsuarioCollectionUsuario = usuarioCollectionUsuario.getPersonaid();
                usuarioCollectionUsuario.setPersonaid(persona);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
                if (oldPersonaidOfUsuarioCollectionUsuario != null) {
                    oldPersonaidOfUsuarioCollectionUsuario.getUsuarioCollection().remove(usuarioCollectionUsuario);
                    oldPersonaidOfUsuarioCollectionUsuario = em.merge(oldPersonaidOfUsuarioCollectionUsuario);
                }
            }
            for (Contacto contactoCollectionContacto : persona.getContactoCollection()) {
                Persona oldPersonaidOfContactoCollectionContacto = contactoCollectionContacto.getPersonaid();
                contactoCollectionContacto.setPersonaid(persona);
                contactoCollectionContacto = em.merge(contactoCollectionContacto);
                if (oldPersonaidOfContactoCollectionContacto != null) {
                    oldPersonaidOfContactoCollectionContacto.getContactoCollection().remove(contactoCollectionContacto);
                    oldPersonaidOfContactoCollectionContacto = em.merge(oldPersonaidOfContactoCollectionContacto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getId());
            TipoPersona tipoPersonaidOld = persistentPersona.getTipoPersonaid();
            TipoPersona tipoPersonaidNew = persona.getTipoPersonaid();
            EstadoCivil estadoCivilidOld = persistentPersona.getEstadoCivilid();
            EstadoCivil estadoCivilidNew = persona.getEstadoCivilid();
            EstadoUsuario estadoUsuarioidOld = persistentPersona.getEstadoUsuarioid();
            EstadoUsuario estadoUsuarioidNew = persona.getEstadoUsuarioid();
            Collection<Usuario> usuarioCollectionOld = persistentPersona.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = persona.getUsuarioCollection();
            Collection<Contacto> contactoCollectionOld = persistentPersona.getContactoCollection();
            Collection<Contacto> contactoCollectionNew = persona.getContactoCollection();
            List<String> illegalOrphanMessages = null;
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioCollectionOldUsuario + " since its personaid field is not nullable.");
                }
            }
            for (Contacto contactoCollectionOldContacto : contactoCollectionOld) {
                if (!contactoCollectionNew.contains(contactoCollectionOldContacto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contacto " + contactoCollectionOldContacto + " since its personaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoPersonaidNew != null) {
                tipoPersonaidNew = em.getReference(tipoPersonaidNew.getClass(), tipoPersonaidNew.getId());
                persona.setTipoPersonaid(tipoPersonaidNew);
            }
            if (estadoCivilidNew != null) {
                estadoCivilidNew = em.getReference(estadoCivilidNew.getClass(), estadoCivilidNew.getId());
                persona.setEstadoCivilid(estadoCivilidNew);
            }
            if (estadoUsuarioidNew != null) {
                estadoUsuarioidNew = em.getReference(estadoUsuarioidNew.getClass(), estadoUsuarioidNew.getId());
                persona.setEstadoUsuarioid(estadoUsuarioidNew);
            }
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getId());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            persona.setUsuarioCollection(usuarioCollectionNew);
            Collection<Contacto> attachedContactoCollectionNew = new ArrayList<Contacto>();
            for (Contacto contactoCollectionNewContactoToAttach : contactoCollectionNew) {
                contactoCollectionNewContactoToAttach = em.getReference(contactoCollectionNewContactoToAttach.getClass(), contactoCollectionNewContactoToAttach.getId());
                attachedContactoCollectionNew.add(contactoCollectionNewContactoToAttach);
            }
            contactoCollectionNew = attachedContactoCollectionNew;
            persona.setContactoCollection(contactoCollectionNew);
            persona = em.merge(persona);
            if (tipoPersonaidOld != null && !tipoPersonaidOld.equals(tipoPersonaidNew)) {
                tipoPersonaidOld.getPersonaCollection().remove(persona);
                tipoPersonaidOld = em.merge(tipoPersonaidOld);
            }
            if (tipoPersonaidNew != null && !tipoPersonaidNew.equals(tipoPersonaidOld)) {
                tipoPersonaidNew.getPersonaCollection().add(persona);
                tipoPersonaidNew = em.merge(tipoPersonaidNew);
            }
            if (estadoCivilidOld != null && !estadoCivilidOld.equals(estadoCivilidNew)) {
                estadoCivilidOld.getPersonaCollection().remove(persona);
                estadoCivilidOld = em.merge(estadoCivilidOld);
            }
            if (estadoCivilidNew != null && !estadoCivilidNew.equals(estadoCivilidOld)) {
                estadoCivilidNew.getPersonaCollection().add(persona);
                estadoCivilidNew = em.merge(estadoCivilidNew);
            }
            if (estadoUsuarioidOld != null && !estadoUsuarioidOld.equals(estadoUsuarioidNew)) {
                estadoUsuarioidOld.getPersonaCollection().remove(persona);
                estadoUsuarioidOld = em.merge(estadoUsuarioidOld);
            }
            if (estadoUsuarioidNew != null && !estadoUsuarioidNew.equals(estadoUsuarioidOld)) {
                estadoUsuarioidNew.getPersonaCollection().add(persona);
                estadoUsuarioidNew = em.merge(estadoUsuarioidNew);
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    Persona oldPersonaidOfUsuarioCollectionNewUsuario = usuarioCollectionNewUsuario.getPersonaid();
                    usuarioCollectionNewUsuario.setPersonaid(persona);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                    if (oldPersonaidOfUsuarioCollectionNewUsuario != null && !oldPersonaidOfUsuarioCollectionNewUsuario.equals(persona)) {
                        oldPersonaidOfUsuarioCollectionNewUsuario.getUsuarioCollection().remove(usuarioCollectionNewUsuario);
                        oldPersonaidOfUsuarioCollectionNewUsuario = em.merge(oldPersonaidOfUsuarioCollectionNewUsuario);
                    }
                }
            }
            for (Contacto contactoCollectionNewContacto : contactoCollectionNew) {
                if (!contactoCollectionOld.contains(contactoCollectionNewContacto)) {
                    Persona oldPersonaidOfContactoCollectionNewContacto = contactoCollectionNewContacto.getPersonaid();
                    contactoCollectionNewContacto.setPersonaid(persona);
                    contactoCollectionNewContacto = em.merge(contactoCollectionNewContacto);
                    if (oldPersonaidOfContactoCollectionNewContacto != null && !oldPersonaidOfContactoCollectionNewContacto.equals(persona)) {
                        oldPersonaidOfContactoCollectionNewContacto.getContactoCollection().remove(contactoCollectionNewContacto);
                        oldPersonaidOfContactoCollectionNewContacto = em.merge(oldPersonaidOfContactoCollectionNewContacto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getId();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Usuario> usuarioCollectionOrphanCheck = persona.getUsuarioCollection();
            for (Usuario usuarioCollectionOrphanCheckUsuario : usuarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Usuario " + usuarioCollectionOrphanCheckUsuario + " in its usuarioCollection field has a non-nullable personaid field.");
            }
            Collection<Contacto> contactoCollectionOrphanCheck = persona.getContactoCollection();
            for (Contacto contactoCollectionOrphanCheckContacto : contactoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Contacto " + contactoCollectionOrphanCheckContacto + " in its contactoCollection field has a non-nullable personaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoPersona tipoPersonaid = persona.getTipoPersonaid();
            if (tipoPersonaid != null) {
                tipoPersonaid.getPersonaCollection().remove(persona);
                tipoPersonaid = em.merge(tipoPersonaid);
            }
            EstadoCivil estadoCivilid = persona.getEstadoCivilid();
            if (estadoCivilid != null) {
                estadoCivilid.getPersonaCollection().remove(persona);
                estadoCivilid = em.merge(estadoCivilid);
            }
            EstadoUsuario estadoUsuarioid = persona.getEstadoUsuarioid();
            if (estadoUsuarioid != null) {
                estadoUsuarioid.getPersonaCollection().remove(persona);
                estadoUsuarioid = em.merge(estadoUsuarioid);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

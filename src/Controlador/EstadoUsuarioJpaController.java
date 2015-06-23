/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Tablas.EstadoUsuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.Persona;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class EstadoUsuarioJpaController implements Serializable {

    public EstadoUsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoUsuario estadoUsuario) {
        if (estadoUsuario.getPersonaCollection() == null) {
            estadoUsuario.setPersonaCollection(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : estadoUsuario.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getId());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            estadoUsuario.setPersonaCollection(attachedPersonaCollection);
            em.persist(estadoUsuario);
            for (Persona personaCollectionPersona : estadoUsuario.getPersonaCollection()) {
                EstadoUsuario oldEstadoUsuarioidOfPersonaCollectionPersona = personaCollectionPersona.getEstadoUsuarioid();
                personaCollectionPersona.setEstadoUsuarioid(estadoUsuario);
                personaCollectionPersona = em.merge(personaCollectionPersona);
                if (oldEstadoUsuarioidOfPersonaCollectionPersona != null) {
                    oldEstadoUsuarioidOfPersonaCollectionPersona.getPersonaCollection().remove(personaCollectionPersona);
                    oldEstadoUsuarioidOfPersonaCollectionPersona = em.merge(oldEstadoUsuarioidOfPersonaCollectionPersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoUsuario estadoUsuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoUsuario persistentEstadoUsuario = em.find(EstadoUsuario.class, estadoUsuario.getId());
            Collection<Persona> personaCollectionOld = persistentEstadoUsuario.getPersonaCollection();
            Collection<Persona> personaCollectionNew = estadoUsuario.getPersonaCollection();
            List<String> illegalOrphanMessages = null;
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Persona " + personaCollectionOldPersona + " since its estadoUsuarioid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getId());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            estadoUsuario.setPersonaCollection(personaCollectionNew);
            estadoUsuario = em.merge(estadoUsuario);
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    EstadoUsuario oldEstadoUsuarioidOfPersonaCollectionNewPersona = personaCollectionNewPersona.getEstadoUsuarioid();
                    personaCollectionNewPersona.setEstadoUsuarioid(estadoUsuario);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                    if (oldEstadoUsuarioidOfPersonaCollectionNewPersona != null && !oldEstadoUsuarioidOfPersonaCollectionNewPersona.equals(estadoUsuario)) {
                        oldEstadoUsuarioidOfPersonaCollectionNewPersona.getPersonaCollection().remove(personaCollectionNewPersona);
                        oldEstadoUsuarioidOfPersonaCollectionNewPersona = em.merge(oldEstadoUsuarioidOfPersonaCollectionNewPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoUsuario.getId();
                if (findEstadoUsuario(id) == null) {
                    throw new NonexistentEntityException("The estadoUsuario with id " + id + " no longer exists.");
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
            EstadoUsuario estadoUsuario;
            try {
                estadoUsuario = em.getReference(EstadoUsuario.class, id);
                estadoUsuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoUsuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Persona> personaCollectionOrphanCheck = estadoUsuario.getPersonaCollection();
            for (Persona personaCollectionOrphanCheckPersona : personaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoUsuario (" + estadoUsuario + ") cannot be destroyed since the Persona " + personaCollectionOrphanCheckPersona + " in its personaCollection field has a non-nullable estadoUsuarioid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoUsuario> findEstadoUsuarioEntities() {
        return findEstadoUsuarioEntities(true, -1, -1);
    }

    public List<EstadoUsuario> findEstadoUsuarioEntities(int maxResults, int firstResult) {
        return findEstadoUsuarioEntities(false, maxResults, firstResult);
    }

    private List<EstadoUsuario> findEstadoUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoUsuario.class));
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

    public EstadoUsuario findEstadoUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoUsuario> rt = cq.from(EstadoUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

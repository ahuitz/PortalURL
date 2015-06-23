/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.Persona;
import java.util.ArrayList;
import java.util.Collection;
import Tablas.PermisoPersona;
import Tablas.TipoPersona;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class TipoPersonaJpaController implements Serializable {

    public TipoPersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPersona tipoPersona) throws PreexistingEntityException, Exception {
        if (tipoPersona.getPersonaCollection() == null) {
            tipoPersona.setPersonaCollection(new ArrayList<Persona>());
        }
        if (tipoPersona.getPermisoPersonaCollection() == null) {
            tipoPersona.setPermisoPersonaCollection(new ArrayList<PermisoPersona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : tipoPersona.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getId());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            tipoPersona.setPersonaCollection(attachedPersonaCollection);
            Collection<PermisoPersona> attachedPermisoPersonaCollection = new ArrayList<PermisoPersona>();
            for (PermisoPersona permisoPersonaCollectionPermisoPersonaToAttach : tipoPersona.getPermisoPersonaCollection()) {
                permisoPersonaCollectionPermisoPersonaToAttach = em.getReference(permisoPersonaCollectionPermisoPersonaToAttach.getClass(), permisoPersonaCollectionPermisoPersonaToAttach.getId());
                attachedPermisoPersonaCollection.add(permisoPersonaCollectionPermisoPersonaToAttach);
            }
            tipoPersona.setPermisoPersonaCollection(attachedPermisoPersonaCollection);
            em.persist(tipoPersona);
            for (Persona personaCollectionPersona : tipoPersona.getPersonaCollection()) {
                TipoPersona oldTipoPersonaidOfPersonaCollectionPersona = personaCollectionPersona.getTipoPersonaid();
                personaCollectionPersona.setTipoPersonaid(tipoPersona);
                personaCollectionPersona = em.merge(personaCollectionPersona);
                if (oldTipoPersonaidOfPersonaCollectionPersona != null) {
                    oldTipoPersonaidOfPersonaCollectionPersona.getPersonaCollection().remove(personaCollectionPersona);
                    oldTipoPersonaidOfPersonaCollectionPersona = em.merge(oldTipoPersonaidOfPersonaCollectionPersona);
                }
            }
            for (PermisoPersona permisoPersonaCollectionPermisoPersona : tipoPersona.getPermisoPersonaCollection()) {
                TipoPersona oldTipoPersonaidOfPermisoPersonaCollectionPermisoPersona = permisoPersonaCollectionPermisoPersona.getTipoPersonaid();
                permisoPersonaCollectionPermisoPersona.setTipoPersonaid(tipoPersona);
                permisoPersonaCollectionPermisoPersona = em.merge(permisoPersonaCollectionPermisoPersona);
                if (oldTipoPersonaidOfPermisoPersonaCollectionPermisoPersona != null) {
                    oldTipoPersonaidOfPermisoPersonaCollectionPermisoPersona.getPermisoPersonaCollection().remove(permisoPersonaCollectionPermisoPersona);
                    oldTipoPersonaidOfPermisoPersonaCollectionPermisoPersona = em.merge(oldTipoPersonaidOfPermisoPersonaCollectionPermisoPersona);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoPersona(tipoPersona.getId()) != null) {
                throw new PreexistingEntityException("TipoPersona " + tipoPersona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPersona tipoPersona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPersona persistentTipoPersona = em.find(TipoPersona.class, tipoPersona.getId());
            Collection<Persona> personaCollectionOld = persistentTipoPersona.getPersonaCollection();
            Collection<Persona> personaCollectionNew = tipoPersona.getPersonaCollection();
            Collection<PermisoPersona> permisoPersonaCollectionOld = persistentTipoPersona.getPermisoPersonaCollection();
            Collection<PermisoPersona> permisoPersonaCollectionNew = tipoPersona.getPermisoPersonaCollection();
            List<String> illegalOrphanMessages = null;
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Persona " + personaCollectionOldPersona + " since its tipoPersonaid field is not nullable.");
                }
            }
            for (PermisoPersona permisoPersonaCollectionOldPermisoPersona : permisoPersonaCollectionOld) {
                if (!permisoPersonaCollectionNew.contains(permisoPersonaCollectionOldPermisoPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisoPersona " + permisoPersonaCollectionOldPermisoPersona + " since its tipoPersonaid field is not nullable.");
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
            tipoPersona.setPersonaCollection(personaCollectionNew);
            Collection<PermisoPersona> attachedPermisoPersonaCollectionNew = new ArrayList<PermisoPersona>();
            for (PermisoPersona permisoPersonaCollectionNewPermisoPersonaToAttach : permisoPersonaCollectionNew) {
                permisoPersonaCollectionNewPermisoPersonaToAttach = em.getReference(permisoPersonaCollectionNewPermisoPersonaToAttach.getClass(), permisoPersonaCollectionNewPermisoPersonaToAttach.getId());
                attachedPermisoPersonaCollectionNew.add(permisoPersonaCollectionNewPermisoPersonaToAttach);
            }
            permisoPersonaCollectionNew = attachedPermisoPersonaCollectionNew;
            tipoPersona.setPermisoPersonaCollection(permisoPersonaCollectionNew);
            tipoPersona = em.merge(tipoPersona);
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    TipoPersona oldTipoPersonaidOfPersonaCollectionNewPersona = personaCollectionNewPersona.getTipoPersonaid();
                    personaCollectionNewPersona.setTipoPersonaid(tipoPersona);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                    if (oldTipoPersonaidOfPersonaCollectionNewPersona != null && !oldTipoPersonaidOfPersonaCollectionNewPersona.equals(tipoPersona)) {
                        oldTipoPersonaidOfPersonaCollectionNewPersona.getPersonaCollection().remove(personaCollectionNewPersona);
                        oldTipoPersonaidOfPersonaCollectionNewPersona = em.merge(oldTipoPersonaidOfPersonaCollectionNewPersona);
                    }
                }
            }
            for (PermisoPersona permisoPersonaCollectionNewPermisoPersona : permisoPersonaCollectionNew) {
                if (!permisoPersonaCollectionOld.contains(permisoPersonaCollectionNewPermisoPersona)) {
                    TipoPersona oldTipoPersonaidOfPermisoPersonaCollectionNewPermisoPersona = permisoPersonaCollectionNewPermisoPersona.getTipoPersonaid();
                    permisoPersonaCollectionNewPermisoPersona.setTipoPersonaid(tipoPersona);
                    permisoPersonaCollectionNewPermisoPersona = em.merge(permisoPersonaCollectionNewPermisoPersona);
                    if (oldTipoPersonaidOfPermisoPersonaCollectionNewPermisoPersona != null && !oldTipoPersonaidOfPermisoPersonaCollectionNewPermisoPersona.equals(tipoPersona)) {
                        oldTipoPersonaidOfPermisoPersonaCollectionNewPermisoPersona.getPermisoPersonaCollection().remove(permisoPersonaCollectionNewPermisoPersona);
                        oldTipoPersonaidOfPermisoPersonaCollectionNewPermisoPersona = em.merge(oldTipoPersonaidOfPermisoPersonaCollectionNewPermisoPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPersona.getId();
                if (findTipoPersona(id) == null) {
                    throw new NonexistentEntityException("The tipoPersona with id " + id + " no longer exists.");
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
            TipoPersona tipoPersona;
            try {
                tipoPersona = em.getReference(TipoPersona.class, id);
                tipoPersona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPersona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Persona> personaCollectionOrphanCheck = tipoPersona.getPersonaCollection();
            for (Persona personaCollectionOrphanCheckPersona : personaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoPersona (" + tipoPersona + ") cannot be destroyed since the Persona " + personaCollectionOrphanCheckPersona + " in its personaCollection field has a non-nullable tipoPersonaid field.");
            }
            Collection<PermisoPersona> permisoPersonaCollectionOrphanCheck = tipoPersona.getPermisoPersonaCollection();
            for (PermisoPersona permisoPersonaCollectionOrphanCheckPermisoPersona : permisoPersonaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoPersona (" + tipoPersona + ") cannot be destroyed since the PermisoPersona " + permisoPersonaCollectionOrphanCheckPermisoPersona + " in its permisoPersonaCollection field has a non-nullable tipoPersonaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoPersona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPersona> findTipoPersonaEntities() {
        return findTipoPersonaEntities(true, -1, -1);
    }

    public List<TipoPersona> findTipoPersonaEntities(int maxResults, int firstResult) {
        return findTipoPersonaEntities(false, maxResults, firstResult);
    }

    private List<TipoPersona> findTipoPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPersona.class));
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

    public TipoPersona findTipoPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPersona> rt = cq.from(TipoPersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

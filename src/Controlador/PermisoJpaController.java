/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Tablas.Permiso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.PermisoPersona;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) {
        if (permiso.getPermisoPersonaCollection() == null) {
            permiso.setPermisoPersonaCollection(new ArrayList<PermisoPersona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PermisoPersona> attachedPermisoPersonaCollection = new ArrayList<PermisoPersona>();
            for (PermisoPersona permisoPersonaCollectionPermisoPersonaToAttach : permiso.getPermisoPersonaCollection()) {
                permisoPersonaCollectionPermisoPersonaToAttach = em.getReference(permisoPersonaCollectionPermisoPersonaToAttach.getClass(), permisoPersonaCollectionPermisoPersonaToAttach.getId());
                attachedPermisoPersonaCollection.add(permisoPersonaCollectionPermisoPersonaToAttach);
            }
            permiso.setPermisoPersonaCollection(attachedPermisoPersonaCollection);
            em.persist(permiso);
            for (PermisoPersona permisoPersonaCollectionPermisoPersona : permiso.getPermisoPersonaCollection()) {
                Permiso oldPermisoPersonaidOfPermisoPersonaCollectionPermisoPersona = permisoPersonaCollectionPermisoPersona.getPermisoPersonaid();
                permisoPersonaCollectionPermisoPersona.setPermisoPersonaid(permiso);
                permisoPersonaCollectionPermisoPersona = em.merge(permisoPersonaCollectionPermisoPersona);
                if (oldPermisoPersonaidOfPermisoPersonaCollectionPermisoPersona != null) {
                    oldPermisoPersonaidOfPermisoPersonaCollectionPermisoPersona.getPermisoPersonaCollection().remove(permisoPersonaCollectionPermisoPersona);
                    oldPermisoPersonaidOfPermisoPersonaCollectionPermisoPersona = em.merge(oldPermisoPersonaidOfPermisoPersonaCollectionPermisoPersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getId());
            Collection<PermisoPersona> permisoPersonaCollectionOld = persistentPermiso.getPermisoPersonaCollection();
            Collection<PermisoPersona> permisoPersonaCollectionNew = permiso.getPermisoPersonaCollection();
            List<String> illegalOrphanMessages = null;
            for (PermisoPersona permisoPersonaCollectionOldPermisoPersona : permisoPersonaCollectionOld) {
                if (!permisoPersonaCollectionNew.contains(permisoPersonaCollectionOldPermisoPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisoPersona " + permisoPersonaCollectionOldPermisoPersona + " since its permisoPersonaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PermisoPersona> attachedPermisoPersonaCollectionNew = new ArrayList<PermisoPersona>();
            for (PermisoPersona permisoPersonaCollectionNewPermisoPersonaToAttach : permisoPersonaCollectionNew) {
                permisoPersonaCollectionNewPermisoPersonaToAttach = em.getReference(permisoPersonaCollectionNewPermisoPersonaToAttach.getClass(), permisoPersonaCollectionNewPermisoPersonaToAttach.getId());
                attachedPermisoPersonaCollectionNew.add(permisoPersonaCollectionNewPermisoPersonaToAttach);
            }
            permisoPersonaCollectionNew = attachedPermisoPersonaCollectionNew;
            permiso.setPermisoPersonaCollection(permisoPersonaCollectionNew);
            permiso = em.merge(permiso);
            for (PermisoPersona permisoPersonaCollectionNewPermisoPersona : permisoPersonaCollectionNew) {
                if (!permisoPersonaCollectionOld.contains(permisoPersonaCollectionNewPermisoPersona)) {
                    Permiso oldPermisoPersonaidOfPermisoPersonaCollectionNewPermisoPersona = permisoPersonaCollectionNewPermisoPersona.getPermisoPersonaid();
                    permisoPersonaCollectionNewPermisoPersona.setPermisoPersonaid(permiso);
                    permisoPersonaCollectionNewPermisoPersona = em.merge(permisoPersonaCollectionNewPermisoPersona);
                    if (oldPermisoPersonaidOfPermisoPersonaCollectionNewPermisoPersona != null && !oldPermisoPersonaidOfPermisoPersonaCollectionNewPermisoPersona.equals(permiso)) {
                        oldPermisoPersonaidOfPermisoPersonaCollectionNewPermisoPersona.getPermisoPersonaCollection().remove(permisoPersonaCollectionNewPermisoPersona);
                        oldPermisoPersonaidOfPermisoPersonaCollectionNewPermisoPersona = em.merge(oldPermisoPersonaidOfPermisoPersonaCollectionNewPermisoPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permiso.getId();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
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
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PermisoPersona> permisoPersonaCollectionOrphanCheck = permiso.getPermisoPersonaCollection();
            for (PermisoPersona permisoPersonaCollectionOrphanCheckPermisoPersona : permisoPersonaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permiso (" + permiso + ") cannot be destroyed since the PermisoPersona " + permisoPersonaCollectionOrphanCheckPermisoPersona + " in its permisoPersonaCollection field has a non-nullable permisoPersonaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(permiso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

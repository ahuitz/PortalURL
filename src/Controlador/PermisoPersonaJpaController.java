/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.TipoPersona;
import Tablas.Permiso;
import Tablas.PermisoPersona;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class PermisoPersonaJpaController implements Serializable {

    public PermisoPersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PermisoPersona permisoPersona) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPersona tipoPersonaid = permisoPersona.getTipoPersonaid();
            if (tipoPersonaid != null) {
                tipoPersonaid = em.getReference(tipoPersonaid.getClass(), tipoPersonaid.getId());
                permisoPersona.setTipoPersonaid(tipoPersonaid);
            }
            Permiso permisoPersonaid = permisoPersona.getPermisoPersonaid();
            if (permisoPersonaid != null) {
                permisoPersonaid = em.getReference(permisoPersonaid.getClass(), permisoPersonaid.getId());
                permisoPersona.setPermisoPersonaid(permisoPersonaid);
            }
            em.persist(permisoPersona);
            if (tipoPersonaid != null) {
                tipoPersonaid.getPermisoPersonaCollection().add(permisoPersona);
                tipoPersonaid = em.merge(tipoPersonaid);
            }
            if (permisoPersonaid != null) {
                permisoPersonaid.getPermisoPersonaCollection().add(permisoPersona);
                permisoPersonaid = em.merge(permisoPersonaid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PermisoPersona permisoPersona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PermisoPersona persistentPermisoPersona = em.find(PermisoPersona.class, permisoPersona.getId());
            TipoPersona tipoPersonaidOld = persistentPermisoPersona.getTipoPersonaid();
            TipoPersona tipoPersonaidNew = permisoPersona.getTipoPersonaid();
            Permiso permisoPersonaidOld = persistentPermisoPersona.getPermisoPersonaid();
            Permiso permisoPersonaidNew = permisoPersona.getPermisoPersonaid();
            if (tipoPersonaidNew != null) {
                tipoPersonaidNew = em.getReference(tipoPersonaidNew.getClass(), tipoPersonaidNew.getId());
                permisoPersona.setTipoPersonaid(tipoPersonaidNew);
            }
            if (permisoPersonaidNew != null) {
                permisoPersonaidNew = em.getReference(permisoPersonaidNew.getClass(), permisoPersonaidNew.getId());
                permisoPersona.setPermisoPersonaid(permisoPersonaidNew);
            }
            permisoPersona = em.merge(permisoPersona);
            if (tipoPersonaidOld != null && !tipoPersonaidOld.equals(tipoPersonaidNew)) {
                tipoPersonaidOld.getPermisoPersonaCollection().remove(permisoPersona);
                tipoPersonaidOld = em.merge(tipoPersonaidOld);
            }
            if (tipoPersonaidNew != null && !tipoPersonaidNew.equals(tipoPersonaidOld)) {
                tipoPersonaidNew.getPermisoPersonaCollection().add(permisoPersona);
                tipoPersonaidNew = em.merge(tipoPersonaidNew);
            }
            if (permisoPersonaidOld != null && !permisoPersonaidOld.equals(permisoPersonaidNew)) {
                permisoPersonaidOld.getPermisoPersonaCollection().remove(permisoPersona);
                permisoPersonaidOld = em.merge(permisoPersonaidOld);
            }
            if (permisoPersonaidNew != null && !permisoPersonaidNew.equals(permisoPersonaidOld)) {
                permisoPersonaidNew.getPermisoPersonaCollection().add(permisoPersona);
                permisoPersonaidNew = em.merge(permisoPersonaidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permisoPersona.getId();
                if (findPermisoPersona(id) == null) {
                    throw new NonexistentEntityException("The permisoPersona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PermisoPersona permisoPersona;
            try {
                permisoPersona = em.getReference(PermisoPersona.class, id);
                permisoPersona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisoPersona with id " + id + " no longer exists.", enfe);
            }
            TipoPersona tipoPersonaid = permisoPersona.getTipoPersonaid();
            if (tipoPersonaid != null) {
                tipoPersonaid.getPermisoPersonaCollection().remove(permisoPersona);
                tipoPersonaid = em.merge(tipoPersonaid);
            }
            Permiso permisoPersonaid = permisoPersona.getPermisoPersonaid();
            if (permisoPersonaid != null) {
                permisoPersonaid.getPermisoPersonaCollection().remove(permisoPersona);
                permisoPersonaid = em.merge(permisoPersonaid);
            }
            em.remove(permisoPersona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PermisoPersona> findPermisoPersonaEntities() {
        return findPermisoPersonaEntities(true, -1, -1);
    }

    public List<PermisoPersona> findPermisoPersonaEntities(int maxResults, int firstResult) {
        return findPermisoPersonaEntities(false, maxResults, firstResult);
    }

    private List<PermisoPersona> findPermisoPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PermisoPersona.class));
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

    public PermisoPersona findPermisoPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PermisoPersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PermisoPersona> rt = cq.from(PermisoPersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

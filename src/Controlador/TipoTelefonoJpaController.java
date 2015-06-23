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
import Tablas.Telefono;
import Tablas.TipoTelefono;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class TipoTelefonoJpaController implements Serializable {

    public TipoTelefonoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoTelefono tipoTelefono) {
        if (tipoTelefono.getTelefonoCollection() == null) {
            tipoTelefono.setTelefonoCollection(new ArrayList<Telefono>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Telefono> attachedTelefonoCollection = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionTelefonoToAttach : tipoTelefono.getTelefonoCollection()) {
                telefonoCollectionTelefonoToAttach = em.getReference(telefonoCollectionTelefonoToAttach.getClass(), telefonoCollectionTelefonoToAttach.getId());
                attachedTelefonoCollection.add(telefonoCollectionTelefonoToAttach);
            }
            tipoTelefono.setTelefonoCollection(attachedTelefonoCollection);
            em.persist(tipoTelefono);
            for (Telefono telefonoCollectionTelefono : tipoTelefono.getTelefonoCollection()) {
                TipoTelefono oldTipoTelefonoidOfTelefonoCollectionTelefono = telefonoCollectionTelefono.getTipoTelefonoid();
                telefonoCollectionTelefono.setTipoTelefonoid(tipoTelefono);
                telefonoCollectionTelefono = em.merge(telefonoCollectionTelefono);
                if (oldTipoTelefonoidOfTelefonoCollectionTelefono != null) {
                    oldTipoTelefonoidOfTelefonoCollectionTelefono.getTelefonoCollection().remove(telefonoCollectionTelefono);
                    oldTipoTelefonoidOfTelefonoCollectionTelefono = em.merge(oldTipoTelefonoidOfTelefonoCollectionTelefono);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoTelefono tipoTelefono) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoTelefono persistentTipoTelefono = em.find(TipoTelefono.class, tipoTelefono.getId());
            Collection<Telefono> telefonoCollectionOld = persistentTipoTelefono.getTelefonoCollection();
            Collection<Telefono> telefonoCollectionNew = tipoTelefono.getTelefonoCollection();
            List<String> illegalOrphanMessages = null;
            for (Telefono telefonoCollectionOldTelefono : telefonoCollectionOld) {
                if (!telefonoCollectionNew.contains(telefonoCollectionOldTelefono)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Telefono " + telefonoCollectionOldTelefono + " since its tipoTelefonoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Telefono> attachedTelefonoCollectionNew = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionNewTelefonoToAttach : telefonoCollectionNew) {
                telefonoCollectionNewTelefonoToAttach = em.getReference(telefonoCollectionNewTelefonoToAttach.getClass(), telefonoCollectionNewTelefonoToAttach.getId());
                attachedTelefonoCollectionNew.add(telefonoCollectionNewTelefonoToAttach);
            }
            telefonoCollectionNew = attachedTelefonoCollectionNew;
            tipoTelefono.setTelefonoCollection(telefonoCollectionNew);
            tipoTelefono = em.merge(tipoTelefono);
            for (Telefono telefonoCollectionNewTelefono : telefonoCollectionNew) {
                if (!telefonoCollectionOld.contains(telefonoCollectionNewTelefono)) {
                    TipoTelefono oldTipoTelefonoidOfTelefonoCollectionNewTelefono = telefonoCollectionNewTelefono.getTipoTelefonoid();
                    telefonoCollectionNewTelefono.setTipoTelefonoid(tipoTelefono);
                    telefonoCollectionNewTelefono = em.merge(telefonoCollectionNewTelefono);
                    if (oldTipoTelefonoidOfTelefonoCollectionNewTelefono != null && !oldTipoTelefonoidOfTelefonoCollectionNewTelefono.equals(tipoTelefono)) {
                        oldTipoTelefonoidOfTelefonoCollectionNewTelefono.getTelefonoCollection().remove(telefonoCollectionNewTelefono);
                        oldTipoTelefonoidOfTelefonoCollectionNewTelefono = em.merge(oldTipoTelefonoidOfTelefonoCollectionNewTelefono);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoTelefono.getId();
                if (findTipoTelefono(id) == null) {
                    throw new NonexistentEntityException("The tipoTelefono with id " + id + " no longer exists.");
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
            TipoTelefono tipoTelefono;
            try {
                tipoTelefono = em.getReference(TipoTelefono.class, id);
                tipoTelefono.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoTelefono with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Telefono> telefonoCollectionOrphanCheck = tipoTelefono.getTelefonoCollection();
            for (Telefono telefonoCollectionOrphanCheckTelefono : telefonoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoTelefono (" + tipoTelefono + ") cannot be destroyed since the Telefono " + telefonoCollectionOrphanCheckTelefono + " in its telefonoCollection field has a non-nullable tipoTelefonoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoTelefono);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoTelefono> findTipoTelefonoEntities() {
        return findTipoTelefonoEntities(true, -1, -1);
    }

    public List<TipoTelefono> findTipoTelefonoEntities(int maxResults, int firstResult) {
        return findTipoTelefonoEntities(false, maxResults, firstResult);
    }

    private List<TipoTelefono> findTipoTelefonoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoTelefono.class));
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

    public TipoTelefono findTipoTelefono(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoTelefono.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoTelefonoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoTelefono> rt = cq.from(TipoTelefono.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

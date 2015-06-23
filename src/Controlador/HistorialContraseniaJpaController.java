/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Tablas.HistorialContrasenia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class HistorialContraseniaJpaController implements Serializable {

    public HistorialContraseniaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistorialContrasenia historialContrasenia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuarioid = historialContrasenia.getUsuarioid();
            if (usuarioid != null) {
                usuarioid = em.getReference(usuarioid.getClass(), usuarioid.getId());
                historialContrasenia.setUsuarioid(usuarioid);
            }
            em.persist(historialContrasenia);
            if (usuarioid != null) {
                usuarioid.getHistorialContraseniaCollection().add(historialContrasenia);
                usuarioid = em.merge(usuarioid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistorialContrasenia historialContrasenia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialContrasenia persistentHistorialContrasenia = em.find(HistorialContrasenia.class, historialContrasenia.getId());
            Usuario usuarioidOld = persistentHistorialContrasenia.getUsuarioid();
            Usuario usuarioidNew = historialContrasenia.getUsuarioid();
            if (usuarioidNew != null) {
                usuarioidNew = em.getReference(usuarioidNew.getClass(), usuarioidNew.getId());
                historialContrasenia.setUsuarioid(usuarioidNew);
            }
            historialContrasenia = em.merge(historialContrasenia);
            if (usuarioidOld != null && !usuarioidOld.equals(usuarioidNew)) {
                usuarioidOld.getHistorialContraseniaCollection().remove(historialContrasenia);
                usuarioidOld = em.merge(usuarioidOld);
            }
            if (usuarioidNew != null && !usuarioidNew.equals(usuarioidOld)) {
                usuarioidNew.getHistorialContraseniaCollection().add(historialContrasenia);
                usuarioidNew = em.merge(usuarioidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historialContrasenia.getId();
                if (findHistorialContrasenia(id) == null) {
                    throw new NonexistentEntityException("The historialContrasenia with id " + id + " no longer exists.");
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
            HistorialContrasenia historialContrasenia;
            try {
                historialContrasenia = em.getReference(HistorialContrasenia.class, id);
                historialContrasenia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialContrasenia with id " + id + " no longer exists.", enfe);
            }
            Usuario usuarioid = historialContrasenia.getUsuarioid();
            if (usuarioid != null) {
                usuarioid.getHistorialContraseniaCollection().remove(historialContrasenia);
                usuarioid = em.merge(usuarioid);
            }
            em.remove(historialContrasenia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistorialContrasenia> findHistorialContraseniaEntities() {
        return findHistorialContraseniaEntities(true, -1, -1);
    }

    public List<HistorialContrasenia> findHistorialContraseniaEntities(int maxResults, int firstResult) {
        return findHistorialContraseniaEntities(false, maxResults, firstResult);
    }

    private List<HistorialContrasenia> findHistorialContraseniaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistorialContrasenia.class));
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

    public HistorialContrasenia findHistorialContrasenia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialContrasenia.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialContraseniaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistorialContrasenia> rt = cq.from(HistorialContrasenia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

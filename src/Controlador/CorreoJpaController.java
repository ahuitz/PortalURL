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
import Tablas.Contacto;
import Tablas.Correo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class CorreoJpaController implements Serializable {

    public CorreoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Correo correo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto contactoid = correo.getContactoid();
            if (contactoid != null) {
                contactoid = em.getReference(contactoid.getClass(), contactoid.getId());
                correo.setContactoid(contactoid);
            }
            em.persist(correo);
            if (contactoid != null) {
                contactoid.getCorreoCollection().add(correo);
                contactoid = em.merge(contactoid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Correo correo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Correo persistentCorreo = em.find(Correo.class, correo.getId());
            Contacto contactoidOld = persistentCorreo.getContactoid();
            Contacto contactoidNew = correo.getContactoid();
            if (contactoidNew != null) {
                contactoidNew = em.getReference(contactoidNew.getClass(), contactoidNew.getId());
                correo.setContactoid(contactoidNew);
            }
            correo = em.merge(correo);
            if (contactoidOld != null && !contactoidOld.equals(contactoidNew)) {
                contactoidOld.getCorreoCollection().remove(correo);
                contactoidOld = em.merge(contactoidOld);
            }
            if (contactoidNew != null && !contactoidNew.equals(contactoidOld)) {
                contactoidNew.getCorreoCollection().add(correo);
                contactoidNew = em.merge(contactoidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = correo.getId();
                if (findCorreo(id) == null) {
                    throw new NonexistentEntityException("The correo with id " + id + " no longer exists.");
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
            Correo correo;
            try {
                correo = em.getReference(Correo.class, id);
                correo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The correo with id " + id + " no longer exists.", enfe);
            }
            Contacto contactoid = correo.getContactoid();
            if (contactoid != null) {
                contactoid.getCorreoCollection().remove(correo);
                contactoid = em.merge(contactoid);
            }
            em.remove(correo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Correo> findCorreoEntities() {
        return findCorreoEntities(true, -1, -1);
    }

    public List<Correo> findCorreoEntities(int maxResults, int firstResult) {
        return findCorreoEntities(false, maxResults, firstResult);
    }

    private List<Correo> findCorreoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Correo.class));
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

    public Correo findCorreo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Correo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorreoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Correo> rt = cq.from(Correo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

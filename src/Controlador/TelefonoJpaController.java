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
import Tablas.TipoTelefono;
import Tablas.Contacto;
import Tablas.Telefono;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class TelefonoJpaController implements Serializable {

    public TelefonoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Telefono telefono) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoTelefono tipoTelefonoid = telefono.getTipoTelefonoid();
            if (tipoTelefonoid != null) {
                tipoTelefonoid = em.getReference(tipoTelefonoid.getClass(), tipoTelefonoid.getId());
                telefono.setTipoTelefonoid(tipoTelefonoid);
            }
            Contacto contactoid = telefono.getContactoid();
            if (contactoid != null) {
                contactoid = em.getReference(contactoid.getClass(), contactoid.getId());
                telefono.setContactoid(contactoid);
            }
            em.persist(telefono);
            if (tipoTelefonoid != null) {
                tipoTelefonoid.getTelefonoCollection().add(telefono);
                tipoTelefonoid = em.merge(tipoTelefonoid);
            }
            if (contactoid != null) {
                contactoid.getTelefonoCollection().add(telefono);
                contactoid = em.merge(contactoid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Telefono telefono) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Telefono persistentTelefono = em.find(Telefono.class, telefono.getId());
            TipoTelefono tipoTelefonoidOld = persistentTelefono.getTipoTelefonoid();
            TipoTelefono tipoTelefonoidNew = telefono.getTipoTelefonoid();
            Contacto contactoidOld = persistentTelefono.getContactoid();
            Contacto contactoidNew = telefono.getContactoid();
            if (tipoTelefonoidNew != null) {
                tipoTelefonoidNew = em.getReference(tipoTelefonoidNew.getClass(), tipoTelefonoidNew.getId());
                telefono.setTipoTelefonoid(tipoTelefonoidNew);
            }
            if (contactoidNew != null) {
                contactoidNew = em.getReference(contactoidNew.getClass(), contactoidNew.getId());
                telefono.setContactoid(contactoidNew);
            }
            telefono = em.merge(telefono);
            if (tipoTelefonoidOld != null && !tipoTelefonoidOld.equals(tipoTelefonoidNew)) {
                tipoTelefonoidOld.getTelefonoCollection().remove(telefono);
                tipoTelefonoidOld = em.merge(tipoTelefonoidOld);
            }
            if (tipoTelefonoidNew != null && !tipoTelefonoidNew.equals(tipoTelefonoidOld)) {
                tipoTelefonoidNew.getTelefonoCollection().add(telefono);
                tipoTelefonoidNew = em.merge(tipoTelefonoidNew);
            }
            if (contactoidOld != null && !contactoidOld.equals(contactoidNew)) {
                contactoidOld.getTelefonoCollection().remove(telefono);
                contactoidOld = em.merge(contactoidOld);
            }
            if (contactoidNew != null && !contactoidNew.equals(contactoidOld)) {
                contactoidNew.getTelefonoCollection().add(telefono);
                contactoidNew = em.merge(contactoidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = telefono.getId();
                if (findTelefono(id) == null) {
                    throw new NonexistentEntityException("The telefono with id " + id + " no longer exists.");
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
            Telefono telefono;
            try {
                telefono = em.getReference(Telefono.class, id);
                telefono.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The telefono with id " + id + " no longer exists.", enfe);
            }
            TipoTelefono tipoTelefonoid = telefono.getTipoTelefonoid();
            if (tipoTelefonoid != null) {
                tipoTelefonoid.getTelefonoCollection().remove(telefono);
                tipoTelefonoid = em.merge(tipoTelefonoid);
            }
            Contacto contactoid = telefono.getContactoid();
            if (contactoid != null) {
                contactoid.getTelefonoCollection().remove(telefono);
                contactoid = em.merge(contactoid);
            }
            em.remove(telefono);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Telefono> findTelefonoEntities() {
        return findTelefonoEntities(true, -1, -1);
    }

    public List<Telefono> findTelefonoEntities(int maxResults, int firstResult) {
        return findTelefonoEntities(false, maxResults, firstResult);
    }

    private List<Telefono> findTelefonoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Telefono.class));
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

    public Telefono findTelefono(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Telefono.class, id);
        } finally {
            em.close();
        }
    }

    public int getTelefonoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Telefono> rt = cq.from(Telefono.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

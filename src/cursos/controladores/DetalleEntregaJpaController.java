/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;

import cursos.controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.Archivos;
import cursos.percistence.DetalleEntrega;
import cursos.percistence.Entrega;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablito Garzona
 */
public class DetalleEntregaJpaController implements Serializable {

    public DetalleEntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleEntrega detalleEntrega) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivos archivosid = detalleEntrega.getArchivosid();
            if (archivosid != null) {
                archivosid = em.getReference(archivosid.getClass(), archivosid.getId());
                detalleEntrega.setArchivosid(archivosid);
            }
            Entrega entregaid = detalleEntrega.getEntregaid();
            if (entregaid != null) {
                entregaid = em.getReference(entregaid.getClass(), entregaid.getId());
                detalleEntrega.setEntregaid(entregaid);
            }
            em.persist(detalleEntrega);
            if (archivosid != null) {
                archivosid.getDetalleEntregaCollection().add(detalleEntrega);
                archivosid = em.merge(archivosid);
            }
            if (entregaid != null) {
                entregaid.getDetalleEntregaCollection().add(detalleEntrega);
                entregaid = em.merge(entregaid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleEntrega detalleEntrega) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleEntrega persistentDetalleEntrega = em.find(DetalleEntrega.class, detalleEntrega.getIdt());
            Archivos archivosidOld = persistentDetalleEntrega.getArchivosid();
            Archivos archivosidNew = detalleEntrega.getArchivosid();
            Entrega entregaidOld = persistentDetalleEntrega.getEntregaid();
            Entrega entregaidNew = detalleEntrega.getEntregaid();
            if (archivosidNew != null) {
                archivosidNew = em.getReference(archivosidNew.getClass(), archivosidNew.getId());
                detalleEntrega.setArchivosid(archivosidNew);
            }
            if (entregaidNew != null) {
                entregaidNew = em.getReference(entregaidNew.getClass(), entregaidNew.getId());
                detalleEntrega.setEntregaid(entregaidNew);
            }
            detalleEntrega = em.merge(detalleEntrega);
            if (archivosidOld != null && !archivosidOld.equals(archivosidNew)) {
                archivosidOld.getDetalleEntregaCollection().remove(detalleEntrega);
                archivosidOld = em.merge(archivosidOld);
            }
            if (archivosidNew != null && !archivosidNew.equals(archivosidOld)) {
                archivosidNew.getDetalleEntregaCollection().add(detalleEntrega);
                archivosidNew = em.merge(archivosidNew);
            }
            if (entregaidOld != null && !entregaidOld.equals(entregaidNew)) {
                entregaidOld.getDetalleEntregaCollection().remove(detalleEntrega);
                entregaidOld = em.merge(entregaidOld);
            }
            if (entregaidNew != null && !entregaidNew.equals(entregaidOld)) {
                entregaidNew.getDetalleEntregaCollection().add(detalleEntrega);
                entregaidNew = em.merge(entregaidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleEntrega.getIdt();
                if (findDetalleEntrega(id) == null) {
                    throw new NonexistentEntityException("The detalleEntrega with id " + id + " no longer exists.");
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
            DetalleEntrega detalleEntrega;
            try {
                detalleEntrega = em.getReference(DetalleEntrega.class, id);
                detalleEntrega.getIdt();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleEntrega with id " + id + " no longer exists.", enfe);
            }
            Archivos archivosid = detalleEntrega.getArchivosid();
            if (archivosid != null) {
                archivosid.getDetalleEntregaCollection().remove(detalleEntrega);
                archivosid = em.merge(archivosid);
            }
            Entrega entregaid = detalleEntrega.getEntregaid();
            if (entregaid != null) {
                entregaid.getDetalleEntregaCollection().remove(detalleEntrega);
                entregaid = em.merge(entregaid);
            }
            em.remove(detalleEntrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleEntrega> findDetalleEntregaEntities() {
        return findDetalleEntregaEntities(true, -1, -1);
    }

    public List<DetalleEntrega> findDetalleEntregaEntities(int maxResults, int firstResult) {
        return findDetalleEntregaEntities(false, maxResults, firstResult);
    }

    private List<DetalleEntrega> findDetalleEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleEntrega.class));
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

    public DetalleEntrega findDetalleEntrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleEntrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleEntrega> rt = cq.from(DetalleEntrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

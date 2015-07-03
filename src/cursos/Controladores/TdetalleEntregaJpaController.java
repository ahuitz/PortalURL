/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.Tarchivos;
import cursos.persistence.TdetalleEntrega;
import cursos.persistence.Tentrega;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TdetalleEntregaJpaController implements Serializable {

    public TdetalleEntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TdetalleEntrega tdetalleEntrega) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarchivos archivosid = tdetalleEntrega.getArchivosid();
            if (archivosid != null) {
                archivosid = em.getReference(archivosid.getClass(), archivosid.getId());
                tdetalleEntrega.setArchivosid(archivosid);
            }
            Tentrega entregaid = tdetalleEntrega.getEntregaid();
            if (entregaid != null) {
                entregaid = em.getReference(entregaid.getClass(), entregaid.getId());
                tdetalleEntrega.setEntregaid(entregaid);
            }
            em.persist(tdetalleEntrega);
            if (archivosid != null) {
                archivosid.getTdetalleEntregaList().add(tdetalleEntrega);
                archivosid = em.merge(archivosid);
            }
            if (entregaid != null) {
                entregaid.getTdetalleEntregaList().add(tdetalleEntrega);
                entregaid = em.merge(entregaid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TdetalleEntrega tdetalleEntrega) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TdetalleEntrega persistentTdetalleEntrega = em.find(TdetalleEntrega.class, tdetalleEntrega.getIdt());
            Tarchivos archivosidOld = persistentTdetalleEntrega.getArchivosid();
            Tarchivos archivosidNew = tdetalleEntrega.getArchivosid();
            Tentrega entregaidOld = persistentTdetalleEntrega.getEntregaid();
            Tentrega entregaidNew = tdetalleEntrega.getEntregaid();
            if (archivosidNew != null) {
                archivosidNew = em.getReference(archivosidNew.getClass(), archivosidNew.getId());
                tdetalleEntrega.setArchivosid(archivosidNew);
            }
            if (entregaidNew != null) {
                entregaidNew = em.getReference(entregaidNew.getClass(), entregaidNew.getId());
                tdetalleEntrega.setEntregaid(entregaidNew);
            }
            tdetalleEntrega = em.merge(tdetalleEntrega);
            if (archivosidOld != null && !archivosidOld.equals(archivosidNew)) {
                archivosidOld.getTdetalleEntregaList().remove(tdetalleEntrega);
                archivosidOld = em.merge(archivosidOld);
            }
            if (archivosidNew != null && !archivosidNew.equals(archivosidOld)) {
                archivosidNew.getTdetalleEntregaList().add(tdetalleEntrega);
                archivosidNew = em.merge(archivosidNew);
            }
            if (entregaidOld != null && !entregaidOld.equals(entregaidNew)) {
                entregaidOld.getTdetalleEntregaList().remove(tdetalleEntrega);
                entregaidOld = em.merge(entregaidOld);
            }
            if (entregaidNew != null && !entregaidNew.equals(entregaidOld)) {
                entregaidNew.getTdetalleEntregaList().add(tdetalleEntrega);
                entregaidNew = em.merge(entregaidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tdetalleEntrega.getIdt();
                if (findTdetalleEntrega(id) == null) {
                    throw new NonexistentEntityException("The tdetalleEntrega with id " + id + " no longer exists.");
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
            TdetalleEntrega tdetalleEntrega;
            try {
                tdetalleEntrega = em.getReference(TdetalleEntrega.class, id);
                tdetalleEntrega.getIdt();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tdetalleEntrega with id " + id + " no longer exists.", enfe);
            }
            Tarchivos archivosid = tdetalleEntrega.getArchivosid();
            if (archivosid != null) {
                archivosid.getTdetalleEntregaList().remove(tdetalleEntrega);
                archivosid = em.merge(archivosid);
            }
            Tentrega entregaid = tdetalleEntrega.getEntregaid();
            if (entregaid != null) {
                entregaid.getTdetalleEntregaList().remove(tdetalleEntrega);
                entregaid = em.merge(entregaid);
            }
            em.remove(tdetalleEntrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TdetalleEntrega> findTdetalleEntregaEntities() {
        return findTdetalleEntregaEntities(true, -1, -1);
    }

    public List<TdetalleEntrega> findTdetalleEntregaEntities(int maxResults, int firstResult) {
        return findTdetalleEntregaEntities(false, maxResults, firstResult);
    }

    private List<TdetalleEntrega> findTdetalleEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TdetalleEntrega.class));
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

    public TdetalleEntrega findTdetalleEntrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TdetalleEntrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getTdetalleEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TdetalleEntrega> rt = cq.from(TdetalleEntrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

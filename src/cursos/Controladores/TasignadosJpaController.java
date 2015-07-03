/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.NonexistentEntityException;
import cursos.persistence.Tasignados;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.TseccionCursos;
import cursos.persistence.Tusuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TasignadosJpaController implements Serializable {

    public TasignadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tasignados tasignados) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TseccionCursos seccionCursoid = tasignados.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid = em.getReference(seccionCursoid.getClass(), seccionCursoid.getId());
                tasignados.setSeccionCursoid(seccionCursoid);
            }
            Tusuario usuarioid = tasignados.getUsuarioid();
            if (usuarioid != null) {
                usuarioid = em.getReference(usuarioid.getClass(), usuarioid.getId());
                tasignados.setUsuarioid(usuarioid);
            }
            em.persist(tasignados);
            if (seccionCursoid != null) {
                seccionCursoid.getTasignadosList().add(tasignados);
                seccionCursoid = em.merge(seccionCursoid);
            }
            if (usuarioid != null) {
                usuarioid.getTasignadosList().add(tasignados);
                usuarioid = em.merge(usuarioid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tasignados tasignados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tasignados persistentTasignados = em.find(Tasignados.class, tasignados.getId());
            TseccionCursos seccionCursoidOld = persistentTasignados.getSeccionCursoid();
            TseccionCursos seccionCursoidNew = tasignados.getSeccionCursoid();
            Tusuario usuarioidOld = persistentTasignados.getUsuarioid();
            Tusuario usuarioidNew = tasignados.getUsuarioid();
            if (seccionCursoidNew != null) {
                seccionCursoidNew = em.getReference(seccionCursoidNew.getClass(), seccionCursoidNew.getId());
                tasignados.setSeccionCursoid(seccionCursoidNew);
            }
            if (usuarioidNew != null) {
                usuarioidNew = em.getReference(usuarioidNew.getClass(), usuarioidNew.getId());
                tasignados.setUsuarioid(usuarioidNew);
            }
            tasignados = em.merge(tasignados);
            if (seccionCursoidOld != null && !seccionCursoidOld.equals(seccionCursoidNew)) {
                seccionCursoidOld.getTasignadosList().remove(tasignados);
                seccionCursoidOld = em.merge(seccionCursoidOld);
            }
            if (seccionCursoidNew != null && !seccionCursoidNew.equals(seccionCursoidOld)) {
                seccionCursoidNew.getTasignadosList().add(tasignados);
                seccionCursoidNew = em.merge(seccionCursoidNew);
            }
            if (usuarioidOld != null && !usuarioidOld.equals(usuarioidNew)) {
                usuarioidOld.getTasignadosList().remove(tasignados);
                usuarioidOld = em.merge(usuarioidOld);
            }
            if (usuarioidNew != null && !usuarioidNew.equals(usuarioidOld)) {
                usuarioidNew.getTasignadosList().add(tasignados);
                usuarioidNew = em.merge(usuarioidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tasignados.getId();
                if (findTasignados(id) == null) {
                    throw new NonexistentEntityException("The tasignados with id " + id + " no longer exists.");
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
            Tasignados tasignados;
            try {
                tasignados = em.getReference(Tasignados.class, id);
                tasignados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tasignados with id " + id + " no longer exists.", enfe);
            }
            TseccionCursos seccionCursoid = tasignados.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid.getTasignadosList().remove(tasignados);
                seccionCursoid = em.merge(seccionCursoid);
            }
            Tusuario usuarioid = tasignados.getUsuarioid();
            if (usuarioid != null) {
                usuarioid.getTasignadosList().remove(tasignados);
                usuarioid = em.merge(usuarioid);
            }
            em.remove(tasignados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tasignados> findTasignadosEntities() {
        return findTasignadosEntities(true, -1, -1);
    }

    public List<Tasignados> findTasignadosEntities(int maxResults, int firstResult) {
        return findTasignadosEntities(false, maxResults, firstResult);
    }

    private List<Tasignados> findTasignadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tasignados.class));
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

    public Tasignados findTasignados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tasignados.class, id);
        } finally {
            em.close();
        }
    }

    public int getTasignadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tasignados> rt = cq.from(Tasignados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

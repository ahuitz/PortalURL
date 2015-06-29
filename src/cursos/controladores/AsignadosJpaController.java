/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;

import cursos.controladores.exceptions.NonexistentEntityException;
import cursos.controladores.exceptions.PreexistingEntityException;
import cursos.percistence.Asignados;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.SeccionCursos;
import cursos.percistence.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablito Garzona
 */
public class AsignadosJpaController implements Serializable {

    public AsignadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignados asignados) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionCursos seccionCursoid = asignados.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid = em.getReference(seccionCursoid.getClass(), seccionCursoid.getId());
                asignados.setSeccionCursoid(seccionCursoid);
            }
            Usuario usuarioid = asignados.getUsuarioid();
            if (usuarioid != null) {
                usuarioid = em.getReference(usuarioid.getClass(), usuarioid.getId());
                asignados.setUsuarioid(usuarioid);
            }
            em.persist(asignados);
            if (seccionCursoid != null) {
                seccionCursoid.getAsignadosCollection().add(asignados);
                seccionCursoid = em.merge(seccionCursoid);
            }
            if (usuarioid != null) {
                usuarioid.getAsignadosCollection().add(asignados);
                usuarioid = em.merge(usuarioid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAsignados(asignados.getId()) != null) {
                throw new PreexistingEntityException("Asignados " + asignados + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignados asignados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignados persistentAsignados = em.find(Asignados.class, asignados.getId());
            SeccionCursos seccionCursoidOld = persistentAsignados.getSeccionCursoid();
            SeccionCursos seccionCursoidNew = asignados.getSeccionCursoid();
            Usuario usuarioidOld = persistentAsignados.getUsuarioid();
            Usuario usuarioidNew = asignados.getUsuarioid();
            if (seccionCursoidNew != null) {
                seccionCursoidNew = em.getReference(seccionCursoidNew.getClass(), seccionCursoidNew.getId());
                asignados.setSeccionCursoid(seccionCursoidNew);
            }
            if (usuarioidNew != null) {
                usuarioidNew = em.getReference(usuarioidNew.getClass(), usuarioidNew.getId());
                asignados.setUsuarioid(usuarioidNew);
            }
            asignados = em.merge(asignados);
            if (seccionCursoidOld != null && !seccionCursoidOld.equals(seccionCursoidNew)) {
                seccionCursoidOld.getAsignadosCollection().remove(asignados);
                seccionCursoidOld = em.merge(seccionCursoidOld);
            }
            if (seccionCursoidNew != null && !seccionCursoidNew.equals(seccionCursoidOld)) {
                seccionCursoidNew.getAsignadosCollection().add(asignados);
                seccionCursoidNew = em.merge(seccionCursoidNew);
            }
            if (usuarioidOld != null && !usuarioidOld.equals(usuarioidNew)) {
                usuarioidOld.getAsignadosCollection().remove(asignados);
                usuarioidOld = em.merge(usuarioidOld);
            }
            if (usuarioidNew != null && !usuarioidNew.equals(usuarioidOld)) {
                usuarioidNew.getAsignadosCollection().add(asignados);
                usuarioidNew = em.merge(usuarioidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asignados.getId();
                if (findAsignados(id) == null) {
                    throw new NonexistentEntityException("The asignados with id " + id + " no longer exists.");
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
            Asignados asignados;
            try {
                asignados = em.getReference(Asignados.class, id);
                asignados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignados with id " + id + " no longer exists.", enfe);
            }
            SeccionCursos seccionCursoid = asignados.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid.getAsignadosCollection().remove(asignados);
                seccionCursoid = em.merge(seccionCursoid);
            }
            Usuario usuarioid = asignados.getUsuarioid();
            if (usuarioid != null) {
                usuarioid.getAsignadosCollection().remove(asignados);
                usuarioid = em.merge(usuarioid);
            }
            em.remove(asignados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asignados> findAsignadosEntities() {
        return findAsignadosEntities(true, -1, -1);
    }

    public List<Asignados> findAsignadosEntities(int maxResults, int firstResult) {
        return findAsignadosEntities(false, maxResults, firstResult);
    }

    private List<Asignados> findAsignadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignados.class));
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

    public Asignados findAsignados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignados.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignados> rt = cq.from(Asignados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

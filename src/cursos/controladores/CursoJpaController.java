/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;

import Principal.exceptions.IllegalOrphanException; // Excepciones del principal
import Principal.exceptions.NonexistentEntityException; // Excepciones del principal
import cursos.percistence.Curso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.SeccionCursos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablito Garzona
 */
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) {
        if (curso.getSeccionCursosCollection() == null) {
            curso.setSeccionCursosCollection(new ArrayList<SeccionCursos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SeccionCursos> attachedSeccionCursosCollection = new ArrayList<SeccionCursos>();
            for (SeccionCursos seccionCursosCollectionSeccionCursosToAttach : curso.getSeccionCursosCollection()) {
                seccionCursosCollectionSeccionCursosToAttach = em.getReference(seccionCursosCollectionSeccionCursosToAttach.getClass(), seccionCursosCollectionSeccionCursosToAttach.getId());
                attachedSeccionCursosCollection.add(seccionCursosCollectionSeccionCursosToAttach);
            }
            curso.setSeccionCursosCollection(attachedSeccionCursosCollection);
            em.persist(curso);
            for (SeccionCursos seccionCursosCollectionSeccionCursos : curso.getSeccionCursosCollection()) {
                Curso oldCursoidOfSeccionCursosCollectionSeccionCursos = seccionCursosCollectionSeccionCursos.getCursoid();
                seccionCursosCollectionSeccionCursos.setCursoid(curso);
                seccionCursosCollectionSeccionCursos = em.merge(seccionCursosCollectionSeccionCursos);
                if (oldCursoidOfSeccionCursosCollectionSeccionCursos != null) {
                    oldCursoidOfSeccionCursosCollectionSeccionCursos.getSeccionCursosCollection().remove(seccionCursosCollectionSeccionCursos);
                    oldCursoidOfSeccionCursosCollectionSeccionCursos = em.merge(oldCursoidOfSeccionCursosCollectionSeccionCursos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getId());
            Collection<SeccionCursos> seccionCursosCollectionOld = persistentCurso.getSeccionCursosCollection();
            Collection<SeccionCursos> seccionCursosCollectionNew = curso.getSeccionCursosCollection();
            List<String> illegalOrphanMessages = null;
            for (SeccionCursos seccionCursosCollectionOldSeccionCursos : seccionCursosCollectionOld) {
                if (!seccionCursosCollectionNew.contains(seccionCursosCollectionOldSeccionCursos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionCursos " + seccionCursosCollectionOldSeccionCursos + " since its cursoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SeccionCursos> attachedSeccionCursosCollectionNew = new ArrayList<SeccionCursos>();
            for (SeccionCursos seccionCursosCollectionNewSeccionCursosToAttach : seccionCursosCollectionNew) {
                seccionCursosCollectionNewSeccionCursosToAttach = em.getReference(seccionCursosCollectionNewSeccionCursosToAttach.getClass(), seccionCursosCollectionNewSeccionCursosToAttach.getId());
                attachedSeccionCursosCollectionNew.add(seccionCursosCollectionNewSeccionCursosToAttach);
            }
            seccionCursosCollectionNew = attachedSeccionCursosCollectionNew;
            curso.setSeccionCursosCollection(seccionCursosCollectionNew);
            curso = em.merge(curso);
            for (SeccionCursos seccionCursosCollectionNewSeccionCursos : seccionCursosCollectionNew) {
                if (!seccionCursosCollectionOld.contains(seccionCursosCollectionNewSeccionCursos)) {
                    Curso oldCursoidOfSeccionCursosCollectionNewSeccionCursos = seccionCursosCollectionNewSeccionCursos.getCursoid();
                    seccionCursosCollectionNewSeccionCursos.setCursoid(curso);
                    seccionCursosCollectionNewSeccionCursos = em.merge(seccionCursosCollectionNewSeccionCursos);
                    if (oldCursoidOfSeccionCursosCollectionNewSeccionCursos != null && !oldCursoidOfSeccionCursosCollectionNewSeccionCursos.equals(curso)) {
                        oldCursoidOfSeccionCursosCollectionNewSeccionCursos.getSeccionCursosCollection().remove(seccionCursosCollectionNewSeccionCursos);
                        oldCursoidOfSeccionCursosCollectionNewSeccionCursos = em.merge(oldCursoidOfSeccionCursosCollectionNewSeccionCursos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = curso.getId();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SeccionCursos> seccionCursosCollectionOrphanCheck = curso.getSeccionCursosCollection();
            for (SeccionCursos seccionCursosCollectionOrphanCheckSeccionCursos : seccionCursosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the SeccionCursos " + seccionCursosCollectionOrphanCheckSeccionCursos + " in its seccionCursosCollection field has a non-nullable cursoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;

import cursos.controladores.exceptions.IllegalOrphanException;
import cursos.controladores.exceptions.NonexistentEntityException;
import cursos.percistence.Seccion;
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
public class SeccionJpaController implements Serializable {

    public SeccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seccion seccion) {
        if (seccion.getSeccionCursosCollection() == null) {
            seccion.setSeccionCursosCollection(new ArrayList<SeccionCursos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SeccionCursos> attachedSeccionCursosCollection = new ArrayList<SeccionCursos>();
            for (SeccionCursos seccionCursosCollectionSeccionCursosToAttach : seccion.getSeccionCursosCollection()) {
                seccionCursosCollectionSeccionCursosToAttach = em.getReference(seccionCursosCollectionSeccionCursosToAttach.getClass(), seccionCursosCollectionSeccionCursosToAttach.getId());
                attachedSeccionCursosCollection.add(seccionCursosCollectionSeccionCursosToAttach);
            }
            seccion.setSeccionCursosCollection(attachedSeccionCursosCollection);
            em.persist(seccion);
            for (SeccionCursos seccionCursosCollectionSeccionCursos : seccion.getSeccionCursosCollection()) {
                Seccion oldSeccionidOfSeccionCursosCollectionSeccionCursos = seccionCursosCollectionSeccionCursos.getSeccionid();
                seccionCursosCollectionSeccionCursos.setSeccionid(seccion);
                seccionCursosCollectionSeccionCursos = em.merge(seccionCursosCollectionSeccionCursos);
                if (oldSeccionidOfSeccionCursosCollectionSeccionCursos != null) {
                    oldSeccionidOfSeccionCursosCollectionSeccionCursos.getSeccionCursosCollection().remove(seccionCursosCollectionSeccionCursos);
                    oldSeccionidOfSeccionCursosCollectionSeccionCursos = em.merge(oldSeccionidOfSeccionCursosCollectionSeccionCursos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seccion seccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion persistentSeccion = em.find(Seccion.class, seccion.getId());
            Collection<SeccionCursos> seccionCursosCollectionOld = persistentSeccion.getSeccionCursosCollection();
            Collection<SeccionCursos> seccionCursosCollectionNew = seccion.getSeccionCursosCollection();
            List<String> illegalOrphanMessages = null;
            for (SeccionCursos seccionCursosCollectionOldSeccionCursos : seccionCursosCollectionOld) {
                if (!seccionCursosCollectionNew.contains(seccionCursosCollectionOldSeccionCursos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionCursos " + seccionCursosCollectionOldSeccionCursos + " since its seccionid field is not nullable.");
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
            seccion.setSeccionCursosCollection(seccionCursosCollectionNew);
            seccion = em.merge(seccion);
            for (SeccionCursos seccionCursosCollectionNewSeccionCursos : seccionCursosCollectionNew) {
                if (!seccionCursosCollectionOld.contains(seccionCursosCollectionNewSeccionCursos)) {
                    Seccion oldSeccionidOfSeccionCursosCollectionNewSeccionCursos = seccionCursosCollectionNewSeccionCursos.getSeccionid();
                    seccionCursosCollectionNewSeccionCursos.setSeccionid(seccion);
                    seccionCursosCollectionNewSeccionCursos = em.merge(seccionCursosCollectionNewSeccionCursos);
                    if (oldSeccionidOfSeccionCursosCollectionNewSeccionCursos != null && !oldSeccionidOfSeccionCursosCollectionNewSeccionCursos.equals(seccion)) {
                        oldSeccionidOfSeccionCursosCollectionNewSeccionCursos.getSeccionCursosCollection().remove(seccionCursosCollectionNewSeccionCursos);
                        oldSeccionidOfSeccionCursosCollectionNewSeccionCursos = em.merge(oldSeccionidOfSeccionCursosCollectionNewSeccionCursos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = seccion.getId();
                if (findSeccion(id) == null) {
                    throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.");
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
            Seccion seccion;
            try {
                seccion = em.getReference(Seccion.class, id);
                seccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SeccionCursos> seccionCursosCollectionOrphanCheck = seccion.getSeccionCursosCollection();
            for (SeccionCursos seccionCursosCollectionOrphanCheckSeccionCursos : seccionCursosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Seccion (" + seccion + ") cannot be destroyed since the SeccionCursos " + seccionCursosCollectionOrphanCheckSeccionCursos + " in its seccionCursosCollection field has a non-nullable seccionid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(seccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seccion> findSeccionEntities() {
        return findSeccionEntities(true, -1, -1);
    }

    public List<Seccion> findSeccionEntities(int maxResults, int firstResult) {
        return findSeccionEntities(false, maxResults, firstResult);
    }

    private List<Seccion> findSeccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seccion.class));
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

    public Seccion findSeccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seccion> rt = cq.from(Seccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.IllegalOrphanException;
import cursos.Controladores.exceptions.NonexistentEntityException;
import cursos.persistence.Tseccion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.TseccionCursos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TseccionJpaController implements Serializable {

    public TseccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tseccion tseccion) {
        if (tseccion.getTseccionCursosList() == null) {
            tseccion.setTseccionCursosList(new ArrayList<TseccionCursos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TseccionCursos> attachedTseccionCursosList = new ArrayList<TseccionCursos>();
            for (TseccionCursos tseccionCursosListTseccionCursosToAttach : tseccion.getTseccionCursosList()) {
                tseccionCursosListTseccionCursosToAttach = em.getReference(tseccionCursosListTseccionCursosToAttach.getClass(), tseccionCursosListTseccionCursosToAttach.getId());
                attachedTseccionCursosList.add(tseccionCursosListTseccionCursosToAttach);
            }
            tseccion.setTseccionCursosList(attachedTseccionCursosList);
            em.persist(tseccion);
            for (TseccionCursos tseccionCursosListTseccionCursos : tseccion.getTseccionCursosList()) {
                Tseccion oldSeccionidOfTseccionCursosListTseccionCursos = tseccionCursosListTseccionCursos.getSeccionid();
                tseccionCursosListTseccionCursos.setSeccionid(tseccion);
                tseccionCursosListTseccionCursos = em.merge(tseccionCursosListTseccionCursos);
                if (oldSeccionidOfTseccionCursosListTseccionCursos != null) {
                    oldSeccionidOfTseccionCursosListTseccionCursos.getTseccionCursosList().remove(tseccionCursosListTseccionCursos);
                    oldSeccionidOfTseccionCursosListTseccionCursos = em.merge(oldSeccionidOfTseccionCursosListTseccionCursos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tseccion tseccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tseccion persistentTseccion = em.find(Tseccion.class, tseccion.getId());
            List<TseccionCursos> tseccionCursosListOld = persistentTseccion.getTseccionCursosList();
            List<TseccionCursos> tseccionCursosListNew = tseccion.getTseccionCursosList();
            List<String> illegalOrphanMessages = null;
            for (TseccionCursos tseccionCursosListOldTseccionCursos : tseccionCursosListOld) {
                if (!tseccionCursosListNew.contains(tseccionCursosListOldTseccionCursos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TseccionCursos " + tseccionCursosListOldTseccionCursos + " since its seccionid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TseccionCursos> attachedTseccionCursosListNew = new ArrayList<TseccionCursos>();
            for (TseccionCursos tseccionCursosListNewTseccionCursosToAttach : tseccionCursosListNew) {
                tseccionCursosListNewTseccionCursosToAttach = em.getReference(tseccionCursosListNewTseccionCursosToAttach.getClass(), tseccionCursosListNewTseccionCursosToAttach.getId());
                attachedTseccionCursosListNew.add(tseccionCursosListNewTseccionCursosToAttach);
            }
            tseccionCursosListNew = attachedTseccionCursosListNew;
            tseccion.setTseccionCursosList(tseccionCursosListNew);
            tseccion = em.merge(tseccion);
            for (TseccionCursos tseccionCursosListNewTseccionCursos : tseccionCursosListNew) {
                if (!tseccionCursosListOld.contains(tseccionCursosListNewTseccionCursos)) {
                    Tseccion oldSeccionidOfTseccionCursosListNewTseccionCursos = tseccionCursosListNewTseccionCursos.getSeccionid();
                    tseccionCursosListNewTseccionCursos.setSeccionid(tseccion);
                    tseccionCursosListNewTseccionCursos = em.merge(tseccionCursosListNewTseccionCursos);
                    if (oldSeccionidOfTseccionCursosListNewTseccionCursos != null && !oldSeccionidOfTseccionCursosListNewTseccionCursos.equals(tseccion)) {
                        oldSeccionidOfTseccionCursosListNewTseccionCursos.getTseccionCursosList().remove(tseccionCursosListNewTseccionCursos);
                        oldSeccionidOfTseccionCursosListNewTseccionCursos = em.merge(oldSeccionidOfTseccionCursosListNewTseccionCursos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tseccion.getId();
                if (findTseccion(id) == null) {
                    throw new NonexistentEntityException("The tseccion with id " + id + " no longer exists.");
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
            Tseccion tseccion;
            try {
                tseccion = em.getReference(Tseccion.class, id);
                tseccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tseccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TseccionCursos> tseccionCursosListOrphanCheck = tseccion.getTseccionCursosList();
            for (TseccionCursos tseccionCursosListOrphanCheckTseccionCursos : tseccionCursosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tseccion (" + tseccion + ") cannot be destroyed since the TseccionCursos " + tseccionCursosListOrphanCheckTseccionCursos + " in its tseccionCursosList field has a non-nullable seccionid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tseccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tseccion> findTseccionEntities() {
        return findTseccionEntities(true, -1, -1);
    }

    public List<Tseccion> findTseccionEntities(int maxResults, int firstResult) {
        return findTseccionEntities(false, maxResults, firstResult);
    }

    private List<Tseccion> findTseccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tseccion.class));
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

    public Tseccion findTseccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tseccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTseccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tseccion> rt = cq.from(Tseccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

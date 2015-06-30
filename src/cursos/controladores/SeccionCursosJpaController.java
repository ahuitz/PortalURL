/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;
import Principal.exceptions.IllegalOrphanException; // Excepciones del principal
import Principal.exceptions.NonexistentEntityException; // Excepciones del principal
import Principal.exceptions.PreexistingEntityException;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.Curso;
import cursos.percistence.Seccion;
import cursos.percistence.Usuario;
import cursos.percistence.Recurso;
import java.util.ArrayList;
import java.util.Collection;
import cursos.percistence.Tarea;
import cursos.percistence.Asignados;
import cursos.percistence.SeccionCursos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablito Garzona
 */
public class SeccionCursosJpaController implements Serializable {

    public SeccionCursosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SeccionCursos seccionCursos) throws PreexistingEntityException, Exception {
        if (seccionCursos.getRecursoCollection() == null) {
            seccionCursos.setRecursoCollection(new ArrayList<Recurso>());
        }
        if (seccionCursos.getTareaCollection() == null) {
            seccionCursos.setTareaCollection(new ArrayList<Tarea>());
        }
        if (seccionCursos.getAsignadosCollection() == null) {
            seccionCursos.setAsignadosCollection(new ArrayList<Asignados>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso cursoid = seccionCursos.getCursoid();
            if (cursoid != null) {
                cursoid = em.getReference(cursoid.getClass(), cursoid.getId());
                seccionCursos.setCursoid(cursoid);
            }
            Seccion seccionid = seccionCursos.getSeccionid();
            if (seccionid != null) {
                seccionid = em.getReference(seccionid.getClass(), seccionid.getId());
                seccionCursos.setSeccionid(seccionid);
            }
            Usuario catedraticoid = seccionCursos.getCatedraticoid();
            if (catedraticoid != null) {
                catedraticoid = em.getReference(catedraticoid.getClass(), catedraticoid.getId());
                seccionCursos.setCatedraticoid(catedraticoid);
            }
            Collection<Recurso> attachedRecursoCollection = new ArrayList<Recurso>();
            for (Recurso recursoCollectionRecursoToAttach : seccionCursos.getRecursoCollection()) {
                recursoCollectionRecursoToAttach = em.getReference(recursoCollectionRecursoToAttach.getClass(), recursoCollectionRecursoToAttach.getId());
                attachedRecursoCollection.add(recursoCollectionRecursoToAttach);
            }
            seccionCursos.setRecursoCollection(attachedRecursoCollection);
            Collection<Tarea> attachedTareaCollection = new ArrayList<Tarea>();
            for (Tarea tareaCollectionTareaToAttach : seccionCursos.getTareaCollection()) {
                tareaCollectionTareaToAttach = em.getReference(tareaCollectionTareaToAttach.getClass(), tareaCollectionTareaToAttach.getId());
                attachedTareaCollection.add(tareaCollectionTareaToAttach);
            }
            seccionCursos.setTareaCollection(attachedTareaCollection);
            Collection<Asignados> attachedAsignadosCollection = new ArrayList<Asignados>();
            for (Asignados asignadosCollectionAsignadosToAttach : seccionCursos.getAsignadosCollection()) {
                asignadosCollectionAsignadosToAttach = em.getReference(asignadosCollectionAsignadosToAttach.getClass(), asignadosCollectionAsignadosToAttach.getId());
                attachedAsignadosCollection.add(asignadosCollectionAsignadosToAttach);
            }
            seccionCursos.setAsignadosCollection(attachedAsignadosCollection);
            em.persist(seccionCursos);
            if (cursoid != null) {
                cursoid.getSeccionCursosCollection().add(seccionCursos);
                cursoid = em.merge(cursoid);
            }
            if (seccionid != null) {
                seccionid.getSeccionCursosCollection().add(seccionCursos);
                seccionid = em.merge(seccionid);
            }
            if (catedraticoid != null) {
                catedraticoid.getSeccionCursosCollection().add(seccionCursos);
                catedraticoid = em.merge(catedraticoid);
            }
            for (Recurso recursoCollectionRecurso : seccionCursos.getRecursoCollection()) {
                SeccionCursos oldSeccionCursoidOfRecursoCollectionRecurso = recursoCollectionRecurso.getSeccionCursoid();
                recursoCollectionRecurso.setSeccionCursoid(seccionCursos);
                recursoCollectionRecurso = em.merge(recursoCollectionRecurso);
                if (oldSeccionCursoidOfRecursoCollectionRecurso != null) {
                    oldSeccionCursoidOfRecursoCollectionRecurso.getRecursoCollection().remove(recursoCollectionRecurso);
                    oldSeccionCursoidOfRecursoCollectionRecurso = em.merge(oldSeccionCursoidOfRecursoCollectionRecurso);
                }
            }
            for (Tarea tareaCollectionTarea : seccionCursos.getTareaCollection()) {
                SeccionCursos oldSeccionCursoidOfTareaCollectionTarea = tareaCollectionTarea.getSeccionCursoid();
                tareaCollectionTarea.setSeccionCursoid(seccionCursos);
                tareaCollectionTarea = em.merge(tareaCollectionTarea);
                if (oldSeccionCursoidOfTareaCollectionTarea != null) {
                    oldSeccionCursoidOfTareaCollectionTarea.getTareaCollection().remove(tareaCollectionTarea);
                    oldSeccionCursoidOfTareaCollectionTarea = em.merge(oldSeccionCursoidOfTareaCollectionTarea);
                }
            }
            for (Asignados asignadosCollectionAsignados : seccionCursos.getAsignadosCollection()) {
                SeccionCursos oldSeccionCursoidOfAsignadosCollectionAsignados = asignadosCollectionAsignados.getSeccionCursoid();
                asignadosCollectionAsignados.setSeccionCursoid(seccionCursos);
                asignadosCollectionAsignados = em.merge(asignadosCollectionAsignados);
                if (oldSeccionCursoidOfAsignadosCollectionAsignados != null) {
                    oldSeccionCursoidOfAsignadosCollectionAsignados.getAsignadosCollection().remove(asignadosCollectionAsignados);
                    oldSeccionCursoidOfAsignadosCollectionAsignados = em.merge(oldSeccionCursoidOfAsignadosCollectionAsignados);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSeccionCursos(seccionCursos.getId()) != null) {
                throw new PreexistingEntityException("SeccionCursos " + seccionCursos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SeccionCursos seccionCursos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionCursos persistentSeccionCursos = em.find(SeccionCursos.class, seccionCursos.getId());
            Curso cursoidOld = persistentSeccionCursos.getCursoid();
            Curso cursoidNew = seccionCursos.getCursoid();
            Seccion seccionidOld = persistentSeccionCursos.getSeccionid();
            Seccion seccionidNew = seccionCursos.getSeccionid();
            Usuario catedraticoidOld = persistentSeccionCursos.getCatedraticoid();
            Usuario catedraticoidNew = seccionCursos.getCatedraticoid();
            Collection<Recurso> recursoCollectionOld = persistentSeccionCursos.getRecursoCollection();
            Collection<Recurso> recursoCollectionNew = seccionCursos.getRecursoCollection();
            Collection<Tarea> tareaCollectionOld = persistentSeccionCursos.getTareaCollection();
            Collection<Tarea> tareaCollectionNew = seccionCursos.getTareaCollection();
            Collection<Asignados> asignadosCollectionOld = persistentSeccionCursos.getAsignadosCollection();
            Collection<Asignados> asignadosCollectionNew = seccionCursos.getAsignadosCollection();
            List<String> illegalOrphanMessages = null;
            for (Recurso recursoCollectionOldRecurso : recursoCollectionOld) {
                if (!recursoCollectionNew.contains(recursoCollectionOldRecurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recurso " + recursoCollectionOldRecurso + " since its seccionCursoid field is not nullable.");
                }
            }
            for (Tarea tareaCollectionOldTarea : tareaCollectionOld) {
                if (!tareaCollectionNew.contains(tareaCollectionOldTarea)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarea " + tareaCollectionOldTarea + " since its seccionCursoid field is not nullable.");
                }
            }
            for (Asignados asignadosCollectionOldAsignados : asignadosCollectionOld) {
                if (!asignadosCollectionNew.contains(asignadosCollectionOldAsignados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asignados " + asignadosCollectionOldAsignados + " since its seccionCursoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cursoidNew != null) {
                cursoidNew = em.getReference(cursoidNew.getClass(), cursoidNew.getId());
                seccionCursos.setCursoid(cursoidNew);
            }
            if (seccionidNew != null) {
                seccionidNew = em.getReference(seccionidNew.getClass(), seccionidNew.getId());
                seccionCursos.setSeccionid(seccionidNew);
            }
            if (catedraticoidNew != null) {
                catedraticoidNew = em.getReference(catedraticoidNew.getClass(), catedraticoidNew.getId());
                seccionCursos.setCatedraticoid(catedraticoidNew);
            }
            Collection<Recurso> attachedRecursoCollectionNew = new ArrayList<Recurso>();
            for (Recurso recursoCollectionNewRecursoToAttach : recursoCollectionNew) {
                recursoCollectionNewRecursoToAttach = em.getReference(recursoCollectionNewRecursoToAttach.getClass(), recursoCollectionNewRecursoToAttach.getId());
                attachedRecursoCollectionNew.add(recursoCollectionNewRecursoToAttach);
            }
            recursoCollectionNew = attachedRecursoCollectionNew;
            seccionCursos.setRecursoCollection(recursoCollectionNew);
            Collection<Tarea> attachedTareaCollectionNew = new ArrayList<Tarea>();
            for (Tarea tareaCollectionNewTareaToAttach : tareaCollectionNew) {
                tareaCollectionNewTareaToAttach = em.getReference(tareaCollectionNewTareaToAttach.getClass(), tareaCollectionNewTareaToAttach.getId());
                attachedTareaCollectionNew.add(tareaCollectionNewTareaToAttach);
            }
            tareaCollectionNew = attachedTareaCollectionNew;
            seccionCursos.setTareaCollection(tareaCollectionNew);
            Collection<Asignados> attachedAsignadosCollectionNew = new ArrayList<Asignados>();
            for (Asignados asignadosCollectionNewAsignadosToAttach : asignadosCollectionNew) {
                asignadosCollectionNewAsignadosToAttach = em.getReference(asignadosCollectionNewAsignadosToAttach.getClass(), asignadosCollectionNewAsignadosToAttach.getId());
                attachedAsignadosCollectionNew.add(asignadosCollectionNewAsignadosToAttach);
            }
            asignadosCollectionNew = attachedAsignadosCollectionNew;
            seccionCursos.setAsignadosCollection(asignadosCollectionNew);
            seccionCursos = em.merge(seccionCursos);
            if (cursoidOld != null && !cursoidOld.equals(cursoidNew)) {
                cursoidOld.getSeccionCursosCollection().remove(seccionCursos);
                cursoidOld = em.merge(cursoidOld);
            }
            if (cursoidNew != null && !cursoidNew.equals(cursoidOld)) {
                cursoidNew.getSeccionCursosCollection().add(seccionCursos);
                cursoidNew = em.merge(cursoidNew);
            }
            if (seccionidOld != null && !seccionidOld.equals(seccionidNew)) {
                seccionidOld.getSeccionCursosCollection().remove(seccionCursos);
                seccionidOld = em.merge(seccionidOld);
            }
            if (seccionidNew != null && !seccionidNew.equals(seccionidOld)) {
                seccionidNew.getSeccionCursosCollection().add(seccionCursos);
                seccionidNew = em.merge(seccionidNew);
            }
            if (catedraticoidOld != null && !catedraticoidOld.equals(catedraticoidNew)) {
                catedraticoidOld.getSeccionCursosCollection().remove(seccionCursos);
                catedraticoidOld = em.merge(catedraticoidOld);
            }
            if (catedraticoidNew != null && !catedraticoidNew.equals(catedraticoidOld)) {
                catedraticoidNew.getSeccionCursosCollection().add(seccionCursos);
                catedraticoidNew = em.merge(catedraticoidNew);
            }
            for (Recurso recursoCollectionNewRecurso : recursoCollectionNew) {
                if (!recursoCollectionOld.contains(recursoCollectionNewRecurso)) {
                    SeccionCursos oldSeccionCursoidOfRecursoCollectionNewRecurso = recursoCollectionNewRecurso.getSeccionCursoid();
                    recursoCollectionNewRecurso.setSeccionCursoid(seccionCursos);
                    recursoCollectionNewRecurso = em.merge(recursoCollectionNewRecurso);
                    if (oldSeccionCursoidOfRecursoCollectionNewRecurso != null && !oldSeccionCursoidOfRecursoCollectionNewRecurso.equals(seccionCursos)) {
                        oldSeccionCursoidOfRecursoCollectionNewRecurso.getRecursoCollection().remove(recursoCollectionNewRecurso);
                        oldSeccionCursoidOfRecursoCollectionNewRecurso = em.merge(oldSeccionCursoidOfRecursoCollectionNewRecurso);
                    }
                }
            }
            for (Tarea tareaCollectionNewTarea : tareaCollectionNew) {
                if (!tareaCollectionOld.contains(tareaCollectionNewTarea)) {
                    SeccionCursos oldSeccionCursoidOfTareaCollectionNewTarea = tareaCollectionNewTarea.getSeccionCursoid();
                    tareaCollectionNewTarea.setSeccionCursoid(seccionCursos);
                    tareaCollectionNewTarea = em.merge(tareaCollectionNewTarea);
                    if (oldSeccionCursoidOfTareaCollectionNewTarea != null && !oldSeccionCursoidOfTareaCollectionNewTarea.equals(seccionCursos)) {
                        oldSeccionCursoidOfTareaCollectionNewTarea.getTareaCollection().remove(tareaCollectionNewTarea);
                        oldSeccionCursoidOfTareaCollectionNewTarea = em.merge(oldSeccionCursoidOfTareaCollectionNewTarea);
                    }
                }
            }
            for (Asignados asignadosCollectionNewAsignados : asignadosCollectionNew) {
                if (!asignadosCollectionOld.contains(asignadosCollectionNewAsignados)) {
                    SeccionCursos oldSeccionCursoidOfAsignadosCollectionNewAsignados = asignadosCollectionNewAsignados.getSeccionCursoid();
                    asignadosCollectionNewAsignados.setSeccionCursoid(seccionCursos);
                    asignadosCollectionNewAsignados = em.merge(asignadosCollectionNewAsignados);
                    if (oldSeccionCursoidOfAsignadosCollectionNewAsignados != null && !oldSeccionCursoidOfAsignadosCollectionNewAsignados.equals(seccionCursos)) {
                        oldSeccionCursoidOfAsignadosCollectionNewAsignados.getAsignadosCollection().remove(asignadosCollectionNewAsignados);
                        oldSeccionCursoidOfAsignadosCollectionNewAsignados = em.merge(oldSeccionCursoidOfAsignadosCollectionNewAsignados);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = seccionCursos.getId();
                if (findSeccionCursos(id) == null) {
                    throw new NonexistentEntityException("The seccionCursos with id " + id + " no longer exists.");
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
            SeccionCursos seccionCursos;
            try {
                seccionCursos = em.getReference(SeccionCursos.class, id);
                seccionCursos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccionCursos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recurso> recursoCollectionOrphanCheck = seccionCursos.getRecursoCollection();
            for (Recurso recursoCollectionOrphanCheckRecurso : recursoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionCursos (" + seccionCursos + ") cannot be destroyed since the Recurso " + recursoCollectionOrphanCheckRecurso + " in its recursoCollection field has a non-nullable seccionCursoid field.");
            }
            Collection<Tarea> tareaCollectionOrphanCheck = seccionCursos.getTareaCollection();
            for (Tarea tareaCollectionOrphanCheckTarea : tareaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionCursos (" + seccionCursos + ") cannot be destroyed since the Tarea " + tareaCollectionOrphanCheckTarea + " in its tareaCollection field has a non-nullable seccionCursoid field.");
            }
            Collection<Asignados> asignadosCollectionOrphanCheck = seccionCursos.getAsignadosCollection();
            for (Asignados asignadosCollectionOrphanCheckAsignados : asignadosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionCursos (" + seccionCursos + ") cannot be destroyed since the Asignados " + asignadosCollectionOrphanCheckAsignados + " in its asignadosCollection field has a non-nullable seccionCursoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso cursoid = seccionCursos.getCursoid();
            if (cursoid != null) {
                cursoid.getSeccionCursosCollection().remove(seccionCursos);
                cursoid = em.merge(cursoid);
            }
            Seccion seccionid = seccionCursos.getSeccionid();
            if (seccionid != null) {
                seccionid.getSeccionCursosCollection().remove(seccionCursos);
                seccionid = em.merge(seccionid);
            }
            Usuario catedraticoid = seccionCursos.getCatedraticoid();
            if (catedraticoid != null) {
                catedraticoid.getSeccionCursosCollection().remove(seccionCursos);
                catedraticoid = em.merge(catedraticoid);
            }
            em.remove(seccionCursos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SeccionCursos> findSeccionCursosEntities() {
        return findSeccionCursosEntities(true, -1, -1);
    }

    public List<SeccionCursos> findSeccionCursosEntities(int maxResults, int firstResult) {
        return findSeccionCursosEntities(false, maxResults, firstResult);
    }

    private List<SeccionCursos> findSeccionCursosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SeccionCursos.class));
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

    public SeccionCursos findSeccionCursos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SeccionCursos.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCursosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SeccionCursos> rt = cq.from(SeccionCursos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

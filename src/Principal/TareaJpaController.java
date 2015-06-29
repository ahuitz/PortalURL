/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import Principal.exceptions.IllegalOrphanException;
import Principal.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.SeccionCursos;
import cursos.percistence.Entrega;
import cursos.percistence.Tarea;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TareaJpaController implements Serializable {

    public TareaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarea tarea) {
        if (tarea.getEntregaCollection() == null) {
            tarea.setEntregaCollection(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionCursos seccionCursoid = tarea.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid = em.getReference(seccionCursoid.getClass(), seccionCursoid.getId());
                tarea.setSeccionCursoid(seccionCursoid);
            }
            Collection<Entrega> attachedEntregaCollection = new ArrayList<Entrega>();
            for (Entrega entregaCollectionEntregaToAttach : tarea.getEntregaCollection()) {
                entregaCollectionEntregaToAttach = em.getReference(entregaCollectionEntregaToAttach.getClass(), entregaCollectionEntregaToAttach.getId());
                attachedEntregaCollection.add(entregaCollectionEntregaToAttach);
            }
            tarea.setEntregaCollection(attachedEntregaCollection);
            em.persist(tarea);
            if (seccionCursoid != null) {
                seccionCursoid.getTareaCollection().add(tarea);
                seccionCursoid = em.merge(seccionCursoid);
            }
            for (Entrega entregaCollectionEntrega : tarea.getEntregaCollection()) {
                Tarea oldTareaidOfEntregaCollectionEntrega = entregaCollectionEntrega.getTareaid();
                entregaCollectionEntrega.setTareaid(tarea);
                entregaCollectionEntrega = em.merge(entregaCollectionEntrega);
                if (oldTareaidOfEntregaCollectionEntrega != null) {
                    oldTareaidOfEntregaCollectionEntrega.getEntregaCollection().remove(entregaCollectionEntrega);
                    oldTareaidOfEntregaCollectionEntrega = em.merge(oldTareaidOfEntregaCollectionEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarea tarea) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarea persistentTarea = em.find(Tarea.class, tarea.getId());
            SeccionCursos seccionCursoidOld = persistentTarea.getSeccionCursoid();
            SeccionCursos seccionCursoidNew = tarea.getSeccionCursoid();
            Collection<Entrega> entregaCollectionOld = persistentTarea.getEntregaCollection();
            Collection<Entrega> entregaCollectionNew = tarea.getEntregaCollection();
            List<String> illegalOrphanMessages = null;
            for (Entrega entregaCollectionOldEntrega : entregaCollectionOld) {
                if (!entregaCollectionNew.contains(entregaCollectionOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaCollectionOldEntrega + " since its tareaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (seccionCursoidNew != null) {
                seccionCursoidNew = em.getReference(seccionCursoidNew.getClass(), seccionCursoidNew.getId());
                tarea.setSeccionCursoid(seccionCursoidNew);
            }
            Collection<Entrega> attachedEntregaCollectionNew = new ArrayList<Entrega>();
            for (Entrega entregaCollectionNewEntregaToAttach : entregaCollectionNew) {
                entregaCollectionNewEntregaToAttach = em.getReference(entregaCollectionNewEntregaToAttach.getClass(), entregaCollectionNewEntregaToAttach.getId());
                attachedEntregaCollectionNew.add(entregaCollectionNewEntregaToAttach);
            }
            entregaCollectionNew = attachedEntregaCollectionNew;
            tarea.setEntregaCollection(entregaCollectionNew);
            tarea = em.merge(tarea);
            if (seccionCursoidOld != null && !seccionCursoidOld.equals(seccionCursoidNew)) {
                seccionCursoidOld.getTareaCollection().remove(tarea);
                seccionCursoidOld = em.merge(seccionCursoidOld);
            }
            if (seccionCursoidNew != null && !seccionCursoidNew.equals(seccionCursoidOld)) {
                seccionCursoidNew.getTareaCollection().add(tarea);
                seccionCursoidNew = em.merge(seccionCursoidNew);
            }
            for (Entrega entregaCollectionNewEntrega : entregaCollectionNew) {
                if (!entregaCollectionOld.contains(entregaCollectionNewEntrega)) {
                    Tarea oldTareaidOfEntregaCollectionNewEntrega = entregaCollectionNewEntrega.getTareaid();
                    entregaCollectionNewEntrega.setTareaid(tarea);
                    entregaCollectionNewEntrega = em.merge(entregaCollectionNewEntrega);
                    if (oldTareaidOfEntregaCollectionNewEntrega != null && !oldTareaidOfEntregaCollectionNewEntrega.equals(tarea)) {
                        oldTareaidOfEntregaCollectionNewEntrega.getEntregaCollection().remove(entregaCollectionNewEntrega);
                        oldTareaidOfEntregaCollectionNewEntrega = em.merge(oldTareaidOfEntregaCollectionNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarea.getId();
                if (findTarea(id) == null) {
                    throw new NonexistentEntityException("The tarea with id " + id + " no longer exists.");
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
            Tarea tarea;
            try {
                tarea = em.getReference(Tarea.class, id);
                tarea.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarea with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Entrega> entregaCollectionOrphanCheck = tarea.getEntregaCollection();
            for (Entrega entregaCollectionOrphanCheckEntrega : entregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarea (" + tarea + ") cannot be destroyed since the Entrega " + entregaCollectionOrphanCheckEntrega + " in its entregaCollection field has a non-nullable tareaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SeccionCursos seccionCursoid = tarea.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid.getTareaCollection().remove(tarea);
                seccionCursoid = em.merge(seccionCursoid);
            }
            em.remove(tarea);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarea> findTareaEntities() {
        return findTareaEntities(true, -1, -1);
    }

    public List<Tarea> findTareaEntities(int maxResults, int firstResult) {
        return findTareaEntities(false, maxResults, firstResult);
    }

    private List<Tarea> findTareaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarea.class));
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

    public Tarea findTarea(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarea.class, id);
        } finally {
            em.close();
        }
    }

    public int getTareaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarea> rt = cq.from(Tarea.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;

import cursos.controladores.exceptions.IllegalOrphanException;
import cursos.controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.Tarea;
import cursos.percistence.Usuario;
import cursos.percistence.DetalleEntrega;
import cursos.percistence.Entrega;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablito Garzona
 */
public class EntregaJpaController implements Serializable {

    public EntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrega entrega) {
        if (entrega.getDetalleEntregaCollection() == null) {
            entrega.setDetalleEntregaCollection(new ArrayList<DetalleEntrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarea tareaid = entrega.getTareaid();
            if (tareaid != null) {
                tareaid = em.getReference(tareaid.getClass(), tareaid.getId());
                entrega.setTareaid(tareaid);
            }
            Usuario usuarioid = entrega.getUsuarioid();
            if (usuarioid != null) {
                usuarioid = em.getReference(usuarioid.getClass(), usuarioid.getId());
                entrega.setUsuarioid(usuarioid);
            }
            Collection<DetalleEntrega> attachedDetalleEntregaCollection = new ArrayList<DetalleEntrega>();
            for (DetalleEntrega detalleEntregaCollectionDetalleEntregaToAttach : entrega.getDetalleEntregaCollection()) {
                detalleEntregaCollectionDetalleEntregaToAttach = em.getReference(detalleEntregaCollectionDetalleEntregaToAttach.getClass(), detalleEntregaCollectionDetalleEntregaToAttach.getIdt());
                attachedDetalleEntregaCollection.add(detalleEntregaCollectionDetalleEntregaToAttach);
            }
            entrega.setDetalleEntregaCollection(attachedDetalleEntregaCollection);
            em.persist(entrega);
            if (tareaid != null) {
                tareaid.getEntregaCollection().add(entrega);
                tareaid = em.merge(tareaid);
            }
            if (usuarioid != null) {
                usuarioid.getEntregaCollection().add(entrega);
                usuarioid = em.merge(usuarioid);
            }
            for (DetalleEntrega detalleEntregaCollectionDetalleEntrega : entrega.getDetalleEntregaCollection()) {
                Entrega oldEntregaidOfDetalleEntregaCollectionDetalleEntrega = detalleEntregaCollectionDetalleEntrega.getEntregaid();
                detalleEntregaCollectionDetalleEntrega.setEntregaid(entrega);
                detalleEntregaCollectionDetalleEntrega = em.merge(detalleEntregaCollectionDetalleEntrega);
                if (oldEntregaidOfDetalleEntregaCollectionDetalleEntrega != null) {
                    oldEntregaidOfDetalleEntregaCollectionDetalleEntrega.getDetalleEntregaCollection().remove(detalleEntregaCollectionDetalleEntrega);
                    oldEntregaidOfDetalleEntregaCollectionDetalleEntrega = em.merge(oldEntregaidOfDetalleEntregaCollectionDetalleEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrega entrega) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega persistentEntrega = em.find(Entrega.class, entrega.getId());
            Tarea tareaidOld = persistentEntrega.getTareaid();
            Tarea tareaidNew = entrega.getTareaid();
            Usuario usuarioidOld = persistentEntrega.getUsuarioid();
            Usuario usuarioidNew = entrega.getUsuarioid();
            Collection<DetalleEntrega> detalleEntregaCollectionOld = persistentEntrega.getDetalleEntregaCollection();
            Collection<DetalleEntrega> detalleEntregaCollectionNew = entrega.getDetalleEntregaCollection();
            List<String> illegalOrphanMessages = null;
            for (DetalleEntrega detalleEntregaCollectionOldDetalleEntrega : detalleEntregaCollectionOld) {
                if (!detalleEntregaCollectionNew.contains(detalleEntregaCollectionOldDetalleEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleEntrega " + detalleEntregaCollectionOldDetalleEntrega + " since its entregaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tareaidNew != null) {
                tareaidNew = em.getReference(tareaidNew.getClass(), tareaidNew.getId());
                entrega.setTareaid(tareaidNew);
            }
            if (usuarioidNew != null) {
                usuarioidNew = em.getReference(usuarioidNew.getClass(), usuarioidNew.getId());
                entrega.setUsuarioid(usuarioidNew);
            }
            Collection<DetalleEntrega> attachedDetalleEntregaCollectionNew = new ArrayList<DetalleEntrega>();
            for (DetalleEntrega detalleEntregaCollectionNewDetalleEntregaToAttach : detalleEntregaCollectionNew) {
                detalleEntregaCollectionNewDetalleEntregaToAttach = em.getReference(detalleEntregaCollectionNewDetalleEntregaToAttach.getClass(), detalleEntregaCollectionNewDetalleEntregaToAttach.getIdt());
                attachedDetalleEntregaCollectionNew.add(detalleEntregaCollectionNewDetalleEntregaToAttach);
            }
            detalleEntregaCollectionNew = attachedDetalleEntregaCollectionNew;
            entrega.setDetalleEntregaCollection(detalleEntregaCollectionNew);
            entrega = em.merge(entrega);
            if (tareaidOld != null && !tareaidOld.equals(tareaidNew)) {
                tareaidOld.getEntregaCollection().remove(entrega);
                tareaidOld = em.merge(tareaidOld);
            }
            if (tareaidNew != null && !tareaidNew.equals(tareaidOld)) {
                tareaidNew.getEntregaCollection().add(entrega);
                tareaidNew = em.merge(tareaidNew);
            }
            if (usuarioidOld != null && !usuarioidOld.equals(usuarioidNew)) {
                usuarioidOld.getEntregaCollection().remove(entrega);
                usuarioidOld = em.merge(usuarioidOld);
            }
            if (usuarioidNew != null && !usuarioidNew.equals(usuarioidOld)) {
                usuarioidNew.getEntregaCollection().add(entrega);
                usuarioidNew = em.merge(usuarioidNew);
            }
            for (DetalleEntrega detalleEntregaCollectionNewDetalleEntrega : detalleEntregaCollectionNew) {
                if (!detalleEntregaCollectionOld.contains(detalleEntregaCollectionNewDetalleEntrega)) {
                    Entrega oldEntregaidOfDetalleEntregaCollectionNewDetalleEntrega = detalleEntregaCollectionNewDetalleEntrega.getEntregaid();
                    detalleEntregaCollectionNewDetalleEntrega.setEntregaid(entrega);
                    detalleEntregaCollectionNewDetalleEntrega = em.merge(detalleEntregaCollectionNewDetalleEntrega);
                    if (oldEntregaidOfDetalleEntregaCollectionNewDetalleEntrega != null && !oldEntregaidOfDetalleEntregaCollectionNewDetalleEntrega.equals(entrega)) {
                        oldEntregaidOfDetalleEntregaCollectionNewDetalleEntrega.getDetalleEntregaCollection().remove(detalleEntregaCollectionNewDetalleEntrega);
                        oldEntregaidOfDetalleEntregaCollectionNewDetalleEntrega = em.merge(oldEntregaidOfDetalleEntregaCollectionNewDetalleEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entrega.getId();
                if (findEntrega(id) == null) {
                    throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.");
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
            Entrega entrega;
            try {
                entrega = em.getReference(Entrega.class, id);
                entrega.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DetalleEntrega> detalleEntregaCollectionOrphanCheck = entrega.getDetalleEntregaCollection();
            for (DetalleEntrega detalleEntregaCollectionOrphanCheckDetalleEntrega : detalleEntregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entrega (" + entrega + ") cannot be destroyed since the DetalleEntrega " + detalleEntregaCollectionOrphanCheckDetalleEntrega + " in its detalleEntregaCollection field has a non-nullable entregaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tarea tareaid = entrega.getTareaid();
            if (tareaid != null) {
                tareaid.getEntregaCollection().remove(entrega);
                tareaid = em.merge(tareaid);
            }
            Usuario usuarioid = entrega.getUsuarioid();
            if (usuarioid != null) {
                usuarioid.getEntregaCollection().remove(entrega);
                usuarioid = em.merge(usuarioid);
            }
            em.remove(entrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrega> findEntregaEntities() {
        return findEntregaEntities(true, -1, -1);
    }

    public List<Entrega> findEntregaEntities(int maxResults, int firstResult) {
        return findEntregaEntities(false, maxResults, firstResult);
    }

    private List<Entrega> findEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrega.class));
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

    public Entrega findEntrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrega> rt = cq.from(Entrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

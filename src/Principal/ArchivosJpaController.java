/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import Principal.exceptions.IllegalOrphanException;
import Principal.exceptions.NonexistentEntityException;
import cursos.percistence.Archivos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.DetalleEntrega;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class ArchivosJpaController implements Serializable {

    public ArchivosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Archivos archivos) {
        if (archivos.getDetalleEntregaCollection() == null) {
            archivos.setDetalleEntregaCollection(new ArrayList<DetalleEntrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DetalleEntrega> attachedDetalleEntregaCollection = new ArrayList<DetalleEntrega>();
            for (DetalleEntrega detalleEntregaCollectionDetalleEntregaToAttach : archivos.getDetalleEntregaCollection()) {
                detalleEntregaCollectionDetalleEntregaToAttach = em.getReference(detalleEntregaCollectionDetalleEntregaToAttach.getClass(), detalleEntregaCollectionDetalleEntregaToAttach.getIdt());
                attachedDetalleEntregaCollection.add(detalleEntregaCollectionDetalleEntregaToAttach);
            }
            archivos.setDetalleEntregaCollection(attachedDetalleEntregaCollection);
            em.persist(archivos);
            for (DetalleEntrega detalleEntregaCollectionDetalleEntrega : archivos.getDetalleEntregaCollection()) {
                Archivos oldArchivosidOfDetalleEntregaCollectionDetalleEntrega = detalleEntregaCollectionDetalleEntrega.getArchivosid();
                detalleEntregaCollectionDetalleEntrega.setArchivosid(archivos);
                detalleEntregaCollectionDetalleEntrega = em.merge(detalleEntregaCollectionDetalleEntrega);
                if (oldArchivosidOfDetalleEntregaCollectionDetalleEntrega != null) {
                    oldArchivosidOfDetalleEntregaCollectionDetalleEntrega.getDetalleEntregaCollection().remove(detalleEntregaCollectionDetalleEntrega);
                    oldArchivosidOfDetalleEntregaCollectionDetalleEntrega = em.merge(oldArchivosidOfDetalleEntregaCollectionDetalleEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Archivos archivos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivos persistentArchivos = em.find(Archivos.class, archivos.getId());
            Collection<DetalleEntrega> detalleEntregaCollectionOld = persistentArchivos.getDetalleEntregaCollection();
            Collection<DetalleEntrega> detalleEntregaCollectionNew = archivos.getDetalleEntregaCollection();
            List<String> illegalOrphanMessages = null;
            for (DetalleEntrega detalleEntregaCollectionOldDetalleEntrega : detalleEntregaCollectionOld) {
                if (!detalleEntregaCollectionNew.contains(detalleEntregaCollectionOldDetalleEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleEntrega " + detalleEntregaCollectionOldDetalleEntrega + " since its archivosid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DetalleEntrega> attachedDetalleEntregaCollectionNew = new ArrayList<DetalleEntrega>();
            for (DetalleEntrega detalleEntregaCollectionNewDetalleEntregaToAttach : detalleEntregaCollectionNew) {
                detalleEntregaCollectionNewDetalleEntregaToAttach = em.getReference(detalleEntregaCollectionNewDetalleEntregaToAttach.getClass(), detalleEntregaCollectionNewDetalleEntregaToAttach.getIdt());
                attachedDetalleEntregaCollectionNew.add(detalleEntregaCollectionNewDetalleEntregaToAttach);
            }
            detalleEntregaCollectionNew = attachedDetalleEntregaCollectionNew;
            archivos.setDetalleEntregaCollection(detalleEntregaCollectionNew);
            archivos = em.merge(archivos);
            for (DetalleEntrega detalleEntregaCollectionNewDetalleEntrega : detalleEntregaCollectionNew) {
                if (!detalleEntregaCollectionOld.contains(detalleEntregaCollectionNewDetalleEntrega)) {
                    Archivos oldArchivosidOfDetalleEntregaCollectionNewDetalleEntrega = detalleEntregaCollectionNewDetalleEntrega.getArchivosid();
                    detalleEntregaCollectionNewDetalleEntrega.setArchivosid(archivos);
                    detalleEntregaCollectionNewDetalleEntrega = em.merge(detalleEntregaCollectionNewDetalleEntrega);
                    if (oldArchivosidOfDetalleEntregaCollectionNewDetalleEntrega != null && !oldArchivosidOfDetalleEntregaCollectionNewDetalleEntrega.equals(archivos)) {
                        oldArchivosidOfDetalleEntregaCollectionNewDetalleEntrega.getDetalleEntregaCollection().remove(detalleEntregaCollectionNewDetalleEntrega);
                        oldArchivosidOfDetalleEntregaCollectionNewDetalleEntrega = em.merge(oldArchivosidOfDetalleEntregaCollectionNewDetalleEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = archivos.getId();
                if (findArchivos(id) == null) {
                    throw new NonexistentEntityException("The archivos with id " + id + " no longer exists.");
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
            Archivos archivos;
            try {
                archivos = em.getReference(Archivos.class, id);
                archivos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The archivos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DetalleEntrega> detalleEntregaCollectionOrphanCheck = archivos.getDetalleEntregaCollection();
            for (DetalleEntrega detalleEntregaCollectionOrphanCheckDetalleEntrega : detalleEntregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Archivos (" + archivos + ") cannot be destroyed since the DetalleEntrega " + detalleEntregaCollectionOrphanCheckDetalleEntrega + " in its detalleEntregaCollection field has a non-nullable archivosid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(archivos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Archivos> findArchivosEntities() {
        return findArchivosEntities(true, -1, -1);
    }

    public List<Archivos> findArchivosEntities(int maxResults, int firstResult) {
        return findArchivosEntities(false, maxResults, firstResult);
    }

    private List<Archivos> findArchivosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Archivos.class));
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

    public Archivos findArchivos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Archivos.class, id);
        } finally {
            em.close();
        }
    }

    public int getArchivosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Archivos> rt = cq.from(Archivos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

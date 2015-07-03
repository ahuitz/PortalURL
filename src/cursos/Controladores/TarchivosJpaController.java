/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.IllegalOrphanException;
import cursos.Controladores.exceptions.NonexistentEntityException;
import cursos.persistence.Tarchivos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.Trecurso;
import java.util.ArrayList;
import java.util.List;
import cursos.persistence.TdetalleEntrega;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TarchivosJpaController implements Serializable {

    public TarchivosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarchivos tarchivos) {
        if (tarchivos.getTrecursoList() == null) {
            tarchivos.setTrecursoList(new ArrayList<Trecurso>());
        }
        if (tarchivos.getTdetalleEntregaList() == null) {
            tarchivos.setTdetalleEntregaList(new ArrayList<TdetalleEntrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Trecurso> attachedTrecursoList = new ArrayList<Trecurso>();
            for (Trecurso trecursoListTrecursoToAttach : tarchivos.getTrecursoList()) {
                trecursoListTrecursoToAttach = em.getReference(trecursoListTrecursoToAttach.getClass(), trecursoListTrecursoToAttach.getId());
                attachedTrecursoList.add(trecursoListTrecursoToAttach);
            }
            tarchivos.setTrecursoList(attachedTrecursoList);
            List<TdetalleEntrega> attachedTdetalleEntregaList = new ArrayList<TdetalleEntrega>();
            for (TdetalleEntrega tdetalleEntregaListTdetalleEntregaToAttach : tarchivos.getTdetalleEntregaList()) {
                tdetalleEntregaListTdetalleEntregaToAttach = em.getReference(tdetalleEntregaListTdetalleEntregaToAttach.getClass(), tdetalleEntregaListTdetalleEntregaToAttach.getIdt());
                attachedTdetalleEntregaList.add(tdetalleEntregaListTdetalleEntregaToAttach);
            }
            tarchivos.setTdetalleEntregaList(attachedTdetalleEntregaList);
            em.persist(tarchivos);
            for (Trecurso trecursoListTrecurso : tarchivos.getTrecursoList()) {
                Tarchivos oldArchivosidOfTrecursoListTrecurso = trecursoListTrecurso.getArchivosid();
                trecursoListTrecurso.setArchivosid(tarchivos);
                trecursoListTrecurso = em.merge(trecursoListTrecurso);
                if (oldArchivosidOfTrecursoListTrecurso != null) {
                    oldArchivosidOfTrecursoListTrecurso.getTrecursoList().remove(trecursoListTrecurso);
                    oldArchivosidOfTrecursoListTrecurso = em.merge(oldArchivosidOfTrecursoListTrecurso);
                }
            }
            for (TdetalleEntrega tdetalleEntregaListTdetalleEntrega : tarchivos.getTdetalleEntregaList()) {
                Tarchivos oldArchivosidOfTdetalleEntregaListTdetalleEntrega = tdetalleEntregaListTdetalleEntrega.getArchivosid();
                tdetalleEntregaListTdetalleEntrega.setArchivosid(tarchivos);
                tdetalleEntregaListTdetalleEntrega = em.merge(tdetalleEntregaListTdetalleEntrega);
                if (oldArchivosidOfTdetalleEntregaListTdetalleEntrega != null) {
                    oldArchivosidOfTdetalleEntregaListTdetalleEntrega.getTdetalleEntregaList().remove(tdetalleEntregaListTdetalleEntrega);
                    oldArchivosidOfTdetalleEntregaListTdetalleEntrega = em.merge(oldArchivosidOfTdetalleEntregaListTdetalleEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarchivos tarchivos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarchivos persistentTarchivos = em.find(Tarchivos.class, tarchivos.getId());
            List<Trecurso> trecursoListOld = persistentTarchivos.getTrecursoList();
            List<Trecurso> trecursoListNew = tarchivos.getTrecursoList();
            List<TdetalleEntrega> tdetalleEntregaListOld = persistentTarchivos.getTdetalleEntregaList();
            List<TdetalleEntrega> tdetalleEntregaListNew = tarchivos.getTdetalleEntregaList();
            List<String> illegalOrphanMessages = null;
            for (Trecurso trecursoListOldTrecurso : trecursoListOld) {
                if (!trecursoListNew.contains(trecursoListOldTrecurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trecurso " + trecursoListOldTrecurso + " since its archivosid field is not nullable.");
                }
            }
            for (TdetalleEntrega tdetalleEntregaListOldTdetalleEntrega : tdetalleEntregaListOld) {
                if (!tdetalleEntregaListNew.contains(tdetalleEntregaListOldTdetalleEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TdetalleEntrega " + tdetalleEntregaListOldTdetalleEntrega + " since its archivosid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Trecurso> attachedTrecursoListNew = new ArrayList<Trecurso>();
            for (Trecurso trecursoListNewTrecursoToAttach : trecursoListNew) {
                trecursoListNewTrecursoToAttach = em.getReference(trecursoListNewTrecursoToAttach.getClass(), trecursoListNewTrecursoToAttach.getId());
                attachedTrecursoListNew.add(trecursoListNewTrecursoToAttach);
            }
            trecursoListNew = attachedTrecursoListNew;
            tarchivos.setTrecursoList(trecursoListNew);
            List<TdetalleEntrega> attachedTdetalleEntregaListNew = new ArrayList<TdetalleEntrega>();
            for (TdetalleEntrega tdetalleEntregaListNewTdetalleEntregaToAttach : tdetalleEntregaListNew) {
                tdetalleEntregaListNewTdetalleEntregaToAttach = em.getReference(tdetalleEntregaListNewTdetalleEntregaToAttach.getClass(), tdetalleEntregaListNewTdetalleEntregaToAttach.getIdt());
                attachedTdetalleEntregaListNew.add(tdetalleEntregaListNewTdetalleEntregaToAttach);
            }
            tdetalleEntregaListNew = attachedTdetalleEntregaListNew;
            tarchivos.setTdetalleEntregaList(tdetalleEntregaListNew);
            tarchivos = em.merge(tarchivos);
            for (Trecurso trecursoListNewTrecurso : trecursoListNew) {
                if (!trecursoListOld.contains(trecursoListNewTrecurso)) {
                    Tarchivos oldArchivosidOfTrecursoListNewTrecurso = trecursoListNewTrecurso.getArchivosid();
                    trecursoListNewTrecurso.setArchivosid(tarchivos);
                    trecursoListNewTrecurso = em.merge(trecursoListNewTrecurso);
                    if (oldArchivosidOfTrecursoListNewTrecurso != null && !oldArchivosidOfTrecursoListNewTrecurso.equals(tarchivos)) {
                        oldArchivosidOfTrecursoListNewTrecurso.getTrecursoList().remove(trecursoListNewTrecurso);
                        oldArchivosidOfTrecursoListNewTrecurso = em.merge(oldArchivosidOfTrecursoListNewTrecurso);
                    }
                }
            }
            for (TdetalleEntrega tdetalleEntregaListNewTdetalleEntrega : tdetalleEntregaListNew) {
                if (!tdetalleEntregaListOld.contains(tdetalleEntregaListNewTdetalleEntrega)) {
                    Tarchivos oldArchivosidOfTdetalleEntregaListNewTdetalleEntrega = tdetalleEntregaListNewTdetalleEntrega.getArchivosid();
                    tdetalleEntregaListNewTdetalleEntrega.setArchivosid(tarchivos);
                    tdetalleEntregaListNewTdetalleEntrega = em.merge(tdetalleEntregaListNewTdetalleEntrega);
                    if (oldArchivosidOfTdetalleEntregaListNewTdetalleEntrega != null && !oldArchivosidOfTdetalleEntregaListNewTdetalleEntrega.equals(tarchivos)) {
                        oldArchivosidOfTdetalleEntregaListNewTdetalleEntrega.getTdetalleEntregaList().remove(tdetalleEntregaListNewTdetalleEntrega);
                        oldArchivosidOfTdetalleEntregaListNewTdetalleEntrega = em.merge(oldArchivosidOfTdetalleEntregaListNewTdetalleEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarchivos.getId();
                if (findTarchivos(id) == null) {
                    throw new NonexistentEntityException("The tarchivos with id " + id + " no longer exists.");
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
            Tarchivos tarchivos;
            try {
                tarchivos = em.getReference(Tarchivos.class, id);
                tarchivos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarchivos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Trecurso> trecursoListOrphanCheck = tarchivos.getTrecursoList();
            for (Trecurso trecursoListOrphanCheckTrecurso : trecursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarchivos (" + tarchivos + ") cannot be destroyed since the Trecurso " + trecursoListOrphanCheckTrecurso + " in its trecursoList field has a non-nullable archivosid field.");
            }
            List<TdetalleEntrega> tdetalleEntregaListOrphanCheck = tarchivos.getTdetalleEntregaList();
            for (TdetalleEntrega tdetalleEntregaListOrphanCheckTdetalleEntrega : tdetalleEntregaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarchivos (" + tarchivos + ") cannot be destroyed since the TdetalleEntrega " + tdetalleEntregaListOrphanCheckTdetalleEntrega + " in its tdetalleEntregaList field has a non-nullable archivosid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarchivos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarchivos> findTarchivosEntities() {
        return findTarchivosEntities(true, -1, -1);
    }

    public List<Tarchivos> findTarchivosEntities(int maxResults, int firstResult) {
        return findTarchivosEntities(false, maxResults, firstResult);
    }

    private List<Tarchivos> findTarchivosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarchivos.class));
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

    public Tarchivos findTarchivos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarchivos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarchivosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarchivos> rt = cq.from(Tarchivos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

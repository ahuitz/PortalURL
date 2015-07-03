/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.IllegalOrphanException;
import cursos.Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.Tactividad;
import cursos.persistence.Tusuario;
import cursos.persistence.TdetalleEntrega;
import cursos.persistence.Tentrega;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TentregaJpaController implements Serializable {

    public TentregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tentrega tentrega) {
        if (tentrega.getTdetalleEntregaList() == null) {
            tentrega.setTdetalleEntregaList(new ArrayList<TdetalleEntrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tactividad actividadid = tentrega.getActividadid();
            if (actividadid != null) {
                actividadid = em.getReference(actividadid.getClass(), actividadid.getId());
                tentrega.setActividadid(actividadid);
            }
            Tusuario usuarioid = tentrega.getUsuarioid();
            if (usuarioid != null) {
                usuarioid = em.getReference(usuarioid.getClass(), usuarioid.getId());
                tentrega.setUsuarioid(usuarioid);
            }
            List<TdetalleEntrega> attachedTdetalleEntregaList = new ArrayList<TdetalleEntrega>();
            for (TdetalleEntrega tdetalleEntregaListTdetalleEntregaToAttach : tentrega.getTdetalleEntregaList()) {
                tdetalleEntregaListTdetalleEntregaToAttach = em.getReference(tdetalleEntregaListTdetalleEntregaToAttach.getClass(), tdetalleEntregaListTdetalleEntregaToAttach.getIdt());
                attachedTdetalleEntregaList.add(tdetalleEntregaListTdetalleEntregaToAttach);
            }
            tentrega.setTdetalleEntregaList(attachedTdetalleEntregaList);
            em.persist(tentrega);
            if (actividadid != null) {
                actividadid.getTentregaList().add(tentrega);
                actividadid = em.merge(actividadid);
            }
            if (usuarioid != null) {
                usuarioid.getTentregaList().add(tentrega);
                usuarioid = em.merge(usuarioid);
            }
            for (TdetalleEntrega tdetalleEntregaListTdetalleEntrega : tentrega.getTdetalleEntregaList()) {
                Tentrega oldEntregaidOfTdetalleEntregaListTdetalleEntrega = tdetalleEntregaListTdetalleEntrega.getEntregaid();
                tdetalleEntregaListTdetalleEntrega.setEntregaid(tentrega);
                tdetalleEntregaListTdetalleEntrega = em.merge(tdetalleEntregaListTdetalleEntrega);
                if (oldEntregaidOfTdetalleEntregaListTdetalleEntrega != null) {
                    oldEntregaidOfTdetalleEntregaListTdetalleEntrega.getTdetalleEntregaList().remove(tdetalleEntregaListTdetalleEntrega);
                    oldEntregaidOfTdetalleEntregaListTdetalleEntrega = em.merge(oldEntregaidOfTdetalleEntregaListTdetalleEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tentrega tentrega) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tentrega persistentTentrega = em.find(Tentrega.class, tentrega.getId());
            Tactividad actividadidOld = persistentTentrega.getActividadid();
            Tactividad actividadidNew = tentrega.getActividadid();
            Tusuario usuarioidOld = persistentTentrega.getUsuarioid();
            Tusuario usuarioidNew = tentrega.getUsuarioid();
            List<TdetalleEntrega> tdetalleEntregaListOld = persistentTentrega.getTdetalleEntregaList();
            List<TdetalleEntrega> tdetalleEntregaListNew = tentrega.getTdetalleEntregaList();
            List<String> illegalOrphanMessages = null;
            for (TdetalleEntrega tdetalleEntregaListOldTdetalleEntrega : tdetalleEntregaListOld) {
                if (!tdetalleEntregaListNew.contains(tdetalleEntregaListOldTdetalleEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TdetalleEntrega " + tdetalleEntregaListOldTdetalleEntrega + " since its entregaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (actividadidNew != null) {
                actividadidNew = em.getReference(actividadidNew.getClass(), actividadidNew.getId());
                tentrega.setActividadid(actividadidNew);
            }
            if (usuarioidNew != null) {
                usuarioidNew = em.getReference(usuarioidNew.getClass(), usuarioidNew.getId());
                tentrega.setUsuarioid(usuarioidNew);
            }
            List<TdetalleEntrega> attachedTdetalleEntregaListNew = new ArrayList<TdetalleEntrega>();
            for (TdetalleEntrega tdetalleEntregaListNewTdetalleEntregaToAttach : tdetalleEntregaListNew) {
                tdetalleEntregaListNewTdetalleEntregaToAttach = em.getReference(tdetalleEntregaListNewTdetalleEntregaToAttach.getClass(), tdetalleEntregaListNewTdetalleEntregaToAttach.getIdt());
                attachedTdetalleEntregaListNew.add(tdetalleEntregaListNewTdetalleEntregaToAttach);
            }
            tdetalleEntregaListNew = attachedTdetalleEntregaListNew;
            tentrega.setTdetalleEntregaList(tdetalleEntregaListNew);
            tentrega = em.merge(tentrega);
            if (actividadidOld != null && !actividadidOld.equals(actividadidNew)) {
                actividadidOld.getTentregaList().remove(tentrega);
                actividadidOld = em.merge(actividadidOld);
            }
            if (actividadidNew != null && !actividadidNew.equals(actividadidOld)) {
                actividadidNew.getTentregaList().add(tentrega);
                actividadidNew = em.merge(actividadidNew);
            }
            if (usuarioidOld != null && !usuarioidOld.equals(usuarioidNew)) {
                usuarioidOld.getTentregaList().remove(tentrega);
                usuarioidOld = em.merge(usuarioidOld);
            }
            if (usuarioidNew != null && !usuarioidNew.equals(usuarioidOld)) {
                usuarioidNew.getTentregaList().add(tentrega);
                usuarioidNew = em.merge(usuarioidNew);
            }
            for (TdetalleEntrega tdetalleEntregaListNewTdetalleEntrega : tdetalleEntregaListNew) {
                if (!tdetalleEntregaListOld.contains(tdetalleEntregaListNewTdetalleEntrega)) {
                    Tentrega oldEntregaidOfTdetalleEntregaListNewTdetalleEntrega = tdetalleEntregaListNewTdetalleEntrega.getEntregaid();
                    tdetalleEntregaListNewTdetalleEntrega.setEntregaid(tentrega);
                    tdetalleEntregaListNewTdetalleEntrega = em.merge(tdetalleEntregaListNewTdetalleEntrega);
                    if (oldEntregaidOfTdetalleEntregaListNewTdetalleEntrega != null && !oldEntregaidOfTdetalleEntregaListNewTdetalleEntrega.equals(tentrega)) {
                        oldEntregaidOfTdetalleEntregaListNewTdetalleEntrega.getTdetalleEntregaList().remove(tdetalleEntregaListNewTdetalleEntrega);
                        oldEntregaidOfTdetalleEntregaListNewTdetalleEntrega = em.merge(oldEntregaidOfTdetalleEntregaListNewTdetalleEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tentrega.getId();
                if (findTentrega(id) == null) {
                    throw new NonexistentEntityException("The tentrega with id " + id + " no longer exists.");
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
            Tentrega tentrega;
            try {
                tentrega = em.getReference(Tentrega.class, id);
                tentrega.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tentrega with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TdetalleEntrega> tdetalleEntregaListOrphanCheck = tentrega.getTdetalleEntregaList();
            for (TdetalleEntrega tdetalleEntregaListOrphanCheckTdetalleEntrega : tdetalleEntregaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tentrega (" + tentrega + ") cannot be destroyed since the TdetalleEntrega " + tdetalleEntregaListOrphanCheckTdetalleEntrega + " in its tdetalleEntregaList field has a non-nullable entregaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tactividad actividadid = tentrega.getActividadid();
            if (actividadid != null) {
                actividadid.getTentregaList().remove(tentrega);
                actividadid = em.merge(actividadid);
            }
            Tusuario usuarioid = tentrega.getUsuarioid();
            if (usuarioid != null) {
                usuarioid.getTentregaList().remove(tentrega);
                usuarioid = em.merge(usuarioid);
            }
            em.remove(tentrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tentrega> findTentregaEntities() {
        return findTentregaEntities(true, -1, -1);
    }

    public List<Tentrega> findTentregaEntities(int maxResults, int firstResult) {
        return findTentregaEntities(false, maxResults, firstResult);
    }

    private List<Tentrega> findTentregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tentrega.class));
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

    public Tentrega findTentrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tentrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getTentregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tentrega> rt = cq.from(Tentrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.IllegalOrphanException;
import cursos.Controladores.exceptions.NonexistentEntityException;
import cursos.persistence.Tciclo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.TcursoCiclo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TcicloJpaController implements Serializable {

    public TcicloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tciclo tciclo) {
        if (tciclo.getTcursoCicloList() == null) {
            tciclo.setTcursoCicloList(new ArrayList<TcursoCiclo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TcursoCiclo> attachedTcursoCicloList = new ArrayList<TcursoCiclo>();
            for (TcursoCiclo tcursoCicloListTcursoCicloToAttach : tciclo.getTcursoCicloList()) {
                tcursoCicloListTcursoCicloToAttach = em.getReference(tcursoCicloListTcursoCicloToAttach.getClass(), tcursoCicloListTcursoCicloToAttach.getId());
                attachedTcursoCicloList.add(tcursoCicloListTcursoCicloToAttach);
            }
            tciclo.setTcursoCicloList(attachedTcursoCicloList);
            em.persist(tciclo);
            for (TcursoCiclo tcursoCicloListTcursoCiclo : tciclo.getTcursoCicloList()) {
                Tciclo oldCicloidOfTcursoCicloListTcursoCiclo = tcursoCicloListTcursoCiclo.getCicloid();
                tcursoCicloListTcursoCiclo.setCicloid(tciclo);
                tcursoCicloListTcursoCiclo = em.merge(tcursoCicloListTcursoCiclo);
                if (oldCicloidOfTcursoCicloListTcursoCiclo != null) {
                    oldCicloidOfTcursoCicloListTcursoCiclo.getTcursoCicloList().remove(tcursoCicloListTcursoCiclo);
                    oldCicloidOfTcursoCicloListTcursoCiclo = em.merge(oldCicloidOfTcursoCicloListTcursoCiclo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tciclo tciclo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tciclo persistentTciclo = em.find(Tciclo.class, tciclo.getId());
            List<TcursoCiclo> tcursoCicloListOld = persistentTciclo.getTcursoCicloList();
            List<TcursoCiclo> tcursoCicloListNew = tciclo.getTcursoCicloList();
            List<String> illegalOrphanMessages = null;
            for (TcursoCiclo tcursoCicloListOldTcursoCiclo : tcursoCicloListOld) {
                if (!tcursoCicloListNew.contains(tcursoCicloListOldTcursoCiclo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TcursoCiclo " + tcursoCicloListOldTcursoCiclo + " since its cicloid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TcursoCiclo> attachedTcursoCicloListNew = new ArrayList<TcursoCiclo>();
            for (TcursoCiclo tcursoCicloListNewTcursoCicloToAttach : tcursoCicloListNew) {
                tcursoCicloListNewTcursoCicloToAttach = em.getReference(tcursoCicloListNewTcursoCicloToAttach.getClass(), tcursoCicloListNewTcursoCicloToAttach.getId());
                attachedTcursoCicloListNew.add(tcursoCicloListNewTcursoCicloToAttach);
            }
            tcursoCicloListNew = attachedTcursoCicloListNew;
            tciclo.setTcursoCicloList(tcursoCicloListNew);
            tciclo = em.merge(tciclo);
            for (TcursoCiclo tcursoCicloListNewTcursoCiclo : tcursoCicloListNew) {
                if (!tcursoCicloListOld.contains(tcursoCicloListNewTcursoCiclo)) {
                    Tciclo oldCicloidOfTcursoCicloListNewTcursoCiclo = tcursoCicloListNewTcursoCiclo.getCicloid();
                    tcursoCicloListNewTcursoCiclo.setCicloid(tciclo);
                    tcursoCicloListNewTcursoCiclo = em.merge(tcursoCicloListNewTcursoCiclo);
                    if (oldCicloidOfTcursoCicloListNewTcursoCiclo != null && !oldCicloidOfTcursoCicloListNewTcursoCiclo.equals(tciclo)) {
                        oldCicloidOfTcursoCicloListNewTcursoCiclo.getTcursoCicloList().remove(tcursoCicloListNewTcursoCiclo);
                        oldCicloidOfTcursoCicloListNewTcursoCiclo = em.merge(oldCicloidOfTcursoCicloListNewTcursoCiclo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tciclo.getId();
                if (findTciclo(id) == null) {
                    throw new NonexistentEntityException("The tciclo with id " + id + " no longer exists.");
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
            Tciclo tciclo;
            try {
                tciclo = em.getReference(Tciclo.class, id);
                tciclo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tciclo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TcursoCiclo> tcursoCicloListOrphanCheck = tciclo.getTcursoCicloList();
            for (TcursoCiclo tcursoCicloListOrphanCheckTcursoCiclo : tcursoCicloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tciclo (" + tciclo + ") cannot be destroyed since the TcursoCiclo " + tcursoCicloListOrphanCheckTcursoCiclo + " in its tcursoCicloList field has a non-nullable cicloid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tciclo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tciclo> findTcicloEntities() {
        return findTcicloEntities(true, -1, -1);
    }

    public List<Tciclo> findTcicloEntities(int maxResults, int firstResult) {
        return findTcicloEntities(false, maxResults, firstResult);
    }

    private List<Tciclo> findTcicloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tciclo.class));
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

    public Tciclo findTciclo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tciclo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTcicloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tciclo> rt = cq.from(Tciclo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

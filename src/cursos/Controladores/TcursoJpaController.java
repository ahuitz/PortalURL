/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.IllegalOrphanException;
import cursos.Controladores.exceptions.NonexistentEntityException;
import cursos.persistence.Tcurso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.TseccionCursos;
import java.util.ArrayList;
import java.util.List;
import cursos.persistence.TcursoCiclo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TcursoJpaController implements Serializable {

    public TcursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tcurso tcurso) {
        if (tcurso.getTseccionCursosList() == null) {
            tcurso.setTseccionCursosList(new ArrayList<TseccionCursos>());
        }
        if (tcurso.getTcursoCicloList() == null) {
            tcurso.setTcursoCicloList(new ArrayList<TcursoCiclo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TseccionCursos> attachedTseccionCursosList = new ArrayList<TseccionCursos>();
            for (TseccionCursos tseccionCursosListTseccionCursosToAttach : tcurso.getTseccionCursosList()) {
                tseccionCursosListTseccionCursosToAttach = em.getReference(tseccionCursosListTseccionCursosToAttach.getClass(), tseccionCursosListTseccionCursosToAttach.getId());
                attachedTseccionCursosList.add(tseccionCursosListTseccionCursosToAttach);
            }
            tcurso.setTseccionCursosList(attachedTseccionCursosList);
            List<TcursoCiclo> attachedTcursoCicloList = new ArrayList<TcursoCiclo>();
            for (TcursoCiclo tcursoCicloListTcursoCicloToAttach : tcurso.getTcursoCicloList()) {
                tcursoCicloListTcursoCicloToAttach = em.getReference(tcursoCicloListTcursoCicloToAttach.getClass(), tcursoCicloListTcursoCicloToAttach.getId());
                attachedTcursoCicloList.add(tcursoCicloListTcursoCicloToAttach);
            }
            tcurso.setTcursoCicloList(attachedTcursoCicloList);
            em.persist(tcurso);
            for (TseccionCursos tseccionCursosListTseccionCursos : tcurso.getTseccionCursosList()) {
                Tcurso oldCursoidOfTseccionCursosListTseccionCursos = tseccionCursosListTseccionCursos.getCursoid();
                tseccionCursosListTseccionCursos.setCursoid(tcurso);
                tseccionCursosListTseccionCursos = em.merge(tseccionCursosListTseccionCursos);
                if (oldCursoidOfTseccionCursosListTseccionCursos != null) {
                    oldCursoidOfTseccionCursosListTseccionCursos.getTseccionCursosList().remove(tseccionCursosListTseccionCursos);
                    oldCursoidOfTseccionCursosListTseccionCursos = em.merge(oldCursoidOfTseccionCursosListTseccionCursos);
                }
            }
            for (TcursoCiclo tcursoCicloListTcursoCiclo : tcurso.getTcursoCicloList()) {
                Tcurso oldCursoidOfTcursoCicloListTcursoCiclo = tcursoCicloListTcursoCiclo.getCursoid();
                tcursoCicloListTcursoCiclo.setCursoid(tcurso);
                tcursoCicloListTcursoCiclo = em.merge(tcursoCicloListTcursoCiclo);
                if (oldCursoidOfTcursoCicloListTcursoCiclo != null) {
                    oldCursoidOfTcursoCicloListTcursoCiclo.getTcursoCicloList().remove(tcursoCicloListTcursoCiclo);
                    oldCursoidOfTcursoCicloListTcursoCiclo = em.merge(oldCursoidOfTcursoCicloListTcursoCiclo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tcurso tcurso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tcurso persistentTcurso = em.find(Tcurso.class, tcurso.getId());
            List<TseccionCursos> tseccionCursosListOld = persistentTcurso.getTseccionCursosList();
            List<TseccionCursos> tseccionCursosListNew = tcurso.getTseccionCursosList();
            List<TcursoCiclo> tcursoCicloListOld = persistentTcurso.getTcursoCicloList();
            List<TcursoCiclo> tcursoCicloListNew = tcurso.getTcursoCicloList();
            List<String> illegalOrphanMessages = null;
            for (TseccionCursos tseccionCursosListOldTseccionCursos : tseccionCursosListOld) {
                if (!tseccionCursosListNew.contains(tseccionCursosListOldTseccionCursos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TseccionCursos " + tseccionCursosListOldTseccionCursos + " since its cursoid field is not nullable.");
                }
            }
            for (TcursoCiclo tcursoCicloListOldTcursoCiclo : tcursoCicloListOld) {
                if (!tcursoCicloListNew.contains(tcursoCicloListOldTcursoCiclo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TcursoCiclo " + tcursoCicloListOldTcursoCiclo + " since its cursoid field is not nullable.");
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
            tcurso.setTseccionCursosList(tseccionCursosListNew);
            List<TcursoCiclo> attachedTcursoCicloListNew = new ArrayList<TcursoCiclo>();
            for (TcursoCiclo tcursoCicloListNewTcursoCicloToAttach : tcursoCicloListNew) {
                tcursoCicloListNewTcursoCicloToAttach = em.getReference(tcursoCicloListNewTcursoCicloToAttach.getClass(), tcursoCicloListNewTcursoCicloToAttach.getId());
                attachedTcursoCicloListNew.add(tcursoCicloListNewTcursoCicloToAttach);
            }
            tcursoCicloListNew = attachedTcursoCicloListNew;
            tcurso.setTcursoCicloList(tcursoCicloListNew);
            tcurso = em.merge(tcurso);
            for (TseccionCursos tseccionCursosListNewTseccionCursos : tseccionCursosListNew) {
                if (!tseccionCursosListOld.contains(tseccionCursosListNewTseccionCursos)) {
                    Tcurso oldCursoidOfTseccionCursosListNewTseccionCursos = tseccionCursosListNewTseccionCursos.getCursoid();
                    tseccionCursosListNewTseccionCursos.setCursoid(tcurso);
                    tseccionCursosListNewTseccionCursos = em.merge(tseccionCursosListNewTseccionCursos);
                    if (oldCursoidOfTseccionCursosListNewTseccionCursos != null && !oldCursoidOfTseccionCursosListNewTseccionCursos.equals(tcurso)) {
                        oldCursoidOfTseccionCursosListNewTseccionCursos.getTseccionCursosList().remove(tseccionCursosListNewTseccionCursos);
                        oldCursoidOfTseccionCursosListNewTseccionCursos = em.merge(oldCursoidOfTseccionCursosListNewTseccionCursos);
                    }
                }
            }
            for (TcursoCiclo tcursoCicloListNewTcursoCiclo : tcursoCicloListNew) {
                if (!tcursoCicloListOld.contains(tcursoCicloListNewTcursoCiclo)) {
                    Tcurso oldCursoidOfTcursoCicloListNewTcursoCiclo = tcursoCicloListNewTcursoCiclo.getCursoid();
                    tcursoCicloListNewTcursoCiclo.setCursoid(tcurso);
                    tcursoCicloListNewTcursoCiclo = em.merge(tcursoCicloListNewTcursoCiclo);
                    if (oldCursoidOfTcursoCicloListNewTcursoCiclo != null && !oldCursoidOfTcursoCicloListNewTcursoCiclo.equals(tcurso)) {
                        oldCursoidOfTcursoCicloListNewTcursoCiclo.getTcursoCicloList().remove(tcursoCicloListNewTcursoCiclo);
                        oldCursoidOfTcursoCicloListNewTcursoCiclo = em.merge(oldCursoidOfTcursoCicloListNewTcursoCiclo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tcurso.getId();
                if (findTcurso(id) == null) {
                    throw new NonexistentEntityException("The tcurso with id " + id + " no longer exists.");
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
            Tcurso tcurso;
            try {
                tcurso = em.getReference(Tcurso.class, id);
                tcurso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tcurso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TseccionCursos> tseccionCursosListOrphanCheck = tcurso.getTseccionCursosList();
            for (TseccionCursos tseccionCursosListOrphanCheckTseccionCursos : tseccionCursosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tcurso (" + tcurso + ") cannot be destroyed since the TseccionCursos " + tseccionCursosListOrphanCheckTseccionCursos + " in its tseccionCursosList field has a non-nullable cursoid field.");
            }
            List<TcursoCiclo> tcursoCicloListOrphanCheck = tcurso.getTcursoCicloList();
            for (TcursoCiclo tcursoCicloListOrphanCheckTcursoCiclo : tcursoCicloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tcurso (" + tcurso + ") cannot be destroyed since the TcursoCiclo " + tcursoCicloListOrphanCheckTcursoCiclo + " in its tcursoCicloList field has a non-nullable cursoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tcurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tcurso> findTcursoEntities() {
        return findTcursoEntities(true, -1, -1);
    }

    public List<Tcurso> findTcursoEntities(int maxResults, int firstResult) {
        return findTcursoEntities(false, maxResults, firstResult);
    }

    private List<Tcurso> findTcursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tcurso.class));
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

    public Tcurso findTcurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tcurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getTcursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tcurso> rt = cq.from(Tcurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

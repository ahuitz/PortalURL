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
import cursos.persistence.TseccionCursos;
import java.util.ArrayList;
import java.util.List;
import cursos.persistence.Tasignados;
import cursos.persistence.Tentrega;
import cursos.persistence.Tusuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TusuarioJpaController implements Serializable {

    public TusuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tusuario tusuario) {
        if (tusuario.getTseccionCursosList() == null) {
            tusuario.setTseccionCursosList(new ArrayList<TseccionCursos>());
        }
        if (tusuario.getTasignadosList() == null) {
            tusuario.setTasignadosList(new ArrayList<Tasignados>());
        }
        if (tusuario.getTentregaList() == null) {
            tusuario.setTentregaList(new ArrayList<Tentrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TseccionCursos> attachedTseccionCursosList = new ArrayList<TseccionCursos>();
            for (TseccionCursos tseccionCursosListTseccionCursosToAttach : tusuario.getTseccionCursosList()) {
                tseccionCursosListTseccionCursosToAttach = em.getReference(tseccionCursosListTseccionCursosToAttach.getClass(), tseccionCursosListTseccionCursosToAttach.getId());
                attachedTseccionCursosList.add(tseccionCursosListTseccionCursosToAttach);
            }
            tusuario.setTseccionCursosList(attachedTseccionCursosList);
            List<Tasignados> attachedTasignadosList = new ArrayList<Tasignados>();
            for (Tasignados tasignadosListTasignadosToAttach : tusuario.getTasignadosList()) {
                tasignadosListTasignadosToAttach = em.getReference(tasignadosListTasignadosToAttach.getClass(), tasignadosListTasignadosToAttach.getId());
                attachedTasignadosList.add(tasignadosListTasignadosToAttach);
            }
            tusuario.setTasignadosList(attachedTasignadosList);
            List<Tentrega> attachedTentregaList = new ArrayList<Tentrega>();
            for (Tentrega tentregaListTentregaToAttach : tusuario.getTentregaList()) {
                tentregaListTentregaToAttach = em.getReference(tentregaListTentregaToAttach.getClass(), tentregaListTentregaToAttach.getId());
                attachedTentregaList.add(tentregaListTentregaToAttach);
            }
            tusuario.setTentregaList(attachedTentregaList);
            em.persist(tusuario);
            for (TseccionCursos tseccionCursosListTseccionCursos : tusuario.getTseccionCursosList()) {
                Tusuario oldCatedraticoidOfTseccionCursosListTseccionCursos = tseccionCursosListTseccionCursos.getCatedraticoid();
                tseccionCursosListTseccionCursos.setCatedraticoid(tusuario);
                tseccionCursosListTseccionCursos = em.merge(tseccionCursosListTseccionCursos);
                if (oldCatedraticoidOfTseccionCursosListTseccionCursos != null) {
                    oldCatedraticoidOfTseccionCursosListTseccionCursos.getTseccionCursosList().remove(tseccionCursosListTseccionCursos);
                    oldCatedraticoidOfTseccionCursosListTseccionCursos = em.merge(oldCatedraticoidOfTseccionCursosListTseccionCursos);
                }
            }
            for (Tasignados tasignadosListTasignados : tusuario.getTasignadosList()) {
                Tusuario oldUsuarioidOfTasignadosListTasignados = tasignadosListTasignados.getUsuarioid();
                tasignadosListTasignados.setUsuarioid(tusuario);
                tasignadosListTasignados = em.merge(tasignadosListTasignados);
                if (oldUsuarioidOfTasignadosListTasignados != null) {
                    oldUsuarioidOfTasignadosListTasignados.getTasignadosList().remove(tasignadosListTasignados);
                    oldUsuarioidOfTasignadosListTasignados = em.merge(oldUsuarioidOfTasignadosListTasignados);
                }
            }
            for (Tentrega tentregaListTentrega : tusuario.getTentregaList()) {
                Tusuario oldUsuarioidOfTentregaListTentrega = tentregaListTentrega.getUsuarioid();
                tentregaListTentrega.setUsuarioid(tusuario);
                tentregaListTentrega = em.merge(tentregaListTentrega);
                if (oldUsuarioidOfTentregaListTentrega != null) {
                    oldUsuarioidOfTentregaListTentrega.getTentregaList().remove(tentregaListTentrega);
                    oldUsuarioidOfTentregaListTentrega = em.merge(oldUsuarioidOfTentregaListTentrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tusuario tusuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tusuario persistentTusuario = em.find(Tusuario.class, tusuario.getId());
            List<TseccionCursos> tseccionCursosListOld = persistentTusuario.getTseccionCursosList();
            List<TseccionCursos> tseccionCursosListNew = tusuario.getTseccionCursosList();
            List<Tasignados> tasignadosListOld = persistentTusuario.getTasignadosList();
            List<Tasignados> tasignadosListNew = tusuario.getTasignadosList();
            List<Tentrega> tentregaListOld = persistentTusuario.getTentregaList();
            List<Tentrega> tentregaListNew = tusuario.getTentregaList();
            List<String> illegalOrphanMessages = null;
            for (TseccionCursos tseccionCursosListOldTseccionCursos : tseccionCursosListOld) {
                if (!tseccionCursosListNew.contains(tseccionCursosListOldTseccionCursos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TseccionCursos " + tseccionCursosListOldTseccionCursos + " since its catedraticoid field is not nullable.");
                }
            }
            for (Tasignados tasignadosListOldTasignados : tasignadosListOld) {
                if (!tasignadosListNew.contains(tasignadosListOldTasignados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tasignados " + tasignadosListOldTasignados + " since its usuarioid field is not nullable.");
                }
            }
            for (Tentrega tentregaListOldTentrega : tentregaListOld) {
                if (!tentregaListNew.contains(tentregaListOldTentrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tentrega " + tentregaListOldTentrega + " since its usuarioid field is not nullable.");
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
            tusuario.setTseccionCursosList(tseccionCursosListNew);
            List<Tasignados> attachedTasignadosListNew = new ArrayList<Tasignados>();
            for (Tasignados tasignadosListNewTasignadosToAttach : tasignadosListNew) {
                tasignadosListNewTasignadosToAttach = em.getReference(tasignadosListNewTasignadosToAttach.getClass(), tasignadosListNewTasignadosToAttach.getId());
                attachedTasignadosListNew.add(tasignadosListNewTasignadosToAttach);
            }
            tasignadosListNew = attachedTasignadosListNew;
            tusuario.setTasignadosList(tasignadosListNew);
            List<Tentrega> attachedTentregaListNew = new ArrayList<Tentrega>();
            for (Tentrega tentregaListNewTentregaToAttach : tentregaListNew) {
                tentregaListNewTentregaToAttach = em.getReference(tentregaListNewTentregaToAttach.getClass(), tentregaListNewTentregaToAttach.getId());
                attachedTentregaListNew.add(tentregaListNewTentregaToAttach);
            }
            tentregaListNew = attachedTentregaListNew;
            tusuario.setTentregaList(tentregaListNew);
            tusuario = em.merge(tusuario);
            for (TseccionCursos tseccionCursosListNewTseccionCursos : tseccionCursosListNew) {
                if (!tseccionCursosListOld.contains(tseccionCursosListNewTseccionCursos)) {
                    Tusuario oldCatedraticoidOfTseccionCursosListNewTseccionCursos = tseccionCursosListNewTseccionCursos.getCatedraticoid();
                    tseccionCursosListNewTseccionCursos.setCatedraticoid(tusuario);
                    tseccionCursosListNewTseccionCursos = em.merge(tseccionCursosListNewTseccionCursos);
                    if (oldCatedraticoidOfTseccionCursosListNewTseccionCursos != null && !oldCatedraticoidOfTseccionCursosListNewTseccionCursos.equals(tusuario)) {
                        oldCatedraticoidOfTseccionCursosListNewTseccionCursos.getTseccionCursosList().remove(tseccionCursosListNewTseccionCursos);
                        oldCatedraticoidOfTseccionCursosListNewTseccionCursos = em.merge(oldCatedraticoidOfTseccionCursosListNewTseccionCursos);
                    }
                }
            }
            for (Tasignados tasignadosListNewTasignados : tasignadosListNew) {
                if (!tasignadosListOld.contains(tasignadosListNewTasignados)) {
                    Tusuario oldUsuarioidOfTasignadosListNewTasignados = tasignadosListNewTasignados.getUsuarioid();
                    tasignadosListNewTasignados.setUsuarioid(tusuario);
                    tasignadosListNewTasignados = em.merge(tasignadosListNewTasignados);
                    if (oldUsuarioidOfTasignadosListNewTasignados != null && !oldUsuarioidOfTasignadosListNewTasignados.equals(tusuario)) {
                        oldUsuarioidOfTasignadosListNewTasignados.getTasignadosList().remove(tasignadosListNewTasignados);
                        oldUsuarioidOfTasignadosListNewTasignados = em.merge(oldUsuarioidOfTasignadosListNewTasignados);
                    }
                }
            }
            for (Tentrega tentregaListNewTentrega : tentregaListNew) {
                if (!tentregaListOld.contains(tentregaListNewTentrega)) {
                    Tusuario oldUsuarioidOfTentregaListNewTentrega = tentregaListNewTentrega.getUsuarioid();
                    tentregaListNewTentrega.setUsuarioid(tusuario);
                    tentregaListNewTentrega = em.merge(tentregaListNewTentrega);
                    if (oldUsuarioidOfTentregaListNewTentrega != null && !oldUsuarioidOfTentregaListNewTentrega.equals(tusuario)) {
                        oldUsuarioidOfTentregaListNewTentrega.getTentregaList().remove(tentregaListNewTentrega);
                        oldUsuarioidOfTentregaListNewTentrega = em.merge(oldUsuarioidOfTentregaListNewTentrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tusuario.getId();
                if (findTusuario(id) == null) {
                    throw new NonexistentEntityException("The tusuario with id " + id + " no longer exists.");
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
            Tusuario tusuario;
            try {
                tusuario = em.getReference(Tusuario.class, id);
                tusuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tusuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TseccionCursos> tseccionCursosListOrphanCheck = tusuario.getTseccionCursosList();
            for (TseccionCursos tseccionCursosListOrphanCheckTseccionCursos : tseccionCursosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tusuario (" + tusuario + ") cannot be destroyed since the TseccionCursos " + tseccionCursosListOrphanCheckTseccionCursos + " in its tseccionCursosList field has a non-nullable catedraticoid field.");
            }
            List<Tasignados> tasignadosListOrphanCheck = tusuario.getTasignadosList();
            for (Tasignados tasignadosListOrphanCheckTasignados : tasignadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tusuario (" + tusuario + ") cannot be destroyed since the Tasignados " + tasignadosListOrphanCheckTasignados + " in its tasignadosList field has a non-nullable usuarioid field.");
            }
            List<Tentrega> tentregaListOrphanCheck = tusuario.getTentregaList();
            for (Tentrega tentregaListOrphanCheckTentrega : tentregaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tusuario (" + tusuario + ") cannot be destroyed since the Tentrega " + tentregaListOrphanCheckTentrega + " in its tentregaList field has a non-nullable usuarioid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tusuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tusuario> findTusuarioEntities() {
        return findTusuarioEntities(true, -1, -1);
    }

    public List<Tusuario> findTusuarioEntities(int maxResults, int firstResult) {
        return findTusuarioEntities(false, maxResults, firstResult);
    }

    private List<Tusuario> findTusuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tusuario.class));
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

    public Tusuario findTusuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tusuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getTusuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tusuario> rt = cq.from(Tusuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

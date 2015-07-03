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
import cursos.persistence.Tcurso;
import cursos.persistence.Tseccion;
import cursos.persistence.Tusuario;
import cursos.persistence.Tactividad;
import java.util.ArrayList;
import java.util.List;
import cursos.persistence.Trecurso;
import cursos.persistence.Tasignados;
import cursos.persistence.TseccionCursos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TseccionCursosJpaController implements Serializable {

    public TseccionCursosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TseccionCursos tseccionCursos) {
        if (tseccionCursos.getTactividadList() == null) {
            tseccionCursos.setTactividadList(new ArrayList<Tactividad>());
        }
        if (tseccionCursos.getTrecursoList() == null) {
            tseccionCursos.setTrecursoList(new ArrayList<Trecurso>());
        }
        if (tseccionCursos.getTasignadosList() == null) {
            tseccionCursos.setTasignadosList(new ArrayList<Tasignados>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tcurso cursoid = tseccionCursos.getCursoid();
            if (cursoid != null) {
                cursoid = em.getReference(cursoid.getClass(), cursoid.getId());
                tseccionCursos.setCursoid(cursoid);
            }
            Tseccion seccionid = tseccionCursos.getSeccionid();
            if (seccionid != null) {
                seccionid = em.getReference(seccionid.getClass(), seccionid.getId());
                tseccionCursos.setSeccionid(seccionid);
            }
            Tusuario catedraticoid = tseccionCursos.getCatedraticoid();
            if (catedraticoid != null) {
                catedraticoid = em.getReference(catedraticoid.getClass(), catedraticoid.getId());
                tseccionCursos.setCatedraticoid(catedraticoid);
            }
            List<Tactividad> attachedTactividadList = new ArrayList<Tactividad>();
            for (Tactividad tactividadListTactividadToAttach : tseccionCursos.getTactividadList()) {
                tactividadListTactividadToAttach = em.getReference(tactividadListTactividadToAttach.getClass(), tactividadListTactividadToAttach.getId());
                attachedTactividadList.add(tactividadListTactividadToAttach);
            }
            tseccionCursos.setTactividadList(attachedTactividadList);
            List<Trecurso> attachedTrecursoList = new ArrayList<Trecurso>();
            for (Trecurso trecursoListTrecursoToAttach : tseccionCursos.getTrecursoList()) {
                trecursoListTrecursoToAttach = em.getReference(trecursoListTrecursoToAttach.getClass(), trecursoListTrecursoToAttach.getId());
                attachedTrecursoList.add(trecursoListTrecursoToAttach);
            }
            tseccionCursos.setTrecursoList(attachedTrecursoList);
            List<Tasignados> attachedTasignadosList = new ArrayList<Tasignados>();
            for (Tasignados tasignadosListTasignadosToAttach : tseccionCursos.getTasignadosList()) {
                tasignadosListTasignadosToAttach = em.getReference(tasignadosListTasignadosToAttach.getClass(), tasignadosListTasignadosToAttach.getId());
                attachedTasignadosList.add(tasignadosListTasignadosToAttach);
            }
            tseccionCursos.setTasignadosList(attachedTasignadosList);
            em.persist(tseccionCursos);
            if (cursoid != null) {
                cursoid.getTseccionCursosList().add(tseccionCursos);
                cursoid = em.merge(cursoid);
            }
            if (seccionid != null) {
                seccionid.getTseccionCursosList().add(tseccionCursos);
                seccionid = em.merge(seccionid);
            }
            if (catedraticoid != null) {
                catedraticoid.getTseccionCursosList().add(tseccionCursos);
                catedraticoid = em.merge(catedraticoid);
            }
            for (Tactividad tactividadListTactividad : tseccionCursos.getTactividadList()) {
                TseccionCursos oldSeccionCursoidOfTactividadListTactividad = tactividadListTactividad.getSeccionCursoid();
                tactividadListTactividad.setSeccionCursoid(tseccionCursos);
                tactividadListTactividad = em.merge(tactividadListTactividad);
                if (oldSeccionCursoidOfTactividadListTactividad != null) {
                    oldSeccionCursoidOfTactividadListTactividad.getTactividadList().remove(tactividadListTactividad);
                    oldSeccionCursoidOfTactividadListTactividad = em.merge(oldSeccionCursoidOfTactividadListTactividad);
                }
            }
            for (Trecurso trecursoListTrecurso : tseccionCursos.getTrecursoList()) {
                TseccionCursos oldSeccionCursoidOfTrecursoListTrecurso = trecursoListTrecurso.getSeccionCursoid();
                trecursoListTrecurso.setSeccionCursoid(tseccionCursos);
                trecursoListTrecurso = em.merge(trecursoListTrecurso);
                if (oldSeccionCursoidOfTrecursoListTrecurso != null) {
                    oldSeccionCursoidOfTrecursoListTrecurso.getTrecursoList().remove(trecursoListTrecurso);
                    oldSeccionCursoidOfTrecursoListTrecurso = em.merge(oldSeccionCursoidOfTrecursoListTrecurso);
                }
            }
            for (Tasignados tasignadosListTasignados : tseccionCursos.getTasignadosList()) {
                TseccionCursos oldSeccionCursoidOfTasignadosListTasignados = tasignadosListTasignados.getSeccionCursoid();
                tasignadosListTasignados.setSeccionCursoid(tseccionCursos);
                tasignadosListTasignados = em.merge(tasignadosListTasignados);
                if (oldSeccionCursoidOfTasignadosListTasignados != null) {
                    oldSeccionCursoidOfTasignadosListTasignados.getTasignadosList().remove(tasignadosListTasignados);
                    oldSeccionCursoidOfTasignadosListTasignados = em.merge(oldSeccionCursoidOfTasignadosListTasignados);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TseccionCursos tseccionCursos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TseccionCursos persistentTseccionCursos = em.find(TseccionCursos.class, tseccionCursos.getId());
            Tcurso cursoidOld = persistentTseccionCursos.getCursoid();
            Tcurso cursoidNew = tseccionCursos.getCursoid();
            Tseccion seccionidOld = persistentTseccionCursos.getSeccionid();
            Tseccion seccionidNew = tseccionCursos.getSeccionid();
            Tusuario catedraticoidOld = persistentTseccionCursos.getCatedraticoid();
            Tusuario catedraticoidNew = tseccionCursos.getCatedraticoid();
            List<Tactividad> tactividadListOld = persistentTseccionCursos.getTactividadList();
            List<Tactividad> tactividadListNew = tseccionCursos.getTactividadList();
            List<Trecurso> trecursoListOld = persistentTseccionCursos.getTrecursoList();
            List<Trecurso> trecursoListNew = tseccionCursos.getTrecursoList();
            List<Tasignados> tasignadosListOld = persistentTseccionCursos.getTasignadosList();
            List<Tasignados> tasignadosListNew = tseccionCursos.getTasignadosList();
            List<String> illegalOrphanMessages = null;
            for (Tactividad tactividadListOldTactividad : tactividadListOld) {
                if (!tactividadListNew.contains(tactividadListOldTactividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tactividad " + tactividadListOldTactividad + " since its seccionCursoid field is not nullable.");
                }
            }
            for (Trecurso trecursoListOldTrecurso : trecursoListOld) {
                if (!trecursoListNew.contains(trecursoListOldTrecurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trecurso " + trecursoListOldTrecurso + " since its seccionCursoid field is not nullable.");
                }
            }
            for (Tasignados tasignadosListOldTasignados : tasignadosListOld) {
                if (!tasignadosListNew.contains(tasignadosListOldTasignados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tasignados " + tasignadosListOldTasignados + " since its seccionCursoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cursoidNew != null) {
                cursoidNew = em.getReference(cursoidNew.getClass(), cursoidNew.getId());
                tseccionCursos.setCursoid(cursoidNew);
            }
            if (seccionidNew != null) {
                seccionidNew = em.getReference(seccionidNew.getClass(), seccionidNew.getId());
                tseccionCursos.setSeccionid(seccionidNew);
            }
            if (catedraticoidNew != null) {
                catedraticoidNew = em.getReference(catedraticoidNew.getClass(), catedraticoidNew.getId());
                tseccionCursos.setCatedraticoid(catedraticoidNew);
            }
            List<Tactividad> attachedTactividadListNew = new ArrayList<Tactividad>();
            for (Tactividad tactividadListNewTactividadToAttach : tactividadListNew) {
                tactividadListNewTactividadToAttach = em.getReference(tactividadListNewTactividadToAttach.getClass(), tactividadListNewTactividadToAttach.getId());
                attachedTactividadListNew.add(tactividadListNewTactividadToAttach);
            }
            tactividadListNew = attachedTactividadListNew;
            tseccionCursos.setTactividadList(tactividadListNew);
            List<Trecurso> attachedTrecursoListNew = new ArrayList<Trecurso>();
            for (Trecurso trecursoListNewTrecursoToAttach : trecursoListNew) {
                trecursoListNewTrecursoToAttach = em.getReference(trecursoListNewTrecursoToAttach.getClass(), trecursoListNewTrecursoToAttach.getId());
                attachedTrecursoListNew.add(trecursoListNewTrecursoToAttach);
            }
            trecursoListNew = attachedTrecursoListNew;
            tseccionCursos.setTrecursoList(trecursoListNew);
            List<Tasignados> attachedTasignadosListNew = new ArrayList<Tasignados>();
            for (Tasignados tasignadosListNewTasignadosToAttach : tasignadosListNew) {
                tasignadosListNewTasignadosToAttach = em.getReference(tasignadosListNewTasignadosToAttach.getClass(), tasignadosListNewTasignadosToAttach.getId());
                attachedTasignadosListNew.add(tasignadosListNewTasignadosToAttach);
            }
            tasignadosListNew = attachedTasignadosListNew;
            tseccionCursos.setTasignadosList(tasignadosListNew);
            tseccionCursos = em.merge(tseccionCursos);
            if (cursoidOld != null && !cursoidOld.equals(cursoidNew)) {
                cursoidOld.getTseccionCursosList().remove(tseccionCursos);
                cursoidOld = em.merge(cursoidOld);
            }
            if (cursoidNew != null && !cursoidNew.equals(cursoidOld)) {
                cursoidNew.getTseccionCursosList().add(tseccionCursos);
                cursoidNew = em.merge(cursoidNew);
            }
            if (seccionidOld != null && !seccionidOld.equals(seccionidNew)) {
                seccionidOld.getTseccionCursosList().remove(tseccionCursos);
                seccionidOld = em.merge(seccionidOld);
            }
            if (seccionidNew != null && !seccionidNew.equals(seccionidOld)) {
                seccionidNew.getTseccionCursosList().add(tseccionCursos);
                seccionidNew = em.merge(seccionidNew);
            }
            if (catedraticoidOld != null && !catedraticoidOld.equals(catedraticoidNew)) {
                catedraticoidOld.getTseccionCursosList().remove(tseccionCursos);
                catedraticoidOld = em.merge(catedraticoidOld);
            }
            if (catedraticoidNew != null && !catedraticoidNew.equals(catedraticoidOld)) {
                catedraticoidNew.getTseccionCursosList().add(tseccionCursos);
                catedraticoidNew = em.merge(catedraticoidNew);
            }
            for (Tactividad tactividadListNewTactividad : tactividadListNew) {
                if (!tactividadListOld.contains(tactividadListNewTactividad)) {
                    TseccionCursos oldSeccionCursoidOfTactividadListNewTactividad = tactividadListNewTactividad.getSeccionCursoid();
                    tactividadListNewTactividad.setSeccionCursoid(tseccionCursos);
                    tactividadListNewTactividad = em.merge(tactividadListNewTactividad);
                    if (oldSeccionCursoidOfTactividadListNewTactividad != null && !oldSeccionCursoidOfTactividadListNewTactividad.equals(tseccionCursos)) {
                        oldSeccionCursoidOfTactividadListNewTactividad.getTactividadList().remove(tactividadListNewTactividad);
                        oldSeccionCursoidOfTactividadListNewTactividad = em.merge(oldSeccionCursoidOfTactividadListNewTactividad);
                    }
                }
            }
            for (Trecurso trecursoListNewTrecurso : trecursoListNew) {
                if (!trecursoListOld.contains(trecursoListNewTrecurso)) {
                    TseccionCursos oldSeccionCursoidOfTrecursoListNewTrecurso = trecursoListNewTrecurso.getSeccionCursoid();
                    trecursoListNewTrecurso.setSeccionCursoid(tseccionCursos);
                    trecursoListNewTrecurso = em.merge(trecursoListNewTrecurso);
                    if (oldSeccionCursoidOfTrecursoListNewTrecurso != null && !oldSeccionCursoidOfTrecursoListNewTrecurso.equals(tseccionCursos)) {
                        oldSeccionCursoidOfTrecursoListNewTrecurso.getTrecursoList().remove(trecursoListNewTrecurso);
                        oldSeccionCursoidOfTrecursoListNewTrecurso = em.merge(oldSeccionCursoidOfTrecursoListNewTrecurso);
                    }
                }
            }
            for (Tasignados tasignadosListNewTasignados : tasignadosListNew) {
                if (!tasignadosListOld.contains(tasignadosListNewTasignados)) {
                    TseccionCursos oldSeccionCursoidOfTasignadosListNewTasignados = tasignadosListNewTasignados.getSeccionCursoid();
                    tasignadosListNewTasignados.setSeccionCursoid(tseccionCursos);
                    tasignadosListNewTasignados = em.merge(tasignadosListNewTasignados);
                    if (oldSeccionCursoidOfTasignadosListNewTasignados != null && !oldSeccionCursoidOfTasignadosListNewTasignados.equals(tseccionCursos)) {
                        oldSeccionCursoidOfTasignadosListNewTasignados.getTasignadosList().remove(tasignadosListNewTasignados);
                        oldSeccionCursoidOfTasignadosListNewTasignados = em.merge(oldSeccionCursoidOfTasignadosListNewTasignados);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tseccionCursos.getId();
                if (findTseccionCursos(id) == null) {
                    throw new NonexistentEntityException("The tseccionCursos with id " + id + " no longer exists.");
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
            TseccionCursos tseccionCursos;
            try {
                tseccionCursos = em.getReference(TseccionCursos.class, id);
                tseccionCursos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tseccionCursos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tactividad> tactividadListOrphanCheck = tseccionCursos.getTactividadList();
            for (Tactividad tactividadListOrphanCheckTactividad : tactividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TseccionCursos (" + tseccionCursos + ") cannot be destroyed since the Tactividad " + tactividadListOrphanCheckTactividad + " in its tactividadList field has a non-nullable seccionCursoid field.");
            }
            List<Trecurso> trecursoListOrphanCheck = tseccionCursos.getTrecursoList();
            for (Trecurso trecursoListOrphanCheckTrecurso : trecursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TseccionCursos (" + tseccionCursos + ") cannot be destroyed since the Trecurso " + trecursoListOrphanCheckTrecurso + " in its trecursoList field has a non-nullable seccionCursoid field.");
            }
            List<Tasignados> tasignadosListOrphanCheck = tseccionCursos.getTasignadosList();
            for (Tasignados tasignadosListOrphanCheckTasignados : tasignadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TseccionCursos (" + tseccionCursos + ") cannot be destroyed since the Tasignados " + tasignadosListOrphanCheckTasignados + " in its tasignadosList field has a non-nullable seccionCursoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tcurso cursoid = tseccionCursos.getCursoid();
            if (cursoid != null) {
                cursoid.getTseccionCursosList().remove(tseccionCursos);
                cursoid = em.merge(cursoid);
            }
            Tseccion seccionid = tseccionCursos.getSeccionid();
            if (seccionid != null) {
                seccionid.getTseccionCursosList().remove(tseccionCursos);
                seccionid = em.merge(seccionid);
            }
            Tusuario catedraticoid = tseccionCursos.getCatedraticoid();
            if (catedraticoid != null) {
                catedraticoid.getTseccionCursosList().remove(tseccionCursos);
                catedraticoid = em.merge(catedraticoid);
            }
            em.remove(tseccionCursos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TseccionCursos> findTseccionCursosEntities() {
        return findTseccionCursosEntities(true, -1, -1);
    }

    public List<TseccionCursos> findTseccionCursosEntities(int maxResults, int firstResult) {
        return findTseccionCursosEntities(false, maxResults, firstResult);
    }

    private List<TseccionCursos> findTseccionCursosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TseccionCursos.class));
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

    public TseccionCursos findTseccionCursos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TseccionCursos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTseccionCursosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TseccionCursos> rt = cq.from(TseccionCursos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.Tarchivos;
import cursos.persistence.Trecurso;
import cursos.persistence.TseccionCursos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TrecursoJpaController implements Serializable {

    public TrecursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trecurso trecurso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarchivos archivosid = trecurso.getArchivosid();
            if (archivosid != null) {
                archivosid = em.getReference(archivosid.getClass(), archivosid.getId());
                trecurso.setArchivosid(archivosid);
            }
            TseccionCursos seccionCursoid = trecurso.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid = em.getReference(seccionCursoid.getClass(), seccionCursoid.getId());
                trecurso.setSeccionCursoid(seccionCursoid);
            }
            em.persist(trecurso);
            if (archivosid != null) {
                archivosid.getTrecursoList().add(trecurso);
                archivosid = em.merge(archivosid);
            }
            if (seccionCursoid != null) {
                seccionCursoid.getTrecursoList().add(trecurso);
                seccionCursoid = em.merge(seccionCursoid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trecurso trecurso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trecurso persistentTrecurso = em.find(Trecurso.class, trecurso.getId());
            Tarchivos archivosidOld = persistentTrecurso.getArchivosid();
            Tarchivos archivosidNew = trecurso.getArchivosid();
            TseccionCursos seccionCursoidOld = persistentTrecurso.getSeccionCursoid();
            TseccionCursos seccionCursoidNew = trecurso.getSeccionCursoid();
            if (archivosidNew != null) {
                archivosidNew = em.getReference(archivosidNew.getClass(), archivosidNew.getId());
                trecurso.setArchivosid(archivosidNew);
            }
            if (seccionCursoidNew != null) {
                seccionCursoidNew = em.getReference(seccionCursoidNew.getClass(), seccionCursoidNew.getId());
                trecurso.setSeccionCursoid(seccionCursoidNew);
            }
            trecurso = em.merge(trecurso);
            if (archivosidOld != null && !archivosidOld.equals(archivosidNew)) {
                archivosidOld.getTrecursoList().remove(trecurso);
                archivosidOld = em.merge(archivosidOld);
            }
            if (archivosidNew != null && !archivosidNew.equals(archivosidOld)) {
                archivosidNew.getTrecursoList().add(trecurso);
                archivosidNew = em.merge(archivosidNew);
            }
            if (seccionCursoidOld != null && !seccionCursoidOld.equals(seccionCursoidNew)) {
                seccionCursoidOld.getTrecursoList().remove(trecurso);
                seccionCursoidOld = em.merge(seccionCursoidOld);
            }
            if (seccionCursoidNew != null && !seccionCursoidNew.equals(seccionCursoidOld)) {
                seccionCursoidNew.getTrecursoList().add(trecurso);
                seccionCursoidNew = em.merge(seccionCursoidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = trecurso.getId();
                if (findTrecurso(id) == null) {
                    throw new NonexistentEntityException("The trecurso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trecurso trecurso;
            try {
                trecurso = em.getReference(Trecurso.class, id);
                trecurso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trecurso with id " + id + " no longer exists.", enfe);
            }
            Tarchivos archivosid = trecurso.getArchivosid();
            if (archivosid != null) {
                archivosid.getTrecursoList().remove(trecurso);
                archivosid = em.merge(archivosid);
            }
            TseccionCursos seccionCursoid = trecurso.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid.getTrecursoList().remove(trecurso);
                seccionCursoid = em.merge(seccionCursoid);
            }
            em.remove(trecurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Trecurso> findTrecursoEntities() {
        return findTrecursoEntities(true, -1, -1);
    }

    public List<Trecurso> findTrecursoEntities(int maxResults, int firstResult) {
        return findTrecursoEntities(false, maxResults, firstResult);
    }

    private List<Trecurso> findTrecursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trecurso.class));
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

    public Trecurso findTrecurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trecurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrecursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trecurso> rt = cq.from(Trecurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

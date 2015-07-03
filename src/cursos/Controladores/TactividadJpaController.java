/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.Controladores;

import cursos.Controladores.exceptions.IllegalOrphanException;
import cursos.Controladores.exceptions.NonexistentEntityException;
import cursos.persistence.Tactividad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.persistence.TseccionCursos;
import cursos.persistence.Tentrega;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diaz
 */
public class TactividadJpaController implements Serializable {

    public TactividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tactividad tactividad) {
        if (tactividad.getTentregaList() == null) {
            tactividad.setTentregaList(new ArrayList<Tentrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TseccionCursos seccionCursoid = tactividad.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid = em.getReference(seccionCursoid.getClass(), seccionCursoid.getId());
                tactividad.setSeccionCursoid(seccionCursoid);
            }
            List<Tentrega> attachedTentregaList = new ArrayList<Tentrega>();
            for (Tentrega tentregaListTentregaToAttach : tactividad.getTentregaList()) {
                tentregaListTentregaToAttach = em.getReference(tentregaListTentregaToAttach.getClass(), tentregaListTentregaToAttach.getId());
                attachedTentregaList.add(tentregaListTentregaToAttach);
            }
            tactividad.setTentregaList(attachedTentregaList);
            em.persist(tactividad);
            if (seccionCursoid != null) {
                seccionCursoid.getTactividadList().add(tactividad);
                seccionCursoid = em.merge(seccionCursoid);
            }
            for (Tentrega tentregaListTentrega : tactividad.getTentregaList()) {
                Tactividad oldActividadidOfTentregaListTentrega = tentregaListTentrega.getActividadid();
                tentregaListTentrega.setActividadid(tactividad);
                tentregaListTentrega = em.merge(tentregaListTentrega);
                if (oldActividadidOfTentregaListTentrega != null) {
                    oldActividadidOfTentregaListTentrega.getTentregaList().remove(tentregaListTentrega);
                    oldActividadidOfTentregaListTentrega = em.merge(oldActividadidOfTentregaListTentrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tactividad tactividad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tactividad persistentTactividad = em.find(Tactividad.class, tactividad.getId());
            TseccionCursos seccionCursoidOld = persistentTactividad.getSeccionCursoid();
            TseccionCursos seccionCursoidNew = tactividad.getSeccionCursoid();
            List<Tentrega> tentregaListOld = persistentTactividad.getTentregaList();
            List<Tentrega> tentregaListNew = tactividad.getTentregaList();
            List<String> illegalOrphanMessages = null;
            for (Tentrega tentregaListOldTentrega : tentregaListOld) {
                if (!tentregaListNew.contains(tentregaListOldTentrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tentrega " + tentregaListOldTentrega + " since its actividadid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (seccionCursoidNew != null) {
                seccionCursoidNew = em.getReference(seccionCursoidNew.getClass(), seccionCursoidNew.getId());
                tactividad.setSeccionCursoid(seccionCursoidNew);
            }
            List<Tentrega> attachedTentregaListNew = new ArrayList<Tentrega>();
            for (Tentrega tentregaListNewTentregaToAttach : tentregaListNew) {
                tentregaListNewTentregaToAttach = em.getReference(tentregaListNewTentregaToAttach.getClass(), tentregaListNewTentregaToAttach.getId());
                attachedTentregaListNew.add(tentregaListNewTentregaToAttach);
            }
            tentregaListNew = attachedTentregaListNew;
            tactividad.setTentregaList(tentregaListNew);
            tactividad = em.merge(tactividad);
            if (seccionCursoidOld != null && !seccionCursoidOld.equals(seccionCursoidNew)) {
                seccionCursoidOld.getTactividadList().remove(tactividad);
                seccionCursoidOld = em.merge(seccionCursoidOld);
            }
            if (seccionCursoidNew != null && !seccionCursoidNew.equals(seccionCursoidOld)) {
                seccionCursoidNew.getTactividadList().add(tactividad);
                seccionCursoidNew = em.merge(seccionCursoidNew);
            }
            for (Tentrega tentregaListNewTentrega : tentregaListNew) {
                if (!tentregaListOld.contains(tentregaListNewTentrega)) {
                    Tactividad oldActividadidOfTentregaListNewTentrega = tentregaListNewTentrega.getActividadid();
                    tentregaListNewTentrega.setActividadid(tactividad);
                    tentregaListNewTentrega = em.merge(tentregaListNewTentrega);
                    if (oldActividadidOfTentregaListNewTentrega != null && !oldActividadidOfTentregaListNewTentrega.equals(tactividad)) {
                        oldActividadidOfTentregaListNewTentrega.getTentregaList().remove(tentregaListNewTentrega);
                        oldActividadidOfTentregaListNewTentrega = em.merge(oldActividadidOfTentregaListNewTentrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tactividad.getId();
                if (findTactividad(id) == null) {
                    throw new NonexistentEntityException("The tactividad with id " + id + " no longer exists.");
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
            Tactividad tactividad;
            try {
                tactividad = em.getReference(Tactividad.class, id);
                tactividad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tactividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tentrega> tentregaListOrphanCheck = tactividad.getTentregaList();
            for (Tentrega tentregaListOrphanCheckTentrega : tentregaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tactividad (" + tactividad + ") cannot be destroyed since the Tentrega " + tentregaListOrphanCheckTentrega + " in its tentregaList field has a non-nullable actividadid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TseccionCursos seccionCursoid = tactividad.getSeccionCursoid();
            if (seccionCursoid != null) {
                seccionCursoid.getTactividadList().remove(tactividad);
                seccionCursoid = em.merge(seccionCursoid);
            }
            em.remove(tactividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tactividad> findTactividadEntities() {
        return findTactividadEntities(true, -1, -1);
    }

    public List<Tactividad> findTactividadEntities(int maxResults, int firstResult) {
        return findTactividadEntities(false, maxResults, firstResult);
    }

    private List<Tactividad> findTactividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tactividad.class));
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

    public Tactividad findTactividad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tactividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getTactividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tactividad> rt = cq.from(Tactividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

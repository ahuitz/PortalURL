/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.DatosLaborales;
import Tablas.Jornada;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class JornadaJpaController implements Serializable {

    public JornadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jornada jornada) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DatosLaborales datosLaboralesid = jornada.getDatosLaboralesid();
            if (datosLaboralesid != null) {
                datosLaboralesid = em.getReference(datosLaboralesid.getClass(), datosLaboralesid.getId());
                jornada.setDatosLaboralesid(datosLaboralesid);
            }
            em.persist(jornada);
            if (datosLaboralesid != null) {
                datosLaboralesid.getJornadaCollection().add(jornada);
                datosLaboralesid = em.merge(datosLaboralesid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jornada jornada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jornada persistentJornada = em.find(Jornada.class, jornada.getId());
            DatosLaborales datosLaboralesidOld = persistentJornada.getDatosLaboralesid();
            DatosLaborales datosLaboralesidNew = jornada.getDatosLaboralesid();
            if (datosLaboralesidNew != null) {
                datosLaboralesidNew = em.getReference(datosLaboralesidNew.getClass(), datosLaboralesidNew.getId());
                jornada.setDatosLaboralesid(datosLaboralesidNew);
            }
            jornada = em.merge(jornada);
            if (datosLaboralesidOld != null && !datosLaboralesidOld.equals(datosLaboralesidNew)) {
                datosLaboralesidOld.getJornadaCollection().remove(jornada);
                datosLaboralesidOld = em.merge(datosLaboralesidOld);
            }
            if (datosLaboralesidNew != null && !datosLaboralesidNew.equals(datosLaboralesidOld)) {
                datosLaboralesidNew.getJornadaCollection().add(jornada);
                datosLaboralesidNew = em.merge(datosLaboralesidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jornada.getId();
                if (findJornada(id) == null) {
                    throw new NonexistentEntityException("The jornada with id " + id + " no longer exists.");
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
            Jornada jornada;
            try {
                jornada = em.getReference(Jornada.class, id);
                jornada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jornada with id " + id + " no longer exists.", enfe);
            }
            DatosLaborales datosLaboralesid = jornada.getDatosLaboralesid();
            if (datosLaboralesid != null) {
                datosLaboralesid.getJornadaCollection().remove(jornada);
                datosLaboralesid = em.merge(datosLaboralesid);
            }
            em.remove(jornada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jornada> findJornadaEntities() {
        return findJornadaEntities(true, -1, -1);
    }

    public List<Jornada> findJornadaEntities(int maxResults, int firstResult) {
        return findJornadaEntities(false, maxResults, firstResult);
    }

    private List<Jornada> findJornadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jornada.class));
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

    public Jornada findJornada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jornada.class, id);
        } finally {
            em.close();
        }
    }

    public int getJornadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jornada> rt = cq.from(Jornada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

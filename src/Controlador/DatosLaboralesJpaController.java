/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.Contacto;
import Tablas.Cargo;
import Tablas.DatosLaborales;
import Tablas.Jornada;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class DatosLaboralesJpaController implements Serializable {

    public DatosLaboralesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DatosLaborales datosLaborales) {
        if (datosLaborales.getJornadaCollection() == null) {
            datosLaborales.setJornadaCollection(new ArrayList<Jornada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto contactoid = datosLaborales.getContactoid();
            if (contactoid != null) {
                contactoid = em.getReference(contactoid.getClass(), contactoid.getId());
                datosLaborales.setContactoid(contactoid);
            }
            Cargo cargoid = datosLaborales.getCargoid();
            if (cargoid != null) {
                cargoid = em.getReference(cargoid.getClass(), cargoid.getId());
                datosLaborales.setCargoid(cargoid);
            }
            Collection<Jornada> attachedJornadaCollection = new ArrayList<Jornada>();
            for (Jornada jornadaCollectionJornadaToAttach : datosLaborales.getJornadaCollection()) {
                jornadaCollectionJornadaToAttach = em.getReference(jornadaCollectionJornadaToAttach.getClass(), jornadaCollectionJornadaToAttach.getId());
                attachedJornadaCollection.add(jornadaCollectionJornadaToAttach);
            }
            datosLaborales.setJornadaCollection(attachedJornadaCollection);
            em.persist(datosLaborales);
            if (contactoid != null) {
                contactoid.getDatosLaboralesCollection().add(datosLaborales);
                contactoid = em.merge(contactoid);
            }
            if (cargoid != null) {
                cargoid.getDatosLaboralesCollection().add(datosLaborales);
                cargoid = em.merge(cargoid);
            }
            for (Jornada jornadaCollectionJornada : datosLaborales.getJornadaCollection()) {
                DatosLaborales oldDatosLaboralesidOfJornadaCollectionJornada = jornadaCollectionJornada.getDatosLaboralesid();
                jornadaCollectionJornada.setDatosLaboralesid(datosLaborales);
                jornadaCollectionJornada = em.merge(jornadaCollectionJornada);
                if (oldDatosLaboralesidOfJornadaCollectionJornada != null) {
                    oldDatosLaboralesidOfJornadaCollectionJornada.getJornadaCollection().remove(jornadaCollectionJornada);
                    oldDatosLaboralesidOfJornadaCollectionJornada = em.merge(oldDatosLaboralesidOfJornadaCollectionJornada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DatosLaborales datosLaborales) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DatosLaborales persistentDatosLaborales = em.find(DatosLaborales.class, datosLaborales.getId());
            Contacto contactoidOld = persistentDatosLaborales.getContactoid();
            Contacto contactoidNew = datosLaborales.getContactoid();
            Cargo cargoidOld = persistentDatosLaborales.getCargoid();
            Cargo cargoidNew = datosLaborales.getCargoid();
            Collection<Jornada> jornadaCollectionOld = persistentDatosLaborales.getJornadaCollection();
            Collection<Jornada> jornadaCollectionNew = datosLaborales.getJornadaCollection();
            List<String> illegalOrphanMessages = null;
            for (Jornada jornadaCollectionOldJornada : jornadaCollectionOld) {
                if (!jornadaCollectionNew.contains(jornadaCollectionOldJornada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jornada " + jornadaCollectionOldJornada + " since its datosLaboralesid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (contactoidNew != null) {
                contactoidNew = em.getReference(contactoidNew.getClass(), contactoidNew.getId());
                datosLaborales.setContactoid(contactoidNew);
            }
            if (cargoidNew != null) {
                cargoidNew = em.getReference(cargoidNew.getClass(), cargoidNew.getId());
                datosLaborales.setCargoid(cargoidNew);
            }
            Collection<Jornada> attachedJornadaCollectionNew = new ArrayList<Jornada>();
            for (Jornada jornadaCollectionNewJornadaToAttach : jornadaCollectionNew) {
                jornadaCollectionNewJornadaToAttach = em.getReference(jornadaCollectionNewJornadaToAttach.getClass(), jornadaCollectionNewJornadaToAttach.getId());
                attachedJornadaCollectionNew.add(jornadaCollectionNewJornadaToAttach);
            }
            jornadaCollectionNew = attachedJornadaCollectionNew;
            datosLaborales.setJornadaCollection(jornadaCollectionNew);
            datosLaborales = em.merge(datosLaborales);
            if (contactoidOld != null && !contactoidOld.equals(contactoidNew)) {
                contactoidOld.getDatosLaboralesCollection().remove(datosLaborales);
                contactoidOld = em.merge(contactoidOld);
            }
            if (contactoidNew != null && !contactoidNew.equals(contactoidOld)) {
                contactoidNew.getDatosLaboralesCollection().add(datosLaborales);
                contactoidNew = em.merge(contactoidNew);
            }
            if (cargoidOld != null && !cargoidOld.equals(cargoidNew)) {
                cargoidOld.getDatosLaboralesCollection().remove(datosLaborales);
                cargoidOld = em.merge(cargoidOld);
            }
            if (cargoidNew != null && !cargoidNew.equals(cargoidOld)) {
                cargoidNew.getDatosLaboralesCollection().add(datosLaborales);
                cargoidNew = em.merge(cargoidNew);
            }
            for (Jornada jornadaCollectionNewJornada : jornadaCollectionNew) {
                if (!jornadaCollectionOld.contains(jornadaCollectionNewJornada)) {
                    DatosLaborales oldDatosLaboralesidOfJornadaCollectionNewJornada = jornadaCollectionNewJornada.getDatosLaboralesid();
                    jornadaCollectionNewJornada.setDatosLaboralesid(datosLaborales);
                    jornadaCollectionNewJornada = em.merge(jornadaCollectionNewJornada);
                    if (oldDatosLaboralesidOfJornadaCollectionNewJornada != null && !oldDatosLaboralesidOfJornadaCollectionNewJornada.equals(datosLaborales)) {
                        oldDatosLaboralesidOfJornadaCollectionNewJornada.getJornadaCollection().remove(jornadaCollectionNewJornada);
                        oldDatosLaboralesidOfJornadaCollectionNewJornada = em.merge(oldDatosLaboralesidOfJornadaCollectionNewJornada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = datosLaborales.getId();
                if (findDatosLaborales(id) == null) {
                    throw new NonexistentEntityException("The datosLaborales with id " + id + " no longer exists.");
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
            DatosLaborales datosLaborales;
            try {
                datosLaborales = em.getReference(DatosLaborales.class, id);
                datosLaborales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The datosLaborales with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Jornada> jornadaCollectionOrphanCheck = datosLaborales.getJornadaCollection();
            for (Jornada jornadaCollectionOrphanCheckJornada : jornadaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DatosLaborales (" + datosLaborales + ") cannot be destroyed since the Jornada " + jornadaCollectionOrphanCheckJornada + " in its jornadaCollection field has a non-nullable datosLaboralesid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Contacto contactoid = datosLaborales.getContactoid();
            if (contactoid != null) {
                contactoid.getDatosLaboralesCollection().remove(datosLaborales);
                contactoid = em.merge(contactoid);
            }
            Cargo cargoid = datosLaborales.getCargoid();
            if (cargoid != null) {
                cargoid.getDatosLaboralesCollection().remove(datosLaborales);
                cargoid = em.merge(cargoid);
            }
            em.remove(datosLaborales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DatosLaborales> findDatosLaboralesEntities() {
        return findDatosLaboralesEntities(true, -1, -1);
    }

    public List<DatosLaborales> findDatosLaboralesEntities(int maxResults, int firstResult) {
        return findDatosLaboralesEntities(false, maxResults, firstResult);
    }

    private List<DatosLaborales> findDatosLaboralesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DatosLaborales.class));
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

    public DatosLaborales findDatosLaborales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DatosLaborales.class, id);
        } finally {
            em.close();
        }
    }

    public int getDatosLaboralesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DatosLaborales> rt = cq.from(DatosLaborales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

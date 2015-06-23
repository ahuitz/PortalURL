/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Tablas.Cargo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.DatosLaborales;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class CargoJpaController implements Serializable {

    public CargoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargo cargo) {
        if (cargo.getDatosLaboralesCollection() == null) {
            cargo.setDatosLaboralesCollection(new ArrayList<DatosLaborales>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DatosLaborales> attachedDatosLaboralesCollection = new ArrayList<DatosLaborales>();
            for (DatosLaborales datosLaboralesCollectionDatosLaboralesToAttach : cargo.getDatosLaboralesCollection()) {
                datosLaboralesCollectionDatosLaboralesToAttach = em.getReference(datosLaboralesCollectionDatosLaboralesToAttach.getClass(), datosLaboralesCollectionDatosLaboralesToAttach.getId());
                attachedDatosLaboralesCollection.add(datosLaboralesCollectionDatosLaboralesToAttach);
            }
            cargo.setDatosLaboralesCollection(attachedDatosLaboralesCollection);
            em.persist(cargo);
            for (DatosLaborales datosLaboralesCollectionDatosLaborales : cargo.getDatosLaboralesCollection()) {
                Cargo oldCargoidOfDatosLaboralesCollectionDatosLaborales = datosLaboralesCollectionDatosLaborales.getCargoid();
                datosLaboralesCollectionDatosLaborales.setCargoid(cargo);
                datosLaboralesCollectionDatosLaborales = em.merge(datosLaboralesCollectionDatosLaborales);
                if (oldCargoidOfDatosLaboralesCollectionDatosLaborales != null) {
                    oldCargoidOfDatosLaboralesCollectionDatosLaborales.getDatosLaboralesCollection().remove(datosLaboralesCollectionDatosLaborales);
                    oldCargoidOfDatosLaboralesCollectionDatosLaborales = em.merge(oldCargoidOfDatosLaboralesCollectionDatosLaborales);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cargo cargo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargo persistentCargo = em.find(Cargo.class, cargo.getId());
            Collection<DatosLaborales> datosLaboralesCollectionOld = persistentCargo.getDatosLaboralesCollection();
            Collection<DatosLaborales> datosLaboralesCollectionNew = cargo.getDatosLaboralesCollection();
            List<String> illegalOrphanMessages = null;
            for (DatosLaborales datosLaboralesCollectionOldDatosLaborales : datosLaboralesCollectionOld) {
                if (!datosLaboralesCollectionNew.contains(datosLaboralesCollectionOldDatosLaborales)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DatosLaborales " + datosLaboralesCollectionOldDatosLaborales + " since its cargoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DatosLaborales> attachedDatosLaboralesCollectionNew = new ArrayList<DatosLaborales>();
            for (DatosLaborales datosLaboralesCollectionNewDatosLaboralesToAttach : datosLaboralesCollectionNew) {
                datosLaboralesCollectionNewDatosLaboralesToAttach = em.getReference(datosLaboralesCollectionNewDatosLaboralesToAttach.getClass(), datosLaboralesCollectionNewDatosLaboralesToAttach.getId());
                attachedDatosLaboralesCollectionNew.add(datosLaboralesCollectionNewDatosLaboralesToAttach);
            }
            datosLaboralesCollectionNew = attachedDatosLaboralesCollectionNew;
            cargo.setDatosLaboralesCollection(datosLaboralesCollectionNew);
            cargo = em.merge(cargo);
            for (DatosLaborales datosLaboralesCollectionNewDatosLaborales : datosLaboralesCollectionNew) {
                if (!datosLaboralesCollectionOld.contains(datosLaboralesCollectionNewDatosLaborales)) {
                    Cargo oldCargoidOfDatosLaboralesCollectionNewDatosLaborales = datosLaboralesCollectionNewDatosLaborales.getCargoid();
                    datosLaboralesCollectionNewDatosLaborales.setCargoid(cargo);
                    datosLaboralesCollectionNewDatosLaborales = em.merge(datosLaboralesCollectionNewDatosLaborales);
                    if (oldCargoidOfDatosLaboralesCollectionNewDatosLaborales != null && !oldCargoidOfDatosLaboralesCollectionNewDatosLaborales.equals(cargo)) {
                        oldCargoidOfDatosLaboralesCollectionNewDatosLaborales.getDatosLaboralesCollection().remove(datosLaboralesCollectionNewDatosLaborales);
                        oldCargoidOfDatosLaboralesCollectionNewDatosLaborales = em.merge(oldCargoidOfDatosLaboralesCollectionNewDatosLaborales);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cargo.getId();
                if (findCargo(id) == null) {
                    throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.");
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
            Cargo cargo;
            try {
                cargo = em.getReference(Cargo.class, id);
                cargo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DatosLaborales> datosLaboralesCollectionOrphanCheck = cargo.getDatosLaboralesCollection();
            for (DatosLaborales datosLaboralesCollectionOrphanCheckDatosLaborales : datosLaboralesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargo (" + cargo + ") cannot be destroyed since the DatosLaborales " + datosLaboralesCollectionOrphanCheckDatosLaborales + " in its datosLaboralesCollection field has a non-nullable cargoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cargo> findCargoEntities() {
        return findCargoEntities(true, -1, -1);
    }

    public List<Cargo> findCargoEntities(int maxResults, int firstResult) {
        return findCargoEntities(false, maxResults, firstResult);
    }

    private List<Cargo> findCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargo.class));
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

    public Cargo findCargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargo> rt = cq.from(Cargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

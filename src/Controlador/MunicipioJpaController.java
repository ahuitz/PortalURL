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
import Tablas.Departamento;
import Tablas.Contacto;
import Tablas.Municipio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class MunicipioJpaController implements Serializable {

    public MunicipioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Municipio municipio) {
        if (municipio.getContactoCollection() == null) {
            municipio.setContactoCollection(new ArrayList<Contacto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamentoid = municipio.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid = em.getReference(departamentoid.getClass(), departamentoid.getId());
                municipio.setDepartamentoid(departamentoid);
            }
            Collection<Contacto> attachedContactoCollection = new ArrayList<Contacto>();
            for (Contacto contactoCollectionContactoToAttach : municipio.getContactoCollection()) {
                contactoCollectionContactoToAttach = em.getReference(contactoCollectionContactoToAttach.getClass(), contactoCollectionContactoToAttach.getId());
                attachedContactoCollection.add(contactoCollectionContactoToAttach);
            }
            municipio.setContactoCollection(attachedContactoCollection);
            em.persist(municipio);
            if (departamentoid != null) {
                departamentoid.getMunicipioCollection().add(municipio);
                departamentoid = em.merge(departamentoid);
            }
            for (Contacto contactoCollectionContacto : municipio.getContactoCollection()) {
                Municipio oldMunicipioidOfContactoCollectionContacto = contactoCollectionContacto.getMunicipioid();
                contactoCollectionContacto.setMunicipioid(municipio);
                contactoCollectionContacto = em.merge(contactoCollectionContacto);
                if (oldMunicipioidOfContactoCollectionContacto != null) {
                    oldMunicipioidOfContactoCollectionContacto.getContactoCollection().remove(contactoCollectionContacto);
                    oldMunicipioidOfContactoCollectionContacto = em.merge(oldMunicipioidOfContactoCollectionContacto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Municipio municipio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio persistentMunicipio = em.find(Municipio.class, municipio.getId());
            Departamento departamentoidOld = persistentMunicipio.getDepartamentoid();
            Departamento departamentoidNew = municipio.getDepartamentoid();
            Collection<Contacto> contactoCollectionOld = persistentMunicipio.getContactoCollection();
            Collection<Contacto> contactoCollectionNew = municipio.getContactoCollection();
            List<String> illegalOrphanMessages = null;
            for (Contacto contactoCollectionOldContacto : contactoCollectionOld) {
                if (!contactoCollectionNew.contains(contactoCollectionOldContacto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contacto " + contactoCollectionOldContacto + " since its municipioid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departamentoidNew != null) {
                departamentoidNew = em.getReference(departamentoidNew.getClass(), departamentoidNew.getId());
                municipio.setDepartamentoid(departamentoidNew);
            }
            Collection<Contacto> attachedContactoCollectionNew = new ArrayList<Contacto>();
            for (Contacto contactoCollectionNewContactoToAttach : contactoCollectionNew) {
                contactoCollectionNewContactoToAttach = em.getReference(contactoCollectionNewContactoToAttach.getClass(), contactoCollectionNewContactoToAttach.getId());
                attachedContactoCollectionNew.add(contactoCollectionNewContactoToAttach);
            }
            contactoCollectionNew = attachedContactoCollectionNew;
            municipio.setContactoCollection(contactoCollectionNew);
            municipio = em.merge(municipio);
            if (departamentoidOld != null && !departamentoidOld.equals(departamentoidNew)) {
                departamentoidOld.getMunicipioCollection().remove(municipio);
                departamentoidOld = em.merge(departamentoidOld);
            }
            if (departamentoidNew != null && !departamentoidNew.equals(departamentoidOld)) {
                departamentoidNew.getMunicipioCollection().add(municipio);
                departamentoidNew = em.merge(departamentoidNew);
            }
            for (Contacto contactoCollectionNewContacto : contactoCollectionNew) {
                if (!contactoCollectionOld.contains(contactoCollectionNewContacto)) {
                    Municipio oldMunicipioidOfContactoCollectionNewContacto = contactoCollectionNewContacto.getMunicipioid();
                    contactoCollectionNewContacto.setMunicipioid(municipio);
                    contactoCollectionNewContacto = em.merge(contactoCollectionNewContacto);
                    if (oldMunicipioidOfContactoCollectionNewContacto != null && !oldMunicipioidOfContactoCollectionNewContacto.equals(municipio)) {
                        oldMunicipioidOfContactoCollectionNewContacto.getContactoCollection().remove(contactoCollectionNewContacto);
                        oldMunicipioidOfContactoCollectionNewContacto = em.merge(oldMunicipioidOfContactoCollectionNewContacto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = municipio.getId();
                if (findMunicipio(id) == null) {
                    throw new NonexistentEntityException("The municipio with id " + id + " no longer exists.");
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
            Municipio municipio;
            try {
                municipio = em.getReference(Municipio.class, id);
                municipio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contacto> contactoCollectionOrphanCheck = municipio.getContactoCollection();
            for (Contacto contactoCollectionOrphanCheckContacto : contactoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Municipio (" + municipio + ") cannot be destroyed since the Contacto " + contactoCollectionOrphanCheckContacto + " in its contactoCollection field has a non-nullable municipioid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento departamentoid = municipio.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid.getMunicipioCollection().remove(municipio);
                departamentoid = em.merge(departamentoid);
            }
            em.remove(municipio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Municipio> findMunicipioEntities() {
        return findMunicipioEntities(true, -1, -1);
    }

    public List<Municipio> findMunicipioEntities(int maxResults, int firstResult) {
        return findMunicipioEntities(false, maxResults, firstResult);
    }

    private List<Municipio> findMunicipioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Municipio.class));
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

    public Municipio findMunicipio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Municipio.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Municipio> rt = cq.from(Municipio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

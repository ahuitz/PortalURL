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
import Tablas.Pais;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) {
        if (pais.getContactoCollection() == null) {
            pais.setContactoCollection(new ArrayList<Contacto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Contacto> attachedContactoCollection = new ArrayList<Contacto>();
            for (Contacto contactoCollectionContactoToAttach : pais.getContactoCollection()) {
                contactoCollectionContactoToAttach = em.getReference(contactoCollectionContactoToAttach.getClass(), contactoCollectionContactoToAttach.getId());
                attachedContactoCollection.add(contactoCollectionContactoToAttach);
            }
            pais.setContactoCollection(attachedContactoCollection);
            em.persist(pais);
            for (Contacto contactoCollectionContacto : pais.getContactoCollection()) {
                Pais oldPaisidOfContactoCollectionContacto = contactoCollectionContacto.getPaisid();
                contactoCollectionContacto.setPaisid(pais);
                contactoCollectionContacto = em.merge(contactoCollectionContacto);
                if (oldPaisidOfContactoCollectionContacto != null) {
                    oldPaisidOfContactoCollectionContacto.getContactoCollection().remove(contactoCollectionContacto);
                    oldPaisidOfContactoCollectionContacto = em.merge(oldPaisidOfContactoCollectionContacto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getId());
            Collection<Contacto> contactoCollectionOld = persistentPais.getContactoCollection();
            Collection<Contacto> contactoCollectionNew = pais.getContactoCollection();
            List<String> illegalOrphanMessages = null;
            for (Contacto contactoCollectionOldContacto : contactoCollectionOld) {
                if (!contactoCollectionNew.contains(contactoCollectionOldContacto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contacto " + contactoCollectionOldContacto + " since its paisid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Contacto> attachedContactoCollectionNew = new ArrayList<Contacto>();
            for (Contacto contactoCollectionNewContactoToAttach : contactoCollectionNew) {
                contactoCollectionNewContactoToAttach = em.getReference(contactoCollectionNewContactoToAttach.getClass(), contactoCollectionNewContactoToAttach.getId());
                attachedContactoCollectionNew.add(contactoCollectionNewContactoToAttach);
            }
            contactoCollectionNew = attachedContactoCollectionNew;
            pais.setContactoCollection(contactoCollectionNew);
            pais = em.merge(pais);
            for (Contacto contactoCollectionNewContacto : contactoCollectionNew) {
                if (!contactoCollectionOld.contains(contactoCollectionNewContacto)) {
                    Pais oldPaisidOfContactoCollectionNewContacto = contactoCollectionNewContacto.getPaisid();
                    contactoCollectionNewContacto.setPaisid(pais);
                    contactoCollectionNewContacto = em.merge(contactoCollectionNewContacto);
                    if (oldPaisidOfContactoCollectionNewContacto != null && !oldPaisidOfContactoCollectionNewContacto.equals(pais)) {
                        oldPaisidOfContactoCollectionNewContacto.getContactoCollection().remove(contactoCollectionNewContacto);
                        oldPaisidOfContactoCollectionNewContacto = em.merge(oldPaisidOfContactoCollectionNewContacto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getId();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
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
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contacto> contactoCollectionOrphanCheck = pais.getContactoCollection();
            for (Contacto contactoCollectionOrphanCheckContacto : contactoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Contacto " + contactoCollectionOrphanCheckContacto + " in its contactoCollection field has a non-nullable paisid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

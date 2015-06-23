/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.Persona;
import Tablas.HistorialContrasenia;
import Tablas.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getHistorialContraseniaCollection() == null) {
            usuario.setHistorialContraseniaCollection(new ArrayList<HistorialContrasenia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona personaid = usuario.getPersonaid();
            if (personaid != null) {
                personaid = em.getReference(personaid.getClass(), personaid.getId());
                usuario.setPersonaid(personaid);
            }
            Collection<HistorialContrasenia> attachedHistorialContraseniaCollection = new ArrayList<HistorialContrasenia>();
            for (HistorialContrasenia historialContraseniaCollectionHistorialContraseniaToAttach : usuario.getHistorialContraseniaCollection()) {
                historialContraseniaCollectionHistorialContraseniaToAttach = em.getReference(historialContraseniaCollectionHistorialContraseniaToAttach.getClass(), historialContraseniaCollectionHistorialContraseniaToAttach.getId());
                attachedHistorialContraseniaCollection.add(historialContraseniaCollectionHistorialContraseniaToAttach);
            }
            usuario.setHistorialContraseniaCollection(attachedHistorialContraseniaCollection);
            em.persist(usuario);
            if (personaid != null) {
                personaid.getUsuarioCollection().add(usuario);
                personaid = em.merge(personaid);
            }
            for (HistorialContrasenia historialContraseniaCollectionHistorialContrasenia : usuario.getHistorialContraseniaCollection()) {
                Usuario oldUsuarioidOfHistorialContraseniaCollectionHistorialContrasenia = historialContraseniaCollectionHistorialContrasenia.getUsuarioid();
                historialContraseniaCollectionHistorialContrasenia.setUsuarioid(usuario);
                historialContraseniaCollectionHistorialContrasenia = em.merge(historialContraseniaCollectionHistorialContrasenia);
                if (oldUsuarioidOfHistorialContraseniaCollectionHistorialContrasenia != null) {
                    oldUsuarioidOfHistorialContraseniaCollectionHistorialContrasenia.getHistorialContraseniaCollection().remove(historialContraseniaCollectionHistorialContrasenia);
                    oldUsuarioidOfHistorialContraseniaCollectionHistorialContrasenia = em.merge(oldUsuarioidOfHistorialContraseniaCollectionHistorialContrasenia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getId()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Persona personaidOld = persistentUsuario.getPersonaid();
            Persona personaidNew = usuario.getPersonaid();
            Collection<HistorialContrasenia> historialContraseniaCollectionOld = persistentUsuario.getHistorialContraseniaCollection();
            Collection<HistorialContrasenia> historialContraseniaCollectionNew = usuario.getHistorialContraseniaCollection();
            List<String> illegalOrphanMessages = null;
            for (HistorialContrasenia historialContraseniaCollectionOldHistorialContrasenia : historialContraseniaCollectionOld) {
                if (!historialContraseniaCollectionNew.contains(historialContraseniaCollectionOldHistorialContrasenia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistorialContrasenia " + historialContraseniaCollectionOldHistorialContrasenia + " since its usuarioid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personaidNew != null) {
                personaidNew = em.getReference(personaidNew.getClass(), personaidNew.getId());
                usuario.setPersonaid(personaidNew);
            }
            Collection<HistorialContrasenia> attachedHistorialContraseniaCollectionNew = new ArrayList<HistorialContrasenia>();
            for (HistorialContrasenia historialContraseniaCollectionNewHistorialContraseniaToAttach : historialContraseniaCollectionNew) {
                historialContraseniaCollectionNewHistorialContraseniaToAttach = em.getReference(historialContraseniaCollectionNewHistorialContraseniaToAttach.getClass(), historialContraseniaCollectionNewHistorialContraseniaToAttach.getId());
                attachedHistorialContraseniaCollectionNew.add(historialContraseniaCollectionNewHistorialContraseniaToAttach);
            }
            historialContraseniaCollectionNew = attachedHistorialContraseniaCollectionNew;
            usuario.setHistorialContraseniaCollection(historialContraseniaCollectionNew);
            usuario = em.merge(usuario);
            if (personaidOld != null && !personaidOld.equals(personaidNew)) {
                personaidOld.getUsuarioCollection().remove(usuario);
                personaidOld = em.merge(personaidOld);
            }
            if (personaidNew != null && !personaidNew.equals(personaidOld)) {
                personaidNew.getUsuarioCollection().add(usuario);
                personaidNew = em.merge(personaidNew);
            }
            for (HistorialContrasenia historialContraseniaCollectionNewHistorialContrasenia : historialContraseniaCollectionNew) {
                if (!historialContraseniaCollectionOld.contains(historialContraseniaCollectionNewHistorialContrasenia)) {
                    Usuario oldUsuarioidOfHistorialContraseniaCollectionNewHistorialContrasenia = historialContraseniaCollectionNewHistorialContrasenia.getUsuarioid();
                    historialContraseniaCollectionNewHistorialContrasenia.setUsuarioid(usuario);
                    historialContraseniaCollectionNewHistorialContrasenia = em.merge(historialContraseniaCollectionNewHistorialContrasenia);
                    if (oldUsuarioidOfHistorialContraseniaCollectionNewHistorialContrasenia != null && !oldUsuarioidOfHistorialContraseniaCollectionNewHistorialContrasenia.equals(usuario)) {
                        oldUsuarioidOfHistorialContraseniaCollectionNewHistorialContrasenia.getHistorialContraseniaCollection().remove(historialContraseniaCollectionNewHistorialContrasenia);
                        oldUsuarioidOfHistorialContraseniaCollectionNewHistorialContrasenia = em.merge(oldUsuarioidOfHistorialContraseniaCollectionNewHistorialContrasenia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<HistorialContrasenia> historialContraseniaCollectionOrphanCheck = usuario.getHistorialContraseniaCollection();
            for (HistorialContrasenia historialContraseniaCollectionOrphanCheckHistorialContrasenia : historialContraseniaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the HistorialContrasenia " + historialContraseniaCollectionOrphanCheckHistorialContrasenia + " in its historialContraseniaCollection field has a non-nullable usuarioid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona personaid = usuario.getPersonaid();
            if (personaid != null) {
                personaid.getUsuarioCollection().remove(usuario);
                personaid = em.merge(personaid);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

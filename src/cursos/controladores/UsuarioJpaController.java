/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.controladores;
import Principal.exceptions.IllegalOrphanException; // Excepciones del principal
import Principal.exceptions.NonexistentEntityException; // Excepciones del principal
import Principal.exceptions.PreexistingEntityException;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cursos.percistence.Asignados;
import java.util.ArrayList;
import java.util.Collection;
import cursos.percistence.Entrega;
import cursos.percistence.SeccionCursos;
import cursos.percistence.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablito Garzona
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
        if (usuario.getAsignadosCollection() == null) {
            usuario.setAsignadosCollection(new ArrayList<Asignados>());
        }
        if (usuario.getEntregaCollection() == null) {
            usuario.setEntregaCollection(new ArrayList<Entrega>());
        }
        if (usuario.getSeccionCursosCollection() == null) {
            usuario.setSeccionCursosCollection(new ArrayList<SeccionCursos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Asignados> attachedAsignadosCollection = new ArrayList<Asignados>();
            for (Asignados asignadosCollectionAsignadosToAttach : usuario.getAsignadosCollection()) {
                asignadosCollectionAsignadosToAttach = em.getReference(asignadosCollectionAsignadosToAttach.getClass(), asignadosCollectionAsignadosToAttach.getId());
                attachedAsignadosCollection.add(asignadosCollectionAsignadosToAttach);
            }
            usuario.setAsignadosCollection(attachedAsignadosCollection);
            Collection<Entrega> attachedEntregaCollection = new ArrayList<Entrega>();
            for (Entrega entregaCollectionEntregaToAttach : usuario.getEntregaCollection()) {
                entregaCollectionEntregaToAttach = em.getReference(entregaCollectionEntregaToAttach.getClass(), entregaCollectionEntregaToAttach.getId());
                attachedEntregaCollection.add(entregaCollectionEntregaToAttach);
            }
            usuario.setEntregaCollection(attachedEntregaCollection);
            Collection<SeccionCursos> attachedSeccionCursosCollection = new ArrayList<SeccionCursos>();
            for (SeccionCursos seccionCursosCollectionSeccionCursosToAttach : usuario.getSeccionCursosCollection()) {
                seccionCursosCollectionSeccionCursosToAttach = em.getReference(seccionCursosCollectionSeccionCursosToAttach.getClass(), seccionCursosCollectionSeccionCursosToAttach.getId());
                attachedSeccionCursosCollection.add(seccionCursosCollectionSeccionCursosToAttach);
            }
            usuario.setSeccionCursosCollection(attachedSeccionCursosCollection);
            em.persist(usuario);
            for (Asignados asignadosCollectionAsignados : usuario.getAsignadosCollection()) {
                Usuario oldUsuarioidOfAsignadosCollectionAsignados = asignadosCollectionAsignados.getUsuarioid();
                asignadosCollectionAsignados.setUsuarioid(usuario);
                asignadosCollectionAsignados = em.merge(asignadosCollectionAsignados);
                if (oldUsuarioidOfAsignadosCollectionAsignados != null) {
                    oldUsuarioidOfAsignadosCollectionAsignados.getAsignadosCollection().remove(asignadosCollectionAsignados);
                    oldUsuarioidOfAsignadosCollectionAsignados = em.merge(oldUsuarioidOfAsignadosCollectionAsignados);
                }
            }
            for (Entrega entregaCollectionEntrega : usuario.getEntregaCollection()) {
                Usuario oldUsuarioidOfEntregaCollectionEntrega = entregaCollectionEntrega.getUsuarioid();
                entregaCollectionEntrega.setUsuarioid(usuario);
                entregaCollectionEntrega = em.merge(entregaCollectionEntrega);
                if (oldUsuarioidOfEntregaCollectionEntrega != null) {
                    oldUsuarioidOfEntregaCollectionEntrega.getEntregaCollection().remove(entregaCollectionEntrega);
                    oldUsuarioidOfEntregaCollectionEntrega = em.merge(oldUsuarioidOfEntregaCollectionEntrega);
                }
            }
            for (SeccionCursos seccionCursosCollectionSeccionCursos : usuario.getSeccionCursosCollection()) {
                Usuario oldCatedraticoidOfSeccionCursosCollectionSeccionCursos = seccionCursosCollectionSeccionCursos.getCatedraticoid();
                seccionCursosCollectionSeccionCursos.setCatedraticoid(usuario);
                seccionCursosCollectionSeccionCursos = em.merge(seccionCursosCollectionSeccionCursos);
                if (oldCatedraticoidOfSeccionCursosCollectionSeccionCursos != null) {
                    oldCatedraticoidOfSeccionCursosCollectionSeccionCursos.getSeccionCursosCollection().remove(seccionCursosCollectionSeccionCursos);
                    oldCatedraticoidOfSeccionCursosCollectionSeccionCursos = em.merge(oldCatedraticoidOfSeccionCursosCollectionSeccionCursos);
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
            Collection<Asignados> asignadosCollectionOld = persistentUsuario.getAsignadosCollection();
            Collection<Asignados> asignadosCollectionNew = usuario.getAsignadosCollection();
            Collection<Entrega> entregaCollectionOld = persistentUsuario.getEntregaCollection();
            Collection<Entrega> entregaCollectionNew = usuario.getEntregaCollection();
            Collection<SeccionCursos> seccionCursosCollectionOld = persistentUsuario.getSeccionCursosCollection();
            Collection<SeccionCursos> seccionCursosCollectionNew = usuario.getSeccionCursosCollection();
            List<String> illegalOrphanMessages = null;
            for (Asignados asignadosCollectionOldAsignados : asignadosCollectionOld) {
                if (!asignadosCollectionNew.contains(asignadosCollectionOldAsignados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asignados " + asignadosCollectionOldAsignados + " since its usuarioid field is not nullable.");
                }
            }
            for (Entrega entregaCollectionOldEntrega : entregaCollectionOld) {
                if (!entregaCollectionNew.contains(entregaCollectionOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaCollectionOldEntrega + " since its usuarioid field is not nullable.");
                }
            }
            for (SeccionCursos seccionCursosCollectionOldSeccionCursos : seccionCursosCollectionOld) {
                if (!seccionCursosCollectionNew.contains(seccionCursosCollectionOldSeccionCursos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionCursos " + seccionCursosCollectionOldSeccionCursos + " since its catedraticoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Asignados> attachedAsignadosCollectionNew = new ArrayList<Asignados>();
            for (Asignados asignadosCollectionNewAsignadosToAttach : asignadosCollectionNew) {
                asignadosCollectionNewAsignadosToAttach = em.getReference(asignadosCollectionNewAsignadosToAttach.getClass(), asignadosCollectionNewAsignadosToAttach.getId());
                attachedAsignadosCollectionNew.add(asignadosCollectionNewAsignadosToAttach);
            }
            asignadosCollectionNew = attachedAsignadosCollectionNew;
            usuario.setAsignadosCollection(asignadosCollectionNew);
            Collection<Entrega> attachedEntregaCollectionNew = new ArrayList<Entrega>();
            for (Entrega entregaCollectionNewEntregaToAttach : entregaCollectionNew) {
                entregaCollectionNewEntregaToAttach = em.getReference(entregaCollectionNewEntregaToAttach.getClass(), entregaCollectionNewEntregaToAttach.getId());
                attachedEntregaCollectionNew.add(entregaCollectionNewEntregaToAttach);
            }
            entregaCollectionNew = attachedEntregaCollectionNew;
            usuario.setEntregaCollection(entregaCollectionNew);
            Collection<SeccionCursos> attachedSeccionCursosCollectionNew = new ArrayList<SeccionCursos>();
            for (SeccionCursos seccionCursosCollectionNewSeccionCursosToAttach : seccionCursosCollectionNew) {
                seccionCursosCollectionNewSeccionCursosToAttach = em.getReference(seccionCursosCollectionNewSeccionCursosToAttach.getClass(), seccionCursosCollectionNewSeccionCursosToAttach.getId());
                attachedSeccionCursosCollectionNew.add(seccionCursosCollectionNewSeccionCursosToAttach);
            }
            seccionCursosCollectionNew = attachedSeccionCursosCollectionNew;
            usuario.setSeccionCursosCollection(seccionCursosCollectionNew);
            usuario = em.merge(usuario);
            for (Asignados asignadosCollectionNewAsignados : asignadosCollectionNew) {
                if (!asignadosCollectionOld.contains(asignadosCollectionNewAsignados)) {
                    Usuario oldUsuarioidOfAsignadosCollectionNewAsignados = asignadosCollectionNewAsignados.getUsuarioid();
                    asignadosCollectionNewAsignados.setUsuarioid(usuario);
                    asignadosCollectionNewAsignados = em.merge(asignadosCollectionNewAsignados);
                    if (oldUsuarioidOfAsignadosCollectionNewAsignados != null && !oldUsuarioidOfAsignadosCollectionNewAsignados.equals(usuario)) {
                        oldUsuarioidOfAsignadosCollectionNewAsignados.getAsignadosCollection().remove(asignadosCollectionNewAsignados);
                        oldUsuarioidOfAsignadosCollectionNewAsignados = em.merge(oldUsuarioidOfAsignadosCollectionNewAsignados);
                    }
                }
            }
            for (Entrega entregaCollectionNewEntrega : entregaCollectionNew) {
                if (!entregaCollectionOld.contains(entregaCollectionNewEntrega)) {
                    Usuario oldUsuarioidOfEntregaCollectionNewEntrega = entregaCollectionNewEntrega.getUsuarioid();
                    entregaCollectionNewEntrega.setUsuarioid(usuario);
                    entregaCollectionNewEntrega = em.merge(entregaCollectionNewEntrega);
                    if (oldUsuarioidOfEntregaCollectionNewEntrega != null && !oldUsuarioidOfEntregaCollectionNewEntrega.equals(usuario)) {
                        oldUsuarioidOfEntregaCollectionNewEntrega.getEntregaCollection().remove(entregaCollectionNewEntrega);
                        oldUsuarioidOfEntregaCollectionNewEntrega = em.merge(oldUsuarioidOfEntregaCollectionNewEntrega);
                    }
                }
            }
            for (SeccionCursos seccionCursosCollectionNewSeccionCursos : seccionCursosCollectionNew) {
                if (!seccionCursosCollectionOld.contains(seccionCursosCollectionNewSeccionCursos)) {
                    Usuario oldCatedraticoidOfSeccionCursosCollectionNewSeccionCursos = seccionCursosCollectionNewSeccionCursos.getCatedraticoid();
                    seccionCursosCollectionNewSeccionCursos.setCatedraticoid(usuario);
                    seccionCursosCollectionNewSeccionCursos = em.merge(seccionCursosCollectionNewSeccionCursos);
                    if (oldCatedraticoidOfSeccionCursosCollectionNewSeccionCursos != null && !oldCatedraticoidOfSeccionCursosCollectionNewSeccionCursos.equals(usuario)) {
                        oldCatedraticoidOfSeccionCursosCollectionNewSeccionCursos.getSeccionCursosCollection().remove(seccionCursosCollectionNewSeccionCursos);
                        oldCatedraticoidOfSeccionCursosCollectionNewSeccionCursos = em.merge(oldCatedraticoidOfSeccionCursosCollectionNewSeccionCursos);
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
            Collection<Asignados> asignadosCollectionOrphanCheck = usuario.getAsignadosCollection();
            for (Asignados asignadosCollectionOrphanCheckAsignados : asignadosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Asignados " + asignadosCollectionOrphanCheckAsignados + " in its asignadosCollection field has a non-nullable usuarioid field.");
            }
            Collection<Entrega> entregaCollectionOrphanCheck = usuario.getEntregaCollection();
            for (Entrega entregaCollectionOrphanCheckEntrega : entregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Entrega " + entregaCollectionOrphanCheckEntrega + " in its entregaCollection field has a non-nullable usuarioid field.");
            }
            Collection<SeccionCursos> seccionCursosCollectionOrphanCheck = usuario.getSeccionCursosCollection();
            for (SeccionCursos seccionCursosCollectionOrphanCheckSeccionCursos : seccionCursosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the SeccionCursos " + seccionCursosCollectionOrphanCheckSeccionCursos + " in its seccionCursosCollection field has a non-nullable catedraticoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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

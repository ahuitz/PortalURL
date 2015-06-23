/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Tablas.Contacto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Tablas.Municipio;
import Tablas.Pais;
import Tablas.Persona;
import Tablas.Correo;
import java.util.ArrayList;
import java.util.Collection;
import Tablas.Idioma;
import Tablas.Telefono;
import Tablas.DatosLaborales;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Geek
 */
public class ContactoJpaController implements Serializable {

    public ContactoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contacto contacto) {
        if (contacto.getCorreoCollection() == null) {
            contacto.setCorreoCollection(new ArrayList<Correo>());
        }
        if (contacto.getIdiomaCollection() == null) {
            contacto.setIdiomaCollection(new ArrayList<Idioma>());
        }
        if (contacto.getTelefonoCollection() == null) {
            contacto.setTelefonoCollection(new ArrayList<Telefono>());
        }
        if (contacto.getDatosLaboralesCollection() == null) {
            contacto.setDatosLaboralesCollection(new ArrayList<DatosLaborales>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio municipioid = contacto.getMunicipioid();
            if (municipioid != null) {
                municipioid = em.getReference(municipioid.getClass(), municipioid.getId());
                contacto.setMunicipioid(municipioid);
            }
            Pais paisid = contacto.getPaisid();
            if (paisid != null) {
                paisid = em.getReference(paisid.getClass(), paisid.getId());
                contacto.setPaisid(paisid);
            }
            Persona personaid = contacto.getPersonaid();
            if (personaid != null) {
                personaid = em.getReference(personaid.getClass(), personaid.getId());
                contacto.setPersonaid(personaid);
            }
            Collection<Correo> attachedCorreoCollection = new ArrayList<Correo>();
            for (Correo correoCollectionCorreoToAttach : contacto.getCorreoCollection()) {
                correoCollectionCorreoToAttach = em.getReference(correoCollectionCorreoToAttach.getClass(), correoCollectionCorreoToAttach.getId());
                attachedCorreoCollection.add(correoCollectionCorreoToAttach);
            }
            contacto.setCorreoCollection(attachedCorreoCollection);
            Collection<Idioma> attachedIdiomaCollection = new ArrayList<Idioma>();
            for (Idioma idiomaCollectionIdiomaToAttach : contacto.getIdiomaCollection()) {
                idiomaCollectionIdiomaToAttach = em.getReference(idiomaCollectionIdiomaToAttach.getClass(), idiomaCollectionIdiomaToAttach.getId());
                attachedIdiomaCollection.add(idiomaCollectionIdiomaToAttach);
            }
            contacto.setIdiomaCollection(attachedIdiomaCollection);
            Collection<Telefono> attachedTelefonoCollection = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionTelefonoToAttach : contacto.getTelefonoCollection()) {
                telefonoCollectionTelefonoToAttach = em.getReference(telefonoCollectionTelefonoToAttach.getClass(), telefonoCollectionTelefonoToAttach.getId());
                attachedTelefonoCollection.add(telefonoCollectionTelefonoToAttach);
            }
            contacto.setTelefonoCollection(attachedTelefonoCollection);
            Collection<DatosLaborales> attachedDatosLaboralesCollection = new ArrayList<DatosLaborales>();
            for (DatosLaborales datosLaboralesCollectionDatosLaboralesToAttach : contacto.getDatosLaboralesCollection()) {
                datosLaboralesCollectionDatosLaboralesToAttach = em.getReference(datosLaboralesCollectionDatosLaboralesToAttach.getClass(), datosLaboralesCollectionDatosLaboralesToAttach.getId());
                attachedDatosLaboralesCollection.add(datosLaboralesCollectionDatosLaboralesToAttach);
            }
            contacto.setDatosLaboralesCollection(attachedDatosLaboralesCollection);
            em.persist(contacto);
            if (municipioid != null) {
                municipioid.getContactoCollection().add(contacto);
                municipioid = em.merge(municipioid);
            }
            if (paisid != null) {
                paisid.getContactoCollection().add(contacto);
                paisid = em.merge(paisid);
            }
            if (personaid != null) {
                personaid.getContactoCollection().add(contacto);
                personaid = em.merge(personaid);
            }
            for (Correo correoCollectionCorreo : contacto.getCorreoCollection()) {
                Contacto oldContactoidOfCorreoCollectionCorreo = correoCollectionCorreo.getContactoid();
                correoCollectionCorreo.setContactoid(contacto);
                correoCollectionCorreo = em.merge(correoCollectionCorreo);
                if (oldContactoidOfCorreoCollectionCorreo != null) {
                    oldContactoidOfCorreoCollectionCorreo.getCorreoCollection().remove(correoCollectionCorreo);
                    oldContactoidOfCorreoCollectionCorreo = em.merge(oldContactoidOfCorreoCollectionCorreo);
                }
            }
            for (Idioma idiomaCollectionIdioma : contacto.getIdiomaCollection()) {
                Contacto oldContactoidOfIdiomaCollectionIdioma = idiomaCollectionIdioma.getContactoid();
                idiomaCollectionIdioma.setContactoid(contacto);
                idiomaCollectionIdioma = em.merge(idiomaCollectionIdioma);
                if (oldContactoidOfIdiomaCollectionIdioma != null) {
                    oldContactoidOfIdiomaCollectionIdioma.getIdiomaCollection().remove(idiomaCollectionIdioma);
                    oldContactoidOfIdiomaCollectionIdioma = em.merge(oldContactoidOfIdiomaCollectionIdioma);
                }
            }
            for (Telefono telefonoCollectionTelefono : contacto.getTelefonoCollection()) {
                Contacto oldContactoidOfTelefonoCollectionTelefono = telefonoCollectionTelefono.getContactoid();
                telefonoCollectionTelefono.setContactoid(contacto);
                telefonoCollectionTelefono = em.merge(telefonoCollectionTelefono);
                if (oldContactoidOfTelefonoCollectionTelefono != null) {
                    oldContactoidOfTelefonoCollectionTelefono.getTelefonoCollection().remove(telefonoCollectionTelefono);
                    oldContactoidOfTelefonoCollectionTelefono = em.merge(oldContactoidOfTelefonoCollectionTelefono);
                }
            }
            for (DatosLaborales datosLaboralesCollectionDatosLaborales : contacto.getDatosLaboralesCollection()) {
                Contacto oldContactoidOfDatosLaboralesCollectionDatosLaborales = datosLaboralesCollectionDatosLaborales.getContactoid();
                datosLaboralesCollectionDatosLaborales.setContactoid(contacto);
                datosLaboralesCollectionDatosLaborales = em.merge(datosLaboralesCollectionDatosLaborales);
                if (oldContactoidOfDatosLaboralesCollectionDatosLaborales != null) {
                    oldContactoidOfDatosLaboralesCollectionDatosLaborales.getDatosLaboralesCollection().remove(datosLaboralesCollectionDatosLaborales);
                    oldContactoidOfDatosLaboralesCollectionDatosLaborales = em.merge(oldContactoidOfDatosLaboralesCollectionDatosLaborales);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacto contacto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto persistentContacto = em.find(Contacto.class, contacto.getId());
            Municipio municipioidOld = persistentContacto.getMunicipioid();
            Municipio municipioidNew = contacto.getMunicipioid();
            Pais paisidOld = persistentContacto.getPaisid();
            Pais paisidNew = contacto.getPaisid();
            Persona personaidOld = persistentContacto.getPersonaid();
            Persona personaidNew = contacto.getPersonaid();
            Collection<Correo> correoCollectionOld = persistentContacto.getCorreoCollection();
            Collection<Correo> correoCollectionNew = contacto.getCorreoCollection();
            Collection<Idioma> idiomaCollectionOld = persistentContacto.getIdiomaCollection();
            Collection<Idioma> idiomaCollectionNew = contacto.getIdiomaCollection();
            Collection<Telefono> telefonoCollectionOld = persistentContacto.getTelefonoCollection();
            Collection<Telefono> telefonoCollectionNew = contacto.getTelefonoCollection();
            Collection<DatosLaborales> datosLaboralesCollectionOld = persistentContacto.getDatosLaboralesCollection();
            Collection<DatosLaborales> datosLaboralesCollectionNew = contacto.getDatosLaboralesCollection();
            List<String> illegalOrphanMessages = null;
            for (Correo correoCollectionOldCorreo : correoCollectionOld) {
                if (!correoCollectionNew.contains(correoCollectionOldCorreo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Correo " + correoCollectionOldCorreo + " since its contactoid field is not nullable.");
                }
            }
            for (Idioma idiomaCollectionOldIdioma : idiomaCollectionOld) {
                if (!idiomaCollectionNew.contains(idiomaCollectionOldIdioma)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Idioma " + idiomaCollectionOldIdioma + " since its contactoid field is not nullable.");
                }
            }
            for (Telefono telefonoCollectionOldTelefono : telefonoCollectionOld) {
                if (!telefonoCollectionNew.contains(telefonoCollectionOldTelefono)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Telefono " + telefonoCollectionOldTelefono + " since its contactoid field is not nullable.");
                }
            }
            for (DatosLaborales datosLaboralesCollectionOldDatosLaborales : datosLaboralesCollectionOld) {
                if (!datosLaboralesCollectionNew.contains(datosLaboralesCollectionOldDatosLaborales)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DatosLaborales " + datosLaboralesCollectionOldDatosLaborales + " since its contactoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (municipioidNew != null) {
                municipioidNew = em.getReference(municipioidNew.getClass(), municipioidNew.getId());
                contacto.setMunicipioid(municipioidNew);
            }
            if (paisidNew != null) {
                paisidNew = em.getReference(paisidNew.getClass(), paisidNew.getId());
                contacto.setPaisid(paisidNew);
            }
            if (personaidNew != null) {
                personaidNew = em.getReference(personaidNew.getClass(), personaidNew.getId());
                contacto.setPersonaid(personaidNew);
            }
            Collection<Correo> attachedCorreoCollectionNew = new ArrayList<Correo>();
            for (Correo correoCollectionNewCorreoToAttach : correoCollectionNew) {
                correoCollectionNewCorreoToAttach = em.getReference(correoCollectionNewCorreoToAttach.getClass(), correoCollectionNewCorreoToAttach.getId());
                attachedCorreoCollectionNew.add(correoCollectionNewCorreoToAttach);
            }
            correoCollectionNew = attachedCorreoCollectionNew;
            contacto.setCorreoCollection(correoCollectionNew);
            Collection<Idioma> attachedIdiomaCollectionNew = new ArrayList<Idioma>();
            for (Idioma idiomaCollectionNewIdiomaToAttach : idiomaCollectionNew) {
                idiomaCollectionNewIdiomaToAttach = em.getReference(idiomaCollectionNewIdiomaToAttach.getClass(), idiomaCollectionNewIdiomaToAttach.getId());
                attachedIdiomaCollectionNew.add(idiomaCollectionNewIdiomaToAttach);
            }
            idiomaCollectionNew = attachedIdiomaCollectionNew;
            contacto.setIdiomaCollection(idiomaCollectionNew);
            Collection<Telefono> attachedTelefonoCollectionNew = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionNewTelefonoToAttach : telefonoCollectionNew) {
                telefonoCollectionNewTelefonoToAttach = em.getReference(telefonoCollectionNewTelefonoToAttach.getClass(), telefonoCollectionNewTelefonoToAttach.getId());
                attachedTelefonoCollectionNew.add(telefonoCollectionNewTelefonoToAttach);
            }
            telefonoCollectionNew = attachedTelefonoCollectionNew;
            contacto.setTelefonoCollection(telefonoCollectionNew);
            Collection<DatosLaborales> attachedDatosLaboralesCollectionNew = new ArrayList<DatosLaborales>();
            for (DatosLaborales datosLaboralesCollectionNewDatosLaboralesToAttach : datosLaboralesCollectionNew) {
                datosLaboralesCollectionNewDatosLaboralesToAttach = em.getReference(datosLaboralesCollectionNewDatosLaboralesToAttach.getClass(), datosLaboralesCollectionNewDatosLaboralesToAttach.getId());
                attachedDatosLaboralesCollectionNew.add(datosLaboralesCollectionNewDatosLaboralesToAttach);
            }
            datosLaboralesCollectionNew = attachedDatosLaboralesCollectionNew;
            contacto.setDatosLaboralesCollection(datosLaboralesCollectionNew);
            contacto = em.merge(contacto);
            if (municipioidOld != null && !municipioidOld.equals(municipioidNew)) {
                municipioidOld.getContactoCollection().remove(contacto);
                municipioidOld = em.merge(municipioidOld);
            }
            if (municipioidNew != null && !municipioidNew.equals(municipioidOld)) {
                municipioidNew.getContactoCollection().add(contacto);
                municipioidNew = em.merge(municipioidNew);
            }
            if (paisidOld != null && !paisidOld.equals(paisidNew)) {
                paisidOld.getContactoCollection().remove(contacto);
                paisidOld = em.merge(paisidOld);
            }
            if (paisidNew != null && !paisidNew.equals(paisidOld)) {
                paisidNew.getContactoCollection().add(contacto);
                paisidNew = em.merge(paisidNew);
            }
            if (personaidOld != null && !personaidOld.equals(personaidNew)) {
                personaidOld.getContactoCollection().remove(contacto);
                personaidOld = em.merge(personaidOld);
            }
            if (personaidNew != null && !personaidNew.equals(personaidOld)) {
                personaidNew.getContactoCollection().add(contacto);
                personaidNew = em.merge(personaidNew);
            }
            for (Correo correoCollectionNewCorreo : correoCollectionNew) {
                if (!correoCollectionOld.contains(correoCollectionNewCorreo)) {
                    Contacto oldContactoidOfCorreoCollectionNewCorreo = correoCollectionNewCorreo.getContactoid();
                    correoCollectionNewCorreo.setContactoid(contacto);
                    correoCollectionNewCorreo = em.merge(correoCollectionNewCorreo);
                    if (oldContactoidOfCorreoCollectionNewCorreo != null && !oldContactoidOfCorreoCollectionNewCorreo.equals(contacto)) {
                        oldContactoidOfCorreoCollectionNewCorreo.getCorreoCollection().remove(correoCollectionNewCorreo);
                        oldContactoidOfCorreoCollectionNewCorreo = em.merge(oldContactoidOfCorreoCollectionNewCorreo);
                    }
                }
            }
            for (Idioma idiomaCollectionNewIdioma : idiomaCollectionNew) {
                if (!idiomaCollectionOld.contains(idiomaCollectionNewIdioma)) {
                    Contacto oldContactoidOfIdiomaCollectionNewIdioma = idiomaCollectionNewIdioma.getContactoid();
                    idiomaCollectionNewIdioma.setContactoid(contacto);
                    idiomaCollectionNewIdioma = em.merge(idiomaCollectionNewIdioma);
                    if (oldContactoidOfIdiomaCollectionNewIdioma != null && !oldContactoidOfIdiomaCollectionNewIdioma.equals(contacto)) {
                        oldContactoidOfIdiomaCollectionNewIdioma.getIdiomaCollection().remove(idiomaCollectionNewIdioma);
                        oldContactoidOfIdiomaCollectionNewIdioma = em.merge(oldContactoidOfIdiomaCollectionNewIdioma);
                    }
                }
            }
            for (Telefono telefonoCollectionNewTelefono : telefonoCollectionNew) {
                if (!telefonoCollectionOld.contains(telefonoCollectionNewTelefono)) {
                    Contacto oldContactoidOfTelefonoCollectionNewTelefono = telefonoCollectionNewTelefono.getContactoid();
                    telefonoCollectionNewTelefono.setContactoid(contacto);
                    telefonoCollectionNewTelefono = em.merge(telefonoCollectionNewTelefono);
                    if (oldContactoidOfTelefonoCollectionNewTelefono != null && !oldContactoidOfTelefonoCollectionNewTelefono.equals(contacto)) {
                        oldContactoidOfTelefonoCollectionNewTelefono.getTelefonoCollection().remove(telefonoCollectionNewTelefono);
                        oldContactoidOfTelefonoCollectionNewTelefono = em.merge(oldContactoidOfTelefonoCollectionNewTelefono);
                    }
                }
            }
            for (DatosLaborales datosLaboralesCollectionNewDatosLaborales : datosLaboralesCollectionNew) {
                if (!datosLaboralesCollectionOld.contains(datosLaboralesCollectionNewDatosLaborales)) {
                    Contacto oldContactoidOfDatosLaboralesCollectionNewDatosLaborales = datosLaboralesCollectionNewDatosLaborales.getContactoid();
                    datosLaboralesCollectionNewDatosLaborales.setContactoid(contacto);
                    datosLaboralesCollectionNewDatosLaborales = em.merge(datosLaboralesCollectionNewDatosLaborales);
                    if (oldContactoidOfDatosLaboralesCollectionNewDatosLaborales != null && !oldContactoidOfDatosLaboralesCollectionNewDatosLaborales.equals(contacto)) {
                        oldContactoidOfDatosLaboralesCollectionNewDatosLaborales.getDatosLaboralesCollection().remove(datosLaboralesCollectionNewDatosLaborales);
                        oldContactoidOfDatosLaboralesCollectionNewDatosLaborales = em.merge(oldContactoidOfDatosLaboralesCollectionNewDatosLaborales);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contacto.getId();
                if (findContacto(id) == null) {
                    throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.");
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
            Contacto contacto;
            try {
                contacto = em.getReference(Contacto.class, id);
                contacto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Correo> correoCollectionOrphanCheck = contacto.getCorreoCollection();
            for (Correo correoCollectionOrphanCheckCorreo : correoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the Correo " + correoCollectionOrphanCheckCorreo + " in its correoCollection field has a non-nullable contactoid field.");
            }
            Collection<Idioma> idiomaCollectionOrphanCheck = contacto.getIdiomaCollection();
            for (Idioma idiomaCollectionOrphanCheckIdioma : idiomaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the Idioma " + idiomaCollectionOrphanCheckIdioma + " in its idiomaCollection field has a non-nullable contactoid field.");
            }
            Collection<Telefono> telefonoCollectionOrphanCheck = contacto.getTelefonoCollection();
            for (Telefono telefonoCollectionOrphanCheckTelefono : telefonoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the Telefono " + telefonoCollectionOrphanCheckTelefono + " in its telefonoCollection field has a non-nullable contactoid field.");
            }
            Collection<DatosLaborales> datosLaboralesCollectionOrphanCheck = contacto.getDatosLaboralesCollection();
            for (DatosLaborales datosLaboralesCollectionOrphanCheckDatosLaborales : datosLaboralesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the DatosLaborales " + datosLaboralesCollectionOrphanCheckDatosLaborales + " in its datosLaboralesCollection field has a non-nullable contactoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Municipio municipioid = contacto.getMunicipioid();
            if (municipioid != null) {
                municipioid.getContactoCollection().remove(contacto);
                municipioid = em.merge(municipioid);
            }
            Pais paisid = contacto.getPaisid();
            if (paisid != null) {
                paisid.getContactoCollection().remove(contacto);
                paisid = em.merge(paisid);
            }
            Persona personaid = contacto.getPersonaid();
            if (personaid != null) {
                personaid.getContactoCollection().remove(contacto);
                personaid = em.merge(personaid);
            }
            em.remove(contacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contacto> findContactoEntities() {
        return findContactoEntities(true, -1, -1);
    }

    public List<Contacto> findContactoEntities(int maxResults, int firstResult) {
        return findContactoEntities(false, maxResults, firstResult);
    }

    private List<Contacto> findContactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacto.class));
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

    public Contacto findContacto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacto> rt = cq.from(Contacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

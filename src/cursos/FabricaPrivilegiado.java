/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo López
 */
public class FabricaPrivilegiado implements FabricaAbstracta {

    @Override
    public Tarea crearTarea(EntityManagerFactory emf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Recurso crearRecurso(EntityManagerFactory emf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package cursos.conexion;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author EST1629311
 */
public abstract class Conexion {
    private EntityManagerFactory emf;
   
    protected void iniciarConexion(String persistenceUnitName){
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEm(EntityManagerFactory emf) {
        this.emf = emf;
    }    
}

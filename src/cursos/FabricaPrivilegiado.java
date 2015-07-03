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
    public Actividad crearActividad(EntityManagerFactory emf) {
        return new A_Privilegiado(emf);
    }

    @Override
    public Recurso crearRecurso(EntityManagerFactory emf) {
        return new R_Privilegiado(emf);
    }
}

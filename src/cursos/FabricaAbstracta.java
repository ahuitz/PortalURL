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
public interface FabricaAbstracta {
    public Tarea crearTarea(EntityManagerFactory emf);
    public Recurso crearRecurso(EntityManagerFactory emf);
}

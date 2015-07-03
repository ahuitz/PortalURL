/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

/**
 *
 * @author Pablo López
 */
public interface FabricaAbstracta {
    public Tarea crearTarea(Object emf);
    public Recurso crearRecurso(Object emf);
}

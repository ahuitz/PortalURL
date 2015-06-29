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
public class FabricaPrivilegiado implements FabricaAbstracta {

    @Override
    public Tarea crearTarea() {
        return new T_Privilegiado();
    }

    @Override
    public Recurso crearRecurso() {
        return new R_Privilegiado();
    }
    
}

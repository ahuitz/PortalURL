/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

/**
 *
 * @author RealG4Life
 */
public class FabricaNoPrivilegiado implements FabricaAbstracta{

    @Override
    public Tarea crearTarea(Object emf) {
        T_NoPrivilegiado t=new T_NoPrivilegiado();
        t.controlador=emf;
        return t;
    }

    @Override
    public Recurso crearRecurso(Object emf) {
        R_NoPrivilegiado r = new R_NoPrivilegiado();
        r.controlador=emf;
        return r;
    }
    
    
    
}

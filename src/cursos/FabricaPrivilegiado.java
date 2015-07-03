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

    public FabricaPrivilegiado() {
    }

    @Override
    public Tarea crearTarea(Object emf) {
        T_Privilegiado t=new T_Privilegiado();
        t.controlador=emf;
        return t;
    }

    @Override
    public Recurso crearRecurso(Object emf) {
        R_Privilegiado r = new R_Privilegiado();
        r.controlador=emf;
        return r;
    }
    
}

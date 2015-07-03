/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import java.util.ArrayList;

/**
 *
 * @author RealG4Life
 */
public class A_NoPrivilegiado extends Actividad{

    public A_NoPrivilegiado(Object controlador) {
        this.controlador = controlador;
        this.actividades= new ArrayList<>();
    }
    
    @Override
    public void visualizar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void entregar(){
    }
    
    public void modificar(){
    }
    
}

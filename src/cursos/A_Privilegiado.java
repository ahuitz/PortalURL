/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import java.util.ArrayList;

/**
 *
 * @author Pablo López
 */
public class A_Privilegiado extends Actividad{

    public A_Privilegiado(Object controlador) {
        this.controlador = controlador;
        this.actividades= new ArrayList<>();
    }
    
    public void publicar(){
        
    }
    
    public void modificar(){
        
    }
    
    public void calificar(){
        
    }
    
    @Override
    public void visualizar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructura;

/**
 *
 * @author Vader33
 */
public class Creador {
   private Persona nuevo;
    
    public void setPersona(Persona p){
        this.nuevo=p;
    }
    
    public void crearUsuario(Conexion conexion,String Nombre, String Contra){
      this.nuevo.crear(Nombre,Contra,conexion);
    }
    
}

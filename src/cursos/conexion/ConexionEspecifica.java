/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos.conexion;
/**
 *
 * @author EST1629311
 */
public class ConexionEspecifica extends Conexion{
    private static ConexionEspecifica unicaConexion;

  
    private ConexionEspecifica(String persistenceUnitName) {
        iniciarConexion(persistenceUnitName);  
    }
    
    public static ConexionEspecifica getConexionEspecifica(String persistenceUnitName){
        if(unicaConexion == null){
            unicaConexion=new ConexionEspecifica(persistenceUnitName);
        }
        return unicaConexion;
    }
}

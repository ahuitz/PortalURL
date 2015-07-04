/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructura;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Vader33
 */
public class Conexion {
    
    private String Nombre;
    private String Pass;
    private Map prop = new HashMap();
    public EntityManagerFactory emf;
    public EntityManager em;
    private static Conexion instancia= null;


    private Conexion(String Name,String Password) {
        this.Nombre=Name;
        this.Pass=Password;
        
        prop.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/Url");
        prop.put("javax.persistence.jdbc.password", Pass);
        prop.put("javax.persistence.jdbc.user", Nombre);
        
       emf= Persistence.createEntityManagerFactory("JPAusuario",prop);
       em = emf.createEntityManager();
           System.out.println("Yeah");
    }
       
    
    public static Conexion getInstance(String na, String pa){
        if(instancia==null){
            instancia = new Conexion(na,pa);
        }
            return instancia;   
        }

   
}

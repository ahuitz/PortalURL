/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructura;

import javax.persistence.Query;

/**
 *
 * @author Vader33
 */
public class Cat implements Persona{

    @Override
    public void crear(String name, String pass, Conexion con) {
               Query crear = con.em.createNativeQuery("create user '"+"CAT"+name+"'@'localhost' identified by '"+pass+"';");

        
    }
    
}

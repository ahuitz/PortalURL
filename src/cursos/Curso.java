/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import cursos.conexion.ConexionEspecifica;

/**
 *
 * @author RealG4Life
 */
public class Curso {
    ConexionEspecifica c;
    FabricaNoPrivilegiado fabricaNP;
    FabricaPrivilegiado fabricaP;
    Recurso recurso;
    Tarea tarea;

    public Curso(Object Usuario, int id_Curso) {
        c=ConexionEspecifica.getConexionEspecifica("PortalUrlPU");
        
    }
    
    
}

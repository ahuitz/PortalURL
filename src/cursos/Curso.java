/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import cursos.conexion.ConexionEspecifica;
import java.util.Objects;

/**
 *
 * @author RealG4Life
 */
public class Curso {
    ConexionEspecifica c;
    FabricaNoPrivilegiado fabricaNP;
    FabricaPrivilegiado fabricaP;
    Recurso recurso;
    Actividad actividad;

    public Curso(Object Usuario, int id_Curso, Boolean aPrivilegiado, Boolean rPrivilegiado) {
        c=ConexionEspecifica.getConexionEspecifica("PortalUrlPU");
        if(Objects.equals(aPrivilegiado, Boolean.TRUE)&&Objects.equals(rPrivilegiado, Boolean.TRUE)){
            fabricaP=new FabricaPrivilegiado();
            actividad=fabricaP.crearActividad(c.getEmf());
            recurso=fabricaP.crearRecurso(c.getEmf());
        }
        else if(Objects.equals(aPrivilegiado, Boolean.TRUE)&&Objects.equals(rPrivilegiado, Boolean.FALSE)){
            fabricaP=new FabricaPrivilegiado();
            fabricaNP=new FabricaNoPrivilegiado();
            actividad=fabricaP.crearActividad(c.getEmf());
            recurso=fabricaNP.crearRecurso(c.getEmf());
        }
        else if(Objects.equals(aPrivilegiado, Boolean.FALSE)&&Objects.equals(rPrivilegiado, Boolean.TRUE)){
            fabricaP=new FabricaPrivilegiado();
            fabricaNP=new FabricaNoPrivilegiado();
            actividad=fabricaNP.crearActividad(c.getEmf());
            recurso=fabricaP.crearRecurso(c.getEmf());
        }
        else if(Objects.equals(aPrivilegiado, Boolean.FALSE)&&Objects.equals(rPrivilegiado, Boolean.FALSE)){
            fabricaNP=new FabricaNoPrivilegiado();
            actividad=fabricaNP.crearActividad(c.getEmf());
            recurso=fabricaNP.crearRecurso(c.getEmf());
        }
        
        
    }
    
    
}

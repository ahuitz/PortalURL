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
public class Curso {
    
    FabricaNoPrivilegiado fabricaNP;
    FabricaPrivilegiado fabricaP;
    Recurso recurso;
    Tarea tarea;

    public Curso(Object Usuario, int id_Curso, Object emf) {
        Boolean tprivilegiado=true;  //permisos tarea
        Boolean rprivilegiado=true;  //permisos recurso
        
        if(tprivilegiado==true){
            fabricaP=new FabricaPrivilegiado();
            tarea=fabricaP.crearTarea(emf);
        }
        else{
            fabricaNP=new FabricaNoPrivilegiado();
            tarea=fabricaNP.crearTarea(emf);
        }
        if(rprivilegiado==true){
            fabricaP=new FabricaPrivilegiado();
            recurso=fabricaP.crearRecurso(emf);
        }
        else{
            fabricaNP=new FabricaNoPrivilegiado();
            recurso=fabricaNP.crearRecurso(emf);
        }
    }
}

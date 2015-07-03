/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo López
 */
public abstract class Actividad {
    protected Object controlador;       // importar de controladores
    protected ArrayList<Object> actividades;    // importar de persistencia
    
    public Object getControlador() {
        return controlador;
    }

    public void setControlador(Object controlador) {
        this.controlador = controlador;
    }

    public ArrayList<Object> getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList<Object> actividades) {
        this.actividades = actividades;
    }

    public abstract void visualizar();
}

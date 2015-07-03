/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import java.util.List;

/**
 *
 * @author Pablo López
 */
public abstract class Tarea {
    protected Object controlador;       // importar de controladores
    protected List<Object> tareas;    // importar de persistencia

    public Object getControlador() {
        return controlador;
    }

    public void setControlador(Object controlador) {
        this.controlador = controlador;
    }

    public List<Object> getTareas() {
        return tareas;
    }

    public void setTareas(List<Object> tareas) {
        this.tareas = tareas;
    }
    
    public abstract void visualizar();
}

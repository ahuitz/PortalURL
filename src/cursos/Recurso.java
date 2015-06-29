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
public abstract class Recurso {
    protected Object controlador;       // importar de controladores
    protected List<Object> recursos;    // importar de persistencia

    public Object getControlador() {
        return controlador;
    }

    public void setControlador(Object controlador) {
        this.controlador = controlador;
    }

    public List<Object> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Object> recursos) {
        this.recursos = recursos;
    }
    
    public abstract void visualizar();
}

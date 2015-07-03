/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursos;

import java.util.ArrayList;

/**
 *
 * @author RealG4Life
 */
public class R_NoPrivilegiado extends Recurso{

    public R_NoPrivilegiado(Object controlador) {
        this.controlador = controlador;
        this.recursos= new ArrayList<>();
    }
    
    @Override
    public void visualizar() {
    }
    
}

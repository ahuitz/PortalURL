/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notas.percistence;

import javax.persistence.Entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Saul
 */
@Entity
public class Curso2 {
    @Id
    public int IdCurso;
    
    public Catedratico IdCatedratico;
    
    public String Nombre;
    public String Descripcion;

    public int getIdCurso() {
        return IdCurso;
    }

    public void setIdCurso(int IdCurso) {
        this.IdCurso = IdCurso;
    }

    public Catedratico getIdCatedratico() {
        return IdCatedratico;
    }

    public void setIdCatedratico(Catedratico IdCatedratico) {
        this.IdCatedratico = IdCatedratico;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    
    
    
    
}

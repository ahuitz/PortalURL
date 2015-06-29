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
public class Notas {
    @Id
    public int IdNotas;
    
    public Estudiante IdEstudiante;
    
    public Curso2 IdCurso;
    
    public Float Nota;
    public Float Promedio;

    
    
    public int getIdNotas() {
        return IdNotas;
    }

    public void setIdNotas(int IdNotas) {
        this.IdNotas = IdNotas;
    }

    public Estudiante getIdEstudiante() {
        return IdEstudiante;
    }

    public void setIdEstudiante(Estudiante IdEstudiante) {
        this.IdEstudiante = IdEstudiante;
    }

    public Curso2 getIdCurso() {
        return IdCurso;
    }

    public void setIdCurso(Curso2 IdCurso) {
        this.IdCurso = IdCurso;
    }

    public Float getNota() {
        return Nota;
    }

    public void setNota(Float Nota) {
        this.Nota = Nota;
    }

    public Float getPromedio() {
        return Promedio;
    }

    public void setPromedio(Float Promedio) {
        this.Promedio = Promedio;
    }
    
    
    
    
}

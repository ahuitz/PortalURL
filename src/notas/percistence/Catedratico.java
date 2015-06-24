/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notas.percistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Saul
 */
@Entity 
public class Catedratico implements Serializable{
    @Id
    public int IdCatedratico;
    
    public String Nombres;
    public String Apellidos;
    public String Carne;
    public String Direccion;
    public String Email;
    public int Telefono;
    public String Password;

    public int getIdCatedratico() {
        return IdCatedratico;
    }

    public void setIdCatedratico(int IdCatedratico) {
        this.IdCatedratico = IdCatedratico;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public String getCarne() {
        return Carne;
    }

    public void setCarne(String Carne) {
        this.Carne = Carne;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int Telefono) {
        this.Telefono = Telefono;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
    
    
    
    
    
    
}

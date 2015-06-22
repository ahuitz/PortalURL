/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import usuarios.Tipo.TipoUsuario;

/**
 *
 * @author Rosario
 */
public class Administrador {
    
    private TipoUsuario usuario;
    
    private Administrador unicoAdministrador;

    public TipoUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(TipoUsuario usuario) {
        this.usuario = usuario;
    }
    
    void CrearUsuario()
        {
            
        }

    private Administrador() {
    
    }
    
}

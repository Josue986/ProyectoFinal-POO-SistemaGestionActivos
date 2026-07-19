/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Usuario {
    private int idUsuario;
    private Custodio custodio;

    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(int idUsuario, Custodio custodio) {
        this.idUsuario = idUsuario;
        this.custodio = custodio;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUusario) {
        this.idUsuario = idUusario;
    }

    public Custodio getCustodio() {
        return custodio;
    }

    public void setCustodio(Custodio custodio) {
        this.custodio = custodio;
    }
}

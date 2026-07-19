/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Custodio {
    private int idCustodio;
    private String cedula;
    private String nombre;
    private String apellido;
    private String rol;

    public Custodio() {
    }

    public Custodio(int idCustodio, String cedula, String nombre, String apellido, String rol) {
        this.idCustodio = idCustodio;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }

    public int getIdCustodio() {
        return idCustodio;
    }

    public void setIdCustodio(int idCustodio) {
        this.idCustodio = idCustodio;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

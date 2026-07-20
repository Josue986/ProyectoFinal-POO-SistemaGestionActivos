/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public abstract class Activo {

    private int idActivo;
    private String nombreActivo;
    private String marca;
    private String tipoActivo;
    private double costoAdquicicion;
    private String estadoActivo;
    private Custodio custodio;

    public Activo() {
    }

    public Activo(int idActivo, String nombreActivo, String marca, String tipoActivo, String estadoActivo, Custodio custodio) {
        this.idActivo = idActivo;
        this.nombreActivo = nombreActivo;
        this.marca = marca;
        this.tipoActivo = tipoActivo;
        this.costoAdquicicion = costoAdquicicion;
        this.estadoActivo = estadoActivo;
        this.custodio = custodio;
    }

    public int getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(int idActivo) {
        this.idActivo = idActivo;
    }

    public String getNombreActivo() {
        return nombreActivo;
    }

    public void setNombreActivo(String nombreActivo) {
        this.nombreActivo = nombreActivo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipoActivo() {
        return tipoActivo;
    }

    public void setTipoActivo(String tipoActivo) {
        this.tipoActivo = tipoActivo;
    }

    public double getCostoAdquicicion() {
        return costoAdquicicion;
    }

    public void setCostoAdquicicion(double costoAdquicicion) {
        this.costoAdquicicion = costoAdquicicion;
    }

    public String getEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(String estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public Custodio getCustodio() {
        return custodio;
    }

    public String getTipo() {
        return this.getTipoActivo(); 
    }

    public void setCustodio(Custodio custodio) {
        this.custodio = custodio;
    }
}

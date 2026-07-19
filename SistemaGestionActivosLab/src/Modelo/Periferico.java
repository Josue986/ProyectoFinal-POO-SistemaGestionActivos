/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Periferico extends Activo {
    private int anniosUso;
    private String tipoConexion;
    private double costoMantenimiento;

    public Periferico(int anniosUso, String tipoConexion, int idActivo, String nombreActivo, String marca, String tipoActivo, String estadoActivo, Custodio custodio) {
        super(idActivo, nombreActivo, marca, tipoActivo, estadoActivo, custodio);
        this.anniosUso = anniosUso;
        this.tipoConexion = tipoConexion;
        this.costoMantenimiento = 0;
    }

    public int getAnniosUso() {
        return anniosUso;
    }

    public void setAnniosUso(int anniosUso) {
        this.anniosUso = anniosUso;
    }

    public String getTipoConexion() {
        return tipoConexion;
    }

    public void setTipoConexion(String tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public double getCostoMantenimiento() {
        return costoMantenimiento;
    }

    public void setCostoMantenimiento(double costoMantenimiento) {
        this.costoMantenimiento = costoMantenimiento;
    }
}

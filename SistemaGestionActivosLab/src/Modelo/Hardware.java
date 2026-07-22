/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Hardware extends Activo {
    private int anniosUso;
    private double costoMantenimiento;

    public Hardware() {
    }

    public Hardware(int anniosUso, int idActivo, String nombreActivo, String marca, String tipoActivo, 
            double costoAdquisicion, String estadoActivo, Custodio custodio) {
        super(idActivo, nombreActivo, marca, tipoActivo, costoAdquisicion, estadoActivo, custodio);
        this.anniosUso = anniosUso;
        this.costoMantenimiento = 0;
    }

    public int getAnniosUso() {
        return anniosUso;
    }

    public void setAnniosUso(int anniosUso) {
        this.anniosUso = anniosUso;
    }

    public double getCostoMantenimiento() {
        return costoMantenimiento;
    }

    public void setCostoMantenimiento(double costoMantenimiento) {
        this.costoMantenimiento = costoMantenimiento;
    }
}

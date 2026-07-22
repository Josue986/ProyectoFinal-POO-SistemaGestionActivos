/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Monitor extends Periferico {
    private String resolucion;
    private String tasaDeRefresco;

    public Monitor() {
    }

    public Monitor(String resolucion, String tasaDeRefresco, int anniosUso, 
            String tipoConexion, int idActivo, String nombreActivo, String marca, 
            String tipoActivo, String estadoActivo, double costoAdquisicion, Custodio custodio) {
        super(anniosUso, tipoConexion, idActivo, nombreActivo, marca, tipoActivo, costoAdquisicion, estadoActivo, custodio);
        this.resolucion = resolucion;
        this.tasaDeRefresco = tasaDeRefresco;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public String getTasaDeRefresco() {
        return tasaDeRefresco;
    }

    public void setTasaDeRefresco(String tasaDeRefresco) {
        this.tasaDeRefresco = tasaDeRefresco;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Mouse extends Periferico {
    private String dpi;

    public Mouse() {
    }

    public Mouse(String dpi, int anniosUso, String tipoConexion, int idActivo, 
            String nombreActivo, String marca, String tipoActivo, double costoAdquisicion, String estadoActivo, Custodio custodio) {
        super(anniosUso, tipoConexion, idActivo, nombreActivo, marca, tipoActivo, costoAdquisicion, estadoActivo, custodio);
        this.dpi = dpi;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }
}

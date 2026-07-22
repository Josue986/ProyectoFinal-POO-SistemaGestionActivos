/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.Date;

/**
 *
 * @author jotue
 */
public class Licencia extends Activo {
    private Date fechaExpiracion;
    private double costoRenovacion;

    public Licencia(Date fechaExpiracion, double costoRenovacion, int idActivo, 
            String nombreActivo, String marca, String tipoActivo, String estadoActivo, double costoAdquicicion, Custodio custodio) {
        super(idActivo, nombreActivo, marca, tipoActivo, costoAdquicicion, estadoActivo, custodio);
        this.fechaExpiracion = fechaExpiracion;
        this.costoRenovacion = costoRenovacion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public double getCostoRenovacion() {
        return costoRenovacion;
    }

    public void setCostoRenovacion(double costoRenovacion) {
        this.costoRenovacion = costoRenovacion;
    }
}

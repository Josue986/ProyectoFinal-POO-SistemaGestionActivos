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
public class RegMantenimiento {
    private int idMantenimiento;
    private String detallesMantenimiento;
    private Date fechaInicio;
    private Date fechaFin;
    private double costoMantenimiento;
    private Activo activo;
    private Usuario usuario;

    public RegMantenimiento() {
    }

    public RegMantenimiento(int idMantenimiento, String detallesMantenimiento, Date fechaInicio, Date fechaFin, double costoMantenimiento, Activo activo, Usuario usuario) {
        this.idMantenimiento = idMantenimiento;
        this.detallesMantenimiento = detallesMantenimiento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoMantenimiento = costoMantenimiento;
        this.activo = activo;
        this.usuario = usuario;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public String getDetallesMantenimiento() {
        return detallesMantenimiento;
    }

    public void setDetallesMantenimiento(String detallesMantenimiento) {
        this.detallesMantenimiento = detallesMantenimiento;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getCostoMantenimiento() {
        return costoMantenimiento;
    }

    public void setCostoMantenimiento(double costoMantenimiento) {
        this.costoMantenimiento = costoMantenimiento;
    }

    public Activo getActivo() {
        return activo;
    }

    public void setActivo(Activo activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

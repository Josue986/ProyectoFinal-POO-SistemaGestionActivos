/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jotue
 */
public class Cpu extends Hardware {
    private String procesador;
    private String memoriaRAM;
    private String almacenamiento;
    
    public Cpu(String procesador, String memoriaRAM, String almacenamiento, int anniosUso, int idActivo, String nombreActivo, String marca, String tipoActivo, String estadoActivo, Custodio custodio) {
        super(anniosUso, idActivo, nombreActivo, marca, tipoActivo, estadoActivo, custodio);
        this.procesador = procesador;
        this.memoriaRAM = memoriaRAM;
        this.almacenamiento = almacenamiento;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public String getMemoriaRAM() {
        return memoriaRAM;
    }

    public void setMemoriaRAM(String memoriaRAM) {
        this.memoriaRAM = memoriaRAM;
    }

    public String getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(String almacenamiento) {
        this.almacenamiento = almacenamiento;
    }
}

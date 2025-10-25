/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.time.LocalDateTime;
/**
 *
 * @author Equipo
 */
public class ClsEjecucionPermiso {
  
    private long idEjecucion;
    private long idPermiso;
    private LocalDateTime salidaReal;
    private LocalDateTime retornoReal;
    private boolean cumpleHorario;
    private boolean marcadoPerdida;
    private String observaciones;
    private Integer verificadoPor;
    private LocalDateTime verificadoEn;

    public ClsEjecucionPermiso() {
    }

    public ClsEjecucionPermiso(long idEjecucion, long idPermiso, LocalDateTime salidaReal, LocalDateTime retornoReal, boolean cumpleHorario, boolean marcadoPerdida, String observaciones, Integer verificadoPor, LocalDateTime verificadoEn) {
        this.idEjecucion = idEjecucion;
        this.idPermiso = idPermiso;
        this.salidaReal = salidaReal;
        this.retornoReal = retornoReal;
        this.cumpleHorario = cumpleHorario;
        this.marcadoPerdida = marcadoPerdida;
        this.observaciones = observaciones;
        this.verificadoPor = verificadoPor;
        this.verificadoEn = verificadoEn;
    }

    public long getIdEjecucion() {
        return idEjecucion;
    }

    public void setIdEjecucion(long idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public LocalDateTime getSalidaReal() {
        return salidaReal;
    }

    public void setSalidaReal(LocalDateTime salidaReal) {
        this.salidaReal = salidaReal;
    }

    public LocalDateTime getRetornoReal() {
        return retornoReal;
    }

    public void setRetornoReal(LocalDateTime retornoReal) {
        this.retornoReal = retornoReal;
    }

    public boolean isCumpleHorario() {
        return cumpleHorario;
    }

    public void setCumpleHorario(boolean cumpleHorario) {
        this.cumpleHorario = cumpleHorario;
    }

    public boolean isMarcadoPerdida() {
        return marcadoPerdida;
    }

    public void setMarcadoPerdida(boolean marcadoPerdida) {
        this.marcadoPerdida = marcadoPerdida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getVerificadoPor() {
        return verificadoPor;
    }

    public void setVerificadoPor(Integer verificadoPor) {
        this.verificadoPor = verificadoPor;
    }

    public LocalDateTime getVerificadoEn() {
        return verificadoEn;
    }

    public void setVerificadoEn(LocalDateTime verificadoEn) {
        this.verificadoEn = verificadoEn;
    }


    
    
    
}

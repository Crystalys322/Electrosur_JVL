/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 *
 * @author Equipo
 */
public class ClsPermiso {
    
    private long idPermiso;
    private int idEmpleado;
    private String dirigidoA; // JEFE_AREA | JEFE_RRHH
    private String motivo;
    private LocalDate fechaSalidaPlan;
    private LocalTime horaSalidaPlan;
    private LocalDate fechaRetornoPlan;
    private LocalTime horaRetornoPlan;
    private String estado; // ENVIADO, APROB_JEFE, APROB_RRHH, DENEGADO, EJECUTADO, INCUMPLIDO
    private String observacionesDenegacion;    

    public ClsPermiso() {
    }

    public ClsPermiso(long idPermiso, int idEmpleado, String dirigidoA, String motivo, LocalDate fechaSalidaPlan, LocalTime horaSalidaPlan, LocalDate fechaRetornoPlan, LocalTime horaRetornoPlan, String estado, String observacionesDenegacion) {
        this.idPermiso = idPermiso;
        this.idEmpleado = idEmpleado;
        this.dirigidoA = dirigidoA;
        this.motivo = motivo;
        this.fechaSalidaPlan = fechaSalidaPlan;
        this.horaSalidaPlan = horaSalidaPlan;
        this.fechaRetornoPlan = fechaRetornoPlan;
        this.horaRetornoPlan = horaRetornoPlan;
        this.estado = estado;
        this.observacionesDenegacion = observacionesDenegacion;
    }

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getDirigidoA() {
        return dirigidoA;
    }

    public void setDirigidoA(String dirigidoA) {
        this.dirigidoA = dirigidoA;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFechaSalidaPlan() {
        return fechaSalidaPlan;
    }

    public void setFechaSalidaPlan(LocalDate fechaSalidaPlan) {
        this.fechaSalidaPlan = fechaSalidaPlan;
    }

    public LocalTime getHoraSalidaPlan() {
        return horaSalidaPlan;
    }

    public void setHoraSalidaPlan(LocalTime horaSalidaPlan) {
        this.horaSalidaPlan = horaSalidaPlan;
    }

    public LocalDate getFechaRetornoPlan() {
        return fechaRetornoPlan;
    }

    public void setFechaRetornoPlan(LocalDate fechaRetornoPlan) {
        this.fechaRetornoPlan = fechaRetornoPlan;
    }

    public LocalTime getHoraRetornoPlan() {
        return horaRetornoPlan;
    }

    public void setHoraRetornoPlan(LocalTime horaRetornoPlan) {
        this.horaRetornoPlan = horaRetornoPlan;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionesDenegacion() {
        return observacionesDenegacion;
    }

    public void setObservacionesDenegacion(String observacionesDenegacion) {
        this.observacionesDenegacion = observacionesDenegacion;
    }
    
    
    
}

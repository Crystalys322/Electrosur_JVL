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
public class ClsAprobacion {
   
    private long idAprobacion;
    private long idPermiso;
    private int idAprobador;
    private String rolAprobador; // JEFE_AREA | JEFE_RRHH
    private String decision;     // APROBAR | DENEGAR | REENVIAR
    private String observaciones;
    private LocalDateTime fechaDecision;

    public ClsAprobacion() {
    }

    public ClsAprobacion(long idAprobacion, long idPermiso, int idAprobador, String rolAprobador, String decision, String observaciones, LocalDateTime fechaDecision) {
        this.idAprobacion = idAprobacion;
        this.idPermiso = idPermiso;
        this.idAprobador = idAprobador;
        this.rolAprobador = rolAprobador;
        this.decision = decision;
        this.observaciones = observaciones;
        this.fechaDecision = fechaDecision;
    }

    public long getIdAprobacion() {
        return idAprobacion;
    }

    public void setIdAprobacion(long idAprobacion) {
        this.idAprobacion = idAprobacion;
    }

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getIdAprobador() {
        return idAprobador;
    }

    public void setIdAprobador(int idAprobador) {
        this.idAprobador = idAprobador;
    }

    public String getRolAprobador() {
        return rolAprobador;
    }

    public void setRolAprobador(String rolAprobador) {
        this.rolAprobador = rolAprobador;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaDecision() {
        return fechaDecision;
    }

    public void setFechaDecision(LocalDateTime fechaDecision) {
        this.fechaDecision = fechaDecision;
    }


    
    
}

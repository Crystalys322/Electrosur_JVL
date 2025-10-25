/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Equipo
 */
public class ClsEmpleado {
    
    private int idEmpleado;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private int idArea;
    private Integer idJefeInmediato; // puede ser null
    private boolean activo;
    private int reincidenteCount;    

    public ClsEmpleado() {
    }

    public ClsEmpleado(int idEmpleado, String dni, String nombres, String apellidos, String email, String telefono, int idArea, Integer idJefeInmediato, boolean activo, int reincidenteCount) {
        this.idEmpleado = idEmpleado;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.idArea = idArea;
        this.idJefeInmediato = idJefeInmediato;
        this.activo = activo;
        this.reincidenteCount = reincidenteCount;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public Integer getIdJefeInmediato() {
        return idJefeInmediato;
    }

    public void setIdJefeInmediato(Integer idJefeInmediato) {
        this.idJefeInmediato = idJefeInmediato;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getReincidenteCount() {
        return reincidenteCount;
    }

    public void setReincidenteCount(int reincidenteCount) {
        this.reincidenteCount = reincidenteCount;
    }
    
    
}

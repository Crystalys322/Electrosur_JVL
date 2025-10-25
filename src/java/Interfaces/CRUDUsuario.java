package Interfaces;

import Modelo.ClsUsuario;

public interface CRUDUsuario {
    ClsUsuario validarLogin(String username, String passwordPlano); // compara contra hash
    ClsUsuario obtenerPorId(int idUsuario);
    boolean crear(ClsUsuario u);
    boolean actualizar(ClsUsuario u);
    boolean eliminar(int idUsuario);
}

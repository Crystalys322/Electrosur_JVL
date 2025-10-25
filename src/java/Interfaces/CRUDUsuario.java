package Interfaces;

import Modelo.ClsUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface CRUDUsuario {
    Optional<ClsUsuario> findByUsername(Connection connection, String username) throws SQLException;
    Optional<ClsUsuario> findById(Connection connection, int idUsuario) throws SQLException;
    boolean crear(Connection connection, ClsUsuario u) throws SQLException;
    boolean actualizar(Connection connection, ClsUsuario u) throws SQLException;
    boolean eliminar(Connection connection, int idUsuario) throws SQLException;
    void actualizarUltimoLogin(Connection connection, int idUsuario) throws SQLException;
}

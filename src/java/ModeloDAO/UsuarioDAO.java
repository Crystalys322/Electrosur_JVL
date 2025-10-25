package ModeloDAO;

import Config.ClsConexion;
import Interfaces.CRUDUsuario;
import Modelo.ClsUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

public class UsuarioDAO implements CRUDUsuario {
    private final ClsConexion conexion = new ClsConexion();

    public Connection getConnection() {
        return conexion.getConnection();
    }

    @Override
    public Optional<ClsUsuario> findByUsername(Connection connection, String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username=? AND estado=1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<ClsUsuario> findById(Connection connection, int idUsuario) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean crear(Connection connection, ClsUsuario u) throws SQLException {
        String sql = "INSERT INTO usuarios(id_empleado, username, pass_hash, id_rol, estado) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, u.getIdEmpleado());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassHash());
            ps.setInt(4, u.getIdRol());
            ps.setBoolean(5, u.isEstado());
            int updated = ps.executeUpdate();
            if (updated > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        u.setIdUsuario(rs.getInt(1));
                    }
                }
            }
            return updated > 0;
        }
    }

    @Override
    public boolean actualizar(Connection connection, ClsUsuario u) throws SQLException {
        String sql = "UPDATE usuarios SET id_empleado=?, username=?, pass_hash=?, id_rol=?, estado=? WHERE id_usuario=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, u.getIdEmpleado());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassHash());
            ps.setInt(4, u.getIdRol());
            ps.setBoolean(5, u.isEstado());
            ps.setInt(6, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Connection connection, int idUsuario) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public void actualizarUltimoLogin(Connection connection, int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET ultimo_login=NOW() WHERE id_usuario=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    private ClsUsuario map(ResultSet rs) throws SQLException {
        ClsUsuario u = new ClsUsuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setIdEmpleado(rs.getInt("id_empleado"));
        u.setUsername(rs.getString("username"));
        u.setPassHash(rs.getString("pass_hash"));
        u.setIdRol(rs.getInt("id_rol"));
        u.setEstado(rs.getBoolean("estado"));
        Timestamp ultimoLogin = rs.getTimestamp("ultimo_login");
        if (ultimoLogin != null) {
            u.setUltimoLogin(ultimoLogin.toLocalDateTime());
        }
        return u;
    }
}

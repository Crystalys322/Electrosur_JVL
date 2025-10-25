package ModeloDAO;

import Config.ClsConexion;
import Interfaces.CRUDUsuario;
import Modelo.ClsUsuario;
import java.sql.*;

public class UsuarioDAO implements CRUDUsuario {
    private final ClsConexion cn = new ClsConexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public ClsUsuario validarLogin(String username, String passwordPlano) {
        ClsUsuario u = null;
        String sql = "SELECT * FROM usuarios WHERE username=? AND estado=1";

        try {
            con = cn.getConnection();  // usa tu conexión viva
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String passBD = rs.getString("pass_hash");

                // Comparación directa con la contraseña guardada (ej: ana123, bruno123, carla123)
                if (passwordPlano.equals(passBD)) {
                    u = new ClsUsuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setIdEmpleado(rs.getInt("id_empleado"));
                    u.setUsername(rs.getString("username"));
                    u.setPassHash(passBD);
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setEstado(rs.getBoolean("estado"));

                    // Actualiza la fecha de último login
                    String upd = "UPDATE usuarios SET ultimo_login=NOW() WHERE id_usuario=?";
                    PreparedStatement psUpd = con.prepareStatement(upd);
                    psUpd.setInt(1, u.getIdUsuario());
                    psUpd.executeUpdate();
                    psUpd.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en validarLogin: " + e.getMessage());
        } finally {
            // Cerramos solo los recursos, NO la conexión global
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("⚠ Error cerrando recursos: " + e.getMessage());
            }
        }

        return u; // si no encuentra, retorna null
    }

    @Override
    public ClsUsuario obtenerPorId(int idUsuario) {
        ClsUsuario u = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario=?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = new ClsUsuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setIdEmpleado(rs.getInt("id_empleado"));
                u.setUsername(rs.getString("username"));
                u.setPassHash(rs.getString("pass_hash"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setEstado(rs.getBoolean("estado"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en obtenerPorId: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("⚠ Error cerrando recursos: " + e.getMessage());
            }
        }
        return u;
    }

    @Override
    public boolean crear(ClsUsuario u) {
        String sql = "INSERT INTO usuarios(id_empleado,username,pass_hash,id_rol,estado) VALUES(?,?,?,?,1)";
        boolean ok = false;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, u.getIdEmpleado());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassHash());
            ps.setInt(4, u.getIdRol());
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al crear usuario: " + e.getMessage());
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }

        return ok;
    }

    @Override
    public boolean actualizar(ClsUsuario u) {
        String sql = "UPDATE usuarios SET id_empleado=?, username=?, pass_hash=?, id_rol=?, estado=? WHERE id_usuario=?";
        boolean ok = false;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, u.getIdEmpleado());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassHash());
            ps.setInt(4, u.getIdRol());
            ps.setBoolean(5, u.isEstado());
            ps.setInt(6, u.getIdUsuario());
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar usuario: " + e.getMessage());
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }

        return ok;
    }

    @Override
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        boolean ok = false;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar usuario: " + e.getMessage());
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }

        return ok;
    }
}

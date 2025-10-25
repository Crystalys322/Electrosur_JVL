package ModeloDAO;

import Config.ClsConexion;
import Interfaces.CRUDEmpleado;
import Modelo.ClsEmpleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements CRUDEmpleado {
    private final ClsConexion cn = new ClsConexion();

    @Override
    public ClsEmpleado obtener(int idEmpleado) {
        String sql = "SELECT * FROM empleados WHERE id_empleado=?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    @Override
    public ClsEmpleado obtenerPorDni(String dni) {
        String sql = "SELECT * FROM empleados WHERE dni=?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    @Override
    public List<ClsEmpleado> listar() {
        List<ClsEmpleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados ORDER BY apellidos, nombres";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (Exception ex) { ex.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean crear(ClsEmpleado e) {
        String sql = "INSERT INTO empleados(dni,nombres,apellidos,email,telefono,id_area,id_jefe_inmediato,activo) " +
                     "VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getDni());
            ps.setString(2, e.getNombres());
            ps.setString(3, e.getApellidos());
            ps.setString(4, e.getEmail());
            ps.setString(5, e.getTelefono());
            ps.setInt(6, e.getIdArea());
            if (e.getIdJefeInmediato()==null) ps.setNull(7, Types.INTEGER); else ps.setInt(7, e.getIdJefeInmediato());
            ps.setBoolean(8, e.isActivo());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

    @Override
    public boolean actualizar(ClsEmpleado e) {
        String sql = "UPDATE empleados SET dni=?, nombres=?, apellidos=?, email=?, telefono=?, id_area=?, id_jefe_inmediato=?, activo=?, reincidente_count=? " +
                     "WHERE id_empleado=?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getDni());
            ps.setString(2, e.getNombres());
            ps.setString(3, e.getApellidos());
            ps.setString(4, e.getEmail());
            ps.setString(5, e.getTelefono());
            ps.setInt(6, e.getIdArea());
            if (e.getIdJefeInmediato()==null) ps.setNull(7, Types.INTEGER); else ps.setInt(7, e.getIdJefeInmediato());
            ps.setBoolean(8, e.isActivo());
            ps.setInt(9, e.getReincidenteCount());
            ps.setInt(10, e.getIdEmpleado());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(int idEmpleado) {
        String sql = "DELETE FROM empleados WHERE id_empleado=?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

    private ClsEmpleado map(ResultSet rs) throws Exception {
        ClsEmpleado e = new ClsEmpleado();
        e.setIdEmpleado(rs.getInt("id_empleado"));
        e.setDni(rs.getString("dni"));
        e.setNombres(rs.getString("nombres"));
        e.setApellidos(rs.getString("apellidos"));
        e.setEmail(rs.getString("email"));
        e.setTelefono(rs.getString("telefono"));
        e.setIdArea(rs.getInt("id_area"));
        int jefe = rs.getInt("id_jefe_inmediato");
        e.setIdJefeInmediato(rs.wasNull() ? null : jefe);
        e.setActivo(rs.getBoolean("activo"));
        e.setReincidenteCount(rs.getInt("reincidente_count"));
        return e;
    }
}

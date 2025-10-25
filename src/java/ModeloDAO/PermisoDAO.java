package ModeloDAO;

import Config.ClsConexion;
import Interfaces.CRUDPermiso;
import Logging.AppLogger;
import Modelo.ClsAprobacion;
import Modelo.ClsPermiso;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermisoDAO implements CRUDPermiso {
    private static final Logger LOGGER = AppLogger.getLogger(PermisoDAO.class);
    private final ClsConexion cn = new ClsConexion();

    @Override
    public long crear(ClsPermiso p) {
        String sql = "INSERT INTO permisos(id_empleado, dirigido_a, motivo, " +
                "fecha_salida_plan, hora_salida_plan, fecha_retorno_plan, hora_retorno_plan, estado) " +
                "VALUES(?,?,?,?,?,?,?, 'ENVIADO')";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdEmpleado());
            ps.setString(2, p.getDirigidoA());
            ps.setString(3, p.getMotivo());
            ps.setDate(4, Date.valueOf(p.getFechaSalidaPlan()));
            ps.setTime(5, Time.valueOf(p.getHoraSalidaPlan()));
            ps.setDate(6, Date.valueOf(p.getFechaRetornoPlan()));
            ps.setTime(7, Time.valueOf(p.getHoraRetornoPlan()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al crear permiso", ex);
        }
        return -1;
    }

    // ✅ NUEVO: horas acumuladas de trabajo del empleado (en horas con decimales)
    public double obtenerHorasAcumuladasTrabajo(int idEmpleado) {
        String sql = """
            SELECT IFNULL(SUM(TIMESTAMPDIFF(MINUTE, ep.salida_real, ep.retorno_real)), 0) AS minutos
            FROM ejecucion_permiso ep
            JOIN permisos p ON p.id_permiso = ep.id_permiso
            WHERE p.id_empleado = ?
        """;
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double minutos = rs.getDouble("minutos");
                return minutos / 60.0; // horas
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al obtener horas acumuladas", e);
        }
        return 0.0;
    }

    @Override
    public ClsPermiso obtener(long idPermiso) {
        String sql = "SELECT * FROM permisos WHERE id_permiso=?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idPermiso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener permiso", ex);
        }
        return null;
    }

    @Override
    public List<ClsPermiso> listarPorEmpleado(int idEmpleado) {
        List<ClsPermiso> lista = new ArrayList<>();
        String sql = "SELECT p.*, CONCAT(e.nombres, ' ', e.apellidos) AS nombre_empleado " +
                "FROM permisos p " +
                "JOIN empleados e ON e.id_empleado = p.id_empleado " +
                "WHERE p.id_empleado=? ORDER BY p.creado_en DESC";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(map(rs));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al listar permisos por empleado", ex);
        }
        return lista;
    }

    @Override
    public List<ClsPermiso> listarPendientesParaJefeArea(int idJefeArea) {
        List<ClsPermiso> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, CONCAT(e.nombres, ' ', e.apellidos) AS nombre_empleado
            FROM permisos p
            JOIN empleados e ON e.id_empleado = p.id_empleado
            JOIN empleados jefe ON jefe.id_empleado = ?
            WHERE e.id_area = jefe.id_area
              AND p.estado = 'ENVIADO'
              AND p.dirigido_a = 'JEFE_AREA'
            ORDER BY p.creado_en DESC
        """;
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idJefeArea);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(map(rs));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al listar permisos para jefe", ex);
        }
        return lista;
    }

    @Override
    public List<ClsPermiso> listarPendientesParaRRHH() {
        List<ClsPermiso> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, CONCAT(e.nombres, ' ', e.apellidos) AS nombre_empleado
            FROM permisos p
            JOIN empleados e ON e.id_empleado = p.id_empleado
            WHERE p.estado = 'APROB_JEFE'
               OR (p.estado = 'ENVIADO' AND p.dirigido_a = 'JEFE_RRHH')
            ORDER BY p.creado_en DESC
        """;
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al listar permisos para RRHH", ex);
        }
        return lista;
    }

    // ✅ ACTUALIZADO: RRHH valida las 50h antes de aprobar
    @Override
    public boolean registrarAprobacion(ClsAprobacion a) {
        try (Connection con = cn.getConnection()) {

            // 1️⃣ Si es RRHH y aprueba, validamos las horas acumuladas
            if ("JEFE_RRHH".equals(a.getRolAprobador()) && "APROBAR".equals(a.getDecision())) {
                int idEmpleado = -1;
                try (PreparedStatement psEmp = con.prepareStatement("SELECT id_empleado FROM permisos WHERE id_permiso=?")) {
                    psEmp.setLong(1, a.getIdPermiso());
                    ResultSet rsEmp = psEmp.executeQuery();
                    if (rsEmp.next()) idEmpleado = rsEmp.getInt("id_empleado");
                }

                if (idEmpleado > -1) {
                    double horas = obtenerHorasAcumuladasTrabajo(idEmpleado);
                    if (horas > 50.0) {
                        a.setDecision("DENEGAR");
                        String obs = a.getObservaciones();
                        a.setObservaciones((obs == null || obs.isBlank())
                                ? "Denegado automáticamente por RR.HH. (supera 50h acumuladas: " + String.format("%.2f", horas) + " h)"
                                : obs + " (Total horas: " + String.format("%.2f", horas) + " h)");
                    }
                }
            }

            // 2️⃣ Registrar la aprobación/denegación
            String sql = "INSERT INTO aprobaciones(id_permiso,id_aprobador,rol_aprobador,decision,observaciones) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, a.getIdPermiso());
                ps.setInt(2, a.getIdAprobador());
                ps.setString(3, a.getRolAprobador());
                ps.setString(4, a.getDecision());
                ps.setString(5, a.getObservaciones());
                ps.executeUpdate();
            }

            // 3️⃣ Actualizar estado del permiso
            String nuevoEstado = switch (a.getRolAprobador()) {
                case "JEFE_AREA" -> a.getDecision().equals("APROBAR") ? "APROB_JEFE" : "DENEGADO";
                case "JEFE_RRHH" -> a.getDecision().equals("APROBAR") ? "APROB_RRHH" : "DENEGADO";
                default -> "ENVIADO";
            };
            String obs = a.getDecision().equals("DENEGAR") ? a.getObservaciones() : null;
            return actualizarEstado(a.getIdPermiso(), nuevoEstado, obs);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar aprobación", ex);
        }
        return false;
    }

    @Override
    public boolean actualizarEstado(long idPermiso, String nuevoEstado, String obsDenegacion) {
        String sql = "UPDATE permisos SET estado=?, observaciones_denegacion=? WHERE id_permiso=?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            if (obsDenegacion == null) ps.setNull(2, Types.VARCHAR);
            else ps.setString(2, obsDenegacion);
            ps.setLong(3, idPermiso);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar estado del permiso", ex);
        }
        return false;
    }

    @Override
    public boolean registrarEjecucion(long idPermiso, LocalDateTime salidaReal, LocalDateTime retornoReal,
                                      boolean cumpleHorario, boolean marcadoPerdida,
                                      Integer verificadoPor, String observaciones) {
        String sql = "INSERT INTO ejecucion_permiso(id_permiso,salida_real,retorno_real,cumple_horario,marcado_perdida,observaciones,verificado_por,verificado_en) " +
                     "VALUES (?,?,?,?,?,?,?,NOW())";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idPermiso);
            ps.setTimestamp(2, salidaReal == null ? null : Timestamp.valueOf(salidaReal));
            ps.setTimestamp(3, retornoReal == null ? null : Timestamp.valueOf(retornoReal));
            ps.setBoolean(4, cumpleHorario);
            ps.setBoolean(5, marcadoPerdida);
            ps.setString(6, observaciones);
            if (verificadoPor == null) ps.setNull(7, Types.INTEGER);
            else ps.setInt(7, verificadoPor);
            boolean ok = ps.executeUpdate() > 0;

            // Si no cumple horario → marcar INCUMPLIDO y sumar reincidente
            if (ok && !cumpleHorario) {
                try (PreparedStatement up = con.prepareStatement("UPDATE permisos SET estado='INCUMPLIDO' WHERE id_permiso=?")) {
                    up.setLong(1, idPermiso);
                    up.executeUpdate();
                }
                try (PreparedStatement up2 = con.prepareStatement(
                        "UPDATE empleados SET reincidente_count = reincidente_count + 1 " +
                        "WHERE id_empleado = (SELECT id_empleado FROM permisos WHERE id_permiso=?)")) {
                    up2.setLong(1, idPermiso);
                    up2.executeUpdate();
                }
            }
            return ok;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar ejecución del permiso", ex);
        }
        return false;
    }

    private ClsPermiso map(ResultSet rs) throws Exception {
        ClsPermiso p = new ClsPermiso();
        p.setIdPermiso(rs.getLong("id_permiso"));
        p.setIdEmpleado(rs.getInt("id_empleado"));
        p.setDirigidoA(rs.getString("dirigido_a"));
        p.setMotivo(rs.getString("motivo"));
        p.setFechaSalidaPlan(rs.getDate("fecha_salida_plan").toLocalDate());
        p.setHoraSalidaPlan(rs.getTime("hora_salida_plan").toLocalTime());
        p.setFechaRetornoPlan(rs.getDate("fecha_retorno_plan").toLocalDate());
        p.setHoraRetornoPlan(rs.getTime("hora_retorno_plan").toLocalTime());
        p.setEstado(rs.getString("estado"));
        p.setObservacionesDenegacion(rs.getString("observaciones_denegacion"));
        if (hasColumn(rs, "nombre_empleado")) {
            p.setNombreEmpleado(rs.getString("nombre_empleado"));
        }
        return p;
    }

    private boolean hasColumn(ResultSet rs, String columnLabel) {
        try {
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                if (columnLabel.equalsIgnoreCase(meta.getColumnLabel(i))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.FINE, "No se pudo verificar columna {0}", columnLabel);
        }
        return false;
    }
}

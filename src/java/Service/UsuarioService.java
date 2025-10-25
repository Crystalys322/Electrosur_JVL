package Service;

import Config.ConnectionPool;
import DTO.UsuarioDTO;
import Logging.AppLogger;
import Modelo.ClsUsuario;
import ModeloDAO.UsuarioDAO;
import Security.PasswordHasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioService {
    private static final Logger LOGGER = AppLogger.getLogger(UsuarioService.class);
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ConnectionPool pool = ConnectionPool.getInstance();

    public Optional<UsuarioDTO> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        try (Connection connection = pool.borrowConnection()) {
            connection.setAutoCommit(false);
            Optional<ClsUsuario> maybeUser = usuarioDAO.findByUsername(connection, username);
            if (maybeUser.isEmpty()) {
                connection.rollback();
                return Optional.empty();
            }
            ClsUsuario usuario = maybeUser.get();
            boolean almacenHash = PasswordHasher.isHash(usuario.getPassHash());
            boolean contrasenaValida = almacenHash
                    ? PasswordHasher.matches(password, usuario.getPassHash())
                    : password.equals(usuario.getPassHash());
            if (!contrasenaValida) {
                connection.rollback();
                return Optional.empty();
            }
            if (!almacenHash) {
                usuario.setPassHash(PasswordHasher.hash(password));
                usuarioDAO.actualizar(connection, usuario);
            }
            usuarioDAO.actualizarUltimoLogin(connection, usuario.getIdUsuario());
            connection.commit();
            usuario.setUltimoLogin(LocalDateTime.now());
            return Optional.of(toDto(usuario));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al procesar el inicio de sesión", e);
            return Optional.empty();
        }
    }

    public Optional<UsuarioDTO> obtenerPorId(int idUsuario) {
        try (Connection connection = pool.borrowConnection()) {
            return usuarioDAO.findById(connection, idUsuario).map(this::toDto);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error al obtener usuario por ID", e);
            return Optional.empty();
        }
    }

    private UsuarioDTO toDto(ClsUsuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdEmpleado(usuario.getIdEmpleado());
        dto.setUsername(usuario.getUsername());
        dto.setIdRol(usuario.getIdRol());
        dto.setActivo(usuario.isEstado());
        dto.setUltimoLogin(usuario.getUltimoLogin());
        return dto;
    }
}



package Config;

import Logging.AppLogger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Expone conexiones gestionadas por un pool seguro.
 */
public class ClsConexion {
    private static final Logger LOGGER = AppLogger.getLogger(ClsConexion.class);
    private final ConnectionPool pool = ConnectionPool.getInstance();

    public Connection getConnection() {
        try {
            return pool.borrowConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "No se pudo obtener una conexión", e);
            throw new IllegalStateException("No hay conexiones disponibles", e);
        }
    }
}


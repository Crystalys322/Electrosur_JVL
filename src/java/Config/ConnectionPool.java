package Config;

import Logging.AppLogger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación simple de un pool de conexiones. Envuelve las conexiones
 * físicas para que el método {@code close()} devuelva la conexión al pool
 * automáticamente.
 */
public final class ConnectionPool {
    private static final Logger LOGGER = AppLogger.getLogger(ConnectionPool.class);
    private static final Duration VALIDATION_TIMEOUT = Duration.ofSeconds(5);
    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private final BlockingQueue<Connection> pool;
    private final AtomicInteger createdConnections = new AtomicInteger(0);
    private final int maxSize;
    private final DatabaseConfig config;

    private ConnectionPool() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró el driver de MySQL", e);
        }
        this.config = DatabaseConfig.getInstance();
        this.maxSize = config.getPoolSize();
        this.pool = new ArrayBlockingQueue<>(maxSize);
    }

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    public Connection borrowConnection() throws SQLException {
        Connection connection = pollConnection();
        if (connection == null) {
            connection = tryCreateNewConnection();
        }

        if (connection == null) {
            try {
                connection = pool.poll(VALIDATION_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SQLException("Interrumpido al esperar una conexión disponible", e);
            }
        }

        if (connection == null) {
            throw new SQLException("No hay conexiones disponibles en el pool");
        }

        return createProxy(connection);
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
            connection.setAutoCommit(true);
            if (!pool.offer(connection)) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error al devolver la conexión al pool", e);
            try {
                connection.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión dañada", ex);
            }
        }
    }

    public void shutdown() {
        pool.forEach(conn -> {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error cerrando conexión durante el apagado del pool", e);
            }
        });
        pool.clear();
    }

    private Connection pollConnection() {
        Connection connection = pool.poll();
        if (connection == null) {
            return null;
        }
        try {
            if (!connection.isValid((int) VALIDATION_TIMEOUT.getSeconds())) {
                connection.close();
                return null;
            }
            return connection;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
            return null;
        }
    }

    private Connection tryCreateNewConnection() throws SQLException {
        if (createdConnections.get() >= maxSize) {
            return null;
        }
        synchronized (this) {
            if (createdConnections.get() >= maxSize) {
                return null;
            }
            Connection connection = DriverManager.getConnection(
                    config.getUrl(),
                    emptyToNull(config.getUsername()),
                    emptyToNull(config.getPassword()));
            createdConnections.incrementAndGet();
            LOGGER.log(Level.FINE, "Conexión creada. Total={0}", createdConnections.get());
            return connection;
        }
    }

    private Connection createProxy(Connection realConnection) {
        InvocationHandler handler = new PooledConnectionHandler(realConnection, this);
        return (Connection) Proxy.newProxyInstance(
                realConnection.getClass().getClassLoader(),
                new Class<?>[]{Connection.class},
                handler
        );
    }

    private static String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private static final class PooledConnectionHandler implements InvocationHandler {
        private final Connection delegate;
        private final ConnectionPool pool;
        private boolean closed;

        private PooledConnectionHandler(Connection delegate, ConnectionPool pool) {
            this.delegate = delegate;
            this.pool = pool;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if ("close".equals(methodName)) {
                if (!closed) {
                    closed = true;
                    pool.releaseConnection(delegate);
                }
                return null;
            }
            if ("isClosed".equals(methodName)) {
                return closed || delegate.isClosed();
            }
            if (closed) {
                throw new SQLException("La conexión ya fue cerrada por el pool");
            }
            try {
                return method.invoke(delegate, args);
            } catch (Throwable throwable) {
                Throwable cause = throwable instanceof InvocationTargetException
                        ? ((InvocationTargetException) throwable).getTargetException()
                        : throwable;
                if (cause instanceof SQLException) {
                    pool.releaseConnection(delegate);
                    closed = true;
                }
                throw cause;
            }
        }
    }
}

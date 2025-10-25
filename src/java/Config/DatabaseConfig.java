package Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Carga las propiedades de conexión a base de datos y permite sobrescribirlas
 * mediante variables de entorno.
 */
public final class DatabaseConfig {
    private static final String PROPERTIES_PATH = "/Config/database.properties";
    private static final String ENV_URL = "DB_URL";
    private static final String ENV_USER = "DB_USERNAME";
    private static final String ENV_PASSWORD = "DB_PASSWORD";
    private static final String ENV_POOL_SIZE = "DB_POOL_SIZE";

    private static final DatabaseConfig INSTANCE = new DatabaseConfig();

    private final String url;
    private final String username;
    private final String password;
    private final int poolSize;

    private DatabaseConfig() {
        Properties props = loadProperties();
        this.url = firstNonBlank(System.getenv(ENV_URL), props.getProperty("db.url"));
        this.username = firstNonBlank(System.getenv(ENV_USER), props.getProperty("db.username"));
        this.password = firstNonBlank(System.getenv(ENV_PASSWORD), props.getProperty("db.password"));
        this.poolSize = parsePoolSize(props.getProperty("db.pool.size"));
    }

    public static DatabaseConfig getInstance() {
        return INSTANCE;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream is = DatabaseConfig.class.getResourceAsStream(PROPERTIES_PATH)) {
            if (is != null) {
                properties.load(is);
            } else {
                throw new IllegalStateException("No se encontró el archivo de propiedades: " + PROPERTIES_PATH);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la configuración de base de datos", e);
        }
        return properties;
    }

    private String firstNonBlank(String primary, String fallback) {
        if (primary != null && !primary.isBlank()) {
            return primary.trim();
        }
        return Objects.requireNonNullElse(fallback, "").trim();
    }

    private int parsePoolSize(String configuredSize) {
        String envValue = System.getenv(ENV_POOL_SIZE);
        String raw = envValue != null && !envValue.isBlank() ? envValue : configuredSize;
        try {
            int size = Integer.parseInt(raw);
            if (size <= 0) {
                throw new IllegalArgumentException("El tamaño del pool debe ser positivo");
            }
            return size;
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("Valor inválido para el tamaño del pool: " + raw, ex);
        }
    }
}

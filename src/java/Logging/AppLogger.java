package Logging;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Configura un logger estructurado que emite las entradas como JSON plano para
 * facilitar el análisis en herramientas externas.
 */
public final class AppLogger {
    private static final String ROOT_NAME = "Electrosur";
    private static final Logger ROOT_LOGGER = Logger.getLogger(ROOT_NAME);
    private static volatile boolean configured;

    private AppLogger() {
    }

    public static Logger getLogger(Class<?> clazz) {
        configure();
        return Logger.getLogger(ROOT_NAME + "." + clazz.getSimpleName());
    }

    private static synchronized void configure() {
        if (configured) {
            return;
        }
        ROOT_LOGGER.setUseParentHandlers(false);
        Handler console = new ConsoleHandler();
        console.setLevel(Level.INFO);
        console.setFormatter(new JsonFormatter());
        ROOT_LOGGER.addHandler(console);
        ROOT_LOGGER.setLevel(Level.INFO);
        configured = true;
    }

    private static final class JsonFormatter extends SimpleFormatter {
        @Override
        public synchronized String format(LogRecord record) {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("timestamp", Instant.ofEpochMilli(record.getMillis()).toString());
            payload.put("level", record.getLevel().getName());
            payload.put("logger", record.getLoggerName());
            payload.put("message", formatMessage(record));
            if (record.getThrown() != null) {
                payload.put("exception", record.getThrown().toString());
            }
            return payload.toString() + System.lineSeparator();
        }
    }
}

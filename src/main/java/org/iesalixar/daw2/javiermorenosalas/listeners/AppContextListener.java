package org.iesalixar.daw2.javiermorenosalas.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.iesalixar.daw2.javiermorenosalas.dao.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener que se ejecuta al iniciar y cerrar la aplicación.
 * Se usa para iniciar y cerrar la conexión a la base de datos.
 */
@WebListener
public class AppContextListener implements ServletContextListener {


    // Logger para trazar eventos usando SLF4J
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Iniciamos la conexión a la base de datos al arrancar la aplicación
        logger.info("Inicializando la aplicación y conectando a la base de datos...");
        DatabaseConnectionManager.getConnection();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cerramos la conexión a la base de datos cuando se destruye el contexto
        logger.info("Cerrando la conexión a la base de datos al apagar la aplicación...");
        DatabaseConnectionManager.closeConnection();
    }
}


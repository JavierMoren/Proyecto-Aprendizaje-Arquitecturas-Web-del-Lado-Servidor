package org.iesalixar.daw2.javiermorenosalas.dao;

import org.iesalixar.daw2.javiermorenosalas.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupermarketDAOImpl implements SupermarketDAO {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketDAOImpl.class);

    /**
     * Lista todos los supermercados almacenados en la base de datos.
     *
     * @return Una lista de todos los supermercados encontrados.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public List<Supermarket> listAllSupermarkets() throws SQLException {
        List<Supermarket> supermarkets = new ArrayList<>();
        String query = "SELECT * FROM supermarkets";

        logger.info("Inicio de listAllSupermarkets: Ejecutando consulta para listar todos los supermercados.");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                supermarkets.add(new Supermarket(id, name));
            }
            logger.info("Consulta ejecutada con exito.");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para obtener supermercados: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Finalizacion de listAllSupermarkets.");
        return supermarkets;
    }

    /**
     * Inserta un nuevo supermercado en la base de datos.
     *
     * @param supermarket El supermercado que se desea insertar.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public void insertSupermarket(Supermarket supermarket) throws SQLException {
        String query = "INSERT INTO supermarkets (name) VALUES (?)";

        logger.info("Inicio de insertSupermarket: Ejecutando consulta para insertar supermarket {}", supermarket);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, supermarket.getName());
            preparedStatement.executeUpdate();
            logger.info("Supermarket '{}' insertado con exito.", supermarket.getName());
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para insertar supermarket: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Finalizacion de insertSupermarket.");
    }

    /**
     * Actualiza un supermercado existente en la base de datos.
     *
     * @param supermarket El supermercado con los nuevos datos a actualizar.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public void updateSupermarket(Supermarket supermarket) throws SQLException {
        String query = "UPDATE supermarkets SET name = ? WHERE id = ?";

        logger.info("Inicio de updateSupermarket: Ejecutando consulta para actualizar supermarket {}", supermarket);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, supermarket.getName());
            preparedStatement.setInt(2, supermarket.getId());
            preparedStatement.executeUpdate();
            logger.info("Supermarket '{}' actualizado con exito.", supermarket.getName());
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para actualizar supermarket: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Finalizacion de updateSupermarket.");
    }

    /**
     * Elimina un supermercado de la base de datos.
     *
     * @param id El ID del supermercado a eliminar.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public void deleteSupermarket(int id) throws SQLException {
        String query = "DELETE FROM supermarkets WHERE id = ?";

        logger.info("Inicio de deleteSupermarket: Ejecutando consulta para eliminar supermarket con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            logger.info("Supermarket con ID {} eliminado con exito.", id);
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para eliminar supermarket con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

        logger.info("Finalizacion de deleteSupermarket.");
    }

    /**
     * Obtiene un supermercado por su ID de la base de datos.
     *
     * @param id El ID del supermercado a buscar.
     * @return Un objeto Supermarket si se encuentra el supermercado, o null si no se encuentra.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public Supermarket getSupermarketById(int id) throws SQLException {
        String query = "SELECT * FROM supermarkets WHERE id = ?";
        Supermarket supermarket = null;

        logger.info("Inicio de getSupermarketById: Ejecutando consulta para obtener supermarket con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                supermarket = new Supermarket(id, name);
                logger.info("Supermarket con ID {} encontrado: {}", id, supermarket);
            } else {
                logger.warn("No se encontro ningun supermarket con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la supermarket con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

        logger.info("Finalizacion de getSupermarketById.");
        return supermarket;
    }

}

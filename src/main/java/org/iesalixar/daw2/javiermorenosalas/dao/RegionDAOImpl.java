package org.iesalixar.daw2.javiermorenosalas.dao;

import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDAOImpl implements RegionDAO {

    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);

    /**
     * Lista todas las regiones de la base de datos.
     * @return Lista de regiones
     * @throws SQLException
     */
    public List<Region> listAllRegions() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String query = "SELECT * FROM regions";

        logger.info("Inicio de listAllRegions: Ejecutando consulta para listar todas las regiones");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                regions.add(new Region(id, code, name));
            }
            logger.info("Consulta ejecutada con éxito.");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para obtener regiones: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de listAllRegions.");
        return regions;
    }

    /**
     * Inserta una nueva región en la base de datos.
     * @param region Región a insertar
     * @throws SQLException
     */
    public void insertRegion(Region region) throws SQLException {
        String query = "INSERT INTO regions (code, name) VALUES (?, ?)";

        logger.info("Inicio de insertRegion: Ejecutando consulta para insertar región {}", region);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.executeUpdate();
            logger.info("Región {} insertada con éxito", region);
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para insertar región: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de insertRegion.");
    }

    /**
     * Actualiza una región existente en la base de datos.
     * @param region Región a actualizar
     * @throws SQLException
     */
    public void updateRegion(Region region) throws SQLException {
        String query = "UPDATE regions SET code = ?, name = ? WHERE id = ?";

        logger.info("Inicio de updateRegion: Ejecutando consulta para actualizar región {}", region);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.setInt(3, region.getId());
            preparedStatement.executeUpdate();
            logger.info("Región {} actualizada con éxito", region);
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para actualizar región: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de updateRegion.");
    }

    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     * @throws SQLException
     */
    public void deleteRegion(int id) throws SQLException {
        String query = "DELETE FROM regions WHERE id = ?";

        logger.info("Inicio de deleteRegion: Ejecutando consulta para eliminar región con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            logger.info("Región con ID {} eliminada con éxito", id);
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para eliminar región con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de deleteRegion.");
    }

    /**
     * Obtiene una región por su ID de la base de datos.
     * @param id ID de la región a buscar
     * @return Objeto Region si se encuentra la región, o null si no se encuentra
     * @throws SQLException
     */
    public Region getRegionById(int id) throws SQLException {
        String query = "SELECT * FROM regions WHERE id = ?";
        Region region = null;

        logger.info("Inicio de getRegionById: Ejecutando consulta para obtener región con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                region = new Region(id, code, name);
                logger.info("Región con ID {} encontrada: {}", id, region);
            } else {
                logger.warn("No se encontró ninguna región con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la región con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de getRegionById.");
        return region;
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsRegionByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";

        logger.info("Inicio de existsRegionByCode: Verificando si existe región con código {}", code);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                boolean exists = resultSet.getInt(1) > 0;
                logger.info("Verificación de existencia de región con código {}: {}", code, exists);
                return exists;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar si existe región con código {}: {}", code, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas, pero excluyendo una región con un ID específico.
     *
     * @param code el código de la región a verificar.
     * @param id   el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con el ID dado),
     *         false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";

        logger.info("Inicio de existsRegionByCodeAndNotId: Verificando si existe región con código {} excluyendo ID {}", code, id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                boolean exists = resultSet.getInt(1) > 0;
                logger.info("Verificación de existencia de región con código {} excluyendo ID {}: {}", code, id, exists);
                return exists;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar si existe región con código {} excluyendo ID {}: {}", code, id, e.getMessage(), e);
            throw e;
        }
    }
}

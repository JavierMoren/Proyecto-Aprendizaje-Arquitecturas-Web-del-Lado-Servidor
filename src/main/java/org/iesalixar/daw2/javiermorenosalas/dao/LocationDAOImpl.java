package org.iesalixar.daw2.javiermorenosalas.dao;

import org.iesalixar.daw2.javiermorenosalas.entity.Location;
import org.iesalixar.daw2.javiermorenosalas.entity.Province;
import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.iesalixar.daw2.javiermorenosalas.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements LocationDAO {

    private static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);

    /**
     * Lista todas las ubicaciones con sus respectivas provincias y supermercados.
     *
     * @return Una lista de todas las ubicaciones encontradas.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public List<Location> listAllLocations() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String query = "SELECT l.id, l.address, l.city, s.id AS supermarket_id, s.name AS supermarket_name, " +
                "p.id AS province_id, p.code AS province_code, p.name AS province_name " +
                "FROM locations l " +
                "INNER JOIN supermarkets s ON l.supermarket_id = s.id " +
                "INNER JOIN provinces p ON l.province_id = p.id";

        logger.info("Inicio de listAllLocations: Ejecutando consulta para listar todas las ubicaciones");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int locationId = resultSet.getInt("l.id");
                String address = resultSet.getString("l.address");
                String city = resultSet.getString("l.city");
                int supermarketId = resultSet.getInt("supermarket_id");
                String supermarketName = resultSet.getString("supermarket_name");
                int provinceId = resultSet.getInt("province_id");
                String provinceCode = resultSet.getString("province_code");
                String provinceName = resultSet.getString("province_name");

                Supermarket supermarket = new Supermarket(supermarketId, supermarketName);
                Province province = new Province(provinceId, provinceCode, provinceName, null);
                locations.add(new Location(locationId, address, city, supermarket, province));
            }
            logger.info("Consulta ejecutada con exito");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para obtener ubicaciones: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalizacion de listAllLocations");
        return locations;
    }

    /**
     * Inserta una nueva ubicacion en la base de datos.
     *
     * @param location La ubicacion que se desea insertar.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public void insertLocation(Location location) throws SQLException {
        String query = "INSERT INTO locations (address, city, supermarket_id, province_id) VALUES (?, ?, ?, ?)";

        logger.info("Inicio de insertLocation: Ejecutando consulta para insertar ubicacion");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, location.getAddress());
            preparedStatement.setString(2, location.getCity());
            preparedStatement.setInt(3, location.getSupermarket().getId());
            preparedStatement.setInt(4, location.getProvince().getId());

            preparedStatement.executeUpdate();
            logger.info("Consulta ejecutada con exito");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para insertar ubicacion: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalizacion de insertLocation");
    }

    /**
     * Actualiza una ubicacion existente en la base de datos.
     *
     * @param location La ubicacion con los nuevos datos.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public void updateLocation(Location location) throws SQLException {
        String query = "UPDATE locations SET address = ?, city = ?, supermarket_id = ?, province_id = ? WHERE id = ?";

        logger.info("Inicio de updateLocation: Ejecutando consulta para actualizar ubicacion");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, location.getAddress());
            preparedStatement.setString(2, location.getCity());
            preparedStatement.setInt(3, location.getSupermarket().getId());
            preparedStatement.setInt(4, location.getProvince().getId());
            preparedStatement.setInt(5, location.getId());

            preparedStatement.executeUpdate();
            logger.info("Consulta ejecutada con exito");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para actualizar ubicacion: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalizacion de updateLocation");
    }

    /**
     * Elimina una ubicacion de la base de datos.
     *
     * @param id El ID de la ubicacion a eliminar.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public void deleteLocation(int id) throws SQLException {
        String query = "DELETE FROM locations WHERE id = ?";

        logger.info("Inicio de deleteLocation: Ejecutando consulta para eliminar ubicacion con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            logger.info("Ubicacion con ID {} eliminada con exito", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar la ubicacion con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        logger.info("Finalizacion de deleteLocation");
    }

    /**
     * Obtiene una ubicacion por su ID de la base de datos.
     *
     * @param id ID de la ubicacion a buscar.
     * @return Un objeto Location si se encuentra la ubicacion, o null si no se encuentra.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public Location getLocationById(int id) throws SQLException {
        String query = "SELECT * FROM locations l " +
                "INNER JOIN supermarkets s ON s.id = l.supermarket_id " +
                "INNER JOIN provinces p ON p.id = l.province_id " +
                "INNER JOIN regions r ON r.id = p.id_region " +
                "WHERE l.id = ?";

        Location location = null;

        logger.info("Inicio de getLocationById: Ejecutando consulta para obtener ubicacion con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Obtener los campos de la ubicacion
                String address = resultSet.getString("l.address");
                String city = resultSet.getString("l.city");
                int supermarketId = resultSet.getInt("l.supermarket_id");
                int provinceId = resultSet.getInt("l.province_id");

                // Obtener informacion del supermercado
                String supermarketName = resultSet.getString("s.name");
                Supermarket supermarket = new Supermarket(supermarketId, supermarketName);

                // Obtener informacion de la provincia y la region
                String provinceCode = resultSet.getString("p.code");
                String provinceName = resultSet.getString("p.name");
                int regionId = resultSet.getInt("r.id");
                String regionCode = resultSet.getString("r.code");
                String regionName = resultSet.getString("r.name");
                Region region = new Region(regionId, regionCode, regionName);

                Province province = new Province(provinceId, provinceCode, provinceName, region);

                // Crear la ubicacion
                location = new Location(id, address, city, supermarket, province);

                logger.info("Ubicacion con ID {} encontrada: {}", id, location);
            } else {
                logger.warn("No se encontro ninguna ubicacion con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la ubicacion con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

        logger.info("Finalizacion de getLocationById");
        return location;
    }
}

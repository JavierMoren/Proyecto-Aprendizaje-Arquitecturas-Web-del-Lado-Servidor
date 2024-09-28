package org.iesalixar.daw2.javiermorenosalas.dao;

import org.iesalixar.daw2.javiermorenosalas.entity.Province;
import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDAOImpl implements ProvinceDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    /**
     * Lista todas las provincias de la base de datos.
     * Realiza una consulta SQL para obtener todas las provincias unidas a sus respectivas regiones.
     * @return Lista de provincias obtenidas de la base de datos.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public List<Province> listAllProvinces() throws SQLException {
        List<Province> Provinces = new ArrayList<>();
        String query = "SELECT * FROM provinces p INNER JOIN regions r on r.id = p.id_region";

        // Registramos el inicio del metodo
        logger.info("Inicio de listAllProvinces: Ejecutando consulta para listar todas las provincias y regiones");

        // Obtener una nueva conexión para cada operación
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int prov_id = resultSet.getInt("p.id");
                String prov_code = resultSet.getString("p.code");
                String prov_name = resultSet.getString("p.name");
                int prov_id_region = resultSet.getInt("p.id_region");
                int reg_id = resultSet.getInt("r.id");
                String reg_name = resultSet.getString("r.name");
                String reg_code = resultSet.getString("r.code");
                Region region = new Region(reg_id, reg_code, reg_name);
                Provinces.add(new Province(prov_id, prov_code, prov_name, region));
            }
            // Registramos la cantidad de provincias obtenidas
            logger.info("Consulta ejecutada con éxito");

        } catch (SQLException e) {
            // Si ocurre una excepción, registramos el error
            logger.error("Error al ejecutar la consulta para obtener provincias: {}", e.getMessage(), e);
            throw e;
        }
        // Registramos la finalización del metodo
        logger.info("Finalización de listAllProvinces.");
        return Provinces;
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Objeto Province que contiene los datos de la nueva provincia.
     * @throws SQLException Si ocurre un error en la inserción a la base de datos.
     */
    public void insertProvince(Province province) throws SQLException {
        String query = "INSERT INTO provinces (code, name, id_region) VALUES (?, ?, ?)";

        logger.info("Inicio de insertProvince: Ejecutando consulta para insertar provincias");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Obtener el ID de la región desde el objeto Region
            int regionId = province.getRegion().getId();

            // Configurar los parámetros de la consulta
            preparedStatement.setString(1, province.getCode());
            preparedStatement.setString(2, province.getName());
            preparedStatement.setInt(3, regionId);  // Usar el id de la región

            // Ejecutar la consulta
            preparedStatement.executeUpdate();

            logger.info("Consulta ejecutada con exito");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para insertar provincias: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalizacion de insertProvince");
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Objeto Province que contiene los nuevos datos de la provincia.
     * @throws SQLException Si ocurre un error en la actualización a la base de datos.
     */
    public void updateProvince(Province province) throws SQLException {
        String query = "UPDATE provinces SET code = ?, name = ?, id_region = ? WHERE id = ?";

        logger.info("Inicio de updateProvince: Ejecutando consulta para actualizar provincias");

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Obtener el ID de la región desde el objeto Region
            int regionId = province.getRegion().getId();

            // Configurar los parámetros de la consulta
            preparedStatement.setString(1, province.getCode());
            preparedStatement.setString(2, province.getName());
            preparedStatement.setInt(3, regionId);  // Actualizar el id_region
            preparedStatement.setInt(4, province.getId());  // El ID de la provincia para la cláusula WHERE

            // Ejecutar la consulta
            preparedStatement.executeUpdate();
            logger.info("Consulta ejecutada con exito");
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta para actualizar provincias: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Finalizacion de updateProvince");
    }

    /**
     * Elimina una provincia de la base de datos según su ID.
     * @param id ID de la provincia a eliminar.
     * @throws SQLException Si ocurre un error en la eliminación a la base de datos.
     */
    public void deleteProvince(int id) throws SQLException {
        String query = "DELETE FROM provinces WHERE id = ?";

        logger.info("Inicio de deleteProvince: Ejecutando consulta para eliminar la provincia con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            logger.info("Provincia con ID {} eliminada con éxito", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de deleteProvince");
    }

    /**
     * Obtiene una provincia por su ID de la base de datos.
     * @param id ID de la provincia a buscar.
     * @return Un objeto Province si se encuentra la provincia, o null si no se encuentra.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public Province getProvinceById(int id) throws SQLException {
        String query = "SELECT * FROM provinces p INNER JOIN regions r on r.id = p.id_region WHERE p.id = ?";
        Province Province = null;

        logger.info("Inicio de getProvinceById: Ejecutando consulta para obtener provincia con ID {}", id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Obtiene los campos de la provincia
                String code = resultSet.getString("p.code");
                String name = resultSet.getString("p.name");
                int id_region = resultSet.getInt("r.id");
                Region region = new Region(id_region, code, name);

                Province = new Province(id, code, name, region);
                logger.info("Provincia con ID {} encontrada: {}", id, Province);
            } else {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la provincia con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        logger.info("Finalización de getProvinceById");
        return Province;
    }

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     * @param code Código de la provincia a verificar.
     * @return true si existe una provincia con el código dado, false de lo contrario.
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsProvinceByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?";

        logger.info("Inicio de existsProvinceByCode: Verificando si existe provincia con código {}", code);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code.toUpperCase());

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                boolean exists = resultSet.getInt(1) > 0;

                logger.info("Verificación de existencia de provincia con código {}: {}", code, exists);
                return exists;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar si existe provincia con código {}: {}", code, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos,
     * excluyendo una provincia con un ID específico.
     * @param code Código de la provincia a verificar.
     * @param id ID de la provincia a excluir de la verificación.
     * @return true si existe una provincia con el código dado, false de lo contrario.
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ? AND id != ?";

        logger.info("Inicio de existsProvinceByCodeAndNotId: Verificando si existe provincia con código {} excluyendo ID {}", code, id);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                boolean exists = resultSet.getInt(1) > 0;

                logger.info("Verificación de existencia de provincia con código {} excluyendo ID {}: {}", code, id, exists);
                return exists;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar si existe provincia con código {} excluyendo ID {}: {}", code, id, e.getMessage(), e);
            throw e;
        }
    }

}

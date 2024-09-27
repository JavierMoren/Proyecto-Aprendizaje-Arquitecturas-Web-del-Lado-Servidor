package org.iesalixar.daw2.javiermorenosalas.dao;

import org.iesalixar.daw2.javiermorenosalas.entity.Province;
import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.iesalixar.daw2.javiermorenosalas.dao.RegionDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDAOImpl implements ProvinceDAO {

    /**
     * Lista todas las provincias de la base de datos.
     * Realiza una consulta SQL para obtener todas las provincias unidas a sus respectivas regiones.
     * @return Lista de provincias obtenidas de la base de datos.
     * @throws SQLException Si ocurre un error en la consulta a la base de datos.
     */
    public List<Province> listAllProvinces() throws SQLException {
        List<Province> Provinces = new ArrayList<>();
        String query = "SELECT * FROM provinces p INNER JOIN regions r on r.id = p.id_region";
        // Lo que he hecho aqui ha sido hacer una consulta completa para poder sacar cada dato y poder introducirlo en un region

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
        }
        return Provinces;
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Objeto Province que contiene los datos de la nueva provincia.
     * @throws SQLException Si ocurre un error en la inserción a la base de datos.
     */
    public void insertProvince(Province province) throws SQLException {
        String query = "INSERT INTO provinces (code, name, id_region) VALUES (?, ?, ?)";

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
        }
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Objeto Province que contiene los nuevos datos de la provincia.
     * @throws SQLException Si ocurre un error en la actualización a la base de datos.
     */
    public void updateProvince(Province province) throws SQLException {
        String query = "UPDATE provinces SET code = ?, name = ?, id_region = ? WHERE id = ?";

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
        }
    }

    /**
     * Elimina una provincia de la base de datos según su ID.
     * @param id ID de la provincia a eliminar.
     * @throws SQLException Si ocurre un error en la eliminación a la base de datos.
     */
    public void deleteProvince(int id) throws SQLException {
        String query = "DELETE FROM provinces WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
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
            }
        }
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
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
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
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

}

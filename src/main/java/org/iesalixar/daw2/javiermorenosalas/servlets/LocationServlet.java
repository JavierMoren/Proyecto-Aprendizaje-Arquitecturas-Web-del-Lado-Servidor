package org.iesalixar.daw2.javiermorenosalas.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.javiermorenosalas.dao.*;
import org.iesalixar.daw2.javiermorenosalas.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja las operaciones CRUD para la entidad `Location`.
 * Este servlet responde a las solicitudes relacionadas con la gestión de ubicaciones
 * y utiliza los DAOs (`LocationDAO`, `SupermarketDAO`, `ProvinceDAO`) para interactuar con la base de datos.
 *
 * Las principales funcionalidades que cubre este servlet incluyen:
 * - Listar las ubicaciones.
 * - Insertar una nueva ubicación.
 * - Actualizar una ubicación existente.
 * - Eliminar una ubicación.
 *
 * Este servlet está mapeado en la URL `/locations`.
 */
@WebServlet("/locations")
public class LocationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LocationServlet.class);

    // DAOs para gestionar las operaciones de las ubicaciones, supermercados y provincias
    private LocationDAO locationDAO;
    private SupermarketDAO supermarketDAO;
    private ProvinceDAO provinceDAO;

    @Override
    public void init() throws ServletException {
        try {
            locationDAO = new LocationDAOImpl();
            supermarketDAO = new SupermarketDAOImpl();
            provinceDAO = new ProvinceDAOImpl();
            logger.info("DAOs inicializados correctamente.");
        } catch (Exception e) {
            logger.error("Error al inicializar los DAOs: {}", e.getMessage(), e);
            throw new ServletException("Error al inicializar los DAOs", e);
        }
    }

    /**
     * Maneja las solicitudes HTTP GET. Según el parámetro "action" recibido en la solicitud,
     * se invoca el método correspondiente (listar, mostrar formulario de edición o creación).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "new":
                    showNewForm(request, response);  // Mostrar formulario para nueva ubicación
                    break;
                case "edit":
                    showEditForm(request, response);  // Mostrar formulario para editar ubicación
                    break;
                default:
                    listLocations(request, response);  // Listar todas las ubicaciones
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en doGet: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Maneja las solicitudes HTTP POST. Según el parámetro "action", decide si insertar,
     * actualizar o eliminar una ubicación.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insert":
                    insertLocation(request, response);  // Insertar nueva ubicación
                    break;
                case "update":
                    updateLocation(request, response);  // Actualizar ubicación existente
                    break;
                case "delete":
                    deleteLocation(request, response);  // Eliminar ubicación
                    break;
                default:
                    listLocations(request, response);  // Listar todas las ubicaciones
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en doPost: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Lista todas las ubicaciones y las pasa como atributo a la vista `location.jsp`.
     */
    private void listLocations(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Location> listLocations = locationDAO.listAllLocations();
        request.setAttribute("listLocations", listLocations);
        logger.info("Listando ubicaciones: {} ubicaciones encontradas.", listLocations.size());
        request.getRequestDispatcher("location.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para crear una nueva ubicación.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Obtener todos los supermercados y provincias
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets();
        List<Province> listProvinces = provinceDAO.listAllProvinces();

        // Pasar la lista de supermercados y provincias a la vista
        request.setAttribute("listSupermarkets", listSupermarkets);
        request.setAttribute("listProvinces", listProvinces);

        // Mostrar el formulario para crear una nueva ubicación
        request.getRequestDispatcher("location-form.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar una ubicación existente.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Obtener la ubicación existente por su ID
        Location existingLocation = locationDAO.getLocationById(id);

        // Obtener todos los supermercados y provincias
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets();
        List<Province> listProvinces = provinceDAO.listAllProvinces();

        // Pasar la ubicación, supermercados y provincias a la vista
        request.setAttribute("location", existingLocation);
        request.setAttribute("listSupermarkets", listSupermarkets);
        request.setAttribute("listProvinces", listProvinces);

        // Mostrar el formulario para editar la ubicación
        request.getRequestDispatcher("location-form.jsp").forward(request, response);
    }

    /**
     * Inserta una nueva ubicación en la base de datos.
     */
    private void insertLocation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String address = request.getParameter("address").trim();
        String city = request.getParameter("city").trim();
        String supermarketIdStr = request.getParameter("supermarket_id");
        String provinceIdStr = request.getParameter("province_id");

        // Verificar que los campos obligatorios estén presentes
        if (address.isEmpty() || city.isEmpty() || supermarketIdStr.isEmpty() || provinceIdStr.isEmpty()) {
            logger.warn("Intento de insertar ubicación fallido: campos vacíos.");
            request.setAttribute("errorMessage", "La dirección, ciudad, supermercado y provincia no pueden estar vacíos.");
            showNewForm(request, response);
            return;
        }

        // Convertir los valores a enteros
        int supermarketId = Integer.parseInt(supermarketIdStr);
        int provinceId = Integer.parseInt(provinceIdStr);

        // Verificar si el supermercado y la provincia existen
        Supermarket supermarket = supermarketDAO.getSupermarketById(supermarketId);
        Province province = provinceDAO.getProvinceById(provinceId);

        if (supermarket == null || province == null) {
            logger.warn("Supermercado o provincia no válidos.");
            request.setAttribute("errorMessage", "El supermercado o la provincia seleccionada no existen.");
            showNewForm(request, response);
            return;
        }

        // Crear nueva ubicación y asociarla a supermercado y provincia
        Location newLocation = new Location(address, city, supermarket, province);
        locationDAO.insertLocation(newLocation);
        logger.info("Ubicación insertada: {} - {}", address, city);

        response.sendRedirect("locations");
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     */
    private void updateLocation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String address = request.getParameter("address").trim();
        String city = request.getParameter("city").trim();
        String supermarketIdStr = request.getParameter("supermarket_id");
        String provinceIdStr = request.getParameter("province_id");

        if (address.isEmpty() || city.isEmpty() || supermarketIdStr.isEmpty() || provinceIdStr.isEmpty()) {
            logger.warn("Intento de actualizar ubicación fallido: campos vacíos.");
            request.setAttribute("errorMessage", "La dirección, ciudad, supermercado y provincia no pueden estar vacíos.");
            showEditForm(request, response);
            return;
        }

        int supermarketId = Integer.parseInt(supermarketIdStr);
        int provinceId = Integer.parseInt(provinceIdStr);

        // Verificar si el supermercado y la provincia existen
        Supermarket supermarket = supermarketDAO.getSupermarketById(supermarketId);
        Province province = provinceDAO.getProvinceById(provinceId);

        if (supermarket == null || province == null) {
            logger.warn("Supermercado o provincia no válidos.");
            request.setAttribute("errorMessage", "El supermercado o la provincia seleccionada no existen.");
            showEditForm(request, response);
            return;
        }

        // Actualizar la ubicación existente
        Location location = new Location(id, address, city, supermarket, province);
        locationDAO.updateLocation(location);
        logger.info("Ubicación actualizada: {} - {}", address, city);

        response.sendRedirect("locations");
    }

    /**
     * Elimina una ubicación existente de la base de datos.
     */
    private void deleteLocation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        locationDAO.deleteLocation(id);
        logger.info("Ubicación eliminada: ID {}", id);
        response.sendRedirect("locations");
    }

}

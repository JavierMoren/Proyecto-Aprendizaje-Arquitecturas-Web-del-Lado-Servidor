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

@WebServlet("/locations")
public class LocationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LocationServlet.class);

    // DAOs para gestionar las operaciones de ubicaciones, supermercados y provincias
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
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error en la solicitud.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
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
                    logger.info("Accion: mostrar formulario de nueva ubicacion.");
                    showNewForm(request, response);  // Mostrar formulario para nueva ubicacion
                    break;
                case "edit":
                    logger.info("Accion: mostrar formulario de edicion de ubicacion.");
                    showEditForm(request, response);  // Mostrar formulario para editar ubicacion
                    break;
                default:
                    logger.info("Accion: listar ubicaciones.");
                    listLocations(request, response);  // Listar ubicaciones
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
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error en la solicitud.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insert":
                    logger.info("Accion: insertar nueva ubicacion.");
                    insertLocation(request, response);  // Insertar nueva ubicacion
                    break;
                case "update":
                    logger.info("Accion: actualizar ubicacion.");
                    updateLocation(request, response);  // Actualizar ubicacion existente
                    break;
                case "delete":
                    logger.info("Accion: eliminar ubicacion.");
                    deleteLocation(request, response);  // Eliminar ubicacion
                    break;
                default:
                    logger.info("Accion no reconocida, listando ubicaciones.");
                    listLocations(request, response);  // Listar ubicaciones
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en doPost: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Lista todas las ubicaciones y las pasa como atributo a la vista `location.jsp`.
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws SQLException Si ocurre un error en la consulta de la base de datos.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
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
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error en la solicitud.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     * @throws SQLException Si ocurre un error al obtener los datos necesarios de la base de datos.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets();
        List<Province> listProvinces = provinceDAO.listAllProvinces();

        request.setAttribute("listSupermarkets", listSupermarkets);
        request.setAttribute("listProvinces", listProvinces);

        logger.info("Mostrando formulario de nueva ubicacion.");
        request.getRequestDispatcher("location-form.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar una ubicación existente.
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws SQLException Si ocurre un error al obtener la ubicación de la base de datos.
     * @throws ServletException Si ocurre un error en la solicitud.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Location existingLocation = locationDAO.getLocationById(id);
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets();
        List<Province> listProvinces = provinceDAO.listAllProvinces();

        request.setAttribute("location", existingLocation);
        request.setAttribute("listSupermarkets", listSupermarkets);
        request.setAttribute("listProvinces", listProvinces);

        logger.info("Mostrando formulario de edicion para la ubicacion con ID: {}", id);
        request.getRequestDispatcher("location-form.jsp").forward(request, response);
    }

    /**
     * Inserta una nueva ubicación en la base de datos.
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws SQLException Si ocurre un error al insertar la ubicación en la base de datos.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     * @throws ServletException Si ocurre un error en la solicitud.
     */
    private void insertLocation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String address = request.getParameter("address").trim();
        String city = request.getParameter("city").trim();
        String supermarketIdStr = request.getParameter("supermarket_id");
        String provinceIdStr = request.getParameter("province_id");

        if (address.isEmpty() || city.isEmpty() || supermarketIdStr.isEmpty() || provinceIdStr.isEmpty()) {
            logger.warn("Intento de insertar ubicación fallido: campos vacíos.");
            request.setAttribute("errorMessage", "La dirección, ciudad, supermercado y provincia no pueden estar vacíos.");
            showNewForm(request, response);
            return;
        }

        int supermarketId = Integer.parseInt(supermarketIdStr);
        int provinceId = Integer.parseInt(provinceIdStr);

        Supermarket supermarket = supermarketDAO.getSupermarketById(supermarketId);
        Province province = provinceDAO.getProvinceById(provinceId);

        if (supermarket == null || province == null) {
            logger.warn("Supermercado o provincia no válidos.");
            request.setAttribute("errorMessage", "El supermercado o la provincia seleccionada no existen.");
            showNewForm(request, response);
            return;
        }

        Location newLocation = new Location(address, city, supermarket, province);
        locationDAO.insertLocation(newLocation);
        logger.info("Ubicación insertada: {} - {}", address, city);

        response.sendRedirect("locations");
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws SQLException Si ocurre un error al actualizar la ubicación en la base de datos.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     * @throws ServletException Si ocurre un error en la solicitud.
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

        Supermarket supermarket = supermarketDAO.getSupermarketById(supermarketId);
        Province province = provinceDAO.getProvinceById(provinceId);

        if (supermarket == null || province == null) {
            logger.warn("Supermercado o provincia no válidos.");
            request.setAttribute("errorMessage", "El supermercado o la provincia seleccionada no existen.");
            showEditForm(request, response);
            return;
        }

        Location location = new Location(id, address, city, supermarket, province);
        locationDAO.updateLocation(location);
        logger.info("Ubicación actualizada: {} - {}", address, city);

        response.sendRedirect("locations");
    }

    /**
     * Elimina una ubicación existente de la base de datos.
     *
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws SQLException Si ocurre un error al eliminar la ubicación de la base de datos.
     * @throws IOException Si ocurre un error en la lectura o escritura de la solicitud.
     */
    private void deleteLocation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        locationDAO.deleteLocation(id);
        logger.info("Ubicación eliminada: ID {}", id);
        response.sendRedirect("locations");
    }
}

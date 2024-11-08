package org.iesalixar.daw2.javiermorenosalas.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.javiermorenosalas.dao.ProvinceDAO;
import org.iesalixar.daw2.javiermorenosalas.dao.ProvinceDAOImpl;
import org.iesalixar.daw2.javiermorenosalas.entity.Province;
import org.iesalixar.daw2.javiermorenosalas.dao.RegionDAOImpl;
import org.iesalixar.daw2.javiermorenosalas.dao.RegionDAO;
import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja las operaciones CRUD para la entidad `Province`.
 * Este servlet responde a las solicitudes relacionadas con la gestión de provincias
 * y utiliza los DAOs (`ProvinceDAO` y `RegionDAO`) para interactuar con la base de datos.
 *
 * Las principales funcionalidades que cubre este servlet incluyen:
 * - Listar las provincias.
 * - Insertar una nueva provincia.
 * - Actualizar una provincia existente.
 * - Eliminar una provincia.
 *
 * Este servlet está mapeado en la URL `/provinces`.
 */
@WebServlet("/provinces")
public class ProvinceServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceServlet.class);

    // DAOs para gestionar las operaciones de las provincias y regiones en la base de datos
    private RegionDAO regionDAO;
    private ProvinceDAO provinceDAO;

    /**
     * Inicializa los DAOs al arrancar el servlet. Si ocurre algún error durante la
     * inicialización, se lanza una excepción `ServletException`.
     *
     * @throws ServletException si ocurre algún error al inicializar los DAOs.
     */
    @Override
    public void init() throws ServletException {
        try {
            regionDAO = new RegionDAOImpl();
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
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Si el parámetro "action" es nulo, se establece una acción predeterminada
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "new":
                    logger.info("Acción solicitada: mostrar formulario para nueva provincia.");
                    showNewForm(request, response);  // Mostrar formulario para nueva provincia
                    break;
                case "edit":
                    logger.info("Acción solicitada: mostrar formulario para editar provincia.");
                    showEditForm(request, response);  // Mostrar formulario para editar provincia
                    break;
                default:
                    logger.info("Acción solicitada: listar todas las provincias.");
                    listProvinces(request, response);  // Listar todas las provincias
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en doGet: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Maneja las solicitudes HTTP POST. Según el parámetro "action", decide si insertar,
     * actualizar o eliminar una provincia.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insert":
                    logger.info("Acción solicitada: insertar nueva provincia.");
                    insertProvince(request, response);  // Insertar nueva provincia
                    break;
                case "update":
                    logger.info("Acción solicitada: actualizar provincia existente.");
                    updateProvince(request, response);  // Actualizar provincia existente
                    break;
                case "delete":
                    logger.info("Acción solicitada: eliminar provincia.");
                    deleteProvince(request, response);  // Eliminar provincia
                    break;
                default:
                    logger.warn("Acción no reconocida. Listar todas las provincias.");
                    listProvinces(request, response);  // Listar todas las provincias
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en doPost: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Lista todas las provincias y las pasa como atributo a la vista `province.jsp`.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de entrada/salida.
     * @throws ServletException en caso de error en el servlet.
     */
    private void listProvinces(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Province> listProvinces = provinceDAO.listAllProvinces();
        request.setAttribute("listProvinces", listProvinces);  // Pasar la lista de provincias a la vista
        logger.info("Listando provincias: {} provincias encontradas.", listProvinces.size());
        request.getRequestDispatcher("province.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para crear una nueva provincia.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de entrada/salida.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Obtener todas las regiones
        List<Region> listRegions = regionDAO.listAllRegions();
        // Pasar la lista de regiones a la vista
        request.setAttribute("listRegions", listRegions);

        // Mostrar el formulario para crear una nueva provincia
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }


    /**
     * Muestra el formulario para editar una provincia existente.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de entrada/salida.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Obtener la provincia existente por su ID
        Province existingProvince = provinceDAO.getProvinceById(id);

        // Obtener todas las regiones
        List<Region> listRegions = regionDAO.listAllRegions();

        // Pasar la provincia y la lista de regiones a la vista
        request.setAttribute("province", existingProvince);
        request.setAttribute("listRegions", listRegions);

        // Mostrar el formulario para editar la provincia
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }


    /**
     * Inserta una nueva provincia en la base de datos tras validar los datos del formulario.
     *
     * @param request  La solicitud HTTP con los datos del formulario.
     * @param response La respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException en caso de error en la base de datos.
     * @throws IOException en caso de error de entrada/salida.
     * @throws ServletException en caso de error en el servlet.
     */
    private void insertProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String idStr = request.getParameter("id");  // Este valor será nulo o vacío en la creación
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();
        String regionIdStr = request.getParameter("id_region");

        // Verificar que los campos obligatorios estén presentes
        if (code.isEmpty() || name.isEmpty() || regionIdStr.isEmpty()) {
            logger.warn("Intento de insertar provincia fallido: campos vacíos.");
            request.setAttribute("errorMessage", "El código, el nombre y el id de la región no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Convertir los valores necesarios a enteros
        int regionId = Integer.parseInt(regionIdStr);

        // Verificar si la región existe
        Region region = regionDAO.getRegionById(regionId);
        if (region == null) {
            logger.warn("La región seleccionada no existe para la nueva provincia con código: {}", code);
            request.setAttribute("errorMessage", "La región seleccionada no existe.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Crear nueva provincia y asociarla a la región
        Province newProvince = new Province(code, name, region);  // No se necesita 'id' aquí
        provinceDAO.insertProvince(newProvince);
        logger.info("Provincia insertada: {} - {}", code, name);

        response.sendRedirect("provinces");
    }

    /**
     * Actualiza una provincia existente en la base de datos tras validar los datos del formulario.
     *
     * @param request  La solicitud HTTP con los datos del formulario.
     * @param response La respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException en caso de error en la base de datos.
     * @throws IOException en caso de error de entrada/salida.
     * @throws ServletException en caso de error en el servlet.
     */
    private void updateProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        // Aquí el 'id' es obligatorio, ya que estás actualizando una provincia existente
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            logger.warn("Intento de actualizar provincia fallido: ID no proporcionado.");
            request.setAttribute("errorMessage", "El ID de la provincia es obligatorio para la actualización.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        int id = Integer.parseInt(idStr);
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();
        String regionIdStr = request.getParameter("id_region");

        if (code.isEmpty() || name.isEmpty() || regionIdStr.isEmpty()) {
            logger.warn("Intento de actualizar provincia fallido: campos vacíos.");
            request.setAttribute("errorMessage", "El código, el nombre y el id de la región no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        int regionId = Integer.parseInt(regionIdStr);

        // Verificar si la región existe
        Region region = regionDAO.getRegionById(regionId);
        if (region == null) {
            logger.warn("La región seleccionada no existe para la provincia con ID: {}", id);
            request.setAttribute("errorMessage", "La región seleccionada no existe.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Actualizar la provincia
        Province updatedProvince = new Province(id, code, name, region);
        provinceDAO.updateProvince(updatedProvince);
        logger.info("Provincia actualizada: ID = {}, Código = {}, Nombre = {}", id, code, name);

        response.sendRedirect("provinces");
    }

    /**
     * Elimina una provincia de la base de datos.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws SQLException en caso de error en la base de datos.
     * @throws IOException en caso de error de entrada/salida.
     */
    private void deleteProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        provinceDAO.deleteProvince(id);  // Eliminar provincia
        logger.info("Provincia eliminada: ID = {}", id);
        response.sendRedirect("provinces");  // Redirigir al listado de provincias
    }
}

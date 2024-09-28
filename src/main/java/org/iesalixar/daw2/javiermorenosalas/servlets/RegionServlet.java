package org.iesalixar.daw2.javiermorenosalas.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.javiermorenosalas.dao.RegionDAO;
import org.iesalixar.daw2.javiermorenosalas.dao.RegionDAOImpl;
import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.slf4j.Logger;      // Importa el logger
import org.slf4j.LoggerFactory; // Importa el factory para crear instancias del logger

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@WebServlet("/regions")
public class RegionServlet extends HttpServlet {

    // Logger para registrar eventos
    private static final Logger logger = LoggerFactory.getLogger(RegionServlet.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    private RegionDAO regionDAO;

    @Override
    public void init() throws ServletException {
        try {
            regionDAO = new RegionDAOImpl();
            logger.info("RegionDAO inicializado correctamente.");  // Logueo al inicializar
        } catch (Exception e) {
            logger.error("Error al inicializar el RegionDAO", e);
            throw new ServletException("Error al inicializar el RegionDAO", e);
        }
    }

    /**
     * Maneja las solicitudes GET al servlet. Según el parámetro "action", decide qué método invocar.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            if (action == null) {
                action = "list"; // Acción predeterminada
            }

            switch (action) {
                case "new":
                    showNewForm(request, response);  // Mostrar formulario para nueva región
                    break;
                case "edit":
                    showEditForm(request, response);  // Mostrar formulario para editar región
                    break;
                default:
                    listRegions(request, response);   // Listar todas las regiones
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en la consulta de las regiones", ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Maneja las solicitudes POST al servlet. Según el parámetro "action", decide qué método invocar.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertRegion(request, response);  // Insertar nueva región
                    break;
                case "update":
                    updateRegion(request, response);  // Actualizar región existente
                    break;
                case "delete":
                    deleteRegion(request, response);  // Eliminar región
                    break;
                default:
                    listRegions(request, response);   // Listar todas las regiones
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error en la operación POST de regiones", ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Lista todas las regiones y las pasa como atributo a la vista `region.jsp`.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void listRegions(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Region> listRegions = regionDAO.listAllRegions(); // Obtener todas las regiones desde el DAO
        request.setAttribute("listRegions", listRegions);      // Pasar la lista de regiones a la vista
        request.getRequestDispatcher("region.jsp").forward(request, response); // Redirigir a la página JSP
        logger.info("Listando todas las regiones."); // Logueo al listar regiones
    }

    /**
     * Muestra el formulario para crear una nueva región.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("region-form.jsp").forward(request, response); // Redirige a la vista para nueva región
        logger.info("Mostrando formulario para nueva región."); // Logueo al mostrar el formulario
    }

    /**
     * Muestra el formulario para editar una región existente.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Region existingRegion = regionDAO.getRegionById(id);   // Obtener región por ID desde el DAO
        request.setAttribute("region", existingRegion);        // Pasar la región a la vista
        request.getRequestDispatcher("region-form.jsp").forward(request, response); // Redirigir a la vista para editar
        logger.info("Mostrando formulario para editar la región con ID: {}", id); // Logueo al mostrar el formulario de edición
    }

    /**
     * Inserta una nueva región en la base de datos después de realizar validaciones.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void insertRegion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String code = request.getParameter("code").trim().toUpperCase(); // Convertir a mayúsculas
        String name = request.getParameter("name").trim();

        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            logger.warn("Intento de insertar región fallido: campos vacíos."); // Logueo de advertencia
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }

        // Validar si el código ya existe ignorando mayúsculas
        if (regionDAO.existsRegionByCode(code)) {
            request.setAttribute("errorMessage", "El código de la región ya existe.");
            logger.warn("Intento de insertar región fallido: código ya existe."); // Logueo de advertencia
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }

        Region newRegion = new Region(code, name);
        try {
            regionDAO.insertRegion(newRegion);
            logger.info("Región insertada: {} - {}", code, name); // Logueo de éxito
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                logger.warn("Error al insertar región: código no único."); // Logueo de advertencia
                request.getRequestDispatcher("region-form.jsp").forward(request, response);
            } else {
                logger.error("Error al insertar región", e); // Logueo de error
                throw e;
            }
        }
        response.sendRedirect("regions"); // Redirigir al listado de regiones
    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void updateRegion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();

        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            logger.warn("Intento de actualizar región fallido: campos vacíos."); // Logueo de advertencia
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }

        Region existingRegion = regionDAO.getRegionById(id);
        if (existingRegion != null && !existingRegion.getCode().equals(code) && regionDAO.existsRegionByCode(code)) {
            request.setAttribute("errorMessage", "El código de la región ya existe.");
            logger.warn("Intento de actualizar región fallido: código ya existe."); // Logueo de advertencia
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }

        Region updatedRegion = new Region(id, code, name);
        try {
            regionDAO.updateRegion(updatedRegion);
            logger.info("Región actualizada: {} - {}", code, name); // Logueo de éxito
        } catch (SQLException e) {
            logger.error("Error al actualizar región", e); // Logueo de error
            throw e;
        }
        response.sendRedirect("regions"); // Redirigir al listado de regiones
    }

    /**
     * Elimina una región de la base de datos según su ID.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void deleteRegion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            regionDAO.deleteRegion(id);  // Eliminar región usando el DAO
            logger.info("Región eliminada con ID: {}", id); // Logueo de éxito
            response.sendRedirect("regions"); // Redirigir al listado de regiones
        } catch (SQLException e) {
            // Manejo de la excepción de restricción de clave foránea
            if (e.getSQLState().equals("23000")) { // Verifica si es un error de clave foránea
                request.setAttribute("errorMessage", "No se puede eliminar la región porque está referenciada por una o más provincias.");
                logger.warn("Intento de eliminar región fallido: clave foránea violada."); // Logueo de advertencia
                RequestDispatcher dispatcher = request.getRequestDispatcher("region.jsp");
                dispatcher.forward(request, response); // Redirigir a la vista con el mensaje de error
            } else {
                logger.error("Error al eliminar región", e); // Logueo de error
                throw e; // Lanza otra excepción si no es de clave foránea
            }
        }
    }
}

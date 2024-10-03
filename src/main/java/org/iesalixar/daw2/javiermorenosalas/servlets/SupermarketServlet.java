package org.iesalixar.daw2.javiermorenosalas.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.javiermorenosalas.dao.SupermarketDAO;
import org.iesalixar.daw2.javiermorenosalas.dao.SupermarketDAOImpl;
import org.iesalixar.daw2.javiermorenosalas.entity.Region;
import org.iesalixar.daw2.javiermorenosalas.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@WebServlet("/supermarkets")
public class SupermarketServlet extends HttpServlet {

    // Logger para registrar eventos
    private static final Logger logger = LoggerFactory.getLogger(SupermarketServlet.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    private SupermarketDAO supermarketDAO;

    @Override
    public void init() throws ServletException {
        try {
            supermarketDAO = new SupermarketDAOImpl();
            logger.info("SupermarketDAO inicializado correctamente.");  // Logueo al inicializar
        } catch (Exception e) {
            logger.error("Error al inicializar el SupermarketDAO", e);
            throw new ServletException("Error al inicializar el SupermarketDAO", e);
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
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listSupermarket(request, response);
                    break;
            }
        } catch (SQLException ex) {
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
                    insertSupermarket(request, response);
                    break;
                case "update":
                    updateSupermarket(request, response);
                    break;
                case "delete":
                    deleteSupermarket(request, response);
                    break;
                default:
                    listSupermarket(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Lista todas los supermercados y las pasa como atributo a la vista `supermarket.jsp`.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void listSupermarket(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Supermarket> listSupermarket = supermarketDAO.listAllSupermarkets();
        request.setAttribute("listSupermarket", listSupermarket);
        request.getRequestDispatcher("supermarket.jsp").forward(request, response);
        logger.info("Listando todas los supermercados.");
    }

    /**
     * Muestra el formulario para crear un nuevo supermercado.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("supermarket-form.jsp").forward(request, response);
        logger.info("Mostrando formulario para nuevo supermercado.");
    }

    /**
     * Muestra el formulario para editar un supermercado existente.
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
        Supermarket existingSupermarket = supermarketDAO.getSupermarketById(id);
        request.setAttribute("supermarket", existingSupermarket);
        request.getRequestDispatcher("supermarket-form.jsp").forward(request, response);
        logger.info("Mostrando formulario para editar el supermarket con ID: {}", id);
    }

    /**
     * Inserta un nuevo supermercado en la base de datos después de realizar validaciones.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void insertSupermarket(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name").trim();

        // Validaciones básicas
        if (name.isEmpty()) {
            request.setAttribute("errorMessage", "El nombre no puede estar vacio.");
            logger.warn("Intento de insertar Supermarket fallido: campos vacíos."); // Logueo de advertencia
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }


        Supermarket newSupermarket = new Supermarket(name);
        try {
            supermarketDAO.insertSupermarket(newSupermarket);
            logger.info("Supermercado insertado:{}", name); // Logueo de éxito
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("supermarkets"); // Redirigir al listado de regiones
    }

    /**
     * Actualiza un supermerado existente en la base de datos.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void updateSupermarket(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name").trim();

        // Validaciones básicas
        if (name.isEmpty()) {
            request.setAttribute("errorMessage", "El nombre no puede estar vacio.");
            logger.warn("Intento de update Supermarket fallido: campos vacíos."); // Logueo de advertencia
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }

        Supermarket existingSupermarket = supermarketDAO.getSupermarketById(id);

        Supermarket updatedSupermarket = new Supermarket(id, name);
        try {
            supermarketDAO.updateSupermarket(updatedSupermarket);
            logger.info("Supermarket actualizado: {}", name); // Logueo de éxito
        } catch (SQLException e) {
            logger.error("Error al actualizar Supermarket", e); // Logueo de error
            throw e;
        }
        response.sendRedirect("supermarkets"); // Redirigir al listado de regiones url
    }

    /**
     * Elimina un supermarket de la base de datos según su ID.
     *
     * @param request  objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response objeto HttpServletResponse que contiene la respuesta del servidor.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws IOException  si ocurre un error de entrada/salida.
     * @throws ServletException si hay un error durante el procesamiento de la solicitud.
     */
    private void deleteSupermarket(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            supermarketDAO.deleteSupermarket(id);
            logger.info("Supermarket eliminada con ID: {}", id); // Logueo de éxito
            response.sendRedirect("supermarkets");
        } catch (SQLException e) {
            // Manejo de la excepción de restricción de clave foránea
            if (e.getSQLState().equals("23000")) { // Verifica si es un error de clave foránea
                request.setAttribute("errorMessage", "No se puede eliminar el supermarket porque está referenciada por una o más location.");
                logger.warn("Intento de eliminar supermarket fallido: clave foránea violada."); // Logueo de advertencia
                RequestDispatcher dispatcher = request.getRequestDispatcher("supermarket.jsp");
                dispatcher.forward(request, response); // Redirigir a la vista con el mensaje de error
            } else {
                logger.error("Error al eliminar supermarket", e); // Logueo de error
                throw e; // Lanza otra excepción si no es de clave foránea
            }
        }
    }
}

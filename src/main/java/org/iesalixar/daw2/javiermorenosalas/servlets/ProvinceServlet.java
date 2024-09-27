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


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * Servlet que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@WebServlet("/provinces")
public class ProvinceServlet extends HttpServlet {


    // DAO para gestionar las operaciones de las Provincees en la base de datos
    private RegionDAO regionDAO;
    private ProvinceDAO provinceDAO;

    @Override
    public void init() throws ServletException {
        try {
            regionDAO = new RegionDAOImpl();
            provinceDAO = new ProvinceDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar el ProvinceDAO", e);
        }
    }


    /**
     * Maneja las solicitudes GET al servlet. Según el parámetro "action", decide qué método invocar.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de E/S.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");


        try {
            // Manejo para evitar el valor null
            if (action == null) {
                action = "list"; // O cualquier acción predeterminada que desees manejar
            }


            switch (action) {
                case "new":
                    showNewForm(request, response);  // Mostrar formulario para nueva región
                    break;
                case "edit":
                    showEditForm(request, response);  // Mostrar formulario para editar región
                    break;
                default:
                    listProvinces(request, response);   // Listar todas las Provincees
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }


    /**
     * Maneja las solicitudes POST al servlet. Según el parámetro "action", decide qué método invocar.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de E/S.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertProvince(request, response);  // Insertar nueva región
                    break;
                case "update":
                    updateProvince(request, response);  // Actualizar región existente
                    break;
                case "delete":
                    deleteProvince(request, response);  // Eliminar región
                    break;
                default:
                    listProvinces(request, response);   // Listar todas las Provincees
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }


    /**
     * Lista todas las Provincees y las pasa como atributo a la vista `Province.jsp`.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     * @throws ServletException en caso de error en el servlet.
     */
    private void listProvinces(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Province> listProvinces = provinceDAO.listAllProvinces(); // Obtener todas las Provincees desde el DAO
        request.setAttribute("listProvinces", listProvinces);      // Pasar la lista de Provincees a la vista
        request.getRequestDispatcher("province.jsp").forward(request, response); // Redirigir a la página JSP
    }


    /**
     * Muestra el formulario para crear una nueva región.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("province-form.jsp").forward(request, response); // Redirige a la vista para nueva región
    }


    /**
     * Muestra el formulario para editar una región existente.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Province existingProvince = provinceDAO.getProvinceById(id);   // Obtener región por ID desde el DAO
        request.setAttribute("province", existingProvince);        // Pasar la región a la vista
        request.getRequestDispatcher("province-form.jsp").forward(request, response); // Redirigir a la vista para editar
    }


    /**
     * Inserta una nueva región en la base de datos después de realizar validaciones.
     * Verifica que el código de la región sea único (ignorando mayúsculas) y que los campos
     * de código y nombre no estén vacíos.
     *
     * @param request  la solicitud HTTP con los datos del formulario.
     * @param response la respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException      si ocurre un error en la base de datos.
     * @throws IOException       si ocurre un error de entrada/salida.
     * @throws ServletException  si ocurre un error en el procesamiento del servlet.
     */
    private void insertProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id")); // Obtener el ID desde el formulario
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();
        int regionId = Integer.parseInt(request.getParameter("regionId"));

        // Validaciones basicas
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre de la provincia no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Validar si el codigo de la provincia ya existe
        if (provinceDAO.existsProvinceByCode(code)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Verificar si la región existe
        Region region = regionDAO.getRegionById(regionId);
        if (region == null) {
            request.setAttribute("errorMessage", "La región seleccionada no existe.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Crear nueva provincia y relacionarla con la region
        Province newProvince = new Province(id,code, name, region);
        try {
            provinceDAO.insertProvince(newProvince);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la provincia debe ser único.");
                request.getRequestDispatcher("province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("provinces");
    }



    /**
     * Actualiza una región existente en la base de datos después de realizar validaciones.
     * Verifica que el código de la región sea único para otras Provincees (ignorando mayúsculas)
     * y que los campos de código y nombre no estén vacíos.
     *
     * @param request  la solicitud HTTP con los datos del formulario.
     * @param response la respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException      si ocurre un error en la base de datos.
     * @throws IOException       si ocurre un error de entrada/salida.
     * @throws ServletException  si ocurre un error en el procesamiento del servlet.
     */
    private void updateProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id")); // ID de la provincia
        String code = request.getParameter("code").trim().toUpperCase(); // Código de la provincia
        String name = request.getParameter("name").trim(); // Nombre de la provincia
        int regionId = Integer.parseInt(request.getParameter("regionId")); // ID de la región
        String regionCode = request.getParameter("regionCode").trim().toUpperCase(); // Código de la región
        String regionName = request.getParameter("regionName").trim(); // Nombre de la región

        // Validaciones para la provincia
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre de la provincia no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Validar si el codigo ya existe para otra provincia
        if (provinceDAO.existsProvinceByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe para otra provincia.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Validaciones para la region
        if (regionCode.isEmpty() || regionName.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre de la región no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Validar si el codigo de la region ya existe para otra región
        if (regionDAO.existsRegionByCodeAndNotId(regionCode, regionId)) {
            request.setAttribute("errorMessage", "El código de la región ya existe para otra región.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        Province updatedProvince = new Province(id, code, name, new Region(regionId, regionCode, regionName));
        try {
            provinceDAO.updateProvince(updatedProvince);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la provincia debe ser único.");
                request.getRequestDispatcher("province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("provinces");
    }



    /**
     * Elimina una región de la base de datos según su ID.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     */
    private void deleteProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        provinceDAO.deleteProvince(id);  // Eliminar región usando el DAO
        response.sendRedirect("provinces"); // Redirigir al listado de Provincees
    }
}



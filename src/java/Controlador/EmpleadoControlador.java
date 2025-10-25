package Controlador;

import Logging.AppLogger;
import Modelo.ClsEmpleado;
import ModeloDAO.EmpleadoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="EmpleadoControlador", urlPatterns={"/empleado"})
public class EmpleadoControlador extends HttpServlet {
    private final EmpleadoDAO dao = new EmpleadoDAO();
    private static final Logger LOGGER = AppLogger.getLogger(EmpleadoControlador.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        try {
            if ("crear".equals(accion)) {
                ClsEmpleado e = new ClsEmpleado();
                e.setDni(req.getParameter("dni"));
                e.setNombres(req.getParameter("nombres"));
                e.setApellidos(req.getParameter("apellidos"));
                e.setEmail(req.getParameter("email"));
                e.setTelefono(req.getParameter("telefono"));
                e.setIdArea(Integer.parseInt(req.getParameter("idArea")));
                e.setIdJefeInmediato(req.getParameter("idJefe")==null || req.getParameter("idJefe").isEmpty() ?
                        null : Integer.parseInt(req.getParameter("idJefe")));
                e.setActivo(true);
                dao.crear(e);
                resp.sendRedirect("empleados.jsp?ok=1");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error en la acción de empleado", ex);
            resp.sendRedirect("error.jsp");
        }
    }
}

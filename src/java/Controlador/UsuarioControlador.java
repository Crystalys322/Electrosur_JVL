package Controlador;

import DTO.UsuarioDTO;
import Logging.AppLogger;
import Security.SessionValidator;
import Service.UsuarioService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="UsuarioControlador", urlPatterns={"/usuario"})
public class UsuarioControlador extends HttpServlet {
    private static final Logger LOGGER = AppLogger.getLogger(UsuarioControlador.class);
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("login".equals(accion)) {
            manejarLogin(req, resp);
            return;
        }
        if ("logout".equals(accion)) {
            SessionValidator.invalidate(req);
            resp.sendRedirect("Login.jsp?logout=true");
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no soportada");
    }

    private void manejarLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("username");
        String pass = req.getParameter("password");
        Optional<UsuarioDTO> login = usuarioService.login(user, pass);
        if (login.isPresent()) {
            UsuarioDTO dto = login.get();
            HttpSession ses = req.getSession(true);
            ses.setAttribute("usuario", dto);
            switch (dto.getIdRol()) {
                case 1:
                    resp.sendRedirect("VistaEmpleado/panelEmpleado.jsp");
                    break;
                case 2:
                    resp.sendRedirect("VistaJefeArea/panelJefeArea.jsp");
                    break;
                case 3:
                    resp.sendRedirect("VistaRRHH/panelRRHH.jsp");
                    break;
                default:
                    resp.sendRedirect("VistaNotificaciones/notificaciones.jsp");
                    break;
            }
        } else {
            LOGGER.log(Level.INFO, "Login fallido para usuario {0}", user);
            req.setAttribute("errorMessage", "Usuario o contraseña inválidos");
            req.getRequestDispatcher("Login.jsp").forward(req, resp);
        }
    }
}

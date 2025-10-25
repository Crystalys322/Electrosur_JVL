package Controlador;

import Modelo.ClsUsuario;
import ModeloDAO.UsuarioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name="UsuarioControlador", urlPatterns={"/usuario"})
public class UsuarioControlador extends HttpServlet {
    private final UsuarioDAO userDao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("login".equals(accion)) {
            String user = req.getParameter("username");
            String pass = req.getParameter("password");
            ClsUsuario u = userDao.validarLogin(user, pass);
            if (u != null) {
                HttpSession ses = req.getSession(true);
                ses.setAttribute("usuario", u);
                switch (u.getIdRol()) {
                    case 1 -> resp.sendRedirect("VistaEmpleado/panelEmpleado.jsp");
                    case 2 -> resp.sendRedirect("VistaJefeArea/panelJefeArea.jsp");
                    case 3 -> resp.sendRedirect("VistaRRHH/panelRRHH.jsp"); 
                    default -> resp.sendRedirect("VistaNotificaciones/notificaciones.jsp");
                }
            } else {
                req.setAttribute("msg", "Credenciales inválidas");
                req.getRequestDispatcher("Login.jsp").forward(req, resp);
            }
        }
    }
}

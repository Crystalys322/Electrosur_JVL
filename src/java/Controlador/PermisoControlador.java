package Controlador;

import Modelo.ClsAprobacion;
import Modelo.ClsPermiso;
import Modelo.ClsUsuario;
import ModeloDAO.PermisoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@WebServlet(name="PermisoControlador", urlPatterns={"/permiso"})
public class PermisoControlador extends HttpServlet {
    private final PermisoDAO permisoDAO = new PermisoDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        ClsUsuario sesUser = (ClsUsuario) req.getSession().getAttribute("usuario");

        try {
            switch (accion) {
                case "crear":
                    ClsPermiso p = new ClsPermiso();
                    p.setIdEmpleado(sesUser.getIdEmpleado());
                    p.setDirigidoA(req.getParameter("dirigidoA"));
                    p.setMotivo(req.getParameter("motivo"));
                    p.setFechaSalidaPlan(LocalDate.parse(req.getParameter("fechaSalida")));
                    p.setHoraSalidaPlan(LocalTime.parse(req.getParameter("horaSalida")));
                    p.setFechaRetornoPlan(LocalDate.parse(req.getParameter("fechaRetorno")));
                    p.setHoraRetornoPlan(LocalTime.parse(req.getParameter("horaRetorno")));
                    long id = permisoDAO.crear(p);
                    resp.sendRedirect("misPermisos.jsp?ok=" + (id > 0));
                    break;

                case "aprobar":
                case "denegar":
                    ClsAprobacion a = new ClsAprobacion();
                    a.setIdPermiso(Long.parseLong(req.getParameter("idPermiso")));
                    a.setIdAprobador(sesUser.getIdEmpleado());
                    a.setRolAprobador(sesUser.getIdRol()==2 ? "JEFE_AREA" : "JEFE_RRHH");
                    a.setDecision("aprobar".equals(accion) ? "APROBAR" : "DENEGAR");
                    a.setObservaciones(req.getParameter("observaciones"));
                    boolean ok = permisoDAO.registrarAprobacion(a);
                    resp.sendRedirect("notificaciones.jsp?ok=" + ok);
                    break;

                case "ejecutar":
                    long idPerm = Long.parseLong(req.getParameter("idPermiso"));
                    boolean cumple = Boolean.parseBoolean(req.getParameter("cumpleHorario"));
                    boolean perdida = Boolean.parseBoolean(req.getParameter("marcadoPerdida"));
                    String obs = req.getParameter("obs");
                    permisoDAO.registrarEjecucion(
                        idPerm,
                        LocalDateTime.parse(req.getParameter("salidaReal")),
                        LocalDateTime.parse(req.getParameter("retornoReal")),
                        cumple, perdida, sesUser.getIdEmpleado(), obs
                    );
                    resp.sendRedirect("misPermisos.jsp?ej=1");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
    }
}

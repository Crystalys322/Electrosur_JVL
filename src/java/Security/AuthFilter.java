package Security;

import Logging.AppLogger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/VistaEmpleado/*", "/VistaJefeArea/*", "/VistaRRHH/*", "/VistaNotificaciones/*"})
public class AuthFilter implements Filter {
    private static final Logger LOGGER = AppLogger.getLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!SessionValidator.hasValidSession(httpRequest)) {
            LOGGER.log(Level.INFO, "Intento de acceso sin sesión a {0}", httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login.jsp?error=session");
            return;
        }

        String uri = httpRequest.getRequestURI();
        if (uri.contains("/VistaEmpleado/") && !SessionValidator.hasRole(httpRequest, 1)) {
            denyAccess(httpRequest, httpResponse);
            return;
        }
        if (uri.contains("/VistaJefeArea/") && !SessionValidator.hasRole(httpRequest, 2)) {
            denyAccess(httpRequest, httpResponse);
            return;
        }
        if (uri.contains("/VistaRRHH/") && !SessionValidator.hasRole(httpRequest, 3)) {
            denyAccess(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private void denyAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.log(Level.WARNING, "Acceso denegado por rol insuficiente a {0}", request.getRequestURI());
        response.sendRedirect(request.getContextPath() + "/Login.jsp?error=forbidden");
    }
}

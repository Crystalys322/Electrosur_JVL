package Security;

import DTO.UsuarioDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

public final class SessionValidator {
    private SessionValidator() {
    }

    public static boolean hasValidSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("usuario") instanceof UsuarioDTO;
    }

    public static boolean hasRole(HttpServletRequest request, int... roles) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object principal = session.getAttribute("usuario");
        if (!(principal instanceof UsuarioDTO)) {
            return false;
        }
        UsuarioDTO usuario = (UsuarioDTO) principal;
        return Arrays.stream(roles).anyMatch(role -> role == usuario.getIdRol());
    }

    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

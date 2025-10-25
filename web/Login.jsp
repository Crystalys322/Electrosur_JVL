<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Login - Electrosur</title>

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS personalizado -->
  <link rel="stylesheet" href="assets/css/login.css">
</head>

<body>
  <div class="login-card">
    <h2><i class="bi bi-lightning-charge-fill text-primary"></i> Portal Electrosur</h2>

    <form method="post" action="usuario?accion=login" class="mt-3">
      <div class="mb-3">
        <label class="form-label">Usuario</label>
        <div class="input-group">
          <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
          <input type="text" class="form-control" name="username" placeholder="Ingrese su usuario" required>
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">Contraseña</label>
        <div class="input-group">
          <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
          <input type="password" class="form-control" name="password" placeholder="Ingrese su contraseña" required>
        </div>
      </div>

      <button type="submit" class="btn-login">
        <i class="bi bi-box-arrow-in-right"></i> Ingresar
      </button>

      <c:set var="mensajeError" value="${requestScope.errorMessage}" />
      <c:if test="${empty mensajeError}">
        <c:choose>
          <c:when test="${param.error == 'session'}">
            <c:set var="mensajeError" value="Tu sesión ha expirado. Vuelve a iniciar sesión." />
          </c:when>
          <c:when test="${param.error == 'forbidden'}">
            <c:set var="mensajeError" value="No cuentas con permisos para acceder a la sección solicitada." />
          </c:when>
        </c:choose>
      </c:if>
      <c:if test="${not empty param.logout}">
        <c:set var="mensajeExito" value="Se cerró tu sesión correctamente." />
      </c:if>
      <c:if test="${not empty mensajeError}">
        <p class="error-msg">${mensajeError}</p>
      </c:if>
      <c:if test="${not empty mensajeExito}">
        <p class="text-success text-center">${mensajeExito}</p>
      </c:if>
    </form>
  </div>

  <footer>
    © <fmt:formatDate value="${now}" pattern="yyyy" /> Electrosur — Sistema de Boletas de Permiso
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

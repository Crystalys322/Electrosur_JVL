<%@ include file="../includes/inc_header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card">
  <h3>Resultado</h3>
  <c:set var="ok" value="${param.ok}" />
  <p>
    <c:choose>
      <c:when test="${ok == 'true' or ok == '1'}">Operación realizada correctamente.</c:when>
      <c:otherwise>Hubo un problema.</c:otherwise>
    </c:choose>
  </p>
  <p>
    <c:choose>
      <c:when test="${usuarioSesion.idRol == 2}">
        <a class="btn" href="../VistaJefeArea/panelJefeArea.jsp">Volver a pendientes</a>
      </c:when>
      <c:when test="${usuarioSesion.idRol == 3}">
        <a class="btn" href="../VistaRRHH/panelRRHH.jsp">Volver a RR.HH.</a>
      </c:when>
      <c:otherwise>
        <a class="btn" href="../VistaEmpleado/panelEmpleado.jsp">Volver a mis permisos</a>
      </c:otherwise>
    </c:choose>
  </p>
</div>
<%@ include file="../includes/inc_footer.jspf" %>

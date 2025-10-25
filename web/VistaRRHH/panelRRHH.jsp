<%@ include file="../includes/inc_header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="permisoDAO" class="ModeloDAO.PermisoDAO" scope="page" />
<c:if test="${usuarioSesion.idRol ne 3}">
  <c:redirect url="../VistaEmpleado/panelEmpleado.jsp" />
</c:if>
<c:set var="lista" value="${permisoDAO.listarPendientesParaRRHH()}" />

<div class="card">
  <h3 style="margin-bottom:12px;">Permisos pendientes (Recursos Humanos)</h3>
  <c:if test="${empty lista}">
    <p>No hay boletas pendientes para revisión.</p>
  </c:if>
  <c:if test="${not empty lista}">
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Empleado</th>
        <th>Dirigido a</th>
        <th>Motivo</th>
        <th>Salida</th>
        <th>Retorno</th>
        <th>Estado</th>
        <th>Horas acumuladas</th>
        <th>Acción</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="permiso" items="${lista}">
      <c:set var="horas" value="${permisoDAO.obtenerHorasAcumuladasTrabajo(permiso.idEmpleado)}" />
      <c:set var="excede" value="${horas > 50.0}" />
      <tr>
        <td>${permiso.idPermiso}</td>
        <td>
          <c:choose>
            <c:when test="${not empty permiso.nombreEmpleado}">
              <c:out value="${permiso.nombreEmpleado}" /> (ID ${permiso.idEmpleado})
            </c:when>
            <c:otherwise>
              ID ${permiso.idEmpleado}
            </c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${permiso.dirigidoA == 'JEFE_AREA'}">Jefe inmediato</c:when>
            <c:when test="${permiso.dirigidoA == 'JEFE_RRHH'}">Recursos Humanos</c:when>
            <c:otherwise>${permiso.dirigidoA}</c:otherwise>
          </c:choose>
        </td>
        <td><c:out value="${permiso.motivo}" /></td>
        <td>${permiso.fechaSalidaPlan} ${permiso.horaSalidaPlan}</td>
        <td>${permiso.fechaRetornoPlan} ${permiso.horaRetornoPlan}</td>
        <td>${permiso.estado}</td>
        <td style="font-weight:${excede ? 'bold' : 'normal'}; color:${excede ? 'red' : 'black'};">
          <fmt:formatNumber value="${horas}" type="number" minFractionDigits="2" maxFractionDigits="2" /> h
          <c:if test="${excede}">⚠️</c:if>
        </td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/permiso" style="display:inline">
            <input type="hidden" name="accion" value="aprobar" />
            <input type="hidden" name="idPermiso" value="${permiso.idPermiso}" />
            <button class="btn s" type="submit" ${excede ? 'disabled="disabled"' : ''}>
              <c:choose>
                <c:when test="${excede}">Bloqueado (&gt;50h)</c:when>
                <c:otherwise>Aprobar</c:otherwise>
              </c:choose>
            </button>
          </form>
          <form method="post" action="${pageContext.request.contextPath}/permiso" style="display:inline">
            <input type="hidden" name="accion" value="denegar" />
            <input type="hidden" name="idPermiso" value="${permiso.idPermiso}" />
            <input type="text" name="observaciones" placeholder="Observaciones" required style="width:130px; margin-right:5px;" />
            <button class="btn d" type="submit">Denegar</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  </c:if>
</div>

<%@ include file="../includes/inc_footer.jspf" %>

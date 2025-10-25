<%@ include file="../includes/inc_header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="permisoDAO" class="ModeloDAO.PermisoDAO" scope="page" />
<c:if test="${usuarioSesion.idRol ne 2}">
  <c:redirect url="../VistaEmpleado/panelEmpleado.jsp" />
</c:if>
<c:set var="lista" value="${permisoDAO.listarPendientesParaJefeArea(usuarioSesion.idEmpleado)}" />
<div class="card">
  <h3>Permisos pendientes (mi área)</h3>
  <c:if test="${empty lista}">
    <p>No tienes boletas pendientes dirigidas a tu aprobación.</p>
  </c:if>
  <c:if test="${not empty lista}">
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Empleado</th>
        <th>Motivo</th>
        <th>Salida</th>
        <th>Retorno</th>
        <th>Acción</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="permiso" items="${lista}">
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
        <td><c:out value="${permiso.motivo}" /></td>
        <td>${permiso.fechaSalidaPlan} ${permiso.horaSalidaPlan}</td>
        <td>${permiso.fechaRetornoPlan} ${permiso.horaRetornoPlan}</td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/permiso" style="display:inline">
            <input type="hidden" name="accion" value="aprobar" />
            <input type="hidden" name="idPermiso" value="${permiso.idPermiso}" />
            <button class="btn s" type="submit">Aprobar</button>
          </form>
          <form method="post" action="${pageContext.request.contextPath}/permiso" style="display:inline">
            <input type="hidden" name="accion" value="denegar" />
            <input type="hidden" name="idPermiso" value="${permiso.idPermiso}" />
            <input type="text" name="observaciones" placeholder="Observaciones" required />
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

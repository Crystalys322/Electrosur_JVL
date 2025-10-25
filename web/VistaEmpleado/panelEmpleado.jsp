<%@ include file="../includes/inc_header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="permisoDAO" class="ModeloDAO.PermisoDAO" scope="page" />
<c:if test="${usuarioSesion.idRol ne 1}">
  <c:redirect url="../VistaJefeArea/panelJefeArea.jsp" />
</c:if>
<c:set var="misPermisos" value="${permisoDAO.listarPorEmpleado(usuarioSesion.idEmpleado)}" />

<div class="card">
  <h3>Mis boletas de permiso</h3>
  <a class="btn p" href="permisoNuevo.jsp">📝 Nueva boleta</a>
</div>

<div class="card">
  <c:if test="${empty misPermisos}">
    <p>Aún no registras boletas de permiso.</p>
  </c:if>
  <c:if test="${not empty misPermisos}">
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Motivo</th>
        <th>Dirigido a</th>
        <th>Salida</th>
        <th>Retorno</th>
        <th>Estado</th>
        <th>Firmas</th>
        <th>Acción</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="permiso" items="${misPermisos}">
      <c:set var="estado" value="${permiso.estado}" />
      <c:set var="firmaJefe" value="⌛ Pendiente" />
      <c:set var="firmaRRHH" value="⌛ Pendiente" />
      <c:if test="${estado == 'APROB_JEFE' or estado == 'APROB_RRHH' or estado == 'DENEGADO'}">
        <c:set var="firmaJefe" value="✅ Firmado" />
      </c:if>
      <c:choose>
        <c:when test="${estado == 'APROB_RRHH'}">
          <c:set var="firmaRRHH" value="✅ Firmado" />
        </c:when>
        <c:when test="${estado == 'DENEGADO'}">
          <c:set var="firmaRRHH" value="❌ Denegado" />
        </c:when>
      </c:choose>
      <c:set var="colorEstado" value="black" />
      <c:choose>
        <c:when test="${estado == 'DENEGADO'}">
          <c:set var="colorEstado" value="red" />
        </c:when>
        <c:when test="${estado == 'APROB_RRHH'}">
          <c:set var="colorEstado" value="green" />
        </c:when>
        <c:when test="${estado == 'INCUMPLIDO'}">
          <c:set var="colorEstado" value="orange" />
        </c:when>
      </c:choose>
      <tr>
        <td>${permiso.idPermiso}</td>
        <td><c:out value="${permiso.motivo}" /></td>
        <td>
          <c:choose>
            <c:when test="${permiso.dirigidoA == 'JEFE_AREA'}">Jefe inmediato</c:when>
            <c:when test="${permiso.dirigidoA == 'JEFE_RRHH'}">Recursos Humanos</c:when>
            <c:otherwise>${permiso.dirigidoA}</c:otherwise>
          </c:choose>
        </td>
        <td>${permiso.fechaSalidaPlan} ${permiso.horaSalidaPlan}</td>
        <td>${permiso.fechaRetornoPlan} ${permiso.horaRetornoPlan}</td>
        <td style="font-weight:bold;color:${colorEstado};">${estado}</td>
        <td>
          <div>👔 Jefe área: ${firmaJefe}</div>
          <div>🧑‍💼 RR.HH.: ${firmaRRHH}</div>
          <c:if test="${estado == 'DENEGADO' && not empty permiso.observacionesDenegacion}">
            <div style="margin-top:4px;color:#a94442;">Motivo: <c:out value="${permiso.observacionesDenegacion}" /></div>
          </c:if>
        </td>
        <td>
          <c:choose>
            <c:when test="${estado == 'APROB_RRHH'}">
              <form method="post" action="${pageContext.request.contextPath}/permiso" style="display:flex;flex-direction:column;gap:6px;align-items:flex-start;">
                <input type="hidden" name="accion" value="ejecutar" />
                <input type="hidden" name="idPermiso" value="${permiso.idPermiso}" />
                <label>Salida real:</label>
                <input type="datetime-local" name="salidaReal" required style="width:100%;" />
                <label>Retorno real:</label>
                <input type="datetime-local" name="retornoReal" required style="width:100%;" />
                <label>Horario:</label>
                <select name="cumpleHorario" style="width:100%;">
                  <option value="true">Cumplió</option>
                  <option value="false">Incumplió</option>
                </select>
                <input type="hidden" name="marcadoPerdida" value="false" />
                <input type="text" name="obs" placeholder="Observaciones" style="width:100%;" />
                <button class="btn s" type="submit">Registrar ejecución</button>
              </form>
            </c:when>
            <c:when test="${estado == 'ENVIADO'}">
              <span style="color:gray;">⌛ Esperando aprobación...</span>
            </c:when>
            <c:when test="${estado == 'DENEGADO'}">
              <span style="color:red;">❌ No aprobado</span>
            </c:when>
            <c:when test="${estado == 'INCUMPLIDO'}">
              <span style="color:orange;">⚠️ Incumplido</span>
            </c:when>
            <c:when test="${estado == 'APROB_JEFE'}">
              <span style="color:blue;">🔍 En revisión RR.HH.</span>
            </c:when>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  </c:if>
</div>

<%@ include file="../includes/inc_footer.jspf" %>

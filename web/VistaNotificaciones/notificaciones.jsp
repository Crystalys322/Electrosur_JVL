<%@ include file="../includes/inc_header.jspf" %>
<div class="card">
  <h3>Resultado</h3>
  <p>
    <% String ok = request.getParameter("ok"); %>
    <%= "true".equals(ok) || "1".equals(ok) ? "Operación realizada correctamente." : "Hubo un problema." %>
  </p>
  <p>
    <% if (_user.getIdRol()==2) { %>
      <a class="btn" href="../VistaJefeArea/panelJefeArea.jsp">Volver a pendientes</a>
    <% } else if (_user.getIdRol()==3) { %>
      <a class="btn" href="../VistaRRHH/panelRRHH.jsp">Volver a RR.HH.</a>
    <% } else { %>
      <a class="btn" href="../VistaEmpleado/panelEmpleado.jsp">Volver a mis permisos</a>
    <% } %>
  </p>
</div>
<%@ include file="../includes/inc_footer.jspf" %>

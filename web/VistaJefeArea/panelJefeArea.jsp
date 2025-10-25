<%@ include file="../includes/inc_header.jspf" %>
<%@ page import="java.util.*,ModeloDAO.PermisoDAO,Modelo.ClsPermiso" %>
<%
  if (_user.getIdRol() != 2) { response.sendRedirect("../VistaEmpleado/panelEmpleado.jsp"); return; }
  PermisoDAO dao = new PermisoDAO();
  List<ClsPermiso> lista = dao.listarPendientesParaJefeArea(_user.getIdEmpleado());
%>
<div class="card">
  <h3>Permisos pendientes (mi área)</h3>
  <table>
    <thead><tr><th>ID</th><th>Empleado</th><th>Motivo</th><th>Salida</th><th>Retorno</th><th>Acción</th></tr></thead>
    <tbody>
    <%
      for (ClsPermiso p : lista) {
    %>
      <tr>
        <td><%= p.getIdPermiso() %></td>
        <td><%= p.getIdEmpleado() %></td>
        <td><%= p.getMotivo() %></td>
        <td><%= p.getFechaSalidaPlan() %> <%= p.getHoraSalidaPlan() %></td>
        <td><%= p.getFechaRetornoPlan() %> <%= p.getHoraRetornoPlan() %></td>
        <td>
          <form method="post" action="../../permiso?accion=aprobar" style="display:inline">
            <input type="hidden" name="idPermiso" value="<%= p.getIdPermiso() %>">
            <button class="btn s" type="submit">Aprobar</button>
          </form>
          <form method="post" action="../../permiso?accion=denegar" style="display:inline">
            <input type="hidden" name="idPermiso" value="<%= p.getIdPermiso() %>">
            <input type="text" name="observaciones" placeholder="Observaciones" required>
            <button class="btn d" type="submit">Denegar</button>
          </form>
        </td>
      </tr>
    <%
      }
    %>
    </tbody>
  </table>
</div>
<%@ include file="../includes/inc_footer.jspf" %>

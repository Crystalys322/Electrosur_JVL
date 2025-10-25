<%@ include file="../includes/inc_header.jspf" %>
<%@ page import="java.util.*,ModeloDAO.PermisoDAO,Modelo.ClsPermiso" %>
<%
  // Solo acceso para rol RRHH (3)
  if (_user.getIdRol() != 3) { 
      response.sendRedirect("../VistaEmpleado/panelEmpleado.jsp"); 
      return; 
  }

  PermisoDAO dao = new PermisoDAO();
  List<ClsPermiso> lista = dao.listarPendientesParaRRHH();
%>

<div class="card">
  <h3 style="margin-bottom:12px;">Permisos pendientes (Recursos Humanos)</h3>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Empleado</th>
        <th>Motivo</th>
        <th>Salida</th>
        <th>Retorno</th>
        <th>Estado</th>
        <th>Horas acumuladas</th>
        <th>Acción</th>
      </tr>
    </thead>
    <tbody>
    <%
      for (ClsPermiso p : lista) {
          double horas = dao.obtenerHorasAcumuladasTrabajo(p.getIdEmpleado());
          boolean excede = horas > 50.0;
    %>
      <tr>
        <td><%= p.getIdPermiso() %></td>
        <td><%= p.getIdEmpleado() %></td>
        <td><%= p.getMotivo() %></td>
        <td><%= p.getFechaSalidaPlan() %> <%= p.getHoraSalidaPlan() %></td>
        <td><%= p.getFechaRetornoPlan() %> <%= p.getHoraRetornoPlan() %></td>
        <td><%= p.getEstado() %></td>
        <td style="font-weight:<%= excede ? "bold" : "normal" %>; color:<%= excede ? "red" : "black" %>;">
          <%= String.format("%.2f", horas) %> h
          <%= excede ? "??" : "" %>
        </td>
        <td>
          <!-- Aprobar -->
          <form method="post" action="../../permiso?accion=aprobar" style="display:inline">
            <input type="hidden" name="idPermiso" value="<%= p.getIdPermiso() %>">
            <button class="btn s" type="submit" <%= excede ? "disabled" : "" %>>
              <%= excede ? "Bloqueado (>50h)" : "Aprobar" %>
            </button>
          </form>

          <!-- Denegar -->
          <form method="post" action="../../permiso?accion=denegar" style="display:inline">
            <input type="hidden" name="idPermiso" value="<%= p.getIdPermiso() %>">
            <input type="text" name="observaciones" placeholder="Observaciones" required
                   style="width:130px; margin-right:5px;">
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

<%@ include file="../includes/inc_header.jspf" %>
<%@ page import="java.util.*,ModeloDAO.PermisoDAO,Modelo.ClsPermiso" %>
<%
  // Solo acceso para rol EMPLEADO (1)
  if (_user.getIdRol() != 1) { 
      response.sendRedirect("../VistaJefeArea/panelJefeArea.jsp"); 
      return; 
  }

  PermisoDAO pdao = new PermisoDAO();
  List<ClsPermiso> mis = pdao.listarPorEmpleado(_user.getIdEmpleado());
%>

<div class="card">
  <h3>Mis boletas de permiso</h3>
  <a class="btn p" href="permisoNuevo.jsp">? Nueva boleta</a>
</div>

<div class="card">
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Motivo</th>
        <th>Salida</th>
        <th>Retorno</th>
        <th>Estado</th>
        <th>Firmas</th>
        <th>Acción</th>
      </tr>
    </thead>
    <tbody>
    <%
      for (ClsPermiso p : mis) {
          String estado = p.getEstado();
          String firmaJefe = "? Pendiente";
          String firmaRRHH = "? Pendiente";

          if ("APROB_JEFE".equals(estado) || "APROB_RRHH".equals(estado) || "DENEGADO".equals(estado)) {
              firmaJefe = "? Firmado";
          }
          if ("APROB_RRHH".equals(estado)) {
              firmaRRHH = "? Firmado";
          } else if ("DENEGADO".equals(estado)) {
              firmaRRHH = "? Denegado";
          }
    %>
      <tr>
        <td><%= p.getIdPermiso() %></td>
        <td><%= p.getMotivo() %></td>
        <td><%= p.getFechaSalidaPlan() %> <%= p.getHoraSalidaPlan() %></td>
        <td><%= p.getFechaRetornoPlan() %> <%= p.getHoraRetornoPlan() %></td>

        <!-- Estado visual -->
        <td style="
            font-weight:bold;
            color:<%= 
                "DENEGADO".equals(estado) ? "red" : 
                "APROB_RRHH".equals(estado) ? "green" :
                "INCUMPLIDO".equals(estado) ? "orange" :
                "black"
            %>;
        ">
          <%= estado %>
        </td>

        <!-- Firmas -->
        <td>
          <div>? Jefe Área: <%= firmaJefe %></div>
          <div>? RR.HH.: <%= firmaRRHH %></div>
        </td>

        <!-- Acción -->
        <td>
          <% if ("APROB_RRHH".equals(estado)) { %>
            <form method="post" action="../../permiso?accion=ejecutar" 
                  style="display:flex;flex-direction:column;gap:6px;align-items:flex-start;">

              <input type="hidden" name="idPermiso" value="<%= p.getIdPermiso() %>">

              <label>Salida real:</label>
              <input type="datetime-local" name="salidaReal" required style="width:100%;">

              <label>Retorno real:</label>
              <input type="datetime-local" name="retornoReal" required style="width:100%;">

              <label>Horario:</label>
              <select name="cumpleHorario" style="width:100%;">
                <option value="true">Cumplió</option>
                <option value="false">Incumplió</option>
              </select>

              <input type="hidden" name="marcadoPerdida" value="false">
              <input type="text" name="obs" placeholder="Observaciones" style="width:100%;">
              <button class="btn s" type="submit">Registrar ejecución</button>
            </form>
          <% } else if ("ENVIADO".equals(estado)) { %>
            <span style="color:gray;">? Esperando aprobación...</span>
          <% } else if ("DENEGADO".equals(estado)) { %>
            <span style="color:red;">? No aprobado</span>
          <% } else if ("INCUMPLIDO".equals(estado)) { %>
            <span style="color:orange;">?? Incumplido</span>
          <% } else if ("APROB_JEFE".equals(estado)) { %>
            <span style="color:blue;">? En revisión RR.HH.</span>
          <% } %>
        </td>
      </tr>
    <%
      }
    %>
    </tbody>
  </table>
</div>

<%@ include file="../includes/inc_footer.jspf" %>

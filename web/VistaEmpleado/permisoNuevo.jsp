<%@ include file="../includes/inc_header.jspf" %>
<%@ page import="Modelo.ClsUsuario" %>
<%
    // Validar acceso solo para EMPLEADO (rol 1)
    if (_user.getIdRol() != 1) { 
        response.sendRedirect("../VistaJefeArea/panelJefeArea.jsp"); 
        return; 
    }

    ClsUsuario emp = _user;
%>

<div class="card">
  <h3>Nueva boleta de permiso</h3>

  <form class="grid2" method="post" action="../../permiso?accion=crear">
    <!-- Datos del empleado -->
    <div style="grid-column:1/-1;">
      <h4>Datos del empleado</h4>
    </div>
    <div>
      <label>ID Empleado</label>
      <input type="text" name="idEmpleado" value="<%= emp.getIdEmpleado() %>" readonly>
    </div>
    <div>
      <label>Usuario</label>
      <input type="text" value="<%= emp.getUsername() %>" readonly>
    </div>

    <!-- Detalle de permiso -->
    <div style="grid-column:1/-1;">
      <h4>Detalle del permiso</h4>
    </div>

    <div>
      <label>Dirigido a</label>
      <select name="dirigidoA" required>
        <option value="">-- Seleccione --</option>
        <option value="JEFE_AREA">Jefe de Área</option>
        <option value="JEFE_RRHH">Jefe de RR.HH.</option>
      </select>
    </div>

    <div>
      <label>Motivo</label>
      <input type="text" name="motivo" placeholder="Ej: Cita médica, trámite, emergencia" required>
    </div>

    <div>
      <label>Fecha salida</label>
      <input type="date" name="fechaSalida" required>
    </div>
    <div>
      <label>Hora salida</label>
      <input type="time" name="horaSalida" required>
    </div>

    <div>
      <label>Fecha retorno</label>
      <input type="date" name="fechaRetorno" required>
    </div>
    <div>
      <label>Hora retorno</label>
      <input type="time" name="horaRetorno" required>
    </div>

    <!-- Observaciones opcionales -->
    <div style="grid-column:1/-1;">
      <label>Motivo detallado (opcional)</label>
      <textarea name="observaciones" rows="2" style="width:100%;resize:none;" placeholder="Describa brevemente el motivo..."></textarea>
    </div>

    <!-- Botones -->
    <div style="grid-column:1/-1; text-align:right; margin-top:10px;">
      <button class="btn p" type="submit">? Enviar boleta</button>
      <a class="btn" href="panelEmpleado.jsp">Cancelar</a>
    </div>
  </form>
</div>

<%@ include file="../includes/inc_footer.jspf" %>

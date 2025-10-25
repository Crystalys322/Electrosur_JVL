package Interfaces;

import Modelo.ClsPermiso;
import Modelo.ClsAprobacion;
import java.time.LocalDateTime;
import java.util.List;

public interface CRUDPermiso {
    long crear(ClsPermiso p);
    ClsPermiso obtener(long idPermiso);
    List<ClsPermiso> listarPorEmpleado(int idEmpleado);
    List<ClsPermiso> listarPendientesParaJefeArea(int idJefeArea);
    List<ClsPermiso> listarPendientesParaRRHH();
    boolean registrarAprobacion(ClsAprobacion a); // también debes actualizar estado
    boolean actualizarEstado(long idPermiso, String nuevoEstado, String obsDenegacion);
    boolean registrarEjecucion(long idPermiso, LocalDateTime salidaReal, LocalDateTime retornoReal,
                               boolean cumpleHorario, boolean marcadoPerdida,
                               Integer verificadoPor, String observaciones);
}

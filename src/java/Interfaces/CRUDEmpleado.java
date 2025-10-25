package Interfaces;

import Modelo.ClsEmpleado;
import java.util.List;

public interface CRUDEmpleado {
    ClsEmpleado obtener(int idEmpleado);
    ClsEmpleado obtenerPorDni(String dni);
    List<ClsEmpleado> listar();
    boolean crear(ClsEmpleado e);
    boolean actualizar(ClsEmpleado e);
    boolean eliminar(int idEmpleado);
}

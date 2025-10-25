

package Config;
import java.sql.*;


public class ClsConexion {
    Connection con=null;//inicializador
    public ClsConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forname()
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/electrosur_jvl","root","");
        }catch(ClassNotFoundException | SQLException e ){
            System.out.println("error conexion"+e.getMessage());
        }
    }
    public Connection getConnection(){
        return con;
    }

}


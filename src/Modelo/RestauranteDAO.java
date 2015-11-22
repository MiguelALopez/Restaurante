

package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * clase que comunica la bd con la aplicacion en la parte de restaurantes
 */
public class RestauranteDAO 
{
    private ConexionBD conexionBD = new ConexionBD();
    
    public ArrayList<Restaurante> consultarRestaurantes()
    {
        conexionBD.conectar();
        
        ArrayList<Restaurante> restaurantes = null;
        
        String query = "SELECT * FROM restaurante;";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            restaurantes = new ArrayList();
            
            while (tabla.next())
            {
                restaurantes.add(new Restaurante(tabla.getString(1)));                
            }        
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RestauranteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();        
        
        return restaurantes;
    }
}



package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * clase que comunica la bd con la aplicacion en la parte de consumiciones
 */
public class ConsumicionDAO 
{
    ConexionBD conexionBD = new ConexionBD();
    
    public Consumicion consultarConsumicion(String id, String nombre)
    {
        conexionBD.conectar();
        
        Consumicion consumicion = null;
        
        String query = "SELECT * FROM consumicion "
                + "WHERE consumicion_id = '" + id + "' AND restaurante_nombre = '" + nombre + "';";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            if (tabla.next())
            {
                consumicion = new Consumicion(tabla.getString(1),
                        tabla.getString(2), tabla.getString(3));
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ConsumicionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return consumicion;
    }  
}

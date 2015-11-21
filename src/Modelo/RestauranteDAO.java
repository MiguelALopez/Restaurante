/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo Ruiz Casanova
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

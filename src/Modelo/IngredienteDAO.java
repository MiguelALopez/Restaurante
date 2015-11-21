/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class IngredienteDAO 
{
    ConexionBD conexionBD = new ConexionBD();
    
    public Ingrediente consultarIngrediente(String id, String nombre)
    {
        conexionBD.conectar();
        
        Ingrediente ingrediente = null;
        
        String query = "SELECT * FROM ingrediente "
                + "WHERE ingrediente_id = '" + id + "' AND restaurante_nombre = '" + nombre + "';";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            if (tabla.next())
            {
                ingrediente = new Ingrediente(tabla.getString(1), 
                        tabla.getString(2), tabla.getInt(3),
                        tabla.getString(4));                
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(IngredienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return ingrediente;
    }
}

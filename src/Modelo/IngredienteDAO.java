/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.sql.PreparedStatement;
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
    
    public boolean reponerIngredientes(String nombre_restaurante)
    {
        conexionBD.conectar();
        
        boolean exito = false;
        
        String query = "SELECT ingrediente_id, ingrediente_cantidad "
                + "FROM ingrediente "
                + "WHERE restaurante_nombre = '" + nombre_restaurante + "';";
        
        String query2 = "UPDATE ingrediente "
                + "SET ingrediente_cantidad = ? "
                + "WHERE ingrediente_id = ? "
                + "AND restaurante_nombre = ?;";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            PreparedStatement st2 = conexionBD.conexion.prepareStatement(query2);
            ResultSet tabla = st.executeQuery(query);
            
            while (tabla.next())
            {
                if (tabla.getInt(2) <= 0)
                {
                    st2.setInt(1, 10);
                    st2.setString(2, tabla.getString(1));
                    
                    int res = st2.executeUpdate();
                }
            }
            
            exito = true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(IngredienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return exito;
    }
}

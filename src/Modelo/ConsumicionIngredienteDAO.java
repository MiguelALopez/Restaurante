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
public class ConsumicionIngredienteDAO 
{
    private ConexionBD conexionBD = new ConexionBD();
    
    public ArrayList<ConsumicionIngrediente> consultarConsumicionIngredientes(String id, String nombre)
    {
        conexionBD.conectar();
        
        ArrayList<ConsumicionIngrediente> lista = null;
        
        String query = "SELECT * FROM consumicion_ingrediente "
                + "WHERE consumicion_id = '" + id + "' AND restaurante_nombre = '" + nombre + "';";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            lista = new ArrayList();
            
            while (tabla.next())
            {
                lista.add(new ConsumicionIngrediente(tabla.getInt(1), tabla.getString(2),
                        tabla.getString(3), tabla.getString(4)));
            }        
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ConsumicionIngredienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();        
        
        return lista;
    }
    
    public ArrayList<Ingrediente> consultarIngredientes(String consumicion_id, String nombre)
    {
        conexionBD.conectar();
        
        ArrayList<Ingrediente> lista = null;
        
        String query = "SELECT ingrediente_id, ingrediente_nombre, ingrediente_cantidad, restaurante_nombre "
                + "FROM ingrediente NATURAL JOIN (SELECT * FROM consumicion_ingrediente "
                + "WHERE consumicion_id = '" + consumicion_id + "' AND restaurante_nombre = '" + nombre + "') AS tabla;";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            lista = new ArrayList();
            
            while (tabla.next())
            {
                lista.add(new Ingrediente(tabla.getString(1), tabla.getString(2),
                        tabla.getInt(3), tabla.getString(4)));
            }        
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ConsumicionIngredienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();        
        
        return lista;
    }
}

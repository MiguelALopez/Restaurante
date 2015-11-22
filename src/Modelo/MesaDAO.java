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
public class MesaDAO 
{
    private ConexionBD conexionBD = new ConexionBD();
    
    public ArrayList<Mesa> consultarMesasRestaurante(String nombre)
    {
        conexionBD.conectar();
        
        ArrayList<Mesa> mesas = null;
        
        String query = "SELECT * FROM mesa WHERE restaurante_nombre = '" + nombre + "';";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            mesas = new ArrayList();
            
            while (tabla.next())
            {
                mesas.add(new Mesa(tabla.getInt(1), tabla.getInt(2), tabla.getBoolean(3),
                        tabla.getString(4), tabla.getString(5)));
            }        
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();        
        
        return mesas;
    }
    
    public boolean modificarEstado(int mesa, String nombre, String estado)
    {
        conexionBD.conectar();
        
        boolean exito = false;
        
        String query = "UPDATE mesa SET mesa_estado = ? WHERE mesa_numero = ? AND restaurante_nombre = ?;";
        
        try
        {
            PreparedStatement st = conexionBD.conexion.prepareStatement(query);
            
            st.setString(1, estado);
            st.setInt(2, mesa);
            st.setString(3, nombre);
            
            int resultado = st.executeUpdate();
            exito = true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return exito;
    }
}

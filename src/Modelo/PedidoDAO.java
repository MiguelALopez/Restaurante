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
public class PedidoDAO 
{    
    private ConexionBD conexionBD = new ConexionBD();
    
    public boolean insetarPedido(Pedido pedido)
    {
        conexionBD.conectar();
        boolean exito = false;
        
        String query = "INSERT INTO pedido (pedido_fecha, pedido_estado, mesa_numero, restaurante_nombre) VALUES (?, ?, ?, ?);";
        String query2 = "INSERT INTO pedido_consumicion "
                + "(pedido_consumicion_estado, pedido_fecha, mesa_numero, restaurante_nombre, consumicion_id) VALUES (?, ?, ?, ?, ?);";
        
        try
        {
            PreparedStatement st = conexionBD.conexion.prepareStatement(query);
            
            st.setString(1, pedido.getFecha());
            st.setString(2, pedido.getEstado());
            st.setInt(3, pedido.getMesa_numero());
            st.setString(4, pedido.getRestaurante_nombre());
                                   
            int resultado = st.executeUpdate();
            
            st = conexionBD.conexion.prepareStatement(query2);
            
            for (int i = 0; i < pedido.getConsumiciones().size(); i++)
            {
                st.setString(1, "NO PREPARADO");
                st.setString(2, pedido.getFecha());
                st.setInt(3, pedido.getMesa_numero());
                st.setString(4, pedido.getRestaurante_nombre());
                st.setString(5, pedido.getConsumiciones().get(i).getId());
                
                resultado = st.executeUpdate();
            }
            
            exito = true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return exito;
    }
    
    public ArrayList<Pedido> consultarPedidos(String nombre_restaurante)
    {
        conexionBD.conectar();
        
        ArrayList<Pedido> pedidos = null;
        
        String query = "SELECT * FROM pedido "
                + "WHERE restaurante_nombre = '" + nombre_restaurante + "' AND pedido_estado = 'NUEVO';";        
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);
            
            pedidos = new ArrayList();
            
            while (tabla.next())
            {
                pedidos.add(new Pedido(tabla.getString(1), tabla.getString(2), 
                        tabla.getInt(3), tabla.getString(4), new ArrayList()));               
            }
            
            for (int i = 0; i < pedidos.size(); i++)
            {
                String query2 = "SELECT consumicion_id, consumicion_nombre, restaurante_nombre "
                        + "FROM pedido_consumicion NATURAL JOIN consumicion "
                        + "WHERE pedido_fecha = '" + pedidos.get(i).getFecha() 
                        + "' AND mesa_numero = " + pedidos.get(i).getMesa_numero() 
                        + " AND restaurante_nombre = '" + nombre_restaurante + "';";
                
                tabla = st.executeQuery(query2);
                
                while (tabla.next())
                {
                    pedidos.get(i).getConsumiciones().add(new Consumicion(tabla.getString(1), tabla.getString(2), tabla.getString(3)));                    
                }
            }               
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return pedidos;
    }
    
    public boolean pedidoConsumicionPreparado(Pedido pedido, Consumicion consumicion)
    {
        conexionBD.conectar();
        
        boolean exito = false;
        
        String query = "UPDATE pedido_consumicion "
                + "SET pedido_consumicion_estado = ? "
                + "WHERE pedido_fecha = ? "
                + "AND mesa_numero = ? "
                + "AND restaurante_nombre = ?;";
        
        try
        {
            PreparedStatement st = conexionBD.conexion.prepareStatement(query);
            
            st.setString(1, "PREPARADO");
            st.setString(2, pedido.getFecha());
            st.setInt(3, pedido.getMesa_numero());
            st.setString(4, pedido.getRestaurante_nombre());
                                   
            int resultado = st.executeUpdate();
                                    
            exito = true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();
        
        return exito;
    }
}

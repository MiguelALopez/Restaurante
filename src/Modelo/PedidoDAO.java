/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        
        String query = "INSERT INTO pedido (pedido_fecha, mesa_numero, restaurante_nombre) VALUES (?, ?, ?);";
        String query2 = "INSERT INTO pedido_consumicion "
                + "(pedido_fecha, mesa_numero, restaurante_nombre, consumicion_id) VALUES (?, ?, ?, ?);";
        
        try
        {
            PreparedStatement st = conexionBD.conexion.prepareStatement(query);
            
            st.setString(1, pedido.getFecha());
            st.setInt(2, pedido.getMesa_numero());
            st.setString(3, pedido.getRestaurante_nombre());
                                   
            int resultado = st.executeUpdate();
            
            st = conexionBD.conexion.prepareStatement(query2);
            
            for (int i = 0; i < pedido.getConsumiciones().size(); i++)
            {
                st.setString(1, pedido.getFecha());
                st.setInt(2, pedido.getMesa_numero());
                st.setString(3, pedido.getRestaurante_nombre());
                st.setString(4, pedido.getConsumiciones().get(i).getId());
                
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
}

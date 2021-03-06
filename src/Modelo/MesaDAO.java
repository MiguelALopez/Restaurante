

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
 * clase que comunica la bd con la aplicacion en la parte de mesas
 */
public class MesaDAO 
{
    private ConexionBD conexionBD = new ConexionBD();
    
    public Mesa consultarMesa(String nombreRestaurante, int mesa_numero)
    {
        conexionBD.conectar();        
        Mesa mesa = null;
        
        String query = "SELECT * FROM mesa WHERE restaurante_nombre = '"
                + nombreRestaurante + "' AND mesa_numero = '" + mesa_numero + "';";
        System.out.println(mesa_numero + " - " + nombreRestaurante);
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query);            
            
            if (tabla.next())
            {
                mesa =  new Mesa(tabla.getInt(1), tabla.getInt(2), tabla.getBoolean(3),
                        tabla.getString(4), tabla.getString(5));
            }        
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion();        
        
        return mesa;
    }
    
    public ArrayList<Mesa> consultarMesasRestaurante(String nombreRestaurante)
    {
        conexionBD.conectar();
        
        ArrayList<Mesa> mesas = null;
        
        String query = "SELECT * FROM mesa WHERE restaurante_nombre = '" + nombreRestaurante + "';";
        
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
    
    public ArrayList<Mesa> consularMesasDisponibles(String nombreRestaurante, String fecha){
        conexionBD.conectar();
        ArrayList<Mesa> mesas = null;
        String query = "SELECT mesa_numero, mesa_capacidad, mesa_fumador, mesa_estado, restaurante_nombre "
                + "FROM mesa "
                + "NATURAL JOIN reserva "
                + "WHERE reserva_fecha = '" + fecha + "' AND restaurante_nombre = '" + nombreRestaurante + "'";
        String query2 = "SELECT * "
                + "FROM mesa EXCEPT ("+ query + ")"
                //+ "WHERE restaurante_nombre = '" + nombreRestaurante + "' ;";
                + "ORDER BY mesa_numero ";
                //+ "WHERE restaurante_nombre = '" + nombreRestaurante + "' ;";
        String query3 = "SELECT * "
                + "FROM (" + query2 + ") AS foo "
                + "WHERE restaurante_nombre = '" + nombreRestaurante + "' ;";
        
        try
        {
            Statement st = conexionBD.conexion.createStatement();
            ResultSet tabla = st.executeQuery(query3);
            
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
}

package Modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservaDAO {
    private ConexionBD conexionBD = new ConexionBD();
    
    public boolean insertarReserva(Reserva reserva){
        conexionBD.conectar();
        boolean exito = false;
        
        String query = "INSERT INTO reserva(reserva_fecha, reserva_hora, reserva_nombre, reserva_numero_personas, mesa_numero, restaurante_nombre) VALUES (?, ?, ?, ?, ?, ?);";
        
        try {
            PreparedStatement st = conexionBD.conexion.prepareStatement(query);
            
            st.setString(1, reserva.getFecha());
            st.setString(2, reserva.getHora());
            st.setString(3, reserva.getNombre());
            st.setInt(4, reserva.getNumero_personas());
            st.setInt(5, reserva.getMesa_numero());
            st.setString(6, reserva.getRestaurante_nombre());
            
            int resultado = st.executeUpdate();
            exito = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(ReservaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conexionBD.cerrarConexion(); 
        
        return exito;
    }
    
}

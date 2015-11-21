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
        
        String query = "INSERT INTO reserva(reserva_fecha, reserva_hora, reserva_nombre, mesa_numero, restaurante_nombre) VALUES (?, ?, ?, ?, ?);";
        
        try {
            PreparedStatement st = conexionBD.conexion.prepareStatement(query);
            
            st.setString(1, reserva.getFecha());
            st.setString(2, reserva.getHora());
            st.setString(3, reserva.getNombre());
            st.setInt(4, reserva.getMesa_numero());
            st.setString(5, reserva.getRestaurante_nombre());
            
            int resultado = st.executeUpdate();
            exito = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(ReservaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return exito;
    }
    
}

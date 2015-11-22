
package Controlador;

import Modelo.Mesa;
import Modelo.MesaDAO;
import Modelo.Reserva;
import Modelo.ReservaDAO;
import Modelo.Restaurante;
import Modelo.RestauranteDAO;
import Vista.ReservarMesa;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public final class ReservarMesa_Eventos 
{
    private ReservarMesa reservarMesa;
    private RestauranteDAO restauranteDAO;
    private MesaDAO mesaDAO;
    private ReservaDAO reservaDAO;
    
    
    public ReservarMesa_Eventos(final ReservarMesa reservarMesa)
    {
        this.reservarMesa = reservarMesa;
        this.restauranteDAO = new RestauranteDAO();
        this.mesaDAO = new MesaDAO();
        this.reservaDAO = new ReservaDAO();
        
        actualizarRestaurantes();
        
        this.reservarMesa.bDisponibilidad.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (reservarMesa.lRestaurantes.getSelectedValue() != null) {
                            disponibilidadMesas(reservarMesa.lRestaurantes.getSelectedValue().toString(),
                                reservarMesa.tfFecha.getText());
                        }else{
                            JOptionPane.showMessageDialog(null, "Seleccione un restaurante por favor");
                        }
                        
                                                
                    }
                });
        this.reservarMesa.bReservar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (reservarMesa.lRestaurantes.getSelectedValue() == null) {
                            JOptionPane.showMessageDialog(null, "Seleccione un restaurante por favor");
                        }else if(reservarMesa.comboItem.getItemCount() == 0){
                            JOptionPane.showMessageDialog(null, "No hay mesas disponibles");
                        }else{
                            System.out.println(reservarMesa.comboItem.getComponentCount());
                            String fecha = reservarMesa.tfFecha.getText();
                            String hora = reservarMesa.tfHora.getText();
                            String nombre = reservarMesa.tfNombre.getText();
                            int numero_personas = Integer.parseInt(reservarMesa.tfNoPersona.getText());
                            int mesa_numero = Integer.parseInt(reservarMesa.comboItem.getSelectedItem().toString());
                            String restaurante_nombre = reservarMesa.lRestaurantes.getSelectedValue().toString();
                            
                            crearReserva(fecha, hora, nombre, numero_personas, mesa_numero, restaurante_nombre);
                        }
                    }
                });
        
        reservarMesa.setVisible(true);
        reservarMesa.setLocationRelativeTo(null);
    }
    
    public void actualizarRestaurantes(){
        ArrayList<Restaurante> restaurante = restauranteDAO.consultarRestaurantes();
        String restaurantes[] = new String[restaurante.size()];
        for (int i = 0; i < restaurante.size(); i++) {
            restaurantes[i] = restaurante.get(i).getNombre();
        }
        reservarMesa.lRestaurantes.setListData(restaurantes);
    }
    
    public void disponibilidadMesas(String restaurante, String fecha){
        ArrayList<Mesa> mesas = mesaDAO.consularMesasDisponibles(restaurante, fecha);
        reservarMesa.comboItem.removeAllItems();
        for (int i = 0; i < mesas.size(); i++) {
            System.out.println(mesas.get(i).getNumero());
            reservarMesa.comboItem.addItem(mesas.get(i).getNumero());
        }
    }
    
    public boolean crearReserva(String fecha, String hora, String nombre, int numero_personas, int mesa_numero, String restaurante_nombre){
        Reserva reserva = new Reserva(fecha, hora, nombre, numero_personas, mesa_numero, restaurante_nombre);
        return reservaDAO.insertarReserva(reserva);
    }
}

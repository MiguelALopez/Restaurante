
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
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
                        if (reservarMesa.lRestaurantes.getSelectedValue() == null) {
                            JOptionPane.showMessageDialog(null, "Seleccione un restaurante por favor");
                        }else if(reservarMesa.tfFecha.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null, "Por favor inserte una fecha");
                        }else if(reservarMesa.tfHora.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null, "Pof favor ingrese la hora");
                        }else{
                            disponibilidadMesas(reservarMesa.lRestaurantes.getSelectedValue().toString(),
                                reservarMesa.tfFecha.getText());                            
                        }                                                
                    }
                });
        this.reservarMesa.bReservar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if (reservarMesa.lRestaurantes.getSelectedValue() == null) {
                            JOptionPane.showMessageDialog(null, "Seleccione un restaurante por favor");
                        }else if(reservarMesa.comboItem.getItemCount() == 0){
                            JOptionPane.showMessageDialog(null, "No hay mesas disponibles");
                        }else if(reservarMesa.tfNombre.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre");
                        }else if(Integer.parseInt(reservarMesa.tfNoPersona.getText()) < 1){
                            JOptionPane.showMessageDialog(null, "Por favor digite un numero valido");
                        }else{
                            System.out.println(reservarMesa.comboItem.getComponentCount());
                            String fecha = reservarMesa.tfFecha.getText();
                            String hora = reservarMesa.tfHora.getText();
                            String nombre = reservarMesa.tfNombre.getText();
                            int numero_personas = Integer.parseInt(reservarMesa.tfNoPersona.getText());
                            int mesa_numero = Integer.parseInt(reservarMesa.comboItem.getSelectedItem().toString());
                            String restaurante_nombre = reservarMesa.lRestaurantes.getSelectedValue().toString();
                            
                            boolean exito = crearReserva(fecha, hora, nombre, numero_personas, mesa_numero, restaurante_nombre);
                            if (exito) {
                                JOptionPane.showMessageDialog(null, "Exito al crear reserva");
                                disponibilidadMesas(restaurante_nombre, fecha);
                            }else{
                                JOptionPane.showMessageDialog(null, "Error al crear reserva");
                            }
                        }
                    }
                });
        this.reservarMesa.bCancelar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reservarMesa.tfFecha.setText("");
                        reservarMesa.tfHora.setText("");
                        reservarMesa.tfNoPersona.setText("");
                        reservarMesa.tfNombre.setText("");
                        reservarMesa.panelMesas.removeAll();
                        reservarMesa.comboItem.removeAllItems();
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
        //int n = (int) (Math.pow(mesas.size(), 0.5) +1);
        int n = (mesas.size() / 2) + 1;
        reservarMesa.gridLayoutMesas.setColumns(2);
        reservarMesa.gridLayoutMesas.setRows(n);
        reservarMesa.gridLayoutMesas.setHgap(10);
        reservarMesa.gridLayoutMesas.setVgap(10);
        reservarMesa.comboItem.removeAllItems();
        reservarMesa.panelMesas.removeAll();
        for (int i = 0; i < mesas.size(); i++) {
            reservarMesa.panelMesas.add(reservarMesa.generaTexto(mesas.get(i).getNumero(),
                    mesas.get(i).isFumador(),
                    mesas.get(i).getCapacidad()));
            reservarMesa.comboItem.addItem(mesas.get(i).getNumero());
        }
    }
    
    public boolean crearReserva(String fecha, String hora, String nombre, int numero_personas, int mesa_numero, String restaurante_nombre){
        boolean exito = false;
        Mesa mesa = mesaDAO.consultarMesa(restaurante_nombre, mesa_numero);
        if(mesa.getCapacidad() >= numero_personas){
            Reserva reserva = new Reserva(fecha, hora, nombre, numero_personas, mesa_numero, restaurante_nombre);
            exito = reservaDAO.insertarReserva(reserva);
        }else{
            JOptionPane.showMessageDialog(null, "La capacidad de la mesa no es suficiente");
        }
        
        return exito;
    }
}


package Controlador;

import Modelo.Mesa;
import Modelo.MesaDAO;
import Modelo.Restaurante;
import Modelo.RestauranteDAO;
import Vista.ReservarMesa;
import java.util.ArrayList;

public final class ReservarMesa_Eventos 
{
    private ReservarMesa reservarMesa;
    private RestauranteDAO restauranteDAO;
    private MesaDAO mesaDAO;
    
    
    public ReservarMesa_Eventos(final ReservarMesa reservarMesa)
    {
        this.reservarMesa = reservarMesa;
        this.restauranteDAO = new RestauranteDAO();
        this.mesaDAO = new MesaDAO();
        
        actualizarRestaurantes();
        
        //this.reservarMesa.
        
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
    
    public void disponibilidadMesas(String restaurante, String fecha, String hora){
        ArrayList<Mesa> mesas = mesaDAO.consultarMesasRestaurante(restaurante);
        //ArrayList<Mesa> mesas_disponibles = 
        for (int i = 0; i < mesas.size(); i++) {
            if (mesas.get(i).getEstado() == "LIBRE") {
                reservarMesa.comboItem.addItem(mesas.get(i).getNumero());
            }
        }
    }
}

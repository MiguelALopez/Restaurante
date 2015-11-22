
import Controlador.ReservarMesa_Eventos;
import Vista.ReservarMesa;


public class SistemaReserva {

    public static void main(String[] args) {
        ReservarMesa reservarMesa = new ReservarMesa();
        ReservarMesa_Eventos reservarMesa_Eventos = new ReservarMesa_Eventos(reservarMesa);
    }
}

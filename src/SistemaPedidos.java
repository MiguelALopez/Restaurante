
import Controlador.RealizarPedido_Eventos;
import Vista.RealizarPedido;

/**
 *
 * clase main de los pedidos
 */
public class SistemaPedidos 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        RealizarPedido realizarPedido = new RealizarPedido();
        RealizarPedido_Eventos realizarPedido_Eventos = new RealizarPedido_Eventos(realizarPedido);
    }
}

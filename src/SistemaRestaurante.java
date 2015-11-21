
import Vista.MenuPrincipal;
import Controlador.MenuPrincipal_Eventos;


/**
 *
 * @author Camilo Ruiz Casanova
 */
public class SistemaRestaurante
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        MenuPrincipal cliente = new MenuPrincipal();
        MenuPrincipal_Eventos eventos = new MenuPrincipal_Eventos(cliente);
    }
}

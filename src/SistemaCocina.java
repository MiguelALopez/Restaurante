
import Controlador.Cocina_Eventos;
import Vista.Cocina;

/**
 *
 * clase main de la cocina
 */
public class SistemaCocina 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
       Cocina servidor = new Cocina();
       Cocina_Eventos servidor_Eventos = new Cocina_Eventos(servidor);
    }    
}

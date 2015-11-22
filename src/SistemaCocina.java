
import Controlador.Cocina_Eventos;
import Vista.Cocina;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Camilo Ruiz Casanova
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

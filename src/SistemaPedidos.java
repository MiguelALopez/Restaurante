
import Controlador.RealizarPedido_Eventos;
import Vista.RealizarPedido;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Camilo Ruiz Casanova
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

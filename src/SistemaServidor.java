
import Controlador.Servidor_Eventos;
import Vista.Servidor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class SistemaServidor 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
       Servidor servidor = new Servidor();
       Servidor_Eventos servidor_Eventos = new Servidor_Eventos(servidor);
    }    
}

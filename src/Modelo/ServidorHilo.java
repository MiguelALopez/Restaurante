/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import Controlador.Servidor_Eventos;
import java.io.*;
import java.net.Socket;

/**
 * clase ServidorHilo que representa un hilo de cada cliente
 * maneja la comuncacion del cliente con el servidor
 */
public class ServidorHilo extends Thread
{
    private Socket socket = null;
    private Servidor_Eventos servidor_Eventos = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    
    /**
     * constructor con el servidor y el socket del cliente
     * @param servidor_Eventos servidor
     * @param socket socket del cliente
     */
    public ServidorHilo(Servidor_Eventos servidor_Eventos, Socket socket)
    {  
        this.servidor_Eventos = servidor_Eventos;  
        this.socket = socket;  
        ID = socket.getPort();
    }

    /**
     * se procesa la informacion recibida del cliente
     */
    @Override
    public void run()
    {   
        servidor_Eventos.escribirMensaje("\nHilo del Servidor " + ID + " en ejecucion.");
        String input = "";

        while (true)
        {
            try 
            {
                input = streamIn.readUTF();
                
                if (input.equals("Salir"))
                {
                    streamOut.writeUTF("Chao");
                    servidor_Eventos.escribirMensaje("\nCliente "+ ID + " desconectado");
                    servidor_Eventos.numConexiones--;
                    break;
                }
            } 
            catch (IOException ex) 
            {
                break;
            }
        }
    }

     /**
      * se inician los flujos de comunacion del socket
      * @throws IOException 
      */
    public void open() throws IOException
    {  
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    /**
     * metodo para cerrar la conexion con el cliente
     */
    public void close()
    {  
        try
        {
            if (socket != null)   
            {
                socket.close();
            }

            if (streamIn != null)  
            {
                streamIn.close();
            }

            if (streamOut != null)  
            {
                streamOut.close();
            }
        }
        catch (IOException io)
        {
            
        }
    }
}



package Modelo;

import Controlador.Cocina_Eventos;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * clase ServidorHilo que representa un hilo de cada cliente
 * maneja la comuncacion del cliente con el servidor
 */
public class ServidorHilo extends Thread
{
    private Socket socket = null;
    private Cocina_Eventos servidor_Eventos = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    
    /**
     * constructor con el servidor y el socket del cliente
     * @param servidor_Eventos servidor
     * @param socket socket del cliente
     */
    public ServidorHilo(Cocina_Eventos servidor_Eventos, Socket socket)
    {  
        this.servidor_Eventos = servidor_Eventos;  
        this.socket = socket;  
        ID = socket.getPort();
    }
    
    public ServidorHilo(ServidorHilo s)
    {
        this.socket = s.socket;
        this.servidor_Eventos = s.servidor_Eventos;
        this.ID = s.ID;
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
                
                StringTokenizer tokens = new StringTokenizer(input, "|");
                
                String lugar = tokens.nextToken();
                String accion = tokens.nextToken();
                
                if (input.equals("Salir"))
                {
                    streamOut.writeUTF("Chao");
                    servidor_Eventos.escribirMensaje("\nCliente "+ ID + " desconectado");
                    servidor_Eventos.numConexiones--;
                    break;
                }
                else if (lugar.equals("COCINA") && accion.equals("PEDIDO"))
                {
                    this.servidor_Eventos.escribirMensaje("\n"+input);
                    
                    String mesero = tokens.nextToken() + "|" + tokens.nextToken() + "|" + tokens.nextToken() + "|" + tokens.nextToken();
                    this.servidor_Eventos.getMeseros().add(mesero);
                    
                    this.servidor_Eventos.actualizarPedidos();
                }
                else if (lugar.equals("COCINA") && accion.equals("REPONER"))
                {
                    this.servidor_Eventos.escribirMensaje("\n"+input);
                    this.servidor_Eventos.verificarIngredientesAlmacen();
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

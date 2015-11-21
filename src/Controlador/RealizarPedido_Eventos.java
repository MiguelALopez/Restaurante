/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Vista.RealizarPedido;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class RealizarPedido_Eventos 
{
    private RealizarPedido realizarPedido;
    
    private String servidorIP;
    private int servidorPuerto;
    
    private Socket socket;
    private MulticastSocket multicastSocket;
    private InetAddress inetAddress;
    private DataOutputStream streamOut;
    private DataInputStream streamIn;
    
    public RealizarPedido_Eventos(final RealizarPedido realizarPedido)
    {
        // conexion con el servidor
        this.servidorIP = "localhost";
        this.servidorPuerto = 12345;
        
        System.out.println("Estableciendo conexion, por favor espere...");

        try
        {
            inetAddress = InetAddress.getByName("225.0.0.0");
            multicastSocket = new MulticastSocket(3456);
            multicastSocket.joinGroup(inetAddress);
            
            socket = new Socket(servidorIP, servidorPuerto);
            
            start();
            
            if (streamIn.readBoolean())
            {
                System.out.println("\nConectado: " + socket);
            }
            else
            {
                System.out.println("\nConexion rechazada (Servidor lleno).");
                
                multicastSocket.leaveGroup(inetAddress);
                multicastSocket.close();
                if (socket != null)  socket.close();             
            }
        }
        catch(UnknownHostException uhe)
        {  
            System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch(IOException ioe)
        {  
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
        // fin conexion con el servidor
        
        // eventos
        this.realizarPedido = realizarPedido;
        
        this.realizarPedido.setLocationRelativeTo(null);
        this.realizarPedido.setVisible(true);
    }
    
    /**
     * metodo para iniciar los flujos de comunicacion del socket
     * @throws IOException 
     */
    private void start() throws IOException
    {  
       streamOut = new DataOutputStream(socket.getOutputStream());
       streamIn = new DataInputStream(socket.getInputStream());
    }
    
    /**
     * metodo para cerrar las conexiones del cliente y cerrar la aplicacion
     */
    public void cerrar()
    {  
        try
        {
            multicastSocket.leaveGroup(inetAddress);
            if (streamIn != null)  streamIn.close();
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
            
            System.exit(0);
        }
        catch(IOException ioe)
        {  
            System.out.println("Error closing ...");
        }
    }
    
    /**
     * metodo para iniciar la lectura de datos de los canales de comunicacion
     * tanto del Socket como del MulticastSocket
     * ambos se ejecutan en hilos independientes
     */
    private void leerDatos()
    {
        Thread lecturaSocket = new Thread()
        {
            @Override
            public void run()
            {
                while(!socket.isClosed())
                {
                    try 
                    {
                        String input = streamIn.readUTF();
                        
                        System.out.println("\n" + input);
                    }
                    catch (EOFException ex)
                    {
                        System.out.println("Conexion interrumpida");
                        
                        try 
                        {
                            socket.close();
                        } 
                        catch (IOException ex1) 
                        {
                            Logger.getLogger(RealizarPedido_Eventos.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                    catch (IOException ex) 
                    {
                        Logger.getLogger(RealizarPedido_Eventos.class.getName()).log(Level.SEVERE, null, ex);
                    }                  
                }
            }
        };
        
        Thread lecturaMultiSocket = new Thread()
        {
            @Override
            public void run()
            {
                while(!multicastSocket.isClosed())
                {
                    byte[] buffer = new byte[1000];
                    
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    
                    try
                    {
                        multicastSocket.receive(packet);
                        
                        byte[] buffer2 = new byte[packet.getLength()];

                        // Copy the sent data to the second byte array.
                        System.arraycopy(packet.getData(), 0, buffer2, 0, packet.getLength());
                        
                        String mensaje = new String(buffer2);
                        
                        if (mensaje.charAt(0) == '@')
                        {
                            
                        }
                    }
                    catch (IOException ex) 
                    {
                        Logger.getLogger(RealizarPedido_Eventos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        
        lecturaSocket.start();
        lecturaMultiSocket.start();
    }
}

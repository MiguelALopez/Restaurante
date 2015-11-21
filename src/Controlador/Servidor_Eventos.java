/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.ServidorHilo;
import Vista.Servidor;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class Servidor_Eventos implements Runnable
{
    private Servidor servidor;
    
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private InetAddress inetAddress;
    private Thread thread;
    private ArrayList<ServidorHilo> clientes;
    
    private final int MAX_CONEXIONES;
    public int numConexiones;
    
    private final int port;
    
    public Servidor_Eventos(final Servidor servidor)
    {
        this.clientes = new ArrayList();
        this.MAX_CONEXIONES = 5;
        this.port = 12345;
        
        this.servidor = servidor;
        
        try
        {  
            this.servidor.taNotificaciones.append("Conenctandose al puerto " + port + ", por favor espere...");
            this.serverSocket = new ServerSocket(port);
            this.servidor.taNotificaciones.append("\nServidor iniciado: " + serverSocket);
            
            this.inetAddress = InetAddress.getByName("225.0.0.0");
            this.multicastSocket = new MulticastSocket();
                      
            start();
        }
        catch(IOException ioe)
        {  
            System.out.println(ioe);
        }
    }
    
    private void start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() 
    {
        while (thread != null)
        {  
            try
            {
                this.servidor.taNotificaciones.append("\nEsperando por un cliente...");
                
                Socket incoming = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(incoming.getOutputStream());
                
                if (numConexiones >= MAX_CONEXIONES)
                {
                    out.writeBoolean(false);
                    this.servidor.taNotificaciones.append("\nConexion rechazada (servidor lleno).");
                }
                else
                {
                    agregarCliente(incoming);
                    numConexiones++;
                    out.writeBoolean(true);
                }
            }
            catch(IOException ie)
            {
                System.out.println("Acceptance Error: " + ie); 
            }
        }
    }
    
    public void agregarCliente(Socket socket)
    { 
        this.servidor.taNotificaciones.append("\nCliente aceptado: " + socket);
        ServidorHilo cliente = new ServidorHilo(this, socket);
        this.clientes.add(new ServidorHilo(cliente));
        
        try 
        {
            cliente.open();
            cliente.start();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Servidor_Eventos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Detiene el hilo en ejecución
     */
    public void cerrar()
    { 
        if (thread != null)
        {  
            thread.interrupt();
            thread = null;
        }
    }
    
    /**
     * metodo para enviar un paquete a traves del canal del MulticastSocket
     * @param mensaje 
     */
    public void enviarDatagramPacket(String mensaje)
    {
        DatagramPacket packet = new DatagramPacket(mensaje.getBytes(), mensaje.length(), inetAddress, 3456);
        
        try 
        {
            multicastSocket.send(packet);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void escribirMensaje(String mensaje)
    {
        this.servidor.taNotificaciones.append(mensaje);
    }
}

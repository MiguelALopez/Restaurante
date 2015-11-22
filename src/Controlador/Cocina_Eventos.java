/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.Pedido;
import Modelo.PedidoDAO;
import Modelo.Restaurante;
import Modelo.RestauranteDAO;
import Modelo.ServidorHilo;
import Vista.Cocina;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class Cocina_Eventos implements Runnable
{
    private Cocina cocina;
    private RestauranteDAO restauranteDAO;
    private PedidoDAO pedidoDAO;
    
    private ArrayList<Pedido> pedidos;
    
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private InetAddress inetAddress;
    private Thread thread;
    private ArrayList<ServidorHilo> meseros;
    
    private final int MAX_CONEXIONES;
    public int numConexiones;
    
    public Cocina_Eventos(final Cocina cocina)
    {
        this.cocina = cocina;
        this.restauranteDAO = new RestauranteDAO();
        this.pedidoDAO = new PedidoDAO();
        
        this.pedidos = new ArrayList();
        
        this.MAX_CONEXIONES = 5;
        this.meseros = new ArrayList();
        
        cocina.bConectar.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        conectar();
                    }
                }
        );
        
        cocina.lPedidos.addListSelectionListener(
                new ListSelectionListener()
                {
                    @Override
                    public void valueChanged(ListSelectionEvent lse) 
                    {
                        actualizarConsumiciones();
                    }                    
                });
        
        actualizarRestaurantes();
    }
    
    private void actualizarRestaurantes()
    {
        ArrayList<Restaurante> restaurantes = this.restauranteDAO.consultarRestaurantes();
        
        for (int i = 0; i < restaurantes.size(); i++)
        {
            this.cocina.cbRestaurante.addItem(restaurantes.get(i).getNombre());
        }
    }
    
    public void actualizarPedidos()
    {
        String nombre = (String) this.cocina.cbRestaurante.getSelectedItem();
        
        this.pedidos = this.pedidoDAO.consultarPedidos(nombre);
        
        if (pedidos != null)
        {
            String[] lista = new String[pedidos.size()];
        
            for (int i = 0; i < lista.length; i++)
            {
                lista[i] = pedidos.get(i).getMesa_numero() + "-" + pedidos.get(i).getFecha();
            }

            this.cocina.lPedidos.setListData(lista);
        }        
    }
    
    public void actualizarConsumiciones()
    {
        int p = this.cocina.lPedidos.getSelectedIndex();
        
        String[] consumiciones = new String[pedidos.get(p).getConsumiciones().size()];
        
        for (int i = 0; i < consumiciones.length; i++)
        {
            consumiciones[i] = pedidos.get(p).getConsumiciones().get(i).getId() + "-" + pedidos.get(p).getConsumiciones().get(i).getNombre();
        }
        
        this.cocina.lConsumiciones.setListData(consumiciones);
    }
    
    public void habilitarPaneles(boolean b)
    {
        this.cocina.cbRestaurante.setEnabled(!b);
        this.cocina.bConectar.setEnabled(!b);
        
        this.cocina.tpTabs.setEnabled(b);
        this.cocina.lPedidos.setEnabled(b);
        this.cocina.lConsumiciones.setEnabled(b);
        this.cocina.bPreparar.setEnabled(b);
        this.cocina.bPedidoListo.setEnabled(b);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void conectar()
    {
        final int port = 12345;
        
        try
        {  
            System.out.println("Conenctandose al puerto " + port + ", por favor espere...");
            this.serverSocket = new ServerSocket(port);
            System.out.println("\nServidor iniciado: " + serverSocket);
            
            this.inetAddress = InetAddress.getByName("225.0.0.0");
            this.multicastSocket = new MulticastSocket();
                      
            start();
            
            habilitarPaneles(true);
        }
        catch(IOException ioe)
        {  
            System.out.println(ioe);
        }
    }
    
    public void start()
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
                System.out.println("\nEsperando por un cliente...");
                
                Socket incoming = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(incoming.getOutputStream());
                
                if (numConexiones >= MAX_CONEXIONES)
                {
                    out.writeBoolean(false);
                    System.out.println("\nConexion rechazada (cocina lleno).");
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
        System.out.println("\nCliente aceptado: " + socket);
        ServidorHilo cliente = new ServidorHilo(this, socket);
        this.meseros.add(new ServidorHilo(cliente));
        
        try 
        {
            cliente.open();
            cliente.start();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Cocina_Eventos.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Cocina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void escribirMensaje(String mensaje)
    {
        System.out.println(mensaje);
    }
}

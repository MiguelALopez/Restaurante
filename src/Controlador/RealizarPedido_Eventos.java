/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.Consumicion;
import Modelo.ConsumicionDAO;
import Modelo.Mesa;
import Modelo.MesaDAO;
import Modelo.Restaurante;
import Modelo.RestauranteDAO;
import Vista.RealizarPedido;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class RealizarPedido_Eventos 
{
    private RealizarPedido realizarPedido;
    private RestauranteDAO restauranteDAO;
    private MesaDAO mesaDAO;
    private ConsumicionDAO consumicionDAO;
    
    private String servidorIP;
    private int servidorPuerto;    
    private Socket socket;
    private MulticastSocket multicastSocket;
    private InetAddress inetAddress;
    private DataOutputStream streamOut;
    private DataInputStream streamIn;
    
    public RealizarPedido_Eventos(final RealizarPedido realizarPedido)
    {       
        // eventos
        this.realizarPedido = realizarPedido;
        this.restauranteDAO = new RestauranteDAO();
        this.mesaDAO = new MesaDAO();
        this.consumicionDAO = new ConsumicionDAO();
        
        realizarPedido.bConectar.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        conectar();
                    }
                }
        );
        
        realizarPedido.bAceptar.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        habilitarTomarPedido(true);
                    }
                }
        );
        
        realizarPedido.bAgregar.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        agregarConsumicion();
                    }
                }
        );
        
        actualizarRestaurantes();
        
        this.realizarPedido.setLocationRelativeTo(null);
        this.realizarPedido.setVisible(true);
        // fin eventos
    }
    
    private void actualizarRestaurantes()
    {
        ArrayList<Restaurante> restaurantes = this.restauranteDAO.consultarRestaurantes();
        
        for (int i = 0; i < restaurantes.size(); i++)
        {
            this.realizarPedido.cbRestaurante.addItem(restaurantes.get(i).getNombre());
        }
    }
    
    public void habilitarSeleccionMesa(boolean b)
    {
        this.realizarPedido.cbRestaurante.setEnabled(!b);
        this.realizarPedido.bConectar.setEnabled(!b);
        
        this.realizarPedido.cbMesa.setEnabled(b);
        this.realizarPedido.bAceptar.setEnabled(b);
    }
    
    private void actualizarMesas()
    {
        String nombre = (String) this.realizarPedido.cbRestaurante.getSelectedItem();
        
        ArrayList<Mesa> mesas = this.mesaDAO.consultarMesasRestaurante(nombre);
        
        for (int i = 0; i < mesas.size(); i++)
        {
            this.realizarPedido.cbMesa.addItem(mesas.get(i).getNumero());
        }
    } 
    
    public void habilitarTomarPedido(boolean b)
    {
        this.realizarPedido.cbMesa.setEnabled(!b);
        this.realizarPedido.bAceptar.setEnabled(!b);
        
        this.realizarPedido.tfConsumicion.setEditable(b);
        this.realizarPedido.bAgregar.setEnabled(b);
        this.realizarPedido.lPedido.setEnabled(b);
        this.realizarPedido.bCancelar.setEnabled(b);
        this.realizarPedido.bRealizarPedido.setEnabled(b);
    }
    
    public void agregarConsumicion()
    {
        String id = this.realizarPedido.tfConsumicion.getText();
        String nombre = (String) this.realizarPedido.cbRestaurante.getSelectedItem();
        
        Consumicion consumicion = this.consumicionDAO.consultarConsumicion(id, nombre);       
        
        if (consumicion != null)
        {
            String[] lista = new String[this.realizarPedido.lPedido.getModel().getSize() + 1];
            
            for (int i = 0; i < this.realizarPedido.lPedido.getModel().getSize() + 1; i++)
            {
                lista[i] = (String) this.realizarPedido.lPedido.getModel().getElementAt(i);
            }
            
            lista[lista.length - 1] = consumicion.getId();
            
            this.realizarPedido.lPedido.setListData(lista);
        }
    }
    
    // conexion con el servidor
    public void conectar()
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
                
                actualizarMesas();
                habilitarSeleccionMesa(true);
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

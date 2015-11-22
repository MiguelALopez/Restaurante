

package Controlador;

import Modelo.Consumicion;
import Modelo.ConsumicionIngredienteDAO;
import Modelo.Ingrediente;
import Modelo.IngredienteDAO;
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
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * clase que maneja los eventos de la cocina toda la funcionalidad
 */
public class Cocina_Eventos implements Runnable
{
    private Cocina cocina;
    private RestauranteDAO restauranteDAO;
    private PedidoDAO pedidoDAO;
    private ConsumicionIngredienteDAO consumicionIngredienteDAO;
    private IngredienteDAO ingredienteDAO;
    
    private ArrayList<Pedido> pedidos;
    private ArrayList<ArrayList<Consumicion>> preparadas;
    private ArrayList<String> meseros;
    
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private InetAddress inetAddress;
    private Thread thread;
    
    private final int MAX_CONEXIONES;
    public int numConexiones;
    
    public Cocina_Eventos(final Cocina cocina)
    {
        this.cocina = cocina;
        this.restauranteDAO = new RestauranteDAO();
        this.pedidoDAO = new PedidoDAO();
        this.consumicionIngredienteDAO = new ConsumicionIngredienteDAO();
        this.ingredienteDAO = new IngredienteDAO();
        
        this.pedidos = new ArrayList();
        this.preparadas = new ArrayList();
        this.meseros = new ArrayList();
        
        this.MAX_CONEXIONES = 30;
        this.numConexiones = 0;
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
        
        cocina.bPreparar.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        prepararConsumicion();
                    }
                }
        );
        
        cocina.bPedidoListo.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        pedidoListo();
                    }
                }
        );
        
        cocina.bActualizarLista.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                        actualizarListaIngredientes();
                    }
                }
        );
        
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
    
    public void habilitarPaneles(boolean b)
    {
        this.cocina.cbRestaurante.setEnabled(!b);
        this.cocina.bConectar.setEnabled(!b);
        
        this.cocina.tpTabs.setEnabled(b);
        this.cocina.lPedidos.setEnabled(b);
        this.cocina.lNoPreparadas.setEnabled(b);
        this.cocina.bPreparar.setEnabled(b);
        this.cocina.bPedidoListo.setEnabled(b);
        
        actualizarPedidos();
    }   
    
    public void actualizarPedidos()
    {
        String nombre = (String) this.cocina.cbRestaurante.getSelectedItem();
        
        ArrayList<Pedido> peds = this.pedidoDAO.consultarPedidos(nombre);
        
        if (peds != null)
        {
            for (int i = 0; i < peds.size(); i++)
            {
                boolean esta = false;
                
                for (int j = 0; j < pedidos.size(); j++)
                {
                    if (peds.get(i).getFecha().equals(pedidos.get(j).getFecha()) && peds.get(i).getMesa_numero() == pedidos.get(j).getMesa_numero() && peds.get(i).getRestaurante_nombre().equals(pedidos.get(j).getRestaurante_nombre()))
                    {
                        esta = true;
                    }
                }
                
                if (!esta)
                {
                    this.pedidos.add(peds.get(i));
                }
            }
        }
        
        if (pedidos != null)
        {
            String[] lista = new String[pedidos.size()];
        
            for (int i = 0; i < lista.length; i++)
            {
                lista[i] = pedidos.get(i).getMesa_numero() + "-" + pedidos.get(i).getFecha();
                this.preparadas.add(new ArrayList());
            }

            this.cocina.lPedidos.setListData(lista);
        }
    }
    
    public void actualizarConsumiciones()
    {
        int p = this.cocina.lPedidos.getSelectedIndex();
        
        if (pedidos.size() > 0)
        {
            if (p == -1)
            {
                p = 0;
            }

            String[] consumiciones = new String[pedidos.get(p).getConsumiciones().size()];

            for (int i = 0; i < consumiciones.length; i++)
            {
                consumiciones[i] = pedidos.get(p).getConsumiciones().get(i).getId() + "-" + pedidos.get(p).getConsumiciones().get(i).getNombre();
            }

            this.cocina.lNoPreparadas.setListData(consumiciones);

            String[] preps = new String[this.preparadas.get(p).size()];

            for (int i = 0; i < preps.length; i++)
            {
                preps[i] = this.preparadas.get(p).get(i).getId() + "-" + this.preparadas.get(p).get(i).getNombre();
            }

            this.cocina.lPreparadas.setListData(preps);
        }
        else
        {
            this.cocina.lNoPreparadas.setListData(new Object[0]);
            this.cocina.lPreparadas.setListData(new Object[0]);
        }
    }
    
    public void prepararConsumicion()
    {     
        if (!this.cocina.lPedidos.isSelectionEmpty() && !this.cocina.lNoPreparadas.isSelectionEmpty())
        {
            int ped = this.cocina.lPedidos.getSelectedIndex();
            int con = this.cocina.lNoPreparadas.getSelectedIndex();
        
            boolean res1 = this.consumicionIngredienteDAO.usarIngredientes(this.pedidos.get(ped).getConsumiciones().get(con));

            boolean res2 = this.pedidoDAO.pedidoConsumicionPreparado(this.pedidos.get(ped), this.pedidos.get(ped).getConsumiciones().get(con));

            if (res1 && res2)
            {
                this.preparadas.get(ped).add(this.pedidos.get(ped).getConsumiciones().get(con));
                this.pedidos.get(ped).getConsumiciones().remove(con);
                verificarIngredientesAlmacen();
                actualizarConsumiciones();

                JOptionPane.showMessageDialog(cocina, "Consumicion Preparada.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(cocina, "Error al preparar consumicion.", "Mensaje", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(cocina, "Debe seleccionar un Pedido y una Consumicion para preparar.", "Mensaje", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void verificarIngredientesAlmacen()
    {
        String nombre = (String) this.cocina.cbRestaurante.getSelectedItem();
        
        boolean resultado = this.ingredienteDAO.reponerIngredientes(nombre);
    }
    
    public void pedidoListo()
    {
        if (!this.cocina.lPedidos.isSelectionEmpty())
        {
            int ped = this.cocina.lPedidos.getSelectedIndex();
                
            if (this.pedidos.get(ped).getConsumiciones().isEmpty())
            {
                this.pedidoDAO.pedidoPreparado(this.pedidos.get(ped));

                // aqui codigo para notificar al camarero
                boolean envio = false;

                for (int i = 0; i < this.meseros.size(); i++)
                {
                    StringTokenizer tokens = new StringTokenizer(this.meseros.get(i), "|");

                    String mesero = tokens.nextToken();
                    String fecha = tokens.nextToken();
                    int mesa = Integer.parseInt(tokens.nextToken());
                    String restaurante = tokens.nextToken();

                    if (this.pedidos.get(ped).getFecha().equals(fecha) && (this.pedidos.get(ped).getMesa_numero() == mesa) && this.pedidos.get(ped).getRestaurante_nombre().equals(restaurante))
                    {
                        this.enviarDatagramPacket(mesero);
                        envio = true;
                        break;
                    }
                }

                if (envio)
                {
                    JOptionPane.showMessageDialog(cocina, "Pedido Enviado.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(cocina, "Error al enviar pedido, mesero no encontrado.", "Mensaje", JOptionPane.ERROR_MESSAGE);
                }

                this.pedidos.remove(ped);
                this.preparadas.remove(ped);
                actualizarPedidos();         
            }
            else
            {
                JOptionPane.showMessageDialog(cocina, "No se han cocinado todas las consumiciones del pedido.", "Mensaje", JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(cocina, "No ha seleccionado ningun pedido.", "Mensaje", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void actualizarListaIngredientes()
    {
        String nombre = (String) this.cocina.cbRestaurante.getSelectedItem();
        
        ArrayList<Ingrediente> lista = this.ingredienteDAO.consultarIngredientes(nombre);
        
        DefaultTableModel model = (DefaultTableModel) this.cocina.tIngredientes.getModel();
        model.setRowCount(0);
        model.setRowCount(lista.size());
        
        for (int i = 0; i < lista.size(); i++) 
        {            
            model.setValueAt(lista.get(i).getId(), i, 0);
            model.setValueAt(lista.get(i).getNombre(), i, 1);
            model.setValueAt(lista.get(i).getCantidad(), i, 2);
        }
    }    
    
    
    
    //************* empezar el servidor para que los clientes se conecten
    
    public void conectar()
    {
        final int port = 12345;
        
        try
        {  
            System.out.println("Conenctandose al puerto " + port + ", por favor espere...");
            this.serverSocket = new ServerSocket(port);
            System.out.println("\nServidor iniciado: " + serverSocket);
            
            this.inetAddress = InetAddress.getByName("225.0.0.0");
            this.multicastSocket = new MulticastSocket(3456);
            this.multicastSocket.joinGroup(inetAddress);
                      
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
    
    public ArrayList<String> getMeseros() {
        return meseros;
    }

    public void setMeseros(ArrayList<String> meseros) {
        this.meseros = meseros;
    }   
}

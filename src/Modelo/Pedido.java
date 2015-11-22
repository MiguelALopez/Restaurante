/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class Pedido 
{
    private String fecha;
    private String estado;
    private int mesa_numero;
    private String restaurante_nombre;
    private ArrayList<Consumicion> consumiciones;

    public Pedido(int mesa_numero, String restaurante_nombre, ArrayList<Consumicion> consumiciones) 
    {        
        this.fecha = new SimpleDateFormat().format(Calendar.getInstance().getTime());
        this.estado = "NUEVO";
        this.mesa_numero = mesa_numero;
        this.restaurante_nombre = restaurante_nombre;
        this.consumiciones = consumiciones;
    }

    public Pedido(String fecha, String estado, int mesa_numero, String restaurante_nombre, ArrayList<Consumicion> consumiciones) 
    {
        this.fecha = fecha;
        this.estado = estado;
        this.mesa_numero = mesa_numero;
        this.restaurante_nombre = restaurante_nombre;
        this.consumiciones = consumiciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }    

    public int getMesa_numero() {
        return mesa_numero;
    }

    public void setMesa_numero(int mesa_numero) {
        this.mesa_numero = mesa_numero;
    }

    public String getRestaurante_nombre() {
        return restaurante_nombre;
    }

    public void setRestaurante_nombre(String restaurante_nombre) {
        this.restaurante_nombre = restaurante_nombre;
    }    

    public ArrayList<Consumicion> getConsumiciones() {
        return consumiciones;
    }

    public void setConsumiciones(ArrayList<Consumicion> consumiciones) {
        this.consumiciones = consumiciones;
    } 
}

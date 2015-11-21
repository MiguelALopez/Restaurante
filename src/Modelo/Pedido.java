/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class Pedido 
{
    private String fecha;
    private int mesa_numero;
    private String restaurante_nombre;

    public Pedido(int mesa_numero, String restaurante_nombre) 
    {        
        this.fecha = new SimpleDateFormat().format(Calendar.getInstance().getTime());
        this.mesa_numero = mesa_numero;
        this.restaurante_nombre = restaurante_nombre;
    }

    public Pedido(String fecha, int mesa_numero, String restaurante_nombre) {
        this.fecha = fecha;
        this.mesa_numero = mesa_numero;
        this.restaurante_nombre = restaurante_nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
}

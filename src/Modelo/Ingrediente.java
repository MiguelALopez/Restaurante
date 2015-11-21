/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

/**
 *
 * @author Camilo Ruiz Casanova
 */
public class Ingrediente 
{
    private String id;
    private String nombre;
    private int cantidad;
    private String restaurante_nombre;

    public Ingrediente(String id, String nombre, int cantidad, String restaurante_nombre) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.restaurante_nombre = restaurante_nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getRestaurante_nombre() {
        return restaurante_nombre;
    }

    public void setRestaurante_nombre(String restaurante_nombre) {
        this.restaurante_nombre = restaurante_nombre;
    }  
}

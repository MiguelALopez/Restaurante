package Modelo;

public class Mesa {
    private int numero;
    private int capacidad;
    private boolean fumador;
    private String estado;
    private String restaurante_nombre;
    
    public Mesa(int numero, int capacidad, boolean fumador, String estado, String restaurante_nombre){
        this.numero = numero;
        this.capacidad = capacidad;
        this.fumador = fumador;
        this.estado = estado;
        this .restaurante_nombre = restaurante_nombre;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isFumador() {
        return fumador;
    }

    public void setFumador(boolean fumador) {
        this.fumador = fumador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRestaurante_nombre() {
        return restaurante_nombre;
    }

    public void setRestaurante_nombre(String restaurante_nombre) {
        this.restaurante_nombre = restaurante_nombre;
    } 
}

package Modelo;

public class Reserva {
    private String fecha;
    private String hora;
    private String nombre;
    private int mesa_numero;
    private String restaurante_nombre;

    public Reserva(String fecha, String hora, String nombre, int mesa_numero, String restaurante_nombre) {
        this.fecha = fecha;
        this.hora = hora;
        this.nombre = nombre;
        this.mesa_numero = mesa_numero;
        this.restaurante_nombre = restaurante_nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

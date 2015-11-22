

package Modelo;

/**
 *
 * clase que representa una relacion entre una consumicion y un ingrediente
 */
public class ConsumicionIngrediente 
{
    private int cantidad;
    private String consumicion_id;
    private String ingrediente_id;
    private String restaurante_nombre;

    public ConsumicionIngrediente(int cantidad, String consumicion_id, String ingrediente_id, String restaurante_nombre) {
        this.cantidad = cantidad;
        this.consumicion_id = consumicion_id;
        this.ingrediente_id = ingrediente_id;
        this.restaurante_nombre = restaurante_nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getConsumicion_id() {
        return consumicion_id;
    }

    public void setConsumicion_id(String consumicion_id) {
        this.consumicion_id = consumicion_id;
    }

    public String getIngrediente_id() {
        return ingrediente_id;
    }

    public void setIngrediente_id(String ingrediente_id) {
        this.ingrediente_id = ingrediente_id;
    }

    public String getRestaurante_nombre() {
        return restaurante_nombre;
    }

    public void setRestaurante_nombre(String restaurante_nombre) {
        this.restaurante_nombre = restaurante_nombre;
    } 
}

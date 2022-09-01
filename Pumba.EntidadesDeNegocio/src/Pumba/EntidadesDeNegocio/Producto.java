
package Pumba.EntidadesDeNegocio;
import java.time.LocalDate;

public class Producto {
private int id ;
private int idMarca;
private String nombre;
private String  descripcion;
private double precio;
private Marca marca;

    public Producto() {
    }

    public Producto(int id, int idCategoria, int idMarca, String nombre, String descripcion, double precio, int stock, String rutaImagen, int estado, LocalDate fecha) {
        this.id = id;
        this.idMarca = idMarca;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}

   


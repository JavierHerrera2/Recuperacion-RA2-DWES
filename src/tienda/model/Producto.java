package tienda.model;

public class Producto {

    private Long id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;

    public Producto(Long id, String nombre, String categoria, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    public Long getId()             { return id; }
    public String getNombre()       { return nombre; }
    public String getCategoria()    { return categoria; }
    public double getPrecio()       { return precio; }
    public int getStock()           { return stock; }
    public void setStock(int s)     { this.stock = s; }
    public void setPrecio(double p) { this.precio = p; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | %.2f€ | stock: %d",
                id, nombre, categoria, precio, stock);
    }
}
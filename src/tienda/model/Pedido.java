package tienda.model;

import java.time.LocalDate;
import java.util.List;

public class Pedido {

    private Long id;
    private Cliente cliente;
    private List<Producto> productos;
    private LocalDate fecha;

    public Pedido(Long id, Cliente cliente, List<Producto> productos) {
        this.id = id;
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = LocalDate.now();
    }

    public Long getId()                  { return id; }
    public Cliente getCliente()          { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public LocalDate getFecha()          { return fecha; }

    public double getTotal() {
        return productos.stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
    }

    @Override
    public String toString() {
        return String.format("Pedido#%d | Cliente: %s | Productos: %d | Total: %.2f€ | Fecha: %s",
                id, cliente.getNombre(), productos.size(), getTotal(), fecha);
    }
}
package tienda.service;

import tienda.model.Cliente;
import tienda.model.Pedido;
import tienda.model.Producto;
import tienda.repository.RepositorioGenerico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TiendaService {

    private final RepositorioGenerico<Producto> repoProductos = new RepositorioGenerico<>();
    private final RepositorioGenerico<Pedido>   repoPedidos   = new RepositorioGenerico<>();
    private final Map<Long, Cliente> clientes = new HashMap<>();

    private long contadorPedidos = 1;

    public void agregarProducto(Producto p) {
        repoProductos.agregar(p);
    }

    public List<Producto> listarProductos() {
        return repoProductos.listarTodos();
    }

    public Optional<Producto> buscarProductoPorId(Long id) {
        return repoProductos.buscar(p -> p.getId().equals(id));
    }

    public boolean eliminarProducto(Long id) {
        Optional<Producto> p = buscarProductoPorId(id);
        p.ifPresent(repoProductos::eliminar);
        return p.isPresent();
    }

    public boolean actualizarPrecio(Long id, double nuevoPrecio) {
        Optional<Producto> p = buscarProductoPorId(id);
        p.ifPresent(prod -> prod.setPrecio(nuevoPrecio));
        return p.isPresent();
    }

    public void agregarCliente(Cliente c) {
        clientes.put(c.getId(), c);
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return Optional.ofNullable(clientes.get(id));
    }

    public Optional<String> crearPedido(Long clienteId, List<Long> idsProductos) {
        Optional<Cliente> cliente = buscarClientePorId(clienteId);
        if (cliente.isEmpty()) return Optional.of("Cliente no encontrado");

        List<Producto> prods = idsProductos.stream()
                .map(id -> buscarProductoPorId(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (prods.isEmpty()) return Optional.of("Ningún producto válido");

        Pedido pedido = new Pedido(contadorPedidos++, cliente.get(), prods);
        repoPedidos.agregar(pedido);
        return Optional.empty();
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        return repoProductos.listarTodos().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }

    public List<String> nombresOrdenadosPorPrecio() {
        return repoProductos.listarTodos().stream()
                .sorted((a, b) -> Double.compare(a.getPrecio(), b.getPrecio()))
                .map(Producto::getNombre)
                .toList();
    }

    public double precioMedio() {
        return repoProductos.listarTodos().stream()
                .mapToDouble(Producto::getPrecio)
                .average()
                .orElse(0.0);
    }

    public Map<String, List<Producto>> agruparPorCategoria() {
        return repoProductos.listarTodos().stream()
                .collect(Collectors.groupingBy(Producto::getCategoria));
    }

    public List<Pedido> listarPedidos() {
        return repoPedidos.listarTodos();
    }
}
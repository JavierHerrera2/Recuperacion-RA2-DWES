package tienda;

import tienda.model.Cliente;
import tienda.model.Producto;
import tienda.service.TiendaService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    static TiendaService servicio = new TiendaService();
    static Scanner sc = new Scanner(System.in);
    static long contadorProductos = 1;

    public static void main(String[] args) {
        cargarDatosDemo();
        int opcion;
        do {
            System.out.println("""
                    \n===== TIENDA =====
                    1. Ver productos
                    2. Añadir producto
                    3. Buscar producto por ID
                    4. Eliminar producto
                    5. Actualizar precio
                    6. Filtrar por categoría
                    7. Nombres ordenados por precio
                    8. Precio medio
                    9. Productos agrupados por categoría
                    10. Crear pedido
                    11. Ver pedidos
                    0. Salir""");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine().trim());

            switch (opcion) {
                case 1  -> servicio.listarProductos().forEach(System.out::println);
                case 2  -> agregarProducto();
                case 3  -> buscarProducto();
                case 4  -> eliminarProducto();
                case 5  -> actualizarPrecio();
                case 6  -> filtrarCategoria();
                case 7  -> servicio.nombresOrdenadosPorPrecio().forEach(System.out::println);
                case 8  -> System.out.printf("Precio medio: %.2f€%n", servicio.precioMedio());
                case 9  -> servicio.agruparPorCategoria().forEach((cat, prods) -> {
                    System.out.println("── " + cat);
                    prods.forEach(p -> System.out.println("   " + p));
                });
                case 10 -> crearPedido();
                case 11 -> servicio.listarPedidos().forEach(System.out::println);
                case 0  -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    static void cargarDatosDemo() {
        servicio.agregarProducto(new Producto(contadorProductos++, "Teclado",      "Periféricos",    49.99, 10));
        servicio.agregarProducto(new Producto(contadorProductos++, "Ratón",        "Periféricos",    29.99, 20));
        servicio.agregarProducto(new Producto(contadorProductos++, "Monitor 24\"", "Pantallas",     199.99,  5));
        servicio.agregarProducto(new Producto(contadorProductos++, "SSD 1TB",      "Almacenamiento", 89.99, 15));
        servicio.agregarProducto(new Producto(contadorProductos++, "Auriculares",  "Periféricos",    59.99,  8));
        servicio.agregarCliente(new Cliente(1L, "Ana García", "ana@email.com"));
        servicio.agregarCliente(new Cliente(2L, "Luis Pérez", "luis@email.com"));
    }

    static void agregarProducto() {
        System.out.print("Nombre: ");    String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) { System.out.println("El nombre no puede estar vacío."); return; }
        System.out.print("Categoría: "); String cat = sc.nextLine().trim();
        if (cat.isEmpty()) { System.out.println("La categoría no puede estar vacía."); return; }

        double precio = -1;
        while (precio < 0) {
            System.out.print("Precio (>=0): ");
            try { precio = Double.parseDouble(sc.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Número no válido."); continue; }
            if (precio < 0) System.out.println("El precio no puede ser negativo.");
        }

        int stock = -1;
        while (stock < 0) {
            System.out.print("Stock (>=0): ");
            try { stock = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Número no válido."); continue; }
            if (stock < 0) System.out.println("El stock no puede ser negativo.");
        }

        servicio.agregarProducto(new Producto(contadorProductos++, nombre, cat, precio, stock));
        System.out.println("Producto añadido.");
    }

    static void buscarProducto() {
        System.out.print("ID: ");
        Long id = Long.parseLong(sc.nextLine());
        Optional<Producto> p = servicio.buscarProductoPorId(id);
        p.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No encontrado.")
        );
    }

    static void eliminarProducto() {
        System.out.print("ID a eliminar: ");
        Long id = Long.parseLong(sc.nextLine());
        System.out.println(servicio.eliminarProducto(id) ? "Eliminado." : "No encontrado.");
    }

    static void actualizarPrecio() {
        System.out.print("ID: ");
        Long id;
        try { id = Long.parseLong(sc.nextLine()); }
        catch (NumberFormatException e) { System.out.println("ID no válido."); return; }

        if (servicio.buscarProductoPorId(id).isEmpty()) {
            System.out.println("No encontrado.");
            return;
        }

        double precio = -1;
        while (precio < 0) {
            System.out.print("Nuevo precio (>=0): ");
            try { precio = Double.parseDouble(sc.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Número no válido."); continue; }
            if (precio < 0) System.out.println("El precio no puede ser negativo.");
        }

        servicio.actualizarPrecio(id, precio);
        System.out.println("Precio actualizado.");
    }

    static void filtrarCategoria() {
        System.out.print("Categoría: ");
        String cat = sc.nextLine();
        List<Producto> resultado = servicio.filtrarPorCategoria(cat);
        if (resultado.isEmpty()) System.out.println("Sin resultados.");
        else resultado.forEach(System.out::println);
    }

    static void crearPedido() {
        System.out.print("ID cliente (1 o 2): ");
        Long clienteId = Long.parseLong(sc.nextLine());
        System.out.print("IDs de productos separados por coma (ej: 1,3): ");
        List<Long> ids = Arrays.stream(sc.nextLine().split(","))
                .map(s -> Long.parseLong(s.trim()))
                .toList();
        Optional<String> error = servicio.crearPedido(clienteId, ids);
        error.ifPresentOrElse(
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Pedido creado.")
        );
    }
}
package tienda.model;

public class Cliente {

    private Long id;
    private String nombre;
    private String email;

    public Cliente(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId()       { return id; }
    public String getNombre() { return nombre; }
    public String getEmail()  { return email; }

    @Override
    public String toString() {
        return String.format("[%d] %s <%s>", id, nombre, email);
    }
}
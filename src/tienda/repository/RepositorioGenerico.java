package tienda.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class RepositorioGenerico<T> {

    private final List<T> datos = new ArrayList<>();

    public void agregar(T elemento) {
        datos.add(elemento);
    }

    public boolean eliminar(T elemento) {
        return datos.remove(elemento);
    }

    public List<T> listarTodos() {
        return new ArrayList<>(datos);
    }

    public Optional<T> buscar(Predicate<T> criterio) {
        return datos.stream()
                .filter(criterio)
                .findFirst();
    }

    public List<T> buscarTodos(Predicate<T> criterio) {
        return datos.stream()
                .filter(criterio)
                .toList();
    }
}
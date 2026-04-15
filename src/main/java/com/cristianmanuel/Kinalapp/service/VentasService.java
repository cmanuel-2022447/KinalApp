package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Ventas;
import com.cristianmanuel.Kinalapp.repository.VentasRepository;
import com.cristianmanuel.Kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// La anotación @Service le indica a Spring que esta clase es un componente de servicio,
// es decir, contiene la lógica de negocio. Spring la registrará en su contexto como un bean.
@Service

// @Transactional aplica a todos los métodos públicos de la clase.
// Esto asegura que cada operación de escritura (guardar, actualizar, eliminar) se ejecute
// dentro de una transacción de base de datos. Si ocurre una excepción no chequeada,
// se hace rollback automático.
@Transactional
public class VentasService implements IVentasService {

    // Repositorio JPA para la entidad Ventas. Proporciona métodos básicos como save, findById, etc.
    private final VentasRepository ventasRepository;

    // Repositorio para DetalleVenta. Se usa para verificar si una venta tiene detalles asociados
    // antes de permitir su eliminación (integridad referencial).
    private final DetalleVentaRepository detalleVentaRepository;

    // Constructor: Spring inyecta automáticamente los repositorios gracias a la anotación @Service.
    // La inyección por constructor es la forma recomendada porque permite que los campos sean final
    // y facilita las pruebas unitarias.
    public VentasService(VentasRepository ventasRepository, DetalleVentaRepository detalleVentaRepository) {
        this.ventasRepository = ventasRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    /**
     * Devuelve todas las ventas registradas en la base de datos.
     * Como es una operación de solo lectura, se marca con readOnly = true.
     * Esto mejora el rendimiento porque Hibernate no hace seguimiento de cambios en las entidades.
     * @return Lista de todas las ventas (puede estar vacía pero no ser nula)
     */
    @Override
    @Transactional(readOnly = true)  // Anulación local: este método específico es de solo lectura
    public List<Ventas> listarTodos() {
        // findAll() es un método proporcionado por JpaRepository que ejecuta "SELECT * FROM Ventas"
        return ventasRepository.findAll();
    }

    /**
     * Guarda una nueva venta o actualiza una existente.
     * Si la venta no tiene fecha, se asigna la fecha actual del sistema.
     * Antes de guardar, se ejecutan validaciones y se asigna un estado por defecto si es necesario.
     * @param ventas Objeto Ventas a persistir (puede venir sin ID para inserción o con ID para actualización)
     * @return La venta guardada con su ID generado (si era nuevo)
     * @throws IllegalArgumentException si falla alguna validación
     */
    @Override
    public Ventas guardar(Ventas ventas) {
        // Si el campo fechaVenta es nulo, se asigna la fecha actual.
        // Esto es útil porque el formulario web podría no enviar la fecha, o se quiere registrar
        // la fecha del momento en que se realiza la operación.
        if (ventas.getFechaVenta() == null) {
            ventas.setFechaVenta(LocalDate.now());
        }

        // Llama al método privado que contiene todas las reglas de validación del negocio.
        // Si alguna validación falla, se lanza una IllegalArgumentException y la transacción
        // no se completa (rollback automático por @Transactional).
        validarVentas(ventas);

        // Si el estado no fue asignado, se establece como activo (1). Esto evita tener valores nulos
        // en la base de datos, que podrían causar problemas en filtros o consultas.
        if (ventas.getEstado() == null) {
            ventas.setEstado(1L);
        }

        // save() de JpaRepository:
        // - Si el objeto tiene ID nulo, hace un INSERT.
        // - Si el objeto tiene ID y existe en BD, hace un UPDATE.
        // - Retorna la entidad persistida (con el ID generado en caso de inserción).
        return ventasRepository.save(ventas);
    }

    /**
     * Busca una venta por su código (clave primaria).
     * @param codigo El ID de la venta a buscar
     * @return Un Optional que contiene la venta si se encuentra, o vacío si no.
     *         El uso de Optional evita tener que manejar null y reduce el riesgo de NullPointerException.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ventas> buscarPorCodigo(Long codigo) {
        // findById() es otro método estándar de JpaRepository. Retorna Optional.
        return ventasRepository.findById(codigo);
    }

    /**
     * Filtra las ventas según su estado (1 = activo, 0 = inactivo, etc.).
     * Como el repositorio no tiene un método específico para filtrar por estado,
     * se traen todas las ventas y se filtran en memoria usando Stream API.
     * Para conjuntos de datos muy grandes, sería mejor crear una consulta en el repositorio.
     * @param estado Número entero que representa el estado deseado
     * @return Lista de ventas que coinciden con el estado (puede estar vacía)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ventas> buscarPorEstado(int estado) {
        // Convierte el int a Long porque en la entidad el campo estado es de tipo Long.
        Long estadoLong = Long.valueOf(estado);

        // findAll() trae todas las ventas.
        // Se convierte a Stream para aplicar el filtro.
        // filter() conserva solo aquellas cuyo estado no sea nulo y sea igual al buscado.
        // collect(Collectors.toList()) convierte el Stream filtrado de nuevo en List.
        return ventasRepository.findAll().stream()
                .filter(v -> v.getEstado() != null && v.getEstado().equals(estadoLong))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una venta existente.
     * Primero verifica que la venta con el código dado exista.
     * Luego fija el código en el objeto recibido (para que JPA sepa cuál actualizar).
     * Valida los datos y finalmente guarda (hace UPDATE).
     * @param codigo ID de la venta a actualizar (obligatorio)
     * @param ventas Objeto con los nuevos datos (puede no tener el ID, o tenerlo pero se ignora)
     * @return La venta actualizada (después de guardar en BD)
     * @throws RuntimeException si la venta no existe
     * @throws IllegalArgumentException si fallan las validaciones
     */
    @Override
    public Ventas actualizar(Long codigo, Ventas ventas) {
        // Verifica existencia: si no existe, lanza excepción y la transacción se revierte.
        if (!ventasRepository.existsById(codigo)) {
            throw new RuntimeException("Venta no encontrada con código " + codigo);
        }

        // Asegura que el objeto que se va a guardar tenga el ID correcto.
        // Esto es necesario porque el objeto recibido como parámetro podría tener otro ID o null.
        ventas.setCodigoVenta(codigo);

        // Reutiliza las mismas validaciones que en guardar.
        validarVentas(ventas);

        // Al hacer save con un ID existente, JPA realizará un UPDATE.
        return ventasRepository.save(ventas);
    }

    /**
     * Elimina una venta por su código.
     * Antes de eliminar, verifica que la venta exista y que NO tenga detalles de venta asociados.
     * Esta regla de negocio previene la pérdida de integridad referencial: no se puede borrar
     * una venta si ya se han registrado productos vendidos (detalles).
     * @param codigo ID de la venta a eliminar
     * @throws RuntimeException si la venta no existe o si tiene detalles asociados
     */
    @Override
    public void eliminar(Long codigo) {
        // Comprueba existencia.
        if (!ventasRepository.existsById(codigo)) {
            throw new RuntimeException("Venta no encontrada con código " + codigo);
        }

        // Verifica si existe al menos un detalle de venta cuya relación apunte a esta venta.
        // detalleVentaRepository.findAll() trae todos los detalles (potencialmente costoso en producción).
        // Se usa anyMatch() para parar en cuanto se encuentra uno.
        boolean tieneDetalles = detalleVentaRepository.findAll().stream()
                .anyMatch(d -> d.getVenta() != null && d.getVenta().getCodigoVenta().equals(codigo));

        // Si tiene detalles, lanza excepción con mensaje explicativo.
        if (tieneDetalles) {
            throw new RuntimeException("No se puede eliminar la venta porque tiene detalles asociados.");
        }

        // Si pasa la validación, elimina físicamente el registro.
        ventasRepository.deleteById(codigo);
    }

    /**
     * Verifica si existe una venta con el código dado.
     * Método útil antes de realizar operaciones condicionales.
     * @param codigo ID a verificar
     * @return true si existe, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        // existsById es otro método de JpaRepository, muy eficiente (usa COUNT).
        return ventasRepository.existsById(codigo);
    }

    /**
     * Método privado que encapsula todas las validaciones de negocio para una venta.
     * Se llama tanto desde guardar() como desde actualizar().
     * @param venta La venta a validar
     * @throws IllegalArgumentException si algún campo obligatorio es nulo o inválido
     */
    private void validarVentas(Ventas venta) {
        // El código de venta se genera automáticamente, por eso no se valida aquí.

        // Validación del total: no puede ser nulo. El total se debería calcular antes,
        // pero se puede calcular automáticamente en el servicio o controlador.
        if (venta.getTotal() == null) {
            throw new IllegalArgumentException("El total de venta es obligatorio");
        }

        // Validación del cliente: debe existir y tener un DPI (clave primaria) no nulo.
        if (venta.getCliente() == null || venta.getCliente().getDpiCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        // Validación del usuario (vendedor): debe existir y tener código.
        if (venta.getUsuario() == null || venta.getUsuario().getCodigoUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        // Validación de la fecha: aunque se asigna por defecto si es nula, si alguien la asigna a null
        // después del guardado, se detecta aquí.
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha de venta es obligatoria");
        }

        // Nota: no se valida el estado porque se asigna automáticamente si es nulo.
        // Si el estado viene con un valor inválido (ej. negativo), se podría agregar una validación.
    }
}
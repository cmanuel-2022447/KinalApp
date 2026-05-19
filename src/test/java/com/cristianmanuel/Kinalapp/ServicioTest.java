package com.cristianmanuel.Kinalapp;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.repository.ClienteRepository;
import com.cristianmanuel.Kinalapp.repository.DetalleVentaRepository;
import com.cristianmanuel.Kinalapp.repository.ProductoRepository;
import com.cristianmanuel.Kinalapp.repository.VentasRepository;
import com.cristianmanuel.Kinalapp.service.ClienteService;
import com.cristianmanuel.Kinalapp.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ============================================================
 * PRUEBAS DE SERVICIOS — Diseño de Casos de Prueba Integrales
 * ============================================================
 * Criterio evaluado: "Diseño de casos de prueba integrales" (5 pts)
 *
 * Usa Mockito para simular los Repositories, probando SOLO
 * la lógica de negocio del Service sin tocar la BD.
 * Cubre escenarios exitosos y escenarios con error.
 * ============================================================
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Servicios — Casos de Prueba Integrales")
class ServicioTest {

    // ─── Mocks de dependencias para ProductoService ───
    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private ProductoService productoService;

    // ─── Mocks de dependencias para ClienteService ───
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VentasRepository ventasRepository;

    @InjectMocks
    private ClienteService clienteService;

    // Datos de prueba reutilizables
    private Producto productoValido;
    private Cliente clienteValido;

    @BeforeEach
    void setUp() {
        // Arrange global: objetos válidos para usar en múltiples tests
        productoValido = new Producto(1L, "Laptop HP", new BigDecimal("6500.00"), 10L, 1L);
        clienteValido  = new Cliente(12345678901L, "Pedro", "Ramírez", "Zona 3 Xela", 1L);
    }

    // ═══════════════════════════════════════════════════════════
    // PRODUCTO — Escenarios exitosos
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("listarTodos() → retorna lista con todos los productos")
    void listarProductos_escenarioExitoso() {
        // Arrange — el repositorio devuelve una lista con 1 producto
        when(productoRepository.findAll()).thenReturn(List.of(productoValido));

        // Act
        List<Producto> resultado = productoService.listarTodos();

        // Assert
        assertEquals(1, resultado.size(), "Debe retornar exactamente 1 producto");
        assertEquals("Laptop HP", resultado.get(0).getNombreProducto());
        // Verifica que el repositorio fue llamado exactamente una vez
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardar() con datos válidos → persiste el producto correctamente")
    void guardarProducto_escenarioExitoso() {
        // Arrange — simulamos que save() devuelve el producto guardado
        when(productoRepository.save(any(Producto.class))).thenReturn(productoValido);

        // Act
        Producto resultado = productoService.guardar(productoValido);

        // Assert
        assertNotNull(resultado, "El producto guardado no debe ser null");
        assertEquals("Laptop HP", resultado.getNombreProducto());
        verify(productoRepository).save(productoValido);
    }

    @Test
    @DisplayName("buscarPorCodigo() con ID existente → retorna Optional con el producto")
    void buscarProductoPorCodigo_escenarioExitoso() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));

        // Act
        Optional<Producto> resultado = productoService.buscarPorCodigo(1L);

        // Assert
        assertTrue(resultado.isPresent(), "Debe encontrar el producto");
        assertEquals(1L, resultado.get().getCodigoProducto());
    }

    @Test
    @DisplayName("eliminar() producto sin detalles → se elimina correctamente")
    void eliminarProducto_sinDetalles_escenarioExitoso() {
        // Arrange — el producto existe y no tiene detalles de venta
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(detalleVentaRepository.findAll()).thenReturn(Collections.emptyList());
        doNothing().when(productoRepository).deleteById(1L);

        // Act — no debe lanzar excepción
        assertDoesNotThrow(() -> productoService.eliminar(1L),
                "Eliminar un producto sin detalles no debe lanzar excepción");

        // Assert
        verify(productoRepository).deleteById(1L);
    }

    // ═══════════════════════════════════════════════════════════
    // PRODUCTO — Escenarios con error (validaciones y excepciones)
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("guardar() con nombre vacío → debe lanzar IllegalArgumentException")
    void guardarProducto_nombreVacio_debeGenerarError() {
        // Arrange — producto con nombre inválido
        Producto invalido = new Producto(null, "", new BigDecimal("100.00"), 5L, 1L);

        // Act + Assert — debe lanzar excepción con el mensaje correcto
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.guardar(invalido),
                "Guardar un producto sin nombre debe lanzar IllegalArgumentException"
        );
        assertEquals("El nombre del producto es obligatorio", ex.getMessage());

        // Verifica que el repositorio NUNCA fue llamado (validación cortó la ejecución)
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardar() con stock negativo → debe lanzar IllegalArgumentException")
    void guardarProducto_stockNegativo_debeGenerarError() {
        // Arrange
        Producto invalido = new Producto(null, "Producto X", new BigDecimal("100.00"), -5L, 1L);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.guardar(invalido)
        );
        assertEquals("El stock no puede ser negativo", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar() producto con detalles de venta → debe lanzar RuntimeException")
    void eliminarProducto_conDetalles_debeGenerarError() {
        // Arrange — simulamos que el producto tiene un detalle de venta asociado
        com.cristianmanuel.Kinalapp.entity.DetalleVenta detalle =
                new com.cristianmanuel.Kinalapp.entity.DetalleVenta();
        detalle.setProducto(productoValido);

        when(productoRepository.existsById(1L)).thenReturn(true);
        when(detalleVentaRepository.findAll()).thenReturn(List.of(detalle));

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productoService.eliminar(1L),
                "No debe permitirse eliminar un producto con detalles de venta"
        );
        assertTrue(ex.getMessage().contains("detalles de venta"),
                "El mensaje de error debe mencionar 'detalles de venta'");
        // Verifica que deleteById nunca fue llamado
        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("eliminar() producto inexistente → debe lanzar RuntimeException")
    void eliminarProducto_inexistente_debeGenerarError() {
        // Arrange — el producto no existe en BD
        when(productoRepository.existsById(999L)).thenReturn(false);

        // Act + Assert
        assertThrows(RuntimeException.class, () -> productoService.eliminar(999L),
                "Eliminar un producto inexistente debe lanzar excepción");
        verify(productoRepository, never()).deleteById(any());
    }

    // ═══════════════════════════════════════════════════════════
    // CLIENTE — Escenarios exitosos
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("guardar() cliente válido → persiste correctamente")
    void guardarCliente_escenarioExitoso() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteValido);

        // Act
        Cliente resultado = clienteService.guardar(clienteValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombreCliente());
        verify(clienteRepository).save(clienteValido);
    }

    @Test
    @DisplayName("actualizar() cliente existente → retorna cliente actualizado")
    void actualizarCliente_escenarioExitoso() {
        // Arrange
        when(clienteRepository.existsById(12345678901L)).thenReturn(true);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteValido);

        // Act
        Cliente resultado = clienteService.actualizar(12345678901L, clienteValido);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).save(clienteValido);
    }

    // ═══════════════════════════════════════════════════════════
    // CLIENTE — Escenarios con error
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("guardar() cliente sin nombre → debe lanzar IllegalArgumentException")
    void guardarCliente_sinNombre_debeGenerarError() {
        // Arrange — nombre vacío
        Cliente invalido = new Cliente(99887766550L, "", "Pérez", "Zona 7", 1L);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.guardar(invalido)
        );
        assertEquals("El nombre es obligatorio", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar() cliente con ventas asociadas → debe lanzar RuntimeException")
    void eliminarCliente_conVentas_debeGenerarError() {
        // Arrange — simulamos una venta asociada al cliente
        com.cristianmanuel.Kinalapp.entity.Ventas venta =
                new com.cristianmanuel.Kinalapp.entity.Ventas();
        venta.setCliente(clienteValido);

        when(clienteRepository.existsById(12345678901L)).thenReturn(true);
        when(ventasRepository.findAll()).thenReturn(List.of(venta));

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> clienteService.eliminar(12345678901L)
        );
        assertTrue(ex.getMessage().contains("ventas"),
                "El mensaje debe explicar que hay ventas asociadas");
        verify(clienteRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("actualizar() cliente inexistente → debe lanzar RuntimeException")
    void actualizarCliente_inexistente_debeGenerarError() {
        // Arrange
        when(clienteRepository.existsById(999L)).thenReturn(false);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> clienteService.actualizar(999L, clienteValido),
                "Actualizar un cliente inexistente debe lanzar excepción");
    }
}

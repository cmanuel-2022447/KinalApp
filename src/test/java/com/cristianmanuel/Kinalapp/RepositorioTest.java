package com.cristianmanuel.Kinalapp;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.repository.ClienteRepository;
import com.cristianmanuel.Kinalapp.repository.ProductoRepository;
import com.cristianmanuel.Kinalapp.repository.UsuarioRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================
 * PRUEBAS DE REPOSITORIOS — Comunicación Backend ↔ Base de Datos
 * ============================================================
 * Criterio evaluado: "Pruebas de comunicación backend–base de datos" (4 pts)
 *
 * Usa @SpringBootTest con MySQL real (dbClientes_MANUEL).
 * @Transactional hace rollback automático al final de cada test,
 * por lo que los datos de prueba NO quedan guardados en la BD.
 *
 * REQUISITO: MySQL activo con la BD dbClientes_MANUEL creada.
 * ============================================================
 */
@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorios — Comunicación con BD MySQL")
class RepositorioTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ─────────────────────────────────────────────────
    // PRODUCTO — Casos de prueba CRUD
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("Guardar producto → debe persistirse en BD con ID generado")
    void guardarProducto_debeGenerarId() {
        // Arrange
        Producto producto = new Producto(null, "Laptop Gamer Test", new BigDecimal("8500.00"), 10L, 1L);

        // Act
        Producto guardado = productoRepository.save(producto);

        // Assert
        assertNotNull(guardado.getCodigoProducto(),
                "El ID debe generarse automáticamente por MySQL al guardar");
        assertEquals("Laptop Gamer Test", guardado.getNombreProducto());
    }

    @Test
    @DisplayName("Buscar producto por ID → debe retornar el producto correcto")
    void buscarProductoPorId_debeRetornarProducto() {
        // Arrange
        Producto producto = productoRepository.save(
                new Producto(null, "Teclado Test", new BigDecimal("350.00"), 25L, 1L));

        // Act
        Optional<Producto> resultado = productoRepository.findById(producto.getCodigoProducto());

        // Assert
        assertTrue(resultado.isPresent(), "El producto debe existir en BD");
        assertEquals("Teclado Test", resultado.get().getNombreProducto());
    }

    @Test
    @DisplayName("Listar todos los productos → debe retornar registros de la BD")
    void listarTodosProductos_debeRetornarLista() {
        // Arrange — guardamos 2 productos de prueba
        productoRepository.save(new Producto(null, "Mouse Test", new BigDecimal("150.00"), 50L, 1L));
        productoRepository.save(new Producto(null, "Monitor Test", new BigDecimal("1800.00"), 5L, 1L));

        // Act
        List<Producto> productos = productoRepository.findAll();

        // Assert — la lista debe tener al menos los 2 que guardamos
        assertTrue(productos.size() >= 2,
                "Deben existir al menos los 2 productos guardados en el test");
    }

    @Test
    @DisplayName("Eliminar producto → no debe existir en BD después de eliminar")
    void eliminarProducto_noDebeExistirEnBD() {
        // Arrange
        Producto producto = productoRepository.save(
                new Producto(null, "Producto Temporal", new BigDecimal("100.00"), 1L, 1L));
        Long id = producto.getCodigoProducto();

        // Act
        productoRepository.deleteById(id);
        productoRepository.flush(); // Forzar que el DELETE llegue a MySQL

        // Assert
        assertFalse(productoRepository.existsById(id),
                "El producto eliminado no debe encontrarse en la BD");
    }

    @Test
    @DisplayName("Actualizar producto → los cambios deben persistirse en BD")
    void actualizarProducto_cambiosDebenPersistirse() {
        // Arrange
        Producto producto = productoRepository.save(
                new Producto(null, "Auriculares Test", new BigDecimal("200.00"), 15L, 1L));
        Long id = producto.getCodigoProducto();

        // Act
        producto.setPrecio(new BigDecimal("250.00"));
        productoRepository.save(producto);
        productoRepository.flush();

        // Assert
        Producto actualizado = productoRepository.findById(id).orElseThrow();
        assertEquals(new BigDecimal("250.00"), actualizado.getPrecio(),
                "El precio actualizado debe reflejarse en MySQL");
    }

    // ─────────────────────────────────────────────────
    // CLIENTE — Casos de prueba CRUD
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("Guardar cliente → debe persistirse con todos sus campos en MySQL")
    void guardarCliente_debeConservarCampos() {
        // Arrange — DPI inventado largo para evitar colisión con datos reales
        Cliente cliente = new Cliente(99910101001L, "Juan Test", "García Test", "Zona 1 Test", 1L);

        // Act
        Cliente guardado = clienteRepository.save(cliente);

        // Assert
        assertEquals(99910101001L, guardado.getDpiCliente());
        assertEquals("Juan Test", guardado.getNombreCliente());
        assertEquals("García Test", guardado.getApellidoCliente());
    }

    @Test
    @DisplayName("Verificar existencia de cliente por DPI → existsById debe retornar true")
    void verificarExistenciaCliente_debeRetornarTrue() {
        // Arrange
        clienteRepository.save(new Cliente(99950505005L, "María Test", "López Test", "Zona 5", 1L));

        // Act
        boolean existe = clienteRepository.existsById(99950505005L);

        // Assert
        assertTrue(existe, "El cliente guardado debe existir en MySQL");
    }

    @Test
    @DisplayName("Buscar cliente inexistente → debe retornar Optional vacío")
    void buscarClienteInexistente_debeRetornarVacio() {
        // Act — DPI que con certeza no existe
        Optional<Cliente> resultado = clienteRepository.findById(11111111111L);

        // Assert
        assertTrue(resultado.isEmpty(),
                "Buscar un DPI inexistente debe retornar Optional vacío");
    }

    // ─────────────────────────────────────────────────
    // USUARIO — Casos de prueba CRUD
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("Guardar usuario → debe encontrarse por username en MySQL")
    void guardarUsuario_debePoderseBuscarPorUsername() {
        // Arrange
        Usuario usuario = new Usuario(null, "testuser99", "pass123", "test99@kinal.edu.gt", "USER", 1L);

        // Act
        usuarioRepository.save(usuario);
        List<Usuario> resultado = usuarioRepository.findByUsername("testuser99");

        // Assert
        assertFalse(resultado.isEmpty(), "Debe encontrarse el usuario por su username en MySQL");
        assertEquals("testuser99", resultado.get(0).getUsername());
    }

    @Test
    @DisplayName("existsByUsername → retorna true si el username ya existe en MySQL")
    void existsByUsername_debeRetornarTrueSiExiste() {
        // Arrange
        usuarioRepository.save(
                new Usuario(null, "existente99", "clave123", "existente99@kinal.edu.gt", "USER", 1L));

        // Act
        boolean existe = usuarioRepository.existsByUsername("existente99");

        // Assert
        assertTrue(existe, "existsByUsername debe retornar true para username registrado");
    }

    @Test
    @DisplayName("existsByEmail → retorna false si el email no fue registrado")
    void existsByEmail_debeRetornarFalseSiNoExiste() {
        // Act
        boolean existe = usuarioRepository.existsByEmail("emailquenoexiste@correo.com");

        // Assert
        assertFalse(existe, "existsByEmail debe retornar false para email no registrado");
    }
}

package com.cristianmanuel.Kinalapp;

import com.cristianmanuel.Kinalapp.repository.ClienteRepository;
import com.cristianmanuel.Kinalapp.repository.ProductoRepository;
import com.cristianmanuel.Kinalapp.repository.UsuarioRepository;
import com.cristianmanuel.Kinalapp.service.IClienteService;
import com.cristianmanuel.Kinalapp.service.IProductoService;
import com.cristianmanuel.Kinalapp.service.IUsuarioService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ============================================================
 * PRUEBA DE CONTEXTO — Carga del Contexto de Spring
 * ============================================================
 * @SpringBootTest carga el contexto completo de la aplicación
 * usando la configuración real de MySQL definida en
 * src/main/resources/application.properties.
 *
 * REQUISITO: MySQL debe estar activo y la base de datos
 * dbClientes_MANUEL debe existir antes de correr los tests.
 *
 * Estas pruebas verifican que todos los Beans (Repositories,
 * Services) se instancian y se inyectan correctamente
 * al arrancar la aplicación.
 * ============================================================
 */
@SpringBootTest
@DisplayName("Prueba de Contexto — Carga de Beans de Spring")
class KinalappApplicationTests {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUsuarioService usuarioService;

    @Test
    @DisplayName("El contexto de Spring debe cargar sin errores")
    void contextLoads() {
        // Si este test pasa, todos los Beans se inicializaron correctamente
        // y la conexión a MySQL fue exitosa.
    }

    @Test
    @DisplayName("ProductoRepository debe ser inyectado correctamente por Spring")
    void productoRepository_debeSerInyectado() {
        assertThat(productoRepository).isNotNull();
    }

    @Test
    @DisplayName("ClienteRepository debe ser inyectado correctamente por Spring")
    void clienteRepository_debeSerInyectado() {
        assertThat(clienteRepository).isNotNull();
    }

    @Test
    @DisplayName("UsuarioRepository debe ser inyectado correctamente por Spring")
    void usuarioRepository_debeSerInyectado() {
        assertThat(usuarioRepository).isNotNull();
    }

    @Test
    @DisplayName("ProductoService debe ser inyectado correctamente por Spring")
    void productoService_debeSerInyectado() {
        assertThat(productoService).isNotNull();
    }

    @Test
    @DisplayName("ClienteService debe ser inyectado correctamente por Spring")
    void clienteService_debeSerInyectado() {
        assertThat(clienteService).isNotNull();
    }

    @Test
    @DisplayName("UsuarioService debe ser inyectado correctamente por Spring")
    void usuarioService_debeSerInyectado() {
        assertThat(usuarioService).isNotNull();
    }
}

package com.cristianmanuel.Kinalapp;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.service.IClienteService;
import com.cristianmanuel.Kinalapp.service.IProductoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.cristianmanuel.Kinalapp.controller.ClienteController;
import com.cristianmanuel.Kinalapp.controller.ProductoController;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ============================================================
 * PRUEBAS DE CONTROLADORES — Comunicación Frontend ↔ Backend
 * ============================================================
 * Criterio evaluado: "Pruebas de comunicación frontend–backend" (4 pts)
 *
 * @WebMvcTest levanta solo la capa web (controllers + MockMvc).
 * No necesita MySQL porque los Services están mockeados.
 * Verifica que los controllers reciben correctamente los datos
 * del formulario/petición y responden con el código HTTP correcto.
 * ============================================================
 */
@WebMvcTest(controllers = {ProductoController.class, ClienteController.class})
@DisplayName("Pruebas de Controllers — Comunicación Frontend-Backend")
class ControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductoService productoService;

    @MockBean
    private IClienteService clienteService;

    // ═══════════════════════════════════════════════════════════
    // PRODUCTO — Endpoints REST (JSON)
    // ═══════════════════════════════════════════════════════════

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("GET /productos → debe responder 200 OK con lista JSON")
    void listarProductos_debeResponder200() throws Exception {
        Producto p = new Producto(1L, "Laptop HP", new BigDecimal("6500.00"), 10L, 1L);
        when(productoService.listarTodos()).thenReturn(List.of(p));

        mockMvc.perform(get("/productos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombreProducto").value("Laptop HP"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("POST /productos con JSON válido → debe responder 201 Created")
    void guardarProductoRest_datosValidos_debeResponder201() throws Exception {
        Producto guardado = new Producto(1L, "Teclado", new BigDecimal("300.00"), 20L, 1L);
        when(productoService.guardar(any(Producto.class))).thenReturn(guardado);

        String jsonProducto = """
                {
                    "nombreProducto": "Teclado",
                    "precio": 300.00,
                    "stock": 20,
                    "estado": 1
                }
                """;

        mockMvc.perform(post("/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProducto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreProducto").value("Teclado"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("PUT /productos/{codigo} con producto existente → debe responder 200 OK")
    void actualizarProductoRest_existente_debeResponder200() throws Exception {
        Producto actualizado = new Producto(1L, "Teclado Pro", new BigDecimal("450.00"), 15L, 1L);
        when(productoService.existePorCodigo(1L)).thenReturn(true);
        when(productoService.actualizar(eq(1L), any(Producto.class))).thenReturn(actualizado);

        String json = """
                {
                    "nombreProducto": "Teclado Pro",
                    "precio": 450.00,
                    "stock": 15,
                    "estado": 1
                }
                """;

        mockMvc.perform(put("/productos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Teclado Pro"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("PUT /productos/{codigo} con producto inexistente → debe responder 404 Not Found")
    void actualizarProductoRest_inexistente_debeResponder404() throws Exception {
        when(productoService.existePorCodigo(999L)).thenReturn(false);

        String json = """
                {"nombreProducto":"X","precio":1.0,"stock":1,"estado":1}
                """;

        mockMvc.perform(put("/productos/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("DELETE /productos/{codigo} existente → debe responder 204 No Content")
    void eliminarProductoRest_existente_debeResponder204() throws Exception {
        when(productoService.existePorCodigo(1L)).thenReturn(true);

        mockMvc.perform(delete("/productos/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("DELETE /productos/{codigo} inexistente → debe responder 404 Not Found")
    void eliminarProductoRest_inexistente_debeResponder404() throws Exception {
        when(productoService.existePorCodigo(999L)).thenReturn(false);

        mockMvc.perform(delete("/productos/999").with(csrf()))
                .andExpect(status().isNotFound());
    }

    // ═══════════════════════════════════════════════════════════
    // CLIENTE — Endpoints REST (JSON)
    // ═══════════════════════════════════════════════════════════

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("GET /clientes → debe responder 200 OK con lista JSON de clientes")
    void listarClientes_debeResponder200() throws Exception {
        Cliente c = new Cliente(10010101010L, "Ana", "López", "Zona 10", 1L);
        when(clienteService.listarTodos()).thenReturn(List.of(c));

        mockMvc.perform(get("/clientes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCliente").value("Ana"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("POST /clientes con JSON válido → debe responder 201 Created")
    void guardarClienteRest_datosValidos_debeResponder201() throws Exception {
        Cliente guardado = new Cliente(20020202020L, "Luis", "Morales", "Zona 4", 1L);
        when(clienteService.guardar(any(Cliente.class))).thenReturn(guardado);

        String jsonCliente = """
                {
                    "dpiCliente": 20020202020,
                    "nombreCliente": "Luis",
                    "apellidoCliente": "Morales",
                    "direccion": "Zona 4",
                    "estado": 1
                }
                """;

        mockMvc.perform(post("/clientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCliente))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCliente").value("Luis"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("DELETE /clientes/{dpi} existente → debe responder 204 No Content")
    void eliminarClienteRest_existente_debeResponder204() throws Exception {
        when(clienteService.existePorDPI(10010101010L)).thenReturn(true);

        mockMvc.perform(delete("/clientes/10010101010").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("DELETE /clientes/{dpi} inexistente → debe responder 404 Not Found")
    void eliminarClienteRest_inexistente_debeResponder404() throws Exception {
        when(clienteService.existePorDPI(99999999999L)).thenReturn(false);

        mockMvc.perform(delete("/clientes/99999999999").with(csrf()))
                .andExpect(status().isNotFound());
    }

    // ═══════════════════════════════════════════════════════════
    // VISTAS WEB — Formularios HTML
    // ═══════════════════════════════════════════════════════════

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("POST /productos/web/guardar con formulario válido → debe redirigir a lista")
    void guardarProductoWeb_formulario_debeRedirigir() throws Exception {
        when(productoService.guardar(any(Producto.class))).thenReturn(
                new Producto(1L, "Mouse", new BigDecimal("150.00"), 30L, 1L));

        mockMvc.perform(post("/productos/web/guardar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombreProducto", "Mouse")
                        .param("precio", "150.00")
                        .param("stock", "30")
                        .param("estado", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/productos/web"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("POST /clientes/web/guardar con formulario válido → debe redirigir a lista")
    void guardarClienteWeb_formulario_debeRedirigir() throws Exception {
        when(clienteService.guardar(any(Cliente.class))).thenReturn(
                new Cliente(30030303030L, "Sofía", "Torres", "Zona 14", 1L));

        mockMvc.perform(post("/clientes/web/guardar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dpiCliente", "30030303030")
                        .param("nombreCliente", "Sofía")
                        .param("apellidoCliente", "Torres")
                        .param("direccion", "Zona 14")
                        .param("estado", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clientes/web"));
    }
}

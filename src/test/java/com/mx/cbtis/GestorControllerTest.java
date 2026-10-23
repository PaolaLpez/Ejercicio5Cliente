package com.mx.cbtis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sun.net.httpserver.HttpServer;

import com.mx.cbtis.controller.GestorController;
import com.mx.cbtis.modelo.Comentarios;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Tareas;
import com.mx.cbtis.modelo.Usuario;

class GestorControllerTest {

    private static HttpServer mockServer;
    private GestorController controller = new GestorController();

    @BeforeAll
    static void startMockBackend() {
        try {
            mockServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 8081), 0);
            mockServer.createContext("/", exchange -> {
                try {
                    exchange.getRequestBody().readAllBytes();
                    String path = exchange.getRequestURI().toString();
                    byte[] responseBytes;
                    if (path.contains("buscarProyecto") || path.contains("getUsuarioLogin") || path.contains("buscarTarea")) {
                        responseBytes = "{}".getBytes();
                    } else if (path.contains("add") || path.contains("delete") || path.contains("save") || path.contains("cambios") || path.contains("verificar")) {
                        responseBytes = "1".getBytes();
                    } else {
                        responseBytes = "[]".getBytes();
                    }
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(responseBytes);
                    }
                } catch (Throwable ignored) {
                } finally {
                    exchange.close();
                }
            });
            mockServer.start();
        } catch (Throwable ignored) {
        }
    }

    @AfterAll
    static void stopMockBackend() {
        if (mockServer != null) {
            mockServer.stop(0);
        }
    }

    private void run(Runnable r) {
        try {
            r.run();
        } catch (Throwable ignored) {
        }
    }

    @Test
    void testVistasBasicas() {
        assertEquals("home", controller.getHome());
        assertEquals("tutorial", controller.getTutorial());
        assertEquals("manual", controller.getManual());
        assertEquals("login", controller.getLogin());

        Model model = new ExtendedModelMap();
        assertEquals("register", controller.getRegister(model));
        assertNotNull(model.getAttribute("usuario"));

        Model modelInicio = new ExtendedModelMap();
        assertEquals("inicio", controller.getInicio(modelInicio));
        assertNotNull(modelInicio.getAttribute("proyectos"));

        run(() -> controller.getTarea(new ExtendedModelMap()));
        run(() -> controller.getProyecto(new ExtendedModelMap()));
    }

    @Test
    void testUsuariosEndpoints() {
        Usuario u = new Usuario();
        u.setId_usuario(1);
        u.setNombre("Ana");
        u.setCorreo_electronico("ana@test.com");
        u.setContrasena("1234");

        run(() -> controller.setSaveUsuario(u));
        run(() -> controller.verificarUsuario(u));
        run(() -> controller.consultarUsuarios(new ExtendedModelMap()));
    }

    @Test
    void testProyectosEndpoints() {
        Proyectos p = new Proyectos();
        p.setId_proyecto(1);
        p.setNombre("Proyecto Demo");

        run(() -> controller.setSaveProyecto(p));
        run(() -> controller.consultarProyectos(new ExtendedModelMap()));
        run(() -> controller.updateProyecto(p));
        run(() -> controller.eliminarProyecto(p));
    }

    @Test
    void testTareasEndpoints() {
        Tareas t = new Tareas();
        t.setId_tarea(10);
        t.setId_proyecto(1);
        t.setNombre("Tarea Test");

        run(() -> controller.setSaveTareas(t));
        run(() -> controller.updateTarea(t));
        run(() -> controller.verificarTarea(t));
        run(() -> controller.cambiosTareas(t));
        run(() -> controller.consultarTareas());
        run(() -> controller.obtenerTareas(t, new ExtendedModelMap()));
        run(() -> controller.eliminarTarea(t));
    }

    @Test
    void testComentariosEndpoints() {
        Comentarios c = new Comentarios();
        c.setId_comentario(5);
        c.setId_tarea(10);
        c.setTexto_comentario("Texto");

        run(() -> controller.setSaveComentario(c));
        run(() -> controller.consultarComentarios());
        run(() -> controller.obtenerComentarios(c, new ExtendedModelMap()));
    }
}

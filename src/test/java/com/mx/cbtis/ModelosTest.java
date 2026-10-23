package com.mx.cbtis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.mx.cbtis.modelo.Asignaciones;
import com.mx.cbtis.modelo.Comentarios;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Tareas;
import com.mx.cbtis.modelo.Usuario;

class ModelosTest {

    @Test
    void testUsuario() {
        Usuario u1 = new Usuario();
        u1.setId_usuario(1);
        u1.setNombre("Ana");
        u1.setCorreo_electronico("ana@test.com");
        u1.setContrasena("1234");
        u1.setRol("Admin");
        u1.setImagen_usuario("avatar.png");

        assertEquals(1, u1.getId_usuario());
        assertEquals("Ana", u1.getNombre());
        assertEquals("ana@test.com", u1.getCorreo_electronico());
        assertEquals("1234", u1.getContrasena());
        assertEquals("Admin", u1.getRol());
        assertEquals("avatar.png", u1.getImagen_usuario());

        Usuario u2 = new Usuario(2, "Luis", "luis@test.com", "pass", "User", "img.png");
        assertEquals(2, u2.getId_usuario());
        assertEquals("Luis", u2.getNombre());
    }

    @Test
    void testProyectos() {
        Proyectos p1 = new Proyectos();
        p1.setId_proyecto(10);
        p1.setNombre("Proyecto 1");
        p1.setDescripcion("Desc");
        p1.setFecha_inicio("2026-01-01");
        p1.setFecha_finalizacion("2026-12-31");
        p1.setEstado_proyecto("Activo");
        p1.setFondo("fondo.png");

        assertEquals(10, p1.getId_proyecto());
        assertEquals("Proyecto 1", p1.getNombre());
        assertEquals("Desc", p1.getDescripcion());
        assertEquals("2026-01-01", p1.getFecha_inicio());
        assertEquals("2026-12-31", p1.getFecha_finalizacion());
        assertEquals("Activo", p1.getEstado_proyecto());
        assertEquals("fondo.png", p1.getFondo());

        Proyectos p2 = new Proyectos(20, "P2", "D2", "2026-02-01", "2026-11-30", "Terminado", "f2.png");
        assertEquals(20, p2.getId_proyecto());
        assertEquals("P2", p2.getNombre());
    }

    @Test
    void testTareas() {
        Tareas t1 = new Tareas();
        t1.setId_tarea(100);
        t1.setNombre("Tarea 1");
        t1.setDescripcion("Desc tarea");
        t1.setFecha_vencimiento("2026-05-01");
        t1.setLista("Pendientes");
        t1.setEstado("En progreso");
        t1.setId_proyecto(10);

        assertEquals(100, t1.getId_tarea());
        assertEquals("Tarea 1", t1.getNombre());
        assertEquals("Desc tarea", t1.getDescripcion());
        assertEquals("2026-05-01", t1.getFecha_vencimiento());
        assertEquals("Pendientes", t1.getLista());
        assertEquals("En progreso", t1.getEstado());
        assertEquals(10, t1.getId_proyecto());

        Tareas t2 = new Tareas(101, "T2", "D2", "2026-06-01", "Hechas", "Completado", 10);
        assertEquals(101, t2.getId_tarea());
        assertEquals("T2", t2.getNombre());
    }

    @Test
    void testComentarios() {
        Comentarios c1 = new Comentarios();
        c1.setId_comentario(50);
        c1.setId_usuario("Ana");
        c1.setId_tarea(100);
        c1.setTexto_comentario("Comentario de prueba");
        c1.setFecha_hora("2026-04-10 10:00:00");

        assertEquals(50, c1.getId_comentario());
        assertEquals("Ana", c1.getId_usuario());
        assertEquals(100, c1.getId_tarea());
        assertEquals("Comentario de prueba", c1.getTexto_comentario());
        assertEquals("2026-04-10 10:00:00", c1.getFecha_hora());

        Comentarios c2 = new Comentarios(51, "Luis", 101, "Texto 2", "2026-04-11 11:00:00");
        assertEquals(51, c2.getId_comentario());
        assertEquals("Luis", c2.getId_usuario());
    }

    @Test
    void testAsignaciones() {
        Asignaciones a1 = new Asignaciones();
        a1.setId_asignacion(1);
        a1.setId_usuario(2);
        a1.setId_tarea(100);
        a1.setFecha_asignacion("2026-03-01");

        assertEquals(1, a1.getId_asignacion());
        assertEquals(2, a1.getId_usuario());
        assertEquals(100, a1.getId_tarea());
        assertEquals("2026-03-01", a1.getFecha_asignacion());

        Asignaciones a2 = new Asignaciones(2, 3, 101, "2026-03-02");
        assertEquals(2, a2.getId_asignacion());
        assertEquals(3, a2.getId_usuario());
    }
}

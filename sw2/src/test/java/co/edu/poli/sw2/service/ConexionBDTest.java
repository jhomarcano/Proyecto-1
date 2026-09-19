package co.edu.poli.sw2.service;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del patron <b>Singleton</b> aplicado en {@link ConexionBD}.
 * <p>
 * No abren ninguna conexion: solo verifican la unicidad de la instancia,
 * el constructor privado y que {@code toString} no filtre credenciales.
 * Por eso no requieren PostgreSQL corriendo.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class ConexionBDTest {
 
    /**
     * Reinicia el campo estatico por reflexion para que cada prueba
     * parta de "aun no existe instancia". Sin esto, la prueba de
     * concurrencia seria trivial si otra prueba ya creo la instancia.
     *
     * @throws Exception si falla el acceso por reflexion
     */
    @BeforeEach
    void reiniciarSingleton() throws Exception {
        Field campo = ConexionBD.class.getDeclaredField("instancia");
        campo.setAccessible(true);
        campo.set(null, null);
    }
 
    @Test
    @DisplayName("getInstancia devuelve siempre el mismo objeto")
    void getInstancia_devuelveLaMismaInstancia() {
        ConexionBD a = ConexionBD.getInstancia();
        ConexionBD b = ConexionBD.getInstancia();
 
        assertNotNull(a);
        assertSame(a, b);
    }
 
    @Test
    @DisplayName("Con hilos simultaneos solo se crea una instancia (double-checked locking)")
    void getInstancia_esSeguraConHilosConcurrentes() throws Exception {
        int hilos = 20;
        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch salida = new CountDownLatch(1);
        Set<ConexionBD> vistas = ConcurrentHashMap.newKeySet();
        List<Future<Void>> futuros = new ArrayList<>();
 
        for (int i = 0; i < hilos; i++) {
            Callable<Void> tarea = () -> {
                salida.await();                    // todos arrancan a la vez
                vistas.add(ConexionBD.getInstancia());
                return null;
            };
            futuros.add(pool.submit(tarea));
        }
 
        salida.countDown();
        for (Future<Void> f : futuros) {
            f.get(5, TimeUnit.SECONDS);
        }
        pool.shutdown();
 
        assertEquals(1, vistas.size(), "Se crearon instancias distintas bajo concurrencia");
    }
 
    @Test
    @DisplayName("El constructor es privado: nadie mas puede hacer new ConexionBD()")
    void constructor_esPrivado() {
        Constructor<?>[] constructores = ConexionBD.class.getDeclaredConstructors();
 
        assertEquals(1, constructores.length);
        assertTrue(Modifier.isPrivate(constructores[0].getModifiers()));
    }
 
    @Test
    @DisplayName("La clase es final: no se puede extender para romper el Singleton")
    void clase_esFinal() {
        assertTrue(Modifier.isFinal(ConexionBD.class.getModifiers()));
    }
 
    @Test
    @DisplayName("El campo de instancia es volatil (requisito del double-checked locking)")
    void campoInstancia_esVolatil() throws Exception {
        Field campo = ConexionBD.class.getDeclaredField("instancia");
 
        assertTrue(Modifier.isVolatile(campo.getModifiers()));
        assertTrue(Modifier.isStatic(campo.getModifiers()));
    }
 
    @Test
    @DisplayName("toString devuelve una cadena fija que no revela credenciales")
    void toString_noExponeCredenciales() {
        String texto = ConexionBD.getInstancia().toString();
 
        assertEquals("ConexionBD[configurada]", texto);
        String password = System.getenv("DB_PASSWORD");
        if (password != null && !password.isBlank()) {
            assertFalse(texto.contains(password));
        }
    }
}
 
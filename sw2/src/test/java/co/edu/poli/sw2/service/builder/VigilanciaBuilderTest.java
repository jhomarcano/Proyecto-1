package co.edu.poli.sw2.service.builder;
 
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del patron <b>Builder</b>: {@link VigilanciaBuilder}.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class VigilanciaBuilderTest {
 
    @Test
    @DisplayName("build() ensambla la Vigilancia con todos los datos acumulados")
    void build_ensamblaTodosLosAtributos() {
        Vigilancia v = new VigilanciaBuilder()
                .conId(7)
                .conSerial("SN-9")
                .conFabricante("Parrot")
                .conModelo("Anafi")
                .conPeso(0.5)
                .conDeteccionTermica(true)
                .build();
 
        assertEquals(7, v.getId());
        assertEquals("SN-9", v.getSerial());
        assertEquals("Parrot", v.getFabricante());
        assertEquals("Anafi", v.getModelo());
        assertEquals(0.5, v.getPeso(), 0.0001);
        assertTrue(v.isDeteccionTermica());
    }
 
    @Test
    @DisplayName("Sin conId el dron se construye con id 0")
    void build_idPorDefectoEsCero() {
        Vigilancia v = new VigilanciaBuilder().conSerial("X").build();
 
        assertEquals(0, v.getId());
    }
 
    @Test
    @DisplayName("Los valores no asignados quedan en su valor por defecto")
    void build_valoresPorDefecto() {
        Vigilancia v = new VigilanciaBuilder().build();
 
        assertNull(v.getSerial());
        assertNull(v.getFabricante());
        assertNull(v.getModelo());
        assertEquals(0.0, v.getPeso(), 0.0001);
        assertFalse(v.isDeteccionTermica());
    }
 
    @Test
    @DisplayName("Cada metodo con... devuelve el mismo builder (fluent API)")
    void metodosCon_devuelvenElMismoBuilder() {
        VigilanciaBuilder b = new VigilanciaBuilder();
 
        assertSame(b, b.conId(1));
        assertSame(b, b.conSerial("s"));
        assertSame(b, b.conFabricante("f"));
        assertSame(b, b.conModelo("m"));
        assertSame(b, b.conPeso(1.0));
        assertSame(b, b.conDeteccionTermica(true));
    }
 
    @Test
    @DisplayName("Dos build() del mismo builder producen objetos distintos")
    void build_dosVecesEntregaInstanciasDistintas() {
        VigilanciaBuilder b = new VigilanciaBuilder().conSerial("S");
 
        Vigilancia uno = b.build();
        Vigilancia dos = b.build();
 
        assertNotSame(uno, dos);
        assertEquals(uno.getSerial(), dos.getSerial());
    }
 
    @Test
    @DisplayName("Un dron construido con el builder se comporta como Vigilancia")
    void build_devuelveTipoVigilancia() {
        assertEquals("VIGILANCIA", new VigilanciaBuilder().build().getTipo());
    }
}
 
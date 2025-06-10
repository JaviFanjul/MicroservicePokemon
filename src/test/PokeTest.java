

package test;

// Importaciones de JUnit 4
import com.sun.tools.javac.util.Assert;
import jdk.jfr.snippets.Snippets;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import main.model.Pokemon;

public class PokeTest {

    Pokemon p;

    @Before // Se ejecuta antes de CADA test
    public void setUp() {
        System.out.println("-> Ejecutando setUp() antes de un test (JUnit 4).");
        this.p = new Pokemon();
    }

    @After // Se ejecuta después de CADA test
    public void tearDown() {
        System.out.println("<- Ejecutando tearDown() después de un test (JUnit 4).");
        this.p = null; // Liberamos el objeto
    }

    @Test
    public void testPokeService() {
        assertEquals(0,p.getSpeed());
    }

    }
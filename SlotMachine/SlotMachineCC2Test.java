import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * 
 * Todas las pruebas deben ejecutarse en modo invisible 
 *  
 * @author (Juan Andrés Rojas Molina)
 * @version (Ciclo 2)
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    @Before
    public void setUp()
    {
        machine = new SlotMachine();

        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");

        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");

        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
    }

    /**
     * Caso de prueba combina lock() con
     * spin(String[]).
     * Al aplicar una nueva configuración con spin(String[]), una rueda fijada 
     * debe mantener su símbolo actual, mientras que las
     * demás ruedas deben adoptar el símbolo solicitado.
     */
    @Test
    public void accordingDcAvShouldKeepLockedWheelUnchangedWhenConfigurationIsApplied()
    {
        machine.lock(2);
        String lockedSymbolBefore = machine.configuration()[1];

        machine.spin(new String[]{"green", "red", "blue"});

        assertTrue(machine.ok());
        assertEquals("la rueda fijada no debe cambiar", lockedSymbolBefore, machine.configuration()[1]);
        assertEquals("green", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[2]);
    }

    /**
     * Caso de prueba combina lock()/unlock() con
     * spin(wheel, steps)
     * Una rueda fijada no debe girar al aplicar spin(wheel, steps),
     * pero al ser liberada con unlock(), debe poder girar con total normalidad.
     */
    @Test
    public void accordingDcAvShouldNotSpinLockedWheelButShouldSpinAfterUnlock()
    {
        machine.lock(3);
        String beforeAttempt = machine.configuration()[2];

        machine.spin(3, 1);
        assertFalse("no debe girar estando fijada", machine.ok());
        assertEquals(beforeAttempt, machine.configuration()[2]);

        machine.unlock(3);
        machine.spin(3, 1);
        assertTrue("debe girar una vez liberada", machine.ok());
        assertEquals("red", machine.configuration()[2]); 
    }

    /**
     * Caso de prueba — combina swap() con lock()
     * swap() debe intercambiar los símbolos a pesar de que una de las ruedas esté fijada, 
     * ya que fijar solo impide el giro, no el intercambio de posición.
     */
    @Test
    public void accordingDcAvShouldSwapWheelsEvenWhenOneIsLocked()
    {
        machine.lock(1);
        String[] before = machine.configuration(); 

        machine.swap(1, 3);

        assertTrue(machine.ok());
        assertEquals(before[2], machine.configuration()[0]); 
        assertEquals(before[0], machine.configuration()[2]); 
    }
}
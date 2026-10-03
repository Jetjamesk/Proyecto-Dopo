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
     * Test case: lock() with spin(String[]).
     * A locked wheel keeps its symbol, and the other wheels take the requested ones.
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
     * Test case: lock()/unlock() with spin(wheel, steps).
     * A locked wheel does not spin, but it spins normally after unlock().
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
     * Test case: swap() with lock().
     * swap() works even if a wheel is locked, since locking only prevents spinning.
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
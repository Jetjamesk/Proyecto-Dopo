import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Shared test cases of cycle 4.
 * All the tests run in invisible mode.
 *
 * @author (Juan Andrés Rojas Molina)
 * @version (Ciclo 4)
 */
public class SlotMachineCC4Test
{
    private SlotMachine machine;

    /** Creates, before each test, an empty invisible machine. */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
    }

    /**
     * Test case: rebel + lefty.
     * A rebel wheel can not be locked, so it keeps spinning, and the lefty wheel
     * on its right copies it every time.
     */
    @Test
    public void accordingJuanShouldLeftyCopyARebelWheelThatCanNotBeLocked()
    {
        machine.addWheel("rebel", 1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");

        machine.lock(1);
        assertFalse(machine.ok());

        machine.spin(1, 1);
        machine.spin(2, 1);

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Test case: normal + lefty + ephemeral + shy + wild.
     * The lefty wheel has the three new symbols. It copies its left wheel, and when a
     * wild symbol is in front of the pointer the machine gives the jackpot.
     */
    @Test
    public void accordingJuanShouldCombineLeftyWithEphemeralShyAndWildSymbols()
    {
        machine.addWheel("normal", 1);
        machine.addSymbol("normal", 1, "red");
        machine.addSymbol("normal", 2, "blue");
        machine.addWheel("lefty", 2);
        machine.addSymbol("ephemeral", 1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("wild", 3, "magenta");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "magenta");
        assertTrue("wild completes the jackpot", machine.isJackpot());

        machine.spin(2, 1);

        assertTrue(machine.ok());
        assertEquals("the lefty copies blue", "blue", machine.configuration()[1]);
        assertTrue("same color in both wheels", machine.isJackpot());

        machine.spin(1, 1);
        machine.spin(2, 1);

        assertEquals("red", machine.configuration()[0]);
        assertEquals("the lefty copies red", "red", machine.configuration()[1]);
    }
}
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * wheel types (normal, lefty, rebel) and
 * symbol types (normal, ephemeral, shy, wild).
 * The machine is checked only through its public methods; the behavior of each
 * type of symbol is checked directly on Wheel and Symbol.
 * All the tests run in invisible mode.
 *
 * @author (Juan Andrés Rojas Molina)
 * @version (Ciclo 4)
 */
public class SlotMachineC4Test
{
    private SlotMachine machine;

    /** Creates, before each test, an empty invisible machine. */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
    }

    /** Adds to the machine a wheel of the given type with red, blue and green. */
    private void addWheelWithColors(String type, int pos)
    {
        machine.addWheel(type, pos);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    // ----------------------------- Wheels -----------------------------

    /** Test case: addWheel(type, pos) creates the three types of wheels. */
    @Test
    public void shouldAddNormalLeftyAndRebelWheels()
    {
        machine.addWheel("normal", 1);
        assertTrue(machine.ok());
        machine.addWheel("lefty", 2);
        assertTrue(machine.ok());
        machine.addWheel("rebel", 3);
        assertTrue(machine.ok());

        assertEquals(3, machine.configuration().length);
    }

    /** Test case: addWheel(type, pos) fails when the type does not exist. */
    @Test
    public void shouldNotAddWheelWithUnknownType()
    {
        machine.addWheel("flying", 1);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /** Test case: a lefty wheel copies the state of the wheel on its left when it spins. */
    @Test
    public void shouldLeftyCopyLeftWheelWhenItSpinsWithSteps()
    {
        addWheelWithColors("normal", 1);
        addWheelWithColors("lefty", 2);
        machine.placeSymbol(1, "green");
        machine.placeSymbol(2, "red");

        machine.spin(2, 1);

        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[1]);
    }

    /** Test case: a lefty wheel also copies its left wheel on spin(wheel) and spin(). */
    @Test
    public void shouldLeftyCopyLeftWheelWhenWheelsSpinRandomly()
    {
        addWheelWithColors("normal", 1);
        addWheelWithColors("lefty", 2);

        for (int i = 0; i < 10; i++)
        {
            machine.spin();
            assertEquals(machine.configuration()[0], machine.configuration()[1]);
        }
        machine.spin(2);
        assertEquals(machine.configuration()[0], machine.configuration()[1]);
    }

    /** Test case: a lefty wheel with no wheel on its left spins like a normal one. */
    @Test
    public void shouldLeftySpinNormallyWhenThereIsNoWheelOnItsLeft()
    {
        addWheelWithColors("lefty", 1);
        machine.placeSymbol(1, "red");

        machine.spin(1, 1);

        assertEquals("blue", machine.configuration()[0]);
    }

    /** Test case: a lefty wheel spins normally if the left color does not exist in it. */
    @Test
    public void shouldLeftySpinNormallyWhenLeftColorDoesNotExistInIt()
    {
        machine.addWheel("normal", 1);
        machine.addSymbol(1, "yellow");
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(2, "red");

        machine.spin(2, 1);

        assertEquals("blue", machine.configuration()[1]);
    }

    /** Test case: a rebel wheel can not be locked, so it keeps spinning. */
    @Test
    public void shouldRebelRefuseToBeLocked()
    {
        addWheelWithColors("rebel", 1);
        machine.placeSymbol(1, "red");

        machine.lock(1);
        assertFalse(machine.ok());

        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /** Test case: a rebel wheel can not be swapped, the machine stays the same. */
    @Test
    public void shouldRebelRefuseToBeSwapped()
    {
        addWheelWithColors("rebel", 1);
        addWheelWithColors("normal", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "green");

        machine.swap(1, 2);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("green", machine.configuration()[1]);
    }

    /** Test case: a rebel wheel can not be deleted. */
    @Test
    public void shouldRebelRefuseToBeDeleted()
    {
        addWheelWithColors("rebel", 1);

        machine.delWheel(1);

        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    /** Test case: a normal wheel can still be locked, swapped and deleted. */
    @Test
    public void shouldNormalWheelAcceptLockSwapAndDelete()
    {
        addWheelWithColors("normal", 1);
        addWheelWithColors("normal", 2);

        machine.lock(1);
        assertTrue(machine.ok());
        machine.swap(1, 2);
        assertTrue(machine.ok());
        machine.delWheel(1);
        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    // ----------------------------- Symbols -----------------------------

    /** Test case: addSymbol(type, pos, color) adds the four types of symbols. */
    @Test
    public void shouldAddEachTypeOfSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol("normal", 1, "red");
        assertTrue(machine.ok());
        machine.addSymbol("ephemeral", 2, "blue");
        assertTrue(machine.ok());
        machine.addSymbol("shy", 3, "green");
        assertTrue(machine.ok());
        machine.addSymbol("wild", 4, "magenta");
        assertTrue(machine.ok());

        assertEquals(4, machine.symbols().length);
    }

    /** Test case: addSymbol(type, pos, color) fails with an unknown type or without wheels. */
    @Test
    public void shouldNotAddSymbolWithUnknownTypeOrWithoutWheels()
    {
        machine.addSymbol("shy", 1, "red");
        assertFalse(machine.ok());

        machine.addWheel(1);
        machine.addSymbol("giant", 1, "red");
        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    /** Test case: a wheel creates the symbols of each type. */
    @Test
    public void shouldWheelCreateEachTypeOfSymbol()
    {
        Wheel wheel = new Wheel();
        wheel.addSymbol("normal", 1, "red");
        wheel.addSymbol("ephemeral", 2, "blue");
        wheel.addSymbol("shy", 3, "green");
        wheel.addSymbol("wild", 4, "magenta");

        assertEquals("normal", wheel.symbolAt(1).getType());
        assertEquals("ephemeral", wheel.symbolAt(2).getType());
        assertEquals("shy", wheel.symbolAt(3).getType());
        assertEquals("wild", wheel.symbolAt(4).getType());
        assertFalse(wheel.addSymbol("giant", 5, "black"));
    }

    /** Test case: an ephemeral symbol gets smaller on every spin until it is a dot. */
    @Test
    public void shouldEphemeralShrinkOnEverySpinUntilItIsADot()
    {
        Wheel wheel = new Wheel();
        wheel.addSymbol("ephemeral", 1, "red");
        wheel.addSymbol("normal", 2, "blue");
        Symbol ephemeral = wheel.symbolAt(1);
        int before = ephemeral.getSize();

        wheel.spin(1);
        assertTrue(ephemeral.getSize() < before);

        for (int i = 0; i < 20; i++)
        {
            wheel.spin(1);
        }
        assertEquals(EphemeralSymbol.MIN_SIZE, ephemeral.getSize());
    }

    /** Test case: only ephemeral symbols shrink, normal ones keep their size. */
    @Test
    public void shouldNormalSymbolKeepItsSizeWhenTheWheelSpins()
    {
        Wheel wheel = new Wheel();
        wheel.addSymbol("ephemeral", 1, "red");
        wheel.addSymbol("normal", 2, "blue");

        wheel.spin(3);

        assertEquals(Symbol.NORMAL_SIZE, wheel.symbolAt(2).getSize());
    }

    /** Test case: a shy symbol toggles visible/invisible each time it is selected. */
    @Test
    public void shouldShyToggleVisibilityEachTimeItIsSelected()
    {
        Wheel wheel = new Wheel();
        wheel.addSymbol("shy", 1, "red");
        wheel.addSymbol("normal", 2, "blue");
        Symbol shy = wheel.symbolAt(1);
        assertFalse(shy.isHidden());

        wheel.placeSymbol("red");
        assertTrue(shy.isHidden());

        wheel.placeSymbol("red");
        assertFalse(shy.isHidden());
    }

    /** Test case: a shy symbol also toggles when a spin leaves it in front of the pointer. */
    @Test
    public void shouldShyToggleWhenASpinSelectsIt()
    {
        Wheel wheel = new Wheel();
        wheel.addSymbol("normal", 1, "red");
        wheel.addSymbol("shy", 2, "blue");
        wheel.placeSymbol("red");

        wheel.spin(1);

        assertEquals("blue", wheel.currentSymbol());
        assertTrue(wheel.symbolAt(2).isHidden());
    }

    /** Test case: a normal symbol never changes its visibility when it is selected. */
    @Test
    public void shouldNormalSymbolNotToggleVisibilityWhenSelected()
    {
        Wheel wheel = new Wheel();
        wheel.addSymbol("normal", 1, "red");

        wheel.placeSymbol("red");

        assertFalse(wheel.symbolAt(1).isHidden());
    }

    // ----------------------------- New type: wild symbol -----------------------------

    /** Test case: a wild symbol completes a jackpot with any color. */
    @Test
    public void shouldWildCompleteAJackpotWithAnyColor()
    {
        addWheelWithColors("normal", 1);
        machine.addWheel("normal", 2);
        machine.addSymbol("wild", 1, "magenta");
        addWheelWithColors("normal", 3);
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "magenta");
        machine.placeSymbol(3, "blue");

        assertTrue(machine.isJackpot());
    }

    /** Test case: wild does not make a jackpot if the other wheels show different colors. */
    @Test
    public void shouldWildNotMakeAJackpotWhenOtherColorsDiffer()
    {
        addWheelWithColors("normal", 1);
        machine.addWheel("normal", 2);
        machine.addSymbol("wild", 1, "magenta");
        addWheelWithColors("normal", 3);
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "magenta");
        machine.placeSymbol(3, "red");

        assertFalse(machine.isJackpot());
    }
}
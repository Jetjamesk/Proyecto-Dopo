import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas del Ciclo 2 de SlotMachine.
 * Todas las pruebas se ejecutan en modo invisible
 *
 * @author (Juan Andrés Rojas Molina)
 * @version (Ciclo 2)
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    /**
     * Creates, before each test, an invisible machine with 3 wheels and
     * known symbols, leaving a predictable starting configuration.
     */
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
    
        machine.addWheel(3);
        machine.addSymbol(1, "green");
        machine.addSymbol(2, "red");
    
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "red");
    }
    
    
    /** Test case: swap() exchanges the visible symbols of two wheels. */
    @Test
    public void swapShouldExchangeVisibleSymbolsOfTwoWheels()
    {
        String[] before = machine.configuration(); 
        machine.swap(1, 3);
        String[] after = machine.configuration();
    
        assertTrue(machine.ok());
        assertEquals(before[0], after[2]);
        assertEquals(before[2], after[0]);
        assertEquals(before[1], after[1]); 
    }
    
    
    /** Test case: swap() fails if the machine has fewer than two wheels. */
    @Test
    public void swapShouldFailWhenFewerThanTwoWheelsExist()
    {
        SlotMachine oneWheel = new SlotMachine();
        oneWheel.addWheel(1);
        oneWheel.addSymbol(1, "red");
    
        oneWheel.swap(1, 1);
    
        assertFalse(oneWheel.ok());
    }
    
    
    /** Test case: swap() adjusts out-of-range positions instead of failing. */
    @Test
    public void swapShouldClampOutOfRangePositionsInsteadOfFailing()
    {
        machine.swap(-5, 999); 
        assertTrue(machine.ok());
    }
    
    
    /** Test case: lock() keeps the wheel unchanged when all wheels spin. */
    @Test
    public void lockShouldKeepWheelUnchangedDuringSpinAll()
    {
        machine.lock(1);
        String beforeLock = machine.configuration()[0];
    
        for (int i = 0; i < 25; i++)
        {
            machine.spin();
        }
    
        assertEquals(beforeLock, machine.configuration()[0]);
    }
    
    
    /** Test case: spin(wheel, steps) does not move a locked wheel. */
    @Test
    public void spinWithStepsShouldFailOnLockedWheel()
    {
        machine.lock(2);
        String beforeLock = machine.configuration()[1];
    
        machine.spin(2, 3);
    
        assertFalse(machine.ok());
        assertEquals(beforeLock, machine.configuration()[1]);
    }
    
    
    /** Test case: unlock() lets the wheel spin again. */
    @Test
    public void unlockShouldAllowWheelToSpinAgain()
    {
        machine.lock(2);
        machine.unlock(2);
    
        machine.spin(2, 1);
    
        assertTrue(machine.ok());
    }
    
    
    /** Test case: lock() fails on a machine with no wheels. */
    @Test
    public void lockShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine empty = new SlotMachine();
        empty.lock(1);
        assertFalse(empty.ok());
    }
    
    
    /** Test case: spin(wheel, steps) rotates the wheel by exactly that many steps (circular). */
    @Test
    public void spinWithStepsShouldRotateWheelByGivenSteps()
    {
        machine.spin(1, 1); 
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
    
        machine.spin(1, 2); 
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }
    
    
    /** Test case: spin(wheel, steps) accepts negative steps (backwards rotation). */
    @Test
    public void spinWithNegativeStepsShouldRotateBackwards()
    {
        machine.spin(1, -1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }
    
    
    /** Test case: spin(wheel, steps) fails if the machine has no wheels. */
    @Test
    public void spinWithStepsShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine empty = new SlotMachine();
        empty.spin(1, 3);
        assertFalse(empty.ok());
    }
    
    
    /** Test case: spin(config) sets each wheel to the given symbol. */
    @Test
    public void spinWithConfigurationShouldSetEachWheelToGivenSymbol()
    {
        machine.spin(new String[]{"red", "blue", "green"});
    
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "blue", "green"}, machine.configuration());
    }
    
    
    /** Test case: spin(config) does not change locked wheels. */
    @Test
    public void spinWithConfigurationShouldNotChangeLockedWheels()
    {
        machine.lock(2);
        String lockedBefore = machine.configuration()[1];
    
        machine.spin(new String[]{"red", "green", "green"});
    
        assertTrue(machine.ok());
        assertEquals(lockedBefore, machine.configuration()[1]);
        assertEquals("red", machine.configuration()[0]);
        assertEquals("green", machine.configuration()[2]);
    }
    
    
    /** Test case: spin(config) fails if the array length differs from the wheel count. */
    @Test
    public void spinWithConfigurationShouldFailWhenArrayLengthDoesNotMatchWheelCount()
    {
        String[] before = machine.configuration();
    
        machine.spin(new String[]{"red", "blue"}); 
    
        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration()); 
    }
    
    
    /** Test case: spin(config) fails on a null array. */
    @Test
    public void spinWithConfigurationShouldFailWhenArrayIsNull()
    {
        machine.spin((String[]) null);
        assertFalse(machine.ok());
    }
}
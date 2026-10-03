import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.util.HashSet;

/**
 * The test class SlotMachineContestTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SlotMachineContestTest
{
    /**
     * Reads the private "visible" attribute of the machine
     * (SlotMachine has no method to query it).
     */
    private boolean isVisible(SlotMachine machine) throws Exception
    {
        Field field = SlotMachine.class.getDeclaredField("visible");
        field.setAccessible(true);
        return field.getBoolean(machine);
    }
    
    
    /** Returns the colors of wheel w (swaps it to position 1 and back). */
    private String[] wheelSymbols(SlotMachine machine, int w)
    {
        machine.swap(1, w);
        String[] symbols = machine.symbols();
        machine.swap(1, w);
        return symbols;
    }
    
    // ---------------------------------------------------------------- SlotMachine(n)
    // Note: SlotMachine only has the constructor SlotMachine(int n), which creates
    // n wheels with n symbols each. These tests use a single size n.
    
    /**
     * Test case: the symbols of a wheel have different colors,
     * and all wheels have the same colors.
     */
    @Test
    public void constructorWithNShouldUseDifferentColors()
    {
        int n = 5;
        SlotMachine machine = new SlotMachine(n);
        HashSet<String> first = new HashSet<String>();
        for (String color : wheelSymbols(machine, 1))
        {
            first.add(color);
        }
        assertEquals("colores diferentes", n, first.size());
        for (int wheel = 2; wheel <= n; wheel++)
        {
            HashSet<String> colors = new HashSet<String>();
            for (String color : wheelSymbols(machine, wheel))
            {
                colors.add(color);
            }
            assertEquals("rueda " + wheel, first, colors);
        }
    }
    
    
    /**
     * Test case: colors are random (two machines differ), and
     * with a large n they are all still different.
     */
    @Test
    public void constructorWithNShouldUseRandomColors()
    {
        HashSet<String> firstColors = new HashSet<String>();
        for (int i = 0; i < 20; i++)
        {
            firstColors.add(new SlotMachine(2).symbols()[0]);
        }
        assertTrue("los colores deben variar", firstColors.size() > 1);
    
        int big = 100;
        HashSet<String> many = new HashSet<String>();
        for (String color : new SlotMachine(big).symbols())
        {
            many.add(color);
        }
        assertEquals(big, many.size());
    }
    
    
    /**
     * Test case: if n is not positive, no wheels are created and ok() is false.
     */
    @Test
    public void constructorWithNShouldFailWhenNIsNotPositive()
    {
        for (int n : new int[]{0, -1, -10})
        {
            SlotMachine machine = new SlotMachine(n);
            assertFalse("n=" + n, machine.ok());
            assertEquals(0, machine.configuration().length);
        }
    }
    
    
    /**
     * Test case: SlotMachine(n) creates n wheels with n symbols each.
     */
    @Test
    public void constructorWithNShouldCreateNWheelsWithNSymbols()
    {
        for (int n : new int[]{1, 2, 5, 12, 50})
        {
            SlotMachine machine = new SlotMachine(n);
            assertTrue("n=" + n, machine.ok());
            assertEquals(n, machine.configuration().length);
            assertEquals(n, machine.symbols().length);
        }
    }
    
    
    /**
     * Test case: every wheel has the same n different colors, in its own order.
     */
    @Test
    public void constructorWithNShouldGiveEveryWheelTheSameNDifferentColors()
    {
        int n = 6;
        SlotMachine machine = new SlotMachine(n);
        HashSet<String> first = new HashSet<String>();
        for (String color : wheelSymbols(machine, 1))
        {
            first.add(color);
        }
        assertEquals(n, first.size());
        for (int wheel = 2; wheel <= n; wheel++)
        {
            HashSet<String> colors = new HashSet<String>();
            for (String color : wheelSymbols(machine, wheel))
            {
                colors.add(color);
            }
            assertEquals("rueda " + wheel, first, colors);
        }
    }
    
    
    /**
     * Test case: the machine never starts in jackpot (n >= 2).
     */
    @Test
    public void constructorWithNShouldNeverStartInJackpot()
    {
        for (int n = 2; n <= 6; n++)
        {
            for (int i = 0; i < 200; i++)
            {
                assertFalse("n=" + n, new SlotMachine(n).isJackpot());
            }
        }
    }
    
    
    /**
     * Test case: a machine with one wheel and one symbol is already a jackpot,
     * and a non-positive n creates nothing.
     */
    @Test
    public void constructorWithNShouldHandleTheLimits()
    {
        assertTrue(new SlotMachine(1).isJackpot());
        SlotMachine empty = new SlotMachine(0);
        assertFalse(empty.ok());
        assertEquals(0, empty.configuration().length);
    }
    
    
    /**
     * Test case: the machine is created invisible.
     */
    @Test
    public void constructorsShouldLeaveMachineInvisible() throws Exception
    {
        assertFalse(isVisible(new SlotMachine(4)));
        assertFalse(isVisible(new SlotMachine(3)));
    }
    
    // ---------------------------------------------------------------- distinctSymbols()
    
    /**
     * Test case: distinctSymbols() counts all different symbols in the machine,
     * not only the visible ones. Spinning does not change the total;
     * adding a new symbol does.
     */
    @Test
    public void distinctSymbolsShouldCountAllSymbolsInTheMachine()
    {
        SlotMachine machine = new SlotMachine();
        for (int wheel = 1; wheel <= 3; wheel++)
        {
            machine.addWheel(wheel);
            machine.addSymbol(1, "red");
            machine.addSymbol(2, "blue");
            machine.addSymbol(3, "green");
        }
        // las 3 ruedas comparten los mismos 3 simbolos
        assertEquals(3, machine.distinctSymbols());
    
        // cambiar cual simbolo esta visible no cambia el total
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "red");
        assertEquals(3, machine.distinctSymbols());
    
        // agregar un simbolo nuevo (a la rueda activa) si aumenta el total
        machine.addSymbol(4, "yellow");
        assertEquals(4, machine.distinctSymbols());
    }
    
    
    /**
     * Test case: without wheels, distinctSymbols() fails (returns 0, ok() is false).
     */
    @Test
    public void distinctSymbolsShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine machine = new SlotMachine();
        assertEquals(0, machine.distinctSymbols());
        assertFalse(machine.ok());
    }
    
    
    /**
     * Test case: solve reaches a jackpot for small and medium sizes.
     * The jackpot is checked with isJackpot(), since distinctSymbols()
     * counts all symbols and does not change when spinning.
     */
    @Test
    public void solveShouldReachJackpotForSmallAndMediumSizes()
    {
        for (int n = 2; n <= 12; n++)
        {
            for (int i = 0; i < 25; i++)
            {
                SlotMachineContest.solve(n);
                assertTrue("n=" + n, SlotMachineContest.machine.isJackpot());
            }
        }
    }
    
    
    /**
     * Test case: solve reaches a jackpot with the maximum size (n = 50).
     */
    @Test
    public void solveShouldReachJackpotForMaximumSize()
    {
        for (int i = 0; i < 5; i++)
        {
            SlotMachineContest.solve(50);
            assertTrue(SlotMachineContest.machine.isJackpot());
        }
    }
    
    
    /**
     * Test case: solve stays within the action limit and the 2n^2 + 1 bound.
     */
    @Test
    public void solveShouldNotExceedTheActionLimit()
    {
        for (int n : new int[]{2, 3, 10, 30, 50})
        {
            int[][] actions = SlotMachineContest.solve(n);
            assertTrue("n=" + n, actions.length <= SlotMachineContest.MAX_ACTIONS);
            assertTrue("n=" + n, actions.length <= 2 * n * n + 1);
        }
    }
    
    
    /**
     * Test case: each action is {wheel, steps} with wheel in 1..n and steps in 1..n-1.
     */
    @Test
    public void solveShouldReturnValidActions()
    {
        int n = 7;
        int[][] actions = SlotMachineContest.solve(n);
        assertTrue(actions.length > 0);
        for (int[] action : actions)
        {
            assertEquals(2, action.length);
            assertTrue(action[0] >= 1 && action[0] <= n);
            assertTrue(action[1] >= 1 && action[1] <= n - 1);
        }
    }

    
    /**
     * Test case: undoing the actions returns the machine to its initial
     * state (no jackpot), and repeating them reaches a jackpot.
     */
    @Test
    public void solveActionsShouldTakeTheInitialMachineToJackpot()
    {
        int n = 6;
        for (int i = 0; i < 20; i++)
        {
            int[][] actions = SlotMachineContest.solve(n);
            SlotMachine machine = SlotMachineContest.machine;
            for (int k = actions.length - 1; k >= 0; k--)
            {
                machine.spin(actions[k][0], n - actions[k][1]);
            }
            assertFalse("al inicio no hay jackpot", machine.isJackpot());
            for (int[] action : actions)
            {
                machine.spin(action[0], action[1]);
            }
            assertTrue("repetir las acciones gana", machine.isJackpot());
        }
    }
    
    
    /**
     * Test case: the machine stays invisible during solve.
     */
    @Test
    public void solveShouldKeepMachineInvisible() throws Exception
    {
        SlotMachineContest.solve(5);
        assertFalse(isVisible(SlotMachineContest.machine));
    }
    
    
    /**
     * Test case: if n is less than 2, there is nothing to solve (no actions).
     */
    @Test
    public void solveShouldReturnNoActionsWhenThereIsNothingToSolve()
    {
        for (int n : new int[]{1, 0, -3})
        {
            assertEquals("n=" + n, 0, SlotMachineContest.solve(n).length);
        }
    }
    
    // ---------------------------------------------------------------- simulate(n)
    
    /**
     * Test case: simulate does nothing if n is outside the limits.
     */
    @Test
    public void simulateShouldDoNothingWhenNIsOutsideTheLimits()
    {
        SlotMachineContest.machine = null;
        SlotMachineContest.simulate(0);
        SlotMachineContest.simulate(SlotMachineContest.MAX_SIMULATION_SIZE + 1);
        assertNull(SlotMachineContest.machine);
    }
    
    
    /**
     * Test case: after simulate, the machine is visible and in jackpot.
     * It needs a display, so it is skipped if there is none.
     */
    @Test
    public void simulateShouldEndWithVisibleMachineInJackpot() throws Exception
    {
        Assume.assumeFalse("necesita pantalla", GraphicsEnvironment.isHeadless());
        SlotMachineContest.simulate(2);
        assertTrue(isVisible(SlotMachineContest.machine));
        assertTrue(SlotMachineContest.machine.isJackpot());
        SlotMachineContest.machine.makeInvisible();
    }
}
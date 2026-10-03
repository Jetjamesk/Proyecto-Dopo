import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test Cases for Cycle 3.
 *
 * @author (Juan Andrés Rojas Molina)
 * @version (Ciclo 3)
 */
public class SlotMachineContestCTest
{
    /**
     * Test case: solve() with the SlotMachine(n) constructor.
     * For each size, it returns valid {wheel, steps} actions within the 10,000 limit.
     */
    @Test
    public void accordingXxYyShouldSolveWithValidActionsInsideTheLimit()
    {
        for (int n = 2; n <= 8; n++)
        {
            int[][] actions = SlotMachineContest.solve(n);
            assertTrue("n=" + n, actions.length > 0);
            assertTrue("n=" + n, actions.length <= 10000);
            for (int[] action : actions)
            {
                assertTrue(action[0] >= 1 && action[0] <= n);
                assertTrue(action[1] >= 1 && action[1] <= n - 1);
            }
        }
    }
    
    
    /**
     * Test case: swap(), lock(), unlock(), spin() and isJackpot() on a machine of n wheels.
     * Swapping keeps the distinct symbols, a locked wheel does not spin,
     * and the machine can be set to a jackpot.
     */
    @Test
    public void accordingXxYyShouldKeepC2BehaviorOnAMachineOfNWheelsAndNSymbols()
    {
        int n = 5;
        SlotMachine machine = new SlotMachine(n);
        String[] before = machine.configuration();
        int distinctBefore = machine.distinctSymbols();
    
        machine.swap(1, 5);
        assertTrue(machine.ok());
        assertEquals(before[4], machine.configuration()[0]);
        assertEquals(distinctBefore, machine.distinctSymbols());
    
        String lockedSymbol = machine.configuration()[2];
        machine.lock(3);
        machine.spin(3, 2);
        assertFalse("no gira fijada", machine.ok());
        assertEquals(lockedSymbol, machine.configuration()[2]);
    
        machine.unlock(3);
        machine.spin(3, 2);
        assertTrue(machine.ok());
        assertNotEquals("gira liberada", lockedSymbol, machine.configuration()[2]);
    
        String color = machine.symbols()[0];
        machine.spin(new String[]{color, color, color, color, color});
        assertTrue(machine.ok());
        assertTrue(machine.isJackpot());
        assertEquals(distinctBefore, machine.distinctSymbols());
    }
}

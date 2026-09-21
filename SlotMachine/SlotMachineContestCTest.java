import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Casos de prueba compartidos del Ciclo 3. Solo usan la interfaz publica y lo
 * que dice el enunciado (SlotMachine(n), spin, distinctSymbols, solve), para
 * que puedan ejecutarse contra la implementacion de otro grupo. Cada uno
 * combina la solucion o el constructor del ciclo 3 con metodos de ciclos
 * anteriores. Todas las pruebas se ejecutan en modo invisible.
 *
 * @author (Juan Andrés Rojas Molina)
 * @version (Ciclo 3)
 */
public class SlotMachineContestCTest
{
    /**
     * Caso de prueba: combina solve() con el constructor SlotMachine(n).
     * Para varios tamaños solve devuelve acciones validas {rueda, pasos} y no
     * pasa el limite de 10000 acciones de la maratón.
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
     * Caso de prueba: combina SlotMachine(n) con swap(), lock(), unlock(),
     * spin(wheel, steps), spin(setSymbols), symbols(), configuration() y
     * distinctSymbols().
     * Intercambiar ruedas no cambia cuantos simbolos se ven; una rueda fijada
     * no gira; y como cada rueda tiene los n simbolos, se puede llevar toda la
     * maquina a un jackpot con un solo color.
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
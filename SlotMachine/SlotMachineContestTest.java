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
     * Lee el atributo privado visible de la maquina (SlotMachine no tiene un
     * metodo para consultarlo).
     */
    private boolean isVisible(SlotMachine machine) throws Exception
    {
        Field field = SlotMachine.class.getDeclaredField("visible");
        field.setAccessible(true);
        return field.getBoolean(machine);
    }

    /** Colores de la rueda w (la lleva a la posicion 1 con swap y la deja igual). */
    private String[] wheelSymbols(SlotMachine machine, int w)
    {
        machine.swap(1, w);
        String[] symbols = machine.symbols();
        machine.swap(1, w);
        return symbols;
    }

    // ---------------------------------------------------------------- SlotMachine(n)
    // Nota: SlotMachine solo tiene el constructor SlotMachine(int n), que crea
    // n ruedas con n simbolos cada una (no existe un SlotMachine(n, y) con
    // cantidades independientes de ruedas y simbolos). Las pruebas de esta
    // seccion antes asumian ese segundo constructor; se dejaron como pruebas
    // equivalentes usando un solo tamaño n, y con "n grande" para cubrir el
    // caso de muchos simbolos.

    /**
     * Caso de prueba: los simbolos de una rueda son de colores diferentes y
     * todas las ruedas tienen los mismos colores.
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
     * Caso de prueba: los colores son aleatorios (dos maquinas no tienen los
     * mismos colores) y con n grande siguen siendo todos diferentes.
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
     * Caso de prueba: si n no es positivo no se crean ruedas y ok() es false.
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
     * Caso de prueba: SlotMachine(n) crea n ruedas con n simbolos cada una.
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
     * Caso de prueba: cada rueda tiene los mismos n simbolos, de colores
     * diferentes, en su propio orden.
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
     * Caso de prueba: la maquina nunca empieza en jackpot (n >= 2).
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
     * Caso de prueba: la maquina de una sola rueda y un solo simbolo ya esta
     * en jackpot; y con n no positivo no se crea nada.
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
     * Caso de prueba: la maquina se crea invisible.
     */
    @Test
    public void constructorsShouldLeaveMachineInvisible() throws Exception
    {
        assertFalse(isVisible(new SlotMachine(4)));
        assertFalse(isVisible(new SlotMachine(3)));
    }

    // ---------------------------------------------------------------- distinctSymbols()

    /**
     * Caso de prueba: distinctSymbols() cuenta todos los simbolos distintos
     * que existen en las ruedas de la maquina, no solo el que esta visible
     * en cada una (spin/placeSymbol solo cambian cual se ve, no la lista de
     * simbolos de la rueda).
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
     * Caso de prueba: sin ruedas distinctSymbols() falla (ok() false, 0).
     */
    @Test
    public void distinctSymbolsShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine machine = new SlotMachine();
        assertEquals(0, machine.distinctSymbols());
        assertFalse(machine.ok());
    }


    /**
     * Caso de prueba: solve gana (jackpot) para tamaños pequeños y medianos.
     * Nota: distinctSymbols() no sirve para verificar el jackpot porque
     * cuenta TODOS los simbolos que existen en las ruedas (no solo el
     * visible), y ese numero no cambia al girar; el jackpot se verifica con
     * isJackpot(), que sí mira los simbolos visibles.
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
     * Caso de prueba: solve gana con el tamaño maximo de la maratón (n = 50).
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
     * Caso de prueba: solve no pasa el limite de acciones de la maratón y
     * cumple la cota 2n^2 + 1.
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
     * Caso de prueba: cada accion es {rueda, pasos} con rueda en 1..n y
     * pasos en 1..n-1.
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
     * Caso de prueba: las acciones devueltas son la solucion. Deshaciendolas
     * la maquina vuelve a su configuracion inicial (que no es un jackpot) y
     * repitiendolas gana.
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
     * Caso de prueba: durante solve la maquina permanece invisible.
     */
    @Test
    public void solveShouldKeepMachineInvisible() throws Exception
    {
        SlotMachineContest.solve(5);
        assertFalse(isVisible(SlotMachineContest.machine));
    }

    /**
     * Caso de prueba: si n es menor que 2 no hay nada que resolver (sin acciones).
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
     * Caso de prueba: simulate no hace nada si n esta fuera de los limites
     * (no se resuelve ni se abre ventana).
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
     * Caso de prueba: despues de simulate la maquina esta visible y en
     * jackpot. Necesita pantalla: si no hay, se omite.
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
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad del Ciclo 2 (Refactoring y Extensión) de SlotMachine.
 * Cubre los requisitos funcionales 9 a 12:
 *   9.  Intercambiar dos ruedas          -> swap(int, int)
 *   10. Fijar y soltar una rueda         -> lock(int) / unlock(int)
 *   11. Rotar una rueda un número de pasos -> spin(int, int)
 *   12. Dejar la máquina en una configuración dada -> spin(String[])
 *
 * Todas las pruebas se ejecutan en modo invisible: nunca se invoca
 * makeVisible(), por lo tanto la simulación no despliega el canvas.
 *
 * @author (Iniciales según convención del curso)
 * @version (Ciclo 2 - 2026-2)
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    /**
     * Crea, antes de cada prueba, una máquina de 3 ruedas invisible con
     * símbolos conocidos, y deja una configuración de partida predecible.
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

        // Configuración de partida conocida: [blue, blue, red]
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "red");
    }

    // ---------- Requisito 9: swap ----------

    /** swap QUÉ DEBERÍA HACER: intercambiar los símbolos visibles de dos ruedas. */
    @Test
    public void swapShouldExchangeVisibleSymbolsOfTwoWheels()
    {
        String[] before = machine.configuration(); // [blue, blue, red]
        machine.swap(1, 3);
        String[] after = machine.configuration();

        assertTrue(machine.ok());
        assertEquals(before[0], after[2]);
        assertEquals(before[2], after[0]);
        assertEquals(before[1], after[1]); // la rueda no involucrada no cambia
    }

    /** swap QUÉ NO DEBERÍA HACER: fallar si hay menos de dos ruedas en la máquina. */
    @Test
    public void swapShouldFailWhenFewerThanTwoWheelsExist()
    {
        SlotMachine oneWheel = new SlotMachine();
        oneWheel.addWheel(1);
        oneWheel.addSymbol(1, "red");

        oneWheel.swap(1, 1);

        assertFalse(oneWheel.ok());
    }

    /** swap QUÉ NO DEBERÍA HACER: posiciones fuera de rango deben ajustarse, no lanzar excepción. */
    @Test
    public void swapShouldClampOutOfRangePositionsInsteadOfFailing()
    {
        machine.swap(-5, 999); // debe interpretarse como swap(1, 3)
        assertTrue(machine.ok());
    }

    // ---------- Requisito 10: lock / unlock ----------

    /** lock QUÉ DEBERÍA HACER: impedir que la rueda cambie al girar todas las ruedas. */
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

    /** lock QUÉ NO DEBERÍA HACER: permitir que spin(wheel, steps) mueva una rueda fijada. */
    @Test
    public void spinWithStepsShouldFailOnLockedWheel()
    {
        machine.lock(2);
        String beforeLock = machine.configuration()[1];

        machine.spin(2, 3);

        assertFalse(machine.ok());
        assertEquals(beforeLock, machine.configuration()[1]);
    }

    /** unlock QUÉ DEBERÍA HACER: permitir que la rueda vuelva a girar normalmente. */
    @Test
    public void unlockShouldAllowWheelToSpinAgain()
    {
        machine.lock(2);
        machine.unlock(2);

        machine.spin(2, 1);

        assertTrue(machine.ok());
    }

    /** lock/unlock QUÉ NO DEBERÍA HACER: afectar máquinas sin ruedas. */
    @Test
    public void lockShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine empty = new SlotMachine();
        empty.lock(1);
        assertFalse(empty.ok());
    }

    // ---------- Requisito 11: spin(wheel, steps) ----------

    /** spin(wheel,steps) QUÉ DEBERÍA HACER: rotar la rueda exactamente esos pasos (circular). */
    @Test
    public void spinWithStepsShouldRotateWheelByGivenSteps()
    {
        // Rueda 1: [red, blue, green], posición actual = blue (índice 1)
        machine.spin(1, 1); // avanza 1 paso -> green
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);

        machine.spin(1, 2); // desde green (índice 2), +2 -> índice (2+2)%3 = 1 -> blue
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /** spin(wheel,steps) QUÉ DEBERÍA HACER: aceptar pasos negativos (rotación inversa). */
    @Test
    public void spinWithNegativeStepsShouldRotateBackwards()
    {
        // desde blue (índice 1), -1 -> índice 0 -> red
        machine.spin(1, -1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /** spin(wheel,steps) QUÉ NO DEBERÍA HACER: fallar si la máquina no tiene ruedas. */
    @Test
    public void spinWithStepsShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine empty = new SlotMachine();
        empty.spin(1, 3);
        assertFalse(empty.ok());
    }

    // ---------- Requisito 12: spin(String[]) — configuración dada ----------

    /** spin(config) QUÉ DEBERÍA HACER: dejar cada rueda mostrando el símbolo indicado. */
    @Test
    public void spinWithConfigurationShouldSetEachWheelToGivenSymbol()
    {
        machine.spin(new String[]{"red", "blue", "green"});

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "blue", "green"}, machine.configuration());
    }

    /** spin(config) QUÉ DEBERÍA HACER: no modificar las ruedas fijadas. */
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

    /** spin(config) QUÉ NO DEBERÍA HACER: aceptar un arreglo de tamaño distinto al número de ruedas. */
    @Test
    public void spinWithConfigurationShouldFailWhenArrayLengthDoesNotMatchWheelCount()
    {
        String[] before = machine.configuration();

        machine.spin(new String[]{"red", "blue"}); // sólo 2, la máquina tiene 3 ruedas

        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration()); // no debe alterar nada
    }

    /** spin(config) QUÉ NO DEBERÍA HACER: fallar ante un arreglo nulo. */
    @Test
    public void spinWithConfigurationShouldFailWhenArrayIsNull()
    {
        machine.spin((String[]) null);
        assertFalse(machine.ok());
    }
}
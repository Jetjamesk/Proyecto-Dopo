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

        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "red");
    }

    /** swap Deberia intercambiar los símbolos visibles de dos ruedas. */
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

    /** swap No deberia hacer que fallar si hay menos de dos ruedas en la máquina. */
    @Test
    public void swapShouldFailWhenFewerThanTwoWheelsExist()
    {
        SlotMachine oneWheel = new SlotMachine();
        oneWheel.addWheel(1);
        oneWheel.addSymbol(1, "red");

        oneWheel.swap(1, 1);

        assertFalse(oneWheel.ok());
    }

    /** swap No deberia hacer que posiciones fuera de rango deben ajustarse, no lanzar excepción. */
    @Test
    public void swapShouldClampOutOfRangePositionsInsteadOfFailing()
    {
        machine.swap(-5, 999); 
        assertTrue(machine.ok());
    }


    /** lock Deberia hacer impedir que la rueda cambie al girar todas las ruedas. */
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

    /** lock No deberia permitir que spin(wheel, steps) mueva una rueda fijada. */
    @Test
    public void spinWithStepsShouldFailOnLockedWheel()
    {
        machine.lock(2);
        String beforeLock = machine.configuration()[1];

        machine.spin(2, 3);

        assertFalse(machine.ok());
        assertEquals(beforeLock, machine.configuration()[1]);
    }

    /** unlock Deberia permitir que la rueda vuelva a girar normalmente. */
    @Test
    public void unlockShouldAllowWheelToSpinAgain()
    {
        machine.lock(2);
        machine.unlock(2);

        machine.spin(2, 1);

        assertTrue(machine.ok());
    }

    /** lock/unlock No deberia afectar máquinas sin ruedas. */
    @Test
    public void lockShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine empty = new SlotMachine();
        empty.lock(1);
        assertFalse(empty.ok());
    }


    /** spin(wheel,steps) Deberia rotar la rueda exactamente esos pasos (circular). */
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

    /** spin(wheel,steps) Deberia aceptar pasos negativos (rotación inversa). */
    @Test
    public void spinWithNegativeStepsShouldRotateBackwards()
    {
        machine.spin(1, -1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /** spin(wheel,steps) No deberia fallar si la máquina no tiene ruedas. */
    @Test
    public void spinWithStepsShouldFailWhenMachineHasNoWheels()
    {
        SlotMachine empty = new SlotMachine();
        empty.spin(1, 3);
        assertFalse(empty.ok());
    }


    /** spin(config) debe dejar cada rueda mostrando el símbolo indicado. */
    @Test
    public void spinWithConfigurationShouldSetEachWheelToGivenSymbol()
    {
        machine.spin(new String[]{"red", "blue", "green"});

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "blue", "green"}, machine.configuration());
    }

    /** spin(config) Debe hacer que no deje modificar las ruedas fijadas. */
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

    /** spin(config) no deberia aceptar un arreglo de tamaño distinto al número de ruedas. */
    @Test
    public void spinWithConfigurationShouldFailWhenArrayLengthDoesNotMatchWheelCount()
    {
        String[] before = machine.configuration();

        machine.spin(new String[]{"red", "blue"}); 

        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration()); 
    }

    /** spin(config)no deberia fallar ante un arreglo nulo. */
    @Test
    public void spinWithConfigurationShouldFailWhenArrayIsNull()
    {
        machine.spin((String[]) null);
        assertFalse(machine.ok());
    }
}
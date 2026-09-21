import java.util.ArrayList;
/**
 * Write a description of class SlotMachineContest here.
 *
 * @author (Juan Andrés Rojas)
 * @version (Ciclo 3 )
 */
public class SlotMachineContest
{
    /** Maximo de acciones (giros) permitido por la maratón. */
    public static final int MAX_ACTIONS = 10000;
    /**
     * Mayor n que se simula: cabe en la ventana (800 x 600) y la animacion
     * dura un tiempo razonable.
     */
    public static final int MAX_SIMULATION_SIZE = 8;
    /**
     * Maquina usada en el ultimo solve. Al terminar solve queda en jackpot;
     * simulate la reutiliza para mostrar la solucion.
     */
    static SlotMachine machine;


    /**
     * Solution of the Slot Machine
     *
     * @param  n int
     * @return  int [][]
     */
    public static int[][] solve(int n)
    {
        machine = new SlotMachine(n);
        ArrayList<int[]> actions = new ArrayList<int[]>();
        if (n < 2){
            return new int[0][];
        }

        String target = machine.configuration()[0];
        for (int wheel = 2; wheel <= n; wheel++){
            int steps = 0;
            while (steps < n - 1 && !machine.configuration()[wheel - 1].equals(target)){
                machine.spin(wheel, 1);
                steps++;
            }
            if (steps > 0){
                actions.add(new int[]{wheel, steps});
            }
        }

        return actions.toArray(new int[0][]);
    }
    
    
    public static void simulate(int n)
    {
        if (n < 1 || n > MAX_SIMULATION_SIZE){
            return;
        }
        int[][] actions = solve(n);
        for (int k = actions.length - 1; k >= 0; k--){
            machine.spin(actions[k][0], n - actions[k][1]);
        }
        machine.makeVisible();
        int k = 0;
        while (k < actions.length){
            int wheel = actions[k][0];
            int total = 0;
            while (k < actions.length && actions[k][0] == wheel){
                total += actions[k][1];
                k++;
            }
            total = total % n;
            if (total > n / 2){
                total = total - n;
            }
            if (total != 0){
                machine.spin(wheel, total);
            }
        }
    }
}
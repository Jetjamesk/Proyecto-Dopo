import java.util.ArrayList;
/**
 * Write a description of class SlotMachineContest here.
 *
 * @author (Juan Andrés Rojas)
 * @version (Ciclo 3 )
 */
public class SlotMachineContest extends SlotMachine{
    
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
        for (int i = 0; i < n - 1; i++){
            int steps = 0;
            while (steps < n - 1 && !machine.configuration()[i + 1].equals(target)){
                machine.spin(i + 2, 1);
                steps++;
            }
            if (steps > 0){
                actions.add(new int[]{i + 2, steps});
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
    for (int i = 0; i < actions.length; i++){
        int k = actions.length - 1 - i;   // recorre de atrás hacia adelante
        machine.spin(actions[k][0], n - actions[k][1]);
    }
    machine.makeVisible();

    int total = 0;
    for (int i = 0; i < actions.length; i++){
        int wheel = actions[i][0];
        total += actions[i][1];
        boolean ultimaDeLaRueda = (i == actions.length - 1) || actions[i + 1][0] != wheel;
        if (ultimaDeLaRueda){
            total = total % n;
            if (total > n / 2){
                total = total - n;
            }
            if (total != 0){
                machine.spin(wheel, total);
            }
            total = 0; 
        }
    }
}
}
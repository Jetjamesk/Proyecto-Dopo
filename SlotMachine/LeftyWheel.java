/**
 * Lefty wheel: if there is a wheel on its left, when it spins it copies the state
 * (the symbol that is shown) of that wheel. If there is no wheel on the left, or the
 * left symbol does not exist in this wheel, it spins like a normal wheel.
 *
 * @author (Juan Andrés Rojas)
 * @version (Ciclo 4)
 */
public class LeftyWheel extends Wheel
{
    public LeftyWheel()
    {
        super("#00BFFF");
    }


    /** A lefty wheel jumps to the copied symbol, so there is no step by step animation. */
    @Override
    public boolean animatesSteps()
    {
        return false;
    }

    /**
     * Tries to copy the symbol shown by the left wheel.
     *
     * @return true if the state was copied
     */
    private boolean copyLeft()
    {
        if (left == null || size() == 0) {
            return false;
        }
        int index = indexOf(left.currentSymbol());
        if (index == -1) {
            return false;
        }
        rotate(index - currentIndex());
        afterSpin();
        return true;
    }

    @Override
    public void spin(int steps)
    {
        if (copyLeft()){
           return;
        }
        super.spin(steps);
    }
}
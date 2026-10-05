/**
 * Rebel wheel: it can not be locked, swapped or deleted.
 *
 * @author (Juan Andrés Rojas)
 * @version (Ciclo 4)
 */
public class RebelWheel extends Wheel
{
    public RebelWheel()
    {
        super("#8B0000");
    }


    @Override
    public boolean canBeLocked()
    {
        return false;
    }

    @Override
    public boolean canBeSwapped()
    {
        return false;
    }

    @Override
    public boolean canBeDeleted()
    {
        return false;
    }

    /** A rebel wheel never stays locked. */
    @Override
    public void lock()
    {
    }
}
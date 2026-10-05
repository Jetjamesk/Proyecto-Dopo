
/**
 * Write a description of class EphemeralSymbol here.
 *
 * @author (Juan Andrés Rojas)
 * @version (ciclo 4)
 */
public class EphemeralSymbol extends Symbol
{
    public static final int STEP = 5;
    public static final int MIN_SIZE = 1;

    /**
     * Constructor for objects of class EphemeralSymbol
     */
    public EphemeralSymbol(String color)
    {
        super(color);
    }

    @Override
    public String getType(){
        return "ephemeral";
    }
    
    @Override
    public void onSpin(){
        for (int i = 0; i <STEP && size > MIN_SIZE;i++){
            size--;
        }
        applySize();
    }
}
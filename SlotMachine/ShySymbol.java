/**
 * Shy symbol: each time it is selected in its wheel it toggles between visible and invisible.
 *
 * @author (Juan Andrés Rojas)
 * @version (Ciclo 4)
 */
public class ShySymbol extends Symbol
{
    private boolean hidden;

    public ShySymbol(String color)
    {
        super(color);
        hidden = false;
    }

    @Override
    public String getType()
    {
        return "shy";
    }
    
    
    @Override
    public boolean isHidden()
    {
        return hidden;
    }


    
    @Override
    protected void draw()
    {
        if (visible && !hidden) {
            shape.makeVisible();
        } 
        else{
            shape.makeInvisible();
        }
    }

    
    @Override
    public void onSelected()
    {
        hidden = !hidden;
        draw();
    }
}
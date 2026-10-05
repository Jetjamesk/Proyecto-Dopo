/**
 * Wild symbol it matches any color when the machine
 * checks the jackpot. It is drawn as a circle with a small black square in the middle.
 *
 * @author (Juan Andrés Rojas)
 * @version (Ciclo 4)
 */
public class WildSymbol extends Symbol
{
    private static final int MARK_SIZE = 10;
    private Rectangle object;

    public WildSymbol(String color)
    {
        super(color);
        object = new Rectangle();
        object.changeColor("black");
        object.changeSize(MARK_SIZE, MARK_SIZE);
    }

    @Override
    public String getType()
    {
        return "wild";
    }

    @Override
    public boolean isWild()
    {
        return true;
    }

    @Override
    protected void draw()
    {
        super.draw();
        if (visible){
            object.makeVisible();
        }
        else{
            object.makeInvisible();
        }
    }

    @Override
    protected void applySize()
    {
        super.applySize();
        if (visible){
            object.makeVisible();
        }
    }

    @Override
    public void setPosition(int x, int y)
    {
        super.setPosition(x, y);
        object.setPosition(x + (NORMAL_SIZE - MARK_SIZE) / 2, y + (NORMAL_SIZE - MARK_SIZE) / 2);
    }
}
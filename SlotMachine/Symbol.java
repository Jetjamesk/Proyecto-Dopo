
/**
 * Write a description of class Symbol here.
 *
 * @author (Juan Andrés Rojas)
 * @version (06/09/2026)
 */
public class Symbol
{ 
    private String color;
    private Circle shape;

    
    /**
     * Constructor for objects of class Symbol
     * Create a symbol
     */
    public Symbol(String color)
    {
        this.color=color;
        shape = new Circle();
        shape.changeColor(color);
    }
        
    
    /**
     * Returns the color of the symbol.
     *
     * @return the color as a String
     */
    public String getColor()
    {
        return color;
    }
    
    
    /**
     * Makes the symbol visible.
     */
    public void makeVisible()
    {
        shape.makeVisible();
    }
    
    
    /**
     * Makes the symbol invisible.
     */
    public void makeInvisible()
    {
        shape.makeInvisible();
    }
    
    
    /**
     * Highlights or unhighlights the symbol by changing the size of its shape.
     *
     * @param on true to enlarge the shape (45), false to return it to its normal size (30)
     */
    public void setHighlighted(boolean on)
    {
        shape.changeSize(on ? 45 : 30);
    }
    
    
    /**
     * Moves the symbol to a new position.
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     */
    public void setPosition(int x, int y){
        shape.setPosition(x, y);
    }
}
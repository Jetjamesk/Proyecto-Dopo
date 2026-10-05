
/**
 * Write a description of class Symbol here.
 *
 * @author (Juan Andrés Rojas)
 * @version (06/09/2026)
 */
public class Symbol
{ 
    public static final int NORMAL_SIZE = 30;
    public static final int HIGHLIGHT_EXTRA = 15;
    
    private String color;
    protected Circle shape;
    protected int size;
    protected boolean visible;
    protected boolean highlighted;

    
    /**
     * Constructor for objects of class Symbol
     * Create a symbol
     */
    public Symbol(String color)
    {
        this.color=color;
        shape = new Circle();
        shape.changeColor(color);
        size = NORMAL_SIZE;
        visible = false;
        highlighted = false;
    }
    
    /**
     * Create a symbol of the given types
     * 
     * @param type "Normal", "Ephemeral" , "shy" or "wild"
     * @param color The color of the symbol
     * @return Symbol
     */
    public static Symbol create(String type, String color){
        if (type == null){
            return null;
        }
        else{
            switch (type.toLowerCase()){
                case "normal": return new Symbol(color);
                case "ephemeral": return new EphemeralSymbol(color);
                case "shy": return new ShySymbol(color);
                case "wild": return new WildSymbol(color);
                default: return null;
            }
        }
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
        if (on){
            shape.changeSize(45);
        }
        else{
            shape.changeSize(30);
        }
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
    
    
    /**
     * The type of the symbol
     * 
     * @return The type of teh symbol
     */
    public String getType(){
        return "normal";
    }

    
    /**
     * Get the size
     * 
     * @return int the size of the symbol
     */
    public int getSize(){
        return size;
    }
    
    
    
    /**
     * if the symbol hides itself (only shy symbols can be hidden)
     * 
     * @return boolean True or false
     */
    public boolean isHidden()
    {
    return false;
    }
    
    
    /**
     * Show if the symbol is not visible
     * 
     * @return true if the symbol matches any color in a jackpot
     */
    public boolean isWild(){
        return false;
    }
    
    
    /**
     * Draw or erase the symbols
     */
    protected void draw(){
        if (visible){
            shape.makeVisible();
        }
        else{
            shape.makeInvisible();
        }
    }
    
    
    /**
     * 
     */
    protected void applySize(){
        if(highlighted){
            shape.changeSize(size + HIGHLIGHT_EXTRA);
        }
        else{
            shape.changeSize(size);
        }
    }
    
    
    /**
     * Called each time the wheel that contains the symbol spins 
     * Normal symbols do nothing
     */
    public void onSpin(){
        
    }
    
    
    /**
     * Called each time the symbol is selected in its wheel.
     */
    public void onSelected(){
        
    }
}
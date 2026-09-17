
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
     * Constructor - Crea un símbolo con color y figura por defecto (círculo)
     * @param color El color del símbolo
     */
    public Symbol(String color)
    {
        this.color=color;
        shape = new Circle();
        shape.changeColor(color);
    }
        
    /**
    * Devuelve el color del símbolo
    * @return El color como String
    */
    public String getColor()
    {
        return color;
    }
    
    /**
     * Hace visible la figura geométrica
     */
    public void makeVisible()
    {
        shape.makeVisible();
    }
    
    /**
     * Hace invisible la figura geométrica
     */
    public void makeInvisible()
    {
        shape.makeInvisible();
    }
    
    
    public void setHighlighted(boolean on)
    {
        shape.changeSize(on ? 45 : 30);
    }
    
    /**
     * Mueve la figura del simbolo a una nueva posicion
     * @param x Coordenada horizontal
     * @param y cordenada vertical
     */
    public void setPosition(int x , int y){
        shape.setPosition(x,y);
    }
}
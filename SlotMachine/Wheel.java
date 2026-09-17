import java.util.ArrayList;
import java.util.Random;

public class Wheel
{
    private ArrayList<Symbol> symbols;
    private int currentPosition;   
    private boolean visible;
    private boolean locked;
    private Rectangle housing;
    private Triangle pointer;

    private static final int HOUSING_WIDTH = 40;       // ancho FIJO de cada rueda
    private static final int ROW_HEIGHT = 35;           // alto que ocupa cada símbolo apilado
    private static final int MIN_HOUSING_HEIGHT = 40;
    private static final int HOUSING_Y = 40;

    public Wheel()
    {
        symbols = new ArrayList<Symbol>();
        currentPosition = -1;
        visible = false;
        locked = false;
        housing = new Rectangle();
        housing.changeColor("gray");
        housing.changeSize(MIN_HOUSING_HEIGHT, HOUSING_WIDTH);
        pointer = new Triangle();
        pointer.changeColor("black");
        pointer.changeSize(10, 10);
    }

    public void addSymbol(int pos, String color)
    {
        pos = posicitionArrayList(pos, symbols.size() + 1);
        int idx = pos - 1;
        symbols.add(idx, new Symbol(color));
        if (currentPosition == -1){
            currentPosition = 0;
        } else if (idx <= currentPosition){
            currentPosition++;
        }
    }

    public void delSymbol(String symbol)
    {
        int idx = -1;
        for (int i = 0; i < symbols.size(); i++){
            if (symbols.get(i).getColor().equals(symbol)){
                idx = i;
                break;
            }
        }
        if (idx == -1) return;
        Symbol removed = symbols.remove(idx);
        removed.makeInvisible();
        if (symbols.isEmpty()){
            currentPosition = -1;
        } else if (idx < currentPosition){
            currentPosition--;
        } else if (idx == currentPosition && currentPosition >= symbols.size()){
            currentPosition = symbols.size() - 1;
        }
    }

    public void placeSymbol(String symbol)
    {
        for (int i = 0; i < symbols.size(); i++){
            if (symbols.get(i).getColor().equals(symbol)){
                currentPosition = i;
                return;
            }
        }
    }

    public void spin()
    {
        if (symbols.isEmpty()) return;
        currentPosition = (currentPosition + 1) % symbols.size();
    }

    public boolean spin(Random rng)
    {
        if (symbols.isEmpty()) return false;
        currentPosition = rng.nextInt(symbols.size());
        return true;
    }

    public void spin(int steps)
    {
        if (symbols.isEmpty()) return;
        int size = symbols.size();
        currentPosition = ((currentPosition + steps) % size + size) % size;
    }

    public String[] allSymbols()
    {
        String[] result = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++){
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }

    public String currentSymbol()
    {
        if (currentPosition == -1) return "";
        return symbols.get(currentPosition).getColor();
    }

    public int size(){
        return symbols.size();
    }

    public void makeVisible()
    {
        visible = true;
        housing.makeVisible();
        for (Symbol s : symbols){
            s.makeVisible();
        }
        if (!symbols.isEmpty()){
            pointer.makeVisible();
        }
    }

    public void makeInvisible()
    {
        visible = false;
        housing.makeInvisible();
        for (Symbol s : symbols){
            s.makeInvisible();
        }
        pointer.makeInvisible();
    }

    private int posicitionArrayList(int pos, int max)
    {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    public void reposition(int x){
        int height = Math.max(MIN_HOUSING_HEIGHT, symbols.size() * ROW_HEIGHT);
        housing.changeSize(height, HOUSING_WIDTH);
        housing.setPosition(x, HOUSING_Y);
        for (int i = 0; i < symbols.size(); i++){
            int symbolY = HOUSING_Y + i * ROW_HEIGHT + 5;
            symbols.get(i).setPosition(x + 5, symbolY);
        }
        if (currentPosition != -1){
            int pointerY = HOUSING_Y + currentPosition * ROW_HEIGHT + 10;
            pointer.setPosition(x - 15, pointerY);
        }
    }

    public void setHighlighted(boolean on){
        housing.changeColor(on ? "gold" : "gray");
    }

    public void lock(){
        locked = true;
    }

    public void unlock(){
        locked = false;
    }

    public boolean isLocked()
    {
        return locked;
    }
}
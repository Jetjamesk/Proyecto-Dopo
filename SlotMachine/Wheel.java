import java.util.ArrayList;
import java.util.Random;
/**
 * Write a description of class Wheel here.
 *
 * @author (Juan Andrés Rojas)
 * @version (06/09/2026)
 */
public class Wheel
{
    private ArrayList<Symbol> symbols;
    private int currentPosition;   
    private boolean visible;
    private boolean locked;
    private Rectangle housing;
    private Triangle pointer;

    private static final int HOUSING_WIDTH = 40;       
    private static final int ROW_HEIGHT = 35;           
    private static final int MIN_HOUSING_HEIGHT = 40;
    private static final int HOUSING_Y = 40;

    /**
     * Constructor for objects of class SlotMachine
     * Create a empty wheel 
     */
    public Wheel(){
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

    
    /**
     * 
     * Add a symbol in the wheel with a color
     *
     * @param  pos, color The position that we will add the symbol and the color that we want
     * 
     */
    public void addSymbol(int pos, String color){
        pos = posicitionArrayList(pos, symbols.size() + 1);
        int idx = pos - 1;
        symbols.add(idx, new Symbol(color));
        if (currentPosition == -1){
            currentPosition = 0;
        } else if (idx <= currentPosition){
            currentPosition++;
        }
    }

    
    /**
     * Search and find the symbol that we want to delete in all the wheels
     * 
     * @param  symbol The symbol that we want to delete
     * 
     */
    public void delSymbol(String symbol){
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

    
    /**
     * Show a specific wheel with a specific symbol color  
     *
     * @param wheel, the wheel that we want and the symbol that we search 
     * 
     */
    public void placeSymbol(String symbol){
        for (int i = 0; i < symbols.size(); i++){
            if (symbols.get(i).getColor().equals(symbol)){
                currentPosition = i;
                return;
            }
        }
    }

    
    /**
     * Spin an specific wheel in a random position
     *
     */
    public void spin(){
        if (symbols.isEmpty()) return;
        currentPosition = (currentPosition + 1) % symbols.size();
    }

    
     /**
     * Spin all the wheels at the same time in a random position
     */
    public boolean spin(Random rng){
        if (symbols.isEmpty()) return false;
        currentPosition = rng.nextInt(symbols.size());
        return true;
    }

    /**
     * Rotates a specific wheel a specified number of steps. If the machine is
     * visible, the movement is displayed step by step 
     * 
     * @param steps number of steps to turn
     */
    public void spin(int steps){
        if (symbols.isEmpty()) return;
        int size = symbols.size();
        currentPosition = ((currentPosition + steps) % size + size) % size;
    }

    
    /**
     * Return a list with all the colors of the wheel in orden 
     *
     * @return String Return a list with the colors  
     */
    public String[] allSymbols(){
        String[] result = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++){
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }

    
    /**
     * Returns the color of the symbol currently shown on the wheel.
     *
     * @return the color of the current symbol, or an empty string if the wheel has no symbols
     */
    public String currentSymbol(){
        if (currentPosition == -1) return "";
        return symbols.get(currentPosition).getColor();
    }

    
    /**
     * Returns the number of symbols on the wheel.
     *
     * @return the number of symbols
     */
    public int size(){
        return symbols.size();
    }

    
    /**
     * Show visually the machine with the wheels
     * 
     * @return void
     */
    public void makeVisible(){
        visible = true;
        housing.makeVisible();
        for (int i = 0; i < symbols.size(); i++){
            symbols.get(i).makeVisible();
        }
        if (!symbols.isEmpty()){
            pointer.makeVisible();
        }
    }


    /**
     * Hide the machine and the wheels without stopping the machine
     * 
     * @return void
     */
    public void makeInvisible(){
        visible = false;
        housing.makeInvisible();
        for (int i = 0; i < symbols.size(); i++){
            symbols.get(i).makeInvisible();
        }
        pointer.makeInvisible();
    }

    
    /**
     * When we add a wheel to the machine we need to verfy the position
     *
     * @param  pos and max 
     * @return int  
     */
    private int posicitionArrayList(int pos, int max){
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    
    /**
     * Moves the wheel to a new horizontal position and updates its layout.
     * The housing is resized to fit the symbols, the symbols are placed one below
     * the other, and the pointer is placed next to the current symbol.
     *
     * @param x the new horizontal position of the wheel
     */
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

    
    /**
     * Highlights or unhighlights the wheel by changing the color of its housing.
     *
     * @param on true to paint the housing gold, false to paint it gray
     */
    public void setHighlighted(boolean on){
        housing.changeColor(on ? "gold" : "gray");
    }

    
    /**
     * Lock the wheel so it can´t turn the wheel 
     *
     * @param wheel Position of the wheel that we want to lock
     * @return void
     */
    public void lock(){
        locked = true;
    }

    
     /**
     * Unlock the wheel that we had locked
     *
     * @param wheel Position of the wheel that we want to unlock
     * @return void
     */
    public void unlock(){
        locked = false;
    }

    /**
     *Show if lock or not. If lock it will be true
     *
     *@return Boolean True or false 
     */
    public boolean isLocked(){
        return locked;
    }
}
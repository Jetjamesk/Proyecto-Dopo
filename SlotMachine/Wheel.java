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
    protected Wheel left;
    private String baseColor;

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
     * Create an empty wheel whose housing has the given color (used by the subclasses
     * so that the wheel types can be told apart visually).
     *
     * @param baseColor the color of the housing
     */
    protected Wheel(String baseColor){
        this.baseColor = baseColor;
        left = null;
        symbols = new ArrayList<Symbol>();
        currentPosition = -1;
        visible = false;
        locked = false;
        housing = new Rectangle();
        housing.changeColor(baseColor);
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
     * Add a symbol of a given type in the wheel
     *
     * @param type "normal", "ephemeral", "shy" or "wild"
     * @param pos the position where the symbol is added
     * @param color the color of the symbol
     * @return true if the type exists and the symbol was added
     */
    public boolean addSymbol(String type, int pos, String color){
        Symbol symbol = Symbol.create(type, color);
        if (symbol == null) return false;
        pos = posicitionArrayList(pos, symbols.size() + 1);
        int idx = pos - 1;
        symbols.add(idx, symbol);
        if (currentPosition == -1){
            currentPosition = 0;
        } else if (idx <= currentPosition){
            currentPosition++;
        }
        return true;
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
        int index = indexOf(symbol);
        if (index != -1){
            currentPosition = index;
            symbols.get(index).onSelected();
        }
    }
    
    
    /**
     * Index of the first symbol with the given color
     *
     * @param color the color to look for
     * @return the index, or -1 if the wheel does not have that color
     */
    protected int indexOf(String color){
        for (int i = 0; i < symbols.size(); i++){
            if (symbols.get(i).getColor().equals(color)){
                return i;
            }
        }
        return -1;
    }

    
    /**
     * Spin an specific wheel in a random position
     *
     */
        public void spin(){
        spin(1);
    }

    
    /**
     * Spin all the wheels at the same time in a random position
     */
    public boolean spin(Random rng){
        if (symbols.isEmpty()) {
            return false;
        }
        
        spin(rng.nextInt(symbols.size()));
        return true;
    }

    
     /**
     * Rotates a specific wheel a specified number of steps. If the machine is
     * visible, the movement is displayed step by step 
     * 
     * @param steps number of steps to turn
     */
    public void spin(int steps){
        if (symbols.isEmpty()) {
            return;
        }
        
        rotate(steps);
        afterSpin();
    }

    
    /**
     * Moves the wheel some steps without counting it as a spin (used to animate a spin
     * step by step). Symbols are not notified.
     *
     * @param steps number of steps (negative = backwards)
     */
    public void rotate(int steps){
        if (symbols.isEmpty()) {
            return;
        }
        
        int size = symbols.size();
        currentPosition = ((currentPosition + steps) % size + size) % size;
    }

    
    /**
     * Notifies the symbols that the wheel has spun: every symbol gets onSpin() and the
     * symbol that stays in front of the pointer gets onSelected().
     */
    public void afterSpin(){
        if (symbols.isEmpty()){
            return;
        }
        for (Symbol symbol : symbols){
            symbol.onSpin();
        }
        symbols.get(currentPosition).onSelected();
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
        if (currentPosition == -1) {
            return "";
        }
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
        if(on){
            housing.changeColor("gold");
        }
        else{
            housing.changeColor("gray");
        }
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
    
    
    /**
     * the index (0..size-1) of the symbol in front of the pointer, or -1 if empty 
     * 
     * @return int 
     */
    protected int currentIndex(){
        return currentPosition;
    }
    
    
    /**
     * Factory: creates a wheel of the given type.
     *
     * @param type "normal", "lefty" or "rebel"
     * @return the new wheel, or null if the type does not exist
     */
    public static Wheel create(String type){
        if (type == null){
            return null;
        }
        switch (type.toLowerCase()){
            case "normal": return new Wheel();
            case "lefty":  return new LeftyWheel();
            case "rebel":  return new RebelWheel();
            default:       return null;
        }
    }
    
    
    
    /** 
     * Sets the wheel that is on the left of this one 
     * 
     */
    public void setLeft(Wheel left){
        this.left = left;
    }
    
    
    /**
     * if a spin is animated step by step when the machine is visible
     * 
     * @return boolena True or false
     */
    public boolean animatesSteps(){
        return true;
    }
    
    
    /** 
     * if the wheel accepts being locked
     * 
     * @return boolean True or false 
     */
    public boolean canBeLocked(){
        return true;
    }
    
    
    /** 
     * if the wheel accepts being swapped
     * 
     * @teurn boolean True or false 
     */
    public boolean canBeSwapped(){
        return true;
    }
    
    
    /** 
     * if the wheel accepts being deleted
     * 
     * @return boolean True or false
     */
    public boolean canBeDeleted(){
        return true;
    }
    
    
    /** 
     * if the symbol in front of the pointer is wild 
     * 
     * @return boolean True or false
     */
    public boolean isCurrentWild(){
        return currentPosition != -1 && symbols.get(currentPosition).isWild();
    }
    
    
    /**
     * Returns the symbol at a position (1..size), mainly for tests.
     *
     * @param pos the position, starting in 1
     * @return the symbol, or null if the position is not valid
     */
    public Symbol symbolAt(int pos){
        if (pos < 1 || pos > symbols.size()) {
            return null;
        }
        return symbols.get(pos - 1);
    }
}
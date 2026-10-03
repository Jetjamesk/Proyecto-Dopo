import java.util.ArrayList;
import java.util.Random;
import java.awt.Color; // Use I.A como recomendacion para la solucion del problema 
import java.util.Collections; // Use I.A como recomendacion para la solucion del problema 

/**
 * Write a description of class SlotMachine here.
 *
 * @author (Juan Andrés Rojas)
 * @version (06/09/2026)
 */
public class SlotMachine
{
    private ArrayList<Wheel>wheels;
    private boolean visible;
    private boolean ok ;
    private Wheel activeWheel;
    private Random random;
    
    private static final int MARGIN_LEFT = 40;
    private static final int WHEEL_SPACING = 60;
    private static final int STEP_DELAY_MS = 150;
    

    /**
     * Constructor for objects of class SlotMachine
     * Create a empty SlotMachine 
     */
    public SlotMachine(){
        wheels = new ArrayList<Wheel>();
        visible = false;
        ok = false;
        activeWheel = null;
        random = new Random();
    }
    
    /**
     * Crear the slot machine with an N wheels and symbols 
     * @param n numebres of wheels and symbols of the slot machine
     */
        public SlotMachine(int n){
        //Use I.A como recomendacion para la solucion del problema 
        this();
        if (n <= 0){
            ok = false;
            return;
        }
        ArrayList<String> colors = new ArrayList<String>();
        String[] palette = {"red", "blue", "yellow", "green", "magenta", "white", "black","gray"};
        for (int i = 0; i < palette.length && colors.size() < n; i++){
            colors.add(palette[i]);
        }
        float firstHue = random.nextFloat();
        for (int i = 0; colors.size() < n; i++){
            int rgb = Color.HSBtoRGB((firstHue + (float) i / n) % 1.0f,
                                     0.6f + 0.4f * random.nextFloat(),
                                     0.7f + 0.3f * random.nextFloat());
            String color = String.format("#%06X", rgb & 0xFFFFFF);
            if (!colors.contains(color)){
                colors.add(color);
            }
        }
        Collections.shuffle(colors, random);
        for (int i = 0; i < n; i++){
            Wheel wheel = new Wheel();
            for (int j = 0; j < n; j++){
                wheel.addSymbol(wheel.size() + 1, colors.get(j));
            }
            wheel.spin(random.nextInt(n));
            wheels.add(wheel);
        }
        activeWheel = wheels.get(n - 1);
        ok = true;
        if (n > 1 && isJackpot()){
            wheels.get(0).spin(1);
        }
    }

    
    /**
     * Add a wheel to the machine  
     *
     * @param pos 
     * @return void
     */
    public void addWheel(int pos){
     pos = posicitionArrayList(pos, wheels.size()+1);
     Wheel newWheel = new Wheel();
     wheels.add(pos-1, newWheel);
     activeWheel = newWheel; 
     ok = true;
     redraw();
    }
    
    /**
     * When we add a wheel to the machine we need to verfy the position
     *
     * @param  pos and max 
     * @return int  
     */
    private int posicitionArrayList (int pos, int max)
    {
        if (pos<1) return 1;
        else if (pos> max) return max;
        else return pos;
    }
    
    /**
     * Delete a wheel of the machine however we need to verfy the size of the wheels. If is 0 we can´t
     * delete. But if is greater tahn 0 we can delete one wheel in that position
     *
     * @param  Position 
     * @return Void
     */
    public void delWheel(int pos)
    {
        if (wheels.size()==0){
            ok = false;
            return; 
        }
            pos = posicitionArrayList(pos, wheels.size());
            Wheel remove = wheels.remove(pos-1);
            remove.makeInvisible();
            if (remove == activeWheel){
                if (wheels.isEmpty()){
                    activeWheel = null;
                }
                else{
                    activeWheel = wheels.get(wheels.size()-1);
                }
            }
        ok = true; 
        redraw();
    }
    
    /**
     * 
     * Add a symbol in the wheel with a color
     *
     * @param  pos, color The position that we will add the symbol and the color that we want
     * 
     */
    public void addSymbol(int pos, String color){
        if (activeWheel == null){
            ok = false;
            return;
        }
        activeWheel.addSymbol(pos, color);
        ok = true;
        redraw();
    }

    /**
     * Search and find the symbol that we want to delete in all the wheels
     * 
     * @param  symbol The symbol that we want to delete
     * 
     */
    public void delSymbol(String symbol){
        if (activeWheel == null){
            ok = false;
            return;
        }
        activeWheel.delSymbol(symbol);
        ok = true;
        redraw();
    }

    /**
     * Show a specific wheel with a specific symbol color  
     *
     * @param wheel, the wheel that we want and the symbol that we search 
     * 
     */
    public void placeSymbol(int wheel, String symbol)
    {
        if (wheels.size() == 0)
        {
            ok = false;
            return;
        }
        int index = posicitionArrayList(wheel, wheels.size());
        wheels.get(index - 1).placeSymbol(symbol);
        ok = true;
        redraw();
    }
    
    /**
     * Spin an specific wheel in a random position
     *
     * @param wheel The wheel that we want to spin
     * @return void
     */
    public void spin(int wheel)
    {
        if (wheels.size() == 0)
        {
            ok = false;
            return;
        }
        int index = posicitionArrayList(wheel, wheels.size());
        Wheel target = wheels.get(index-1);
        if (target.isLocked()){
            ok = false;
            return;
        }
        target.spin(random);
        ok = true;
        redraw();
    }

    /**
     * Spin all the wheels at the same time in a random position
     * the wheels that are locked we ignore
     * 
     *
     * @return void
     */
    public void spin(){
        if(wheels.size() == 0){
            ok = false;
            return;
        }
        for (Wheel wheel : wheels){
            if (wheel.isLocked()){
                continue;
            }
            int size = wheel.size();
            if (size > 0){
                int steps = random.nextInt(size);
                wheel.spin(steps);
            }
            
        }
        ok = true;
        redraw();
    }
    
    /**
     * Return a list with all the colors of the wheel in orden 
     *
     * @return String Return a list with the colors  
     */
    public String[] symbols(){
        if (wheels.size() == 0)
        {
            return new String[0];
        }
        return wheels.get(0).allSymbols();
    }
    
    /**
     * Cuenta cuántos colores únicos y diferentes existen en total en toda la máquina
     * Count how many colors are in the all machine
     * 
     * @return int The number of the colors
     */
    public int distinctSymbols(){
        if (activeWheel == null){
            ok = false;
            return 0;
        }
        ArrayList<String> distinct = new ArrayList<String>();
        for (Wheel wheel : wheels)
        {
            for (String color : wheel.allSymbols())
            {
                if (!distinct.contains(color))
                {
                    distinct.add(color);
                }
            }
        }
        return distinct.size();
    }
    
     /**
     * Devuelve un arreglo con los colores visibles actuales de cada rueda de izquierda a derecha
     * Return a arrangement with the all current colors visibles in the wheels from left to right
     * 
     * @return String The name of the funtion of the visual state
     */
    public String[] configuration(){
        String[] visibleColors = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++)
        {
            visibleColors[i] = wheels.get(i).currentSymbol();
        }
        return visibleColors;
    }
    
    /**
     * Verify if the player won. You won if the all wheels shows the same symbol with the same color 
     * 
     * @return Boolean True or false 
     */
    public boolean isJackpot(){   
        if (wheels.size() == 0){
            return false;
        }
        
        String[] visibleColors = configuration();
        if (visibleColors.length == 0 || visibleColors[0].isEmpty())
        {
            return false;
        }
        for (int i=0; i< visibleColors.length;i++)
        {
            String color = visibleColors[i];
            if (color == null || !color.equals(visibleColors[0])){
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Show visually the machine with the wheels
     * 
     * @return void
     */
    public void makeVisible(){
        visible = true;
        redraw();
    }
    
    
    /**
     * Hide the machine and the wheels without stopping the machine
     * 
     * @return void
     */
    public void makeInvisible(){
        visible = false;
        redraw();
    }
    
    
    /**
     * Close the game and clear all the canvas 
     * 
     * @return void  
     */
    public void exit(){
        visible = false;
        redraw();
    }
    
    
    /**
     * Indicate if the last action was done correctly.
     *
     * @return true or false
     */
    public boolean ok(){
        return ok;
    }

    
    /**
     * Swap the position of two wheels in the machine 
     *
     * @param wheel1 position of the first wheel
     * @param wheel2 position of the second wheel
     * @return void
     */
    public void swap(int wheel1, int wheel2){
        if (wheels.size() < 2){
            ok = false;
            return;
        }
        int index1 = posicitionArrayList(wheel1, wheels.size()) - 1;
        int index2 = posicitionArrayList(wheel2, wheels.size()) - 1;
        Wheel temp = wheels.set(index1, wheels.get(index2));
        wheels.set(index2, temp);
        ok = true;
        redraw();
    }

    
    /**
     * Lock the wheel so it can´t turn the wheel 
     *
     * @param wheel Position of the wheel that we want to lock
     * @return void
     */
    public void lock(int wheel){
        if (wheels.size() == 0){
            ok = false;
            return;
        }
        int index = posicitionArrayList(wheel, wheels.size());
        wheels.get(index - 1).lock();
        ok = true;
        redraw();
    }

    
    /**
     * Unlock the wheel that we had locked
     *
     * @param wheel Position of the wheel that we want to unlock
     * @return void
     */
    public void unlock(int wheel){
        if (wheels.size() == 0){
            ok = false;
            return;
        }
        int index = posicitionArrayList(wheel, wheels.size());
        wheels.get(index - 1).unlock();
        ok = true;
        redraw();
    }

    
    /**
     * Sets the machine to a specific configuration: each position in the array indicates
     * the symbol that should be visible on the corresponding wheel.
     * Locked wheels are not changed.
     * 
     *
     * @param setSymbols Arrange the desired symbol for each wheel, from left to right
     * @return void
     */
    public void spin(String[] setSymbols){
        if (setSymbols == null || setSymbols.length != wheels.size()){
            ok = false;
            return;
        }
        for (int i = 0; i < wheels.size(); i++){
            Wheel wheel = wheels.get(i);
            if (!wheel.isLocked()){
                wheel.placeSymbol(setSymbols[i]);
            }
        }
        ok = true;
        redraw();
    }

    
    /**
     * Rotates a specific wheel a specified number of steps. If the machine is
     * visible, the movement is displayed step by step 
     * A locked wheel does not rotate.
     *
     * @param wheel position of the wheel that we want to turn 
     * @param steps number of steps to turn
     * @return void
     */
    public void spin(int wheel, int steps){
        if (wheels.size() == 0){
            ok = false;
            return;
        }
        int index = posicitionArrayList(wheel, wheels.size()) - 1;
        Wheel target = wheels.get(index);
        if (target.isLocked()){
            ok = false;
            return;
        }
        if (visible){
            int direction = steps < 0 ? -1 : 1;
            int totalSteps = Math.abs(steps);
            for (int i = 0; i < totalSteps; i++){
                target.spin(direction);
                redraw();
            }
        } else {
            target.spin(steps);
        }
        ok = true;
        redraw();
    }

    
    private void redraw(){
    if (!visible)
    {
        for (Wheel wheel : wheels)
        {
            wheel.makeInvisible();
        }
        return;
    }
    boolean jackpot = isJackpot();
    int x = MARGIN_LEFT;
    for (Wheel wheel : wheels){
        wheel.setHighlighted(jackpot);
        wheel.reposition(x);
        wheel.makeVisible();
        x += WHEEL_SPACING;
        }
    }
}
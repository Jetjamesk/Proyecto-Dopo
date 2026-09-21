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
     * Crear the slot machine with an N wheels and sysmbols 
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
        for (int i = 0; i < n; i++){
            Wheel wheel = new Wheel();
            Collections.shuffle(colors, random);
            for (int j = 0; j < n; j++){
                wheel.addSymbol(wheel.size() + 1, colors.get(j));
            }
            wheels.add(wheel);
        }
        activeWheel = wheels.get(n - 1);
        ok = true;
        if (n > 1 && isJackpot()){
            wheels.get(0).spin(1);
        }
    }

    
    /**
     * Añadir una reuda en la maquina 
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
     * Al añadir una rueda verificamos en que posicion esta antes de todo
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
     * Borrar una rueda de la maquina. Pero tenemso que verificar que al menos haya alguna rueda
     * Si hay una rueda se puede eliminar. Si no pues no se realiza el metodo.
     *
     * @param  Posición 
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
     * Agregar un simbolo con un color y una figura (Triangulo,Rectangulo y Circulo)
     *
     * @param  pos, color la posicion donde se le va a asignar y que color se le va a
     * asignar
     * @return void
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
     * Buscar y eliminar un simbolo de cada rueda
     * @param  symbol la figura 
     * @return void
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
     * Mostar de una rueda en especifico un simbolo con un cierto color 
     *
     * @param wheel, symbol la rueda que queremos ver y el simbolo que buscamos 
     * @return void
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
     * Hacer girar una sola rueda selecionada a la siguiente posicion
     *
     * @param wheel La rueda la cual vamos a girar
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
        wheels.get(index - 1).spin();
        ok = true;
        redraw();
    }

    /**
     * Hacer girar todas las ruedas al mismo tiempo, de forma aleatoria.
     * Las ruedas fijadas (locked) se ignoran y conservan su símbolo actual.
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
     * Devuelve una lista con todos los colores de la primera rueda en orden
     *
     * @return String Devuelve un arreglo de la guinete forma symbols: Nombre 
     */
    public String[] symbols()
    {
        if (wheels.size() == 0)
        {
            return new String[0];
        }
        return wheels.get(0).allSymbols();
    }
    
    /**
     * Cuenta cuántos colores únicos y diferentes existen en total en toda la máquina
     * 
     * @return int Un numeor entero con el total de distintos simbolos
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
     * 
     * @return String El nombre de la funcion del esatdo visual
     */
    public String[] configuration()
    {
        String[] visibleColors = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++)
        {
            visibleColors[i] = wheels.get(i).currentSymbol();
        }
        return visibleColors;
    }
    
    /**
     * Verifica si el jugador ganó si todas las ruedas muestran el mismo color.
     * 
     * @return Boolean O verdadero o falso 
     */
    public boolean isJackpot()
    {   
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
     * Muestra visualmente la interfaz de la máquina y sus ruedas.
     * 
     * @return void
     */
    
    public void makeVisible()
    {
        visible = true;
        redraw();
    }
    
    /**
     * Oculta la interfaz de la máquina sin detener su funcionamiento interno.
     * 
     * @return void
     */

    public void makeInvisible()
    {
        visible = false;
        redraw();
    }
    
    /**
     * Apaga o cierra la ventana de simulación del juego.
     * 
     * @return void  
     */
   
    public void exit()
    {
        visible = false;
        redraw();
    }
    
    /**
     * Indica si la última acción realizada por el usuario se ejecutó correctamente.
     *
     * @return true si la última operación fue válida, false en caso contrario
     */
    public boolean ok()
    {
        return ok;
    }

    /**
     * Intercambia la posición de dos ruedas dentro de la máquina.
     * Requisito 9 (Extensión Ciclo 2): intercambiar dos ruedas.
     *
     * @param wheel1 posición de la primera rueda
     * @param wheel2 posición de la segunda rueda
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
     * Fija (bloquea) una rueda para que no gire hasta que sea liberada con {@link #unlock(int)}.
     * Requisito 10 (Extensión Ciclo 2): fijar y soltar una rueda.
     *
     * @param wheel posición de la rueda a fijar
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
     * Suelta (desbloquea) una rueda previamente fijada, permitiendo que vuelva a girar.
     * Requisito 10 (Extensión Ciclo 2): fijar y soltar una rueda.
     *
     * @param wheel posición de la rueda a soltar
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
     * Deja la máquina en una configuración dada: cada posición del arreglo indica
     * el símbolo (color) que debe quedar visible en la rueda correspondiente.
     * Las ruedas fijadas (locked) no se modifican.
     * Requisito 12 (Extensión Ciclo 2): dejar la máquina en una configuración dada.
     *
     * @param setSymbols arreglo con el símbolo deseado para cada rueda, de izquierda a derecha
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
     * Gira una rueda específica un número de pasos determinado. Si la máquina está
     * visible, el movimiento se visualiza paso a paso (Requisito de usabilidad 1).
     * Una rueda fijada (locked) no gira.
     * Requisito 11 (Extensión Ciclo 2): rotar una rueda un número de pasos.
     *
     * @param wheel posición de la rueda a girar
     * @param steps número de pasos a girar (positivo o negativo)
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
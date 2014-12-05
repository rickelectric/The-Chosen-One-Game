package rickelectric.game.chosen;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class KeyboardInputService implements KeyListener {
	private static KeyboardInputService thisInst;
	
	private HashMap<Integer,Boolean> keys;

	private KeyboardInputService() {
		keys = new  HashMap<Integer,Boolean>();
	}

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static synchronized KeyboardInputService getInstance() {
		if (thisInst == null) {
			thisInst = new KeyboardInputService();
		}
		return thisInst;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void reset() {
		keys = new  HashMap<Integer,Boolean>();
	}

	public boolean isLeft() {
		Boolean v =  keys.get(KeyEvent.VK_LEFT);
		return v==null?false:v;
	}

	public boolean isRight() {
		Boolean v =  keys.get(KeyEvent.VK_RIGHT);
		return v==null?false:v;
	}

	public boolean isUp() {
		Boolean v = keys.get(KeyEvent.VK_UP);
		return v==null?false:v;
	}

	public boolean isDown() {
		Boolean v =  keys.get(KeyEvent.VK_DOWN);
		return v==null?false:v;
	}

	public boolean isSpace() {
		Boolean v =  keys.get(KeyEvent.VK_SPACE);
		return v==null?false:v;
	}
	
	public boolean isW(){
		Boolean v =  keys.get(KeyEvent.VK_W);
		return v==null?false:v;
	}
	
	public boolean isA(){
		Boolean v =  keys.get(KeyEvent.VK_A);
		return v==null?false:v;
	}
	
	public boolean isS(){
		Boolean v =  keys.get(KeyEvent.VK_S);
		return v==null?false:v;
	}
	
	public boolean isD(){
		Boolean v =  keys.get(KeyEvent.VK_D);
		return v==null?false:v;
	}
	
	public boolean isZ() {
		Boolean v =  keys.get(KeyEvent.VK_Z);
		return v==null?false:v;
	}
	
	public boolean isX() {
		Boolean v =  keys.get(KeyEvent.VK_X);
		return v==null?false:v;
	}
	
	public boolean isPageUp(){
		Boolean v =  keys.get(KeyEvent.VK_PAGE_UP);
		return v==null?false:v;
	}
	
	public boolean isPageDown(){
		Boolean v =  keys.get(KeyEvent.VK_PAGE_DOWN);
		return v==null?false:v;
	}
	
	public boolean isBackspace() {
		Boolean v =  keys.get(KeyEvent.VK_BACK_SPACE);
		return v==null?false:v;
	}

	public boolean isEnter() {
		Boolean v =  keys.get(KeyEvent.VK_ENTER);
		return v==null?false:v;
	}

	public boolean isEscape() {
		Boolean v =  keys.get(KeyEvent.VK_ESCAPE);
		return v==null?false:v;
	}

	public boolean anyKeyPressed() {
		for(Integer key:keys.keySet()){
			if(keys.get(key)) return true; 
		}
		return false;
	}

}

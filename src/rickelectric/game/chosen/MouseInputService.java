package rickelectric.game.chosen;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputService implements MouseListener, MouseMotionListener {
	private static MouseInputService thisInst;
	private float mouseX;
	private float mouseY;
	private float dragMouseX;
	private float dragMouseY;

	private boolean mouseButtonL, mouseButtonR, mouseIn;

	public boolean buttonL() {
		return mouseButtonL;
	}

	public boolean buttonR() {
		return mouseButtonR;
	}

	public boolean mouseIn() {
		return mouseIn;
	}

	private MouseInputService() {
		mouseX = mouseY = dragMouseX = dragMouseY = 0.0f;
		mouseButtonL = mouseButtonR = false;
	}

	public synchronized static MouseInputService getInstance() {
		if (thisInst == null)
			thisInst = new MouseInputService();
		return thisInst;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragMouseX = e.getX();
		dragMouseY = e.getY();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			if (e.getX() < 20 && e.getY() < 20) {
				//Fullcreen
				//GameSystem.getInstance().fullscreen();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseIn = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseIn = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseButtonL = true;
			mouseX = dragMouseX = e.getX();
			mouseY = dragMouseY = e.getY();
			break;
		case MouseEvent.BUTTON3:
			mouseButtonR = true;
			mouseX = dragMouseX = e.getX();
			mouseY = dragMouseY = e.getY();
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseButtonL = false;
			break;
		case MouseEvent.BUTTON3:
			mouseButtonR = false;
			break;
		}
	}

	public float getMouseX() {
		return mouseX;
	}

	public float getMouseY() {
		return mouseY;
	}

	public float getDragMouseX() {
		return dragMouseX;
	}

	public float getDragMouseY() {
		return dragMouseY;
	}

}

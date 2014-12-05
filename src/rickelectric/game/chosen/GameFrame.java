package rickelectric.game.chosen;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Component area;
	private Canvas canvas;

	/**
	 * Constructor
	 * 
	 * @param windowTitle
	 */
	public GameFrame(String windowTitle) {
		super(windowTitle);

		JPanel panel = (JPanel) getContentPane();
		panel.setPreferredSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());

		panel.setLayout(null);
		//setBounds(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());

		canvas = new Canvas();
		area = canvas;
		area.setSize(panel.getPreferredSize());
		panel.add(area);

		setIgnoreRepaint(true);
		pack();
		setVisible(true);

		canvas.createBufferStrategy(2);

		// set up Listeners
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		area.requestFocus();
	}

	public Component getArea() {
		return area;
	}

	public BufferStrategy getBufferStrategy() {
		return canvas.getBufferStrategy();
	}

}

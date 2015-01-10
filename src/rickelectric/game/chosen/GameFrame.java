package rickelectric.game.chosen;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Component area;
	private Canvas canvas;

	private JPanel contentPane;

	/**
	 * Constructor
	 * 
	 * @param windowTitle
	 */
	public GameFrame(String windowTitle) {
		super(windowTitle);
		setUndecorated(true);
		setAlwaysOnTop(true);
		
		addWindowFocusListener(new WindowFocusListener(){

			@Override
			public void windowGainedFocus(WindowEvent e) {
				setState(NORMAL);
				setSize(Toolkit.getDefaultToolkit().getScreenSize());
				contentPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
				area.setSize(contentPane.getPreferredSize());
				GameSystem.getInstance().resumeGame();
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				setState(ICONIFIED);
				GameSystem.getInstance().suspendGame();
			}
			
		});
		setBackground(Color.black);
		
		contentPane = new JPanel();
		contentPane.setBackground(getBackground());
		setContentPane(contentPane);
		contentPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		contentPane.setLayout(null);
		//setBounds(0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		

		canvas = new Canvas();
		area = canvas;
		area.setSize(contentPane.getPreferredSize());
		contentPane.add(area);

		setIgnoreRepaint(true);
		pack();
		setLocationRelativeTo(null);
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

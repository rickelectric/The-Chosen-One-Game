package converter;

import java.awt.*;
import javax.swing.*;
import java.net.URL;

public class MainClass {
	public static void main(String[] args) throws Exception {
		URL fontUrl = MainClass.class.getResource("font1.TTF");
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
		font = font.deriveFont(Font.BOLD, 20);
		
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		ge.registerFont(font);

		JLabel l = new JLabel(
				"The quick brown fox jumps over the lazy dog. 0123456789");
		l.setFont(font);
		JOptionPane.showMessageDialog(null, l);
	}
}
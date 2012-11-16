package japcheckers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Александр
 */
@SuppressWarnings("serial")
public class ImageLabel extends JLabel {

	private int row, col, cell_size;
	private int left_spc = 0, up_spc = 0;

	private ArrayList<Checker> checkers;
	private ArrayList<Checker[]> lines;
	private Color[][] colors = { {Color.BLUE, Color.CYAN}, {Color.RED, Color.PINK} };

	public ImageLabel () {
		checkers = new ArrayList<>();
		lines = new ArrayList<>();
	}

	public void setGridParams(int r, int c, int cell, int left, int up) {
		row = r;
		col = c;
		cell_size = cell;
		left_spc = left;
		up_spc = up;
	}

	public void addChecker (Checker ch) {
		checkers.add(ch);
	}

	public void addLine (Checker ch1, Checker ch2) {
		Checker[] chArray = {ch1, ch2};
		lines.add(chArray);
	}


	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Tahoma", Font.PLAIN, 11);
		g2.setFont(font);

		// draw grid
		g.setColor(Color.GRAY);
		for (int i = 0; i <= row; i++) {
			g.drawLine(left_spc, up_spc + i * cell_size, left_spc + col * cell_size, up_spc + i * cell_size);
		}
		for (int i = 0; i <= col; i++) {
			g.drawLine(left_spc + i * cell_size, up_spc, left_spc + i * cell_size, up_spc + row * cell_size);
		}
		// draw checkers
		for (Checker ch : checkers) {
			g.setColor(colors[ch.getTurn()][JapCheckers.Bool2Int(ch.getState() == Checker.State.CAPTURED)]);
			g.fillOval(left_spc + ch.getCrd().getX() * cell_size - 5, up_spc + ch.getCrd().getY() * cell_size - 5, 10, 10);
//			g2.drawString(Integer.toString(ch.getID()), left_spc + ch.getCrd().getX() * cell_size + 10, up_spc + ch.getCrd().getY() * cell_size + 10);
		}

		// draw lines
		for (Checker[] chArray : lines) {
			g2.setStroke(new BasicStroke(3));
			g2.setColor(colors[chArray[0].getTurn()][0]);
			g2.drawLine(chArray[0].getCrd().getX() * cell_size + left_spc, chArray[0].getCrd().getY() * cell_size + up_spc,
					   (chArray[1].getCrd().getX() ) * cell_size + left_spc,
					   (chArray[1].getCrd().getY() ) * cell_size + up_spc);
		}
	}

}

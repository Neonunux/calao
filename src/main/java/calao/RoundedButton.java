/***********************************************
This file is part of the Calao project (https://github.com/Neonunux/calao/wiki).

Calao is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Calao is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Calao.  If not, see <http://www.gnu.org/licenses/>.

 **********************************************/
package calao;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class RoundedButton.
 *
 * @author Neonunux
 */
public class RoundedButton extends JButton {

	private static final Logger logger = LogManager
			.getLogger(RoundedButton.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8458705986423151858L;

	/** The b label. */
	private String bLabel;

	/** The app bundle. */
	private ResourceBundle appBundle;

	/** The img. */
	private Image img = null;

	/** The end color. */
	private Color endColor = Color.decode("0x4D5D8F");

	/** The font size. */
	private int fontSize = 20;

	/** The text off y. */
	private int textOffX = 0, textOffY = 0;

	/** The img w. */
	private int imgW = -1;

	/** The img h. */
	private int imgH = -1;

	private boolean border = false;

	private boolean gradientPaint = false;

	private int borderWidth = 3;

	/**
	 * Instantiates a new rounded button.
	 *
	 * @param label
	 *            the label
	 * @param b
	 *            the b
	 */
	public RoundedButton(String label, ResourceBundle b) {
		super(label);
		bLabel = label;
		appBundle = b;
	}

	/**
	 * Instantiates a new rounded button.
	 *
	 * @param label
	 *            the label
	 * @param b
	 *            the b
	 * @param eC
	 *            the e c
	 */
	public RoundedButton(String label, ResourceBundle b, Color eC) {
		super(label);
		bLabel = label;
		appBundle = b;
		endColor = eC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractButton#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		bLabel = label;
		repaint();
	}

	/**
	 * Sets the res bundle.
	 *
	 * @param b
	 *            the new res bundle
	 */
	public void setResBundle(ResourceBundle b) {
		appBundle = b;
		repaint();
	}

	/**
	 * Sets the font size.
	 *
	 * @param size
	 *            the new font size
	 */
	public void setFontSize(int size) {
		fontSize = size;
	}

	/**
	 * Sets the text offsets.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setTextOffsets(int x, int y) {
		textOffX = x;
		textOffY = y;
		// repaint();
	}

	/**
	 * Sets the button image.
	 *
	 * @param i
	 *            the new button image
	 */
	public void setButtonImage(Image i) {
		img = i;
	}

	/**
	 * Sets the imag size.
	 *
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 */
	public void setImageSize(int w, int h) {
		imgW = w;
		imgH = h;
		repaint();
	}

	public boolean hasBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public boolean isGradientPainted() {
		return gradientPaint;
	}

	public void setGradientPaint(boolean gradientPaint) {
		this.gradientPaint = gradientPaint;
	}

	// Draw the button
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color bgColor;
		Color tmpColor = endColor;
		int bWidth = getBorderWidth();

		if (this.isEnabled() == false) {
			g.setColor(Color.decode("0x666666"));
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
			if (this.hasBorder()) {
				g.setColor(Color.decode("0xBBBBBB"));
				g.fillRoundRect(getBorderWidth(), getBorderWidth(),
						getSize().width - 2 * bWidth, getSize().height - 2
								* bWidth, 27, 27);
			}
			g.setColor(Color.decode("0x888888"));
		} else {
			if (this.hasBorder()) {
				g.setColor(Color.decode("0x5F8DD3"));
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
			}

			if (getModel().isArmed()) // is button being clicked ?
				bgColor = Color.decode("0x869EBA");
			else if (getModel().isRollover()) // rollover effect
			{
				bgColor = Color.decode("0xB8D8FF");
				tmpColor = Color.decode("0x667BBD");
			} else {
				bgColor = getBackground(); // normal state
			}

			((Graphics2D) g).setPaint(bgColor);

			if (this.isGradientPainted()) {
				// gradient fill:
				GradientPaint vertGrad = new GradientPaint(0, 0, bgColor, 0,
						getHeight(), tmpColor);
				((Graphics2D) g).setPaint(vertGrad);
			}
			g.fillRoundRect(getBorderWidth(), getBorderWidth(), getSize().width
					- 2 * bWidth, getSize().height - 2 * bWidth, 27, 27);
			g.setColor(Color.black);
		}

		int textWidth = 0;
		int vOffset = getHeight() / 2;
		int hOffset = getWidth() / 2;
		if (bLabel == "RBL_INLINE") {
			String title = appBundle.getString("_menuNotereading");
			g.setFont(new Font("Arial", Font.BOLD, fontSize));
			FontMetrics fM1 = g.getFontMetrics();
			textWidth = fM1.stringWidth(title);
			g.drawString(title, (getSize().width - textWidth) / 2, 50);
			g.setFont(getFont().deriveFont(75f));
			// FontMetrics fM2 = g.getFontMetrics();
			this.setButtonImage(new ImageIcon(getClass()
					.getResource("home.png")).getImage());
		} else if (bLabel == "RBL_RHYTHM") {
			String title = appBundle.getString("_menuRythmreading");
			String ss = "" + (char) 0xA9 + (char) 0xA9 + (char) 0xA9
					+ (char) 0xA9; // staff symbol
			g.setFont(new Font("Arial", Font.BOLD, fontSize));
			FontMetrics fM1 = g.getFontMetrics();
			textWidth = fM1.stringWidth(title);
			g.drawString(title, (getSize().width - textWidth) / 2, 50);
			g.setFont(getFont().deriveFont(75f));
			FontMetrics fM2 = g.getFontMetrics();
			textWidth = fM2.stringWidth(ss);
			g.drawString(ss, (getSize().width - textWidth) / 2, vOffset + 60);
			String rs = "" + (char) 0xDA;
			g.drawString(rs, hOffset, vOffset + 39);
		} else if (bLabel == "RBL_SCORE") {
			String title = appBundle.getString("_menuScorereading");
			String ss = "" + (char) 0xA9 + (char) 0xA9 + (char) 0xA9
					+ (char) 0xA9; // staff symbol
			g.setFont(new Font("Arial", Font.BOLD, fontSize));
			FontMetrics fM1 = g.getFontMetrics();
			textWidth = fM1.stringWidth(title);
			g.drawString(title, (getSize().width - textWidth) / 2, 50);
			g.setFont(getFont().deriveFont(75f));
			FontMetrics fM2 = g.getFontMetrics();
			textWidth = fM2.stringWidth(ss);
			g.drawString(ss, (getSize().width - textWidth) / 2, vOffset + 60);
			String sm = "" + (char) 0xF4;
			g.drawString(sm, hOffset - 30, vOffset + 68);
			g.drawString(sm, hOffset + 15, vOffset + 38);
		} else if (bLabel == "RBL_NOTES") {
			String titlep1 = appBundle.getString("_menuClef");
			String titlep2 = appBundle.getString("_menuNotes");
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			FontMetrics fM = g.getFontMetrics();
			textWidth = fM.stringWidth(titlep1) / 2;
			g.drawString(titlep1, 15 + 25 - textWidth, vOffset - 10);
			fM = g.getFontMetrics();
			textWidth = fM.stringWidth("&") / 2;
			g.drawString("&", 15 + 25 - textWidth, vOffset + 5);
			fM = g.getFontMetrics();
			textWidth = fM.stringWidth(titlep2) / 2;
			g.drawString(titlep2, 15 + 25 - textWidth, vOffset + 20);
			// g.fillRoundRect(80, vOffset - 25, 70, 50, 15, 15); // 22
			g.setFont(getFont().deriveFont(27f));
			String ss = "" + (char) 0xA9 + (char) 0xA9 + (char) 0xA9
					+ (char) 0xA9; // staff symbol
			String sm = "" + (char) 0xF4;
			g.setColor(Color.black);
			g.drawString(ss, 87, vOffset + 11);
			g.setFont(getFont().deriveFont(35f));
			g.drawString("G", 90, vOffset + 12);
			g.drawString(sm, 110, vOffset + 18);
			g.drawString(sm, 130, vOffset + 11);
		} else if (bLabel == "RBL_STATS") {
			String title = appBundle.getString("_menuStatistics");
			g.setFont(new Font("Arial", Font.BOLD, fontSize));
			FontMetrics fM1 = g.getFontMetrics();
			textWidth = fM1.stringWidth(title);
			g.drawString(title, (getSize().width - textWidth) / 2, 50);

			((Graphics2D) g).setStroke(new BasicStroke(2,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawLine(hOffset - 60, vOffset - 10, hOffset - 60, vOffset + 60);
			g.drawLine(hOffset - 65, vOffset + 53, hOffset + 70, vOffset + 53);

			g.drawLine(hOffset - 39, vOffset + 40, hOffset - 10, vOffset + 20);
			g.drawLine(hOffset - 10, vOffset + 20, hOffset + 10, vOffset + 30);
			g.drawLine(hOffset + 10, vOffset + 30, hOffset + 40, vOffset - 5);
		} else if (bLabel == "RBL_LESSONS") {
			String title = appBundle.getString("_menuExercises");
			g.setFont(new Font("Arial", Font.BOLD, fontSize));
			FontMetrics fM1 = g.getFontMetrics();
			textWidth = fM1.stringWidth(title);
			g.drawString(title, (getSize().width - textWidth) / 2, 50);

			((Graphics2D) g).setStroke(new BasicStroke(2,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawRoundRect(hOffset - 50, vOffset - 10, 100, 65, 10, 10);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("A", hOffset - 30, vOffset + 30);
			g.drawString("B", hOffset - 5, vOffset + 20);
			g.drawString("C", hOffset + 20, vOffset + 40);
		} else if (bLabel == "RBL_EARTRAIN") {
			String title = appBundle.getString("_menuEarTraining");
			g.setFont(new Font("Arial", Font.BOLD, fontSize));
			FontMetrics fM1 = g.getFontMetrics();
			textWidth = fM1.stringWidth(title);
			g.drawString(title, (getSize().width - textWidth) / 2, 50);

			// Draw an ear !
			((Graphics2D) g).setStroke(new BasicStroke(2,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawArc(hOffset - 40, vOffset - 20, 50, 50, -20, 210);
			g.drawArc(hOffset - 32, vOffset - 12, 35, 32, -30, 230);
			QuadCurve2D q = new QuadCurve2D.Float();
			q.setCurve(hOffset + 7, vOffset + 15, hOffset, vOffset + 30,
					hOffset, vOffset + 40);
			((Graphics2D) g).draw(q);
			g.drawArc(hOffset - 25, vOffset + 20, 25, 40, 190, 170);
			g.fillOval(hOffset - 25, vOffset + 20, 10, 10);

			// draw sonic waves coming from the outer space
			g.drawArc(hOffset + 35, vOffset, 15, 50, 90, 150);
			g.drawArc(hOffset + 45, vOffset + 5, 10, 40, 90, 150);
			((Graphics2D) g).setStroke(new BasicStroke(3,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawArc(hOffset + 25, vOffset - 10, 20, 70, 90, 150);
		} else {
			FontMetrics fM1 = g.getFontMetrics(this.getFont());
			textWidth = fM1.stringWidth(bLabel);
			g.drawString(bLabel,
					textOffX + ((getSize().width - textWidth) / 2),
					textOffY + 25);
		}

		if (img != null) {
			if (imgW != -1)
				g.drawImage(img, (getWidth() - imgW) / 2,
						(getHeight() - imgH) / 2, imgW, imgH, this);
			else
				g.drawImage(img, (getWidth() - img.getWidth(null)) / 2,
						(getHeight() - img.getHeight(null)) / 2, null);
		}
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

}

class RoundPanel extends JPanel {
	private static final long serialVersionUID = 2133404549466988014L;
	boolean gradientBack = false;
	Color startColor;
	Color endColor;
	Color borderColor = Color.decode("0x5F8DD3");
	private boolean isBorder = false;
	private int borderWidth = 3;

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public RoundPanel() {
	}

	public RoundPanel(Color startCol, Color endCol) {
		gradientBack = false;
		isBorder = true;
		startColor = startCol;
		endColor = endCol;
	}

	public boolean isBorder() {
		return isBorder;
	}

	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}

	public void setBorderColor(Color bc) {
		this.borderColor = bc;
	}

	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int bWidth = getBorderWidth();
		if (isBorder) {
			g.setColor(borderColor);
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
		}
		if (gradientBack == false)
			g.setColor(getBackground());
		else {
			GradientPaint vertGrad = new GradientPaint(0, 0, startColor, 0,
					getHeight(), endColor);
			((Graphics2D) g).setPaint(vertGrad);
		}
		g.fillRoundRect(bWidth, bWidth, getWidth() - 2 * bWidth, getHeight()
				- 2 * bWidth, 15, 15);
	}
}

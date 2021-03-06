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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class GameBar.
 *
 * @author Neonunux
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class GameBar extends JPanel implements ActionListener
{
	
	
	private static final Logger logger =  LogManager.getLogger(GameBar.class.getName());
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6433532306226295481L;
	
	/** The app bundle. */
	ResourceBundle appBundle;
	
	
	Preferences appPrefs;
	
	/** The app font. */
	Font appFont;

	/** The notes number. */
	public JComboBox notesNumber;
	
	/** The progress. */
	public GradientBar progress;
	
	/** The score label. */
	private JLabel scoreLabel;
	
	/** The score cnt. */
	public JTextField scoreCnt;
	
	/** The precision label. */
	private JLabel precisionLabel;
	
	/** The precision cnt. */
	public JTextField precisionCnt;
	
	/** The is inline. */
	boolean isInline = false;
	
	/** The total obj width. */
	int totalObjWidth = 600;
	
	/** The upper margin. */
	int upperMargin = 7;

	/**
	 * Instantiates a new game bar.
	 *
	 * @param d the d
	 * @param b the b
	 * @param f the f
	 * @param p the p
	 * @param inline the inline
	 */
	public GameBar (Dimension d, ResourceBundle b, Font f, Preferences p, boolean inline)
	{
		appBundle = b;
		appFont = f;
		appPrefs = p;
		isInline = inline;
		setSize(d);
		setLayout(null);

		int posX = (d.width - totalObjWidth) / 2;
		
		if (isInline == true)
		{
			totalObjWidth = 730;
			Font sbf = new Font("Arial", Font.BOLD, 13);
			notesNumber = new JComboBox();
			notesNumber.setPreferredSize(new Dimension(100, 30));
			notesNumber.setFont(sbf);
			notesNumber.setBounds(posX, upperMargin, 120, 27);
			posX+=130;
		}
		
		progress = new GradientBar(0, 100);
		progress.setPreferredSize(new Dimension(200, 27));
		progress.setValue(0);
		progress.setBounds(posX, upperMargin, 200, 27);
		posX += 210;
		
		Font scf = new Font("Arial", Font.BOLD, 18);
		scoreLabel = new JLabel(appBundle.getString("_gameScore"), null, JLabel.RIGHT);
		scoreLabel.setFont(scf);
		scoreLabel.setBounds(posX, upperMargin, 100, 30);
		posX += 110;

		scoreCnt = new JTextField("0");
		scoreCnt.setPreferredSize(new Dimension(80, 30));
		scoreCnt.setEditable(false);
		scoreCnt.setFont(scf);
		scoreCnt.setBounds(posX, upperMargin, 80, 30);
		posX += 90;
		
		precisionLabel = new JLabel(appBundle.getString("_gamePrecision"), null, JLabel.RIGHT);
		precisionLabel.setFont(scf);
		precisionLabel.setPreferredSize(new Dimension(110, 30));
		precisionLabel.setBounds(posX, upperMargin, 110, 30);
		posX += 120;

		precisionCnt = new JTextField("0%");
		precisionCnt.setPreferredSize(new Dimension(70, 30));
		precisionCnt.setEditable(false);
		precisionCnt.setFont(scf);
		precisionCnt.setBounds(posX, upperMargin, 70, 30);

		if (isInline == true)
			add(notesNumber);
		add(progress);
		add(scoreLabel);
		add(scoreCnt);
		add(precisionLabel);
		add(precisionCnt);
		
		updateLanguage(appBundle);
	}
	
	/**
	 * Update language.
	 *
	 * @param bundle the bundle
	 */
	public void updateLanguage(ResourceBundle bundle)
	{
		appBundle = bundle;
		scoreLabel.setText(appBundle.getString("_gameScore"));
		precisionLabel.setText(appBundle.getString("_gamePrecision"));
		if (isInline == true)
		{
			notesNumber.removeAllItems();
			notesNumber.addItem("10 " + appBundle.getString("_menuNotes"));
			notesNumber.addItem("20 " + appBundle.getString("_menuNotes"));
			notesNumber.addItem("40 " + appBundle.getString("_menuNotes"));
			notesNumber.addItem("80 " + appBundle.getString("_menuNotes"));
			notesNumber.setSelectedIndex(2);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) 
	{
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint vertGrad = new GradientPaint(0, 0, Color.decode("0xE7E734"), 0, getHeight() - 30, Color.decode("0xE7A935"));
		((Graphics2D) g).setPaint(vertGrad);
		g.fillRoundRect(20, 0, getWidth() - 40, getHeight()+ 15, 15, 15);
		
		int posX = (getWidth() - totalObjWidth) / 2;
		if (isInline == true)
		{
			notesNumber.setBounds(posX, upperMargin, 120, 27);
			posX+=130;
		}
		progress.setBounds(posX, upperMargin, 200, 27);
		posX += 210;
		scoreLabel.setBounds(posX, upperMargin, 100, 30);
		posX += 110;
		scoreCnt.setBounds(posX, upperMargin, 80, 30);
		posX += 90;
		precisionLabel.setBounds(posX, upperMargin, 110, 30);
		posX += 120;
		precisionCnt.setBounds(posX, upperMargin, 70, 30);
	}
}


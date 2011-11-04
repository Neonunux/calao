import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
//import java.awt.Polygon;

import javax.swing.JPanel;

public class Piano extends JPanel 
{
  private static final long serialVersionUID = -5581159862523677986L;
  Vector<Key> whiteKeys = new Vector<Key>();
  Vector<Key> blackKeys = new Vector<Key>();
  Vector<Key> keys = new Vector<Key>(); // Array of whiteKeys + blackKeys
  
  // painting parameters
  boolean paintArrows = true;
  int currentWidth = 0;
  
  int keysNumber = 73;
  int firstLowPitch = 40;
  int firstHighPitch = 42;  
  int secondLowPitch = 70;
  int secondHighPitch = 75;
  int pitchcourant0 = 0;
  int pitchcourant1 = 0;
  int pitchcourant2 = 0;

  // Keys dimensions and offsets
  Key prevKey;
  final int kw = 16, kh = 80;
  final int ypos = 5;
  int leftMargin = 43;
  

  public Piano(int l) 
  {
    int octavesNumber;
    int transpose;
    int offset = 0;

    this.keysNumber = l;

    if (keysNumber == 61) 
    {
        octavesNumber = 5;
        transpose = 36;
    }
    else 
    {
        octavesNumber = 6;
        transpose = 24;
    }

    keys.removeAll(blackKeys);
    keys.removeAll(whiteKeys);
    
    setLayout(null);

    int whiteIDs[] = { 0, 2, 4, 5, 7, 9, 11 };

    for (int i = 0, x = 0; i < octavesNumber; i++) 
    {
      for (int j = 0; j < 7; j++, x += kw) 
      {
        int keyPitch = i * 12 + whiteIDs[j] + transpose;
        whiteKeys.add(new Key(x + leftMargin, ypos, kw, kh, keyPitch, j, 0, false));
      }
    }

    whiteKeys.add(new Key(7 * octavesNumber * kw + leftMargin + 3, ypos, kw, kh, 
			octavesNumber * 12 + transpose, 0, 0, false));

    for (int i = 0, x = -1; i < octavesNumber; i++, x += kw) 
    {
      int keyPitch = i * 12 + transpose;
      blackKeys.add(new Key( x += kw + leftMargin, ypos, kw / 2, kh / 2 + 11, keyPitch + 1, 0, offset, true));
      blackKeys.add(new Key( x += kw + leftMargin, ypos, kw / 2, kh / 2 + 11, keyPitch + 3, 1, offset, true));
      x += kw;
      offset +=kw;
      blackKeys.add(new Key( x += kw + leftMargin, ypos, kw / 2, kh / 2 + 11, keyPitch + 6, 3, offset, true));
      blackKeys.add(new Key( x += kw + leftMargin, ypos, kw / 2, kh / 2 + 11, keyPitch + 8, 4, offset, true));
      blackKeys.add(new Key( x += kw + leftMargin, ypos, kw / 2, kh / 2 + 11, keyPitch + 10, 5, offset, true));
      offset += kw;
    }
    keys.addAll(blackKeys);
    keys.addAll(whiteKeys);
    for (int i = 0; i < keys.size(); i++)
    {
    	if (keys.get(i).pitch == 60)
    		keys.get(i).setBackground(new Color(255, 175, 175));
    	this.add(keys.get(i));
    }
  }
  
  public void reset(boolean highlightStart)
  {
	  for (int i = 0; i < whiteKeys.size(); i++)
	  {
		  if (highlightStart == true && whiteKeys.get(i).pitch == 60)
			  whiteKeys.get(i).setBackground(new Color(255, 175, 175));
		  else
			  whiteKeys.get(i).setBackground(Color.white);
	  }
	  for (int i = 0; i < blackKeys.size(); i++)
	  {
		  blackKeys.get(i).setBackground(Color.black);
	  }
  }

  // highlight a key and returns the note index from 0 to 7
  public int highlightKey(int pitch, boolean enable)
  {
	  for (int i = 0; i < keys.size(); i++)
	  {
		  if (keys.get(i).pitch == pitch)
		  {
			  if (enable == true)
				  keys.get(i).setBackground(Color.decode("0xB8D8FF"));
			  else
			  {
				  if (keys.get(i).is_black == true)
					  keys.get(i).setBackground(Color.black);
				  else
					  keys.get(i).setBackground(Color.white);
			  }
		  	  return keys.get(i).noteIndex;
		  }
	  }
	  return 0;
  }

  public boolean is73keys() 
  {
    return this.keysNumber == 73;
  }

  public boolean is61keys() 
  {
    return this.keysNumber == 61;
  }

  public void setNewBound(int low, int high)
  {
	  if (low == -1 || high == -1)
		  return;
	  // reset previously set keys
	  /*
	  for (int i = 0; i < whiteKeys.size(); i++)
	  {
		  if (whiteKeys.get(i).pitch >= leftHandLowPitch && whiteKeys.get(i).pitch <= leftHandHighPitch)
		  	whiteKeys.get(i).setBackground(Color.white);
		  if (whiteKeys.get(i).pitch > leftHandHighPitch)
			  break;
	  }
	  leftHandLowPitch = low;
	  leftHandHighPitch = high;
	  */
	  // highlight new range
	  for (int i = 0; i < whiteKeys.size(); i++)
	  {
		  if (whiteKeys.get(i).pitch >= low && whiteKeys.get(i).pitch <= high)
		  	whiteKeys.get(i).setBackground(new Color(152, 251, 152));
		  if (whiteKeys.get(i).pitch > high)
			  break;
      }
	  repaint();
  }

  protected void paintComponent(Graphics g) 
  {
    Graphics2D g2 = (Graphics2D) g;
    
    int offx = 0;
    if (keysNumber == 73)
    	offx = (getWidth()/2) - 390;
    else
    	offx = (getWidth()/2) - 325;

/*
    if (paintArrows)
    {
	    // draw arrow buttons to move left hand and right hand bounds
	    g2.setColor(new Color(152, 251, 152));
	    g2.fill3DRect(740 + offx, 30, 30,30, true) ;
	    g2.fill3DRect(5 + offx, 30, 30,30, true) ;
	    g2.setColor(Color.black);
	    g2.fillRect(745 + offx, 41, 12, 8);
	    g2.fillRect(15 + offx, 41, 12, 8);
	
	    int[]x = new int[3];
	    int[]y = new int[3]; 
	    //     Make a triangle
	    x[0]=756 + offx; x[1]=766 + offx; x[2]=756 + offx;
	    y[0]=35; y[1]=45; y[2]=55;
	    Polygon myTri = new Polygon(x, y, 3); 
	    g2.fillPolygon(myTri);
	    x[0]=18 + offx; x[1]=8 + offx; x[2]=18 + offx;
	    myTri = new Polygon(x, y, 3); 
	    g2.fillPolygon(myTri);
    }
 */
    // repaint keys only if piano panel has been resized
    if (currentWidth != getWidth())
    {
    	currentWidth = getWidth();
    	g2.setColor(Color.black);
    	for (int i = 0; i < whiteKeys.size(); i++) 
    	{
    		Key key = (Key) whiteKeys.get(i);
    		key.setXpos(2 + leftMargin + offx + (kw * i));
    		key.repaint();
    	}

    	for (int i = 0; i < blackKeys.size(); i++) 
    	{
    		Key key = (Key) blackKeys.get(i);
    		key.setXpos(leftMargin + offx + key.getXoffset() - 2 + ((i+1) * kw)); 
    		key.repaint();
    	}
    }
  }
} // End class Piano


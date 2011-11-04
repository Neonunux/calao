
public class Note 
{
	public int xpos;
	public int ypos;
	public int type; // 0 whole, 1 half, 2 quarter, 3 eighth, 4 triplet, 5 silence
	public double duration;

	public int clef; // note clef
	public int altType; // alteration to be displayed. Can be: -1 = flat, 0 = none, 1 = sharp
	public boolean secondRow; // indicates whether the note is on the first or second row
	public int level; // note level as handled in the ClefSelector
	public int addLinesNumber; // number of additional lines (if present)
	public int addLinesYpos; // Y position of the first additional line (if present)
	public int pitch; // MIDI note pitch
	public int tripletValue = 0;
	public boolean highlight; // used when playing a rhtyhm or score sequence
	
	public Note(int xPos, int nClef, int nLevel, int nPitch, int nType, boolean nSecondRow, int nAlt)
	{
		xpos = xPos;
		clef = nClef;
		altType = nAlt;
		level = nLevel;
		pitch = nPitch;
		type = nType;
		secondRow = nSecondRow;
		addLinesNumber = 0;
		addLinesYpos = 0;
		highlight = false;

		switch(type)
		{
			case 0: duration = 4; break;
			case 1: duration = 2; break;
			case 2: duration = 1; break;
			case 3: duration = 0.5; break;
			case 4: duration = 1.0 / 3.0; break;
			case 5: duration = 0; level = 10; pitch = 71; break;
		}
		
		System.out.println("[Note] t: " + type + ", p: " + pitch + ", l: " + level + ", dur: " + duration + ", alt: " + altType);
		
		ypos = 0; // y positions are calculated by the NotesPanel
	}
	
	public double getDuration(int type)
	{
		double dur = 1;
		switch(type)
		{
			case 0: dur = 4; break;
			case 1: dur = 2; break;
			case 2: dur = 1; break;
			case 3: dur = 0.5; break;
			case 4: dur = 1.0 / 3.0; break;
			case 5: dur = 0; break;
		}
		return dur;
	}
	public void setTripletValue(int val)
	{
		tripletValue = val;
	}
}

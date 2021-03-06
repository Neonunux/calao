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

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class NoteGenerator.
 *
 * @author Neonunux
 */
public class NoteGenerator {

	private static final Logger logger = LogManager
			.getLogger(NoteGenerator.class.getName());

	Preferences appPrefs;

	/** The accidentals. */
	Accidentals accidentals;

	/** The base note. */
	int baseNote = 24; // pitch of C0

	/** The CLE f_ g2_ basepitch. */
	int CLEF_G2_BASEPITCH = 50; // D2

	/** The CLE f_ c1_ basepitch. */
	int CLEF_C1_BASEPITCH = 47; // B1

	/** The CLE f_ c2_ basepitch. */
	int CLEF_C2_BASEPITCH = 43; // G1

	/** The CLE f_ c3_ basepitch. */
	int CLEF_C3_BASEPITCH = 40; // E1

	/** The CLE f_ c4_ basepitch. */
	int CLEF_C4_BASEPITCH = 36; // C1

	/** The CLE f_ c5_ basepitch. */
	int CLEF_C5_BASEPITCH = 33; // A0

	/** The CLE f_ f4_ basepitch. */
	int CLEF_F4_BASEPITCH = 29; // F0

	// intervals from base note to build chords or intervals
	/** The chords intervals. */
	char[][] chordsIntervals = { { 4, 7 }, // major
			{ 3, 7 }, // minor
			{ 3, 6 }, // diminished
			{ 4, 8 } }; // augmented

	/** The interval no perfect. */
	int[] intervalNoPerfect = { -2, -1, 0, 1 };

	/** The interval perfect. */
	int[] intervalPerfect = { -2, 0, 1 };

	/** The intervals. */
	char[] intervals = { 0, 1, 2, 4, 5, 7, 9, 11, 12 };

	/** The clef mask. */
	int clefMask = -1;

	/** The single clef. */
	boolean singleClef = false;

	/** The base range clef. */
	int baseRangeClef;

	/** The add range index. */
	int addRangeIndex; // index of randomPitchList of a second range (if added)

	/** The add range clef. */
	int addRangeClef; // clef mask of a second range (if added)

	/** The time sign numerator. */
	private int timeSignNumerator = 0;

	/** The time sign denominator. */
	private int timeSignDenominator = 0;

	/** The base list. */
	Vector<Integer> baseList = new Vector<Integer>(); // list holding the whole
														// list of notes, from
														// C0 to C6

	/** The altered list. */
	Vector<Integer> alteredList = new Vector<Integer>(); // list holding the
															// whole list of
															// alterated notes,
															// from C0 to C6

	/** The random pitch list. */
	Vector<Note> randomPitchList = new Vector<Note>(); // list of notes holding
														// the user selected
														// notes

	/** The notes type list. */
	Vector<Integer> notesTypeList = new Vector<Integer>(); // list of note types
															// to facilitate
															// random generation

	/** The random enabled. */
	private boolean randomEnabled = true;

	/** The notes list index. */
	private int notesListIndex = 0;

	/* C D E F G A B */
	/* ---------------------------- */
	/** The sharps matrix. */
	char[][] sharpsMatrix = { { 0, 2, 4, 5, 7, 9, 11 }, // offsets from the
														// octave first note
			{ 0, 0, 0, 1, 0, 0, 0 }, // 1 alteration
			{ 1, 0, 0, 1, 0, 0, 0 }, // 2 alterations
			{ 1, 0, 0, 1, 1, 0, 0 }, // 3 alterations
			{ 1, 1, 0, 1, 1, 0, 0 }, // 4 alterations
			{ 1, 1, 0, 1, 1, 1, 0 }, // 5 alterations
			{ 1, 1, 1, 1, 1, 1, 0 }, // 6 alterations
			{ 1, 1, 1, 1, 1, 1, 1 } // 7 alterations
	};

	/* C D E F G A B */
	/* ---------------------------- */
	/** The flats matrix. */
	char[][] flatsMatrix = { { 0, 2, 4, 5, 7, 9, 11 }, // offsets from the
														// octave first note
			{ 0, 0, 0, 0, 0, 0, 1 }, // 1 alteration
			{ 0, 0, 1, 0, 0, 0, 1 }, // 2 alterations
			{ 0, 0, 1, 0, 0, 1, 1 }, // 3 alterations
			{ 0, 1, 1, 0, 0, 1, 1 }, // 4 alterations
			{ 0, 1, 1, 0, 1, 1, 1 }, // 5 alterations
			{ 1, 1, 1, 0, 1, 1, 1 }, // 6 alterations
			{ 1, 1, 1, 1, 1, 1, 1 } // 7 alterations
	};

	/**
	 * Instantiates a new note generator.
	 *
	 * @param p
	 *            the p
	 * @param acc
	 *            the acc
	 * @param oneClef
	 *            the one clef
	 */
	public NoteGenerator(Preferences p, Accidentals acc, boolean oneClef) {
		this.appPrefs = p;
		this.accidentals = acc;

		singleClef = oneClef;
		baseRangeClef = -1;
		addRangeIndex = -1;
		addRangeClef = -1;

		randomPitchList.clear();
		initLists();
	}

	/**
	 * Inits the lists.
	 */
	private void initLists() {
		int pitch = baseNote; // starts from C0
		for (int i = 0; i < 6; i++) // 6 octaves, from C0 to C6
		{
			for (int j = 0; j < 7; j++) // 7 notes per octave
			{
				baseList.add(pitch + sharpsMatrix[0][j]);
				if (this.accidentals == null
						|| this.accidentals.getType() == "")
					alteredList.add(pitch + sharpsMatrix[0][j]);
				else if (this.accidentals.getType().equals("#"))
					alteredList.add(pitch + sharpsMatrix[0][j]
							+ sharpsMatrix[this.accidentals.getNumber()][j]);
				else if (this.accidentals.getType().equals("b"))
					alteredList.add(pitch + flatsMatrix[0][j]
							- flatsMatrix[this.accidentals.getNumber()][j]);

			}
			pitch += 12;
		}
		logger.debug(baseList);
	}

	/**
	 * Gets the pitch from level. Functions used to convert ClefSelector levels
	 * to real pitches (and vice versa), to be saved on preferences. Also used
	 * to calculate Y position when creating a new note
	 *
	 * @param clefLowestPitch
	 *            the clef lowest pitch
	 * @param level
	 *            the level
	 * @return the pitch from level
	 */
	public int getPitchFromLevel(int clefLowestPitch, int level) {
		int i = baseList.indexOf(clefLowestPitch);
		return baseList.get(i + level);
	}

	/**
	 * Gets the index from pitch.
	 *
	 * @param clefLowestPitch
	 *            the clef lowest pitch
	 * @param pitch
	 *            the pitch
	 * @param alterated
	 *            the alterated
	 * @return the index from pitch
	 */
	public int getIndexFromPitch(int clefLowestPitch, int pitch,
			boolean alterated) {
		int i;
		i = baseList.indexOf(clefLowestPitch);
		int j = 0;
		for (j = i; j < baseList.size(); j++) {
			int p;
			if (alterated == true)
				p = alteredList.get(j);
			else
				p = baseList.get(j);
			if (p == pitch)
				return j - i;
		}
		return 0;
	}

	/**
	 * Gets the rows distance.
	 *
	 * @return the rows distance
	 */
	public int getRowsDistance() {
		int height = 0, levels = 0;

		if (addRangeIndex == -1)
			levels = randomPitchList.size();
		else {
			int c1l = addRangeIndex;
			int c2l = randomPitchList.size() - addRangeIndex;
			if (c1l > c2l)
				levels = c1l;
			else
				levels = c2l;
		}
		if (levels * 5 < 50)
			height = 90;
		else
			height = 90 + ((levels - 9) * 5);

		if (addRangeIndex != -1)
			height *= 2;

		return height;
	}

	/**
	 * Gets the rows distance from clefs.
	 *
	 * @param clMask
	 *            the cl mask
	 * @return the rows distance from clefs
	 */
	public int getRowsDistanceFromClefs(int clMask) {
		int clefsNum = 0;
		if ((clMask & appPrefs.CLEF_G2) > 0)
			clefsNum++;
		if ((clMask & appPrefs.CLEF_F4) > 0)
			clefsNum++;
		if ((clMask & appPrefs.CLEF_C3) > 0)
			clefsNum++;
		if ((clMask & appPrefs.CLEF_C4) > 0)
			clefsNum++;

		return clefsNum * 90;
	}

	/**
	 * Gets the clefs number.
	 *
	 * @return the clefs number
	 */
	public int getClefsNumber() {
		if (addRangeIndex != -1)
			return 2;
		return 1;
	}

	/* ************************************************************** */

	/**
	 * Reset.
	 */
	public void reset() {
		// wholeList.clear();
		randomPitchList.clear();
		baseRangeClef = -1;
		addRangeIndex = -1;
		addRangeClef = -1;
	}

	/**
	 * Update.
	 */
	public void update() {
		int accIdx = Integer.parseInt(appPrefs.getProperty("accidentals"));
		if (accIdx <= 0)
			accidentals.setTypeAndCount("", 0);
		else if (accIdx < 8)
			accidentals.setTypeAndCount("#", accIdx);
		else
			accidentals.setTypeAndCount("b", accIdx - 7);
		reset();

		// reinit whole list with new accidentals
		baseList.clear();
		alteredList.clear();
		initLists();

		clefMask = Integer.parseInt(appPrefs.getProperty("clefsMask"));
		if (clefMask == -1)
			clefMask = appPrefs.CLEF_G2;
		if ((clefMask & appPrefs.CLEF_G2) > 0) {
			int lowerPitch = Integer.parseInt(appPrefs
					.getProperty("ClefG2Lower"));
			int higherPitch = Integer.parseInt(appPrefs
					.getProperty("ClefG2Upper"));
			if (lowerPitch == -1)
				lowerPitch = 64; // default, set to E3
			if (higherPitch == -1)
				higherPitch = 77; // default, set to F4
			addRange(appPrefs.CLEF_G2,
					alteredList.get(baseList.indexOf(lowerPitch)),
					alteredList.get(baseList.indexOf(higherPitch)));
			if (singleClef == true)
				clefMask = appPrefs.CLEF_G2;
		}
		if ((clefMask & appPrefs.CLEF_F4) > 0) {
			int lowerPitch = Integer.parseInt(appPrefs
					.getProperty("ClefF4Lower"));
			int higherPitch = Integer.parseInt(appPrefs
					.getProperty("ClefF4Upper"));
			if (lowerPitch == -1)
				lowerPitch = 43; // default, set to G1
			if (higherPitch == -1)
				higherPitch = 57; // default, set to A2
			addRange(appPrefs.CLEF_F4,
					alteredList.get(baseList.indexOf(lowerPitch)),
					alteredList.get(baseList.indexOf(higherPitch)));
			if (singleClef == true)
				clefMask = appPrefs.CLEF_F4;
		}
		if ((clefMask & appPrefs.CLEF_C3) > 0) {
			int lowerPitch = Integer.parseInt(appPrefs
					.getProperty("ClefC3Lower"));
			int higherPitch = Integer.parseInt(appPrefs
					.getProperty("ClefC3Upper"));
			if (lowerPitch == -1)
				lowerPitch = 53; // default, set to F2
			if (higherPitch == -1)
				higherPitch = 67; // default, set to G3
			addRange(appPrefs.CLEF_C3,
					alteredList.get(baseList.indexOf(lowerPitch)),
					alteredList.get(baseList.indexOf(higherPitch)));
			if (singleClef == true)
				clefMask = appPrefs.CLEF_C3;
		}
		if ((clefMask & appPrefs.CLEF_C4) > 0) {
			int lowerPitch = Integer.parseInt(appPrefs
					.getProperty("ClefC4Lower"));
			int higherPitch = Integer.parseInt(appPrefs
					.getProperty("ClefC4Upper"));
			if (lowerPitch == -1)
				lowerPitch = 50; // default, set to D2
			if (higherPitch == -1)
				higherPitch = 64; // default, set to E3
			addRange(appPrefs.CLEF_C4,
					alteredList.get(baseList.indexOf(lowerPitch)),
					alteredList.get(baseList.indexOf(higherPitch)));
			if (singleClef == true)
				clefMask = appPrefs.CLEF_C4;
		}

		notesTypeList.clear();
		int nType = Integer.parseInt(appPrefs.getProperty("wholeNote"));
		if (nType == -1 || nType == 1)
			notesTypeList.add(0);
		nType = Integer.parseInt(appPrefs.getProperty("halfNote"));
		if (nType == -1 || nType == 1)
			notesTypeList.add(1);
		if (Integer.parseInt(appPrefs.getProperty("quarterNote")) == 1)
			notesTypeList.add(2);
		if (Integer.parseInt(appPrefs.getProperty("eighthNote")) == 1)
			notesTypeList.add(3);
		if (Integer.parseInt(appPrefs.getProperty("tripletNote")) == 1)
			notesTypeList.add(4);
		if (Integer.parseInt(appPrefs.getProperty("3_4_Note")) == 1)
			notesTypeList.add(6);
		if (Integer.parseInt(appPrefs.getProperty("3_8_Note")) == 1)
			notesTypeList.add(7);
		nType = Integer.parseInt(appPrefs.getProperty("silenceNote"));
		if (nType == -1 || nType == 1)
			notesTypeList.add(5);

		int tsIdx = Integer.parseInt(appPrefs.getProperty("timeSignature"));
		if (tsIdx <= 0) {
			timeSignNumerator = 4;
			timeSignDenominator = 4;
		} else if (tsIdx == 1) {
			timeSignNumerator = 2;
			timeSignDenominator = 4;
		} else if (tsIdx == 2) {
			timeSignNumerator = 3;
			timeSignDenominator = 4;
		} else if (tsIdx == 3) {
			timeSignNumerator = 6;
			timeSignDenominator = 8;
		} else if (tsIdx == 4) {
			timeSignNumerator = 6;
			timeSignDenominator = 4;
		} else if (tsIdx == 5) {
			timeSignNumerator = 3;
			timeSignDenominator = 8;
		}
	}

	/**
	 * Sets the notes list.
	 *
	 * @param n
	 *            the n
	 * @param n2
	 *            the n2
	 * @param random
	 *            the random
	 */
	public void setNotesList(Vector<Note> n, Vector<Note> n2, boolean random) {
		int idx = 0, idx2 = 0;
		boolean done = false;
		while (!done) {
			double ts = -1, ts2 = -1;
			if (idx == n.size() && idx2 == n2.size()) {
				done = true;
				continue;
			}
			if (idx < n.size()) {
				if (n.get(idx).type == 5) {
					idx++;
					continue;
				}
				ts = n.get(idx).timestamp;
				ts2 = 999999;
			}
			if (idx2 < n2.size()) {
				if (n2.get(idx2).type == 5) {
					idx2++;
					continue;
				}
				ts2 = n2.get(idx2).timestamp;
				if (ts == -1)
					ts = 999999;
			}
			logger.debug("[NG setNotesList] idx: " + idx + " (ts=" + ts
					+ ") idx2: " + idx2 + " (ts2=" + ts2 + ")");
			if (ts <= ts2) {
				randomPitchList.add(n.get(idx));
				idx++;
			} else {
				randomPitchList.add(n2.get(idx2));
				idx2++;
			}
		}

		randomEnabled = random;
		notesListIndex = 0;
	}

	/**
	 * Gets the clef mask.
	 *
	 * @return the clef mask
	 */
	public int getClefMask() {
		return clefMask;
	}

	/**
	 * Gets the level from clef and pitch.
	 *
	 * @param clef
	 *            the clef
	 * @param pitch
	 *            the pitch
	 * @return the level from clef and pitch
	 */
	private int getLevelFromClefAndPitch(int clef, int pitch) {
		if (clef == appPrefs.CLEF_G2)
			return 24 - getIndexFromPitch(CLEF_G2_BASEPITCH, pitch, true);
		else if (clef == appPrefs.CLEF_F4)
			return 24 - getIndexFromPitch(CLEF_F4_BASEPITCH, pitch, true);
		else if (clef == appPrefs.CLEF_C3)
			return 24 - getIndexFromPitch(CLEF_C3_BASEPITCH, pitch, true);
		else if (clef == appPrefs.CLEF_C4)
			return 24 - getIndexFromPitch(CLEF_C4_BASEPITCH, pitch, true);

		return 0;
	}

	/**
	 * Gets the pitch from clef and level. Remember that this function returns
	 * the base pitch of a note !!!
	 *
	 * @param clef
	 *            the clef
	 * @param level
	 *            the level
	 * @return the base pitch from clef and level
	 */
	public int getPitchFromClefAndLevel(int clef, int level) {
		if (clef == appPrefs.CLEF_G2)
			return getPitchFromLevel(CLEF_G2_BASEPITCH, 24 - level);
		else if (clef == appPrefs.CLEF_F4)
			return getPitchFromLevel(CLEF_F4_BASEPITCH, 24 - level);
		else if (clef == appPrefs.CLEF_C3)
			return getPitchFromLevel(CLEF_C3_BASEPITCH, 24 - level);
		else if (clef == appPrefs.CLEF_C4)
			return getPitchFromLevel(CLEF_C4_BASEPITCH, 24 - level);

		return 0;
	}

	/**
	 * Adds the range. add notes to randomPitchList. lower and upper pitches
	 * must be NOT alterated
	 *
	 * @param clef
	 *            the clef
	 * @param lower
	 *            the lower
	 * @param upper
	 *            the upper
	 */
	public void addRange(int clef, int lower, int upper) {
		// int altIndex = this.accidentals.getNumber();
		// String accType = this.accidentals.getType();
		int lowIdx = alteredList.indexOf(lower);
		int highIdx = alteredList.indexOf(upper);
		boolean secondRow = false;

		// find the index on sharpsMatrix/flatsMatrix to add correctly alterated
		// notes
		int matrixIdx = lowIdx % 7;

		logger.debug("[NG addRange] clef: " + clef + ", lower: " + lower
				+ ", upper: " + upper);

		if (randomPitchList.size() == 0)
			baseRangeClef = clef;
		else {
			if (singleClef == true)
				return;
			addRangeClef = clef;
			addRangeIndex = randomPitchList.size();
			secondRow = true;
		}

		for (int i = lowIdx; i < highIdx + 1; i++) {
			int pitch = alteredList.get(i);
			// int altType = pitch - baseList.get(i);

			int level = getLevelFromClefAndPitch(clef, pitch);

			Note tmpNote = new Note(0, clef, level, pitch, 0, secondRow, 0 /* altType */);
			randomPitchList.add(tmpNote);

			if (matrixIdx == 6)
				matrixIdx = 0;
			else
				matrixIdx++;
		}

		for (int n = 0; n < randomPitchList.size(); n++)
			logger.debug(randomPitchList.get(n).pitch + ", ");
		logger.debug("");
	}

	/**
	 * Gets the notes number.
	 *
	 * @return the notes number
	 */
	public int getNotesNumber() {
		return randomPitchList.size();
	}

	/**
	 * Gets the first low pitch.
	 *
	 * @return the first low pitch
	 */
	public int getFirstLowPitch() {
		if (randomPitchList.size() == 0)
			return -1;
		return randomPitchList.get(0).pitch;
	}

	/**
	 * Gets the first high pitch.
	 *
	 * @return the first high pitch
	 */
	public int getFirstHighPitch() {
		if (randomPitchList.size() == 0)
			return -1;
		if (addRangeIndex != -1)
			return randomPitchList.get(addRangeIndex - 1).pitch;
		else
			return randomPitchList.get(randomPitchList.size() - 1).pitch;
	}

	/**
	 * Gets the second low pitch.
	 *
	 * @return the second low pitch
	 */
	public int getSecondLowPitch() {
		if (randomPitchList.size() == 0 || addRangeIndex == -1)
			return -1;
		return randomPitchList.get(addRangeIndex).pitch;
	}

	/**
	 * Gets the second high pitch.
	 *
	 * @return the second high pitch
	 */
	public int getSecondHighPitch() {
		if (randomPitchList.size() == 0 || addRangeIndex == -1)
			return -1;
		return randomPitchList.get(randomPitchList.size() - 1).pitch;
	}

	/**
	 * Gets the alteration.
	 *
	 * @param pitch
	 *            the pitch
	 * @return the alteration
	 */
	public int getAlteration(int pitch) {
		int idx = alteredList.indexOf(pitch);
		if (idx == -1)
			return 0;
		return alteredList.get(idx) - baseList.get(idx);
	}

	/**
	 * Gets the altered from base.
	 *
	 * @param basePitch
	 *            the base pitch
	 * @return the altered from base
	 */
	public int getAlteredFromBase(int basePitch) {
		int baseNoteIdx = baseList.indexOf(basePitch);
		if (baseNoteIdx == -1)
			return -1;
		return alteredList.get(baseNoteIdx);
	}

	/**
	 * Gets the rhythm pitch.
	 *
	 * @param clef
	 *            the clef
	 * @return the rhythm pitch
	 */
	public int getRhythmPitch(int clef) {
		if (clef == appPrefs.CLEF_G2)
			return baseList.get(baseList.indexOf(CLEF_G2_BASEPITCH) + 12);
		else if (clef == appPrefs.CLEF_F4)
			return baseList.get(baseList.indexOf(CLEF_F4_BASEPITCH) + 12);
		else if (clef == appPrefs.CLEF_C3)
			return baseList.get(baseList.indexOf(CLEF_C3_BASEPITCH) + 12);
		else if (clef == appPrefs.CLEF_C4)
			return baseList.get(baseList.indexOf(CLEF_C4_BASEPITCH) + 12);

		return 71;
	}

	/**
	 * Gets the random note.
	 *
	 * @param forcedType
	 *            the forced type
	 * @param altered
	 *            the altered
	 * @param forcedClef
	 *            the forced clef
	 * @return the random note
	 */
	public Note getRandomNote(int forcedType, boolean altered, int forcedClef) {
		int randIdx = 0;
		if (randomEnabled == true) {
			if (forcedClef == -1 || addRangeIndex == -1)
				randIdx = (int) (randomPitchList.size() * Math.random());
			else {
				if (forcedClef == 1)
					randIdx = (int) (addRangeIndex * Math.random());
				else if (forcedClef == 2)
					randIdx = (int) ((randomPitchList.size() - addRangeIndex) * Math
							.random()) + addRangeIndex;
			}
		} else {
			randIdx = notesListIndex;
			notesListIndex++;
			if (notesListIndex == randomPitchList.size()) // wrap up
				notesListIndex = 0;
		}

		Note tmpNote = randomPitchList.get(randIdx);
		int type = forcedType;
		if (forcedType == -1)
			type = notesTypeList.get((int) (notesTypeList.size() * Math
					.random()));
		Note randNote = new Note(0, tmpNote.clef, tmpNote.level, tmpNote.pitch,
				type, tmpNote.secondRow, tmpNote.altType);
		randNote.type = type;
		randNote.duration = randNote.getDuration(type);

		if (altered == true) {
			int randAlt = (int) (Math.random() * 3) - 1; // get a value between
															// -1 and +1
			int altIdx = alteredList.indexOf(randNote.pitch);
			int altOffset = alteredList.get(altIdx) - baseList.get(altIdx);

			// logger.debug("[NG getRandomNote] pitch: " + randNote.pitch +
			// ", randAlt: " + randAlt + ", altOffset: " + altOffset);
			randNote.pitch += randAlt;

			switch (altOffset) {
			case 0:
				randNote.altType = randAlt;
				break;
			case -1:
				if (randAlt == 1)
					randNote.altType = 2; // natural needed
				else if (randAlt == -1)
					randNote.level++;
				break;
			case 1:
				if (randAlt == -1)
					randNote.altType = 2; // natural needed
				else if (randAlt == 1)
					randNote.level--;
				break;
			}
		}

		// logger.debug("[NG getRandomNote] clef: " + baseRangeClef +
		// ", addRangeIndex: " + addRangeIndex);
		// logger.debug("selClef: " + selClef + ", level: " + level);
		// logger.debug("New random pitch = " + pitch);

		return randNote;
	}

	/**
	 * Gets the triplet random note.
	 *
	 * @param basePitch
	 *            the base pitch
	 * @return the triplet random note
	 */
	public Note getTripletRandomNote(int basePitch) {
		int baseIndex = 0;
		int lowerIndex = 0;
		int upperIndex = randomPitchList.size();
		int delta = 4; // within +2 and -2 tones from basePitch
		int shift = -2; // index shift from base note

		for (int i = 0; i < upperIndex; i++) {
			if (randomPitchList.get(i).pitch == basePitch) {
				baseIndex = i;
				break;
			}
		}

		if (addRangeIndex != -1) {
			if (baseIndex >= addRangeIndex) // triplet base note is on the
											// second range
				lowerIndex = addRangeIndex;
			else
				upperIndex = addRangeIndex - 1; // triplet base note is on the
												// first range
		}
		if (baseIndex - lowerIndex < 2)
			shift += (2 - (baseIndex - lowerIndex));
		if (upperIndex - baseIndex < 2)
			delta -= (upperIndex - baseIndex);
		int randIndex = baseIndex + shift + (int) (Math.random() * delta);
		Note baseNote = randomPitchList.get(baseIndex);
		Note newNote = randomPitchList.get(randIndex);
		Note randNote = new Note(0, baseNote.clef, newNote.level,
				newNote.pitch, 4, baseNote.secondRow, newNote.altType);

		logger.debug("Triplet base: " + basePitch + ", baseIndex: " + baseIndex
				+ ", randIdx: " + randIndex);
		logger.debug("Triplet new note: " + randNote.pitch + ", level: "
				+ randNote.level + ", dur: " + randNote.duration);

		return randNote;
	}

	/**
	 * Gets the random sequence.
	 *
	 * @param seq
	 *            the seq
	 * @param measuresNumber
	 *            the measures number
	 * @param isRhythm
	 *            the is rhythm
	 * @param forcedClef
	 *            the forced clef
	 * @return the random sequence
	 */
	public void getRandomSequence(Vector<Note> seq, int measuresNumber,
			boolean isRhythm, int forcedClef) {
		double measureCounter = 0;
		double timeCounter = 0;
		boolean eighthPresent = false;
		seq.clear(); // clear sequence in case it was already set
		for (int i = 0; i < measuresNumber; i++) {
			measureCounter = timeSignNumerator;
			if (timeSignDenominator == 8)
				measureCounter /= 2;
			eighthPresent = false;
			while (measureCounter != 0) {
				Note tmpNote = getRandomNote(-1, false, forcedClef);
				if (tmpNote.type == 4) // triplet
				{
					if (measureCounter < 1 || eighthPresent == true)
						continue;
					Note secondNote = getTripletRandomNote(tmpNote.pitch);
					Note thirdNote = getTripletRandomNote(tmpNote.pitch);
					if (isRhythm == true) {
						tmpNote.level = secondNote.level = thirdNote.level = 12;
						tmpNote.pitch = secondNote.pitch = thirdNote.pitch = getRhythmPitch(tmpNote.clef);
					}

					int tripletLevel = tmpNote.level;
					int mult = 1;
					if (isRhythm == false) {
						if (tmpNote.level >= 12) // oriented upward. Find the
													// lowest level
						{
							if (secondNote.level < tripletLevel)
								tripletLevel = secondNote.level;
							if (thirdNote.level < tripletLevel)
								tripletLevel = thirdNote.level;
						} else // oriented downward. Find the highest level
						{
							if (secondNote.level > tripletLevel)
								tripletLevel = secondNote.level;
							if (thirdNote.level > tripletLevel)
								tripletLevel = thirdNote.level;
							mult = -1; // negative values will tell the renderer
										// to stretch downward
						}
						tripletLevel *= mult;
					}

					tmpNote.setTripletValue(tripletLevel);
					secondNote.setTripletValue(tripletLevel + (mult * 1000));
					thirdNote.setTripletValue(tripletLevel + (mult * 1000));

					tmpNote.setTimeStamp(timeCounter);
					timeCounter += tmpNote.duration;
					secondNote.setTimeStamp(timeCounter);
					timeCounter += secondNote.duration;
					thirdNote.setTimeStamp(timeCounter);
					timeCounter += thirdNote.duration;

					measureCounter--; // one quarter for the triplet group
					seq.add(tmpNote);
					logger.debug("Random Note: #" + seq.size() + ": Pitch: "
							+ tmpNote.pitch + ", level: " + tmpNote.level);
					seq.add(secondNote);
					logger.debug("Random Note: #" + seq.size() + ": Pitch: "
							+ secondNote.pitch + ", level: " + secondNote.level);
					seq.add(thirdNote);
					logger.debug("Random Note: #" + seq.size() + ": Pitch: "
							+ thirdNote.pitch + ", level: " + thirdNote.level);
					continue;

				} else if (tmpNote.type == 5) // if this is silence, then set a
												// random duration
				{
					int randTypeIdx = (int) ((notesTypeList.size() - 1) * Math
							.random());
					if (notesTypeList.get(randTypeIdx) == 6
							|| notesTypeList.get(randTypeIdx) == 7)
						continue;
					tmpNote.duration = tmpNote.getDuration(notesTypeList
							.get(randTypeIdx));
					if (tmpNote.duration == (1.0 / 3.0))
						continue;
				}

				if (isRhythm == true) {
					tmpNote.level = 12;
					// tmpNote.ypos = 62;
					tmpNote.pitch = getRhythmPitch(tmpNote.clef);
				}

				logger.debug("Generated note pitch: " + tmpNote.pitch
						+ ", duration: " + tmpNote.duration);
				if (tmpNote.duration <= measureCounter) {
					measureCounter -= tmpNote.duration;
					seq.add(tmpNote);
					if (tmpNote.type == 3)
						eighthPresent = true;
					tmpNote.setTimeStamp(timeCounter);
					logger.debug("Random Note: #" + seq.size() + ": p: "
							+ tmpNote.pitch + ", lev: " + tmpNote.level
							+ ", type: " + tmpNote.type + ", ts: "
							+ timeCounter);
					timeCounter += tmpNote.duration;
				}
				logger.debug("tempMesCnt: " + measureCounter);
			}
		}
	}

	/**
	 * Gets the random chord or interval.
	 *
	 * @param seq
	 *            the seq
	 * @param xpos
	 *            the xpos
	 * @param chord
	 *            the chord
	 * @param intervalDegree
	 *            the interval degree
	 * @return the random chord or interval
	 */
	public int getRandomChordOrInterval(Vector<Note> seq, int xpos,
			boolean chord, int intervalDegree) {
		char notesWanted = 1;
		// int randType = (int)(Math.random() * 4); // 0 major, 1 minor, 2
		// diminished, 3 augmented
		int randType = 0;
		if (chord == true)
			randType = (int) (Math.random() * 2); // 0 major, 1 minor
		else {
			if (intervalDegree == 4 || intervalDegree == 5
					|| intervalDegree == 8)
				randType = intervalPerfect[(int) (Math.random() * 3)]; // interval
																		// without
																		// perfect
			else
				randType = intervalNoPerfect[(int) (Math.random() * 4)]; // interval
																			// with
																			// perfect
		}

		int[] addNotes = { 0, 0 };
		Note baseNote = getRandomNote(0, false, -1);

		logger.debug("[getRandomChordorInterval] randType: " + randType);

		baseNote.xpos = xpos;
		seq.add(baseNote);

		if (chord == true)
			notesWanted = 2;

		for (int i = 0; i < notesWanted; i++) {
			if (chord == true)
				addNotes[i] = baseNote.pitch + chordsIntervals[randType][i];
			else {
				addNotes[i] = baseNote.pitch + intervals[intervalDegree]
						+ randType;
			}
			int level = 0;
			if (chord == true)
				level = baseNote.level - (2 * (i + 1)); // calculate 3rd and 5th
														// position
			else
				level = baseNote.level - intervalDegree + 1;
			int addNoteBasePitch = getPitchFromClefAndLevel(baseNote.clef,
					level);
			int addNoteIdx = baseList.indexOf(addNoteBasePitch);
			int altOnClef = alteredList.get(addNoteIdx)
					- baseList.get(addNoteIdx);
			int altType = addNotes[i] - alteredList.get(addNoteIdx);
			logger.debug("BEFORE altType: " + altType + ", altOnClef: "
					+ altOnClef);
			if (altType != 0 && altOnClef != 0) {
				if (altType + altOnClef == 0)
					altType = 2; // there is an accidental on key. Must show a
									// natural
				else
					altType += altOnClef;
			}

			logger.debug("addNotes: " + addNotes[i] + ", idx: " + addNoteIdx
					+ ", lev: " + level);
			logger.debug("AFTER altType: " + altType + ", altOnClef: "
					+ altOnClef);
			if (chord == false && intervalDegree == 2)
				xpos -= 20;
			Note newNote = new Note(xpos, baseNote.clef, level, addNotes[i], 0,
					baseNote.secondRow, altType);
			seq.add(newNote);
		}

		return randType;
	}
}

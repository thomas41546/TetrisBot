package tetris;

import java.awt.Color;
import java.awt.Point;

/**
 * A class to play the tetris games at TetrisFriends.com
 */

// Red (Z)

// Blue (L)

// Yellow (O)

// Purple (T)

// Green(Z)

// Orange (L)

// light blue (I)

public class TetrisFriendsPlayer extends TetrisAIPlayer {

	public TetrisFriendsPlayer() {
		Color[] temp = new Color[7];
		/*
		 * temp[0] = new Color(63,13,23); //Z temp[1] = new Color(14,24,59);
		 * //_| temp[2] = new Color(64,51,12); //[] temp[3] = new
		 * Color(47,11,41); // T temp[4] = new Color(28,55,9); //S temp[5] = new
		 * Color(64,33,10); //L temp[6] = new Color(12,45,58); // |
		 */

		temp[0] = new Color(58, 21, 25); // Z
		temp[1] = new Color(14, 27, 58); // _|
		temp[2] = new Color(62, 50, 17); // []
		temp[3] = new Color(48, 21, 46); // T
		temp[4] = new Color(34, 51, 12); // S
		temp[5] = new Color(60, 35, 15); // L
		temp[6] = new Color(20, 45, 58); // |

		this.pieces = temp;
		String[] tempStrings = { "red", "blue", "yellow", "purple", "green",
				"orange", "lightblue" };
		this.pieceStrings = tempStrings;
		this.WAIT_TIME = 0;
		this.KEY_TIME = 150;
		this.tetris = new Tetris();
	}

	public TetrisFriendsPlayer(double aiConstants[]) {
		this();
		this.tetris = new Tetris(aiConstants);
	}
}

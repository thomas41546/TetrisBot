package tetris;

import java.util.Random;

/**
 * A class for a Tetris AI backend.
 * 
 * @author mellort
 * 
 */
public class TetrisAI {

	// the time to wait between dropping pieces
	public int WAIT_TIME;

	// constants for the ai
	public double[] AI_CONSTANTS;

	/**
	 * A constructor for the class, sets the wait time to be 0.
	 */
	public TetrisAI() {
		this.WAIT_TIME = 0;
	}

	/**
	 * A constructor for the class, sets the wait time to be wait_time.
	 */
	public TetrisAI(int wait_time) {
		this.WAIT_TIME = wait_time;
	}

	/**
	 * A constructor for the class, sets the constants for the AI scoring and
	 * sets the wait time to be 0.
	 */
	public TetrisAI(double[] aiConstants) {
		this.WAIT_TIME = 0;
		this.AI_CONSTANTS = aiConstants;
	}

	/**
	 * A method to play the game. Creates random pieces and adds them to the
	 * board in the best position, until the game ends.
	 * 
	 * @return the number of pieces played
	 */
	public int playGame() throws Exception {
		Tetris t;
		// create a new tetris game
		if (this.AI_CONSTANTS != null && this.AI_CONSTANTS.length > 0)
			t = new Tetris(this.AI_CONSTANTS);
		else
			t = new Tetris();
		// set piece counter to 0
		int pieces = 0;
		// random number generator
		Random rnd = new Random();

		// while game isn't over
		while (!t.over) {
			// add piece, make the move, and remove tetrises
			t.addPiece(rnd.nextInt(7));
			t.makeMove();
			t.tetrisify();

			// show the board
			t.printBoard();

			// print information
			System.out.println("pieces: " + pieces);
			System.out.println("tetrises: " + t.tetrises);

			// increment pieces
			pieces++;

			// sleep
			Thread.sleep(WAIT_TIME);
		}

		// return the number of pieces
		return pieces;
	}

}

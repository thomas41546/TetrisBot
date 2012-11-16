package tetris;

import java.awt.Dimension;
import java.awt.Point;
import java.util.*;

/**
 * A class to handle the Tetris game backend.
 * 
 * @author mellort
 * 
 */
public class Tetris {

	/**
	 * A Dimension for the board dimensions. Note, we use 10x25 even though the
	 * board is actually 10x19 so we can hav a vertical buffer to rotate pieces
	 * which are at the top of the board. Note, THe game still ends if you have
	 * a piece above the 19th row.
	 */
	final static Dimension BOARD_DIMENSIONS = new Dimension(10, 25);
	/**
	 * How much vertical buffer there is between the top of the board dimension
	 * and the actual board size.
	 */
	final static int VERTICAL_BUFFER = 6;
	/**
	 * Where each piece is dropped from.
	 */
	final Point DROP_POINT = new Point(4, 18);
	/**
	 * The game board
	 */
	int[][] board;
	/**
	 * The current piece that is in play
	 */
	Piece currentPiece;
	/**
	 * The number of tetrises that have occurred this game
	 */
	int tetrises;
	/**
	 * Whether or not the game is over
	 */
	public boolean over;
	/**
	 * Constants for the AI
	 */
	public double[] AI_CONSTANTS;

	/**
	 * A constructor for the class. THis sets the board.
	 */
	public Tetris() {
		board = new int[Tetris.BOARD_DIMENSIONS.height][Tetris.BOARD_DIMENSIONS.width];
		// fill board with zeroes
		initializeBoard();
		// initialize tetrises
		this.tetrises = 0;
		this.over = false;
	}

	/**
	 * If passed in AI constants, store them after initializing the board.
	 * 
	 * @param aiConstants
	 *            the AI constants
	 */
	public Tetris(double[] aiConstants) {
		this();
		this.AI_CONSTANTS = aiConstants;
	}

	/**
	 * Add a piece to the board of the given type. If we have AI constants, use
	 * them to create the piece
	 * 
	 * @param type
	 *            the type of the piece.
	 */
	public void addPiece(int type) {
		if (this.AI_CONSTANTS == null || this.AI_CONSTANTS.length == 0)
			currentPiece = new Piece(type, this.DROP_POINT);
		else
			currentPiece = new Piece(type, this.DROP_POINT, AI_CONSTANTS);
	}

	/**
	 * Move the current piece right.
	 */
	public void right() {
		currentPiece.right();
	}

	/**
	 * Move the current piece left
	 */
	public void left() {
		currentPiece.left();
	}

	/**
	 * Add the current Piece to the board
	 */
	public void drop() {
		currentPiece.add(board);
	}

	/**
	 * Rotate the current piece.
	 */
	public void rotate() {
		currentPiece.rotate();
	}

	/**
	 * Rotate the current piece by i * 90 degrees clockwise.
	 * 
	 * @param i
	 *            the multiplier
	 */
	public void rotate(int i) {
		currentPiece.rotateBy(i);
	}

	/**
	 * Fill the board with zeroes
	 */
	public void initializeBoard() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = 0;
			}
		}
	}

	/**
	 * A class to hold possible move Options.
	 * 
	 * @author mellort
	 */
	public class Option implements Comparable {
		/**
		 * Store the point of the piece, the rotation, and the piece score.
		 */
		Point point;
		int rot;
		double score;

		/**
		 * Constructor
		 * 
		 * @param point
		 *            the point
		 * @param rot
		 *            the rotation
		 * @param score
		 *            the score
		 */
		public Option(Point point, int rot, double score) {
			this.point = point;
			this.rot = rot;
			this.score = score - point.y;
		}

		/**
		 * Compare to another option. Just compares their scores.
		 */
		public int compareTo(Object o) {
			Option option = (Option) o;
			return (new Double(this.score))
					.compareTo((new Double(option.score)));
		}

		/**
		 * A toString for the option (for debugging).
		 */
		public String toString() {
			String output = "";
			output += "Option:\n";
			output += "\tPoint: " + this.point.toString() + "\n";
			output += "\tRotation: " + this.rot + "\n";
			output += "\tScore: " + this.score;

			return output;
		}
	}

	/**
	 * Print the board.
	 */
	public void printBoard() {
		for (int y = board.length - 1; y >= 0; y--) {
			for (int x = 0; x < board[y].length; x++) {
				if (board[y][x] == 0) {

					if (y == 19) {
						System.out.print("_");
					} else {
						System.out.print(" ");
					}
				} else {
					System.out.print("1");
				}
			}
			System.out.println("|");
		}
	}

	/**
	 * A method to find the best next move and then make it. It goes through all
	 * possible moves and scores them, and then sorts that list.
	 * 
	 * 
	 * @return an array with the displacement of the distance and the rotation
	 *         of the current Piece to move it to the best location.
	 */
	@SuppressWarnings("unchecked")
	public int[] makeMove() {
		// an array list of the moves
		ArrayList<Option> moves = new ArrayList<Option>();

		int currentType = this.currentPiece.type;

		/*
		 * Drop a piece of this type at every possible location on the board.
		 */
		for (int i = 0; i < board[0].length; i++) {
			for (int r = 0; r < 4; r++) {
				// create the point
				Piece newPiece = new Piece(currentType, new Point(i, 18),
						this.AI_CONSTANTS);

				double score = 0;

				// move to the location, rotate, and drop
				newPiece.rotateTo(r);
				newPiece.drop(board);

				// if the piece is on the board, then store it
				if (newPiece.isOnBoard(10, 19)) {
					// set score
					score += newPiece.score(board);
					// add the option to th emoves list
					moves.add(new Option(new Point(i, 18), r, score));
				} else {
					// piece is off board, we don't care about it
				}
			}
		}

		// sort the moves, last element will ahve highest score, like we want
		Collections.sort(moves);

		// if we can get a move, get it
		if (moves.size() > 0) {
			// get best move
			Option best = moves.get(moves.size() - 1);
			placePiece(best.point, best.rot);
			// return array of displacement for distance and rotation
			int[] returnArray = { 4 - best.point.x, best.rot };
			return returnArray;
			// else, no move on board, just end game
		} else {
			// System.out.println("hit");
			this.over = true;
			return null;
		}

	}

	/**
	 * Set the current piece at a given location and add it to the board.
	 * 
	 * @param point
	 *            the specified point
	 * @param rot
	 *            the specified rotation
	 */
	public void placePiece(Point point, int rot) {
		currentPiece.center = point;
		currentPiece.rotateTo(rot);
		currentPiece.add(board);
	}

	/**
	 * Remove tetrises from the board, move all other blocks down, and t hen
	 * increment the tetris counter.
	 */
	public void tetrisify() {
		int linesCleared = 0;

		for (int row = 0; row < board.length; row++) {
			int sum = 0;

			for (int col = 0; col < board[row].length; col++) {
				sum += board[row][col];
			}

			if (sum == 0) {
				continue;
			} else if (sum == board[row].length) { // ie 10

				// need to clear line
				for (int col = 0; col < board[row].length; col++) {
					board[row][col] = 0;
				}

				// note
				linesCleared++;

				// move everything down
				for (int row2 = row + 1; row2 < board.length; row2++) {
					for (int col = 0; col < board[row].length; col++) {
						board[row2 - 1][col] = board[row2][col];
					}
				}

				// re check current row
				row--;
			}

		}

		tetrises += linesCleared;
	}

}

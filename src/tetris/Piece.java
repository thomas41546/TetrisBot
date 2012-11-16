package tetris;

import java.awt.Point;

/**
 * A class to store Tetris pieces.
 * 
 * @author mellort
 */
public class Piece {

	/**
	 * This is a huge matrix of all of the possible pieces and their rotations.
	 * This is used in the constructor to create the appropriate piece.
	 * 
	 * A given entry of this matrix contains all of the possible rotations of a
	 * given piece. Moreover, the entries within this are in order of 90 degree
	 * clockwise rotations.
	 * 
	 * So, once we have an entry, we can iterate through the rotations by just
	 * incrementing the index.
	 * 
	 * The point entries themselves relate to the relative position of a point
	 * to the center of the piece.
	 */
	Point[][][] allRotations = {
			{
					// Red (Z)
					{ new Point(1, 0), new Point(0, 0), new Point(0, 1),
							new Point(-1, 1) },
					{ new Point(0, -1), new Point(0, 0), new Point(1, 0),
							new Point(1, 1) },
					{ new Point(-1, 0), new Point(0, 0), new Point(0, -1),
							new Point(1, -1) },
					{ new Point(0, 1), new Point(0, 0), new Point(-1, 0),
							new Point(-1, -1) } },

			{
					// Blue (L)
					{ new Point(-1, 0), new Point(0, 0), new Point(-1, 1),
							new Point(1, 0) },
					{ new Point(0, 1), new Point(0, 0), new Point(1, 1),
							new Point(0, -1) },
					{ new Point(1, 0), new Point(0, 0), new Point(-1, 0),
							new Point(1, -1) },
					{ new Point(0, -1), new Point(0, 0), new Point(-1, -1),
							new Point(0, 1) } },

			{
					// Yellow (O)
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1),
							new Point(0, 1) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1),
							new Point(0, 1) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1),
							new Point(0, 1) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1),
							new Point(0, 1) } },

			{
					// Purple (T)
					{ new Point(0, 0), new Point(-1, 0), new Point(1, 0),
							new Point(0, 1) },
					{ new Point(0, 0), new Point(0, -1), new Point(1, 0),
							new Point(0, 1) },
					{ new Point(0, 0), new Point(-1, 0), new Point(1, 0),
							new Point(0, -1) },
					{ new Point(0, 0), new Point(0, 1), new Point(0, -1),
							new Point(-1, 0) } },

			{
					// Green(Z)
					{ new Point(-1, 0), new Point(0, 0), new Point(0, 1),
							new Point(1, 1) },
					{ new Point(0, 1), new Point(0, 0), new Point(1, 0),
							new Point(1, -1) },
					{ new Point(1, 0), new Point(0, 0), new Point(0, -1),
							new Point(-1, -1) },
					{ new Point(0, -1), new Point(0, 0), new Point(-1, 0),
							new Point(-1, 1) } },

			{
					// Orange (L)
					{ new Point(1, 0), new Point(0, 0), new Point(1, 1),
							new Point(-1, 0) },
					{ new Point(0, -1), new Point(0, 0), new Point(1, -1),
							new Point(0, 1) },
					{ new Point(-1, 0), new Point(0, 0), new Point(1, 0),
							new Point(-1, -1) },
					{ new Point(0, 1), new Point(0, 0), new Point(-1, 1),
							new Point(0, -1) } },

			{
					// light blue (I)
					{ new Point(-1, 0), new Point(0, 0), new Point(1, 0),
							new Point(2, 0) },
					{ new Point(1, -1), new Point(1, 0), new Point(1, 1),
							new Point(1, 2) },
					{ new Point(-1, 1), new Point(0, 1), new Point(1, 1),
							new Point(2, 1) },
					{ new Point(0, -1), new Point(0, 0), new Point(0, 1),
							new Point(0, 2) } } };

	/**
	 * Some variables the piece uses.
	 */

	/** The center is the center point of the piece. */
	Point center;
	/**
	 * The rotations array is the array of all of the possible rotations of THIS
	 * specific piece.
	 */
	Point[][] rotations;
	/**
	 * The currentRotation variable stores the index of the piece formation
	 * currently being used.
	 */
	int currentRotation;
	/**
	 * The points array just stores the current configuration of the points.
	 * This is equivalent to taking the currentRotation index element of the
	 * rotations array.
	 */
	Point[] points;
	/**
	 * The type variable determines what kind of piece this piece is. It can be
	 * an integer from 0-6. The numbers correspond to pieces as follows:
	 * 
	 * {"red"","blue","yellow","purple","green","orange","lightblue"} {0, 1, 2,
	 * 3, 4, 5, 6}
	 */
	int type;
	/**
	 * Constants for the AI scoring algorithm
	 */
	double[] AI_CONSTANTS;

	/**
	 * The constructor for the piece. It takes in a piece type as an integer and
	 * a Point corresponding to the center of the piece.
	 * 
	 * @param type
	 *            of the piece as an int
	 * @param center
	 *            the center of the piece as a Point
	 */
	public Piece(int type, Point center) {
		// set the rotations array and the currentRotation
		Point[][] temp = this.allRotations[type];
		this.rotations = temp;
		this.currentRotation = 0;

		// set the points array
		this.points = rotations[this.currentRotation];

		// set the type of the piece
		this.type = type;
		// set the center of the piece
		this.center = center;
		// set the default AI_constants
		double[] tempArray = { 2.0, 3.0, 5.0, 10.0 };
		this.AI_CONSTANTS = tempArray;
	}

	/**
	 * A separate constructor for passing in AI constants
	 * 
	 * @param type
	 *            the type of the piece
	 * @param center
	 *            the center of the piece
	 * @param aiConstants
	 *            the AI constants to use
	 */
	public Piece(int type, Point center, double[] aiConstants) {

		this(type, center);
		this.AI_CONSTANTS = aiConstants;

	}

	/**
	 * A method to determine whether or not the current piece is on the Tetris
	 * board. It grabs the dimensions of the board from the Tetris class. Note
	 * that we subtract some vertical buffer in the check because the actual
	 * board has a little bit higher height so as to handle rotations when the
	 * piece is at the top of the board.
	 * 
	 * The method goes through each point of the piece and makes sure it is
	 * within the dimensions.
	 * 
	 * FIXME removed buffer, add to doc FIXME update doc for overloading
	 * 
	 * @return whether or not this current piece is completely on the board, as
	 *         a boolean
	 */
	public boolean isOnBoard() {
		return this.isOnBoard(Tetris.BOARD_DIMENSIONS.width,
				Tetris.BOARD_DIMENSIONS.height);
	}

	public boolean isOnBoard(int DIM_X, int DIM_Y) {
		// store whether or not it is on the board
		boolean answer = true;
		// go through each point, breaking if answer is false
		for (int i = 0; i < 4 && answer; i++) {
			// store the actual x,y point of the piece.
			// note we have to add the points x,y to the center
			// for this.
			int pointX = this.center.x + points[i].x;
			int pointY = this.center.y + points[i].y;

			// perform check
			if ((pointX >= 0 && pointX < DIM_X)
					&& (pointY >= 0 && pointY < DIM_Y)) {
				// point is on board
			} else {
				answer = false;
			}
		}
		// return the result
		return answer;
	}

	/**
	 * A method to determine whether or not the piece can move right. It does
	 * this in a bit of a cheap way: it moves the piece right and checks if it
	 * is on the board, and then it just moves it back.
	 * 
	 * @return whether or not the piece can move right, as a boolean
	 */
	public boolean canMoveRight() {
		// move piece right
		this.center.x++;
		boolean result = false;
		// if the moved piece is on the board
		// note that it can be moved
		if (this.isOnBoard()) {
			result = true;
		}
		this.center.x--;
		return result;
	}

	/**
	 * A method to determine whether or not the piece can move left. It does
	 * this in a bit of a cheap way: it moves the piece left and checks if it is
	 * on the board, and then it just moves it back.
	 * 
	 * @return whether or not the piece can move left, as a boolean
	 */
	public boolean canMoveLeft() {
		// move piece left
		this.center.x--;
		boolean result = false;
		// if the moved piece is on the board
		// note that it can be moved
		if (this.isOnBoard()) {
			result = true;
		}
		this.center.x++;
		return result;
	}

	/**
	 * A method to move the piece right. It first checks to see if it can move
	 * right, and if it can, then it does.
	 */
	public void right() {
		if (canMoveRight()) {
			this.center.x++;
		}
	}

	/**
	 * A method to move the piece left. It first checks to see if it can move
	 * left, and if it can, then it does.
	 */
	public void left() {
		if (canMoveLeft()) {
			this.center.x--;
		}
	}

	/**
	 * A method to rotate the piece 90 degrees clockwise. It does this by
	 * incrementing the currentRotation variable (modulo 4).
	 */
	public void rotate() {
		// change the rotation
		this.currentRotation = (this.currentRotation + 1) % 4;
		// change the current points to correspond to the rotation
		this.points = this.rotations[currentRotation];
	}

	/**
	 * A method to rotate the piece by some multiple of 90 degrees clockwise.
	 * 
	 * @param i
	 *            the number of 90 degree rotations to peform
	 */

	public void rotateBy(int i) {
		// change the rotation
		this.currentRotation = (this.currentRotation + i) % 4;
		// change the current points to correspond to the rotation
		this.points = this.rotations[currentRotation];
	}

	/**
	 * A method to rotate to a specific formation.
	 * 
	 * @param i
	 *            The rotation to change to, where i is an int in 0-3.
	 */
	public void rotateTo(int i) {
		// change the rotation
		this.currentRotation = (i) % 4;
		// change the current points to correspond to the rotation
		this.points = this.rotations[currentRotation];
	}

	/**
	 * Given a Tetris board, 'drop' the piece as far down as possible. Note:
	 * this method does not actually store the piece on the board, just drops it
	 * down to the location it would be.
	 * 
	 * @param board
	 *            the board to drop the piece on; a 2-dimensional array of ints
	 *            with entries as 0 or 1, where 0 means empty and 1 means has a
	 *            block.
	 */
	public void drop(int[][] board) {
		// has the piece collided with any other points yet?
		boolean noCollisions = true;

		// while we have no collisions and the piece is still on the board,
		// drop it down
		while (noCollisions && this.isOnBoard()) {

			// check each piece to make sure there are no collisions
			for (int i = 0; i < 4 && noCollisions; i++) {
				// store the absolute coordinates of the point
				int pointX = this.center.x + points[i].x;
				int pointY = this.center.y + points[i].y;

				// if there is a collision, note this
				if (board[pointY][pointX] == 1) {
					// System.out.println("hit collision");
					noCollisions = false;
					break;
				}
			}

			// if there were no collisions, move down
			if (noCollisions) {
				this.center.y--; // move down
			}
		}
		// coming out of the loop, there must have been
		// a collision or the piece must be below the board.
		// so, we move it up.
		this.center.y++;
	}

	/**
	 * A method to calculate the 'score' of a piece on a board. This method will
	 * be used by an AI to determine what position to play a piece in.
	 * 
	 * The score is determined by -piece height -whitespace below the piece -the
	 * neighbor blocks of the piece -the number of tetrises caused by the piece
	 * 
	 * @param board
	 *            The board to perform the calculations on. Should be a 2-dim
	 *            array of 0's and 1's.
	 * @return The double corresponding to the score.
	 */
	public double score(int[][] board) {
		// store the initial score
		double score = 0;

		/*
		 * Now we will calculate the parts of the score. The first is the
		 * neighbor point score.
		 * 
		 * We go through each of the points of the piece and 'score' its
		 * neighbors (left, right, down).
		 */
		double neighborPoints = 0;

		for (Point point : points) {
			int pointx = this.center.x + point.x;
			int pointy = this.center.y + point.y;

			// System.out.println("coutn: "+ count);
			neighborPoints += scorePoint(board, new Point(pointx + 1, pointy));
			neighborPoints += scorePoint(board, new Point(pointx - 1, pointy));
			neighborPoints += scorePoint(board, new Point(pointx, pointy - 1));
		}

		// store the number of tetrises caused by dropping the piece
		int tetrises = tetrises(board);
		// find the highest block of the board, and subtract the center of this
		// piece
		int highestPiece = highestPiece(board);
		int heightScore = (highestPiece == 0 ? 0
				: (highestPiece - this.center.y));

		// now calculate the score
		// TODO need to abstract these constants
		score += this.AI_CONSTANTS[0] * heightScore;
		score -= this.AI_CONSTANTS[1] * whitespace(board);
		score += this.AI_CONSTANTS[2] * neighborPoints;
		score += this.AI_CONSTANTS[3] * tetrises;

		// return the score
		return score;
	}

	/**
	 * Score a specific point. If the point entry is empty, then score 0. If has
	 * a block, score 1. If it is below the board, score .9, and if it is on the
	 * sides of the board, score .5.
	 * 
	 * @param board
	 *            The board to score the point in.
	 * @param point
	 *            The point to score
	 * @return the points score, calculated as above
	 */
	public double scorePoint(int[][] board, Point point) {
		// score 0 or 1, depending on boards contents
		if (point.x >= 0 && point.x < board[0].length && point.y >= 0
				&& point.y < board.length) {
			return board[point.y][point.x]; // 0 or 1
			// if not on board but below board, return .9
		} else if (point.y == -1) {
			return .9;
			// if not on board by on side, then return .5
		} else if (point.x == -1 || point.x == board[0].length) {
			return .5;
			// otherwise, return 0
		} else {
			return 0;
		}
	}

	/**
	 * Calculate the whitespace on the board that is below the current piece.
	 * 
	 * @param board
	 *            the board to calculate the result on
	 * @return the number of empty squares on the board below the piece
	 */
	public int whitespace(int[][] board) {
		// the counter for whitespace
		int counter = 0;

		/*
		 * for each point, we only want to calculate it if it has a unique X
		 * value from all other pieces so we don't overcount whitespace.
		 * 
		 * So only count unique points. When we do this, then go through each
		 * square below the point and sum up the white space, stopping if we hit
		 * a piece that is full.
		 */
		for (Point point : points) {
			// if the piece is unique or not
			boolean dontCount = false;

			// see if piece is unique
			for (Point point2 : points) {
				if (!point.equals(point2)) {
					if (point2.x == point.x && point2.y < point.y) {
						dontCount = true;
					}
				}
			}

			// if piece is unique, calculate whitespace
			if (!dontCount) {
				// get absolute coords
				int pointx = this.center.x + point.x;
				int pointy = this.center.y + point.y;

				// calculate whitespace
				if (this.isOnBoard()) {
					for (int i = pointy; i >= 0; i--) {
						// whitespace here
						if (board[i][pointx] == 0) {
							counter++;
							// no whitespace, stop
						} else {
							break;
						}
					}
				}
			}
		}

		// return the counter of whitespace
		return counter;
	}

	/**
	 * Returns the number of tetrises on the board, including the points of the
	 * current piece (which may not be on the board).
	 * 
	 * @param board
	 *            the input board
	 * @return how many tetrises are created by adding the piece to the board
	 */
	public int tetrises(int[][] board) {
		// the count of the number of tetrises
		int numTetrises = 0;

		/*
		 * We need to create a new board with the same values as board so we can
		 * add our piece to it without modifying the original board.
		 */
		int[][] copyBoard = new int[board.length][board[0].length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				copyBoard[i][j] = board[i][j];
			}
		}

		// add our piece to the copy board
		for (Point point : points) {
			int pointX = this.center.x + point.x;
			int pointY = this.center.y + point.y;
			copyBoard[pointY][pointX] = 1;
		}

		// go through each row and see if there is a tetris.
		// do this by summing each row and then seeing if the
		// sum equals the length of the row
		for (int i = 0; i < copyBoard.length; i++) {
			// initialize the sum of the row
			int sum = 0;
			for (int j = 0; j < copyBoard[i].length; j++) {
				if (copyBoard[i][j] == 0)
					break;
				sum += copyBoard[i][j];
			}

			// if there is a tetris
			if (sum == board[i].length) {
				numTetrises++;
			}
		}

		// return the number of tetrises
		return numTetrises;
	}

	/**
	 * Returns the highest non zero entry of the input board.
	 * 
	 * @param board
	 *            the input board, a 2-dim array of 0's and 1's
	 * @return the y-coordinate of the highest non zero entry of board
	 */
	public int highestPiece(int[][] board) {
		// go through each row, starting from top
		for (int i = board.length - 1; i >= 0; i--) {
			// go through each entry
			for (int j = 0; j < board[i].length; j++) {
				// if we find a non-zero entry
				// just return this row, no need to continue
				if (board[i][j] != 0)
					return i;
			}
		}
		// if for some reason we didn't find a non-zero element,
		// just return 0
		return 0;
	}

	/**
	 * A method to add the piece to the given board. The piece is first
	 * 'dropped' using the drop method and then each point of the piece is
	 * marked on the board.
	 * 
	 * @param board
	 *            the board to add the piece to
	 */
	public void add(int[][] board) {
		// drop the piece to its appropriate spot
		this.drop(board);
		// add each piece to the board
		for (Point point : points) {
			// get absolute coordinates
			int pointX = this.center.x + point.x;
			int pointY = this.center.y + point.y;
			// add
			board[pointY][pointX] = 1;
		}
	}

}

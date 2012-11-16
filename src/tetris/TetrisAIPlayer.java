package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * A class to play tetris online using the Java Robot class.
 * 
 * @author mellort
 */
public class TetrisAIPlayer {
	/**
	 * Piece_X and piece_Y tell us where to grab the screen pixel for the
	 * current faling piece.
	 */
	public Robot robot;

	public Rectangle nextPieceRect;
	public Rectangle gridRect;

	/**
	 * Pieces tells us the colors of the pieces of the game, and piecestrings
	 * tells us the corresponding string colors.
	 */
	public Color[] pieces;
	public String[] pieceStrings;

	public int DROP_AREA_HEIGHT;

	/**
	 * The wait times for the AI player.
	 * 
	 */
	public int WAIT_TIME;
	public int KEY_TIME;
	// the tetris game
	public Tetris tetris;

	/**
	 * A constructor for the AI Player. takes in the colors for the pieces along
	 * with the strings of the pieces names, and constants for the AI.
	 * 
	 */
	public TetrisAIPlayer(Color[] pieces, String[] pieceStrings,
			double[] aiConstants) {
		tetris = new Tetris(aiConstants);
		this.pieces = pieces;
		this.pieceStrings = pieceStrings;
	}

	public TetrisAIPlayer(Color[] pieces, String[] pieceStrings) {
		double[] aiConstants = { 2.0, 5.0, 7.0, 10.0 };
		tetris = new Tetris(aiConstants);
		this.pieces = pieces;
		this.pieceStrings = pieceStrings;
	}

	public TetrisAIPlayer() {
	}

	/**
	 * setters for wait time and key time
	 */
	public void setWaitTime(int time) {
		this.WAIT_TIME = time;
	}

	public void setKeyTime(int time) {
		this.KEY_TIME = time;
	}

	/**
	 * setters for the pixels to scrape
	 * 
	 * @return
	 */

	public void setNextPieceRect(Rectangle r) {
		nextPieceRect = r;
	}

	public Color averageColor(BufferedImage image) {
		Image scaled = image
				.getScaledInstance(1, 1, Image.SCALE_AREA_AVERAGING);
		BufferedImageBuilder bib = new BufferedImageBuilder();
		int rgb = bib.bufferImage(scaled).getRGB(0, 0);
		return new Color(rgb, true);
	}

	public double equalColor(Color c1, Color c2) {

		double dist = Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2)
				+ Math.pow(c1.getBlue() - c2.getBlue(), 2));

		return dist;
	}

	// TODO this does not work correctly!
	// Write a function that correctly finds out if a block simply shifted
	// Down, is the same, or was changed.
	public boolean pieceRotated(int[][] oldState, int[][] newState) {

		boolean isSame = true;
		// check if the block just moved down by 1 normally

		Point oldAvg = new Point();
		Point newAvg = new Point();
		double oldCount = 0, newCount = 0;

		for (int y = 1; y < DROP_AREA_HEIGHT-1; y++) {
			for (int x = 0; x < 10; x++) {

				if (oldState[x][y] != 0) {
					oldAvg.setLocation(oldAvg.x + x, oldAvg.y + y);
					oldCount++;
				}

				if (newState[x][y] != 0) {
					newAvg.setLocation(newAvg.x + x, newAvg.y + y);
					newCount++;
				}
			}
		}
		oldAvg.setLocation(oldAvg.x / oldCount, oldAvg.y / oldCount);
		newAvg.setLocation(newAvg.x / newCount, newAvg.y / newCount);

		if (oldAvg.distance(newAvg) < 0.01) {
			return false;
		}

		if (Math.abs(oldAvg.x - newAvg.x) < 0.01
				&& Math.abs(newAvg.y - oldAvg.y)
						- Math.floor(Math.abs(newAvg.y - oldAvg.y)) < 0.1) {
			return false;
		}

		return true;
	}
	public boolean pieceTranslated(int[][] oldState, int[][] newState) {

		boolean isSame = true;
		// check if the block just moved down by 1 normally

		Point oldAvg = new Point();
		Point newAvg = new Point();
		double oldCount = 0, newCount = 0;

		for (int y = 1; y < DROP_AREA_HEIGHT-1; y++) {
			for (int x = 0; x < 10; x++) {

				if (oldState[x][y] != 0) {
					oldAvg.setLocation(oldAvg.x + x, oldAvg.y + y);
					oldCount++;
				}

				if (newState[x][y] != 0) {
					newAvg.setLocation(newAvg.x + x, newAvg.y + y);
					newCount++;
				}
			}
		}
		oldAvg.setLocation(oldAvg.x / oldCount, oldAvg.y / oldCount);
		newAvg.setLocation(newAvg.x / newCount, newAvg.y / newCount);


		if (Math.abs(oldAvg.x - newAvg.x) < 0.01) {
			return false;
		}

		return true;
	}


	public void printMap(int[][] boardState, boolean shifted) {

		for (int y = 0; y < 19; y++) {
			if (y == 18 && shifted)
				break;
			for (int x = 0; x < 10; x++) {

				if (shifted) {
					if (boardState[x][y + 1] != 0)
						System.out.print("X");
					else
						System.out.print("_");
				} else {
					if (boardState[x][y] != 0)
						System.out.print("X");
					else
						System.out.print("_");
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}

	// resyncs board state using image recongnition
	public void syncBoardMap(int[][] boardState) {

		for (int y = 0; y < 19; y++) {
			for (int x = 0; x < 10; x++) {
				tetris.board[19 - y][x] = boardState[x][y];
			}
		}
	}

	public int[][] boardState() {

		int[][] boardState = new int[10][19];

		BufferedImage bf1 = robot.createScreenCapture(gridRect);

		RescaleOp rescale = new RescaleOp(3.6f, 20.0f, null);
		bf1 = rescale.filter(bf1, null);// constrast filter

		float dx = gridRect.width;
		float dy = gridRect.height;

		// set offset
		float sx = dx / (float) 10.0;
		float sy = dy / (float) 19.0;

		for (int y = 0; y < 19; y++) {
			for (int x = 0; x < 10; x++) {

				BufferedImage rectImg = bf1.getSubimage((int) (sx * x),
						(int) (sy * y), (int) sx, (int) sy);

				float pixCount = pixelCount(rectImg);

				if (pixCount > 30000) {
					boardState[x][y] = 1;
				} else {
					boardState[x][y] = 0;
				}

			}
		}
		return boardState;
	}

	public float pixelCount(BufferedImage bf1) {
		float runningTotal = 0;
		for (int i = 0; i < bf1.getWidth(); i++) {
			for (int j = 0; j < bf1.getHeight(); j++) {
				Color a = new Color(bf1.getRGB(i, j), true);

				float differenceRed = a.getRed();
				float differenceGreen = a.getGreen();
				float differenceBlue = a.getBlue();

				if (Math.abs(differenceRed - differenceBlue) < 10
						&& Math.abs(differenceGreen - differenceBlue) < 10
						&& Math.abs(differenceGreen - differenceRed) < 10) {
					continue;
				}

				float differenceForThisPixel = (differenceRed + differenceGreen + differenceBlue) / 3;
				runningTotal += Math.abs(differenceForThisPixel);
			}
		}
		return (runningTotal);
	}

	/**
	 * A method to play the game.
	 * 
	 */
	public int playGame() throws Exception {
		// make a new robot
		robot = new Robot();
		// keep track of cleared pieces
		int clearedPieces = 0;
		boolean firstRun = true;
		boolean willTetrisfy = false;

		// while the game isn't over, play it
		while (!tetris.over) {
			// get piece from color
			int next = -1;

			/*
			 * Get the next piece.
			 */
			while (next == -1) {
				// get the color of the pixel
				// robot.createScreenCapture(screenRect)

				Color avgColor = averageColor(robot
						.createScreenCapture(nextPieceRect));

				// match the color with known colors

				double smallestThresh = 999;
				for (int i = 0; i < pieces.length; i++) {
					double thres = equalColor(pieces[i], avgColor);
					if (avgColor != null && thres < 20) {
						if (thres < smallestThresh) {
							next = i;
							smallestThresh = thres;
						}
					}
				}

				if (next == -1) {

				} else {
					System.out.println("color is " + avgColor);
					/*
					 * next = -1; Thread.sleep(1000);
					 */
				}
			}

			Thread.sleep(WAIT_TIME);

			if (firstRun) {
				firstRun = false;
				System.out.println("got first piece: press shift");
				Thread.sleep(1000);

			} else {
				// now, hard drop the piece
				System.out.println("\tspace");
				Thread.sleep(KEY_TIME * 2);
				robot.keyPress(KeyEvent.VK_SPACE);
				Thread.sleep(KEY_TIME);
				robot.keyRelease(KeyEvent.VK_SPACE);
			}
			if (willTetrisfy) {
				System.out.println("Tetrisify");
				Thread.sleep(120);
				willTetrisfy = false;
			}

			syncBoardMap(boardState());

			// print out the kind of piece we got
			System.out.println("got a " + pieceStrings[next]);
			// add the piece to the tetris game
			tetris.addPiece(next);
			// make the move, and determine where the piece needs to move in the
			// online game
			int[] moves = tetris.makeMove();
			// remove tetrises
			// add extra delay if we get a 1+ lines
			if (tetris.tetrisify() > 0) {
				willTetrisfy = true;
			}

			// print the board
			tetris.printBoard();
			// increment cleared pieces
			clearedPieces++;

			if (moves == null)
				continue;

			// move piece
			int displacement = moves[0];
			int rotation = moves[1];

			System.out.println("need to move: " + displacement + " rotate: "
					+ rotation);

			while (true) {
				int[][] oldState = boardState();
				int[][] newState = oldState;
				do {
					System.out.print(".");
					Thread.sleep(100);
					newState = boardState();
				} while (!pieceTranslated(oldState, newState));
				System.out.println("\nPiece translated");
				if (oldState == null)
					break;
			}

			while (rotation == 3) {

				int[][] oldState = boardState();
				int[][] newState = oldState;
				do {
					robot.keyPress(KeyEvent.VK_Z);
					Thread.sleep(20);
					newState = boardState();
				} while (!pieceRotated(oldState, newState));
				robot.keyRelease(KeyEvent.VK_Z);
				Thread.sleep(KEY_TIME);
				newState = boardState();
				if (!pieceRotated(oldState, newState))
					continue;
				break;
			}

			// rotate the piece
			while (rotation > 0 && rotation < 3) {

				int[][] oldState = boardState();
				int[][] newState = oldState;
				do {
					robot.keyPress(KeyEvent.VK_UP);
					Thread.sleep(20);
					newState = boardState();
				} while (!pieceRotated(oldState, newState));
				robot.keyRelease(KeyEvent.VK_UP);
				Thread.sleep(KEY_TIME);
				newState = boardState();
				if (!pieceRotated(oldState, newState))
					continue;
				rotation--;
			}
			// now move the piece
			if (displacement < 0) {
				// move right
				while (displacement < 0) {

					Thread.sleep(KEY_TIME);
					System.out.println("\tright");
					robot.keyPress(KeyEvent.VK_RIGHT);
					Thread.sleep(KEY_TIME);
					robot.keyRelease(KeyEvent.VK_RIGHT);
					displacement++;

				}
			} else if (displacement > 0) {
				// move left
				while (displacement > 0) {
					Thread.sleep(KEY_TIME);
					System.out.println("\tleft");
					robot.keyPress(KeyEvent.VK_LEFT);
					Thread.sleep(KEY_TIME);
					robot.keyRelease(KeyEvent.VK_LEFT);
					displacement--;
				}
			}

		}

		return 0;
	}
}

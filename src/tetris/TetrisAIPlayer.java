package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

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

	/**
	 * Pieces tells us the colors of the pieces of the game, and piecestrings
	 * tells us the corresponding string colors.
	 */
	public Color[] pieces;
	public String[] pieceStrings;

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

	public int[] averageLocationDiff(BufferedImage bf1) {

		double xxsum = 0;
		double yysum = 0;
		double ccsum = 0;

		BufferedImage bi = new BufferedImage(bf1.getWidth(), bf1.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics g = bi.getGraphics();
		g.drawImage(bf1, 0, 0, null);
		g.dispose();

		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {

				Color a = new Color(bi.getRGB(i, j), true);

				if (a.getRed() > 30) {
					xxsum += a.getRed() * i;
					yysum += a.getRed() * j;
					ccsum += a.getRed();
				}
			}
		}
		return new int[] { (int) (xxsum / ccsum), (int) (yysum / ccsum) };
	}

	public float averageDiff(BufferedImage bf1, BufferedImage bf2) {
		int numberOfPixels = 0;
		float runningTotal = 0;
		for (int i = 0; i < bf1.getWidth(); i++) {
			for (int j = 0; j < bf1.getHeight(); j++) {
				Color a = new Color(bf1.getRGB(i, j), true);
				Color b = new Color(bf2.getRGB(i, j), true);

				float differenceRed = Math.abs(a.getRed() - b.getRed()) / 255;
				float differenceGreen = Math.abs(a.getGreen() - b.getGreen()) / 255;
				float differenceBlue = Math.abs(a.getBlue() - b.getBlue()) / 255;

				float differenceForThisPixel = (differenceRed + differenceGreen + differenceBlue) / 3;
				runningTotal += differenceForThisPixel;
				numberOfPixels++;

			}
		}
		return (runningTotal / numberOfPixels);
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
				Thread.sleep(KEY_TIME);
				robot.keyPress(KeyEvent.VK_SPACE);
				Thread.sleep(KEY_TIME);
				robot.keyRelease(KeyEvent.VK_SPACE);
			}
			// print out the kind of piece we got
			System.out.println("got a " + pieceStrings[next]);
			// add the piece to the tetris game
			tetris.addPiece(next);
			// make the move, and determine where the piece needs to move in the
			// online game
			int[] moves = tetris.makeMove();
			// remove tetrises
			tetris.tetrisify();
			// print the board
			tetris.printBoard();
			// increment cleared pieces
			clearedPieces++;

			// move piece
			int displacement = moves[0];
			int rotation = moves[1];

			System.out.println("need to move: " + displacement + " rotate: "
					+ rotation);

			if (rotation == 3) {
				Thread.sleep(KEY_TIME);
				System.out.println("\trotate left");
				Thread.sleep(KEY_TIME);
				robot.keyPress(KeyEvent.VK_Z);
				Thread.sleep(KEY_TIME);
				robot.keyRelease(KeyEvent.VK_Z);
				rotation -= 3;
			}

			// rotate the piece
			while (rotation > 0 && rotation < 3) {
				Thread.sleep(KEY_TIME);
				System.out.println("\trotate");
				Thread.sleep(KEY_TIME);
				robot.keyPress(KeyEvent.VK_UP);
				Thread.sleep(KEY_TIME);
				robot.keyRelease(KeyEvent.VK_UP);
				rotation--;

			}

			// now move the piece
			if (displacement < 0) {
				// move right
				while (displacement < 0) {
					Thread.sleep(KEY_TIME);
					System.out.println("\tright");
					Thread.sleep(KEY_TIME);
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
					Thread.sleep(KEY_TIME);
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

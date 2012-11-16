import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import tetris.*;

public class Main {

	// These arguments are perfect for my hackintosh split screen on the left
	// side
	// 607 408 667 468 395 395 575 735
	public static void main(String[] args) throws Exception {

		double[] constants = { 0.7079009304384309, 3.8753536098633123,
				7.015729027236182, 5.720294020792873 };

		TetrisFriendsPlayer tfp = new TetrisFriendsPlayer(constants);
		Rectangle r = null;
		Rectangle r2 = null;

		System.out.printf("Number of arguments: %d\n", args.length);
		if (args.length >= 4) {
			try {
				Integer x = Integer.parseInt(args[0]);
				Integer y = Integer.parseInt(args[1]);
				Integer x2 = Integer.parseInt(args[2]);
				Integer y2 = Integer.parseInt(args[3]);

				r = new Rectangle(x, y, x2 - x, y2 - y);

			} catch (Exception e) {
				System.out.println("Parsing error");
			}
		}
		if (args.length == 8) {
			try {
				Integer x = Integer.parseInt(args[4]);
				Integer y = Integer.parseInt(args[5]);
				Integer x2 = Integer.parseInt(args[6]);
				Integer y2 = Integer.parseInt(args[7]);

				r2 = new Rectangle(x, y, x2 - x, y2 - y);

			} catch (Exception e) {
				System.out.println("Parsing error 2");
			}
		}

		if (r == null) {
			Point b, b2;

			System.out.println("Waiting..");
			Thread.sleep(4000);

			System.out.println("Top-Left Corner\n");
			b = MouseInfo.getPointerInfo().getLocation();
			System.out.printf("Got mouse positions %d,%d\n", (int) b.x,
					(int) b.y);
			Thread.sleep(2000);
			System.out.println("Bottom-Right Corner\n");
			b2 = MouseInfo.getPointerInfo().getLocation();

			tfp.setNextPieceRect(new Rectangle((int) b.getX(), (int) b.getY(),
					(int) b2.getX() - (int) b.getX(), (int) b2.getY()
							- (int) b.getY()));
			System.out.printf("Got mouse positions %d,%d\n", (int) b2.x,
					(int) b2.y);
		} else {
			tfp.setNextPieceRect(r);
		}

		if (r2 == null) {

			System.out.println("Place Mouse over tetris block drop area\n");
			Thread.sleep(3000);
			Point b;
			r = new Rectangle();
			System.out.println("Top-Left Corner\n");
			b = MouseInfo.getPointerInfo().getLocation();
			r.x = (int) b.getX();
			r.y = (int) b.getY();
			System.out.printf("Got mouse positions %d,%d\n", (int) b.x,
					(int) b.y);
			Thread.sleep(3000);
			System.out.println("Bottom-Right Corner\n");
			b = MouseInfo.getPointerInfo().getLocation();
			r.width = (int) b.getX() - r.x;
			r.height = (int) b.getY() - r.y;
			System.out.printf("Got mouse positions %d,%d\n", (int) b.x,
					(int) b.y);
			tfp.gridRect = r;
		} else {
			tfp.gridRect = r2;
		}

		System.out.println("Starting in 2 sec!");
		Thread.sleep(2000);
		System.out.println("Starting...");
		tfp.playGame();
	}
}

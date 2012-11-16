package tetris;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class BufferedImageBuilder {

	private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

	public BufferedImage bufferImage(Image image) {
		return bufferImage(image, DEFAULT_IMAGE_TYPE);
	}

	public BufferedImage bufferImage(Image image, int type) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), type);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, null, null);
		return bufferedImage;
	}

}
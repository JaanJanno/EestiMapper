package models;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.avaje.ebean.Model;

@Entity
public class ImageEntry extends Model {

	@Id
	public Long id;

	@Lob
	public byte[] image;

	public ImageEntry(BufferedImage img) {
		setImage(img);
	}

	public static Find<Long, ImageEntry> find = new Find<Long, ImageEntry>() {
	};

	public void setImage(BufferedImage img) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] imageBytes = baos.toByteArray();
		image = imageBytes;
	}

	public BufferedImage getImage() {
		ByteArrayInputStream baos = new ByteArrayInputStream(image);
		try {
			return ImageIO.read(baos);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

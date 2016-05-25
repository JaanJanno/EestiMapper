package controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import models.DrawCallForm;
import models.ImageEntry;
import play.data.Form;
import play.mvc.*;
import services.csv.CSVFileReader;
import services.draw.Entry;
import services.draw.EntryDrawer;
import services.draw.MapDrawer;
import views.html.*;

import static play.data.Form.*;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	MapDrawer omavalitsusDrawer = new MapDrawer("conf/resources/omavalitsus/omavalitsus_20160501.shp", "ONIMI");
	MapDrawer maakondDrawer = new MapDrawer("conf/resources/maakond/maakond_20160501.shp", "MNIMI");

	public Result index() {
		@SuppressWarnings("deprecation")
		Form<DrawCallForm> form = Form.form(DrawCallForm.class);
		return ok(index.render(form));
	}

	public Result getImages() {
		return ok(images.render(ImageEntry.find.all()));
	}

	public Result postImage() {
		@SuppressWarnings("deprecation")
		Form<DrawCallForm> callReq = form(DrawCallForm.class).bindFromRequest();

		play.mvc.Http.MultipartFormData<File> body = request().body().asMultipartFormData();
		play.mvc.Http.MultipartFormData.FilePart<File> csv = body.getFile("csv");

		if (csv != null) {
			java.io.File file = csv.getFile();
			List<Entry> entries = CSVFileReader.read(file);

			MapDrawer pick;
			Integer width;
			boolean autofill = true;

			if (callReq.data().get("level").equals("Omavalitsus")) {
				pick = omavalitsusDrawer;
			} else {
				pick = maakondDrawer;
			}

			if(callReq.data().get("autozoom") == null)
				autofill = false;

			try {
				width = Integer.valueOf(callReq.data().get("width"));
			} catch (Exception e) {
				width = 1024;
			}

			Color min = hex2Rgb(callReq.data().get("mincol"));
			Color max = hex2Rgb(callReq.data().get("maxcol"));

			ImageEntry entry = null;
			try {
				Integer year = Integer.parseInt(callReq.data().get("year"));
				BufferedImage img = EntryDrawer.getImageByYear(entries, pick, min, max, width, year, autofill);
				entry = save(img);
			} catch (Exception e) {
				BufferedImage img = EntryDrawer.getImage(entries, pick, min, max, width, autofill);
				entry = save(img);
			}

			if (entry == null)
				return redirect("/");
			else {
				return redirect("/image/" + Long.toString(entry.id));
			}
		} else {
			return redirect("/");
		}

	}

	private ImageEntry save(BufferedImage img) {
		ImageEntry e = new ImageEntry(img);
		e.insert();
		return e;
	}

	public Result getImage(Long id) {

		ImageEntry entry = ImageEntry.find.byId(id);

		if (entry == null) {
			return redirect("/");
		}

		BufferedImage img = entry.getImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] image = baos.toByteArray();
		ByteArrayInputStream input = new ByteArrayInputStream(image);

		return ok(input).as("image/png");
	}

	public static Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

}

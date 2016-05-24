package controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import play.mvc.*;
import services.draw.MapDrawer;
import services.draw.SimpleFeatureDrawCall;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	public Result index() {
		return ok(index.render("Y2 new application is ready."));
	}

	public Result getImage(Long id) {

		MapDrawer d = new MapDrawer("conf/resources/omavalitsus/omavalitsus_20160501.shp", "ONIMI");

		List<SimpleFeatureDrawCall> calls = new ArrayList<>();
		for(String s: d.getFeatures())
			calls.add(new SimpleFeatureDrawCall(s, Color.RED));

		BufferedImage img = d.generateMap(1024, calls, false);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] image = baos.toByteArray();

		ByteArrayInputStream input = new ByteArrayInputStream(image);

		return ok(input).as("image/jpeg");
	}
}

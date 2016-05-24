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
import services.draw.SimpleDrawCall;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	/**
	 * An action that renders an HTML page with a welcome message. The
	 * configuration in the <code>routes</code> file means that this method will
	 * be called when the application receives a <code>GET</code> request with a
	 * path of <code>/</code>.
	 */
	public Result index() {
		return ok(index.render("Y2 new application is ready."));
	}

	public Result getImage(Long id) {

		MapDrawer d = new MapDrawer("conf/resources/maakond/omavalitsus_20160501.shp");

		List<SimpleDrawCall> calls = new ArrayList<>();
		calls.add(new SimpleDrawCall("Tartu vald", Color.RED));
		/*calls.add(new SimpleDrawCall("Järva maakond", Color.BLUE));
		calls.add(new SimpleDrawCall("Harju maakond", Color.GREEN));
		calls.add(new SimpleDrawCall("Põlva maakond", Color.YELLOW));
		calls.add(new SimpleDrawCall("Saare maakond", Color.ORANGE));*/
		BufferedImage img = d.generateMap(1024, calls);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "jpg", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] image = baos.toByteArray();

		ByteArrayInputStream input = new ByteArrayInputStream(image);

		return ok(input).as("image/jpeg");
	}
}

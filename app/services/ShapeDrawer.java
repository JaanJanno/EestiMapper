package services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class ShapeDrawer {

	public static BufferedImage draw(MultiPolygon poly, int width, int height, double xMin, double xMax, double yMin,
			double yMax) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		System.out.println(poly.getNumGeometries());

		for (int x = 0; x < width; x += 100) {
			for (int y = 0; y < height; y += 100) {
				@SuppressWarnings("deprecation")
				Point p = new Point(
						new Coordinate(revScaleCoords(width, xMin, xMax, x), revScaleCoords(height, yMin, yMax, y)),
						new PrecisionModel(PrecisionModel.FLOATING), 1);
				if (poly.contains(p)) {
					// System.out.println("tru");
					g.drawString("x", x, y);
				}

				/*
				 * System.out.println(poly.contains(p)); System.out.println(p);
				 * System.out.println(xMin+(xMax-xMin)/2);
				 * System.out.println(yMin+(yMax-yMin)/2);
				 */
			}
			// System.out.println(x);
		}

		/*
		 * Coordinate[] cs = poly.getCoordinates();
		 * 
		 * for(Coordinate c : cs) { System.out.println(c.x); g.drawString("t",
		 * (int)scaleCoords(width, xMin, xMax, c.x), height -
		 * (int)scaleCoords(height, yMin, yMax, c.y)); }
		 * 
		 * Point p = new Point(new
		 * Coordinate(xMin+(xMax-xMin)/2,yMin+(yMax-yMin)/2), new
		 * PrecisionModel(PrecisionModel.FLOATING), 1); g.drawString("x",
		 * (int)scaleCoords(width, xMin, xMax, (xMax-xMin)/2), height -
		 * (int)scaleCoords(height, yMin, yMax, (yMax-yMin)/2)); boolean b =
		 * poly.contains(p.getCentroid()); System.out.println(b);
		 */

		return img;
	}

	public static double scaleCoords(int width, double min, double max, double coord) {
		return width * ((coord - min) / (max - min));
	}

	public static double revScaleCoords(int width, double min, double max, double coord) {
		return min + (max - min) * (coord / width);
	}

	public static RenderedImage usingFeatureCaching(int width, int height) throws Exception {

		// Shape-file's path
		URL url = new URL("file://C:\\Users\\Jaan\\Desktop\\maakond_20160501.shp");
		ShapefileDataStore shapefile = new ShapefileDataStore(url);

		// Creates a map and adds the shapefile
		MapContent map = new MapContent();

		// Set's windows title
		map.setTitle("Estonia");

		// Creates the map style
		StyleBuilder styleBuilder = new StyleBuilder();
		PolygonSymbolizer restrictedSymb1 = styleBuilder.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 0);
		PolygonSymbolizer restrictedSymb2 = styleBuilder.createPolygonSymbolizer(Color.RED, Color.BLACK, 0);

		// Sets opacity
		restrictedSymb1.getFill().setOpacity(styleBuilder.literalExpression(0.5));
		restrictedSymb2.getFill().setOpacity(styleBuilder.literalExpression(0.5));
		org.geotools.styling.Style myStyle1 = styleBuilder.createStyle(restrictedSymb1);
		org.geotools.styling.Style myStyle2 = styleBuilder.createStyle(restrictedSymb2);
		
		List<SimpleFeature> feature = new ArrayList<>();	
		FeatureIterator<SimpleFeature> iterator = shapefile.getFeatureSource().getFeatures().features();
		while (iterator.hasNext()) {
			feature.add(iterator.next());
		}
		
		DefaultFeatureCollection lineCollection1 = new DefaultFeatureCollection();
		lineCollection1.add(feature.get(0));

		
		DefaultFeatureCollection lineCollection2 = new DefaultFeatureCollection();

		lineCollection2.add(feature.get(5));

		//SimpleFeature feature = shapefile.getFeatureSource().getFeatures().features().next();

		//FeatureSource feature = shapefile.getFeatureSource();
		// Adds another layer to the map
		FeatureLayer layer = new FeatureLayer(lineCollection1 , myStyle1);
		map.addLayer(layer);
		
		FeatureLayer layer2 = new FeatureLayer(lineCollection2 , myStyle2);
		map.addLayer(layer2);

		StreamingRenderer renderer = new StreamingRenderer();
		renderer.setMapContent(map);

		return saveImage(map, width);
		/*
		JMapPane p = new JMapFrame(map).getMapPane();
		return p.getBaseImage();*/
	}

	public static BufferedImage saveImage(final MapContent map, int width) {

		GTRenderer renderer = new StreamingRenderer();
		renderer.setMapContent(map);

		Rectangle imageBounds = null;
		ReferencedEnvelope mapBounds = null;
		try {
			mapBounds = map.getMaxBounds();
			double heightToWidth = mapBounds.getSpan(1) / mapBounds.getSpan(0);
			imageBounds = new Rectangle(0, 0, width, (int)(width*heightToWidth));

		} catch (Exception e) {
			// failed to access map layers
			throw new RuntimeException(e);
		}

		BufferedImage image = new BufferedImage(imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB);

		Graphics2D gr = image.createGraphics();
		gr.setPaint(Color.WHITE);
		gr.fill(imageBounds);

		renderer.paint(gr, imageBounds, mapBounds);

		return image;

	}

}

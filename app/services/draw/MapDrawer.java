package services.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeature;

import services.ShapeReader;

public class MapDrawer {

	Map<String, SimpleFeature> featureMap;

	public MapDrawer(String url) {
		this.featureMap = ShapeReader.readFeatureMap(url, "ONIMI");
	}

	public BufferedImage generateMap(int width, List<SimpleDrawCall> calls) {

		MapContent map = new MapContent();
		StyleBuilder styleBuilder = new StyleBuilder();

		for (SimpleDrawCall call : calls) {
			PolygonSymbolizer restrictedSymb1 = styleBuilder.createPolygonSymbolizer(call.color, Color.BLACK, 0);
			restrictedSymb1.getFill().setOpacity(styleBuilder.literalExpression(0.5));
			org.geotools.styling.Style myStyle1 = styleBuilder.createStyle(restrictedSymb1);
			DefaultFeatureCollection lineCollection1 = new DefaultFeatureCollection();
			lineCollection1.add(featureMap.get(call.name));
			FeatureLayer layer = new FeatureLayer(lineCollection1, myStyle1);
			map.addLayer(layer);
		}

		BufferedImage image = generateImage(map, width);
		map.dispose();
		return image;
	}

	private static BufferedImage generateImage(final MapContent map, int width) {

		GTRenderer renderer = new StreamingRenderer();
		renderer.setMapContent(map);

		Rectangle imageBounds = null;
		ReferencedEnvelope mapBounds = null;
		try {
			mapBounds = map.getMaxBounds();
			double heightToWidth = mapBounds.getSpan(1) / mapBounds.getSpan(0);
			imageBounds = new Rectangle(0, 0, width, (int) (width * heightToWidth));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		BufferedImage image = new BufferedImage(imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB);

		Graphics2D gr = image.createGraphics();
		gr.setPaint(Color.WHITE);
		gr.fill(imageBounds);

		renderer.paint(gr, imageBounds, mapBounds);

		return image;

	}

	public static void main(String[] args) {
		MapDrawer d = new MapDrawer("conf/resources/omavalitsus/omavalitsus_20160501.shp");

		try {
			List<SimpleDrawCall> calls = new ArrayList<>();
			for(String s: d.featureMap.keySet())
				calls.add(new SimpleDrawCall(s, Color.RED));
			/*calls.add(new SimpleDrawCall("Tartu linn", Color.RED));
			calls.add(new SimpleDrawCall("Järva maakond", Color.BLUE));
			calls.add(new SimpleDrawCall("Harju maakond", Color.GREEN));
			calls.add(new SimpleDrawCall("Põlva maakond", Color.YELLOW));
			calls.add(new SimpleDrawCall("Saare maakond", Color.ORANGE));*/
			BufferedImage img = d.generateMap(1024, calls);

			JFrame f = new JFrame() {
				private static final long serialVersionUID = 1L;

				@Override
				public void paint(Graphics g) {
					super.paintComponents(g);
					g.drawImage((Image) img, 20, 50, null);
				}
			};
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setSize(1074, 1074);
			f.setLocation(100, 100);
			f.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

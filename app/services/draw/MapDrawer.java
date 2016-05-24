package services.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	private Map<String, SimpleFeature> featureMap;

	public MapDrawer(String url, String feature) {
		this.featureMap = ShapeReader.readFeatureMap(url, feature);
	}

	public List<String> getFeatures() {
		List<String> features = new ArrayList<>();
		features.addAll(featureMap.keySet());
		return features;
	}

	public BufferedImage generateMap(int width, List<SimpleFeatureDrawCall> calls, boolean autofill) {

		MapContent map = new MapContent();
		StyleBuilder styleBuilder = new StyleBuilder();
		
		if(autofill) {
			PolygonSymbolizer restrictedSymb1 = styleBuilder.createPolygonSymbolizer(Color.white, Color.BLACK, 0);
			org.geotools.styling.Style myStyle1 = styleBuilder.createStyle(restrictedSymb1);
			DefaultFeatureCollection lineCollection1 = new DefaultFeatureCollection();
			for(String f: featureMap.keySet()) {
				
				lineCollection1.add(featureMap.get(f));
				FeatureLayer layer = new FeatureLayer(lineCollection1, myStyle1);
				map.addLayer(layer);
			}
			
		}

		for (SimpleFeatureDrawCall call : calls) {
			if (featureMap.containsKey(call.name.toLowerCase())) {

				PolygonSymbolizer restrictedSymb1 = styleBuilder.createPolygonSymbolizer(call.color, Color.BLACK, 0);
				org.geotools.styling.Style myStyle1 = styleBuilder.createStyle(restrictedSymb1);
				DefaultFeatureCollection lineCollection1 = new DefaultFeatureCollection();
				lineCollection1.add(featureMap.get(call.name.toLowerCase()));
				FeatureLayer layer = new FeatureLayer(lineCollection1, myStyle1);
				map.addLayer(layer);
			}
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
		MapDrawer d = new MapDrawer("conf/resources/omavalitsus/omavalitsus_20160501.shp", "ONIMI");
		List<SimpleFeatureDrawCall> calls = new ArrayList<>();
		for (String s : d.featureMap.keySet())
			calls.add(new SimpleFeatureDrawCall(s, Color.RED));

		BufferedImage img = d.generateMap(1024, calls, false);
		new DrawTest(img);

	}

}

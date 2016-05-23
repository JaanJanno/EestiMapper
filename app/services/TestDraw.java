package services;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.net.URL;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class TestDraw {

	public static void main(String[] args) {
		try {
			usingFeatureCaching();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void usingFeatureCaching() throws Exception {

		// Shape-file's path
		URL url = new URL("file://C:\\Users\\Jaan\\Desktop\\maakond_20160501.shp");
		ShapefileDataStore shapefile = new ShapefileDataStore(url);

		// Creates a map and adds the shapefile
		MapContent map = new MapContent();

		// Set's windows title
		map.setTitle("Italy");

		// Creates the map style
		StyleBuilder styleBuilder = new StyleBuilder();
		PolygonSymbolizer restrictedSymb = styleBuilder.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 0);

		// Sets opacity
		restrictedSymb.getFill().setOpacity(styleBuilder.literalExpression(0.5));
		org.geotools.styling.Style myStyle = styleBuilder.createStyle(restrictedSymb);

		// Adds another layer to the map
		FeatureLayer layer = new FeatureLayer(shapefile.getFeatureSource(), myStyle);
		map.addLayer(layer);
		
		

		StreamingRenderer renderer = new StreamingRenderer();
		renderer.setMapContent(map);
		
		JMapPane p = new JMapFrame(map).getMapPane();
		RenderedImage bufferedImage = p.getBaseImage();

		// Shows the map
		JMapFrame.showMap(map);
	}
}

package services;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.MultiPolygon;

public class ShapeReader {

	public static Map<String, Shape> read(String url) {

		Map<String, Shape> locations = new HashMap<>();

		File file = new File(url);

		try {

			Map<String, URL> connect = new HashMap<String, URL>();
			connect.put("url", file.toURI().toURL());

			DataStore dataStore = DataStoreFinder.getDataStore(connect);
			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];

			FeatureSource<?, ?> featureSource = dataStore.getFeatureSource(typeName);
			FeatureCollection<?, ?> collection = featureSource.getFeatures();
			FeatureIterator<?> iterator = collection.features();

			try {
				while (iterator.hasNext()) {
					Feature feature = iterator.next();

					String name = feature.getProperty("MNIMI").getValue().toString();
					BoundingBox box = feature.getDefaultGeometryProperty().getBounds();
					MultiPolygon poly = (MultiPolygon) feature.getDefaultGeometryProperty().getValue();

					locations.put(name, new Shape(poly, box));
				}
			} finally {
				iterator.close();
			}

		} catch (Throwable e) {
		}

		return locations;

	}

	public static Map<String, SimpleFeature> readFeatureMap(String url) {
		
		Map<String, SimpleFeature> locations = new HashMap<>();
		File file = new File(url);
		
		try {
			Map<String, URL> connect = new HashMap<String, URL>();
			connect.put("url", file.toURI().toURL());

			DataStore dataStore = DataStoreFinder.getDataStore(connect);
			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];

			FeatureSource<?, ?> featureSource = dataStore.getFeatureSource(typeName);
			FeatureCollection<?, ?> collection = featureSource.getFeatures();
			FeatureIterator<?> iterator = collection.features();

			try {
				while (iterator.hasNext()) {
					SimpleFeature feature = (SimpleFeature) iterator.next();
					String name = feature.getProperty("MNIMI").getValue().toString();

					locations.put(name, feature);
				}
			} finally {
				iterator.close();
			}

		} catch (Throwable e) {
		}

		return locations;

	}

	public static void main(String[] args) {
		Map<String, SimpleFeature> map = readFeatureMap("C:\\Users\\Jaan\\Desktop\\maakond_20160501.shp");
		SimpleFeature tartu = map.get("Tartu maakond");
		System.out.println(tartu);
	}

}

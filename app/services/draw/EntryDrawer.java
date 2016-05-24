package services.draw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import services.csv.CSVFileReader;

public class EntryDrawer {

	public static BufferedImage getImage(List<Entry> allEntries, MapDrawer drawer, Color min, Color max, int width,
			boolean autofill) {

		List<Entry> entries = filter(allEntries, drawer);

		double minVal = getMin(entries);
		double maxVal = getMax(entries);

		List<SimpleFeatureDrawCall> calls = new ArrayList<>();
		for (Entry e : entries) {
			calls.add(e.toSimpleDrawCall(minVal, maxVal, min, max));
		}

		return drawer.generateMap(width, calls, autofill);

	}

	public static BufferedImage getImageByYear(List<Entry> allEntries, MapDrawer drawer, Color min, Color max,
			int width, int year, boolean autofill) {

		List<Entry> entries = filter(allEntries, drawer, year);

		double minVal = getMin(entries);
		double maxVal = getMax(entries);

		List<SimpleFeatureDrawCall> calls = new ArrayList<>();
		for (Entry e : entries) {
			if (e.getYear() == year) {
				calls.add(e.toSimpleDrawCall(minVal, maxVal, min, max));
			}
		}

		return drawer.generateMap(width, calls, autofill);

	}

	private static List<Entry> filter(List<Entry> allEntries, MapDrawer drawer) {
		List<Entry> entries = new ArrayList<>();
		for (Entry e : allEntries) {
			if (drawer.getFeatures().contains(e.getName())) {
				entries.add(e);
			}
		}
		return entries;
	}

	private static List<Entry> filter(List<Entry> allEntries, MapDrawer drawer, int year) {
		List<Entry> entries = new ArrayList<>();
		for (Entry e : allEntries) {
			if (drawer.getFeatures().contains(e.getName()) && e.getYear() == year) {
				entries.add(e);
			}
		}
		return entries;
	}

	private static double getMax(List<Entry> entries) {

		double max = entries.get(0).getValue();
		for (Entry e : entries) {
			if (e.getValue() > max) {
				max = e.getValue();
			}
		}
		return max;
	}

	private static double getMin(List<Entry> entries) {

		double min = entries.get(0).getValue();
		for (Entry e : entries) {
			if (e.getValue() < min) {
				min = e.getValue();
			}
		}
		return min;
	}

	public static void main(String[] args) {
		List<Entry> entries = CSVFileReader.read("conf/resources/csv_samples/rain.csv");

		int width = 1024;
		MapDrawer drawer = new MapDrawer("conf/resources/omavalitsus/omavalitsus_20160501.shp", "ONIMI");
		Color max = new Color(255, 0, 0);
		Color min = new Color(0, 0, 255);
		BufferedImage img = getImageByYear(entries, drawer, min, max, width, 2015, true);

		new DrawTest(img);

	}

}

package services.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import services.draw.Entry;

public class CSVFileReader {

	public static List<Entry> read(String path) {
		List<String> lines = new ArrayList<>();
		try {
			lines.addAll(Files.readAllLines(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Entry> entries = new ArrayList<>();
		for (String s : lines) {
			String[] vals = s.split(",");
			String name = vals[0].toLowerCase();
			double value = Double.parseDouble(vals[1]);

			if (vals.length == 3) {
				int year = Integer.parseInt(vals[2]);
				entries.add(new Entry(name, value, year));
			} else {
				entries.add(new Entry(name, value));
			}
		}

		return entries;
	}

}

package services.draw;

import java.awt.Color;

public class Entry {
	
	private String name;
	private double value;
	private int year;
	
	public Entry(String name, double value, int year) {
		super();
		this.name = name;
		this.value = value;
		this.year = year;
	}
	
	public Entry(String name, double value) {
		super();
		this.name = name;
		this.value = value;
		this.year = 0;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public int getYear() {
		return year;
	}
	
	public SimpleFeatureDrawCall toSimpleDrawCall(double minVal, double maxVal, Color min, Color max) {		
		double ratio = (value - minVal) / (maxVal - minVal);
		Color color = new Color(
				(int)(min.getRed() + (max.getRed() - min.getRed()) * ratio), 
				(int)(min.getGreen() + (max.getGreen() - min.getGreen()) * ratio), 
				(int)(min.getBlue() + (max.getBlue() - min.getBlue()) * ratio));
		return new SimpleFeatureDrawCall(name, color);
	}

	@Override
	public String toString() {
		return "Entry [name=" + name + ", value=" + value + ", year=" + year + "]";
	}

}

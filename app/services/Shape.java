package services;

import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.MultiPolygon;

public class Shape {
	
	MultiPolygon polygon;
	BoundingBox bound;
	
	public Shape(MultiPolygon polygon, BoundingBox bound) {
		super();
		this.polygon = polygon;
		this.bound = bound;
	}
	
	

}

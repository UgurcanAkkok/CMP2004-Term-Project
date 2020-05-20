package com.bau.connect;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Whiteboard
 * Client uses when it gets an add shape message
 */
public interface Whiteboard {
    public void addPolygon(Integer id, Color c, ArrayList<Double> x, ArrayList<Double> y);

    public void addText(Integer id, Color c, String s, Integer i, Double x, Double y, String text);

    public void addLine(Integer id, Color c, Double x1, Double y1, Double x2, Double y2);

	public void addRect(Integer id, Color c, Double x, Double y, Double w, Double h);
    
	public void addOval(Integer id, Color c, Double x, Double y, Double w, Double h);
	
	public void removeShape(Integer id);
}

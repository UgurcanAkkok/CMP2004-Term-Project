package com.bau.connect;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Whiteboard
 * Client uses when it gets an add shape message
 */
public interface Whiteboard {
    public void addPolygon(Integer id, Color c, ArrayList<Float> x, ArrayList<Float> y);

    public void addText(Integer id, Color c, String s, Integer i, Float x, Float y, String text);

    public void addLine(Integer id, Color c, Float x1, Float y1, Float x2, Float y2);

	public void addRect(Integer id, Color c, Float x, Float y, Float w, Float h);
    
	public void addOval(Integer id, Color c, Float x, Float y, Float w, Float h);
	
	public void removeShape(Integer id);
}

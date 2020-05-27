package com.bau.connect;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Whiteboard
 * Client uses when it gets an add shape message
 */
public interface Whiteboard {
    public void addPolygon(Integer id, Color c, ArrayList<Integer> x, ArrayList<Integer> y);

    public void addText(Integer id, Color c, String s, Integer i, Integer x, Integer y, String text);

    public void addLine(Integer id, Color c, Integer x1, Integer y1, Integer x2, Integer y2);

	public void addRect(Integer id, Color c, Integer x, Integer y, Integer w, Integer h);
    
	public void addOval(Integer id, Color c, Integer x, Integer y, Integer w, Integer h);
	
	public void removeShape(Integer id);
}

package com.bau.connect;
import java.awt.Color;

/**
 * Whiteboard
 * Client uses when it gots an add shape message
 */
public interface Whiteboard {
    public void drawPolygon(Color c, ArrayList<Float> x, ArrayList<Float> y);

    public void drawText(Color c, String s, Integer i, Float x, Float y, String text);

    public void drawLine(Color c, Float x1, Float y1, Float x2, Float y2);

	public void drawRect(Color c, Float x, Float y, Float w, Float h) {
    
	public void drawOval(Color c, Float x, Float y, Float w, Float h) {
}

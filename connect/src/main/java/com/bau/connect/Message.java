package com.bau.connect;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.swing.text.Style;

public class Message {

	static final Logger LOGGER = Logger.getLogger(Message.class.getName());

	public static final String SEPERATOR = "#";
	public static final String PACKET_START = "UE11";
	public static final String PACKET_END = "11EU";

	public static final String LECTURE_JOIN = "LectureJoin";
	/* UE11#LectureJoin#USERNAME#PASSWORD#11EU */
	public static final String LECTURE_LEAVE = "LectureLeave";
	/* UE11#LectureLeave#USERNAME#11EU */
	public static final String LECTURE_START = "LectureStart";
	public static final String LECTURE_END = "LectureEnd";
	public static final String JOIN_FAILURE = "JoinFailure";
	/* UE11#JoinFailure#USERNAME#11EU */
	public static final String INVALID_MESSAGE = "InvalidMessage";
	/* UE11#InvalidMessage#Message#11EU */
	public static final String ADDSHAPE = "AddShape";
	public static final String RECT = "Rect";
	/* UE11#AddShape#ID#Rect#COLOR#X,Y,Width,Height#11EU */
	public static final String OVAL = "Oval";
	/* UE11#AddShape#ID#Oval#COLOR#X,Y,Width,Height#11EU */
	public static final String LINE = "Line";
	/* UE11#AddShape#ID#Line#COLOR#X1,Y1, X2, Y2#11EU */
	public static final String POLYGON = "Polygon";
	/* UE11#AddShape#ID#Polygon#COLOR#X1,X2,X3,...,XN#Y1,Y2,Y3,...,YN#11EU */
	public static final String TEXT = "Text";
	/* UE11#AddShape#ID#Text#COLOR#STYLE#SIZE#X,Y#TEXT#11EU */
	public static final String REMOVESHAPE = "RemoveShape";
	public static final String CHAT = "Chat";
	/* UE11#Chat#USERNAME#MESSAGE BODY#11EU */

	public static final List<String> opList = List.of(LECTURE_JOIN, LECTURE_END, LECTURE_LEAVE,
			LECTURE_START, ADDSHAPE, REMOVESHAPE);

	String operation;
	List<String> arguments;

	static String pack(String msg) {
		return PACKET_START + SEPERATOR + msg + SEPERATOR + PACKET_END;
	}

	static String shape(String id, String s) {
		return pack(ADDSHAPE + SEPERATOR + id + SEPERATOR + s);
	}

	public String shapeText(Integer id, Color c, String s, Integer size, Float x, Float y, String text){
		String name = TEXT;
		String color = String.valueOf(c);
		String style = s;
		String sizeString = String.valueOf(size);
		String coor = x.toString() + "," + y.toString();
		
		String temp = String.join(SEPERATOR, name, color, style, sizeString, coor, text);
				
		return shape(String.valueOf(id), temp);
	}
	public String shapePolygon(Integer id, Color c, ArrayList<Float> xPoints, ArrayList<Float> yPoints) {
		String name = POLYGON;
		String color = String.valueOf(c.getRGB());
		ArrayList<String> xVec = new ArrayList();
		xPoints.forEach((x) -> {
			xVec.add(x.toString());
		});
		String xString = String.join(",", xVec);		
		ArrayList<String> yVec = new ArrayList();
		yPoints.forEach((y) -> {
			yVec.add(y.toString());
		});
		String yString = String.join(",", yVec);
		String temp = String.join(SEPERATOR, name, color, xString, yString);
		return shape(String.valueOf(id), temp);
	}

	public String shapeLine(Integer id, Color c, Float x1, Float y1, Float x2, Float y2) {
		String name = LINE;
		String color = String.valueOf(c.getRGB());
		String properties = String.join(",", x1.toString(), y1.toString(), x2.toString(), y2.toString());
		String temp = String.join(SEPERATOR, name, color, properties);
		return shape(String.valueOf(id), temp);
	}

	public String shapeRect(Integer id, Color c, Float x, Float y, Float w, Float h) {
		String name = RECT;
		String color = String.valueOf(c.getRGB());
		String properties = String.join(",", x.toString(), y.toString(), w.toString(), h.toString());
		String temp = String.join(SEPERATOR, name, color, properties);
		return shape(String.valueOf(id), temp);
	}

	public String shapeOval(Integer id, Color c, Float x, Float y, Float w, Float h) {
		String name = OVAL;
		String color = String.valueOf(c.getRGB());
		String properties = String.join(",", x.toString(), y.toString(), w.toString(), h.toString());
		String temp = String.join(SEPERATOR, name, color, properties);
		return shape(String.valueOf(id), temp);
	}

	public static String chat(String user, String body) {
		var temp = String.join(SEPERATOR, CHAT, user, body);
		return pack(temp);
	}

	public static String invalidMessage(Message message) {
		var temp = String.join(SEPERATOR, INVALID_MESSAGE, message.toString());
		return pack(temp);
	}

	public static String lectureJoin(String user, String pass) {
		var temp = String.join(SEPERATOR, LECTURE_JOIN, user, pass);
		return pack(temp);
	}

	public static String joinFailure(String user) {
		var temp = String.join(SEPERATOR, JOIN_FAILURE, user);
		return pack(temp);
	}

	public List<String> getarguments() {
		return this.arguments;
	}

	public String getoperation() {
		return this.operation;
	}

	/*
     * Message Format: UE11#<OPERATION>#[ ARGUMENT1 ]#[ ARGUMENT2 ]#[...]#11EU
     * Example Message: UE11#LectureStart#41287#11EU
	 */
	Message(String data) throws InvalidMessageException {
		var messageArray = List.of(data.split(SEPERATOR));
		if ((!messageArray.get(0).equals(PACKET_START))
				|| (!messageArray.get(messageArray.size() - 1).equals(PACKET_END))) {
			LOGGER.log(Level.WARNING, "We have an invalid message, {0}", messageArray.toString());
		} else {
			/*
             * After checking if message is from correct source, removes the unneccessary
             * parts
			 */
			messageArray.remove(0);
			messageArray.remove(messageArray.size() - 1);
			throw new InvalidMessageException();
		}
		if (checkOperation(messageArray.get(0)) == false) {
			throw new InvalidMessageException(data);
		} else {
			operation = messageArray.get(0);
			arguments = messageArray.subList(1, messageArray.size());
		}

	}

	static Boolean checkOperation(String op) {
		/* Returns false if op is not a valid one */
		return opList.contains(op);
	}

	@Override
	public String toString() {
		return operation + arguments.toString();
	}

}
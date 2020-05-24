package com.bau.connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeacherMonitor extends JFrame implements ActionListener {

	public static int ourShapeCounter = 0;
	public static DefaultListModel<String> l1 = new DefaultListModel<>();
	JPanel ourBoard;
	JMenuBar menuBar;
	public static Shapes selectedShape = Shapes.RECTANGLE;

	public TeacherMonitor() {
		super("Teacher Monitor");
		setSize(new Dimension(800, 600));
		JMenu menu = new JMenu("Shapes");
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem item = new JRadioButtonMenuItem("Rectangle");
		JRadioButtonMenuItem item2 = new JRadioButtonMenuItem("Circle");
		JRadioButtonMenuItem item3 = new JRadioButtonMenuItem("Polygom");
		JRadioButtonMenuItem item4 = new JRadioButtonMenuItem("Line");
		item.setSelected(true);
		group.add(item);
		group.add(item2);
		group.add(item3);
		group.add(item4);
		item.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
		menu.add(item);
		menu.add(item2);
		menu.add(item3);
		menu.add(item4);
		ourBoard = new MyBoard();
		menuBar = new JMenuBar();
		menuBar.add(menu);
		this.getContentPane().add(ourBoard);
		setJMenuBar(menuBar);
		setVisible(true);
		ChatScreen chatScreen = new ChatScreen();
		Participants participants = new Participants();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		switch (e.getActionCommand()) {
		case "Rectangle":
			selectedShape = Shapes.RECTANGLE;
			break;
		case "Circle":
			selectedShape = Shapes.CIRCLE;
			break;
		case "Polygom":
			selectedShape = Shapes.POLYGOM;
			break;
		case "Line":
			selectedShape = Shapes.LINE;
			break;
		default:
			System.out.println("Error.");
			break;
		}
		ourBoard.repaint();
	}
}

enum Shapes {
	RECTANGLE, CIRCLE, POLYGOM, LINE
}

class MyShape {

	Shapes shapeType;
	int x1, y1, x2, y2;
	int id;

	public MyShape(Shapes shapeType, int x1, int y1, int x2, int y2) {
		this.shapeType = shapeType;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.id = this.hashCode();
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
}

class MyBoard extends JPanel implements MouseListener, MouseMotionListener {

	private List<MyShape> list = new ArrayList<MyShape>();
	boolean isClickingAlready = false;
	private int counter = 0;
	Server server;
	Lecture lecture;

	public MyBoard() {
		setPreferredSize(new Dimension(400, 300));
		setSize(400, 300);
		setMaximumSize(new Dimension(400, 300));
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		lecture = new Lecture("CMP");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					++counter;
					repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		while (true) {
			try {
				server = new Server();
				server.setLecture(lecture);
				server.run();
				break;
			} catch (IOException ex) {
				Logger.getLogger(TeacherMonitor.class.getName()).log(Level.SEVERE, null, ex);
				var dialogResult = JOptionPane.showConfirmDialog(null, "Could not initialize server, try again?");
				if (dialogResult != JOptionPane.YES_OPTION) {
					break;
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (MyShape shape : list) {
			if (shape.shapeType.equals(Shapes.RECTANGLE)) {
				g.drawRect(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.abs(shape.x1 - shape.x2), Math.abs(shape.y1 - shape.y2));
			} else if (shape.shapeType.equals(Shapes.CIRCLE)) {
				g.drawOval(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.abs(shape.x1 - shape.x2), Math.abs(shape.y1 - shape.y2));
			} else if (shape.shapeType.equals(Shapes.POLYGOM)) {
				//g.drawPolygon();
			} else if (shape.shapeType.equals(Shapes.LINE)) {
				g.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
			}
		}

		g.setColor(Color.BLACK);
		String s = "Count = " + String.valueOf(list.size());
		int minutes = (counter / 60) % 60;
		int seconds = counter % 60;
		int hour = (counter / 60) / 60;
		String lectureTimeCounter = "Time : " + String.valueOf(hour) + ":" + String.valueOf(minutes) + ":" + String.valueOf(seconds);
		g.drawString(s, this.getWidth() - 20 - g.getFontMetrics().stringWidth(s), 20);
		g.drawString(lectureTimeCounter, this.getWidth() - 20 - g.getFontMetrics().stringWidth(lectureTimeCounter), 60);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Clicked");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Pressed");
		if (TeacherMonitor.selectedShape.equals(Shapes.RECTANGLE)) {
			list.add(new MyShape(TeacherMonitor.selectedShape, e.getX(), e.getY(), e.getX(), e.getY()));
		} else if (TeacherMonitor.selectedShape.equals(Shapes.CIRCLE)) {
			list.add(new MyShape(TeacherMonitor.selectedShape, e.getX(), e.getY(), e.getX(), e.getY()));
		} else if (TeacherMonitor.selectedShape.equals(Shapes.POLYGOM)) {
			

		} else if (TeacherMonitor.selectedShape.equals(Shapes.LINE)) {
			list.add(new MyShape(TeacherMonitor.selectedShape, e.getX(), e.getY(), e.getX(), e.getY()));
		}
		isClickingAlready = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Released");
		var temp = list.get(list.size() - 1);
		switch (temp.shapeType) {
			case RECTANGLE:
				Double width = Double.valueOf(temp.x1 - temp.x2);
				Double height = Double.valueOf(temp.y1 - temp.y2);
				server.sendRect(temp.id, new Color(0), (double) temp.x1, (double) temp.y1, width, height);
				break;
			case CIRCLE:
				width = Double.valueOf(temp.x1 - temp.x2);
				height = Double.valueOf(temp.y1 - temp.y2);
				server.sendOval(temp.id, Color.black, (double) temp.x1, (double) temp.y1, width, height);
				break;
			case POLYGOM:
				break;
			case LINE:
				server.sendLine(temp.id, Color.black, (double) temp.x1, (double) temp.y1, (double) temp.x2, (double) temp.y2);
				break;
			default:
				throw new AssertionError(temp.shapeType.name());

			}
		isClickingAlready = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isClickingAlready) {
			var temp = list.get(list.size() - 1);
			temp.setX2(e.getX());
			temp.setY2(e.getY());
			repaint();
			
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}

class ChatScreen extends JFrame {

	JTextArea chatArea;

	public ChatScreen() {
		super("Chats");
		setSize(new Dimension(320, 240));
		chatArea = new JTextArea();
		chatArea.setEnabled(false);
		chatArea.setText("Ekin Öcal :\nHello World!\n");
		getContentPane().add(chatArea);
		setVisible(true);
	}
}

class Participants extends JFrame {

	public Participants() {
		super("Chats");
		setSize(new Dimension(320, 240));

		TeacherMonitor.l1.addElement("Ekin Öcal (Teacher)");
		TeacherMonitor.l1.addElement("Ekin Öcal (Student)");
		JList<String> list = new JList<>(TeacherMonitor.l1);
		getContentPane().add(list);
		setVisible(true);
	}
}

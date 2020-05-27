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
        setSize(new Dimension(800,600));
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
		new ChatScreen();
		new Participants();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
        switch(e.getActionCommand()) {
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
    RECTANGLE,CIRCLE,POLYGOM,LINE
}

class MyShape {
	Shapes shapeType;
	ArrayList<Integer> x = new ArrayList<Integer>();
	ArrayList<Integer> y = new ArrayList<Integer>();
	int id;

	public MyShape(Shapes shapeType, int x1, int y1, int x2, int y2) {
		this.shapeType = shapeType;
		x.add(x1);
		x.add(x2);
		y.add(y1);
		y.add(y2);
	}

	public void setX2(int x2) {
        x.set(1,x2);
	}

	public void setY2(int y2) {
        y.set(1,y2);
	}
}

class MyBoard extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private List<MyShape> list = new ArrayList<MyShape>();
	boolean isClickingAlready = false;
	private int counter = 0;
	public static Server server;
	public static Lecture lecture;

	public MyBoard() {
        setPreferredSize(new Dimension(400,300));
        setSize(400,300);
        setMaximumSize(new Dimension(400,300));
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		requestFocus();
		setBackground(Color.WHITE);
		lecture = new Lecture("CMP");
		lecture.setTeacher("Ekin Öcal"); // TODO Ask for a name 
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
                while(true) {
					++counter;
					repaint();
                    if(counter == 60) {
                        JOptionPane.showMessageDialog(null,"Ders Bitti qnq");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		// Start the server, assigning a lecture to it
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

    public static int[] convertIntegers(List<Integer> integers)
    {
		int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (MyShape shape : list) {
            if(shape.shapeType.equals(Shapes.RECTANGLE)) {
                g.drawRect(Math.min(shape.x.get(0),shape.x.get(1)),Math.min(shape.y.get(0),shape.y.get(1)),Math.abs(shape.x.get(0)-shape.x.get(1)),Math.abs(shape.y.get(0)-shape.y.get(1)));
            }
            else if(shape.shapeType.equals(Shapes.CIRCLE)) {
				//g.drawOval(Math.min(shape.x1,shape.x2),Math.min(shape.y1,shape.y2),Math.abs(shape.x1-shape.x2),Math.abs(shape.y1-shape.y2));
                g.drawOval(Math.min(shape.x.get(0),shape.x.get(1)),Math.min(shape.y.get(0),shape.y.get(1)),Math.abs(shape.x.get(0)-shape.x.get(1)),Math.abs(shape.y.get(0)-shape.y.get(1)));
            }
            else if(shape.shapeType.equals(Shapes.POLYGOM)) {
                g.drawPolygon(convertIntegers(shape.x),convertIntegers(shape.y),shape.x.size());
            }
            else if(shape.shapeType.equals(Shapes.LINE)) {
				//g.drawLine(shape.x1,shape.y1,shape.x2,shape.y2);
                g.drawLine(shape.x.get(0),shape.y.get(0),shape.x.get(1),shape.y.get(1));
			}
		}

		g.setColor(Color.BLACK);
		String s = "Count = " + String.valueOf(list.size());
		int minutes = (counter / 60) % 60;
		int seconds = counter % 60;
        int hour = (counter / 60)/60;
		String lectureTimeCounter = "Time : " + String.valueOf(hour) + ":" + String.valueOf(minutes) + ":" + String.valueOf(seconds);
        g.drawString(s, this.getWidth()-20-g.getFontMetrics().stringWidth(s),20);
        g.drawString(lectureTimeCounter, this.getWidth()-20-g.getFontMetrics().stringWidth(lectureTimeCounter),60);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Clicked");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Pressed");
        if(TeacherMonitor.selectedShape.equals(Shapes.RECTANGLE)) {
            list.add(new MyShape(TeacherMonitor.selectedShape,e.getX(),e.getY(),e.getX(),e.getY()));
		} 
        else if(TeacherMonitor.selectedShape.equals(Shapes.CIRCLE)) {
            list.add(new MyShape(TeacherMonitor.selectedShape,e.getX(),e.getY(),e.getX(),e.getY()));
		}
        else if(TeacherMonitor.selectedShape.equals(Shapes.POLYGOM)) {
            list.add(new MyShape(TeacherMonitor.selectedShape,e.getX(),e.getY(),e.getX(),e.getY()));
		}
        else if(TeacherMonitor.selectedShape.equals(Shapes.LINE)) {
            list.add(new MyShape(TeacherMonitor.selectedShape,e.getX(),e.getY(),e.getX(),e.getY()));
		}
        isClickingAlready=true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Released");
		var temp = list.get(list.size() - 1);
		isClickingAlready = false;
		Integer x1, x2, y1, y2;
		x1 = temp.x.get(0);
		x2 = temp.x.get(1);
		y1 = temp.y.get(0);
		y2 = temp.y.get(1);
		Integer width = x1 - x2;
		Integer height = y1 - y2;
		ArrayList<Integer> x = new ArrayList<>();
		temp.x.forEach((i) -> {
			x.add(i);
		});
		ArrayList<Integer> y = new ArrayList<>();
		temp.y.forEach((i) -> {
			y.add(i);
		});
		switch (temp.shapeType) {
		case RECTANGLE:
			server.sendRect(temp.id, Color.black, x1, y1, width, height);
			break;
		case CIRCLE:
			server.sendOval(temp.id, Color.black, x1, y1, width, height);
			break;
		case LINE:
			server.sendLine(temp.id, Color.black, x1, y1, x2, y2);
			break;
		case POLYGOM:
			server.sendPolygon(temp.id, Color.black, x, y);
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
        if(isClickingAlready) {
            List<Integer> xArray =  list.get(list.size()-1).x;
            List<Integer> yArray =  list.get(list.size()-1).y;
            xArray.set(xArray.size()-1,e.getX());
            yArray.set(yArray.size()-1,e.getY());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE && TeacherMonitor.selectedShape.equals(Shapes.POLYGOM)) {
			Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
            List<Integer> xArray =  list.get(list.size()-1).x;
            List<Integer> yArray =  list.get(list.size()-1).y;
            xArray.set(xArray.size()-1,mouseLoc.x);
            yArray.set(yArray.size()-1,mouseLoc.y);
			xArray.add(mouseLoc.x);
			yArray.add(mouseLoc.y);
			repaint();
		}
	}
}

class ChatScreen extends JFrame implements KeyListener {
	JTextArea chatArea;
	JTextArea textArea;
	public ChatScreen() {
		super("Chats");
        setSize(new Dimension(320,240));
		chatArea = new JTextArea();
		textArea = new JTextArea();
		chatArea.setBackground(Color.lightGray);
		chatArea.setEnabled(false);
		chatArea.setText("Ekin Öcal :\nHello World!\n");
		textArea.setText("Test Deneme 123");
		textArea.addKeyListener(this);
        getContentPane().setLayout(new GridLayout(2,1));
		getContentPane().add(chatArea);
		getContentPane().add(textArea);
		setVisible(true);
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			chatArea.setText(chatArea.getText() + "\n" + "Ekin Öcal:\n" + textArea.getText() + "\n");
			textArea.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}

class Participants extends JFrame {
	public Participants() {
		super("Participants");
        setSize(new Dimension(320,240));
//        TeacherMonitor.l1.addElement("Ekin Öcal (Teacher)");
//        TeacherMonitor.l1.addElement("Ekin Öcal (Student)");
		JList<String> list = new JList<>(MyBoard.lecture.participants);
		getContentPane().add(list);
		setVisible(true);
	}
}

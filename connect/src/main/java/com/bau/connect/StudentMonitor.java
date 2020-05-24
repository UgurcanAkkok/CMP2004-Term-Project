package com.bau.connect;

import static com.bau.connect.MyBoard.convertIntegers;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StudentMonitor extends JFrame implements ActionListener {
    public static int ourShapeCounter = 0;
    public static DefaultListModel<String> l1 = new DefaultListModel<>();
    JPanel ourBoard;
    JMenuBar menuBar;
    public static Shapes selectedShape = Shapes.RECTANGLE;

    public StudentMonitor() {
        super("Student Monitor");
        setSize(new Dimension(800,600));
        ourBoard = new MyBoard();
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

enum MyShapes {
    RECTANGLE,CIRCLE,POLYGOM,LINE
}

class MyStudentShape {
    Shapes shapeType;
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();


    public MyStudentShape(Shapes shapeType, int x1, int y1, int x2, int y2) {
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

class MyStudentBoard extends JPanel {
    private List<MyShape> list = new ArrayList<MyShape>();
    boolean isClickingAlready = false;
    private int counter = 0;
    public MyStudentBoard() {
        setPreferredSize(new Dimension(400,300));
        setSize(400,300);
        setMaximumSize(new Dimension(400,300));
        setBackground(Color.WHITE);
        Thread thread= new Thread(new Runnable() {
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
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (MyShape shape : list) {
            if(shape.shapeType.equals(Shapes.RECTANGLE)) {
                g.drawRect(Math.min(shape.x.get(0),shape.x.get(1)),Math.min(shape.y.get(0),shape.y.get(1)),Math.abs(shape.x.get(0)-shape.x.get(1)),Math.abs(shape.y.get(0)-shape.y.get(1)));
            }
            else if(shape.shapeType.equals(Shapes.CIRCLE)) {
                g.drawOval(Math.min(shape.x.get(0),shape.x.get(1)),Math.min(shape.y.get(0),shape.y.get(1)),Math.abs(shape.x.get(0)-shape.x.get(1)),Math.abs(shape.y.get(0)-shape.y.get(1)));
            }
            else if(shape.shapeType.equals(Shapes.POLYGOM)) {
              g.drawPolygon(convertIntegers(shape.x),convertIntegers(shape.y),shape.x.size());  
            }
            
            else if(shape.shapeType.equals(Shapes.LINE)) {
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
}

class StudentChatScreen extends JFrame implements KeyListener {
    JTextArea chatArea;
    JTextArea textArea;
    public StudentChatScreen() {
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

class StudentParticipants extends JFrame {
    public StudentParticipants() {
        super("Participants");
        setSize(new Dimension(320,240));

        TeacherMonitor.l1.addElement("Ekin Öcal (Teacher)");
        TeacherMonitor.l1.addElement("Ekin Öcal (Student)");
        JList<String> list = new JList<>(TeacherMonitor.l1);
        getContentPane().add(list);
        setVisible(true);
    }
}
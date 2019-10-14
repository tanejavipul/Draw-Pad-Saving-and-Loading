package ca.utoronto.utm.paint;

import javax.swing.*;  
import java.awt.*;
import java.io.PrintWriter;
import java.util.ArrayList;

class PaintPanel extends JPanel {
	private static final long serialVersionUID = 3277442988868869424L;
	private ArrayList<PaintCommand> commands = new ArrayList<PaintCommand>();
	
	public PaintPanel(){
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(300,300));
	}
	
	public void setCommands(ArrayList<PaintCommand> commands){
		this.commands=commands;
		this.repaint();
	}
	public void reset(){
		this.commands.clear();
		this.repaint();
	}
	
	public void addCommand(PaintCommand command){
		this.commands.add(command);
	}
	
	public ArrayList getCommands(){
		return this.commands;
	}
	
	public void save(PrintWriter writer)
	{
		for(PaintCommand c: this.commands)
		{
		writer.println((c.getShape().getShapeData()));
		}
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g); //paint background
        Graphics2D g2d = (Graphics2D) g;		
		for(PaintCommand c: this.commands){
			c.execute(g2d);
		}
		g2d.dispose();
	}
}

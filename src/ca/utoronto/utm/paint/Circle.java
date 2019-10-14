package ca.utoronto.utm.paint;

import java.awt.Color;

public class Circle extends Shape {
	private Point centre;
	private int radius;
	
	public Circle(){
		this(new Point(0,0), 0);
	}
	public Circle(Point centre, int radius){
		this.centre = centre;
		this.radius = radius;
	}
	public Point getCentre() { return centre; }
	public void setCentre(Point centre) { this.centre = centre; }
	public int getRadius() { return radius; }
	public void setRadius(int radius) { this.radius = radius; }
	
	public String getShapeData()
	{
		String s = "Circle" + this.ENTER;
		s+= this.toString() + TAB + "center:"+centre.getString() + ENTER;
		s+= TAB + "radius:"+this.radius + ENTER;
		s+= "End Circle";
		return s;
	}
	
}
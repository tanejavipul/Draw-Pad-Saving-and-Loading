package ca.utoronto.utm.paint;

import java.util.ArrayList;

public class Squiggle extends Shape {
	private ArrayList<Point> points=new ArrayList<Point>();
	
	public Squiggle(){
		
	}
	public void add(Point p){ this.points.add(p); }
	public ArrayList<Point> getPoints()
	{ 
		return this.points; 
	}
	public String getShapeData()
	{
		String s = "Squiggle" + ENTER;
		s+= this.toString();
		s+= TAB + "points" + ENTER;
		for(Point c: this.points)
		{
			s+= TAB + TAB + "point:" + c.getString() + ENTER;
		}
		s+= TAB + "end points" + ENTER;
		s+= "End Squiggle";
		return s;
	}
	
}
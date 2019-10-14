package ca.utoronto.utm.paint;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 * 
 * @author 
 *
 */
public class PaintSaveFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private ArrayList<PaintCommand> commands;// created as a result of the parse
	private ArrayList<Point> points = new ArrayList<Point>();
	String digits = "([0-9]+)";
	/**
	 * Below are Patterns used in parsing 
	 * There are many methods below due to case sensitive issues
	 */
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");
	private Pattern pCircleStart=Pattern.compile("^Circle$");
	private Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	private Pattern pPointStart=Pattern.compile("^points$");
	private Pattern pPointEnd=Pattern.compile("^endpoints$");
	private Pattern pRectangleStart=Pattern.compile("^Rectangle$");
	private Pattern pRectangleEnd=Pattern.compile("^EndRectangle$");
	private Pattern pSquiggleStart=Pattern.compile("^Squiggle$");
	private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");
	private Pattern pColor=Pattern.compile("^color[:]([0-9]+)[,]([0-9]+)[,]([0-9]+)");
	private Pattern pFilled=Pattern.compile("^filled[:](true|false)");
	private Pattern pCenter=Pattern.compile("^center[:]\\(([0-9]+)[,]([0-9]+)\\)");
	private Pattern pRadius=Pattern.compile("^radius[:]([0-9]+)");
	private Pattern pP1=Pattern.compile("^p1[:]\\(([0-9]+)[,]([0-9]+)\\)");
	private Pattern pP2=Pattern.compile("^p2[:]\\((-?[0-9]+)[,](-?[0-9]+)\\)");
	private Pattern pPoints=Pattern.compile("^point[:]\\((-?[0-9]+)[,](-?[0-9]+)\\)");

	/**
	 * Store an appropriate error message in this, including 
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}
	/**
	 * 
	 * @return the PaintCommands resulting from the parse
	 */
	public ArrayList<PaintCommand> getCommands(){
		return this.commands;
	}
	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}
	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 * 
	 * @param inputStream the open file to parse
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream) {
		this.commands = new ArrayList<PaintCommand>();
		this.errorMessage="";
		
		// During the parse, we will be building one of the 
		// following shapes. As we parse the file, we modify 
		// the appropriate shape.
		
		Circle circle = null; 
		Rectangle rectangle = null;
		Squiggle squiggle = null;
	
		try {	
			int state=0; Matcher m; String l;
			int currentShape = 0; //1 = circle, 2 = rectangle, 3 = sqiuggle  //used for 
			Color currentColor = null; boolean filled = false; 
			Point center = null, p1 = null, p2 = null, sPoint = null;
			int radius = 0;
			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) 
			{	
				this.lineNumber++;
				System.out.println(lineNumber+" "+l+" "+state);
				l.trim();
				l = l.replaceAll(" ", "");
				l = l.replaceAll("\t", "");
				while(l.equals(""))
				{
					l = inputStream.readLine();
					this.lineNumber++;
					System.out.println(lineNumber+" "+l+" "+state);
					l.trim();
					l = l.replaceAll(" ", "");
					l = l.replaceAll("\t", "");
				}
				switch(state){
					case 0:
						m=pFileStart.matcher(l);
						if(m.matches()){
							state=1;
							break;
						}
						error("Expected Start of Paint Save File");
						inputStream.close();
						return false;
					case 1: // Looking for the start of a new object or end of the save file
						
						m=pFileEnd.matcher(l);
						if(m.matches()){
							state=11; 
							break;
						}
						m=pCircleStart.matcher(l);
						if(m.matches()){
							currentShape = 1;
							state=2; 
							break;
						}
						m=pRectangleStart.matcher(l);
						if(m.matches())
						{
							currentShape = 2;
							state=2; 
							break;
						}
						m = pFileEnd.matcher(l);
						m=pSquiggleStart.matcher(l);
						if(m.matches())
						{
							squiggle = new Squiggle(); // we can declare this early as has no constructor
							currentShape = 3;
							state=2; 
							break;
						}
						error("Expected Shape or End of File");
						inputStream.close();
						return false;
					case 2: //color 
						m=pColor.matcher(l);
						if(m.matches())
						{
							int r = Integer.parseInt(m.group(1));
							int g = Integer.parseInt(m.group(2));
							int b = Integer.parseInt(m.group(3));
							state=3;
							
							if (r>255 || g>255 || b>255)
							{
								error("Expected Valid Color");
								inputStream.close();
								break;
							}
							currentColor = new Color(r,g,b);
							break;
						}
						error("Expected Color of Shape");
						inputStream.close();
						return false;
					case 3:
						m=pFilled.matcher(l);
						if(m.matches())
						{
							filled = false;
							if(m.group(1).equals("true"))
							{
								filled = true;
							}
							if(currentShape == 1) //circle
							{
								state = 4;
							}
							else if(currentShape == 2) //rectangle
							{
								state = 6;
							}
							else if(currentShape == 3) //squiggle
							{
								state = 8;
							}
							break;
						}
						error("Expected True or False");
						inputStream.close();
						return false;
					case 4:
						m=pCenter.matcher(l);
						if(m.matches())
						{
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							center = new Point(x,y);
							state = 5;
							break;
						}
						
						error("Expected x and y");
						inputStream.close();
						return false;
					case 5:
						m=pRadius.matcher(l);
						if(m.matches())
						{
							radius = Integer.parseInt(m.group(1));
							if(radius >= 0)
							{
								state = 10;
								break;
							}
							
						}
						error("Expected A Valid Radius");
						inputStream.close();
						return false;
					case 6:
						m=pP1.matcher(l);
						if(m.matches())
						{
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							p1 = new Point(x,y);
							state = 7;
							break;
						}
						error("Expected A Valid Point");
						inputStream.close();
						return false;
					case 7:
						m=pP2.matcher(l);
						if(m.matches())
						{
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							p2 = new Point(x,y);
							state = 10;
							break;
						}
						error("Expected A Valid Point");
						inputStream.close();
						return false;
					case 8:
						m=pPointStart.matcher(l);
						
						if(m.matches())
						{
							state = 9;
							break;
						}
						error("Expected a Start of Point");
						inputStream.close();
						return false;
					case 9:
						m=pPoints.matcher(l);
						if(m.matches())
						{
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							sPoint = new Point(x,y);
							state = 9;
							squiggle.add(sPoint);
							break;
						}
						m=pPointEnd.matcher(l);
						if(m.matches())
						{
							state = 10;
							break;
						}
						error("Expected a Valid Point or End of Point");
						inputStream.close();
						return false;
					case 10:
							m = pCircleEnd.matcher(l);
							if(m.matches() && currentShape == 1)
							{

								circle = new Circle(center, radius);
								circle.setColor(currentColor);
								circle.setFill(filled);
								CircleCommand circleCommand = new CircleCommand(circle);
								commands.add(circleCommand);
								circle = null;
								state = 1;
								break;
							}
							m = pRectangleEnd.matcher(l);
							if(m.matches() && currentShape == 2)
							{
								rectangle = new Rectangle(p1,p2);
								rectangle.setColor(currentColor);
								rectangle.setFill(filled);
								RectangleCommand rectangleCommand = new RectangleCommand(rectangle);
								commands.add(rectangleCommand);
								rectangle = null;
								state = 1;
								break;
							}
							m = pSquiggleEnd.matcher(l);
							if(m.matches()  && currentShape == 3)
							{
								//we can ignore if filled function in squiggle as squiggle does use it to draw
								squiggle.setColor(currentColor);
								SquiggleCommand squiggleCommand = new SquiggleCommand(squiggle);
								commands.add(squiggleCommand);
								squiggle = null;
								state = 1;
								break;
							}
							error("Expected Correct Shape Ending");
							inputStream.close();
							return false;
					case 11:
						state = 11;
						if(l.length() != 0)
						{
							error("Expected Nothing after End of Paint");
							inputStream.close();
							return false;
						}
				}
			}
			if(state == 11)
			{
				return true;
			}
			else
			{ 
				error("Expected End Paint Save File");
				return false;
			}
		}  
		catch (Exception e)
		{
			return false;
		}
	}
}



/*
made the regex and tested it out with a external program so if error occur, it not the regex.
CIRCLE
color....
String x = "([0-9]|[0-9][0-9]|[0-2][0-5][0-9])";   //can use "[0-9]+" but need to add constraints for > 255
Pattern p = Pattern.compile("^color[:]" + x + "[,]"+ x + "[,]"+ x);

filled....
Pattern p = Pattern.compile("^filled[:](true|false)")

center....
^center[:]\\(([0-9]+)[,]([0-9]+)\\)

radius....
^radius[:]([0-9]+)

 */

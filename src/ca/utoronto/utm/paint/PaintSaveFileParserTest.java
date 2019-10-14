package ca.utoronto.utm.paint;
import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.utoronto.utm.paint.PaintSaveFileParser;

public class PaintSaveFileParserTest {

	private Pattern pErrorMessage = Pattern.compile("^Error in line\\s+(\\d+)\\s+");
	private void doParserTestCase(String fileName, String message, String expectedErrorMessage) throws IOException {
		BufferedReader lineInput = new BufferedReader(new FileReader(fileName));
		PaintSaveFileParser parser = new PaintSaveFileParser();
		
		boolean retVal = parser.parse(lineInput);
		String errorMessage = parser.getErrorMessage();
		// System.out.println(fileName+" "+ errorMessage);

		if (expectedErrorMessage.equals("")) {
			assertTrue(fileName + ": Returns true for basic file with no spaces", retVal);
			assertEquals(fileName + ": No error message", "", errorMessage);
		} else {
			String reportedLine = "", expectedLine = "";

			assertFalse(fileName + ": Returns false for basic incorrect file format", retVal);
			// System.out.println(errorMessage);
			Matcher m;
			m = pErrorMessage.matcher(expectedErrorMessage);
			if (m.find())
				expectedLine = m.group(1);

			m = pErrorMessage.matcher(errorMessage);
			if (m.find())
				reportedLine = m.group(1);
			// System.out.println(expectedLine+":"+reportedLine);
			assertEquals(fileName + ": Error Message", expectedLine, reportedLine);
		}
		lineInput.close();
	}

	@Test
	public void parserTest1() throws IOException {
		this.doParserTestCase("samplefiles/basic_nospace.txt", "Returns true for basic file with no spaces", "");
	}

	@Test
	public void parserTest2() throws IOException {
		this.doParserTestCase("samplefiles/basic_spaces.txt", "Returns true for basic file with no spaces", "");
	}

	@Test
	public void parserTest3() throws IOException {
		this.doParserTestCase("samplefiles/basic_multispaces.txt", "Returns true for basic file with multiple spaces",
				"");
	}

	@Test
	public void parserTest4() throws IOException {
		this.doParserTestCase("samplefiles/basic_fail.txt", 
				"Returns false for basic incorrect file format", 
				"Error in line 1 ");
	}

	@Test
	public void parserTest5() throws IOException {
		this.doParserTestCase("samplefiles/circle_single.txt", "Returns true for file with one circle", "");
	}

	@Test
	public void parserTest6() throws IOException {
		this.doParserTestCase("samplefiles/circle_multi.txt", "Returns true for file with multiple circles", "");
	}

	@Test
	public void parserTest7() throws IOException {
		this.doParserTestCase("samplefiles/circle_fail_values.txt", "Returns false for circle with incorrect values",
				"Error in line 3 ");
	}

	@Test
	public void parserTest8() throws IOException {
		this.doParserTestCase("samplefiles/circle_fail_wrongend.txt", "Returns false for circle with wrong end", "Error in line 7 ");
	}

	@Test
	public void parserTest9() throws IOException {
		this.doParserTestCase("samplefiles/circle_fail_wrongorder.txt",
				"Returns false for circle with properties in wrong order", "Error in line 3 ");
	}

	@Test
	public void parserTest10() throws IOException {
		this.doParserTestCase("samplefiles/rectangle_single.txt", "Returns true for file with one rect", "");
	}

	@Test
	public void parserTest11() throws IOException {
		this.doParserTestCase("samplefiles/rectangle_multi.txt", "Returns true for file with multiple rect", "");
	}

	@Test
	public void parserTest12() throws IOException {
		this.doParserTestCase("samplefiles/rectangle_wrongorder.txt",
				"Returns false for rect with properties in wrong order", "Error in line 5 ");
	}

	@Test
	public void parserTest13() throws IOException {
		this.doParserTestCase("samplefiles/squiggle_single.txt", "Returns true for file with one circle", "");
	}

	@Test
	public void parserTest14() throws IOException {
		this.doParserTestCase("samplefiles/multishapes.txt", "Returns true for file with one circle", "");
	}

	@Test
	public void parserTest15() throws IOException {
		this.doParserTestCase("samplefiles/multishapes_fail_missingend.txt",
				"Returns false for multiple shapes file with incorrect end file line", "Error in line 48 ");
	}

	@Test
	public void parserTest16() throws IOException {
		this.doParserTestCase("samplefiles/multishapes_fail_missingendshape.txt",
				"Returns false for multiple shapes file with incorrect end shape", "Error in line 13 ");
	}
	//New Test Cases

	@Test
	public void parserTest17() throws IOException {
		this.doParserTestCase("samplefiles/mutiple_shapes.txt",
				"Returns true for multiple shapes", "");
	}
	
	@Test
	public void parserTest18() throws IOException {
		this.doParserTestCase("samplefiles/missing_paint_start_fail.txt",
				"Returns false for missing Paint Save File Version 1.0", "Error in line 1 ");
	}
	
	@Test
	public void parserTest19() throws IOException {
		this.doParserTestCase("samplefiles/missing_paint_end_fail.txt",
				"Returns false for missing End Paint Save File", "Error in line 345 ");
	}
	
	@Test
	public void parserTest20() throws IOException {
		this.doParserTestCase("samplefiles/extra_blank_lines.txt",
				"Returns true for file with extra blank lines", "");
	}
	
	@Test
	public void parserTest21() throws IOException {
		this.doParserTestCase("samplefiles/missing_point_end_fail.txt",
				"Returns false for missing end of point", "Error in line 326 ");
	}
	
	@Test
	public void parserTest22() throws IOException {
		this.doParserTestCase("samplefiles/missing_point_start_fail.txt",
				"Returns false for missing start of point", "Error in line 5 ");
	}
	
	@Test
	public void parserTest23() throws IOException {
		this.doParserTestCase("samplefiles/invalid_color_fail.txt",
				"Returns false for invalid color", "Error in line 3 ");
	}
	
	@Test
	public void parserTest24() throws IOException {
		this.doParserTestCase("samplefiles/invalid_filled_fail.txt",
				"Returns false for invalid filled", "Error in line 4 ");
	}
	
	@Test
	public void parserTest25() throws IOException {
		this.doParserTestCase("samplefiles/missing_shape_start_fail.txt",
				"Returns false for missing start of shape", "Error in line 328 ");
	}
	
	@Test
	public void parserTest26() throws IOException {
		this.doParserTestCase("samplefiles/missing_shape_end_fail.txt",
				"Returns false for missing end of shape", "Error in line 327 ");
	}
	
	@Test
	public void parserTest27() throws IOException {
		this.doParserTestCase("samplefiles/invalid_save.txt",
				"Returns false for invalid start line", "Error in line 1 ");
	}

	@Test
	public void parserTest28() throws IOException {
		this.doParserTestCase("samplefiles/negative_x_squiggle.txt",
				"Returns true for negative x value for squiggle", "");
	}
	
	@Test
	public void parserTest29() throws IOException {
		this.doParserTestCase("samplefiles/negative_y_squiggle.txt",
				"Returns true for negative y value for squiggle", "");
	}
	
	@Test
	public void parserTest30() throws IOException {
		this.doParserTestCase("samplefiles/negative_radius.txt", "Returns false for negative radius", "Error in line 6 ");
	}
	
	@Test
	public void parserTest31() throws IOException {
		this.doParserTestCase("samplefiles/negative_x_circle.txt", "Returns false for negative x vlaue of circle", "Error in line 5 ");
	}
	
	@Test
	public void parserTest32() throws IOException {
		this.doParserTestCase("samplefiles/negative_y_circle.txt", "Returns false for negative y vlaue of circle", "Error in line 5 ");
	}
	
	@Test
	public void parserTest33() throws IOException {
		this.doParserTestCase("samplefiles/negative_x_rect.txt",
				"Returns true for negative x value for rectangle", "");
	}
	
	@Test
	public void parserTest34() throws IOException {
		this.doParserTestCase("samplefiles/negative_y_rect.txt",
				"Returns true for negative y value for rectangle", "");
	}
	
	@Test
	public void parserTest35() throws IOException {
		this.doParserTestCase("samplefiles/zero_radius.txt",
				"Returns true for zero radius of a circle", "");
	}
	
}


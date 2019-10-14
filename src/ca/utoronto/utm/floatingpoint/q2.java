package ca.utoronto.utm.floatingpoint;

public class q2 {
	public static void main(String[] args) {
		q2 p = new q2();
		double x = 7.11;
		System.out.println(p.solve711(x));
	}

	/**
	 * takes any float number and returns 4 number that add and multiply to equal the inputed float number
	 * if no such 4 number exit then it returns "none".
	 * 
	 * this algorithm goes through 3 loop to find the first 3 number then with some math it calculates the fourth number 
	 * missing the last loop saves a lot of time as now the algorithm is x^3 instead of x^4. to make it even faster if there isn't
	 * a number found we decrease loop two and as loop one increases and loop three decreases by a lot when loop one and two increase.
	 * We use the property of "all four number add up to equal to input value" so we can subtract loop two and three bounds by a and loop 
	 * three by b as it staying within the input value bounds.
	 * 
	 * @param x any float number (in this case 7.11)
	 * @return returns 4 number that add up and multiply together to result in the value 
	 * 		
	 */
	public String solve711(double x) 
	{
		double a, b, c, d;
		double add = x*100;
		double mult = x*Math.pow(100, 4);
		for (a = 0; a < add; a++) 
		{
			for (b = 0; b < add-a; b++) 
			{
				for (c = 0; c < add-a-b; c++) 
				{
					d = add - a - b - c;
					if(a*b*c*d == mult && a+b+c+d == add)
					{
						return (a/100 + " " + b/100 + " " + c/100 + " " + d/100);
					}
				}
			}
		}
		return "none";
	}
}

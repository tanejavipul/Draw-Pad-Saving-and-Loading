package ca.utoronto.utm.floatingpoint;

import java.math.*;
import java.util.ArrayList;
public class q3 {
	// See https://docs.oracle.com/javase/8/docs/api/java/lang/Float.html
	// as well as the lecture notes.txt for Week11.
	
	/**
	 * Complete the code below, so that when executed, the output should exactly match 
	 * IEEE754SingleOut.txt included in this project. Do not modify main in any way. 
	 * Implement the methods below so that they perform as expected. You can add additional
	 * static constants as well as static helper methods if it helps.
	 */
	public static void main(String[] args) {
		System.out.println("0 to 10");
		for (float i = 0.0f; i <= 10.0f; i++) {
			System.out.println(binRep(i));
		}
		
		System.out.println("misc");
		System.out.println(binRep(-6.8f));
		System.out.println(binRep(23.1f));
		System.out.println(binRep(14.625f));
		System.out.println(binRep(.1f));
		System.out.println(binRep(5.75f));
		System.out.println(binRep(1.0f / 3.0f));
		
		System.out.println("Machine Epsilon");
		float me = machineEpsilon();
		System.out.println("Machine Epsilon = " + binRep(me));
		System.out.println("1+machine epsilon = " + binRep(1.0f + me));
		System.out.println("Underflow");
		System.out.println("Underflow = " + binRep(underflow()));

		// System.out.println("rounds by");
		System.out.println("Overflow");
		System.out.println("Overflow = " + binRep(overflow()));
		System.out.println("MAX_VALUE = " + binRep(Float.MAX_VALUE));
	}
	/**
	 * Search for machine epsilon (eps), that is, the smallest
	 * float such that 1+eps>1. 
	 * Print out progress along the way.
	 * 
	 * @return machine epsilon
	 */
	public static float machineEpsilon() {
		float me = 1.0f;
		float meNew = 1.0f;
		ArrayList<Float> obj = new ArrayList<Float>();
		
		for (int x = 0; x <= 24; x++) {
			meNew = (float) (1/Math.pow(2, x));
			obj.add(meNew);
			System.out.println(binRep(me + meNew));
		}
		
		return (obj.get(obj.size() - 2));
	}

	/**
	 * Search for underflow, that is the smallest float
	 * number that is greater than 0. 
	 * Print out progress along the way.
	 * @return underflow
	 */
	public static float underflow() {
		float me = 0.0f;
		float meNew = 1.0f;
		ArrayList<Float> obj = new ArrayList<Float>();
		int x = 0;
		
		while (meNew > 0.0f) {
				meNew = (float) (1/Math.pow(2, x));
				obj.add(meNew);
				x += 1;
				System.out.println(binRep(me + meNew));
			}
		
		return (obj.get(obj.size() - 2));
	}

	/**
	 * Search for overflow, the largest float, 
	 * by first finding the largest exponent, and
	 * then finding the largest mantissa. 
	 * Print out progress along the way.
	 * @return overflow
	 */
	
	public static float overflow() {
		
		System.out.println("Maximum Exponent");
		
		float ofl = 0.0f;
		float oflNew = 1.0f;
		ArrayList<Float> obj = new ArrayList<Float>();
		
		for (int x = 0; x <= 127; x++) {
			oflNew = (float)Math.pow(2, x);
			obj.add(oflNew);
			System.out.println(binRep(ofl + oflNew));
		}
		
		System.out.println("Maximum Mantissa");
		
		int count = 9;
		float y = (float) ofl+oflNew;
		
		System.out.println(binRep(y));
		String first = floatToBinary(y);
		
		while (count<32) {
			String r = String.format("%32s", first).replace(' ', '0');
			String addOne = r.substring(count).replaceFirst("0", "1");
			y = binaryToFloat(r.substring(0,count) + addOne); 
			first = r.substring(0,count) + addOne;
			System.out.println(binRep(y));
			count++;
		}

		return (y);
	}

	/**
	 * Take apart a floating point number and return a string representation of it.
	 * @param d the floating point number to investigate
	 * @return By example, this method returns strings like...
	 * 
	 * binRep(0.0f) returns "0[00000000]00000000000000000000000=+0.00000000000000000000000x2^(0)=0.0"
	 * binRep(1.0f) returns "0[01111111]00000000000000000000000=+1.00000000000000000000000x2^(0)=1.0"
	 * binRep(2.0f) returns "0[10000000]00000000000000000000000=+1.00000000000000000000000x2^(1)=2.0"
	 * binRep(14.625f) returns "0[10000010]11010100000000000000000=+1.11010100000000000000000x2^(3)=14.625"
	 * binRep(0.1f) returns "0[01111011]10011001100110011001101=+1.10011001100110011001101x2^(-4)=0.1"
	 */
	// Return information about the representation of floating point number d
	public static String binRep(float d) {

		int sign = 0;
		if (d<0) {sign = 1;}
		int decimal = 1;
		int l = Float.floatToRawIntBits(d); // Use this to pull bits of d
		
		String binary = String.format("%32s", Integer.toBinaryString(l)).replace(' ', '0');
		String sSign = Integer.toString(sign);
		String sExponent = binary.substring(1,9);
		String sMantissa = binary.substring(9);
		int exponent = Integer.parseInt(sExponent, 2) - 127;
		
		if (d == 0.0f) {
			decimal = 0;
			exponent = 0;
		} else {
			if (exponent < -126) {
				decimal = 0;
				exponent = -126;
			} else if (exponent > 127) {
				decimal = 0;
				exponent = 127;
			}
		}
		
		String s = sSign + "[" + sExponent + "]" + sMantissa;
		String t = (sign == 0) ? "+" : "-";
		t = t + decimal + "." + sMantissa + "x2^(" + exponent + ")";
		
		return (s + "=" + t + "=" + d);
		
	}
	
	/**
	 * @author csc207 lecture
	 * @param x the floating point number to investigate
	 * @return x converted into a binary string
	 */
	
	static String floatToBinary(float x) {
		int intBits = Float.floatToRawIntBits(x);
		String s = Integer.toBinaryString(intBits);
		return String.format("%32s", s).replace(' ', '0');
	}
	
	/**
	 * @author csc207 lecture
	 * @param s the binary string to investigate
	 * @return s converted into a float
	 */
	
	static float binaryToFloat(String s) {
		if (s.length() != 32) {
			System.out.println("Not a valid binary string for float, must be 32-bit");
			return Float.NaN;
		}
		// Use long integer first then cast down to int
		long intBits = Long.parseLong(s, 2);
		return Float.intBitsToFloat((int)intBits);
	}
}
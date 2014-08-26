import java.awt.Color;


public class ColorUtil 	
{
	/*
	 * Code from http://www.camick.com/java/source/HSLColor.java
	 */
	public static float[] fromRGB(Color color)
	{
		//  Get RGB values in the range 0 - 1

		float[] rgb = color.getRGBColorComponents( null );
		float r = rgb[0];
		float g = rgb[1];
		float b = rgb[2];

		//	Minimum and Maximum RGB values are used in the HSL calculations

		float min = Math.min(r, Math.min(g, b));
		float max = Math.max(r, Math.max(g, b));

		//  Calculate the Hue

		float h = 0;

		if (max == min)
			h = 0;
		else if (max == r)
			h = ((60 * (g - b) / (max - min)) + 360) % 360;
		else if (max == g)
			h = (60 * (b - r) / (max - min)) + 120;
		else if (max == b)
			h = (60 * (r - g) / (max - min)) + 240;

		//  Calculate the Luminance

		float l = (max + min) / 2;

		//  Calculate the Saturation

		float s = 0;

		if (max == min)
			s = 0;
		else if (l <= .5f)
			s = (max - min) / (max + min);
		else
			s = (max - min) / (2 - max - min);

		return new float[] {h, s * 100, l * 100};
	}
}

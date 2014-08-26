import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class PointAnalysis {
	private BufferedImage img;
	private Rectangle rect;
	private int[] red, green, blue;
	
	public PointAnalysis(BufferedImage img, Rectangle r, double scalingFactor)
	{
		this.img = img;
		rect = r;
		int rows = (int)(rect.height/scalingFactor);
		int cols = (int)(rect.width/scalingFactor);
		int xInImage = (int)(rect.x/scalingFactor);
		int yInImage = (int)(rect.y/scalingFactor);
		int[] rgbArray = new int[rows*cols];
		red = new int[rgbArray.length];
		green = new int[rgbArray.length];
		blue = new int[rgbArray.length];
		this.img.getRGB(xInImage, yInImage, cols, rows, rgbArray, 0, cols);
		int curr;
		for(int i = 0; i < rgbArray.length; i++)
		{
			curr = rgbArray[i];
			red[i] = (curr >> 16) & 0x000000FF;
			green[i] = (curr >> 8) & 0x000000FF;
			blue[i] = curr & 0x000000FF;
		}	
		JFrame blah = new JFrame();
		blah.setBounds( 700, 0, 256*Histogram.BIN_DISPLAY_WIDTH + 40, 
				3*Histogram.MAX_DISPLAY_HEIGHT + 80);
		Container pane = blah.getContentPane();
		pane.setLayout(new GridLayout(3, 1));
		pane.add(new Histogram(red, Color.red));
		pane.add(new Histogram(green, Color.green));
		pane.add(new Histogram(blue, Color.blue));
		
		blah.setVisible(true);
	}
	
	public String toString()
	{
		return  "[" + getMin(red) + ", "  + getMax(red) + "]\n"+
				"[" + getMin(green) + ", "  + getMax(green) + "]\n"+
				"[" + getMin(blue) + ", "  + getMax(blue) + "]";
	}
	
	public int getMax(int[] arr)
	{
		if(arr == null || arr.length < 1)
			return -1;
		int max = arr[0];
		for(int x: arr)
			if(x > max)
				max = x;
		return max;
	}
	
	public int getMin(int[] arr)
	{
		if(arr == null || arr.length < 1)
			return -1;
		int min = arr[0];
		for(int x: arr)
			if(x < min)
				min = x;
		return min;
	}
	
}

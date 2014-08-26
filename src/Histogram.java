import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JPanel;

/* This class expects an array of pixel values (one channel) as its input.
 * These values are not sorted and they are not alreadd a histogram.
 */
public class Histogram extends JPanel
{
	private int[] bins;
	
	private BufferedImage image;
	
	public static final int BIN_DISPLAY_WIDTH = 3;
	public static final int MAX_DISPLAY_HEIGHT = 300;
	private final int bottomGutterHeight = 25;

	private int maxHistValue;
	
	private Color drawColor;
	
	public Histogram(int[] pixelValues)
	{
		this(pixelValues, Color.BLACK);
	}
	
	public Histogram(int[] pixelValues, Color drawClr)
	{
		drawColor = drawClr;
		System.out.println(drawColor);
		bins = new int[256];
		Arrays.fill(bins, 0);
		for(int currValue: pixelValues)
			bins[currValue]++;
		maxHistValue = 0;
		for(int curr: bins)
			if(curr > maxHistValue)
				maxHistValue = curr;
		generateImage();
	}
	
	private void generateImage() {
		image = new BufferedImage(256*BIN_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT, 
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 256*BIN_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT);
		g.setColor(drawColor);
		g.setFont(new Font("SanSerif", Font.PLAIN, 24));
		System.out.println(g.getColor());
		int x = 0;
		FontMetrics fm = g.getFontMetrics();
		for(int currValue: bins)
		{
			g.fillRect(x * BIN_DISPLAY_WIDTH, 
					MAX_DISPLAY_HEIGHT -(int)((double)currValue/maxHistValue*MAX_DISPLAY_HEIGHT) - bottomGutterHeight, 
					BIN_DISPLAY_WIDTH, (int)((double)currValue/maxHistValue*MAX_DISPLAY_HEIGHT));
			if(x%20 == 0)
			{
				Color currColor = g.getColor();
				g.setColor(Color.black);
				int halfStringWidth = (int)(fm.getStringBounds(""+x, g).getWidth()/2);
				g.drawString(""+x, x*BIN_DISPLAY_WIDTH - (x!=0?halfStringWidth:0), MAX_DISPLAY_HEIGHT);
				g.drawLine(x*BIN_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT - bottomGutterHeight, 
						x*BIN_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT - bottomGutterHeight - 10);
				g.setColor(currColor);
			}

			x++;	
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(image != null)
			g.drawImage(image, 0, 0, null);
	}
	
}

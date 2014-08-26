import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class DnDJPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener
{
	private BufferedImage image, overlayImage;
	private Rectangle rect;
	private boolean dragging = false;
	private String colorInfo = "";
	private double drawScalingFactor = 1.0;
	
	public DnDJPanel()
	{
		
	}
	
	public void setImage(BufferedImage img)
	{
		image = img;
		overlayImage = null;
		drawScalingFactor = 1.0;
	}
	
	public void drawOverlay(RangeSlider redSlider, RangeSlider greenSlider, RangeSlider blueSlider )
	{
		if(image == null)
			return;
		
		int rMin = redSlider.getValue(), rMax = redSlider.getValue() + redSlider.getExtent();
		int gMin = greenSlider.getValue(), gMax = greenSlider.getValue() + greenSlider.getExtent();
		int bMin = blueSlider.getValue(), bMax = blueSlider.getValue() + blueSlider.getExtent();
		
		overlayImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		
		int highLightColor = new Color(255, 192, 203).getRGB();
		int currPixel, currRed, currGreen, currBlue;
		for(int row = 0; row < image.getHeight(); row++)
		{
			for(int col = 0; col < image.getWidth(); col++)
			{
				currPixel = image.getRGB(col, row);
				currRed = (currPixel >> 16) & 0x000000FF;
				currGreen = (currPixel >> 8) & 0x000000FF;
				currBlue = currPixel & 0x000000FF;
				
				
				overlayImage.setRGB(col, row, currPixel);
				
				if( inRangeInc(currRed, rMin, rMax) && inRangeInc(currGreen, gMin, gMax)
						&& inRangeInc(currBlue, bMin, bMax) )
				{
					overlayImage.setRGB(col, row, highLightColor);
					
				}
				
					
				
			}
		}
		repaint();
	}
	
	public boolean inRangeInc(int targ, int low, int high)
	{
		return targ >= low && targ <= high;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(image != null)
			g.drawImage(image, 0, 0, (int)(image.getWidth()*drawScalingFactor), 
					(int)(image.getHeight()*drawScalingFactor), null);
		
		if(rect != null)
			g.drawRect(rect.x, rect.y, rect.width, rect.height);
		
		if(overlayImage != null)
			g.drawImage(overlayImage, (int)(image.getWidth()*drawScalingFactor) + 10, 0, 
					(int)(overlayImage.getWidth()*drawScalingFactor), 
					(int)(overlayImage.getHeight()*drawScalingFactor), null);
		g.setFont(new Font("SanSerif", Font.PLAIN, 36));
		g.drawString(colorInfo, 10, getHeight());
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(dragging)
		{
			rect = new Rectangle(rect.x, rect.y, 
						evt.getX() - rect.x, evt.getY() - rect.y);
			repaint();
		}
		System.out.println("dragging + " + dragging);
	//	repaint();
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
		int x = evt.getX(), y = evt.getY();
		
		if(image != null && x < image.getWidth() && y < image.getHeight())
		{
			int pixel = image.getRGB(x, y);
			
			colorInfo = "loc: (" + x + ", " + y + " [" + ((pixel >> 16) & 0x000000FF) + ", " +
						((pixel >> 8) & 0x000000FF) + ", " +
						(pixel & 0x000000FF) + "]";
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		dragging = true;
		rect = new Rectangle((int)(evt.getX()), (int)(evt.getY()), 5, 5);
		System.out.println("pressed");
		repaint();
		
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		dragging = false;
		if(		rect!=null && image!=null &&
				rect.x + rect.width <= image.getWidth()*drawScalingFactor && 
				rect.y + rect.height <= image.getHeight()*drawScalingFactor
		)
			System.out.println(new PointAnalysis(image, rect, drawScalingFactor));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent evt) {
		
		if(evt.getWheelRotation() > 0)
			drawScalingFactor += .1;
		else
			drawScalingFactor -= .1;
		
		if(drawScalingFactor < .1)
			drawScalingFactor = .1;
		
		repaint();
		
		System.out.println(drawScalingFactor);
	}
}


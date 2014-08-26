import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class DragDropTest implements ChangeListener
{
	
	
	private DnDJPanel panel;
	private RangeSlider redSlider, greenSlider, blueSlider;
	
	public DragDropTest()
	{
		
		panel = new DnDJPanel();
		panel.setBackground(Color.white);
		panel.addMouseListener(panel);
		panel.addMouseMotionListener(panel);
		panel.addMouseWheelListener(panel);
		panel.setDropTarget( new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {

                

                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                Transferable t = dtde.getTransferable();
                List fileList = null;
                try {
                    fileList = (List) t
                            .getTransferData(DataFlavor.javaFileListFlavor);
                } catch (UnsupportedFlavorException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                File f = (File) fileList.get(0);
                try {
					BufferedImage img = ImageIO.read(f);
					panel.setImage(img);
					panel.repaint();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                catch(Exception e)
                {
                	e.printStackTrace();
                }
              //  JOptionPane.showMessageDialog(null, f.getAbsolutePath());
             //   super.drop(dtde);
            }
        });
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		
		
		panel.setPreferredSize(new Dimension(5000,5000));
		JScrollPane scrolly = new JScrollPane(panel);
		scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrolly, BorderLayout.CENTER);
		
		
		redSlider = new RangeSlider(0, 255);
		redSlider.setValue(0);
		redSlider.setExtent(255);
		redSlider.addChangeListener(this);
		redSlider.setMajorTickSpacing(20);
		redSlider.setPaintTicks(true);
		redSlider.setPaintLabels(true);
		greenSlider = new RangeSlider(0, 255);
		greenSlider.setValue(0);
		greenSlider.setExtent(255);
		greenSlider.setMajorTickSpacing(20);
		greenSlider.setPaintTicks(true);
		greenSlider.setPaintLabels(true);
		greenSlider.addChangeListener(this);
		blueSlider = new RangeSlider(0, 255);
		blueSlider.setValue(0);
		blueSlider.setExtent(255);
		blueSlider.setMajorTickSpacing(20);
		blueSlider.setPaintTicks(true);
		blueSlider.setPaintLabels(true);
		blueSlider.addChangeListener(this);
		Box topBox = Box.createHorizontalBox();
		topBox.add(redSlider);
		topBox.add(Box.createHorizontalStrut(80));
		topBox.add(greenSlider);
		topBox.add(Box.createHorizontalStrut(80));
		topBox.add(blueSlider);
		topBox.add(Box.createVerticalStrut(50));
		frame.getContentPane().add(topBox, BorderLayout.NORTH);
		
		frame.setSize(1900,1000);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception allOfThem)
		{
			allOfThem.printStackTrace();
		}
		new DragDropTest();
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if((evt.getSource() == redSlider && !redSlider.getValueIsAdjusting()) ||
				(evt.getSource() == greenSlider && !greenSlider.getValueIsAdjusting()) ||	
				(evt.getSource() == blueSlider && !blueSlider.getValueIsAdjusting()) )
		{
			updateImage();
			System.out.println("updating image overlay");
		}
		
	}
	
	public void updateImage()
	{
		panel.drawOverlay(redSlider, greenSlider, blueSlider);
	}
}

//package pohplanner;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Creates the background image when in Room View.
 */
class RoomBackground extends JPanel {
	private static final long serialVersionUID = 783106628387333576L;
	
	Image image;
	  public RoomBackground(String img)
	  {
	    try
	    {
	      image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource(img), img));
	    }
	    catch (Exception e) { }
	  }

	  @Override
	  protected void paintComponent(Graphics g)
	  {
	    super.paintComponent(g); 
	    if (image != null)
	      g.drawImage(image, 0,0,this.getWidth(),this.getHeight(),this);
	  }
	
	/*private Image img;

	  public RoomBackground(String img) {
	    this(new ImageIcon(img).getImage());
	  }

	  public RoomBackground(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  }

	  public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, null);
	  }*/
	  
}
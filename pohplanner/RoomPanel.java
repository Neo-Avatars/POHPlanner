package pohplanner;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

class RoomPanel extends JPanel {
    private static final long serialVersionUID = 2428606422339652098L;
    private Image image;
    public Room room; 
    
    /**
     * 
     * @param image
     * @param room
     */
    public RoomPanel( String image, Room room ) {
        this( new ImageIcon( image ).getImage(), room );
    }
    
    /**
     * 
     * @param image
     * @param room
     */
    public RoomPanel( Image image, Room room ) {
      this.image = image;
      this.room = room;
      Dimension size = new Dimension( image.getWidth( null ), image.getHeight( null ) );
      setPreferredSize( size );
      setMinimumSize( size );
      setMaximumSize( size );
      setSize( size );
      setLayout( null );
    }
    
    /**
     * 
     */
    public void paintComponent(Graphics g) {
        super.paintComponent( g );
        
        Graphics2D g2d = ( Graphics2D ) g;
        drawHotspots( g2d );
    }
    
    /**
     * 
     * @param g
     */
    private void drawHotspots( Graphics2D g ) {
        Hotspot[] hotspots = this.room.getHotspots();
        Polygon poly = null;
        
        /* Draw image "onto" panel */
        g.drawImage( image, 0, 0, null );
        /* Backgroud/fill color */
        g.setPaint( Color.white );
        /* Transparency for hotspots */
        g.setComposite( makeComposite( 0.5F ) );
        
        /* Iterate over hotspots and generate polygons */
        for( int i = 0; i < hotspots.length; i++ ) {
            poly = new Polygon( hotspots[ i ].getXCoords(), hotspots[ i ].getYCoords(), hotspots[ i ].getNumberOfPoints() );
            g.fill( poly );
            g.drawPolygon( poly );
        }
    }
    
    /**
     * Creates a composite used for creating transparency.
     * 
     * @param alpha The degree of opaqueness.
     * @return AlphaComposite The composite instance.
     */
    private AlphaComposite makeComposite( float alpha ) {
        return AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha );
    }
  }
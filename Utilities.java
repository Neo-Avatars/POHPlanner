package pohplanner;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.swing.ImageIcon;

public class Utilities {
    private static final int MAX_IMAGE_SIZE = 102400;
    
    public ImageIcon loadImage(String path ) {
        int count = 0;
        
        BufferedInputStream imgStream = new BufferedInputStream( this.getClass().getResourceAsStream( path ) );
        
        if ( imgStream == null ) {
            System.err.println("Couldn't find file: " + path);
            return null;        
        }
        
        byte buffer[] = new byte[ MAX_IMAGE_SIZE ];
        
        try {
            count = imgStream.read( buffer );
            imgStream.close();
        } catch ( IOException e ) {
            System.err.println( "Couldn't read stream from file: " + path );
            return null;
        }
        
        if ( count < 1 ) {
            System.err.println( "Empty file: " + path );
            return null;
        }
        
        return new ImageIcon( Toolkit.getDefaultToolkit().createImage( buffer ) );
    }
}

package pohplanner;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Limits the input of a field to a specific length.
 * 
 * @author Rick (Salmoneus)
 * @version 2.1
 * @date 04 January 2009
 */
public class TextFieldLimiter extends PlainDocument {
    private static final long serialVersionUID = 1487814374796100390L;
    int maxLength = -1;

    public TextFieldLimiter( int length ) {
        this.maxLength = length;
    }

    public void insertString( int offs, String str, AttributeSet a ) throws BadLocationException {
        if( str != null && maxLength > 0 && this.getLength() + str.length() > maxLength ) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        super.insertString( offs, str, a );
    }
}
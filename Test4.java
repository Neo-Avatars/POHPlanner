import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JApplet;
import javax.swing.JPanel;


public class Test4 extends JApplet {

	private static final long serialVersionUID = -8957897083350854781L;
	
	
	Color polycol = new Color(89,63,255,128);
	Rectangle rect = new Rectangle(30,40,50,60);
	
	JPanel corePanel = new JPanel();
	
	public void init() {
		createAndShowGUI();
	}
	
	public void createAndShowGUI() {
		//this.setForeground( Color.white );
		add(corePanel);
	    this.setSize(715, 544);
	    this.setVisible( true );

	    Graphics g = getGraphics();
	    int[] xpoints = {20,29,37,58,33,169,125};
		int[] ypoints = {26,67,78,28,63,69,165};
		Polygon poly = new Polygon(xpoints,ypoints,xpoints.length);
		
		g.setColor(polycol);
		
		g.fillPolygon(poly);
	    
	    System.out.println(this);
	    System.out.println(g);
	    System.out.println(poly);
	    
	}
	
	public void paint(Graphics g){
		/*
		int[] xpoints = {20,29,37,58,33,169,125};
		int[] ypoints = {26,67,78,28,63,69,165};
		Polygon poly = new Polygon(xpoints,ypoints,xpoints.length);
		
		//Graphics g1 = getGraphics();
	    //Graphics2D g = (Graphics2D) g1;

		g.setColor(polycol);
		
		g.drawPolygon(poly);
		//g.fill(rect);
	    
	    System.out.println(this);
	    System.out.println(g);
	    //System.out.println(poly);
	    System.out.println(rect);

		*/
	}
	
    
}


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class Test1 extends JApplet implements ActionListener, MouseListener{

	private static final long serialVersionUID = -4986474296742496209L;

	private ImageIcon icon = new ImageIcon( this.getClass().getResource( "icongarden.png" ) );
	private JLabel iconLabel = new JLabel("Room", icon, JLabel.CENTER);
	
	private ImageIcon room1 = new ImageIcon( this.getClass().getResource( "1Garden.png" ) );
	private JLabel roomLabel1 = new JLabel("", room1, JLabel.CENTER);
	
	private ImageIcon room2 = new ImageIcon( this.getClass().getResource( "1Parlour.png" ) );
	private JLabel roomLabel2 = new JLabel("", room2, JLabel.CENTER);
	
	private int status = 0;
	private int counter = 0;
	private String counterTxt = Integer.toString(counter);
	
	//private ImageIcon room3 = new ImageIcon( this.getClass().getResource( "15Workshop.png" ) );
	//private JLabel roomLabel3 = new JLabel("", room3, JLabel.CENTER);
	
	private JPanel corePanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel smallPanel = new JPanel();
	private JPanel smallTop = new JPanel();
	private JPanel smallMid = new JPanel();
	private JPanel smallBottom = new JPanel();
	private JPanel smallBottom2 = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	//smallPanel.setSize(513,179);
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mainMenu = new JMenu( "Menu" );
	private JMenuItem menuItem = new JMenu( "Item" );
	
	private Color bgblue = new Color(83, 115, 191);
	private Color bgcream = new Color(217, 198, 163);
	
	Border blueBorder = BorderFactory.createLineBorder(new Color(100, 136, 201));
    Border titledBorder = BorderFactory.createTitledBorder( blueBorder,
            "Special Info", TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, null, Color.white );
    Border roomBorder1 = BorderFactory.createBevelBorder(0, new Color(27, 24, 17), new Color(42, 38, 28), new Color(94, 83, 58), new Color(101, 91, 57));
    Border roomBorder2 = BorderFactory.createBevelBorder(0, new Color(42, 36, 21), new Color(42, 38, 28), new Color(101, 91, 67), new Color(106, 98, 75));
    Border roomBorder = BorderFactory.createCompoundBorder(roomBorder2, roomBorder1);
	
	JButton small = new JButton("Small");
	JButton right = new JButton("Right");
	
	boolean gridView = true;
	
	public void init() {

		createAndShowGUI();
		createAndShowRoom();
		
	}    
	
	
	public void createAndShowGUI() {
		System.out.println(this);
		
		this.setForeground( Color.white );
	    this.setSize(715, 544);
	    this.setVisible( true );
	    this.add(corePanel);
	    
	    System.out.println(this);
	    
	    menuBar.add(mainMenu);
	    mainMenu.add(menuItem);
	    
	    setJMenuBar(menuBar);
	    
	    
	    corePanel.setLayout(new GridBagLayout());
	    leftPanel.setLayout(new GridBagLayout());
	    rightPanel.setLayout(new FlowLayout());
	    mainPanel.setLayout(new GridLayout(9, 9, 0, 0));
	    smallPanel.setLayout(new BorderLayout());
	    smallTop.setLayout(new FlowLayout());
	    smallMid.setLayout(new FlowLayout());
	    smallBottom.setLayout(new FlowLayout());
	    smallBottom2.setLayout(new FlowLayout());
	    //mainPanel.setSize(513, 513);
	    
	    //rightPanel.setSize(200, 513);
	    mainPanel.setBorder(roomBorder);
	    smallPanel.setBorder(roomBorder);
	    rightPanel.setBorder(roomBorder);
	    
        rightPanel.setBackground( bgblue );
        corePanel.setBackground( bgblue );
        leftPanel.setBackground( bgblue );
        smallPanel.setBackground( bgcream );
        
        //rightPanel.setBorder(titledBorder);
	    right.addMouseListener(this);
	    rightPanel.add(right);
	    
	    GridBagConstraints coreConstraints = new GridBagConstraints();
	    coreConstraints.fill = GridBagConstraints.VERTICAL;
	    coreConstraints.anchor = GridBagConstraints.EAST;
	    coreConstraints.weighty = 1.0;
	    corePanel.add(leftPanel, coreConstraints);
	    //GridBagConstraints core = new GridBagConstraints();
	    coreConstraints.anchor = GridBagConstraints.WEST;
	    coreConstraints.weightx = 1.0;
	    coreConstraints.fill = GridBagConstraints.BOTH;
	    corePanel.add(rightPanel, coreConstraints);
	    
    
	    corePanel.setVisible( true );
	    mainPanel.setVisible( true );
	    rightPanel.setVisible( true );
	    leftPanel.setVisible( true );
	    

	    
	}
    
	
	public void createAndShowGrid() {
		
		gridView = true;
		
		leftPanel.removeAll();
		mainPanel.removeAll();
		mainPanel.setLayout(new GridLayout(9, 9, 0, 0));
		
		GridBagConstraints layoutConstraints = new GridBagConstraints();
	    layoutConstraints.fill = GridBagConstraints.BOTH;
	    layoutConstraints.anchor = GridBagConstraints.NORTH;
	    layoutConstraints.weightx = 1.0;
	    layoutConstraints.weighty = 1.0;
	    leftPanel.add(mainPanel, layoutConstraints);
	    
	    for(int i = 0;i<9;i++) {
	    	for(int j = 0;j<9;j++){
	    		mainPanel.add(new JLabel("", icon, JLabel.CENTER));
	    	}
	    }
	    repaint();
		
	}
	
	
	public void createAndShowRoom() {
		
		gridView = false;
		
		leftPanel.removeAll();
		mainPanel.removeAll();
	    mainPanel.setLayout(new GridLayout(1, 1, 0, 0));

	    System.out.println(this);
	    
	    roomLabel1.addMouseListener(this);
	    roomLabel2.addMouseListener(this);
	    
	    mainPanel.add(roomLabel1);
	    status = 1;

	    //mainPanel.setSize(513, 334);
	    //smallPanel.setSize(513, 170);
	    
	    leftPanel.removeAll();
	    
	    GridBagConstraints leftConstraints = new GridBagConstraints();
	    
	    leftConstraints.gridy = 0;
	    leftConstraints.fill = GridBagConstraints.HORIZONTAL;
	    leftConstraints.anchor = GridBagConstraints.NORTH;
	    leftConstraints.weightx = 1.0;
	    leftPanel.add(mainPanel, leftConstraints);
	    
	    leftConstraints.weighty = 1.0;
	    leftConstraints.gridy = 1;
	    leftConstraints.fill = GridBagConstraints.BOTH;
	    leftConstraints.anchor = GridBagConstraints.SOUTH;
	    leftPanel.add(smallPanel, leftConstraints);
	    
	    smallPanel.setForeground( Color.orange );
	    small.addMouseListener(this);
	    small.setToolTipText(counterTxt);
	    smallPanel.add(smallTop, BorderLayout.NORTH);
	    smallPanel.add(smallMid, BorderLayout.CENTER);
	    smallPanel.add(smallBottom, BorderLayout.SOUTH);
	    smallPanel.add(smallBottom2, BorderLayout.SOUTH);
	    smallMid.add(small);
	    smallTop.add( new JButton("hello world top"));
	    smallBottom.add( new JButton("hello world b"));
	    smallBottom2.add( new JButton("hello world b2"));
	    smallPanel.setVisible( true );
	    
	    System.out.println(this);
	    
	    repaint();
		
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == small) {
			
			if (status == 1){
				mainPanel.removeAll();
			    System.out.println(this);
			    System.out.println(status);
			    mainPanel.add(roomLabel2);
			    roomLabel2.addMouseListener(this);
			    status = 2;
			    small.setText("Status: 2");
			    counter++;
			    counterTxt = Integer.toString(counter);
			    small.setToolTipText(counterTxt);
			    repaint();
			} else if (status == 2){
				mainPanel.removeAll();
			    System.out.println(this);
			    System.out.println(status);
			    mainPanel.add(roomLabel1);
			    roomLabel1.addMouseListener(this);
			    status = 1;
			    small.setText("Status: 1");
			    counter++;
			    counterTxt = Integer.toString(counter);
			    small.setToolTipText(counterTxt);
			    repaint();
			} else {
				
			}
			
		} else if (e.getSource() == roomLabel2) {
			small.setText("lolpwntby#2");
		    System.out.println(this);
		    System.out.println("Magic");
		    mainPanel.removeAll();
		    mainPanel.add(roomLabel1);
		    repaint();
			
		} else if (e.getSource() == right) {
			System.out.println("right");
			if(gridView)createAndShowRoom();
			else createAndShowGrid();
		} else {
			
		}
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}

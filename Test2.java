import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class Test2 extends JApplet implements ActionListener, MouseListener {

	private static final long serialVersionUID = -4986474296742496209L;

	private int status = 0;
	private int counter = 0;
	private String counterTxt = Integer.toString(counter);
	
	private JPanel corePanel = new JPanel();
	private JPanel overviewPanel = new JPanel();
	private JPanel roomPanel = new JPanel();
	private JPanel roomInfoPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	
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
	
	JButton rightButton = new JButton("Right");
	
	GridLayout mainRoomGridLayout = new GridLayout(1, 1, 0, 0);
	GridLayout mainOverviewLayout = new GridLayout(9, 9, 0, 0);
	
	boolean viewingGrid;
	
	LinkedHashMap<Object, Room> roomHash = new LinkedHashMap<Object, Room>();
	
	int overviewViewingLevel = 0;
	String lastViewedRoom;
	String overviewSelectedRoom = "";
	
	Color polycol = new Color(89,63,255,128);
	
	public void init() {
		createAndShowGUI();
		createAndShowRightPanelContent();
		initRoomArray();
		createAndShowGrid(1);
	}
	
	public void initRoomArray(){

		String hashKey;
		
		for(int i = 0;i < 3;i++){
			for(int j = 0;j < 9; j++){
				for(int k = 0;k < 9; k++){
					hashKey = "overviewRoom" + Integer.toString(i) + Integer.toString(j) + Integer.toString(k);
					
					if(i != 0){
						roomHash.put( hashKey , new Room("EmptyFloor1", i));
						roomHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
					} else {
						roomHash.put( hashKey , new Room("EmptyDungeon", 0));
						roomHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
					}
				}
			}
		}
		
		//add a parlour and garden as a default house layout
		hashKey = "overviewRoom" + Integer.toString(1) + Integer.toString(3) + Integer.toString(4);
		roomHash.put( hashKey , new Room("Parlour", 1));
		roomHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
		hashKey = "overviewRoom" + Integer.toString(1) + Integer.toString(4) + Integer.toString(4);
		roomHash.put( hashKey , new Room("Garden", 1));
		roomHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
	}
	
	public void createAndShowGUI() {
		
		System.out.println(this);
		
		//set size and basic things like that
		this.setForeground( Color.white );
	    this.setSize(715, 544);
	    this.setVisible( true );
	    this.add(corePanel);
	    
	    //add menu bar
	    createAndShowMenuBar();
	    
	    //set the various layouts
	    corePanel.setLayout(new GridBagLayout());
	    leftPanel.setLayout(new GridBagLayout());
	    rightPanel.setLayout(new FlowLayout());
	    overviewPanel.setLayout(mainOverviewLayout);
	    roomPanel.setLayout(mainRoomGridLayout);
	    roomInfoPanel.setLayout(new BorderLayout());
	    
	    //add borders
	    overviewPanel.setBorder(roomBorder);
	    roomPanel.setBorder(roomBorder);
	    roomInfoPanel.setBorder(roomBorder);
	    rightPanel.setBorder(roomBorder);
	    
	    //set backgrounds
        rightPanel.setBackground( bgblue );
        corePanel.setBackground( bgblue );
        leftPanel.setBackground( bgblue );
        roomInfoPanel.setBackground( bgcream );

        //create the left and right columns
	    GridBagConstraints coreConstraints = new GridBagConstraints();
	    coreConstraints.fill = GridBagConstraints.VERTICAL;
	    coreConstraints.anchor = GridBagConstraints.EAST;
	    coreConstraints.weighty = 1.0;
	    corePanel.add(leftPanel, coreConstraints);
	    coreConstraints.anchor = GridBagConstraints.WEST;
	    coreConstraints.weightx = 1.0;
	    coreConstraints.fill = GridBagConstraints.BOTH;
	    corePanel.add(rightPanel, coreConstraints);
	    
	    //add panel for the grid overview
	    GridBagConstraints layoutConstraints = new GridBagConstraints();
	    layoutConstraints.fill = GridBagConstraints.BOTH;
	    layoutConstraints.anchor = GridBagConstraints.NORTH;
	    layoutConstraints.weightx = 1.0;
	    layoutConstraints.weighty = 1.0;
	    leftPanel.add(overviewPanel, layoutConstraints);
	    
	    //add panels for the room view
	    GridBagConstraints leftConstraints = new GridBagConstraints();
	    leftConstraints.gridy = 0;
	    leftConstraints.fill = GridBagConstraints.HORIZONTAL;
	    leftConstraints.anchor = GridBagConstraints.NORTH;
	    leftConstraints.weightx = 1.0;
	    leftPanel.add(roomPanel, leftConstraints);
	    
	    leftConstraints.weighty = 1.0;
	    leftConstraints.gridy = 1;
	    leftConstraints.fill = GridBagConstraints.BOTH;
	    leftConstraints.anchor = GridBagConstraints.SOUTH;
	    leftPanel.add(roomInfoPanel, leftConstraints);

	    
	}
	
	public void workOutRoomInfoContent() {
		
		roomInfoPanel.removeAll();
		

	    /*Graphics g = roomInfoPanel.getGraphics();
	    int[] xpoints = {20,29,37,58,33,169,125};
		int[] ypoints = {26,67,78,28,63,69,165};
		Polygon poly = new Polygon(xpoints,ypoints,xpoints.length);
		
		g.setColor(polycol);
		
		g.fillPolygon(poly);
		g.drawRect(20,30,40,50);
	    
	    System.out.println(this);
	    System.out.println(g);
	    System.out.println(poly);*/
	    
		
	}

	public void createAndShowMenuBar() {
		
		menuBar.add(mainMenu);
	    mainMenu.add(menuItem);
	    
	    setJMenuBar(menuBar);
	    
	}
	
	public void createAndShowRightPanelContent() {
		
		rightButton.addMouseListener(this);
	    rightPanel.add(rightButton);
		
	}

	public void createAndShowGrid(int houseLevel) {

		//clear grid
		overviewPanel.removeAll();
		//add rooms
		String hashKey = null;

		for(int i = 0;i<9;i++) {
	    	for(int j = 0;j<9;j++){
	            hashKey = "overviewRoom" + Integer.toString(houseLevel) + Integer.toString(i) + Integer.toString(j);
	            //System.out.println(hashKey);
	    		overviewPanel.add(roomHash.get( hashKey ).getRoomOverviewLabel());
	    	}
		}
		
		//repaint();
		
		//set the level of the house being viewed
		overviewViewingLevel = houseLevel;
		viewingGrid = true;
		
	    overviewPanel.setVisible(true);
	    roomPanel.setVisible(false);
	    roomInfoPanel.setVisible(false);
	    
	    System.out.println("grid");
	}
	
	
	public void createAndShowRoom(String roomKey) {

		roomPanel.removeAll();
		
	    roomPanel.add(roomHash.get(roomKey).getRoomImageLabel());
	    status = 1;

	    //calculate contents for the roomInfo panel
	    workOutRoomInfoContent();
	    
	    overviewPanel.setVisible(false);
	    roomPanel.setVisible(true);
	    roomInfoPanel.setVisible(true);

	    System.out.println("room");
	    viewingGrid = false;
		
	}
	
	public void goToRoomFromGrid(String roomKey){
		if(roomHash.get(roomKey).getIsSelected() == false){
			//set a previously selected room to unselected
			if(overviewSelectedRoom != ""){
				roomHash.get(overviewSelectedRoom).setIsSelected(false);
			}
			//set the clicked room as selected
			roomHash.get(roomKey).setIsSelected(true);
			//specify the new selected room
			overviewSelectedRoom = roomKey;
			System.out.println(roomKey + " is selected " + roomHash.get(roomKey).getIsSelected());
		} else if(roomHash.get(roomKey).getIsSelected() == true) {
			System.out.println(roomKey + " is not selected " + roomHash.get(roomKey).getIsSelected());
			//unselect room
			roomHash.get(roomKey).setIsSelected(false);
			overviewSelectedRoom = "";
			System.out.println(roomKey + " is not selected " + roomHash.get(roomKey).getIsSelected());
			
			//show room
			if(roomHash.get(roomKey).getRoomType() != ""){
				createAndShowRoom(roomKey);
				lastViewedRoom = roomKey;
			}			
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
		if (e.getSource() == rightButton) {
			System.out.println("right");
			//System.out.println(viewingGrid);
			if(viewingGrid)createAndShowRoom(lastViewedRoom);
			else if(!viewingGrid) createAndShowGrid(1);
			//System.out.println(viewingGrid);
		} else if (viewingGrid && e.getSource().toString().contains("57x57")){
			String[] splitArray = null;
			int x;
			int y;
			
			//work out which room has been clicked
			for(int i = 0; i < 3; i++){
				splitArray = e.getSource().toString().split( "," );
			}
			x = (Integer.parseInt(splitArray[1])-4)/57;
			y = (Integer.parseInt(splitArray[2])-4)/57;

			String roomKey = "overviewRoom"+Integer.toString(overviewViewingLevel)+Integer.toString(y)+Integer.toString(x);
			
			System.out.println("Clicked on room: overviewRoom"+roomKey+" type: "+roomHash.get(roomKey).getRoomType());
			
			goToRoomFromGrid(roomKey);
		}
		else {
			
		}
		//System.out.println(e.getSource().toString());
		
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

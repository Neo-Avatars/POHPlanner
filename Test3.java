import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;


public class Test3 extends JApplet implements ActionListener, MouseListener {

	private static final long serialVersionUID = -4986474296742496209L;
	
	private JPanel corePanel = new JPanel();
	private JPanel overviewPanel = new JPanel();
	private JPanel roomPanel = new JPanel();
	private JPanel roomInfoPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel rightButtonPanel = new JPanel();
	private JPanel rightRoomInfoPanel = new JPanel();
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
	private String[] comboSelectGridLevelStrings = {"Dungeon", "Ground Floor", "Upper Floor" };
	private JComboBox comboSelectGridLevel = new JComboBox(comboSelectGridLevelStrings);
	
	GridLayout mainRoomGridLayout = new GridLayout(1, 1, 0, 0);
	GridLayout mainOverviewLayout = new GridLayout(9, 9, 0, 0);
	
	boolean viewingGrid;
	
	LinkedHashMap<Object, Room> roomHash = new LinkedHashMap<Object, Room>();
	
	int overviewViewingLevel = 1;
	String lastViewedRoom;
	String overviewSelectedRoom = "";
	
	Color polycol = new Color(89,63,255,128);
	
	String[] splitArray = null;

	private JButton buildButtonGarden = new JButton();
	private JButton buildButtonParlour = new JButton();
	private JButton buildButtonKitchen = new JButton();
	private JButton buildButtonDiningRoom = new JButton();
	private JButton buildButtonWorkshop = new JButton();
	private JButton buildButtonBedroom = new JButton();
	private JButton buildButtonSkillHall = new JButton();
	private JButton buildButtonGamesRoom = new JButton();
	private JButton buildButtonCombatRoom = new JButton();
	private JButton buildButtonQuestHall = new JButton();
	private JButton buildButtonStudy = new JButton();
	private JButton buildButtonCostumeRoom = new JButton();
	private JButton buildButtonChapel = new JButton();
	private JButton buildButtonPortalChamber = new JButton();
	private JButton buildButtonFormalGarden = new JButton();
	private JButton buildButtonThroneRoom = new JButton();
	private JButton buildButtonOubliette = new JButton();
	private JButton buildButtonDungeonCorridor = new JButton();
	private JButton buildButtonDungeonJunction = new JButton();
	private JButton buildButtonDungeonStairs = new JButton();
	private JButton buildButtonTreasureRoom = new JButton();

	private JLabel buildIconGarden = new JLabel("Cost: 1000gp  Requires Level: 1", new ImageIcon("buildIcons/garden.gif"), JLabel.CENTER);
	private JLabel buildIconParlour = new JLabel("Cost: 1000gp  Requires Level: 1", new ImageIcon("buildIcons/parlour.gif"), JLabel.CENTER);
	private JLabel buildIconKitchen = new JLabel("Cost: 5000gp  Requires Level: 5", new ImageIcon("buildIcons/kitchen.gif"), JLabel.CENTER);
	private JLabel buildIconDiningRoom = new JLabel("Cost: 5000gp  Requires Level: 10", new ImageIcon("buildIcons/diningroom.gif"), JLabel.CENTER);
	private JLabel buildIconWorkshop = new JLabel("Cost: 10000gp  Requires Level: 15", new ImageIcon("buildIcons/workshop.gif"), JLabel.CENTER);
	private JLabel buildIconBedroom = new JLabel("Cost: 10000gp  Requires Level: 20", new ImageIcon("buildIcons/bedroom.gif"), JLabel.CENTER);
	private JLabel buildIconSkillHall = new JLabel("Cost: 15000gp  Requires Level: 25", new ImageIcon("buildIcons/skillhall.gif"), JLabel.CENTER);
	private JLabel buildIconGamesRoom = new JLabel("Cost: 25000gp  Requires Level: 30", new ImageIcon("buildIcons/gamesroom.gif"), JLabel.CENTER);
	private JLabel buildIconCombatRoom = new JLabel("Cost: 25000gp  Requires Level: 32", new ImageIcon("buildIcons/combatroom.gif"), JLabel.CENTER);
	private JLabel buildIconQuestHall = new JLabel("Cost: 25000gp  Requires Level: 35", new ImageIcon("buildIcons/questhall.gif"), JLabel.CENTER);
	private JLabel buildIconStudy = new JLabel("Cost: 50000gp  Requires Level: 40", new ImageIcon("buildIcons/study.gif"), JLabel.CENTER);
	private JLabel buildIconCostumeRoom = new JLabel("Cost: 50000gp  Requires Level: 42", new ImageIcon("buildIcons/costumeroom.gif"), JLabel.CENTER);
	private JLabel buildIconChapel = new JLabel("Cost: 50000gp  Requires Level: 45", new ImageIcon("buildIcons/chapel.gif"), JLabel.CENTER);
	private JLabel buildIconPortalChamber = new JLabel("Cost: 100000gp  Requires Level: 50", new ImageIcon("buildIcons/portalchamber.gif"), JLabel.CENTER);
	private JLabel buildIconFormalGarden = new JLabel("Cost: 75000gp  Requires Level: 55", new ImageIcon("buildIcons/formalgarden.gif"), JLabel.CENTER);
	private JLabel buildIconThroneRoom = new JLabel("Cost: 150000gp  Requires Level: 60", new ImageIcon("buildIcons/throneroom.gif"), JLabel.CENTER);
	private JLabel buildIconOubliette = new JLabel("Cost: 150000gp  Requires Level: 65", new ImageIcon("buildIcons/oubliette.gif"), JLabel.CENTER);
	private JLabel buildIconDungeonCorridor = new JLabel("Cost: 7500gp  Requires Level: 70", new ImageIcon("buildIcons/dungeon.gif"), JLabel.CENTER);
	private JLabel buildIconDungeonJunction = new JLabel("Cost: 7500gp  Requires Level: 70", new ImageIcon("buildIcons/dungeon.gif"), JLabel.CENTER);
	private JLabel buildIconDungeonStairs = new JLabel("Cost: 7500gp  Requires Level: 70", new ImageIcon("buildIcons/dungeon.gif"), JLabel.CENTER);
	private JLabel buildIconTreasureRoom = new JLabel("Cost: 250000gp  Requires Level: 75", new ImageIcon("buildIcons/treasureroom.gif"), JLabel.CENTER);

	private JFrame roomBuildingFrame = new JFrame();
	private JPanel roomBuildingScrollPanel = new JPanel();
	private JScrollPane roomBuildingScrollPane = new JScrollPane(roomBuildingScrollPanel);
	
	JTabbedPane roomInfoTabbedPane = new JTabbedPane();
	private JPanel roomInfoOverviewPanel = new JPanel();
	private JPanel roomInfoBuildFurniScrollPanel = new JPanel();
	private JScrollPane roomInfoBuildFurniScrollPane = new JScrollPane(roomInfoBuildFurniScrollPanel);
	
	public void init() {
		initRoomBuildingFrame();
		initRoomArray();
		createAndShowGUI();
		initRoomInfoPanel();
		createAndShowRightPanelContent();
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
	    rightPanel.setLayout(new GridLayout(2, 1, 0, 0));
	    rightButtonPanel.setLayout(new FlowLayout());
	    rightRoomInfoPanel.setLayout(new GridLayout(10, 1, 0, 0));
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
        rightRoomInfoPanel.setBackground( bgblue );

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
	
	public void initRoomInfoPanel(){
		
		roomInfoPanel.add(roomInfoTabbedPane);
		
		roomInfoTabbedPane.addTab("Room Overview", null, roomInfoOverviewPanel,
				"Gives an Overview of the room, with various stats about it.");
		roomInfoTabbedPane.addTab("Build Furniture", null, roomInfoBuildFurniScrollPane,
				"Lets you build furniture within the room.");

		/*roomInfoBuildFurniScrollPanel.setLayout(new GridLayout(7, 2, 0, 0));
		
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));
		roomInfoBuildFurniScrollPanel.add(new JButton("g fgh fdmgdf"));*/

	}
	
	public void workOutRoomInfoContent(String roomKey) {
		
		//roomInfoPanel.removeAll();
		roomInfoBuildFurniScrollPanel.removeAll();
		roomInfoBuildFurniScrollPanel.setLayout(new GridLayout(roomHash.get(roomKey).getFurniSpots(), 1, 0, 0));

		//add hotspot buttons
		for(int i = 0; i < roomHash.get(roomKey).getHotspots().length; i++){
			Object[] test = roomHash.get(roomKey).getHotspots();
			System.out.println(test[i].hashCode());
			//roomInfoBuildFurniScrollPanel.add(test[i].getHotspotButton());
		}
		
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
	
	public void initRoomBuildingFrame(){
		
		roomBuildingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		roomBuildingFrame.setBounds(50, 80, 600, 400);
		roomBuildingFrame.setIconImage(new ImageIcon("hammer.gif").getImage());
		roomBuildingFrame.setTitle("Build a new Room");

		roomBuildingFrame.add(roomBuildingScrollPane);
		roomBuildingScrollPanel.setLayout(new GridLayout(21, 2, 0, 0));
		
		buildButtonGarden.setText("Build a Garden");
		buildButtonParlour.setText("Build a Parlour");
		buildButtonKitchen.setText("Build a Kitchen");
		buildButtonDiningRoom.setText("Build a Dining Room");
		buildButtonWorkshop.setText("Build a Workshop");
		buildButtonBedroom.setText("Build a Bedroom");
		buildButtonSkillHall.setText("Build a Skill Hall");
		buildButtonGamesRoom.setText("Build a Games Room");
		buildButtonCombatRoom.setText("Build a Combat Room");
		buildButtonQuestHall.setText("Build a Quest Hall");
		buildButtonStudy.setText("Build a Study");
		buildButtonCostumeRoom.setText("Build a Costume Room");
		buildButtonChapel.setText("Build a Chapel");
		buildButtonPortalChamber.setText("Build a Portal Chamber");
		buildButtonFormalGarden.setText("Build a Formal Garden");
		buildButtonThroneRoom.setText("Build a Throne Room");
		buildButtonOubliette.setText("Build a Oubliette");
		buildButtonDungeonCorridor.setText("Build a Dungeon Corridor");
		buildButtonDungeonJunction.setText("Build a Dungeon Junction");
		buildButtonDungeonStairs.setText("Build a Dungeon Stairs");
		buildButtonTreasureRoom.setText("Build a Treasure Room");

		buildButtonGarden.addMouseListener(this);
		buildButtonParlour.addMouseListener(this);
		buildButtonKitchen.addMouseListener(this);
		buildButtonDiningRoom.addMouseListener(this);
		buildButtonWorkshop.addMouseListener(this);
		buildButtonBedroom.addMouseListener(this);
		buildButtonSkillHall.addMouseListener(this);
		buildButtonGamesRoom.addMouseListener(this);
		buildButtonCombatRoom.addMouseListener(this);
		buildButtonQuestHall.addMouseListener(this);
		buildButtonStudy.addMouseListener(this);
		buildButtonCostumeRoom.addMouseListener(this);
		buildButtonChapel.addMouseListener(this);
		buildButtonPortalChamber.addMouseListener(this);
		buildButtonFormalGarden.addMouseListener(this);
		buildButtonThroneRoom.addMouseListener(this);
		buildButtonOubliette.addMouseListener(this);
		buildButtonDungeonCorridor.addMouseListener(this);
		buildButtonDungeonJunction.addMouseListener(this);
		buildButtonDungeonStairs.addMouseListener(this);
		buildButtonTreasureRoom.addMouseListener(this);

		int buildIconSpace = 20;

		buildIconGarden.setIconTextGap(buildIconSpace);
		buildIconParlour.setIconTextGap(buildIconSpace);
		buildIconKitchen.setIconTextGap(buildIconSpace);
		buildIconDiningRoom.setIconTextGap(buildIconSpace);
		buildIconWorkshop.setIconTextGap(buildIconSpace);
		buildIconBedroom.setIconTextGap(buildIconSpace);
		buildIconSkillHall.setIconTextGap(buildIconSpace);
		buildIconGamesRoom.setIconTextGap(buildIconSpace);
		buildIconCombatRoom.setIconTextGap(buildIconSpace);
		buildIconQuestHall.setIconTextGap(buildIconSpace);
		buildIconStudy.setIconTextGap(buildIconSpace);
		buildIconCostumeRoom.setIconTextGap(buildIconSpace);
		buildIconChapel.setIconTextGap(buildIconSpace);
		buildIconPortalChamber.setIconTextGap(buildIconSpace);
		buildIconFormalGarden.setIconTextGap(buildIconSpace);
		buildIconThroneRoom.setIconTextGap(buildIconSpace);
		buildIconOubliette.setIconTextGap(buildIconSpace);
		buildIconDungeonCorridor.setIconTextGap(buildIconSpace);
		buildIconDungeonJunction.setIconTextGap(buildIconSpace);
		buildIconDungeonStairs.setIconTextGap(buildIconSpace);
		buildIconTreasureRoom.setIconTextGap(buildIconSpace);

		roomBuildingScrollPanel.add(buildIconGarden);
		roomBuildingScrollPanel.add(buildButtonGarden);
		roomBuildingScrollPanel.add(buildIconParlour);
		roomBuildingScrollPanel.add(buildButtonParlour);
		roomBuildingScrollPanel.add(buildIconKitchen);
		roomBuildingScrollPanel.add(buildButtonKitchen);
		roomBuildingScrollPanel.add(buildIconDiningRoom);
		roomBuildingScrollPanel.add(buildButtonDiningRoom);
		roomBuildingScrollPanel.add(buildIconWorkshop);
		roomBuildingScrollPanel.add(buildButtonWorkshop);
		roomBuildingScrollPanel.add(buildIconBedroom);
		roomBuildingScrollPanel.add(buildButtonBedroom);
		roomBuildingScrollPanel.add(buildIconSkillHall);
		roomBuildingScrollPanel.add(buildButtonSkillHall);
		roomBuildingScrollPanel.add(buildIconGamesRoom);
		roomBuildingScrollPanel.add(buildButtonGamesRoom);
		roomBuildingScrollPanel.add(buildIconCombatRoom);
		roomBuildingScrollPanel.add(buildButtonCombatRoom);
		roomBuildingScrollPanel.add(buildIconQuestHall);
		roomBuildingScrollPanel.add(buildButtonQuestHall);
		roomBuildingScrollPanel.add(buildIconStudy);
		roomBuildingScrollPanel.add(buildButtonStudy);
		roomBuildingScrollPanel.add(buildIconCostumeRoom);
		roomBuildingScrollPanel.add(buildButtonCostumeRoom);
		roomBuildingScrollPanel.add(buildIconChapel);
		roomBuildingScrollPanel.add(buildButtonChapel);
		roomBuildingScrollPanel.add(buildIconPortalChamber);
		roomBuildingScrollPanel.add(buildButtonPortalChamber);
		roomBuildingScrollPanel.add(buildIconFormalGarden);
		roomBuildingScrollPanel.add(buildButtonFormalGarden);
		roomBuildingScrollPanel.add(buildIconThroneRoom);
		roomBuildingScrollPanel.add(buildButtonThroneRoom);
		roomBuildingScrollPanel.add(buildIconOubliette);
		roomBuildingScrollPanel.add(buildButtonOubliette);
		roomBuildingScrollPanel.add(buildIconDungeonCorridor);
		roomBuildingScrollPanel.add(buildButtonDungeonCorridor);
		roomBuildingScrollPanel.add(buildIconDungeonJunction);
		roomBuildingScrollPanel.add(buildButtonDungeonJunction);
		roomBuildingScrollPanel.add(buildIconDungeonStairs);
		roomBuildingScrollPanel.add(buildButtonDungeonStairs);
		roomBuildingScrollPanel.add(buildIconTreasureRoom);
		roomBuildingScrollPanel.add(buildButtonTreasureRoom);
		
		//roomBuildingFrame.setVisible(true);
		
	}
	
	public void createAndShowRightPanelContent() {
		
		rightButton.addMouseListener(this);
	    rightPanel.add(rightButtonPanel);

	    comboSelectGridLevel.setSelectedIndex(1);
	    comboSelectGridLevel.addActionListener(this);
	    
	    rightButtonPanel.add(rightButton);
	    rightButtonPanel.add(comboSelectGridLevel);
	    rightPanel.add(rightRoomInfoPanel);
		
	}

	public void createAndShowGrid(int houseLevel) {

		//clear grid
		overviewPanel.removeAll();
		//add rooms
		String hashKey = null;

		for(int i = 0;i<9;i++) {
	    	for(int j = 0;j<9;j++){
	            hashKey = "overviewRoom" + Integer.toString(houseLevel) + Integer.toString(i) + Integer.toString(j);
	    		overviewPanel.add(roomHash.get( hashKey ).getRoomOverviewLabel());
	    	}
		}

		//set the level of the house being viewed
		overviewViewingLevel = houseLevel;
		viewingGrid = true;
		
	    overviewPanel.setVisible(true);
	    roomPanel.setVisible(false);
	    roomInfoPanel.setVisible(false);

	    repaint();
	    
	    System.out.println("grid");
	}
	
	public void createAndShowRoom(String roomKey) {

		roomPanel.removeAll();
		
	    roomPanel.add(roomHash.get(roomKey).getRoomImageLabel());

	    //calculate contents for the roomInfo panel
	    workOutRoomInfoContent(roomKey);
	    
	    overviewPanel.setVisible(false);
	    roomPanel.setVisible(true);
	    roomInfoPanel.setVisible(true);

	    System.out.println("room");
	    viewingGrid = false;
		
	}
	
	public void setRightRoomInfoPanelContent(){
		
		rightRoomInfoPanel.removeAll();
		
		JLabel levelLabel = new JLabel("Required level: " + Integer.toString(roomHash.get(overviewSelectedRoom).getRoomLevel()));
		JLabel costLabel = new JLabel(roomHash.get(overviewSelectedRoom).getRoomType() + " Cost: " + Integer.toString(roomHash.get(overviewSelectedRoom).getRoomCost()) + "gp");
		
		rightRoomInfoPanel.add(levelLabel);
		rightRoomInfoPanel.add(costLabel);
		
		repaint();
		System.out.println("in");
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

			//check whether the selected room is empty or not
			if(roomHash.get(roomKey).getRoomType() == ""){
				//clear any room info
				rightRoomInfoPanel.removeAll();
				System.out.println("clear");
				//show building pane
				roomBuildingFrame.setVisible(true);
			} else {
				//show room info
				System.out.println("hi");
				setRightRoomInfoPanelContent();
				System.out.println("out");
				//show delete room button
			}
			
		} 
		
		else if(roomHash.get(roomKey).getIsSelected() == true) {
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
		if(e.getSource().equals(comboSelectGridLevel)){
			
			String level = (String) comboSelectGridLevel.getSelectedItem();
			int houseLevel;
			houseLevel = level == comboSelectGridLevelStrings[0] ? 0 : level == comboSelectGridLevelStrings[1] ? 1 : 2;
			
			System.out.println(comboSelectGridLevel.getSelectedItem() + " " + houseLevel);
			
			createAndShowGrid(houseLevel);
		}
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
			splitArray = null;
			int x;
			int y;
			
			//work out which room has been clicked
			splitArray = e.getSource().toString().split( "," );

			x = (Integer.parseInt(splitArray[1])-4)/57;
			y = (Integer.parseInt(splitArray[2])-4)/57;

			String roomKey = "overviewRoom"+Integer.toString(overviewViewingLevel)+Integer.toString(y)+Integer.toString(x);
			
			System.out.println("Clicked on room: overviewRoom"+roomKey+" type: "+roomHash.get(roomKey).getRoomType());
			
			goToRoomFromGrid(roomKey);
		} else if(e.getSource().toString().contains("Build a")) {
			
			//hide building pane
			roomBuildingFrame.setVisible(false);
			
			String calcRoomType;
			String roomType = "";
			splitArray = null;
			
			//work out which room build button has been clicked
			splitArray = e.getSource().toString().split( "," );
			calcRoomType = splitArray[splitArray.length - 2];
			
			//would probably work better with a regex, but this works, so heh
			for(int i = 13; i < calcRoomType.length(); i++){
				roomType += calcRoomType.charAt(i);
			}

			System.out.println(overviewViewingLevel + "     build   " + overviewSelectedRoom + "     (" + roomType + ")");
			//build the selected room
			System.out.println(roomType);
			roomHash.put( overviewSelectedRoom , new Room(roomType, overviewViewingLevel));
			roomHash.get( overviewSelectedRoom ).getRoomOverviewLabel().addMouseListener(this);
			roomHash.get( overviewSelectedRoom ).printValues();
			//createAndShowGrid(overviewViewingLevel);
			createAndShowRoom(overviewSelectedRoom);
			overviewSelectedRoom = "";
		}
		else {
			//System.out.println(e);
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

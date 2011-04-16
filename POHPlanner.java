package pohplanner;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import pohplanner.HiscoreData;
import pohplanner.TextFieldLimiter;

//test codes
//001,13403,1350200000000000000,1360204000000000000,1440201000000000000,14503,1460201000000000000,
//001,00419,01421,02222,02321,02422,02521,02622,03221,03421,03621,04016,04121,04222,04321,04420,04521,04622,04721,04816,05221,05421,05621,06222,06321,06422,06521,06622,07424,13405,1440401000000000000,
//001,10408,10509,11407,11510,12406,12511,12624,13405,13512,13623,1440401000000000000,14513,14622,15514,15621,16515,16620,17516,17619,18517,18618,
//001,12405fttt00,12513tttt,12608ftft,12704tttt00000000000000,13405fttt00,13515ftff,13605fttt00,13707tftt,14404tttt01000000000000,14508ftft,14604tttt00000000000000,14716ttff,15514fttt,15616ttff,15714fttt,
//001,11111fttt,11409ttff,11507tftt,11607tftt,12209ttff,12309ttff,12409ttff,12510tttt,12609ttff,13210tttt,13309ttff,13405fttt0000,13508ftft,13607tftt,14210tttt,14309ttff,14404tttt01000000000000,14509ttff,14609ttff,15310tttt,15410tttt,15510tttt,15610tttt,15710tttt
//001,11404tttt00000000000000,11504tttt00000000000000,11604tttt00000000000000,11704tttt00000000000000,12304tttt00000000000000,12404tttt00000000000000,12504tttt00000000000000,12604tttt00000000000000,12704tttt00000000000000,13304tttt00000000000000,13405fttt0000,13504tttt00000000000000,13604tttt00000000000000,13704tttt00000000000000,14304tttt00000000000000,14404tttt01000000000000,14504tttt00000000000000,14605fttt0000,14704tttt00000000000000,15304tttt00000000000000,15404tttt00000000000000,15504tttt00000000000000,15604tttt00000000000000,15704tttt00000000000000,
//001,13403,1350200000000000000,1360204000000000000,1440201000000000000,14503,1460201000000000000,
//001,13405fttf27312733364042,13506fttf46495058000000,14404tttt00000000000000,
//001,13403,1350200000000000000,1360204000000000000,1440201000000000000,14503,1460201000000000000,

//NOTE: DO NOT put apostrophes in names of anything or it won't work!

//TODO add more rules and confirmations when building eg. stairs
//TODO similar thing with the dungeon as with the upper floors, that you can't build unless you have a room connecting to it or there is a Dungeon entrance/Throne room above.
//TODO fix it when you're loading from a sharing code and click 'No' when the 'Too high a level' message appears since currently it leaves the space blank
//TODO add some sort of ToolTip to Polygons
//TODO add support for GE prices
//TODO bring flatpack prices into the price equation
//TODO optimise the sharing code to contain less 0s
//TODO get a CostumeRoom screenshot - Josh said he may do it
//TODO create a window showing all the materials that you need
//TODO choose the type of nails you want to use
//TODO make sure a room stays selected when you rotate it - EEK!
//TODO add indicators in the dungeon to show where things are above
//TODO better colour for when selecting a room in a dungeon
//TODO add a way to rotate the house
//TODO add an 'Edit Stats' option to the dimension warning message
//TODO fix vertical spacing for Parlour viewInfo - fixes when you have lots of Hotspots
//TODO work out what's wrong when room images stop showing

/*
Done:
The skill icons in the View Stats pane now show
Fixed the dimension limit problem when manually setting your stats
You can only build one Costume Room
Fixed the floor-related problem explained in http://runescape.salmoneus.net/forums/index.php?s=&showtopic=291756&view=findpost&p=3353045
Fixed http://runescape.salmoneus.net/forums/index.php?s=&showtopic=291756&view=findpost&p=3353464
 * */

/**
 * The main class file for the POH Planner.
 * 
 * @author Neo Avatars
 */
public class POHPlanner extends JApplet implements ActionListener, MouseListener, ItemListener {

	private static final long serialVersionUID = -4986474296742496209L;
	private static Utilities util = new Utilities();
	
	private JLabel powerImageLabel = new JLabel(util.loadImage("poweredby.gif"));
	
	//loading things
	private JPanel loadingPanel = new JPanel();
	private JLabel loadingLabel = new JLabel();
	private JPanel almightyPanel = new JPanel();
	
	//main panels
	private JPanel corePanel = new JPanel();
	private JPanel overviewPanel = new JPanel();
	//private JPanel roomPanel = new JPanel();
	private RoomPanel roomPanel = new RoomPanel( "", null );
	private JPanel roomInfoPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	
	//right panel
	private JPanel rightPanel = new JPanel();
	private JPanel rightNavPanel = new JPanel();
	private JPanel rightHouseOverviewPanel = new JPanel();
	private JPanel rightGeneralToolsPanel = new JPanel();
	private JPanel rightRoomInfoPanel = new JPanel();
	private JPanel rightPoweredByPanel = new JPanel();
	
	//menu bar
	private JMenuBar menuBar = new JMenuBar();
	private JTextField menuHouseCodeTextbox = new JTextField(10);
	private JLabel menuHouseCodeLabel = new JLabel();
	private JButton menuHouseCodeButton = new JButton();
	//private JMenu mainMenu = new JMenu( "Menu" );
	//private JMenuItem menuItem = new JMenu( "Item" );
	private JMenu transformMenu = new JMenu("Transform");
	private JMenuItem moveHouseNorthMenuItem = new JMenuItem("Move House North");
	private JMenuItem moveHouseSouthMenuItem = new JMenuItem("Move House South");
	private JMenuItem moveHouseEastMenuItem = new JMenuItem("Move House East");
	private JMenuItem moveHouseWestMenuItem = new JMenuItem("Move House West");
	private JMenuItem flipHouseHorizontallyMenuItem = new JMenuItem("Flip House Horizontally");
	private JMenuItem flipHouseVerticallyMenuItem = new JMenuItem("Flip House Vertically");
	private JMenuItem rotateHouseClockwiseMenuItem = new JMenuItem("Rotate House Clockwise");
	private JMenuItem rotateHouseAntiClockwiseMenuItem = new JMenuItem("Rotate House Anti-Clockwise");
	
	//colours
	//private Color bgblue = new Color(83, 115, 191);
	private Color bgcream = new Color(217, 198, 163);
	//private Color bgtransparent = new Color(0, 0, 0, 0);
	private Color txtred = new Color(127, 2, 1);
	private Color txtblue = new Color(1, 0, 254);
	//private Color polycol = new Color(89,63,255,128);
	
	//Fonts
	private Font titleFont = new Font("Helvetica", Font.BOLD, 14);
	private Font titleFont16 = new Font("Helvetica", Font.BOLD, 16);
	
	//Insets
	private Insets topTenInsets = new Insets(10, 0, 0, 0);
	private Insets zeroedInsets = new Insets(0, 0, 0, 0);
	private Insets threesInsets = new Insets(3, 3, 3, 3);
	private Insets topFiveInsets = new Insets(5, 0, 0, 0);
	private Insets leftSpaceInsets = new Insets(0, 30, 0, 0);
	private Insets bottomFiveInsets = new Insets(0, 0, 5, 0);
	private Insets bottomFiveTopThreeInsets = new Insets(3, 0, 5, 0);
	
	//key layouts that are here because they can be
	GridLayout mainRoomGridLayout = new GridLayout(1, 1, 0, 0);
	GridLayout mainOverviewLayout = new GridLayout(9, 9, 0, 0);
	
	//borders
	Border blueBorder = BorderFactory.createLineBorder(new Color(100, 136, 201));
    Border titledBorder = BorderFactory.createTitledBorder( blueBorder,
            "Special Info", TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, null, Color.white );
    Border roomBorder1 = BorderFactory.createBevelBorder(0, new Color(27, 24, 17), new Color(42, 38, 28), new Color(94, 83, 58), new Color(101, 91, 57));
    Border roomBorder2 = BorderFactory.createBevelBorder(0, new Color(42, 36, 21), new Color(42, 38, 28), new Color(101, 91, 67), new Color(106, 98, 75));
    Border roomBorder = BorderFactory.createCompoundBorder(roomBorder2, roomBorder1);

	//data
    //private LinkedHashMap<Object, Room> houseLoadHash = new LinkedHashMap<Object, Room>();
    private LinkedHashMap<Object, Room> houseHash = new LinkedHashMap<Object, Room>();
    private LinkedHashMap<Object, Room> houseLoadHash = new LinkedHashMap<Object, Room>();
	private ArrayList<HiscoreData> hiscoreList = new ArrayList<HiscoreData>();
	private LinkedHashMap<Object, FurniBuildPanel> furniBuildHash = new LinkedHashMap<Object, FurniBuildPanel>();
	private LinkedHashMap<Object, FurniBuildButton> furniButtonHash = new LinkedHashMap<Object, FurniBuildButton>();
	private LinkedHashMap<Object, FurniStatusLabel> furniStatusHash = new LinkedHashMap<Object, FurniStatusLabel>();
	private LinkedHashMap<Object, StatsPane> statsPanelHash = new LinkedHashMap<Object, StatsPane>();
	private LinkedHashMap<Object, Quest> questHash = new LinkedHashMap<Object, Quest>();
	
	//data to do with rooms, furniture, etc
	private LinkedHashMap<Object, String> roomIDHash = new LinkedHashMap<Object, String>();
	private LinkedHashMap<Object, Room> roomHash = new LinkedHashMap<Object, Room>();
	private LinkedHashMap<Object, Hotspot> hotspotHash = new LinkedHashMap<Object, Hotspot>();
	private LinkedHashMap<Object, String> furniIDHash = new LinkedHashMap<Object, String>();
	private LinkedHashMap<Object, Furniture> furniHash = new LinkedHashMap<Object, Furniture>();
	private LinkedHashMap<Object, String> materialIDHash = new LinkedHashMap<Object, String>();
	private LinkedHashMap<Object, Materials> materialHash = new LinkedHashMap<Object, Materials>();
	
	//lets you split strings
	String[] splitArray = null;

	//room building frame
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

	private JLabel buildIconGarden = new JLabel("Cost: 1000gp  Requires Level: 1", util.loadImage( "buildIcons/garden.gif" ), JLabel.CENTER);
	private JLabel buildIconParlour = new JLabel("Cost: 1000gp  Requires Level: 1", util.loadImage("buildIcons/parlour.gif"), JLabel.CENTER);
	private JLabel buildIconKitchen = new JLabel("Cost: 5000gp  Requires Level: 5", util.loadImage("buildIcons/kitchen.gif"), JLabel.CENTER);
	private JLabel buildIconDiningRoom = new JLabel("Cost: 5000gp  Requires Level: 10", util.loadImage("buildIcons/diningroom.gif"), JLabel.CENTER);
	private JLabel buildIconWorkshop = new JLabel("Cost: 10000gp  Requires Level: 15", util.loadImage("buildIcons/workshop.gif"), JLabel.CENTER);
	private JLabel buildIconBedroom = new JLabel("Cost: 10000gp  Requires Level: 20", util.loadImage("buildIcons/bedroom.gif"), JLabel.CENTER);
	private JLabel buildIconSkillHall = new JLabel("Cost: 15000gp  Requires Level: 25", util.loadImage("buildIcons/skillhall.gif"), JLabel.CENTER);
	private JLabel buildIconGamesRoom = new JLabel("Cost: 25000gp  Requires Level: 30", util.loadImage("buildIcons/gamesroom.gif"), JLabel.CENTER);
	private JLabel buildIconCombatRoom = new JLabel("Cost: 25000gp  Requires Level: 32", util.loadImage("buildIcons/combatroom.gif"), JLabel.CENTER);
	private JLabel buildIconQuestHall = new JLabel("Cost: 25000gp  Requires Level: 35", util.loadImage("buildIcons/questhall.gif"), JLabel.CENTER);
	private JLabel buildIconStudy = new JLabel("Cost: 50000gp  Requires Level: 40", util.loadImage("buildIcons/study.gif"), JLabel.CENTER);
	private JLabel buildIconCostumeRoom = new JLabel("Cost: 50000gp  Requires Level: 42", util.loadImage("buildIcons/costumeroom.gif"), JLabel.CENTER);
	private JLabel buildIconChapel = new JLabel("Cost: 50000gp  Requires Level: 45", util.loadImage("buildIcons/chapel.gif"), JLabel.CENTER);
	private JLabel buildIconPortalChamber = new JLabel("Cost: 100000gp  Requires Level: 50", util.loadImage("buildIcons/portalchamber.gif"), JLabel.CENTER);
	private JLabel buildIconFormalGarden = new JLabel("Cost: 75000gp  Requires Level: 55", util.loadImage("buildIcons/formalgarden.gif"), JLabel.CENTER);
	private JLabel buildIconThroneRoom = new JLabel("Cost: 150000gp  Requires Level: 60", util.loadImage("buildIcons/throneroom.gif"), JLabel.CENTER);
	private JLabel buildIconOubliette = new JLabel("Cost: 150000gp  Requires Level: 65", util.loadImage("buildIcons/oubliette.gif"), JLabel.CENTER);
	private JLabel buildIconDungeonCorridor = new JLabel("Cost: 7500gp  Requires Level: 70", util.loadImage("buildIcons/dungeon.gif"), JLabel.CENTER);
	private JLabel buildIconDungeonJunction = new JLabel("Cost: 7500gp  Requires Level: 70", util.loadImage("buildIcons/dungeon.gif"), JLabel.CENTER);
	private JLabel buildIconDungeonStairs = new JLabel("Cost: 7500gp  Requires Level: 70", util.loadImage("buildIcons/dungeon.gif"), JLabel.CENTER);
	private JLabel buildIconTreasureRoom = new JLabel("Cost: 250000gp  Requires Level: 75", util.loadImage("buildIcons/treasureroom.gif"), JLabel.CENTER);

	private JFrame roomBuildingFrame = new JFrame();
	private JPanel roomBuildingScrollPanel = new JPanel();
	private JScrollPane roomBuildingScrollPane = new JScrollPane(roomBuildingScrollPanel);
	
	//furniture building frame
	private JFrame furniBuildingFrame = new JFrame();
	private JPanel furniBuildingScrollPanel = new JPanel();
	private JScrollPane furniBuildingScrollPane = new JScrollPane(furniBuildingScrollPanel);
	private JLabel furniRemoveFromHotspotLabel = new JLabel();
	//private JLabel furniTooHighLevelImgLabel = new JLabel(new ImageIcon("furniIcons/unavailable.gif"));
		
    //rightPanel objects
	//combobox to select viewing level
	private String[] comboSelectGridLevelStrings = {"Dungeon", "Ground Floor", "Upper Floor" };
	private JComboBox comboSelectGridLevel = new JComboBox(comboSelectGridLevelStrings);
	private JLabel comboSelectGridLevelLabel = new JLabel("View Floor: ");
	private JButton rightViewGridButton = new JButton("House Overview");
	
	//rightRoomInfoPanel things
	private JLabel rightRoomInfoRoomTypeLabel = new JLabel("", JLabel.CENTER);
	private JLabel rightRoomInfoLevelLabel = new JLabel();
	private JLabel rightRoomInfoRoomCostLabel = new JLabel();
	private JLabel rightRoomInfoFurniCostLabel = new JLabel();
	private JLabel rightRoomInfoFurniXPLabel = new JLabel();
	private JButton rightRoomInfoRotateClockwise = new JButton("Rotate Clockwise");
	private JButton rightRoomInfoRotateAntiClockwise = new JButton("Rotate Anti-Clockwise");
	private JButton rightRoomInfoDeleteRoom = new JButton("Demolish Room");
	private boolean initialisingRoomAray = false;
	
	//rightGeneralToolsPanel things
	private JButton rightGeneralToolsUpdateGEPrices = new JButton("Update Item Prices");
	private JLabel rightGeneralToolsUsernameLabel = new JLabel("Username: ");
	private JTextField rightGeneralToolsUsernameTextbox = new JTextField(9);
	private JButton rightGeneralToolsUpdateUsername = new JButton("Fetch My Exp!");
	private JButton rightGeneralToolsViewStats = new JButton("Stats");
	
	//rightHouseOverviewPanel things
	private JLabel rightOverviewTitleLabel = new JLabel("House Stats", JLabel.CENTER);
	private JLabel rightOverviewRoomNumberLabel = new JLabel();
	private JLabel rightOverviewRoomCostLabel = new JLabel();
	private JLabel rightOverviewFurniNumberLabel = new JLabel();
	private JLabel rightOverviewFurniCostLabel = new JLabel();
	private JLabel rightOverviewTotalCostLabel = new JLabel();
	
	//various stats to do with the status of the house
	/**
	 * Numbers to be used in the rightOverviewLabels
	 * 0 = number of rooms.
	 * 1 = cost of rooms.
	 * 2 = cost of furniture.
	 * 3 = experience gained by building furniture.
	 */
	private int[] rightOverviewLabelNumbers = new int[4];
	private String furniBuildingFrameHotspotType = "";
	//private boolean clickedOnHotspot = false;
	private int selectedHotspotID = -1;
	private int currentRoomFurniNumber = 0;
	private int overviewViewingLevel = 1;
	/**
	 * Lets you switch back to the last viewed room.
	 * @type String
	 */
	private String lastViewedRoom;
	/**
	 * The room currently being viewed.
	 */
	private String viewingRoom = "";
	/**
	 * Currently selected room. The hashKey to be used with houseHash.
	 */
	private String overviewSelectedRoom = "";
	/**
	 * If True: ViewingGrid. If False: ViewingRoom.
	 */
	private boolean viewingGrid;
	private boolean ignoreLevelWarnings = false;
	private boolean viewingFurniBuildFrame = false;
	private boolean viewingRoomBuildFrame = false;
	private boolean rotatingRoom = false;
	private boolean loadingHouse = false;
	private boolean initialising = false;
	private boolean costumeRoomBuilt = false;
	private boolean stairsBuilt = false;
	
	//room limits
	/**
	 * Indicates the room that's furthest in each direction to
	 * determine the dimensions of the house.
	 * 0 = North.
	 * 1 = East.
	 * 2 = South.
	 * 3 = West.
	 */
	private int[] mostDistantRoom = new int[]{10, 10, 10, 10};
	private boolean loadAllRooms = false;
	private int roomDimensionLimit = 0;
	
	//tabbed room info pane
	private JTabbedPane roomInfoTabbedPane = new JTabbedPane();
	private JPanel roomInfoOverviewPanel = new JPanel();
	private JPanel roomInfoBuildFurniScrollPanel = new JPanel();
	private JScrollPane roomInfoBuildFurniScrollPane = new JScrollPane(roomInfoBuildFurniScrollPanel);
	private JScrollPane roomInfoOverviewScrollPane = new JScrollPane(roomInfoOverviewPanel);
	
	//infoOverviewPanel things
	private JLabel roomInfoRoomTypeLabel = new JLabel("", JLabel.CENTER);
	private JLabel roomInfoLevelLabel = new JLabel();
	private JLabel roomInfoRoomCostLabel = new JLabel();
	private JLabel roomInfoFurniCostLabel = new JLabel();
	private JLabel roomInfoFurniXPLabel = new JLabel();
	private JLabel roomInfoFurniNumberLabel = new JLabel();
	private JLabel roomInfoHotspotTitleLabel = new JLabel("Hotspot Contents", JLabel.CENTER);
	
	//stats frame
	private JFrame viewStatsFrame = new JFrame();
	private JPanel viewStatsPanel = new JPanel();
	private JScrollPane viewStatsScrollPane = new JScrollPane(viewStatsPanel);
	private JLabel viewStatsTitleLabel = new JLabel("Stats", JLabel.CENTER);
	private JButton viewStatsSaveAndCloseButton = new JButton("Save and close Window");
	private JButton viewStatsSaveButton = new JButton("Save Stats");
	private JLabel viewStatsQuestTitleLabel = new JLabel("Important Quests", JLabel.CENTER);
	private JLabel viewStatsQuestHelpLabel = new JLabel("Select the Quests that you have completed.");
	private JButton viewStatsSelectAllQuestsButton = new JButton("Select all Quests");
	private JButton viewStatsDeselectAllQuestsButton = new JButton("Deselect all Quests");
	
	//error messages
	private JFrame rightDeleteRoomConfirmFrame = new JFrame();
	private JFrame ignoreLevelWarningsConfirmFrame = new JFrame();
	
	//used for testing and should probably exist in a
	//different form by the finished version
	private String username = ""; //set to "" - name is for easier testing Lostsamurai7
	//private boolean[] fourTrues = new boolean[]{true, true, true, true}; //was used
	
	public void init() {
		
		initialising = true;
	    initMenuBar();
		initLoadingPanel();
		loadingLabel.setText("Loading . . . Initialising Stat Editor");
		//needs to be done before the hiscore lookup so the stats can be inserted
		initStatsFrame();
		roomDimensionLimit = getRoomDimensionLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()));
		loadingLabel.setText("Loading . . . Fetching Stats");
		if(username != ""){
			new RuneScapeHiscore();
		}
		loadingLabel.setText("Loading . . . Initialising Interface");
		createAndShowGUI();
		loadingLabel.setText("Loading . . . Initialising Room Builder");
		initRoomBuildingFrame();
		loadingLabel.setText("Loading . . . Initialising Furniture Construction");
		initFurniBuildingFrame();
		//needs to be done in 'reverse order' - smallest to largest
		loadingLabel.setText("Loading . . . Creating Material IDs");
		materialIDHash = initMaterialDHash(materialIDHash);
		loadingLabel.setText("Loading . . . Creating Materials");
		materialHash = initMaterialHash(materialHash);
		loadingLabel.setText("Loading . . . Creating Furniture IDs");
		furniIDHash = initFurniIDHash(furniIDHash);
		loadingLabel.setText("Loading . . . Creating Furniture");
		furniHash = initFurniHash(furniHash);
		loadingLabel.setText("Loading . . . Creating Hotspots");
		hotspotHash = initHotspotHash(hotspotHash);
		loadingLabel.setText("Loading . . . Creating Room IDs");
		roomIDHash = initRoomIDHash(roomIDHash);
		loadingLabel.setText("Loading . . . Creating Rooms");
		roomHash = initRoomHash(roomHash, util);
		loadingLabel.setText("Loading . . . Building House");
		//causes problems when rotating the Parlour >.<
		//menuHouseCodeTextbox.setText("001,13405fttt00000000000000,14404tttt01000000000000,");
		//loadHouseFromSharingCode();
		initRoomArray(); //should eventually be replaced by loadHouseFromSharingCode()
		//setEmptyRoomArray();
		loadingLabel.setText("Loading . . . Initialising Room Information");
		initRoomInfoPanel();
		loadingLabel.setText("Loading . . . Initialising Statistics Panel");
		initRightPanel();
		
		//inducePointlessWait();
		
		loadingLabel.setText("Loading . . . Initialising House Statistics");
		initRightHouseOverviewPanel();
		loadingLabel.setText("Loading . . . Showing House");
		createAndShowGrid(overviewViewingLevel);
		loadingPanel.setVisible(false);
		corePanel.setVisible(true);
		menuHouseCodeButton.setEnabled(true);
		calculateSharingCode();
		initialising = false;
	}

	/**
     * Initialises the Menu Bar
     */
	public void initMenuBar() {
		
		//menuBar.add(mainMenu);
	    //mainMenu.add(menuItem);
		menuBar.add(transformMenu);
		transformMenu.add(moveHouseNorthMenuItem);
		transformMenu.add(moveHouseEastMenuItem);
		transformMenu.add(moveHouseSouthMenuItem);
		transformMenu.add(moveHouseWestMenuItem);
		//transformMenu.add(flipHouseHorizontallyMenuItem);
		//transformMenu.add(flipHouseVerticallyMenuItem);
		//transformMenu.add(rotateHouseClockwiseMenuItem); //not working
		//transformMenu.add(rotateHouseAntiClockwiseMenuItem);
		
		moveHouseNorthMenuItem.addActionListener(this);
		moveHouseSouthMenuItem.addActionListener(this);
		moveHouseEastMenuItem.addActionListener(this);
		moveHouseWestMenuItem.addActionListener(this);
		flipHouseHorizontallyMenuItem.addActionListener(this);
		flipHouseVerticallyMenuItem.addActionListener(this);
		rotateHouseClockwiseMenuItem.addActionListener(this);
		rotateHouseAntiClockwiseMenuItem.addActionListener(this);
		
		//add a way to use keys to do things
		moveHouseNorthMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_UP, ActionEvent.ALT_MASK));
		moveHouseSouthMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_DOWN, ActionEvent.ALT_MASK));
		moveHouseEastMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK));
		moveHouseWestMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_LEFT, ActionEvent.ALT_MASK));
		moveHouseNorthMenuItem.setMnemonic(KeyEvent.VK_N);
		moveHouseSouthMenuItem.setMnemonic(KeyEvent.VK_S);
		moveHouseEastMenuItem.setMnemonic(KeyEvent.VK_E);
		moveHouseWestMenuItem.setMnemonic(KeyEvent.VK_W);
		
	    //add space between menu and House Code
	    menuBar.add(Box.createRigidArea(new Dimension(50, 0)));
	    //menuBar.add(Box.createHorizontalGlue());
	    
	    menuHouseCodeLabel.setText("House Plan: ");
	    menuHouseCodeButton.setText("Load");
	    menuHouseCodeButton.setEnabled( false );
	    menuHouseCodeButton.addMouseListener(this);
	    menuHouseCodeTextbox.addActionListener(this);
	    menuHouseCodeTextbox.addMouseListener(this);
	    menuHouseCodeButton.addActionListener(this);
	    
	    String tooltip = "The code for the house, which you can share with friends and "
    		+ "save to load later";
	    menuHouseCodeLabel.setToolTipText(tooltip);
	    menuHouseCodeTextbox.setToolTipText(tooltip);
	    menuHouseCodeButton.setToolTipText("Load house design from code");
	    
	    menuBar.add(menuHouseCodeLabel);
	    menuBar.add(menuHouseCodeTextbox);
	    menuBar.add(menuHouseCodeButton);
	    
	    setJMenuBar(menuBar);

	}

	/**
	 * Initialises the loading panel. Yes, it's ridiculously simple, but
	 * makes it look nicer. =P
	 */
	public void initLoadingPanel(){
		
		//set basics
		this.setForeground( bgcream );
	    this.setSize(725, 549);
	    this.setVisible( true );
	    this.add(almightyPanel);
	    corePanel.setVisible(false);
		
		loadingPanel.setLayout(new GridBagLayout());
		loadingPanel.setBackground( bgcream );
		loadingPanel.add(loadingLabel);
		loadingLabel.setForeground(txtblue);
		loadingLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		
		//big main almighty Panel
	    almightyPanel.setLayout(new GridBagLayout());
	    GridBagConstraints almightyConstraints = new GridBagConstraints();
	    almightyConstraints.fill = GridBagConstraints.BOTH;
	    almightyConstraints.weightx = 1.0;
	    almightyConstraints.weighty = 1.0;
	    almightyPanel.add(loadingPanel, almightyConstraints);
	    almightyPanel.add(corePanel, almightyConstraints);
	}
	
	/**
	 * Initialises the Stats Frame
	 */
	public void initStatsFrame(){
		viewStatsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		viewStatsFrame.setBounds(200, 60, 350, 488);
		viewStatsFrame.setIconImage(util.loadImage("statsicon.gif").getImage());
		viewStatsFrame.setTitle("View and Edit your Stats and completed Quests");

		viewStatsFrame.add(viewStatsScrollPane);
		viewStatsPanel.setLayout(new GridBagLayout());
		viewStatsPanel.setBackground( bgcream );
		
		viewStatsTitleLabel.setForeground(txtblue);
		viewStatsTitleLabel.setFont(titleFont16);
		viewStatsQuestTitleLabel.setForeground(txtblue);
		viewStatsQuestTitleLabel.setFont(titleFont16);
		
		//set ToolTips
		viewStatsTitleLabel.setToolTipText("Set your stats");
		viewStatsQuestTitleLabel.setToolTipText("Set the important Quests related to Construction that you have completed.");
		viewStatsSaveAndCloseButton.setToolTipText("Save your stats and close this window.");
		viewStatsSaveButton.setToolTipText("Save your stats");
		
		//create the various skills
		int numberOfStats = 24;
		statsPanelHash.put(0, new StatsPane("Attack", 1, util));
		statsPanelHash.put(1, new StatsPane("Defence", 2, util));
		statsPanelHash.put(2, new StatsPane("Strength", 3, util));
		statsPanelHash.put(3, new StatsPane("Hitpoints", 4, util));
		statsPanelHash.put(4, new StatsPane("Ranged", 5, util));
		loadingLabel.setText("Loading . . . Initialising Stat Editor 5/"+numberOfStats);
		statsPanelHash.put(5, new StatsPane("Prayer", 6, util));
		statsPanelHash.put(6, new StatsPane("Magic", 7, util));
		statsPanelHash.put(7, new StatsPane("Cooking", 8, util));
		statsPanelHash.put(8, new StatsPane("Woodcutting", 9, util));
		statsPanelHash.put(9, new StatsPane("Fletching", 10, util));
		loadingLabel.setText("Loading . . . Initialising Stat Editor 10/"+numberOfStats);
		statsPanelHash.put(10, new StatsPane("Fishing", 11, util));
		statsPanelHash.put(11, new StatsPane("Firemaking", 12, util));
		statsPanelHash.put(12, new StatsPane("Crafting", 13, util));
		statsPanelHash.put(13, new StatsPane("Smithing", 14, util));
		statsPanelHash.put(14, new StatsPane("Mining", 15, util));
		loadingLabel.setText("Loading . . . Initialising Stat Editor 15/"+numberOfStats);
		statsPanelHash.put(15, new StatsPane("Herblore", 16, util));
		statsPanelHash.put(16, new StatsPane("Agility", 17, util));
		statsPanelHash.put(17, new StatsPane("Thieving", 18, util));
		statsPanelHash.put(18, new StatsPane("Slayer", 19, util));
		statsPanelHash.put(19, new StatsPane("Farming", 20, util));
		statsPanelHash.put(20, new StatsPane("Runecrafting", 21, util));
		loadingLabel.setText("Loading . . . Initialising Stat Editor 20/"+numberOfStats);
		statsPanelHash.put(21, new StatsPane("Hunter", 22, util));
		statsPanelHash.put(22, new StatsPane("Construction", 23, util));
		statsPanelHash.put(23, new StatsPane("Summoning", 24, util));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridwidth = 4;
		c.insets = bottomFiveTopThreeInsets;
		viewStatsPanel.add(viewStatsTitleLabel, c);
		c.gridwidth = 1;
		c.gridy = 1;
		c.gridx = 0;
		c.insets = threesInsets;
		
		for(int i = 0; i < 24; i++){
			//work out which row and column to add to
			if(i%2 == 0){
				c.gridy++;
			}
			if(c.gridx > 3){
				c.gridx = 0;
			} 
			//add the label
			viewStatsPanel.add(statsPanelHash.get(i).getSkillLabel(), c);
			//move into the next column
			c.gridx++;
			//add the textBox
			viewStatsPanel.add(statsPanelHash.get(i).getLevelBox(), c);
			statsPanelHash.get(i).getLevelBox().addActionListener(this);
			statsPanelHash.get(i).getLevelBox().addMouseListener(this);
			c.gridx++;
		}
		
		//add a save stats button
		c.insets = bottomFiveTopThreeInsets;
		c.gridwidth = 4;
		c.gridy++;
		c.gridx = 0;
		viewStatsPanel.add(viewStatsSaveButton, c);
		viewStatsSaveButton.addMouseListener(this);
		
		//add Quest title
		
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy++;
		viewStatsPanel.add(viewStatsQuestTitleLabel, c);
		
		//add Quest help
		c.gridy++;
		viewStatsPanel.add(viewStatsQuestHelpLabel, c);
		
		//add buttons to select and deselect all the Quests
		c.gridwidth = 2;
		c.gridy++;
		viewStatsPanel.add(viewStatsSelectAllQuestsButton, c);
		viewStatsSelectAllQuestsButton.addMouseListener(this);
		c.gridx+=2;
		viewStatsPanel.add(viewStatsDeselectAllQuestsButton, c);
		viewStatsDeselectAllQuestsButton.addMouseListener(this);
		
		initQuestHash();
		
		c.gridx = 0;
		c.gridy++;
		c.insets = zeroedInsets;
		//add quest checkboxes
		for(int i = 0; i < 25; i++){
			if(c.gridx > 2){
				c.gridx = 0;
				c.gridy++;
			} 
			viewStatsPanel.add(questHash.get(i).getQuestBox(), c);
			questHash.get(i).getQuestBox().addItemListener(this);
			questHash.get(i).getQuestBox().setBackground(bgcream);
			c.gridx+=2;
		}
		
		//add a second save stats button which also closes the window
		c.insets = bottomFiveTopThreeInsets;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 4;
		viewStatsPanel.add(viewStatsSaveAndCloseButton, c);
		viewStatsSaveAndCloseButton.addMouseListener(this);
		
	}
	/**
	 * Sets the stats based on what's entered in the boxes
	 */
	public void updateStats(){
		boolean validEntries = true;
		
		//loop through boxes
		for(int i = 0; i < 24; i++){
        	//set to a default of level 1 is empty
        	if(statsPanelHash.get(i).getLevelBox().getText().equals("")) {
        		statsPanelHash.get(i).getLevelBox().setText("1");
            }
            //System.out.println(i + " i: " + statsPanelHash.get(i).getLevelBox().getText());
        	
        	//update the stored level
        	try{hiscoreList.add((i+1), new HiscoreData(
        			0,
        			Integer.parseInt(statsPanelHash.get(i).getLevelBox().getText()),
        			calculateXPForLevel(Integer.parseInt(statsPanelHash.get(i).getLevelBox().getText())),
        			false));
        	} catch(NumberFormatException n){
        		JOptionPane.showMessageDialog( null,
						"Your stats should be numbers and not contain letters or other characters.",
						"Invalid Stats", 
			            JOptionPane.ERROR_MESSAGE );
        		validEntries = false;
        		viewStatsPanel.setVisible(true);
        		break;
        	}
        	catch(Exception e){
        		//System.out.println("err: " + e);
        	}
        	
        	//update dimension limit
        	roomDimensionLimit = getRoomDimensionLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()));
        	
        	//System.out.println("update: " + i);
        }
		//System.out.println("User: "+username+" Con lvl: "+Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()));
		
		if(validEntries){
			//update furniBuildFrame if you've decided to choose the error
			//message option which asks if you want to update your stats
			if(viewingFurniBuildFrame){
				createFurniBuildingFrameContent(furniBuildingFrameHotspotType);
			} else if(viewingRoomBuildFrame){
				roomBuildingFrame.setVisible(true);
			}
			
			//update displayed room limit
			rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]
			     + "/" + getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())));
		}
	}
	
	/**
	 * Initialises the QuestHash with lots of nice Quests
	 */
	public void initQuestHash(){
		
		questHash.put(0, new Quest("51 Quest Points"));
		questHash.put(1, new Quest("101 Quest Points"));
		questHash.put(2, new Quest("151 Quest Points"));
		questHash.put(3, new Quest("The Holy Grail"));
		questHash.put(4, new Quest("Plague City"));
		questHash.put(5, new Quest("The Giant Dwarf"));
		questHash.put(6, new Quest("Throne of Miscellania"));
		questHash.put(7, new Quest("Sheep Shearer"));
		questHash.put(8, new Quest("Cook's Assistant"));
		questHash.put(9, new Quest("Rune Mysteries"));
		questHash.put(10, new Quest("Restless Ghost"));
		questHash.put(11, new Quest("Prince Ali Rescue"));
		questHash.put(12, new Quest("Tourist Trap"));
		questHash.put(13, new Quest("The Feud"));
		questHash.put(14, new Quest("The Golem"));
		questHash.put(15, new Quest("Shades of Mort'ton"));
		questHash.put(16, new Quest("Creature of Fenkenstrain"));
		questHash.put(17, new Quest("Ghosts Ahoy"));
		questHash.put(18, new Quest("Haunted Mine"));
		questHash.put(19, new Quest("Pirate's Treasure"));
		questHash.put(20, new Quest("Tai Bwo Wannai Trio"));
		questHash.put(21, new Quest("Shilo Village"));
		questHash.put(22, new Quest("Roving Elves"));
		questHash.put(23, new Quest("Watchtower"));
		questHash.put(24, new Quest("Desert Treasure"));
	}
	/**
	 * Selects all the Quests
	 */
	public void selectAllQuests(){
		for(int i = 0; i < 25; i++){
			questHash.get(i).getQuestBox().setSelected(true);
		}
	}
	/**
	 * Deselects all the Quests
	 */
	public void deselectAllQuests(){
		for(int i = 0; i < 25; i++){
			questHash.get(i).getQuestBox().setSelected(false);
		}
	}

	/**
     * Initialises the main GUI window.
     */
	public void createAndShowGUI() {
		
		//moved into initLoadingPanel();
		/*//set size and basic things like that
		this.setForeground( Color.white );
	    this.setSize(715, 544);
	    this.setVisible( true );
	    this.add(almightyPanel);
	    //this.add(corePanel); //use almightyPanel so there's a hawt loading screen

	    //loadingPanel.setVisible(true); //unneded
	    corePanel.setVisible(false);*/

	    //set the various layouts
	    corePanel.setLayout(new GridBagLayout());
	    leftPanel.setLayout(new GridBagLayout());
	    rightPanel.setLayout(new GridBagLayout());
	    overviewPanel.setLayout(mainOverviewLayout);
	    roomPanel.setLayout(mainRoomGridLayout);
	    roomInfoPanel.setLayout(new BorderLayout());
	    
	    //add borders
	    overviewPanel.setBorder(roomBorder);
	    roomPanel.setBorder(roomBorder);
	    roomInfoPanel.setBorder(roomBorder);
	    rightPanel.setBorder(roomBorder);
	    
	    //set backgrounds
        corePanel.setBackground( bgcream );
        leftPanel.setBackground( bgcream );

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
	
	/**
     * Initialises the Room Building Frame
     */
	public void initRoomBuildingFrame(){
		
		roomBuildingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		roomBuildingFrame.setBounds(50, 80, 600, 400);
		roomBuildingFrame.setIconImage(util.loadImage("hammer.gif").getImage());
		roomBuildingFrame.setTitle("Build a new Room");

		roomBuildingFrame.add(roomBuildingScrollPane);
		roomBuildingScrollPanel.setLayout(new GridLayout(21, 2, 0, 0));
		
		buildButtonGarden.setText("Build Room: Garden");
		buildButtonParlour.setText("Build Room: Parlour");
		buildButtonKitchen.setText("Build Room: Kitchen");
		buildButtonDiningRoom.setText("Build Room: Dining Room");
		buildButtonWorkshop.setText("Build Room: Workshop");
		buildButtonBedroom.setText("Build Room: Bedroom");
		buildButtonSkillHall.setText("Build Room: Skill Hall");
		buildButtonGamesRoom.setText("Build Room: Games Room");
		buildButtonCombatRoom.setText("Build Room: Combat Room");
		buildButtonQuestHall.setText("Build Room: Quest Hall");
		buildButtonStudy.setText("Build Room: Study");
		buildButtonCostumeRoom.setText("Build Room: Costume Room");
		buildButtonChapel.setText("Build Room: Chapel");
		buildButtonPortalChamber.setText("Build Room: Portal Chamber");
		buildButtonFormalGarden.setText("Build Room: Formal Garden");
		buildButtonThroneRoom.setText("Build Room: Throne Room");
		buildButtonOubliette.setText("Build Room: Oubliette");
		buildButtonDungeonCorridor.setText("Build Room: Dungeon Corridor");
		buildButtonDungeonJunction.setText("Build Room: Dungeon Junction");
		buildButtonDungeonStairs.setText("Build Room: Dungeon Stairs");
		buildButtonTreasureRoom.setText("Build Room: Treasure Room");
		
		buildButtonGarden.setToolTipText("Build a Garden");
		buildButtonParlour.setToolTipText("Build a Parlour");
		buildButtonKitchen.setToolTipText("Build a Kitchen");
		buildButtonDiningRoom.setToolTipText("Build a Dining Room");
		buildButtonWorkshop.setToolTipText("Build a Workshop");
		buildButtonBedroom.setToolTipText("Build a Bedroom");
		buildButtonSkillHall.setToolTipText("Build a Skill Hall");
		buildButtonGamesRoom.setToolTipText("Build a Games Room");
		buildButtonCombatRoom.setToolTipText("Build a Combat Room");
		buildButtonQuestHall.setToolTipText("Build a Quest Hall");
		buildButtonStudy.setToolTipText("Build a Study");
		buildButtonCostumeRoom.setToolTipText("Build a Costume Room");
		buildButtonChapel.setToolTipText("Build a Chapel");
		buildButtonPortalChamber.setToolTipText("Build a Portal Chamber");
		buildButtonFormalGarden.setToolTipText("Build a Formal Garden");
		buildButtonThroneRoom.setToolTipText("Build a Throne Room");
		buildButtonOubliette.setToolTipText("Build an Oubliette");
		buildButtonDungeonCorridor.setToolTipText("Build a Dungeon Corridor");
		buildButtonDungeonJunction.setToolTipText("Build a Dungeon Junction");
		buildButtonDungeonStairs.setToolTipText("Build Dungeon Stairs");
		buildButtonTreasureRoom.setToolTipText("Build a Treasure Room");

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

	}

	/**
     * Initialises the Furniture Building Frame
     */
	public void initFurniBuildingFrame(){
		
		furniBuildingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//furniBuildingFrame.setBounds(50, 80, 600, 400);
		furniBuildingFrame.setIconImage(util.loadImage("hammer.gif").getImage());
		furniBuildingFrame.setTitle("Build Furniture");
		
		furniRemoveFromHotspotLabel.setForeground(txtred);
		
		furniBuildingFrame.add(furniBuildingScrollPane);
		furniBuildingScrollPanel.setBackground( bgcream );
		
		//init furniBuildHash with 9 panels
		for(int i = 0; i < 9; i++){
			//create the panel
			furniBuildHash.put( i , new FurniBuildPanel());
			//set layout
			furniBuildHash.get( i ).getBuildPanel().setLayout(new GridBagLayout());
			//set the background colour
			furniBuildHash.get( i ).getBuildPanel().setBackground( bgcream );
			//set border
			furniBuildHash.get( i ).getBuildPanel().setBorder(roomBorder);
			//add mouse listener to pane
			furniBuildHash.get( i ).getBuildPanel().addMouseListener(this);
		}
		
		furniBuildingFrame.setVisible(false);
		viewingFurniBuildFrame = false;

	}
	/**
     * Adds info to the Furniture Building Frame
     * 
     * @param hotspotType The type of hotspot to set up data for
     */
	public void createFurniBuildingFrameContent(String hotspotType){
		Hotspot hotspot = new Hotspot(hotspotHash.get(hotspotType), new int[][]{ new int[]{0}, }, new int[][]{ new int[]{0}, }, new int[]{0} );
		//workOuthotspotID(hotspotType);
		furniBuildingFrameHotspotType = hotspotType;
		
		int furniCost = 0;
		int furniXP = 0;
		int numberOfAddedPanels = 0;
		int i = 0;
		int scrollPaneGridRows;
		int remainder = hotspot.getFurniTypes().length % 2;
		String builtFurni = "";
		//work out what's already built in the spot
		builtFurni = houseHash.get(viewingRoom).getHotspots()[selectedHotspotID].getBuiltFurniture().getFurniType();

		//houseHash.get(viewingRoom).getHotspots()[i].getBuiltFurniture().getFurniType();
		//reset panels
		for(int j = 0; j < 8; j++){
			furniBuildHash.get( j ).getBuildPanel().removeAll();
		//	furniBuildHash.get( j ).setBuiltInStatus(false);
		}
		furniBuildingScrollPanel.removeAll();
		//set number of columns so there's not lots of spare space
		scrollPaneGridRows = remainder != 0 ? (hotspot.getFurniTypes().length/2)+1 : hotspot.getFurniTypes().length/2 ;
		furniBuildingScrollPanel.setLayout(new GridLayout(scrollPaneGridRows, 2, 0, 0));
		//then set the size for the same reason
		furniBuildingFrame.setBounds(50, 80, 630, (scrollPaneGridRows*100));
		
		//add various information about what you're about to build
		for( ; i < hotspot.getFurniTypes().length; i++){
			furniBuildHash.get( i ).setFurni(hotspot.getFurniTypes()[i]);
			
			GridBagConstraints c = new GridBagConstraints();
			//set panels to display content in
			if(i == 0){
				//set text for label based on if anything is currently built or not
				if(builtFurni != "None"){
					furniRemoveFromHotspotLabel.setText("Remove " + builtFurni);
					furniBuildHash.get( i ).getBuildPanel().setToolTipText("Remove " + builtFurni);
				} else {
					furniRemoveFromHotspotLabel.setText("Close Furniture Building Window");
					furniBuildHash.get( i ).getBuildPanel().setToolTipText("Close Furniture Building Window");
				}
				furniBuildHash.get( i ).getBuildPanel().add(furniRemoveFromHotspotLabel, c);

				//add it to furniBuildingScrollPanel
				furniBuildingScrollPanel.add(furniBuildHash.get( i ).getBuildPanel());
				numberOfAddedPanels++;
				continue;
			}

			furniCost = 0;
			furniXP = 0;
			
			JLabel furniTypeLabel = new JLabel(hotspot.getFurniTypes()[i].getFurniType());
			JLabel furniImageLabel = new JLabel(util.loadImage("furniIcons/"+hotspot.getFurniTypes()[i].getFurniURL()+".gif" ));
			JLabel materialsLabel = new JLabel("Materials:");
			JLabel furniLevelReqLabel = new JLabel("Level " + hotspot.getFurniTypes()[i].getLevel());
			
			furniBuildHash.get( i ).getBuildPanel().setToolTipText("Build " + hotspot.getFurniTypes()[i].getFurniType());
			
			String[] furniMats = new String[hotspot.getFurniTypes()[i].getMaterials().length];
			for(int j = 0; j < furniMats.length; j++){
				//MaterialName: NumberOfMaterial
				furniMats[j] = hotspot.getFurniTypes()[i].getMaterials()[j].getMaterialType() + ": "
						+ hotspot.getFurniTypes()[i].getMaterialNumbers()[j];
			}

			//calculate cost and xp of item
			furniCost = calculateSingleFurniCost(hotspot.getFurniTypes()[i]);
			furniXP = calculateSingleFurniXP(hotspot.getFurniTypes()[i]);
			
			JLabel furniCostLabel = new JLabel("Cost: " + Integer.toString(furniCost) + "gp");
			JLabel furniXPLabel = new JLabel("Exp: " + Integer.toString(furniXP));
			
			//set colours
			materialsLabel.setForeground(txtred);
			furniTypeLabel.setForeground(txtred);
			furniLevelReqLabel.setForeground(txtblue);
			furniCostLabel.setForeground(txtblue);
			furniXPLabel.setForeground(txtblue);
			
			//put everything together and display
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridheight = 3;
			c.insets = new Insets(0, 20, 0, 20);
			
			//adds a cross if the level requirement is too high
			//also changes the toolTipText
			try{
				if(hotspot.getFurniTypes()[i].getLevel() > Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())
						&& hotspot.getFurniTypes()[i].getLevel() > 1
							&& !ignoreLevelWarnings){
					furniBuildHash.get( i ).getBuildPanel().add(new JLabel(util.loadImage("furniIcons/unavailable.gif")), c);
					furniBuildHash.get( i ).getBuildPanel().setToolTipText("The level requirement to build a " +
							hotspot.getFurniTypes()[i].getFurniType() + " is too high for you to build.");
				}
			} catch(Exception e){}
			
			furniBuildHash.get( i ).getBuildPanel().add(furniImageLabel, c);
			
			c.gridheight = 1;
			c.weightx = 1.0;
			c.gridx = 1;
			c.insets = new Insets(0, 0, 0, 0);
			furniBuildHash.get( i ).getBuildPanel().add(furniTypeLabel, c);
	
			c.gridy = 1;
			furniBuildHash.get( i ).getBuildPanel().add(furniLevelReqLabel, c);
			
			c.gridy = 2;
			furniBuildHash.get( i ).getBuildPanel().add(furniCostLabel, c);
			
			c.gridy = 3;
			furniBuildHash.get( i ).getBuildPanel().add(furniXPLabel, c);
			
			c.gridx = 2;
			c.gridy = 0;
			furniBuildHash.get( i ).getBuildPanel().add(materialsLabel, c);
			for(int j = 0; j < furniMats.length; j++){
				c.gridy++;
				JLabel matLabel = new JLabel(furniMats[j]);
				matLabel.setForeground(txtblue);
				furniBuildHash.get( i ).getBuildPanel().add(matLabel, c);
			}
			
			//add it to furniBuildingScrollPanel
			furniBuildingScrollPanel.add(furniBuildHash.get( i ).getBuildPanel());
			numberOfAddedPanels++;
			
		}
		//ensure there are an even number of panels so it looks hawt
		if(remainder != 0){
			i++;
			furniBuildingScrollPanel.add(furniBuildHash.get( i ).getBuildPanel());
		}

		furniBuildingFrame.setVisible(true);
		viewingFurniBuildFrame = true;

	}
	/**
	 * Works out the ID of the selected hotspot based on which room is being
	 * viewed and the type of hotspot that has been selected.
	 * SHOULD SET selectedHotspotID INSTEAD OF USING THIS OTHERWISE PROBLEMS
	 * OCCUR WHEN THERE IS MORE THAN ONE OF A TYPE OF HOTSPOT IN A ROOM eg. 
	 * THERE ARE 3 CHAIRS IN A PARLOUR
	 * @param hotspotType The type of hotspot that has been selected
	 */
	public void workOuthotspotID(String hotspotType){
		//work out what type of room and hotspot you're looking at
		selectedHotspotID = 0;

		for(; selectedHotspotID < houseHash.get( viewingRoom ).getHotspots().length; selectedHotspotID++){
			if(houseHash.get( viewingRoom ).getHotspots()[selectedHotspotID].getHotspotType().equals(hotspotType)){
				break;
			}
		}
	}

	/**
     * Initialises and resets the room layout so there is just the basic Parlour and Garden
     */
	public void initRoomArray(){

		initialisingRoomAray = true;
		//String hashKey;
		
		setEmptyRoomArray();
		
		//add a parlour and garden as a default house layout
		buildRoom("overviewRoom134", "Parlour", 1);
		buildRoom("overviewRoom144", "Garden", 1);
		
		//add exit portal to Garden
		houseHash.get("overviewRoom144").getHotspots()[0].setBuiltFurniture(new Furniture(furniHash.get("Exit Portal")));
		
		initialisingRoomAray = false;
	}
	/**
	 * Sets all the rooms in houseHash to empty.
	 */
	public void setEmptyRoomArray(){
		String hashKey;
		
		for(int i = 0;i < 3;i++){
			loadingLabel.setText("Loading . . . Building House - Floor " + (i+1) + "/3");
			for(int j = 0;j < 9; j++){
				for(int k = 0;k < 9; k++){
					hashKey = "overviewRoom" + i + j + k;
					
					if(i == 1){
						houseHash.put( hashKey , new Room(roomHash.get("EmptyFloor1"), i));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 0) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyDungeon"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 2) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyUpstairs"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
					}
				}
			}
		}
		
	}
	
	/**
     * Initialises the tabbed pane with stats and such when in Room View
     */
	public void initRoomInfoPanel(){
		
		//set background colours
		roomInfoPanel.setBackground( bgcream );
		//looks icky, so left as default colour :P
        //roomInfoOverviewPanel.setBackground( bgcream );
        //roomInfoBuildFurniScrollPane.setBackground( bgcream );
		
		//init furniStatusHash with 8 labels (roughly the max hotspots in a room :P)
		//also create 8 buttons for building furni to furniButtonHash
		for(int i = 0; i < 8; i++){
			//create the panel
			furniStatusHash.put( i , new FurniStatusLabel());
			//create a new button
			furniButtonHash.put(i, new FurniBuildButton());
			furniButtonHash.get(i).getBuildButton().addMouseListener(this);
		}
		
		roomInfoPanel.add(roomInfoTabbedPane);
		
		roomInfoOverviewPanel.setLayout(new GridBagLayout());
		roomInfoRoomTypeLabel.setFont(titleFont);
		roomInfoRoomTypeLabel.setForeground(txtblue);
		roomInfoHotspotTitleLabel.setFont(titleFont);
		roomInfoHotspotTitleLabel.setForeground(txtblue);
		
		roomInfoTabbedPane.addTab("Room Overview", null, roomInfoOverviewScrollPane,
				"Gives an Overview of the room, with various stats about it.");
		roomInfoTabbedPane.addTab("Build Furniture", null, roomInfoBuildFurniScrollPane,
				"Lets you build furniture within the room.");

	}
	
	/**
	 * Initialises the right panel
	 */
	public void initRightPanel(){
		//set layouts for sub-panels
		rightNavPanel.setLayout(new GridBagLayout());
	    rightRoomInfoPanel.setLayout(new GridBagLayout());
	    rightHouseOverviewPanel.setLayout(new GridBagLayout());
	    rightGeneralToolsPanel.setLayout(new GridBagLayout());
	    rightPoweredByPanel.setLayout(new GridBagLayout());
	    
	    //set background colours
	    rightPanel.setBackground( bgcream );
	    rightNavPanel.setBackground( bgcream );
	    rightGeneralToolsPanel.setBackground( bgcream );
	    rightHouseOverviewPanel.setBackground( bgcream );
	    rightRoomInfoPanel.setBackground( bgcream );
	    rightPoweredByPanel.setBackground( bgcream );
	    
	    //set text colours and styles
	    rightRoomInfoRoomTypeLabel.setForeground( txtblue );
	    rightRoomInfoRoomTypeLabel.setFont(titleFont);
	    rightOverviewTitleLabel.setForeground( txtblue );
	    rightOverviewTitleLabel.setFont(titleFont);
	    
	    //set layout
		GridBagConstraints rightConstraints = new GridBagConstraints();
	    rightConstraints.fill = GridBagConstraints.HORIZONTAL;
	    rightConstraints.anchor = GridBagConstraints.NORTH;
	    rightConstraints.gridy = 0;
	    rightPanel.add(rightNavPanel, rightConstraints);
	    rightConstraints.fill = GridBagConstraints.BOTH;
	    rightConstraints.gridy = 1;
	    rightPanel.add(rightGeneralToolsPanel, rightConstraints);
	    rightConstraints.gridy = 2;
	    rightConstraints.weighty = 0.05; //stops some odd overlapping issues
	    rightPanel.add(rightHouseOverviewPanel, rightConstraints);
	    rightConstraints.gridy = 3;
	    rightPanel.add(rightRoomInfoPanel, rightConstraints);
	    rightConstraints.weighty = 1.0;
	    rightConstraints.gridy = 4;
	    rightPanel.add(rightPoweredByPanel, rightConstraints);
	    
	    //add event listeners for Rotate Buttons
	    rightRoomInfoRotateAntiClockwise.addMouseListener(this);
	    rightRoomInfoRotateClockwise.addMouseListener(this);
	    //and for the Delete Room Button
	    rightRoomInfoDeleteRoom.addMouseListener(this);
	    
	    //add event listeners for Nav buttons
	    rightViewGridButton.addMouseListener(this);
	    rightViewGridButton.setToolTipText("Switch to House Overview mode.");
	    comboSelectGridLevel.setSelectedIndex(overviewViewingLevel);
	    comboSelectGridLevel.addActionListener(this);
	    comboSelectGridLevel.setToolTipText("Choose which floor of the house to view.");
	    
	    //add Nav buttons
	    rightNavPanel.add(comboSelectGridLevelLabel);
	    rightNavPanel.add(comboSelectGridLevel);
	    rightNavPanel.add(rightViewGridButton);
	    rightViewGridButton.setVisible(false);
	    
	    //set settings for General Tools things
	    rightGeneralToolsUpdateGEPrices.addMouseListener(this);
	    rightGeneralToolsUpdateGEPrices.setToolTipText("Fetches Grand Exchange prices for items.");
	    rightGeneralToolsUsernameTextbox.setDocument(new TextFieldLimiter(12));
		rightGeneralToolsUsernameTextbox.setText(username);
		rightGeneralToolsUsernameTextbox.addMouseListener(this);
		rightGeneralToolsUsernameTextbox.addActionListener(this);
		rightGeneralToolsUsernameTextbox.setToolTipText("Insert your RuneScape username.");
		rightGeneralToolsUsernameLabel.setToolTipText("Insert your RuneScape username.");
		rightGeneralToolsUpdateUsername.addActionListener(this);
		rightGeneralToolsUpdateUsername.setToolTipText("Retrieves your stats from the RuneScape Hiscores.");
		rightGeneralToolsViewStats.addMouseListener(this);
		rightGeneralToolsViewStats.setToolTipText("View and edit your stats and completed Quests.");
		
	    //add General Tools things
		GridBagConstraints rgs = new GridBagConstraints();
		rightConstraints.fill = GridBagConstraints.HORIZONTAL;
		rgs.insets = topTenInsets;
		rightGeneralToolsPanel.add(rightGeneralToolsUsernameLabel, rgs);
		rightGeneralToolsPanel.add(rightGeneralToolsUsernameTextbox, rgs);
		rgs.insets = topFiveInsets;
		rgs.gridy = 1;
		rightGeneralToolsPanel.add(rightGeneralToolsViewStats, rgs);
		rightGeneralToolsPanel.add(rightGeneralToolsUpdateUsername, rgs);
		rgs.gridwidth = 2;
		rgs.gridy = 2;
		rgs.weighty = 1.0;
		rightGeneralToolsPanel.add(rightGeneralToolsUpdateGEPrices, rgs);
		
		//add poweredBy icon
		GridBagConstraints pby = new GridBagConstraints();
		pby.anchor = GridBagConstraints.PAGE_END;
		pby.insets = bottomFiveInsets;
		pby.weighty = 1.0;
		powerImageLabel.setToolTipText("Powered by Sal's Realm");
		rightPoweredByPanel.add(powerImageLabel, pby);
		
	}
	/**
	 * Initialises the rightHouseOverviewPanel
	 */
	public void initRightHouseOverviewPanel(){
		
		rightOverviewLabelNumbers = calculateNumberOfRoomsAndFurniAndStats();
		
		//set text for labels
		rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]
		        + "/" + getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())));
		rightOverviewRoomCostLabel.setText("Rooms Cost: " + rightOverviewLabelNumbers[1] + "gp");
		rightOverviewFurniCostLabel.setText("Cost of Furniture: " + rightOverviewLabelNumbers[2] + "gp");
		rightOverviewFurniNumberLabel.setText("Furniture Exp: " + rightOverviewLabelNumbers[3] + "xp");
		rightOverviewTotalCostLabel.setText("Total Cost: "
				+ (rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2]) + "gp");
		
		//add overview label
		GridBagConstraints ros = new GridBagConstraints();
		ros.anchor = GridBagConstraints.FIRST_LINE_START;
		ros.fill = GridBagConstraints.HORIZONTAL;
		ros.ipady = 20;
		rightHouseOverviewPanel.add(rightOverviewTitleLabel, ros);

		//add the rest of the labels
		ros.ipady = 0;
		ros.gridy = 1;
		rightHouseOverviewPanel.add(rightOverviewRoomNumberLabel, ros);
		ros.gridy = 2;
		rightHouseOverviewPanel.add(rightOverviewRoomCostLabel, ros);
		ros.gridy = 3;
		rightHouseOverviewPanel.add(rightOverviewFurniCostLabel, ros);
		ros.gridy = 4;
		rightHouseOverviewPanel.add(rightOverviewFurniNumberLabel, ros);
		ros.weighty = 1.0;
		ros.gridy = 5;
		rightHouseOverviewPanel.add(rightOverviewTotalCostLabel, ros);
	}
	
	/**
	 * No idea how or why this works, but it fixes several problems with
	 * the UI not updating properly. (DO NOT REMOVE :P)
	 */
	public void refreshGrid(){
		overviewPanel.setVisible(false);
		overviewPanel.setVisible(true);
	}
	/**
	 * Fixes a problem with rightRoomInfoPanel not updating properly.
	 */
	public void refreshRightRoomInfoPanel(){
		rightRoomInfoPanel.setVisible(false);
		rightRoomInfoPanel.setVisible(true);
	}
	/**
	 * Fixes a problem with roomPanel not updating properly.
	 */
	public void refreshRoomPanel(){
		roomPanel.setVisible(false);
		roomPanel.setVisible(true);
	}
		
	/**
     * Creates the content for and shows Grid View
     * 
     * @param houseLevel The level of the house that you want to view.
     * 0 = Dungeon. 1 = Ground Floor. 2 = Upstairs.
     */
	public void createAndShowGrid(int houseLevel) {

		//clear grid
		overviewPanel.removeAll();
		//you're no longer viewing a room
		viewingRoom = "";
		//and don't have one selected
		unselectRoom();
		//add rooms
		String hashKey = null;

		for(int i = 0;i<9;i++) {
	    	for(int j = 0;j<9;j++){
	            hashKey = "overviewRoom" + Integer.toString(houseLevel) + Integer.toString(i) + Integer.toString(j);
	    		overviewPanel.add(houseHash.get( hashKey ).getRoomOverviewLabel());
	    		//System.out.println(houseHash.get( hashKey ).getRoomOverviewLabel());
	    	}
		}

		//set the level of the house being viewed
		overviewViewingLevel = houseLevel;
		viewingGrid = true;

		//show grid
		roomPanel.setVisible(false);
	    roomInfoPanel.setVisible(false);
	    overviewPanel.setVisible(true);
	    
	    //show correct rightNav buttons
	    rightViewGridButton.setVisible(false);
	    comboSelectGridLevel.setVisible(true);
	    comboSelectGridLevelLabel.setText("View Floor: ");

	}
	/**
     * Creates the content for and shows the Room View.
     * Called through goToRoomFromGrid()
     * 
     * @param roomKey The key used to access data in houseHash
     */
	private void createAndShowRoom(String roomKey) {
	    Room room = houseHash.get(roomKey);
		//roomPanel.removeAll();
		refreshGrid();
		
		lastViewedRoom = roomKey;
		viewingRoom = roomKey;
		
		roomPanel = new RoomPanel( util.loadImage( room.getRoomImage() ).getImage(), room );
		roomPanel.addMouseListener( this );
		leftPanel.add( roomPanel );
		
	    //calculate contents for the roomInfo panel
	    workOutRoomInfoContent(roomKey);
	    
	    //show room
	    overviewPanel.setVisible(false);
	    roomPanel.setVisible(true);

	    roomInfoPanel.setVisible(true);
	    
	    //show correct rightNav buttons
	    comboSelectGridLevel.setVisible(false);
	    rightViewGridButton.setVisible(true);
	    comboSelectGridLevelLabel.setText("View: ");

	    viewingGrid = false;
		
	}
	
	/**
	 * Selects a room.
	 * @param roomKey The key used to access data in houseHash
	 */
	public void selectRoom(String roomKey){
		
		//set a previously selected room to unselected
		if(overviewSelectedRoom != ""
			&& houseHash.get(roomKey).getRoomType() != " "
				&& !rotatingRoom){
				unselectRoom();
		}
		//set the clicked room as selected
		houseHash.get(roomKey).setIsSelected(true);
		
		//specify the new selected room
		overviewSelectedRoom = roomKey;
		
		//System.out.println(roomKey + " is selected " + houseHash.get(roomKey).getIsSelected());

		//check whether the selected room is empty or not
		if(houseHash.get(roomKey).getRoomType().equals("")
			|| houseHash.get(roomKey).getRoomType().equals(" ")){
			//show building pane
			roomBuildingFrame.setVisible(true);
			viewingRoomBuildFrame = true;
		} else if(houseHash.get(roomKey).getRoomType().equals("  ")){
			JOptionPane.showMessageDialog( null, "You need to build a Room downstairs to build one here", 
                    "Oops!", JOptionPane.ERROR_MESSAGE );
		} else {
			//If not empty, update room info
			//show room info
			setRightRoomInfoPanelContent();

			//indicate that the room's selected by changing the text colour
			houseHash.get(overviewSelectedRoom).getRoomOverviewLabel().setForeground(txtred);
		}
	}
	/**
	 * Unselects a room
	 */
	public void unselectRoom(){
		if(overviewSelectedRoom != ""){
			houseHash.get(overviewSelectedRoom).setIsSelected(false);
			//indicate that the room's unselected by reverting the text colour
			houseHash.get(overviewSelectedRoom).getRoomOverviewLabel().setForeground(Color.WHITE);
			overviewSelectedRoom = "";
			resetRightRoomInfoPanelContent();
		}
	}
	
	/**
     * Switches from Grid View to Room View, checking that there has first
     * been a 'selection click'
     * @param roomKey The key used to access data in houseHash
     */
	public void goToRoomFromGrid(String roomKey){
		
		//System.out.println("go to room from grid");
		if(houseHash.get(roomKey).getIsSelected() == false){
			selectRoom(roomKey);
		} 
		
		else if(houseHash.get(roomKey).getIsSelected() == true) {
			//unselect room
			houseHash.get(roomKey).setIsSelected(false);
			resetRightRoomInfoPanelContent();
			//indicate that the room's unselected by reverting the text colour
			houseHash.get(overviewSelectedRoom).getRoomOverviewLabel().setForeground(Color.WHITE);
			overviewSelectedRoom = "";
			
			//show room
			if(houseHash.get(roomKey).getRoomType() != ""
				&& houseHash.get(roomKey).getRoomType() != " "
					&& houseHash.get(roomKey).getRoomType() != "  "){
				createAndShowRoom(roomKey);
			}			
		}
	}
	
	/**
	 * Calculates and shows the info about a room when it's selected in Grid View.
	 */
	public void setRightRoomInfoPanelContent(){
		
		resetRightRoomInfoPanelContent();
		
		//set room name label text
		rightRoomInfoRoomTypeLabel.setText(houseHash.get(overviewSelectedRoom).getRoomType()  + " Stats");

		//work out the rest of the labels
		rightRoomInfoLevelLabel.setText("Required level: " + Integer.toString(houseHash.get(overviewSelectedRoom).getRoomLevel()));
		rightRoomInfoRoomCostLabel.setText("Room Cost: " + Integer.toString(houseHash.get(overviewSelectedRoom).getRoomCost()) + "gp");
		rightRoomInfoFurniCostLabel.setText("Furniture Cost: " + calculateRoomFurniCost(overviewSelectedRoom) + "gp");
		rightRoomInfoFurniXPLabel.setText("Furniture XP: " + calculateRoomFurniXP(overviewSelectedRoom) + "xp");
		rightRoomInfoDeleteRoom.setText("Demolish " + houseHash.get(overviewSelectedRoom).getRoomType());
		
		//add room name label
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 20;
		//add the rest of the labels
		rightRoomInfoPanel.add(rightRoomInfoRoomTypeLabel, c);
		c.ipady = 0;
		c.gridy = 1;
		rightRoomInfoPanel.add(rightRoomInfoLevelLabel, c);
		c.gridy = 2;
		rightRoomInfoPanel.add(rightRoomInfoRoomCostLabel, c);
		c.gridy = 3;
		rightRoomInfoPanel.add(rightRoomInfoFurniCostLabel, c);
		c.gridy = 4;
		
		/*for(int q = 0; q < 4; q++){
			System.out.println(overviewSelectedRoom + " rightdoor: " + houseHash.get(overviewSelectedRoom).getDoorLayout()[q]);
		} System.out.println("image: " + houseHash.get(overviewSelectedRoom).getImageURL());
		*/
		//if there are 4 doors, don't show the rotate buttons
		boolean rotateable = houseHash.get(overviewSelectedRoom).getImageURL().contains("nesw")?false:true;
		if(!rotateable){
			rightRoomInfoPanel.add(rightRoomInfoFurniXPLabel, c);
			c.gridy = 5;
			c.insets = topTenInsets;
		} else {
			rightRoomInfoPanel.add(rightRoomInfoFurniXPLabel, c);
			c.insets = topTenInsets;
			c.gridy = 5;
			rightRoomInfoPanel.add(rightRoomInfoRotateClockwise, c);
			c.insets = zeroedInsets;
			c.gridy = 6;
			rightRoomInfoPanel.add(rightRoomInfoRotateAntiClockwise, c);
			c.gridy = 7;
		}
		c.weighty = 1.0;
		rightRoomInfoPanel.add(rightRoomInfoDeleteRoom, c);

		refreshRightRoomInfoPanel();
	}
	/**
	 * Resets rightRoomInfoPanel when something gets unselected.
	 */
	public void resetRightRoomInfoPanelContent(){
		rightRoomInfoPanel.removeAll();
		refreshGrid();
	}	

	/**
	 * Updates the rightHouseOverviewPanel when you build or demolish something.
	 * @param building true = building something new. false = demolition
	 * @param room true = room. false = furniture
	 * @param location room = overviewSelectedRoom (demolish) / roomKey (build).
	 * furniture = viewingRoom.
	 */
	public void updateRightHouseOverviewPanel(boolean building, boolean room, String location){
		
		if(building){
			if(room
				&& houseHash.get(location).getRoomType() != ("")
					&& houseHash.get(location).getRoomType() != (" ")
						&& houseHash.get(location).getRoomType() != ("  ")){
				//add one to the number of rooms, then update the label
				rightOverviewLabelNumbers[0] += 1;
				rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]
					+ "/" + getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())));
				
				//increase room cost by the cost of the new room, then update the label
				rightOverviewLabelNumbers[1] += houseHash.get(location).getRoomCost();
				rightOverviewRoomCostLabel.setText("Rooms Cost: "+rightOverviewLabelNumbers[1]+"gp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");

			} else if(!room) {
				//add the furni cost to the total furni cost, then update the label
				rightOverviewLabelNumbers[2] += calculateSingleFurniCost(houseHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniCostLabel.setText("Cost of Furniture: "+rightOverviewLabelNumbers[2]+"gp");
				
				//add the furni xp to the total furni xp, then update the label
				rightOverviewLabelNumbers[3] += calculateSingleFurniXP(houseHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniNumberLabel.setText("Furniture Exp: "+rightOverviewLabelNumbers[3]+"xp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");
			}
		} //if demolishing
		else {
			if(room){
				//subtract one from the number of rooms, then update the label
				rightOverviewLabelNumbers[0] -= 1;
				rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]
				     + "/" + getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())));
				
				//decrease room cost by the cost of the new room, then update the label
				rightOverviewLabelNumbers[1] -= houseHash.get(location).getRoomCost();
				rightOverviewRoomCostLabel.setText("Rooms Cost: "+rightOverviewLabelNumbers[1]+"gp");
				
				//subtract the furni cost from the total furni cost, then update the label
				rightOverviewLabelNumbers[2] -= calculateRoomFurniCost(location);
				rightOverviewFurniCostLabel.setText("Cost of Furniture: "+rightOverviewLabelNumbers[2]+"gp");
				
				//subtract the furni xp from the total furni xp, then update the label
				rightOverviewLabelNumbers[3] -= calculateRoomFurniXP(location);
				rightOverviewFurniNumberLabel.setText("Furniture Exp: "+rightOverviewLabelNumbers[3]+"xp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");
				
			} else {
				//subtract the furni cost from the total furni cost, then update the label
				rightOverviewLabelNumbers[2] -= calculateSingleFurniCost(houseHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniCostLabel.setText("Cost of Furniture: "+rightOverviewLabelNumbers[2]+"gp");
				
				//subtract the furni xp from the total furni xp, then update the label
				rightOverviewLabelNumbers[3] -= calculateSingleFurniXP(houseHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniNumberLabel.setText("Furniture Exp: "+rightOverviewLabelNumbers[3]+"xp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");
			}
		}
	}
	
	/**
	 * Builds a room.
	 * @param roomKey The key used to access data in houseHash.
	 * @param roomType The type of room you want to build.
	 * @param level The floor you want to build on.
	 * 0 = Dungeon. 1 = Ground Floor. 2 = Upstairs.
	 */
	public void buildRoom(String roomKey, String roomType, int level){
		boolean buildable = true;
		
		//check that there isn't a Costume Room already built
		if(roomType.equals("Costume Room")){
			if(costumeRoomBuilt == true){
				buildable = false;
				JOptionPane.showMessageDialog( null,
						"You can only build one Costume Room, no matter how many clothes you have or how awesome you are.",
						"Costume Room", 
			            JOptionPane.ERROR_MESSAGE );
			}
		}

		//check that you aren't trying to build a dungeon room out of the dungeon
		if(level != 0){
			if(roomType.equals("Dungeon Corridor")
				|| roomType.equals("Dungeon Junction") || roomType.equals("Dungeon Stairs")
					|| roomType.equals("Treasure Room")){
				JOptionPane.showMessageDialog( null,
						"You can only build a " + roomType + " in a Dungeon.",
						"Dungeon Room", 
			            JOptionPane.ERROR_MESSAGE );
				buildable = false;
				roomBuildingFrame.setVisible(true);
			}//only difference is 'an' instead of 'a' in error message
			else if(roomType.equals("Oubliette")){
				JOptionPane.showMessageDialog( null,
						"You can only build an " + roomType + " in a Dungeon.",
						"Dungeon Room", 
			            JOptionPane.ERROR_MESSAGE );
				buildable = false;
				roomBuildingFrame.setVisible(true);
			}
		}
		//check that you aren't trying to build a garden upstairs or in the dungeon
		if(level != 1 && roomType.contains("Garden")){
			JOptionPane.showMessageDialog( null,
				"You can only build a " + roomType + " on the Ground Floor.",
				roomType, 
			    JOptionPane.ERROR_MESSAGE );
			buildable = false;
			roomBuildingFrame.setVisible(true);
		
		}
		
		if(!loadingHouse){
			//check that you haven't already built as many rooms as you're allowed
			if(rightOverviewLabelNumbers[0]
			     >= getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()))
			     	&& roomType != "EmptyFloor1" && roomType != "EmptyUpstairs"
			     		&& roomType != "AboveUpstairs" && roomType != "EmptyDungeon"
			     			&& !loadAllRooms && !loadingHouse){
				buildable = false;
				//show an error dialogue
				String conLvl = statsPanelHash.get(22).getLevelBox().getText().equals("-1") ? "1" : statsPanelHash.get(22).getLevelBox().getText();
				JOptionPane.showMessageDialog( null,
						"You have already build the " + 
						getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())) +
						" rooms that your level " + conLvl +
						" Construction\nallows and cannot build any more without removing existing\nrooms or changing your specified Construction level.",
						"Too many rooms", 
		                JOptionPane.ERROR_MESSAGE );
			}
			
			
			//check house dimensions
			else if(!checkMostDistantRoom(roomKey)
					&& roomType != "EmptyFloor1" && roomType != "EmptyUpstairs"
			     		&& roomType != "AboveUpstairs" && roomType != "EmptyDungeon"){
				buildable = false;
				String conLvl = statsPanelHash.get(22).getLevelBox().getText().equals("-1") ? "1" : statsPanelHash.get(22).getLevelBox().getText();
				JOptionPane.showMessageDialog( null,
					"With level " + conLvl
					+ " Construction, you can only build a house with a dimension of "
					+ roomDimensionLimit + "x" + roomDimensionLimit + " rooms.\nHaving a higher Construction level allows you to create larger houses.",
					"House dimension too large", 
		            JOptionPane.ERROR_MESSAGE );
			}
		}
			
		if(buildable){
			//check that you have the level to actually build it
			if(initialisingRoomAray
					|| roomHash.get(roomType).getRoomLevel() <= Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())
						|| roomHash.get(roomType).getRoomLevel() < 2
							|| ignoreLevelWarnings){
				//build the room
				houseHash.put( roomKey , new Room(roomHash.get(roomType), level));
				houseHash.get( roomKey ).getRoomOverviewLabel().addMouseListener(this);
				houseHash.get( roomKey ).getRoomOverviewLabel().setToolTipText(houseHash.get( roomKey ).getRoomType());
		
				//increment the number of costume rooms
				//if done with the Costume Room bit further up, problems occur if your level is too low
				if(roomType.equals("Costume Room")){
					costumeRoomBuilt = true;
				}
				
				//indicate upstairs
				//when building
				if(level == 1 && houseHash.get(roomKey).getRoomType() != ""
					&& houseHash.get(roomKey).getRoomType() != "Garden"
						&& houseHash.get(roomKey).getRoomType() != "Formal Garden"){
					String newKey = "overviewRoom2" + roomKey.substring(13);
					
					houseHash.put( newKey , new Room(roomHash.get("AboveUpstairs"), 2));
					houseHash.get( newKey ).getRoomOverviewLabel().addMouseListener(this);
					houseHash.get( newKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
				}
				//when demolishing
				if(level == 1 && houseHash.get(roomKey).getRoomType().equals("")){
					String newKey = "overviewRoom2" + roomKey.substring(13);
					
					houseHash.put( newKey , new Room(roomHash.get("EmptyUpstairs"), 2));
					houseHash.get( newKey ).getRoomOverviewLabel().addMouseListener(this);
					houseHash.get( newKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
				}
				
				if(!initialisingRoomAray){
					updateRightHouseOverviewPanel(true, true, roomKey);
					calculateSharingCode();
				}
	
				setMostDistantRoom(roomKey);
				//hide building pane
				roomBuildingFrame.setVisible(false);
				viewingRoomBuildFrame = false;
				overviewSelectedRoom = "";
				
				//show the grid with the new shiny room
				//createAndShowRoom(overviewSelectedRoom);
				refreshGrid();
				createAndShowGrid(overviewViewingLevel);
				
			}
			
			
			
			else {
				//show a confirmation dialogue
				Object[] confirmFurniBuildOptions = {"Yes",
		        "No", "Edit my Stats"};
				int ignoreLevelWarningsPane = JOptionPane.showOptionDialog(ignoreLevelWarningsConfirmFrame,
				"Your Construction level is not high enough to build a " +
				roomHash.get(roomType).getRoomType() + ".\n" + //stops odd loading error messages
				"Do you want to ignore level requirements and build it anyway?",
				"Confirm Room Construction",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null,     //do not use a custom Icon
				confirmFurniBuildOptions,  //the titles of buttons
				confirmFurniBuildOptions[1]); //default button title
				
				//build it anyway
				if (ignoreLevelWarningsPane == JOptionPane.YES_OPTION) {
					ignoreLevelWarnings = true;
					buildRoom(roomKey, roomType, level);
		        } 
				else if (ignoreLevelWarningsPane == JOptionPane.NO_OPTION) {
					if(!loadingHouse){
						roomBuildingFrame.setVisible(true);
						viewingRoomBuildFrame = true;
					}
		        }
				else if (ignoreLevelWarningsPane == JOptionPane.CANCEL_OPTION) {
					viewStatsFrame.setVisible(true);
		        }
			}
		}
	}
	/**
	 * Demolishes a room.
	 */
	public void demolishRoom(){
		//show a confirmation dialogue
		Object[] deleteRoomOptions = {"Demolish " + houseHash.get(overviewSelectedRoom).getRoomType(),
        "Cancel"};
		int demolitionPane = JOptionPane.showOptionDialog(rightDeleteRoomConfirmFrame,
		"Are you sure you want to demolish the "+houseHash.get(overviewSelectedRoom).getRoomType()+
			"?\nYou will lose any furniture built within.",
		"Confirm Room Demolition",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.WARNING_MESSAGE,
		null,     //do not use a custom Icon
		deleteRoomOptions,  //the titles of buttons
		deleteRoomOptions[1]); //default button title

		//if you want to delete, delete the room
		if (demolitionPane == JOptionPane.YES_OPTION) {

			//you can only have one costume room
			if(houseHash.get(overviewSelectedRoom).getRoomType().equals("Costume Room")){
				costumeRoomBuilt = false;
			}
			
			updateRightHouseOverviewPanel(false, true, overviewSelectedRoom);
			calculateSharingCode();
			if(overviewViewingLevel == 1){
				buildRoom(overviewSelectedRoom, "EmptyFloor1", 1);
			} else if(overviewViewingLevel == 0) {
				buildRoom(overviewSelectedRoom, "EmptyDungeon", 0);
			} else if(overviewViewingLevel == 2) {
				buildRoom(overviewSelectedRoom, "EmptyUpstairs", 2);
			}
	        refreshGrid();
	        createAndShowGrid(overviewViewingLevel);
	        resetRightRoomInfoPanelContent();
	        overviewSelectedRoom = "";
	        determineMostDistantRoom();
			
        } //if not, don't delete the room
		else if (demolitionPane == JOptionPane.NO_OPTION) {

        }
	}
	/**
	 * Removes furniture
	 * @param roomKey The key used to access data in houseHash
	 * @param hotspotID The ID of the Hotspot within the room
	 */
	public void removeFurni(String roomKey, int hotspotID){

		updateRightHouseOverviewPanel(false, false, viewingRoom);
		
		//delete anything that may be in the selected spot
		houseHash.get( roomKey ).getHotspots()[hotspotID].setBuiltFurniture(new Furniture(furniHash.get("None")));
		//hide build panel
		furniBuildingFrame.setVisible(false);
		viewingFurniBuildFrame = false;
		
		refreshRoomPanel();
		
		//update stats about the room in the room panel
		setRoomInfoPanelContent();
		calculateSharingCode();

	}
	/**
	 * Builds furniture
	 * @param roomKey The key used to access data in houseHash
	 * @param hotspotID The ID of the Hotspot within the room
	 * @param panelID The ID of the Panel that was clicked on
	 */
	public void buildFurni(String roomKey, int hotspotID, int panelID){
		//check that you have the level to actually build it
		if(furniBuildHash.get( panelID ).getFurni().getLevel() <= Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())
				|| furniBuildHash.get( panelID ).getFurni().getLevel() < 2
					|| ignoreLevelWarnings){
			
			//remove anything already in the spot
			removeFurni(roomKey, hotspotID);
			
			//build the new things
			houseHash.get( roomKey ).getHotspots()[hotspotID].
			setBuiltFurniture(new Furniture(furniHash.get(furniBuildHash.get( panelID ).getFurni().getFurniType())));

			//hide build panel
			furniBuildingFrame.setVisible(false);
			viewingFurniBuildFrame = false;
			
			refreshRoomPanel();
			
			updateRightHouseOverviewPanel(true, false, viewingRoom);
			//update stats about the room in the room panel
			setRoomInfoPanelContent();
			calculateSharingCode();
		} else {
			//show a confirmation dialogue
			Object[] confirmFurniBuildOptions = {"Yes",
	        "No", "Edit my Stats"};
			int ignoreLevelWarningsPane = JOptionPane.showOptionDialog(ignoreLevelWarningsConfirmFrame,
			"Your Construction level is not high enough to build a " +
			furniBuildHash.get( panelID ).getFurni().getFurniType() + ".\n" +
			"Do you want to ignore level requirements and build it anyway?",
			"Confirm Furniture Construction",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,     //do not use a custom Icon
			confirmFurniBuildOptions,  //the titles of buttons
			confirmFurniBuildOptions[1]); //default button title
			
			//build it anyway
			if (ignoreLevelWarningsPane == JOptionPane.YES_OPTION) {
				ignoreLevelWarnings = true;
				buildFurni(roomKey, hotspotID, panelID);
	        }
			else if (ignoreLevelWarningsPane == JOptionPane.NO_OPTION) {
				furniBuildingFrame.setVisible(true);
				viewingFurniBuildFrame = true;
	        }
			else if (ignoreLevelWarningsPane == JOptionPane.CANCEL_OPTION) {
				viewStatsFrame.setVisible(true);
				//viewingFurniBuildFrame = true;
	        }
		}
	}

	/**
     * Adds info to the tabbed pane with stats and such when in Room View
     * 
     * @param roomKey The key used to access data in houseHash
     */
	public void workOutRoomInfoContent(String roomKey) {
		
		//roomInfoPanel.removeAll();
		roomInfoBuildFurniScrollPanel.removeAll();
		roomInfoBuildFurniScrollPanel.setLayout(new GridLayout(houseHash.get(roomKey).getFurniSpots(), 1, 0, 0));

		//add hotspot buttons
		Hotspot[] hotspotArray = houseHash.get(roomKey).getHotspots();
		for(int i = 0; i < hotspotArray.length; i++){
			//will probably need to add something here for Hotspots that exist in
			//multiple places eg. Curtains
			furniButtonHash.get(i).getBuildButton().setText("Build Furniture: "+hotspotArray[i].getHotspotType());
			//add the buttons - all were removed further up
			roomInfoBuildFurniScrollPanel.add(furniButtonHash.get(i).getBuildButton());
		}
		
		setRoomInfoPanelContent();
	}
	/**
	 * Calculates and shows the info about a room when you're viewing it.
	 * (the stats, not the Furni Build Buttons)
	 */
	public void setRoomInfoPanelContent(){
		roomInfoOverviewPanel.removeAll();
		//set room name label text
		roomInfoRoomTypeLabel.setText(houseHash.get(viewingRoom).getRoomType() + " Stats");
	
		//work out the rest of the labels
		roomInfoLevelLabel.setText("Required level: " + Integer.toString(houseHash.get(viewingRoom).getRoomLevel()));
		roomInfoRoomCostLabel.setText("Room Cost: " + Integer.toString(houseHash.get(viewingRoom).getRoomCost()) + "gp");
		roomInfoFurniCostLabel.setText("Furniture Cost: " + calculateRoomFurniCost(viewingRoom) + "gp");
		roomInfoFurniXPLabel.setText("Furniture XP: " + calculateRoomFurniXP(viewingRoom) + "xp");

		//add room name label
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.ipady = 20;
		//add the rest of the labels
		roomInfoOverviewPanel.add(roomInfoRoomTypeLabel, c);
		c.ipady = 0;
		c.gridy = 1;
		roomInfoOverviewPanel.add(roomInfoLevelLabel, c);
		c.gridy = 2;
		roomInfoOverviewPanel.add(roomInfoRoomCostLabel, c);
		c.gridy = 3;
		roomInfoOverviewPanel.add(roomInfoFurniCostLabel, c);
		c.gridy = 4;
		roomInfoOverviewPanel.add(roomInfoFurniXPLabel, c);
		c.gridy = 5;
		c.weighty = 1.0;
		roomInfoOverviewPanel.add(roomInfoFurniNumberLabel, c);
		
		c.weighty = 0.0;
		//30px left padding
		c.insets = leftSpaceInsets;
		c.gridx = 1;
		c.gridy = 0;
		roomInfoOverviewPanel.add(roomInfoHotspotTitleLabel, c);

		//work out hotspot content labels and add them
		currentRoomFurniNumber = 0;
		for(int i = 0; i < houseHash.get(viewingRoom).getHotspots().length; i++){
			//work out what it should say
			furniStatusHash.get( i ).getStatusLabel().setText(
					houseHash.get(viewingRoom).getHotspots()[i].getHotspotType() + ": " +
					houseHash.get(viewingRoom).getHotspots()[i].getBuiltFurniture().getFurniType());
			//set layout stuff
			c.gridy = (i+1);
			if(i == (houseHash.get(viewingRoom).getHotspots().length - 1)){
				c.weighty = 1.0;
			}
			//add the labels
			roomInfoOverviewPanel.add(furniStatusHash.get( i ).getStatusLabel(), c);
			//if there's furniture here in a spot, say so
			if(houseHash.get(viewingRoom).getHotspots()[i].getBuiltFurniture().getFurniType() != "None"){
				currentRoomFurniNumber++;
			}
		}
		//set text now you know the number of pieces of furniture
		roomInfoFurniNumberLabel.setText("Pieces of Furniture: " + currentRoomFurniNumber);
	}
	
	/**
	 * Calculates the number of rooms in the house.
	 * @return Array with various pieces of data in.
	 * 0 = number of rooms.
	 * 1 = cost of rooms.
	 * 2 = cost of furniture.
	 * 3 = experience gained by building furniture.
	 */
	public int[] calculateNumberOfRoomsAndFurniAndStats(){
		
		int[] roomsAndCost = new int[]{0, 0, 0, 0};
		String hashKey = "";
		
		//loops through rooms
		for(int i = 0;i < 3;i++){
			for(int j = 0;j < 9; j++){
				for(int k = 0;k < 9; k++){
					//set key
					hashKey = "overviewRoom" + Integer.toString(i) + Integer.toString(j) + Integer.toString(k);

					if(houseHash.get( hashKey ).getRoomType().equals("")
							||houseHash.get( hashKey ).getRoomType().equals(" ")
								||houseHash.get( hashKey ).getRoomType().equals("  ")){
						continue;
					}
					else {
						//increases number of rooms
						roomsAndCost[0]++;
						//increases room cost
						roomsAndCost[1] += houseHash.get( hashKey ).getRoomCost();
						//increase furniture cost
						roomsAndCost[2] += calculateRoomFurniCost(hashKey);
						//increase furniture experience
						roomsAndCost[3] += calculateRoomFurniXP(hashKey);
					}
				}
			}
		}
		
		return roomsAndCost;
	}
	/**
	 * Calculates the cost of a piece of Furniture based on GEMidPrices.
	 * @param furni A furniture object.
	 * @return cost (int) The cost of the furniture based on GE prices.
	 */
	public int calculateSingleFurniCost(Furniture furni){
		int cost = 0;
		int setGEprice = 0;
		int shopPrice = 0;
		int lowerPrice = 0;

		for(int j = 0; j < furni.getMaterials().length; j++){
			setGEprice = furni.getMaterials()[j].getGeMidPrice();
			shopPrice = furni.getMaterials()[j].getShopPrice();
			lowerPrice = setGEprice < shopPrice || shopPrice == 0 ? setGEprice : shopPrice;
			
			cost += (lowerPrice * furni.getMaterialNumbers()[j]);
		}
		
		return cost;
	}
	/**
	 * Calculates the xp a piece of Furniture will give based on the materials used.
	 * @param furni A furniture object.
	 * @return xp (int) The xp gained by building the furniture.
	 */
	public int calculateSingleFurniXP(Furniture furni){
		int xp = 0;
		
		for(int j = 0; j < furni.getMaterials().length; j++){
			xp += (furni.getMaterials()[j].getExperience() * furni.getMaterialNumbers()[j]);
		}
		
		return xp;
	}
	/**
	 * Calculates the xp a whole room will give based on the Furniture within
	 * and materials used to make that furniture.
	 * @param roomKey The key used to access data in houseHash
	 * @return xp (int) The xp gained by building the furniture in a room.
	 */
	public int calculateRoomFurniXP(String roomKey){
		int xp = 0;
		//get hotspot status
		Hotspot[] room = new Hotspot[houseHash.get(roomKey).getHotspots().length];
		room = houseHash.get(roomKey).getHotspots();
		Furniture furni;
		
		//loop through hotspots in the room
		for(int i = 0; i < room.length; i++){
			//get built furniture
			furni = room[i].getBuiltFurniture();

			xp += calculateSingleFurniXP(furni);
		}
		
		return xp;
	}
	/**
	 * Calculates the cost of the Furniture within a room based on GEMidPrices.
	 * @param roomKey The key used to access data in houseHash
	 * @return cost (int) The cost of the furniture within a room based on GE prices.
	 */
	public int calculateRoomFurniCost(String roomKey){
		int cost = 0;
		//get hotspot status
		Hotspot[] room = new Hotspot[houseHash.get(roomKey).getHotspots().length];
		room = houseHash.get(roomKey).getHotspots();
		Furniture furni;
		
		//loop through hotspots in the room
		for(int i = 0; i < room.length; i++){
			//get built furniture
			furni = room[i].getBuiltFurniture();

			cost += calculateSingleFurniCost(furni);
		}
		
		return cost;
		/*for(int j = 0; j < hotspot.getFurniTypes()[i].getMaterials().length; j++){
			furniCost += (hotspot.getFurniTypes()[i].getMaterials()[j].getGeMidPrice()
					* hotspot.getFurniTypes()[i].getMaterialNumbers()[j]);
			
			furniXP += (hotspot.getFurniTypes()[i].getMaterials()[j].getExperience()
					* hotspot.getFurniTypes()[i].getMaterialNumbers()[j]);
		}*/
	}
	
	/**
	 * Calculates the sharing code for the house and updates the
	 * big text box at the top with it
	 */
	public void calculateSharingCode(){
		//if(!initialising){ //stops it being run twice when initialising //reverted to old code, so commented out
			String hashKey = "";
			String code = "";
			
			code += ("001,");
			
			//loops through rooms
			for(int i = 0;i < 3;i++){
				for(int j = 0;j < 9; j++){
					for(int k = 0;k < 9; k++){
						//set key
						hashKey = "overviewRoom" + Integer.toString(i) + Integer.toString(j) + Integer.toString(k);
	
						//if there's no room, we don't care
						if(houseHash.get( hashKey ).getRoomType().equals("")
								||houseHash.get( hashKey ).getRoomType().equals(" ")
									||houseHash.get( hashKey ).getRoomType().equals("  ")){
							continue;
						}
						else {
							//add the ID of the room
							code += (Integer.toString(i) + Integer.toString(j) + Integer.toString(k));
							
							//add the type of room
							code += houseHash.get(hashKey).getRoomID();
							
							//add the door layout
							for(int m = 0; m < 4; m++){
								if(houseHash.get(hashKey).getDoorLayout()[m] == true){
									code += "t";
								} else {
									code += "f";
								}
							}
							
							//loop through hotspots in the room and add the active furniture
							for(int l = 0; l < houseHash.get( hashKey ).getHotspots().length; l++){
								code += houseHash.get(hashKey).getHotspots()[l].getBuiltFurniture().getFurniID();
							}
							
							//add a separator to indicate the start of the next room
							code += ",";
						}
					}
				}
			}
			
			menuHouseCodeTextbox.setText(code);
		//}
	}
	/**
	 * Loads a house from the sharing code
	 */
	public void loadHouseFromSharingCode(){

		String code = menuHouseCodeTextbox.getText();
        String[] codePieces = code.split( "," );
        String hashKey = "";
        String roomID = "";
        String furniID = "";
        int floor = 0;
        boolean[] doors = new boolean[4];
        boolean load = true;
        int roomsToLoad = codePieces.length;
        
        loadingHouse = true;

        /*for(int i = 0; i < codePieces.length; i++){
        	System.out.println("code: " + codePieces[i]);
        }*/

        //check that there are not more rooms than the max limit
        if((codePieces.length-1) > getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()))){        
        	//show a confirmation dialogue
			Object[] confirmHouseLoadOptions = {"Load the House anyway",
	        "Don't Load",
	        "Just load the allowed "+getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()))+" Rooms"};
			int ignoreLevelWarningsPane = JOptionPane.showOptionDialog(ignoreLevelWarningsConfirmFrame,
			"This house design contains more than the " + 
			getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())) +
			" rooms that your level " +	statsPanelHash.get(22).getLevelBox().getText() +
			" Construction\nallows and you will not be able to add any new rooms without changing your specified\nConstruction level.",
			"Confirm House Load",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,     //do not use a custom Icon
			confirmHouseLoadOptions,  //the titles of buttons
			confirmHouseLoadOptions[1]); //default button title
			
			if (ignoreLevelWarningsPane == JOptionPane.YES_OPTION) {
				//load the house anyway
				loadAllRooms = true;
	        }
			else if (ignoreLevelWarningsPane == JOptionPane.NO_OPTION) {
				//don't load it
				load = false;
	        }
			else if (ignoreLevelWarningsPane == JOptionPane.CANCEL_OPTION) {
				//just load as many rooms as the limit allows
				roomsToLoad = 1+getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()));
				//System.out.println("toload: " + roomsToLoad);
	        }

        }
        if(load){
            
            //reset house so it's nice and empty
            setEmptyRoomArray();
            
            //set username
            //setUsername(codePieces[1]);
            
	        //go through the rest of the pieces and load the rooms
	        for(int i = 1; i < roomsToLoad; i++){
	        	initRightHouseOverviewPanel();
	        	//work out the room location
	        	hashKey = "overviewRoom" + codePieces[i].substring(0, 3);
	        	floor = Integer.parseInt(codePieces[i].substring(0, 1));
	        	
	        	//work out the room type
	        	roomID = codePieces[i].substring(3, 5);
	        	
	        	//System.out.println("roomtype: " + roomType + " floor: " + floor + " location: " + hashKey);
	        	
	        	//build a new room of specified type in specified spot
	        	//houseLoadHash.put(hashKey, new Room(roomType, floor));
	        	//System.out.println("key: " + hashKey + " type: " + roomType + " floor: " + floor);
	        	buildRoom(hashKey, roomIDHash.get(roomID), floor);
	        	
	        	//make sure the doors are correct
	        	//add the door layout
				for(int m = 0; m < 4; m++){
					//System.out.println("door " + m + ": " + codePieces[i].substring(5+m,6+m));
					if(codePieces[i].substring(5+m,6+m).equals("t")){
						doors[m] = true;
					} else {
						doors[m] = false;
					}
				}
				//for(int q = 0; q < 4; q++){
				//	System.out.println("loc: " + hashKey + " q: " + q + " d: " + doors[q]);
				//}
				//System.out.println("doors: " + doors);
				houseHash.get( hashKey ).setDoorLayout(doors);
	        	houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
				houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText(houseHash.get( hashKey ).getRoomType());
			
	        	//loop through the furniture and build it
	        	for(int j = 0; j < houseHash.get(hashKey).getHotspots().length; j++){
	        		//work out the furni type
	        		furniID = codePieces[i].substring((9+(j*2)), (11+(j*2)));
	        		
	        		//System.out.println("furnitype: " + furniType);
	        		
	        		//build that type of furni
	        		houseHash.get(hashKey).getHotspots()[j].setBuiltFurniture(new Furniture(furniHash.get(furniIDHash.get(furniID))));
	        	}
	            
	        }
	        initRightHouseOverviewPanel();
            createAndShowGrid(overviewViewingLevel);
            refreshGrid();
            
	        //update sharing code if you only loaded some of the rooms
	        if(roomsToLoad != codePieces.length){
	        	calculateSharingCode();
	        }
        }
        loadingHouse = false;
        roomsToLoad = 0;
        loadAllRooms = false;
    }
	
	/**
	 * Returns a value from 20-30 which is the max number of rooms that
	 * you can build with your Construction level.
	 * @param level The Construction level you want a limit for
	 * @return The number of rooms that can be built at that level
	 */
	public int getRoomNumberLimit(int level){
		int limit=level<38 ? 20: level>=99 ? 32: level>95 ? 31: (int)Math.floor(20+((level-32)/6));
		return limit;
	}
	/**
	 * Returns a value from 3-8 which is the max dimension of house
	 * you can build with your Construction level.
	 * @param level The Construction level you want a limit for
	 * @return The dimension (YxY rooms)
	 */
	public int getRoomDimensionLimit(int level){
		int limit=level<15? 3 : level>=75 ? 8 : (int)Math.floor(3+(level/15));
		return limit;
	}
	/**
	 * Lets you determine whether it's possible to build a room
	 * in a specified location or whether it'd make the house
	 * larger than the dimension limits.
	 * @param roomKey The key used to access data in houseHash
	 * @return Whether it's possible to build the room in the specified location
	 */
	public boolean checkMostDistantRoom(String roomKey){
		boolean possible = false;
		int vertical = Integer.parseInt(roomKey.substring(13,14));
		int horizontal = Integer.parseInt(roomKey.substring(14,15));

		if(horizontal <= mostDistantRoom[1] && horizontal >= mostDistantRoom[3]
		   && vertical >= mostDistantRoom[0] && vertical <= mostDistantRoom[2]
		   && mostDistantRoom[2] - mostDistantRoom[0] <= roomDimensionLimit
		   && mostDistantRoom[1] - mostDistantRoom[3] <= roomDimensionLimit){
			//within the previous area
			possible = true;
		} //check directions
		else if(mostDistantRoom[0] + roomDimensionLimit > vertical //south
				&& mostDistantRoom[2] - roomDimensionLimit < vertical //north
				&& mostDistantRoom[1] - roomDimensionLimit < horizontal //west
				&& mostDistantRoom[3] + roomDimensionLimit > horizontal //east
				){
				possible = true;
		} //if no rooms built yet
		else if(rightOverviewLabelNumbers[0] == 0){
			possible = true;
		}
		
		return possible;
	}
	/**
	 * Sets where the furthest room in each direction is.
	 * @param roomKey The key used to access data in houseHash
	 */
	public void setMostDistantRoom(String roomKey){
		int vertical = Integer.parseInt(roomKey.substring(13,14));
		int horizontal = Integer.parseInt(roomKey.substring(14,15));

		if(horizontal >= mostDistantRoom[1] || mostDistantRoom[1] == 10){
			mostDistantRoom[1] = horizontal;
		}
		if(horizontal <= mostDistantRoom[3] || mostDistantRoom[3] == 10){
			mostDistantRoom[3] = horizontal;
		}
		if(vertical <= mostDistantRoom[0] || mostDistantRoom[0] == 10){
			mostDistantRoom[0] = vertical;
		}
		if(vertical >= mostDistantRoom[2] || mostDistantRoom[2] == 10){
			mostDistantRoom[2] = vertical;
		}

		/*if(horizontal <= mostDistantRoom[1] && horizontal >= mostDistantRoom[3]
		   && vertical >= mostDistantRoom[0] && vertical <= mostDistantRoom[2]){
			System.out.println("in area v2");
		}*/

	}
	/**
	 * Works out where the most distant room is when rooms are demolished.
	 */
	public void determineMostDistantRoom(){
		String hashKey;
		
		//reset mostDistantRooms
		for(int i = 0; i < 4; i++){
			mostDistantRoom[i] = 10;
		}
		
		//work out where they are
		for(int i = 0;i < 3;i++){
			for(int j = 0;j < 9; j++){
				for(int k = 0;k < 9; k++){
					hashKey = "overviewRoom" + Integer.toString(i) + Integer.toString(j) + Integer.toString(k);
					
					if(houseHash.get(hashKey).getRoomType() != ""
						&& houseHash.get(hashKey).getRoomType() != " "
							&& houseHash.get(hashKey).getRoomType() != "  "){
						//north
						if(mostDistantRoom[0] == 10
								|| j < mostDistantRoom[0]){
							mostDistantRoom[0] = j;
						}
						//east
						if(mostDistantRoom[1] == 10
								|| k > mostDistantRoom[1]){
							mostDistantRoom[1] = k;
						}
						//south
						if(mostDistantRoom[2] == 10
								|| j > mostDistantRoom[2]){
							mostDistantRoom[2] = j;
						}
						//west
						if(mostDistantRoom[3] == 10
								|| k < mostDistantRoom[3]){
							mostDistantRoom[3] = k;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Moves the whole house 1 room north.
	 */
	public void moveHouseNorth(){
		boolean move = true;
		String hashKey = "";
		String hashKeySource = "";
		
		//check that the top row is empty
		houseloop:
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				hashKey = "overviewRoom" + i + "0" + j;
				
				//if there's something built in the space
				if(houseHash.get(hashKey).getRoomType() != ""
					&& houseHash.get(hashKey).getRoomType() != " "
						&& houseHash.get(hashKey).getRoomType() != "  "){
					move = false;
					
					JOptionPane.showMessageDialog( null,
							"There is a room built in the northern row of the house which would"
							+ "\ndisappear if you move the house. Please demolish everything in"
							+ "\nthe northern row of the house before attempting to move it again.",
							"Cannot Move House", 
				            JOptionPane.ERROR_MESSAGE );
					break houseloop; //only one error message
				}
			}
		}
		
		//move the house 1 room north
		if(move){
			if(overviewSelectedRoom != ""){
				//unselect room
				houseHash.get(overviewSelectedRoom).setIsSelected(false);
				resetRightRoomInfoPanelContent();
			}
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 8; j++){
					for(int k = 0; k < 9; k++){
						hashKey = "overviewRoom" + i + j + k;
						hashKeySource = "overviewRoom" + i + (j+1) + k;

						houseHash.put( hashKey , houseHash.get( hashKeySource ));
					}
				}
			}
			
			//add a new blank row at the bottom
			for(int i = 0; i < 3; i++){
				for(int k = 0; k < 9; k++){
					hashKey = "overviewRoom" + i + "8" + k;
					
					if(i == 1){
						houseHash.put( hashKey , new Room(roomHash.get("EmptyFloor1"), i));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 0) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyDungeon"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 2) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyUpstairs"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
					}
				}
			}
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		}
	}
	/**
	 * Moves the whole house 1 room south.
	 */
	public void moveHouseSouth(){
		boolean move = true;
		String hashKey = "";
		String hashKeySource = "";
		
		//check that the bottom row is empty
		houseloop:
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				hashKey = "overviewRoom" + i + "8" + j;
				
				//if there's something built in the space
				if(houseHash.get(hashKey).getRoomType() != ""
					&& houseHash.get(hashKey).getRoomType() != " "
						&& houseHash.get(hashKey).getRoomType() != "  "){
					move = false;
					
					JOptionPane.showMessageDialog( null,
							"There is a room built in the southern row of the house which would"
							+ "\ndisappear if you move the house. Please demolish everything in"
							+ "\nthe southern row of the house before attempting to move it again.",
							"Cannot Move House", 
				            JOptionPane.ERROR_MESSAGE );
					break houseloop; //only one error message
				}
			}
		}
		
		//move the house 1 room south
		if(move){
			if(overviewSelectedRoom != ""){
				//unselect room
				houseHash.get(overviewSelectedRoom).setIsSelected(false);
				resetRightRoomInfoPanelContent();
			}
			for(int i = 0; i < 3; i++){
				for(int j = 8; j > 0; j--){
					for(int k = 0; k < 9; k++){
						hashKey = "overviewRoom" + i + j + k;
						hashKeySource = "overviewRoom" + i + (j-1) + k;

						houseHash.put( hashKey , houseHash.get( hashKeySource ));

					}
				}
			}
			
			//add a new blank row at the top
			for(int i = 0; i < 3; i++){
				for(int k = 0; k < 9; k++){
					hashKey = "overviewRoom" + i + "0" + k;
					
					if(i == 1){
						houseHash.put( hashKey , new Room(roomHash.get("EmptyFloor1"), i));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 0) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyDungeon"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 2) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyUpstairs"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
					}
				}
			}
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		}
	}
	/**
	 * Moves the whole house 1 room east.
	 */
	public void moveHouseEast(){
		boolean move = true;
		String hashKey = "";
		String hashKeySource = "";
		
		//check that the right row is empty
		houseloop:
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				hashKey = "overviewRoom" + i + j + "8";
				
				//if there's something built in the space
				if(houseHash.get(hashKey).getRoomType() != ""
					&& houseHash.get(hashKey).getRoomType() != " "
						&& houseHash.get(hashKey).getRoomType() != "  "){
					move = false;
					
					JOptionPane.showMessageDialog( null,
							"There is a room built in the eastern row of the house which would"
							+ "\ndisappear if you move the house. Please demolish everything in"
							+ "\nthe eastern row of the house before attempting to move it again.",
							"Cannot Move House", 
				            JOptionPane.ERROR_MESSAGE );
					break houseloop; //only one error message
				}
			}
		}
		
		//move the house 1 room east
		if(move){
			if(overviewSelectedRoom != ""){
				//unselect room
				houseHash.get(overviewSelectedRoom).setIsSelected(false);
				resetRightRoomInfoPanelContent();
			}
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 9; j++){
					for(int k = 8; k > 0; k--){
						hashKey = "overviewRoom" + i + j + k;
						hashKeySource = "overviewRoom" + i + j + (k-1);

						houseHash.put( hashKey , houseHash.get( hashKeySource ));

					}
				}
			}
			
			//add a new blank row on the left
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 9; j++){
					hashKey = "overviewRoom" + i + j + "0";
					
					if(i == 1){
						houseHash.put( hashKey , new Room(roomHash.get("EmptyFloor1"), i));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 0) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyDungeon"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 2) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyUpstairs"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
					}
				}
			}
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		}
	}
	/**
	 * Moves the whole house 1 room west.
	 */
	public void moveHouseWest(){
		boolean move = true;
		String hashKey = "";
		String hashKeySource = "";
		
		//check that the left row is empty
		houseloop:
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				hashKey = "overviewRoom" + i + j + "0";
				
				//if there's something built in the space
				if(houseHash.get(hashKey).getRoomType() != ""
					&& houseHash.get(hashKey).getRoomType() != " "
						&& houseHash.get(hashKey).getRoomType() != "  "){
					move = false;
					
					JOptionPane.showMessageDialog( null,
							"There is a room built in the western row of the house which would"
							+ "\ndisappear if you move the house. Please demolish everything in"
							+ "\nthe western row of the house before attempting to move it again.",
							"Cannot Move House", 
				            JOptionPane.ERROR_MESSAGE );
					break houseloop; //only one error message
				}
			}
		}
		
		//move the house 1 room west
		if(move){
			if(overviewSelectedRoom != ""){
				//unselect room
				houseHash.get(overviewSelectedRoom).setIsSelected(false);
				resetRightRoomInfoPanelContent();
			}
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 9; j++){
					for(int k = 0; k < 8; k++){
						hashKey = "overviewRoom" + i + j + k;
						hashKeySource = "overviewRoom" + i + j + (k+1);
						
						houseHash.put( hashKey , houseHash.get( hashKeySource ));

					}
				}
			}
			
			//add a new blank row on the right
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 9; j++){
					hashKey = "overviewRoom" + i + j + "8";
						
					if(i == 1){
						houseHash.put( hashKey , new Room(roomHash.get("EmptyFloor1"), i));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 0) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyDungeon"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 2) {
						houseHash.put( hashKey , new Room(roomHash.get("EmptyUpstairs"), 0));
						houseHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						houseHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
					}
				}
			}
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		}
	}
	/**
	 * Rotates the house 90 degrees clockwise.
	 * Why the heck do only half the things show up? >.<
	 */
	private void rotateHouseClockwise(){
		houseLoadHash = houseHash;
		String hashKey;
		String hashKeySource;
		int y2;
		int x2;
		int count = 0;
		
		for(int i = 0; i < 3; i++){
			for(int y = 0; y < 9; y++){
				for(int x = 0; x < 9; x++){
					hashKeySource = "overviewRoom" + i + y + x;
					//http://homepages.inf.ed.ac.uk/rbf/HIPR2/rotate.htm
					x2 = 0 - (y - 4) + 4;
					y2 = (x - 4) + 4;
					hashKey = "overviewRoom" + i + y2 + x2;

					System.out.println("from: " + hashKeySource + " to: " + hashKey + " " + houseHash.get(hashKeySource).getRoomID() + " " + houseHash.get(hashKey).getRoomID());
					count++;
					houseLoadHash.put( hashKey , houseHash.get( hashKeySource ));
				}
			}
		}
		System.out.println("count: " + count);
		for(int i = 0; i < 3; i++){
			for(int y = 0; y < 9; y++){
				for(int x = 0; x < 9; x++){
					hashKey = "overviewRoom" + i + y + x;

					System.out.println("Key: " + hashKey + " type: " + houseHash.get( hashKey).getRoomID() + " " + houseLoadHash.get( hashKey).getRoomID());
				}
			}
		}
		houseHash = houseLoadHash;
		createAndShowGrid(overviewViewingLevel);
		refreshGrid();
		
	}
	/**
	 * Rotates the house 90 degrees anti-clockwise.
	 * EMPTY - DOES NOTHING
	 */
	private void rotateHouseAntiClockwise(){
		
	}
	
	/**
	 * Sets the username and updates the textBox with it in
	 * @param newUsername The new username to set it to
	 */
	public void setUsername(String newUsername){
		username = newUsername;
		rightGeneralToolsUsernameTextbox.setText(username);
	}

	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getItemSelectable();
		int i = 0;
		for(; i < 25; i++){
			if(source == questHash.get(i).getQuestBox()){
				//System.out.println(questHash.get(i).getQuestBox());
				break;
			}
		}
	}
		
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(comboSelectGridLevel)){
			refreshGrid();
			String level = (String) comboSelectGridLevel.getSelectedItem();
			int houseLevel;
			houseLevel = level == comboSelectGridLevelStrings[0] ? 0 : level == comboSelectGridLevelStrings[1] ? 1 : 2;
			
			createAndShowGrid(houseLevel);
		} else if( e.getSource().equals( rightGeneralToolsUpdateUsername ) || e.getSource().equals( rightGeneralToolsUsernameTextbox ) ) {
            if( rightGeneralToolsUsernameTextbox.getText().isEmpty() ) {
                JOptionPane.showMessageDialog( null, "You must enter a username to use " +
                		"the Hiscore Lookup feature!", "Error", JOptionPane.ERROR_MESSAGE );
                rightGeneralToolsUsernameTextbox.requestFocus();
            }
            else {
                username = rightGeneralToolsUsernameTextbox.getText();
                new RuneScapeHiscore();
            }
        } else if(e.getSource().equals(menuHouseCodeTextbox)/* || e.getSource().equals(menuHouseCodeButton)*/){
        	loadHouseFromSharingCode();
        } else if(e.getSource() == moveHouseNorthMenuItem){
			moveHouseNorth();
			determineMostDistantRoom();
		} else if(e.getSource() == moveHouseSouthMenuItem){
			moveHouseSouth();
			determineMostDistantRoom();
		} else if(e.getSource() == moveHouseEastMenuItem){
			moveHouseEast();
			determineMostDistantRoom();
		} else if(e.getSource() == moveHouseWestMenuItem){
			moveHouseWest();
			determineMostDistantRoom();
		} else if(e.getSource() == rotateHouseClockwiseMenuItem){
			rotateHouseClockwise();
			determineMostDistantRoom();
		} else if(e.getSource() == rotateHouseAntiClockwiseMenuItem){
			rotateHouseAntiClockwise();
			determineMostDistantRoom();
		}
	    
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == rightViewGridButton) {
			
			if(viewingGrid)createAndShowRoom(lastViewedRoom);
			else if(!viewingGrid) createAndShowGrid(overviewViewingLevel);
			
		} else if (viewingGrid && e.getSource().toString().contains("57x57")){
			splitArray = null;
			int x;
			int y;
			
			//work out which room has been clicked
			splitArray = e.getSource().toString().split( "," );

			x = (Integer.parseInt(splitArray[1])-4)/57;
			y = (Integer.parseInt(splitArray[2])-4)/57;

			String roomKey = "overviewRoom"+Integer.toString(overviewViewingLevel)+Integer.toString(y)+Integer.toString(x);
			
			goToRoomFromGrid(roomKey);
		} else if(e.getSource().toString().contains("Build Room:")) {

			String roomType = calculatePressedBuildButtonBasedOnName(e.getSource().toString());

			//System.out.println("building: " + roomType);
			//build the selected room
			buildRoom(overviewSelectedRoom, roomType, overviewViewingLevel);

		} /*else if(e.getSource().toString().contains("Build Furniture:")) {
			String furniType = calculatePressedBuildButtonBasedOnName(e.getSource().toString());

			createFurniBuildingFrameContent(furniType);

		}*/ //rotate room
		else if(e.getSource() == rightRoomInfoRotateAntiClockwise){
			rotatingRoom = true;
			houseHash.get( overviewSelectedRoom ).rotateRoomCcw();
			houseHash.get( overviewSelectedRoom ).getRoomOverviewLabel().addMouseListener(this);
			houseHash.get(overviewSelectedRoom).getRoomOverviewLabel().setToolTipText(houseHash.get(overviewSelectedRoom).getRoomType());
			selectRoom(overviewSelectedRoom);
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
			calculateSharingCode();
			rotatingRoom = false;
		} else if(e.getSource() == rightRoomInfoRotateClockwise){
			rotatingRoom = true;
			houseHash.get(overviewSelectedRoom).rotateRoomCw();
			houseHash.get(overviewSelectedRoom).getRoomOverviewLabel().addMouseListener(this);
			houseHash.get(overviewSelectedRoom).getRoomOverviewLabel().setToolTipText(houseHash.get(overviewSelectedRoom).getRoomType());
			selectRoom(overviewSelectedRoom);
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
			calculateSharingCode();
			rotatingRoom = false;
		} else if(e.getSource() == rightRoomInfoDeleteRoom){
			demolishRoom();
		}//this can almost certainly be writen more effiently, but it's late and I can't think :P
		//could possibly be put in a for() loop in else { //Stuff here // }
		else if(e.getSource() == furniBuildHash.get( 0 ).getBuildPanel()){
			removeFurni(viewingRoom, selectedHotspotID);
		} else if(e.getSource() == furniBuildHash.get( 1 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 1);
		} else if(e.getSource() == furniBuildHash.get( 2 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 2);
		} else if(e.getSource() == furniBuildHash.get( 3 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 3);
		} else if(e.getSource() == furniBuildHash.get( 4 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 4);
		} else if(e.getSource() == furniBuildHash.get( 5 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 5);
		} else if(e.getSource() == furniBuildHash.get( 6 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 6);
		} else if(e.getSource() == furniBuildHash.get( 7 ).getBuildPanel()){
			buildFurni(viewingRoom, selectedHotspotID, 7);
		} else if(e.getSource() == viewStatsSelectAllQuestsButton){
			selectAllQuests();
		} else if(e.getSource() == viewStatsDeselectAllQuestsButton){
			deselectAllQuests();
		} else if(e.getSource() == rightGeneralToolsViewStats){
			viewStatsFrame.setVisible(true);
		} else if(e.getSource() == viewStatsSaveAndCloseButton){
			updateStats();
			viewStatsFrame.setVisible(false);
		} else if(e.getSource() == viewStatsSaveButton){
			updateStats();
		} else if(e.getSource() == menuHouseCodeButton){
			loadHouseFromSharingCode();
		} else if(e.getSource() == menuHouseCodeTextbox){
			menuHouseCodeTextbox.selectAll();
		}
		/* User has clicked on the room panel */
		else if( e.getSource().equals( roomPanel ) ) {
		    Polygon area;
			Hotspot[] hotspots = houseHash.get( lastViewedRoom ).getHotspots();
			
			/* Iterate over this room's hotspots */
			outerhotspotloop:
			for( int i = 0; i < hotspots.length; i++ ) {
				//loop through the Polygons that indicate a Hotspot eg. Curtains have up to 8
	        	for(int j = 0; j < hotspots[ i ].getNumberOfPoints().length; j++){
				    /* Create a polygon for the current hotspot */
				    area = new Polygon( hotspots[ i ].getXCoords()[j], hotspots[ i ].getYCoords()[j], hotspots[ i ].getNumberOfPoints()[j] );
				    
				    /* Check if mouse click was within the current hotspot */
				    if( area.contains( e.getX(), e.getY() ) ) {
				        //System.out.println( "RAWR!!!" );
				        /* User has clicked on a valid hotspot -  */
				        selectedHotspotID = i;
				        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[i].getHotspotType());
				        break outerhotspotloop;
				    }
	        	}
			}
		}//looks horribly inefficient and could probably go in a for() loop, but heh 
		else if(e.getSource() == furniButtonHash.get( 0 ).getBuildButton()){
			selectedHotspotID = 0;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[0].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 1 ).getBuildButton()){
			selectedHotspotID = 1;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[1].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 2 ).getBuildButton()){
			selectedHotspotID = 2;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[2].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 3 ).getBuildButton()){
			selectedHotspotID = 3;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[3].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 4 ).getBuildButton()){
			selectedHotspotID = 4;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[4].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 5 ).getBuildButton()){
			selectedHotspotID = 5;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[5].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 6 ).getBuildButton()){
			selectedHotspotID = 6;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[6].getHotspotType());
		} else if(e.getSource() == furniButtonHash.get( 7 ).getBuildButton()){
			selectedHotspotID = 7;
	        createFurniBuildingFrameContent(houseHash.get(lastViewedRoom).getHotspots()[7].getHotspotType());
		} else if(e.getSource() == rightGeneralToolsUpdateGEPrices){
			JOptionPane.showMessageDialog( null,
					"This isn't ready for you to love yet. =(",
					"Be Patient...", 
		            JOptionPane.ERROR_MESSAGE );
		}
		//System.out.println(e.getSource().toString());
		
	}
	
	/**
     * Works out what it is you're trying to build
     * 
     * @param source The source of the button that was clicked
     */
	public String calculatePressedBuildButtonBasedOnName(String source){
		
		String calcItemType;
		String itemType = "";
		splitArray = null;
		int startPosition = 0;
		
		//work out which build button has been clicked
		splitArray = source.split( "," );
		calcItemType = splitArray[splitArray.length - 2];
		
		//works out whether it's a Room or Furniture or something else button
		if(calcItemType.contains("Build Furniture:")){
			startPosition = 22;
		} else if(calcItemType.contains("Build Room:")){
			startPosition = 17;
		}
		
		//would probably work better with a regex, but this works, so heh
		for(; startPosition < calcItemType.length(); startPosition++){
			itemType += calcItemType.charAt(startPosition);
		}
		
		return itemType;
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

	
	/**
     * Performs a hiscore lookup on the specified name.
     * Oiginally written for the Skill Calcs, modified for the POH Planner.
     * @return "ok" on success, otherwise an error message.
     * @author Rick (Salmoneus), Neo Avatars
     */
    private String hiscoreLookup() {
    	//System.out.println("Fetching1");
        BufferedReader in = null;
        URL url = null;
        HttpURLConnection hiscoreHTTP = null;
        String line = null;
        String hiscoreURL = "http://hiscore.runescape.com/index_lite.ws?player=" + username;
        String[] linePieces = null;
        int lineNumber = 0;
        int responseCode = 0;
        //System.out.println("Fetching2");
        try {
            url = new URL( hiscoreURL );
            //System.out.println("Fetching3a");
            hiscoreHTTP = (HttpURLConnection)url.openConnection();
            hiscoreHTTP.setUseCaches( false );
            hiscoreHTTP.setRequestMethod( "GET" );
            //System.out.println("Fetching3b");
            hiscoreHTTP.connect();
            //System.out.println("Fetching3c");
            responseCode = hiscoreHTTP.getResponseCode();
            //System.out.println("Fetching3");
        } catch( IOException e ) { }
        
        if( responseCode == 404 ) {
            return username + " does not appear to be in the RuneScape Hiscores.";
        }
        else if( responseCode != 200 ) {
            return "The calculator was unable to connect to the RuneScape Hiscore server.";
        }
        
        try {
            in = new BufferedReader( new InputStreamReader( url.openStream() ) );
            //System.out.println("Fetching4");
            while( ( line = in.readLine() ) != null ) {
                linePieces = line.split( "," );
                
                if( linePieces.length != 3 ) {
                    continue;
                }
                //System.out.println("Fetching5");
                hiscoreList.add( lineNumber++, new HiscoreData(
                    Integer.parseInt( linePieces[0] ), 
                    Integer.parseInt( linePieces[1] ), 
                    Integer.parseInt( linePieces[2] ),
                    true
                ) );
                if(lineNumber > 1 && lineNumber < 26){
                	statsPanelHash.get(lineNumber-2).getLevelBox().setText(linePieces[1]);
                }
                
            }
            //System.out.println("Fetching6");
            in.close();
            in = null;
            hiscoreHTTP.disconnect();
            hiscoreHTTP = null;
        } catch( IOException e ) { }
        //System.out.println("Fetching7");
        return "ok";
    }

	/**
     * This class is essential for multi-threading to work while performing a 
     * hiscore lookup. Otherwise, the applet will freeze while the hiscore data
     * is retrieved.
     * 
     * Originally written for the Skill Calcs, modified for the POH Planner,
     * with some Easter Eggs added.
     * 
     * @author Rick (Salmoneus), Neo Avatars
     * @version 2.1
     * @date 4 January 2009
     *
     */
    private class RuneScapeHiscore extends Thread {
        
        /**
         * Constructor.
         */
        public RuneScapeHiscore() {
            super( "HiscoreThread" );
            start();
        }
        
        /**
         * Run the hiscore lookup.
         */
        public void run() {
            String result = null;
            
            //nom nom easter eggs
            if(username.length() >= 3 && username.substring(0, 3).equalsIgnoreCase("mod")){
            	//lots of special ones for various special JMods
            	if(username.equalsIgnoreCase("mod mmg")){
            		rightGeneralToolsUsernameTextbox.setText("The Saviour");
            	} else if(username.equalsIgnoreCase("mod maz")){
            		//stupid "Squirrel Queen" being 14 characters long =(
            		rightGeneralToolsUsernameTextbox.setText("Queen of Sq");
            	} else if(username.equalsIgnoreCase("mod hohbein")){
            		rightGeneralToolsUsernameTextbox.setText("Communicator");
            	} else if(username.equalsIgnoreCase("mod luke")){
            		rightGeneralToolsUsernameTextbox.setText("My Son");
            	} else if(username.equalsIgnoreCase("mod mark h")){
            		rightGeneralToolsUsernameTextbox.setText("NOT Mod MMG!");
            	} else if(username.equalsIgnoreCase("mod korpz")){
            		rightGeneralToolsUsernameTextbox.setText("FunOrb King");
            	} else if(username.equalsIgnoreCase("mod sallyd")){
            		rightGeneralToolsUsernameTextbox.setText("The Salad");
            	} else if(username.equalsIgnoreCase("mod craddock")){
            		rightGeneralToolsUsernameTextbox.setText("The Crab");
            	} else if(username.equalsIgnoreCase("mod thomas")){
            		rightGeneralToolsUsernameTextbox.setText("The Arcane");
            	} else if(username.equalsIgnoreCase("mod ajd")){
            		rightGeneralToolsUsernameTextbox.setText("1st Tweeter");
            	} else if(username.equalsIgnoreCase("mod zapp")){
            		rightGeneralToolsUsernameTextbox.setText("2nd Tweeter");
            	} else if(username.equalsIgnoreCase("mod fran")){
            		rightGeneralToolsUsernameTextbox.setText("Frantastic");
            	} else if(username.equalsIgnoreCase("mod tytn")){
            		rightGeneralToolsUsernameTextbox.setText("QuestCrafter");
            	} else if(username.equalsIgnoreCase("mod lorenzo")){
            		rightGeneralToolsUsernameTextbox.setText("Don Lorenzo");
            	} else if(username.equalsIgnoreCase("mod seven")){
            		rightGeneralToolsUsernameTextbox.setText("Se7en");
            	} else if(username.equalsIgnoreCase("mod ash")){
            		rightGeneralToolsUsernameTextbox.setText("Wise Old Man");
            	} else if(username.equalsIgnoreCase("mod gabriel")){
            		rightGeneralToolsUsernameTextbox.setText("Not Sylar");
            	} else if(username.equalsIgnoreCase("mod john a")){
            		rightGeneralToolsUsernameTextbox.setText("Red Axe Plz?");
            	} else if(username.equalsIgnoreCase("mod mark")){
            		rightGeneralToolsUsernameTextbox.setText("LeadDesigner");
            	} else if(username.equalsIgnoreCase("mod newmatic")){
            		rightGeneralToolsUsernameTextbox.setText("Hi Sumona!");
            	} else if(username.equalsIgnoreCase("mod mat k")){
            		rightGeneralToolsUsernameTextbox.setText("Max Power");
            	} else if(username.equalsIgnoreCase("mod chris l")){
            		rightGeneralToolsUsernameTextbox.setText("#1 Choob");
            	} else if(username.equalsIgnoreCase("mod knox")){
            		rightGeneralToolsUsernameTextbox.setText("Coordinator");
            	}//and a generic one for everybody else =P 
            	else {
	            	rightGeneralToolsUsernameTextbox.setText("Welcome! ^_^");
            	}
            	JOptionPane.showMessageDialog( null,
            			"Jagex Moderators do not appear in the Runescape hiscores.",
            			"Oops!",
                        JOptionPane.ERROR_MESSAGE );
            } else if(username.equalsIgnoreCase("andrew")){
            	rightGeneralToolsUsernameTextbox.setText("The Almighty");
            	JOptionPane.showMessageDialog( null,
            			"Andrew makes the Runescape hiscores - of course he isn't in them!",
            			"Oops!",
                        JOptionPane.ERROR_MESSAGE );
            } else if(username.equalsIgnoreCase("paul")){
            	rightGeneralToolsUsernameTextbox.setText("The Brother");
            	JOptionPane.showMessageDialog( null,
            			"Paul, brother of Andrew, does not appear in the Runescape hiscores.",
            			"Oops!",
                        JOptionPane.ERROR_MESSAGE );
            }
            
            //past all the eggs and onto the proper fetching stuff
            else {
	            rightGeneralToolsUpdateUsername.setText( "Fetching Stats" );
	            rightGeneralToolsUpdateUsername.setEnabled( false );
	            result = hiscoreLookup();
	            rightGeneralToolsUpdateUsername.setText( "Fetch My Exp!" );
	            rightGeneralToolsUpdateUsername.setEnabled( true );
	            
	            if( result != "ok" ) {
	                JOptionPane.showMessageDialog( null, result, "An Error Occurred", 
	                        JOptionPane.ERROR_MESSAGE );
	            }
	            else {            
	                //update various things to do with the highscores
	            	//update room limit
	            	rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]
	            	      + "/" + getRoomNumberLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())));
	            	//update dimension limit
	            	roomDimensionLimit = getRoomDimensionLimit(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()));
	            	
	            	//hiscoreList.get(23).getLevel()
	            	//Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText())
	            	//System.out.println(Integer.parseInt(statsPanelHash.get(22).getLevelBox().getText()));
	            	//System.out.println("User: "+username+"Con lvl: "+hiscoreList.get(23);.getLevel());
	            }
            }
        }
    }
    /**
     * Calculates the number of xp required to reach a specific level.
     * 
     * @param level The level to calculate for.
     * @return The number of xp to reach the specified level.
     * @since 2.1
     * @author Rick (Salmoneus)
     */    
    private int calculateXPForLevel( int level ) {
        return ( int ) Math.floor( level + 300 * Math.pow( 2, level / 7.0  ) );
    }

    /**
     * Yes, it's totally pointless, but you can just comment it out
     */
    public void inducePointlessWait(){
    	int j = 0;
		for(int i = 0; i < 3000000; i++){
			if(i%30000 == 0){
				j++;
			}
			loadingLabel.setText("Inducing Pointless Wait . . . " + j + "% Complete");
		}
		j = 0;
		for(int i = 0; i < 1000000; i++){
			if(i%10000 == 0){
				j++;
			}
			loadingLabel.setText("Oh no, here's another . . . " + j + "% Complete");
		}
		j = 0;
		for(int i = 0; i < 2500000; i++){
			if(i%25000 == 0){
				j++;
			}
			loadingLabel.setText("Counting to 100 . . . " + j + " / 100");
		}
		j = 0;
		for(int i = 0; i < 8000000; i++){
			if(i%80000 == 0){
				j++;
			}
			loadingLabel.setText("Testing your patience . . . " + j + "% Complete");
		}
    }
    
    
    /**
     * Initialises the Room ID Hash with the various room IDs so they can be
     * accessed when loading from a Sharing Code
     */
    LinkedHashMap<Object, String> initRoomIDHash(LinkedHashMap<Object, String> hash){
    	
    	hash.put("00", "EmptyFloor1");
    	hash.put("01", "EmptyDungeon");
    	hash.put("02", "AboveUpstairs");
    	hash.put("03", "EmptyUpstairs");
    	hash.put("04", "Garden");
    	hash.put("05", "Parlour");
    	hash.put("06", "Kitchen");
    	hash.put("07", "Dining Room");
    	hash.put("08", "Workshop");
    	hash.put("09", "Bedroom");
    	hash.put("10", "Skill Hall");
    	hash.put("11", "Games Room");
    	hash.put("12", "Combat Room");
    	hash.put("13", "Quest Hall");
    	hash.put("14", "Study");
    	hash.put("15", "Costume Room");
    	hash.put("16", "Chapel");
    	hash.put("17", "Portal Chamber");
    	hash.put("18", "Formal Garden");
    	hash.put("19", "Throne Room");
    	hash.put("20", "Oubliette");
    	hash.put("21", "Dungeon Corridor");
    	hash.put("22", "Dungeon Junction");
    	hash.put("23", "Dungeon Stairs");
    	hash.put("24", "Treasure Room");
    	
    	return hash;
    }
    
    /**
     * Initialises the Room Hash with the different types of room
     */
    LinkedHashMap<Object, Room> initRoomHash(LinkedHashMap<Object, Room> hash,
			Utilities util){
	
    	int numberOfRooms = 25;
    	
		hash.put("EmptyFloor1", new Room(
			/*Type*/ "EmptyFloor1",
			/*labelText*/ "",
			/*Level*/ 1,
			util, /*Cost*/ 0,
			/*doorLayout*/ new boolean[]{false, false, false, false},
			/*Hotspots*/ new Hotspot[0],
			/*roomImageURL*/ "empty",
			/*ID*/ "00"));
		hash.put("EmptyDungeon", new Room(
			/*Type*/ "EmptyDungeon",
			/*labelText*/ "",
			/*Level*/ 1,
			util, /*Cost*/ 0,
			/*doorLayout*/ new boolean[]{false, false, false, false},
			/*Hotspots*/ new Hotspot[0],
			/*roomImageURL*/ "empty",
			/*ID*/ "01"));
		hash.put("AboveUpstairs", new Room(
			/*Type*/ "AboveUpstairs",
			/*labelText*/ "",
			/*Level*/ 1,
			util, /*Cost*/ 0,
			/*doorLayout*/ new boolean[]{false, false, false, false},
			/*Hotspots*/ new Hotspot[0],
			/*roomImageURL*/ "empty",
			/*ID*/ "02"));
		hash.put("EmptyUpstairs", new Room(
			/*Type*/ "EmptyUpstairs",
			/*labelText*/ "",
			/*Level*/ 1,
			util, /*Cost*/ 0,
			/*doorLayout*/ new boolean[]{false, false, false, false},
			/*Hotspots*/ new Hotspot[0],
			/*roomImageURL*/ "empty",
			/*ID*/ "03"));
		hash.put("Garden", new Room(
			/*Type*/ "Garden",
			/*labelText*/ "Garden",
			/*Level*/ 1,
			util, /*Cost*/ 1000,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[] {
			new Hotspot(hotspotHash.get("Centrepiece"), 
					new int[][]{ new int[]{ 268, 278, 278, 268, 268}, }, 
					new int[][]{ new int[]{ 118, 118, 190, 190, 118}, }, 
					new int[]{4} ),
			new Hotspot(hotspotHash.get("Tree"), 
					new int[][]{ new int[]{ 128, 190, 190, 128, 128}, }, 
					new int[][]{ new int[]{ 10, 10, 90 , 90, 10}, }, 
					new int[]{4} ),
			new Hotspot(hotspotHash.get("Big Tree"), 
					new int[][]{ new int[]{ 146, 222, 222, 146, 146}, }, 
					new int[][]{ new int[]{ 168, 168, 262, 262, 168}, }, 
					new int[]{4} ),
			new Hotspot(hotspotHash.get("Small Plant 1"), 
					new int[][]{ new int[]{ 196, 234, 234, 196, 196}, }, 
					new int[][]{ new int[]{ 125, 125, 160, 160, 125}, }, 
					new int[]{4} ),
			new Hotspot(hotspotHash.get("Small Plant 2"), 
					new int[][]{ new int[]{ 352, 392, 392, 352, 352}, }, 
					new int[][]{ new int[]{ 158, 158, 195, 195, 158}, }, 
					new int[]{4} ),
			new Hotspot(hotspotHash.get("Big Plant 1"), 
					new int[][]{ new int[]{ 370, 452, 452, 370, 370}, }, 
					new int[][]{ new int[]{ 238, 238, 316, 316, 238}, }, 
					new int[]{4} ),
			new Hotspot(hotspotHash.get("Big Plant 2"), 
					new int[][]{ new int[]{ 351, 406, 406, 351, 351}, }, 
					new int[][]{ new int[]{ 24, 24, 89, 89, 24}, }, 
					new int[]{4} ),
			},
			/*roomImageURL*/ "1Garden",
			/*ID*/ "04"));
		loadingLabel.setText("Loading . . . Creating Rooms 5/"+numberOfRooms);
		hash.put("Parlour", new Room(
			/*Type*/ "Parlour",
			/*labelText*/ "Parlour",
			/*Level*/ 1,
			util, /*Cost*/ 1000,
			/*doorLayout*/ new boolean[]{false, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			new Hotspot(hotspotHash.get("Chairs"), 
					new int[][]{new int[]{ 182, 210, 215, 198, 182, 182},}, 
					new int[][]{ new int[]{108, 125, 145, 160, 146, 108},}, 
					new int[]{5}),
			new Hotspot(hotspotHash.get("Chairs"), 
					new int[][]{new int[]{ 277, 300, 300, 274, 274, 277},}, 
					new int[][]{ new int[]{149, 149, 186, 186, 164, 149},}, 
					new int[]{5}),
			new Hotspot(hotspotHash.get("Chairs"), 
					new int[][]{new int[]{ 346, 343, 329, 313, 318, 346},}, 
					new int[][]{ new int[]{111, 149, 158, 147, 126, 111},}, 
					new int[]{5}),
			new Hotspot(hotspotHash.get("Rugs"), 
					new int[][]{new int[]{ 202, 325, 338, 189, 202},}, 
					new int[][]{ new int[]{115, 115, 216, 216, 115},}, 
					new int[]{4}),
			new Hotspot(hotspotHash.get("Fireplace"), 
					new int[][]{new int[]{ 231, 295, 295, 231, 231},}, 
					new int[][]{ new int[]{25, 25, 83, 83, 25},}, 
					new int[]{4}),
			new Hotspot(hotspotHash.get("Curtains"),
					new int[][]{ new int[]{ 192, 218, 219, 196, 192},	new int[]{ 310, 336, 333, 310, 310},new int[]{ 435, 443, 432, 424, 435},new int[]{ 472, 484, 468, 457, 472},new int[]{ 344, 377, 374, 343, 344},new int[]{ 153, 185, 186, 156, 153},new int[]{ 57, 73, 62, 47, 57},		new int[]{ 95, 104, 98, 85, 95},},
					new int[][]{ new int[]{ 4, 4, 38, 37, 4}, 			new int[]{ 4, 4, 37, 37, 4}, 		new int[]{ 50, 68, 95, 81, 50},		new int[]{ 144, 170, 194, 176, 144},new int[]{ 287, 287, 301, 300, 287},new int[]{ 285, 285, 299, 298, 285},new int[]{ 141, 176, 193, 168, 141},new int[]{ 49, 80, 98, 68, 49},},
					new int[]{ 4, 4, 4, 4, 4, 4, 4, 4}),
			new Hotspot(hotspotHash.get("Bookcases"),
				 	new int[][]{ new int[]{ 46, 74, 94, 85, 48, 29, 46}, 		new int[]{ 456, 484, 502, 479, 443, 434, 456}, },
				 	new int[][]{ new int[]{ 182, 182, 231, 264, 264, 227, 182}, new int[]{ 186, 186, 232, 267, 267, 232, 186}, },
				 	new int[]{ 6, 6}),
			},
			/*roomImageURL*/ "1Parlour",
			/*ID*/ "05"));
		hash.put("Kitchen", new Room(
			/*Type*/ "Kitchen",
			/*labelText*/ "Kitchen",
			/*Level*/ 5,
			util, /*Cost*/ 5000,
			/*doorLayout*/ new boolean[]{false, true, true, false},
			/*Hotspots*/ new Hotspot[]{
			
			new Hotspot(hotspotHash.get("Kitchen Table"),
			    		new int[][]{ new int[]{ 199, 276, 276, 194, 199}, },
			    		new int[][]{ new int[]{ 82, 82, 157, 157, 82}, },
			    		new int[]{ 4}),
			    new Hotspot(hotspotHash.get("Larder"),
				    	new int[][]{ new int[]{ 382, 386, 372, 317, 314, 317, 382}, },
				    	new int[][]{ new int[]{ 0, 29, 71, 73, 15, 0, 0}, },
				    	new int[]{ 6}),
			    new Hotspot(hotspotHash.get("Sink"),
				 	new int[][]{ new int[]{ 275, 274, 209, 203, 226, 226, 275}, },
				 	new int[][]{ new int[]{ 16, 45, 45, 0, 0, 16, 16}, },
				 	new int[]{ 6}),
			    new Hotspot(hotspotHash.get("Shelf"),
				 	new int[][]{ new int[]{ 98, 113, 108, 92, 98},	new int[]{ 68, 86, 78, 60, 68}, },
				 	new int[][]{ new int[]{ 6, 12, 43, 40, 6},		new int[]{ 170, 173, 214, 214, 170}, },
				 	new int[]{ 4, 4}),
			    new Hotspot(hotspotHash.get("Stove"),
				 	new int[][]{ new int[]{ 95, 131, 131, 95, 95}, },
				 	new int[][]{ new int[]{ 97, 97, 145, 145, 97}, },
				 	new int[]{ 4}),
			    new Hotspot(hotspotHash.get("Barrel"),
				 	new int[][]{ new int[]{ 101, 152, 153, 101, 101}, },
				 	new int[][]{ new int[]{ 213, 213, 245, 245, 213}, },
				 	new int[]{ 4}),
			    new Hotspot(hotspotHash.get("Cat Basket"),
			    		new int[][]{ new int[]{ 351, 389, 389, 351, 351}, },
				 	new int[][]{ new int[]{ 232, 232, 248, 248, 232}, },
				 	new int[]{ 4}),
			
			},
			/*roomImageURL*/ "5Kitchen",
			/*ID*/ "06"));
		hash.put("Dining Room", new Room(
			/*Type*/ "Dining Room",
			/*labelText*/ "<html>Dining<br />Room</html>",
			/*Level*/ 10,
			util, /*Cost*/ 5000,
			/*doorLayout*/ new boolean[]{true, false, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "10DiningRoom",
			/*ID*/ "07"));
		hash.put("Workshop", new Room(
			/*Type*/ "Workshop",
			/*labelText*/ "Workshop",
			/*Level*/ 15,
			util, /*Cost*/ 10000,
			/*doorLayout*/ new boolean[]{false, true, false, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "15Workshop",
			/*ID*/ "08"));
		hash.put("Bedroom", new Room(
			/*Type*/ "Bedroom",
			/*labelText*/ "Bedroom",
			/*Level*/ 20,
			util, /*Cost*/ 10000,
			/*doorLayout*/ new boolean[]{true, true, false, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "20Bedroom",
			/*ID*/ "09"));
		loadingLabel.setText("Loading . . . Creating Rooms 10/"+numberOfRooms);
		hash.put("Skill Hall", new Room(
			/*Type*/ "Skill Hall",
			/*labelText*/ "Skill Hall",
			/*Level*/ 25,
			util, /*Cost*/ 15000,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "25SkillHall",
			/*ID*/ "10"));
		hash.put("Games Room", new Room(
			/*Type*/ "Games Room",
			/*labelText*/ "<html>Games<br />Room</htm>",
			/*Level*/ 30,
			util, /*Cost*/ 25000,
			/*doorLayout*/ new boolean[]{false, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "30GamesRoom",
			/*ID*/ "11"));
		hash.put("Combat Room", new Room(
			/*Type*/ "Combat Room",
			/*labelText*/ "<html>Combat<br />Room</html>",
			/*Level*/ 32,
			util, /*Cost*/ 25000,
			/*doorLayout*/ new boolean[]{false, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "32CombatRoom",
			/*ID*/ "12"));
		hash.put("Quest Hall", new Room(
			/*Type*/ "Quest Hall",
			/*labelText*/ "<html>Quest<br/>Hall</html>",
			/*Level*/ 35,
			util, /*Cost*/ 25000,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "35QuestHall",
			/*ID*/ "13"));
		hash.put("Study", new Room(
			/*Type*/ "Study",
			/*labelText*/ "Study",
			/*Level*/ 40,
			util, /*Cost*/ 50000,
			/*doorLayout*/ new boolean[]{false, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "40Study",
			/*ID*/ "14"));
		loadingLabel.setText("Loading . . . Creating Rooms 15/"+numberOfRooms);
		hash.put("Costume Room", new Room(
			/*Type*/ "Costume Room",
			/*labelText*/ "<html>Costume<br />Room</html>",
			/*Level*/ 42,
			util, /*Cost*/ 50000,
			/*doorLayout*/ new boolean[]{false, true, false, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "40Study",
			/*ID*/ "15"));
		hash.put("Chapel", new Room(
			/*Type*/ "Chapel",
			/*labelText*/ "Chapel",
			/*Level*/ 45,
			util, /*Cost*/ 50000,
			/*doorLayout*/ new boolean[]{true, true, false, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "45Chapel",
			/*ID*/ "16"));
		hash.put("Portal Chamber", new Room(
			/*Type*/ "Portal Chamber",
			/*labelText*/ "<html>Portal<br />Chamber</html>",
			/*Level*/ 50,
			util, /*Cost*/ 100000,
			/*doorLayout*/ new boolean[]{false, true, false, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "50PortalChamber",
			/*ID*/ "17"));
		hash.put("Formal Garden", new Room(
			/*Type*/ "Formal Garden",
			/*labelText*/ "<html>Formal<br />Garden</html>",
			/*Level*/ 55,
			util, /*Cost*/ 75000,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "55FormalGarden",
			/*ID*/ "18"));
		hash.put("Throne Room", new Room(
			/*Type*/ "Throne Room",
			/*labelText*/ "<html>Throne<br />Room</html>",
			/*Level*/ 60,
			util, /*Cost*/ 150000,
			/*doorLayout*/ new boolean[]{false, false, true, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "60ThroneRoom",
			/*ID*/ "19"));
		loadingLabel.setText("Loading . . . Creating Rooms 20/"+numberOfRooms);
		hash.put("Oubliette", new Room(
			/*Type*/ "Oubliette",
			/*labelText*/ "Oubliette",
			/*Level*/ 65,
			util, /*Cost*/ 150000,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "65Oubliette",
			/*ID*/ "20"));
		hash.put("Dungeon Corridor", new Room(
			/*Type*/ "Dungeon Corridor",
			/*labelText*/ "<html>Dungeon<br /><br />Corridor</html>",
			/*Level*/ 70,
			util, /*Cost*/ 7500,
			/*doorLayout*/ new boolean[]{true, false, true, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "70DungeonCorridor",
			/*ID*/ "21"));
		hash.put("Dungeon Junction", new Room(
			/*Type*/ "Dungeon Junction",
			/*labelText*/ "Junction", //<html>Dungeon<br /><br />Junction</html>
			/*Level*/ 70,
			util, /*Cost*/ 7500,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "70DungeonJunction",
			/*ID*/ "22"));
		hash.put("Dungeon Stairs", new Room(
			/*Type*/ "Dungeon Stairs",
			/*labelText*/ "<html>Dungeon<br />Stairs</html>",
			/*Level*/ 70,
			util, /*Cost*/ 7500,
			/*doorLayout*/ new boolean[]{true, true, true, true},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
			},
			/*roomImageURL*/ "70DungeonStairs",
			/*ID*/ "23"));
		hash.put("Treasure Room", new Room(
			/*Type*/ "Treasure Room",
			/*labelText*/ "<html>Treasure<br />Room</html>",
			/*Level*/ 75,
			util, /*Cost*/ 250000,
			/*doorLayout*/ new boolean[]{false, false, true, false},
			/*Hotspots*/ new Hotspot[]{
			
			//	new Hotspot(""),
			
		      },
		      /*roomImageURL*/ "75TreasureRoom",
			/*ID*/ "24"));
		
		return hash;
	}

    /**
     * Initialises the Hotspot Hash with the different types of hotspot
     */
    LinkedHashMap<Object, Hotspot> initHotspotHash(LinkedHashMap<Object, Hotspot> hash){
    	
    	hash.put("Centrepiece", new Hotspot("Centrepiece",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Exit Portal")),
					new Furniture(furniHash.get("Decorative Rock")),
					new Furniture(furniHash.get("Pond")),
					new Furniture(furniHash.get("Imp Statue")),
					new Furniture(furniHash.get("Dungeon Entrance")),
				}));
			hash.put("Tree", new Hotspot("Tree",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Tree")),
					new Furniture(furniHash.get("Nice Tree")),
					new Furniture(furniHash.get("Oak Tree")),
					new Furniture(furniHash.get("Willow Tree")),
					new Furniture(furniHash.get("Maple Tree")),
					new Furniture(furniHash.get("Yew Tree")),
					new Furniture(furniHash.get("Magic Tree")),
				}));
			hash.put("Big Tree", new Hotspot("Big Tree",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Tree")),
					new Furniture(furniHash.get("Nice Tree")),
					new Furniture(furniHash.get("Oak Tree")),
					new Furniture(furniHash.get("Willow Tree")),
					new Furniture(furniHash.get("Maple Tree")),
					new Furniture(furniHash.get("Yew Tree")),
					new Furniture(furniHash.get("Magic Tree")),
				}));
			hash.put("Small Plant 1", new Hotspot("Small Plant 1",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Plant")),
					new Furniture(furniHash.get("Small Fern")),
					new Furniture(furniHash.get("Fern")),
				}));
			hash.put("Small Plant 2", new Hotspot("Small Plant 2",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Dock Leaf")),
					new Furniture(furniHash.get("Thistle")),
					new Furniture(furniHash.get("Reeds")),
				}));
			hash.put("Big Plant 1", new Hotspot("Big Plant 1",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Fern ")), //space stops it getting confused with the other Fern
					new Furniture(furniHash.get("Bush")),
					new Furniture(furniHash.get("Tall Plant")),
				}));
			hash.put("Big Plant 2", new Hotspot("Big Plant 2",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Short Plant")),
					new Furniture(furniHash.get("Large Leaf Bush")),
					new Furniture(furniHash.get("Huge Plant")),
				}));
			hash.put("Chairs", new Hotspot("Chairs",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Crude Wooden Chair")),
					new Furniture(furniHash.get("Wooden Chair")),
					new Furniture(furniHash.get("Rocking Chair")),
					new Furniture(furniHash.get("Oak Chair")),
					new Furniture(furniHash.get("Oak Armchair")),
					new Furniture(furniHash.get("Teak Armchair")),
					new Furniture(furniHash.get("Mahogany Armchair")),
				}));
			hash.put("Rugs", new Hotspot("Rugs",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Brown Rug")),
					new Furniture(furniHash.get("Rug")),
					new Furniture(furniHash.get("Opulent Rug")),
				}));
			hash.put("Fireplace", new Hotspot("Fireplace",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Clay Fireplace")),
					new Furniture(furniHash.get("Stone Fireplace")),
					new Furniture(furniHash.get("Marble Fireplace")),
				}));
			hash.put("Curtains", new Hotspot("Curtains",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Torn Curtains")),
					new Furniture(furniHash.get("Curtains")),
					new Furniture(furniHash.get("Opulent Curtains")),
				}));
			hash.put("Bookcases", new Hotspot("Bookcases",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Wooden Bookcase")),
					new Furniture(furniHash.get("Oak Bookcase")),
					new Furniture(furniHash.get("Mahogany Bookcase")),
				}));
			hash.put("Kitchen Table", new Hotspot("Kitchen Table",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Wood Table")),
					new Furniture(furniHash.get("Oak Table")),
					new Furniture(furniHash.get("Teak Table")),
				}));
			hash.put("Larder", new Hotspot("Larder",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Wooden Larder")),
					new Furniture(furniHash.get("Oak Larder")),
					new Furniture(furniHash.get("Teak Larder")),
				}));
			hash.put("Sink", new Hotspot("Sink",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Pump and Drain")),
					new Furniture(furniHash.get("Pump and Tub")),
					new Furniture(furniHash.get("Sink")),
				}));
			hash.put("Shelf", new Hotspot("Shelf",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Wooden Shelves 1")),
					new Furniture(furniHash.get("Wooden Shelves 2")),
					new Furniture(furniHash.get("Wooden Shelves 3")),
					new Furniture(furniHash.get("Oak Shelves 1")),
					new Furniture(furniHash.get("Oak Shelves 2")),
					new Furniture(furniHash.get("Teak Shelves 1")),
					new Furniture(furniHash.get("Teak Shelves 2")),
				}));
			hash.put("Stove", new Hotspot("Stove",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Firepit")),
					new Furniture(furniHash.get("Firepit with Hook")),
					new Furniture(furniHash.get("Firepit with Pot")),
					new Furniture(furniHash.get("Small Oven")),
					new Furniture(furniHash.get("Large Oven")),
					new Furniture(furniHash.get("Steel Range")),
					new Furniture(furniHash.get("Fancy Range")),
				}));
			hash.put("Barrel", new Hotspot("Barrel",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Beer Barrel")),
					new Furniture(furniHash.get("Cider Barrel")),
					new Furniture(furniHash.get("Asgarnian Ale")),
					new Furniture(furniHash.get("Greenmans Ale")),
					new Furniture(furniHash.get("Dragon Bitter")),
					new Furniture(furniHash.get("Chefs Delight")),
				}));
			hash.put("Cat Basket", new Hotspot("Cat Basket",
				new Furniture[] {
					new Furniture(furniHash.get("None")),
					new Furniture(furniHash.get("Cat Blanket")),
					new Furniture(furniHash.get("Cat Basket")),
					new Furniture(furniHash.get("Cushioned Basket")),
				}));
    	
    	return hash;
    }

    /**
     * Initialises the Furniture ID Hash with the various furniture IDs so they can be
     * accessed when loading from a Sharing Code
     */
    LinkedHashMap<Object, String> initFurniIDHash(LinkedHashMap<Object, String> hash){
    	
    	hash.put("00", "None");
    	hash.put("01", "Exit Portal");
    	hash.put("02", "Decorative Rock");
    	hash.put("03", "Pond");
    	hash.put("04", "Imp Statue");
    	hash.put("05", "Dungeon Entrance");
    	hash.put("06", "Tree");
    	hash.put("07", "Nice Tree");
    	hash.put("08", "Oak Tree");
    	hash.put("09", "Willow Tree");
    	hash.put("10", "Maple Tree");
    	hash.put("11", "Yew Tree");
    	hash.put("12", "Magic Tree");
    	hash.put("13", "Plant");
    	hash.put("14", "Small Fern");
    	hash.put("15", "Fern");
    	hash.put("16", "Dock Leaf");
    	hash.put("17", "Thistle");
    	hash.put("18", "Reeds");
    	hash.put("19", "Fern ");
    	hash.put("20", "Bush");
    	hash.put("21", "Tall Plant");
    	hash.put("22", "Short Plant");
    	hash.put("23", "Large Leaf Bush");
    	hash.put("24", "Huge Plant");
    	hash.put("25", "Crude Wooden Chair");
    	hash.put("26", "Wooden Chair");
    	hash.put("27", "Rocking Chair");
    	hash.put("28", "Oak Chair");
    	hash.put("29", "Oak Armchair");
    	hash.put("30", "Teak Armchair");
    	hash.put("31", "Mahogany Armchair");
    	hash.put("32", "Brown Rug");
    	hash.put("33", "Rug");
    	hash.put("34", "Opulent Rug");
    	hash.put("35", "Clay Fireplace");
    	hash.put("36", "Stone Fireplace");
    	hash.put("37", "Marble Fireplace");
    	hash.put("38", "Torn Curtains");
    	hash.put("39", "Curtains");
    	hash.put("40", "Opulent Curtains");
    	hash.put("41", "Wooden Bookcase");
    	hash.put("42", "Oak Bookcase");
    	hash.put("43", "Mahogany Bookcase");
    	hash.put("44", "Wood Table");
    	hash.put("45", "Oak Table");
    	hash.put("46", "Teak Table");
    	hash.put("47", "Wooden Larder");
    	hash.put("48", "Oak Larder");
    	hash.put("49", "Teak Larder");
    	hash.put("50", "Pump and Drain");
    	hash.put("51", "Pump and Tub");
    	hash.put("52", "Sink");
    	hash.put("53", "Wooden Shelves 1");
    	hash.put("54", "Wooden Shelves 2");
    	hash.put("55", "Wooden Shelves 3");
    	hash.put("56", "Oak Shalves 1");
    	hash.put("57", "Oak Shalves 2");
    	hash.put("58", "Teak Shelves 1");
    	hash.put("59", "Teak Shelves 2");
    	hash.put("60", "Firepit");
    	hash.put("61", "Firepit with Hook");
    	hash.put("62", "Firepit with Pot");
    	hash.put("63", "Small Oven");
    	hash.put("64", "Large Oven");
    	hash.put("65", "Steel Range");
    	hash.put("66", "Fancy Range");
    	hash.put("67", "Beer Barrel");
    	hash.put("68", "Cider Barrel");
    	hash.put("69", "Asgarnian Ale");
    	hash.put("70", "Greenmans Ale");
    	hash.put("71", "Dragon Bitter");
    	hash.put("72", "Chefs Delight");
    	hash.put("73", "Cat Blanket");
    	hash.put("74", "Cat Basket");
    	hash.put("75", "Cushioned Basket");
    	
    	
    	return hash;
    }
    /**
     * Initialises the Furniture Hash with the different types of furniture
     */
    LinkedHashMap<Object, Furniture> initFurniHash(LinkedHashMap<Object, Furniture> hash){
		
    	hash.put("None", new Furniture(/*Type*/ "None",
    			/*furniURL*/ "none/empty",
    			/*Materials*/ new Materials[0],
    			/*materialNumbers*/ new int[0],
    			/*Level*/ 1,
    			/*ID*/ "00"));
    	hash.put("Exit Portal", new Furniture(/*Type*/ "Exit Portal",
    			/*furniURL*/ "centrepiece/exit_portal",
    			/*Materials*/ new Materials[]{
    				new Materials(materialHash.get("Iron Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 10},
    			/*Level*/ 1,
    			/*ID*/ "01"));
    	hash.put("Decorative Rock", new Furniture(/*Type*/ "Decorative Rock",
    			/*furniURL*/ "centrepiece/decorative_rock",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Limestone Brick")),
    			},
    			/*materialNumbers*/ new int[]{ 5},
    			/*Level*/ 5,
    			/*ID*/ "02"));
    	hash.put("Pond", new Furniture(/*Type*/ "Pond",
    			/*furniURL*/ "centrepiece/pond",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 10},
    			/*Level*/ 10,
    			/*ID*/ "03"));
    	hash.put("Imp Statue", new Furniture(/*Type*/ "Imp Statue",
    			/*furniURL*/ "centrepiece/imp_statue",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Limestone Brick")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 5, 5},
    			/*Level*/ 15,
    			/*ID*/ "04"));
    	hash.put("Dungeon Entrance", new Furniture(/*Type*/ "Dungeon Entrance",
    			/*furniURL*/ "centrepiece/dungeon_entrance",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Marble Block")),
    			},
    			/*materialNumbers*/ new int[]{ 1},
    			/*Level*/ 70,
    			/*ID*/ "05"));
    	hash.put("Tree", new Furniture(/*Type*/ "Tree",
    			/*furniURL*/ "tree/tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Dead Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 5,
    			/*ID*/ "06"));
    	hash.put("Nice Tree", new Furniture(/*Type*/ "Nice Tree",
    			/*furniURL*/ "tree/nice_tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Nice Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 10,
    			/*ID*/ "07"));
    	hash.put("Oak Tree", new Furniture(/*Type*/ "Oak Tree",
    			/*furniURL*/ "tree/oak_tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Oak Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 15,
    			/*ID*/ "08"));
    	hash.put("Willow Tree", new Furniture(/*Type*/ "Willow Tree",
    			/*furniURL*/ "tree/willow_tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Willow Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 30,
    			/*ID*/ "09"));
    	hash.put("Maple Tree", new Furniture(/*Type*/ "Maple Tree",
    			/*furniURL*/ "tree/maple_tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Maple Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 45,
    			/*ID*/ "10"));
    	hash.put("Yew Tree", new Furniture(/*Type*/ "Yew Tree",
    			/*furniURL*/ "tree/yew_tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Yew Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 60,
    			/*ID*/ "11"));
    	hash.put("Magic Tree", new Furniture(/*Type*/ "Magic Tree",
    			/*furniURL*/ "tree/magic_tree",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Magic Tree")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 75,
    			/*ID*/ "12"));
    	hash.put("Plant", new Furniture(/*Type*/ "Plant",
    			/*furniURL*/ "smallPlant1/plant",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 1")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 31,
    			/*ID*/ "13"));
    	hash.put("Small Fern", new Furniture(/*Type*/ "Small Fern",
    			/*furniURL*/ "smallPlant1/small_fern",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 2")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 6,
    			/*ID*/ "14"));
    	hash.put("Fern", new Furniture(/*Type*/ "Fern",
    			/*furniURL*/ "smallPlant1/fern",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 3")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 12,
    			/*ID*/ "15"));
    	hash.put("Dock Leaf", new Furniture(/*Type*/ "Dock Leaf",
    			/*furniURL*/ "smallPlant2/dock_leaf",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 1")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 1,
    			/*ID*/ "16"));
    	hash.put("Thistle", new Furniture(/*Type*/ "Thistle",
    			/*furniURL*/ "smallPlant2/thistle",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 2")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 6,
    			/*ID*/ "17"));
    	hash.put("Reeds", new Furniture(/*Type*/ "Reeds",
    			/*furniURL*/ "smallPlant2/reeds",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 3")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 12,
    			/*ID*/ "18"));
    	hash.put("Fern ", new Furniture(/*Type*/ "Fern",
    			/*furniURL*/ "bigPlant1/fern",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 1")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 1,
    			/*ID*/ "19"));
    	hash.put("Bush", new Furniture(/*Type*/ "Bush",
    			/*furniURL*/ "bigPlant1/bush",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 2")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 6,
    			/*ID*/ "20"));
    	hash.put("Tall Plant", new Furniture(/*Type*/ "Tall Plant",
    			/*furniURL*/ "bigPlant1/tall_plant",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 3")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 12,
    			/*ID*/ "21"));
    	hash.put("Short Plant", new Furniture(/*Type*/ "Short Plant",
    			/*furniURL*/ "bigPlant2/short_plant",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 1")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 1,
    			/*ID*/ "22"));
    	hash.put("Large Leaf Bush", new Furniture(/*Type*/ "Large Leaf Bush",
    			/*furniURL*/ "bigPlant2/large_leaf_bush",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 2")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 6,
    			/*ID*/ "23"));
    	hash.put("Huge Plant", new Furniture(/*Type*/ "Huge Plant",
    			/*furniURL*/ "bigPlant2/huge_plant",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Bagged Plant 3")),
    					new Materials(materialHash.get("Watering Can")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 1},
    			/*Level*/ 12,
    			/*ID*/ "24"));
    	hash.put("Crude Wooden Chair", new Furniture(/*Type*/ "Crude Wooden Chair",
    			/*furniURL*/ "chairs/crude_wooden_chair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 2, 2},
    			/*Level*/ 1,
    			/*ID*/ "25"));
    	hash.put("Wooden Chair", new Furniture(/*Type*/ "Wooden Chair",
    			/*furniURL*/ "chairs/wooden_chair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 8,
    			/*ID*/ "26"));
    	hash.put("Rocking Chair", new Furniture(/*Type*/ "Rocking Chair",
    			/*furniURL*/ "chairs/rocking_chair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 14,
    			/*ID*/ "27"));
    	hash.put("Oak Chair", new Furniture(/*Type*/ "Oak Chair",
    			/*furniURL*/ "chairs/oak_chair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 2},
    			/*Level*/ 19,
    			/*ID*/ "28"));
    	hash.put("Oak Armchair", new Furniture(/*Type*/ "Oak Armchair",
    			/*furniURL*/ "chairs/oak_armchair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 3},
    			/*Level*/ 26,
    			/*ID*/ "29"));
    	hash.put("Teak Armchair", new Furniture(/*Type*/ "Teak Armchair",
    			/*furniURL*/ "chairs/teak_armchair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Teak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 2},
    			/*Level*/ 35,
    			/*ID*/ "30"));
    	hash.put("Mahogany Armchair", new Furniture(/*Type*/ "Mahogany Armchair",
    			/*furniURL*/ "chairs/mahogany_armchair",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Mahogany Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 2},
    			/*Level*/ 50,
    			/*ID*/ "31"));
    	hash.put("Brown Rug", new Furniture(/*Type*/ "Brown Rug",
    			/*furniURL*/ "rugs/brown_rug",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Cloth")),
    			},
    			/*materialNumbers*/ new int[]{ 2},
    			/*Level*/ 2,
    			/*ID*/ "32"));
    	hash.put("Rug", new Furniture(/*Type*/ "Rug",
    			/*furniURL*/ "rugs/rug",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Cloth")),
    			},
    			/*materialNumbers*/ new int[]{ 4},
    			/*Level*/ 13,
    			/*ID*/ "33"));
    	hash.put("Opulent Rug", new Furniture(/*Type*/ "Opulent Rug",
    			/*furniURL*/ "rugs/opulent_rug",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Cloth")),
    					new Materials(materialHash.get("Gold Leaf")),
    			},
    			/*materialNumbers*/ new int[]{ 4, 1},
    			/*Level*/ 65,
    			/*ID*/ "34"));
    	hash.put("Clay Fireplace", new Furniture(/*Type*/ "Clay Fireplace",
    			/*furniURL*/ "fireplace/clay_fireplace",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3},
    			/*Level*/ 3,
    			/*ID*/ "35"));
    	hash.put("Stone Fireplace", new Furniture(/*Type*/ "Stone Fireplace",
    			/*furniURL*/ "fireplace/stone_fireplace",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Limestone Brick")),
    			},
    			/*materialNumbers*/ new int[]{ 2},
    			/*Level*/ 33,
    			/*ID*/ "36"));
    	hash.put("Marble Fireplace", new Furniture(/*Type*/ "Marble Fireplace",
    			/*furniURL*/ "fireplace/marble_fireplace",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Marble Block")),
    			},
    			/*materialNumbers*/ new int[]{ 1},
    			/*Level*/ 63,
    			/*ID*/ "37"));
    	hash.put("Torn Curtains", new Furniture(/*Type*/ "Torn Curtains",
    			/*furniURL*/ "curtains/torn_curtains",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Cloth")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3, 3},
    			/*Level*/ 2,
    			/*ID*/ "38"));
    	hash.put("Curtains", new Furniture(/*Type*/ "Curtains",
    			/*furniURL*/ "curtains/curtains",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Cloth")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 18,
    			/*ID*/ "39"));
    	hash.put("Opulent Curtains", new Furniture(/*Type*/ "Opulent Curtains",
    			/*furniURL*/ "curtains/opulent_curtains",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Teak Plank")),
    					new Materials(materialHash.get("Cloth")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 40,
    			/*ID*/ "40"));
    	hash.put("Wooden Bookcase", new Furniture(/*Type*/ "Wooden Bookcase",
    			/*furniURL*/ "bookcases/wooden_bookcase",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 4, 4},
    			/*Level*/ 4,
    			/*ID*/ "41"));
    	hash.put("Oak Bookcase", new Furniture(/*Type*/ "Oak Bookcase",
    			/*furniURL*/ "bookcases/oak_bookcase",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 3},
    			/*Level*/ 29,
    			/*ID*/ "42"));
    	hash.put("Mahogany Bookcase", new Furniture(/*Type*/ "Mahogany Bookcase",
    			/*furniURL*/ "bookcases/mahogany_bookcase",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Mahogany Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 3},
    			/*Level*/ 40,
    			/*ID*/ "43"));
    	hash.put("Wood Table", new Furniture(/*Type*/ "Wood Table",
    			/*furniURL*/ "kitchenTable/wood_table",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 12,
    			/*ID*/ "44"));
    	hash.put("Oak Table", new Furniture(/*Type*/ "Oak Table",
    			/*furniURL*/ "kitchenTable/oak_table",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 3},
    			/*Level*/ 32,
    			/*ID*/ "45"));
    	hash.put("Teak Table", new Furniture(/*Type*/ "Teak Table",
    			/*furniURL*/ "kitchenTable/teak_table",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Teak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 3},
    			/*Level*/ 52,
    			/*ID*/ "46"));
    	hash.put("Wooden Larder", new Furniture(/*Type*/ "Wooden Larder",
    			/*furniURL*/ "larder/wooden_larder",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 8, 8},
    			/*Level*/ 9,
    			/*ID*/ "47"));
    	hash.put("Oak Larder", new Furniture(/*Type*/ "Oak Larder",
    			/*furniURL*/ "larder/oak_larder",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    			},
    			/*materialNumbers*/ new int[]{ 8},
    			/*Level*/ 33,
    			/*ID*/ "48"));
    	hash.put("Teak Larder", new Furniture(/*Type*/ "Teak Larder",
    			/*furniURL*/ "larder/teak_larder",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Teak Plank")),
    					new Materials(materialHash.get("Cloth")),
    			},
    			/*materialNumbers*/ new int[]{ 8, 2},
    			/*Level*/ 43,
    			/*ID*/ "49"));
    	hash.put("Pump and Drain", new Furniture(/*Type*/ "Pump and Drain",
    			/*furniURL*/ "sink/pump_and_drain",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 5},
    			/*Level*/ 7,
    			/*ID*/ "50"));
    	hash.put("Pump and Tub", new Furniture(/*Type*/ "Pump and Tub",
    			/*furniURL*/ "sink/pump_and_tub",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 10},
    			/*Level*/ 27,
    			/*ID*/ "51"));
    	hash.put("Sink", new Furniture(/*Type*/ "Sink",
    			/*furniURL*/ "sink/sink",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 15},
    			/*Level*/ 47,
    			/*ID*/ "52"));
    	hash.put("Wooden Shelves 1", new Furniture(/*Type*/ "Wooden Shelves 1",
    			/*furniURL*/ "shelves/wooden_shelves_1",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 6,
    			/*ID*/ "53"));
    	hash.put("Wooden Shelves 2", new Furniture(/*Type*/ "Wooden Shelves 2",
    			/*furniURL*/ "shelves/wooden_shelves_2",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3, 6},
    			/*Level*/ 12,
    			/*ID*/ "54"));
    	hash.put("Wooden Shelves 3", new Furniture(/*Type*/ "Wooden Shelves 3",
    			/*furniURL*/ "shelves/wooden_shelves_3",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3, 6},
    			/*Level*/ 23,
    			/*ID*/ "55"));
    	hash.put("Oak Shelves 1", new Furniture(/*Type*/ "Oak Shelves 1",
    			/*furniURL*/ "shelves/oak_shelves_1",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 6},
    			/*Level*/ 34,
    			/*ID*/ "56"));
    	hash.put("Oak Shelves 2", new Furniture(/*Type*/ "Oak Shelves 2",
    			/*furniURL*/ "shelves/oak_shelves_2",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 6},
    			/*Level*/ 45,
    			/*ID*/ "57"));
    	hash.put("Teak Shelves 1", new Furniture(/*Type*/ "Teak Shelves 1",
    			/*furniURL*/ "shelves/teak_shelves_1",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Teak Plank")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 6},
    			/*Level*/ 56,
    			/*ID*/ "58"));
    	hash.put("Teak Shelves 2", new Furniture(/*Type*/ "Teak Shelves 2",
    			/*furniURL*/ "shelves/teak_shelves_2",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Teak Plank")),
    					new Materials(materialHash.get("Soft Clay")),
    					new Materials(materialHash.get("Gold Leaf")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 6, 2},
    			/*Level*/ 67,
    			/*ID*/ "59"));
    	hash.put("Firepit", new Furniture(/*Type*/ "Firepit",
    			/*furniURL*/ "stove/firepit",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 1, 2},
    			/*Level*/ 5,
    			/*ID*/ "60"));
    	hash.put("Firepit with Hook", new Furniture(/*Type*/ "Firepit with Hook",
    			/*furniURL*/ "stove/firepit_with_hook",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 2, 2},
    			/*Level*/ 11,
    			/*ID*/ "61"));
    	hash.put("Firepit with Pot", new Furniture(/*Type*/ "Firepit with Pot",
    			/*furniURL*/ "stove/firepit_with_pot",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    					new Materials(materialHash.get("Soft Clay")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 2},
    			/*Level*/ 17,
    			/*ID*/ "62"));
    	hash.put("Small Oven", new Furniture(/*Type*/ "Small Oven",
    		/*furniURL*/ "stove/small_oven",
    		/*Materials*/ new Materials[]{
    				new Materials(materialHash.get("Steel Bar")),
    		},
    		/*materialNumbers*/ new int[]{ 4},
    		/*Level*/ 24,
    		/*ID*/ "63"));
    	hash.put("Large Oven", new Furniture(/*Type*/ "Large Oven",
    			/*furniURL*/ "stove/large_oven",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 5},
    			/*Level*/ 29,
    			/*ID*/ "64"));
    	hash.put("Steel Range", new Furniture(/*Type*/ "Steel Range",
    			/*furniURL*/ "stove/steel_range",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 6},
    			/*Level*/ 34,
    			/*ID*/ "65"));
    	hash.put("Fancy Range", new Furniture(/*Type*/ "Fancy Range",
    			/*furniURL*/ "stove/fancy_range",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 8},
    			/*Level*/ 42,
    			/*ID*/ "66"));
    	hash.put("Beer Barrel", new Furniture(/*Type*/ "Beer Barrel",
    			/*furniURL*/ "barrel/beer_barrel",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3},
    			/*Level*/ 7,
    			/*ID*/ "67"));
    	hash.put("Cider Barrel", new Furniture(/*Type*/ "Cider Barrel",
    			/*furniURL*/ "barrel/cider_barrel",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    					new Materials(materialHash.get("Cider")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 3, 8},
    			/*Level*/ 12,
    			/*ID*/ "68"));
    	hash.put("Asgarnian Ale", new Furniture(/*Type*/ "Asgarnian Ale",
    			/*furniURL*/ "barrel/asgarnian_ale",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Asgarnian Ale")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 8},
    			/*Level*/ 18,
    			/*ID*/ "69"));
    	hash.put("Greenmans Ale", new Furniture(/*Type*/ "Greenmans Ale",
    			/*furniURL*/ "barrel/greenmans_ale",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Greenmans Ale")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 8},
    			/*Level*/ 26,
    			/*ID*/ "70"));
    	hash.put("Dragon Bitter", new Furniture(/*Type*/ "Dragon Bitter",
    			/*furniURL*/ "barrel/dragon_bitter",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Dragon Bitter")),
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 8, 2},
    			/*Level*/ 36,
    			/*ID*/ "71"));
    	hash.put("Chefs Delight", new Furniture(/*Type*/ "Chefs Delight",
    			/*furniURL*/ "barrel/chefs_delight",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Oak Plank")),
    					new Materials(materialHash.get("Chefs Delight")),
    					new Materials(materialHash.get("Steel Bar")),
    			},
    			/*materialNumbers*/ new int[]{ 3, 8, 2},
    			/*Level*/ 48,
    			/*ID*/ "72"));
    	hash.put("Cat Blanket", new Furniture(/*Type*/ "Cat Blanket",
    			/*furniURL*/ "catBasket/cat_blanket",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Cloth")),
    			},
    			/*materialNumbers*/ new int[]{ 1},
    			/*Level*/ 5,
    			/*ID*/ "73"));
    	hash.put("Cat Basket", new Furniture(/*Type*/ "Cat Basket",
    			/*furniURL*/ "catBasket/cat_basket",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    			},
    			/*materialNumbers*/ new int[]{ 2, 2},
    			/*Level*/ 19,
    			/*ID*/ "74"));
    	hash.put("Cushioned Basket", new Furniture(/*Type*/ "Cushioned Basket",
    			/*furniURL*/ "catBasket/cushioned_basket",
    			/*Materials*/ new Materials[]{
    					new Materials(materialHash.get("Plank")),
    					new Materials(materialHash.get("Nails")),
    					new Materials(materialHash.get("Wool")),
    			},
    			/*materialNumbers*/ new int[]{ 2, 2, 2},
    			/*Level*/ 33,
    			/*ID*/ "75"));
    	/*hash.put("", new Furniture());
    	hash.put("", new Furniture());
    	hash.put("", new Furniture());
    	hash.put("", new Furniture());
    	hash.put("", new Furniture());*/
    	
    	
    	
    	return hash;
    }
    /**
     * Initialises the Material ID Hash with the various material IDs (not GEIDs) so they can be
     * accessed from various places
     */
    LinkedHashMap<Object, String> initMaterialDHash(LinkedHashMap<Object, String> hash){
    	
    	hash.put("0", "Iron Bar");
    	hash.put("1", "Steel Bar");
    	hash.put("2", "Cider");
    	hash.put("3", "Asgarnian Ale");
    	hash.put("4", "Greenmans Ale");
    	hash.put("5", "Dragon Bitter");
    	hash.put("6", "Chefs Delight");
    	hash.put("7", "Wool");
    	hash.put("8", "Limestone Brick");
    	hash.put("9", "Soft Clay");
    	hash.put("10", "Marble Block");
    	hash.put("11", "Plank");
    	hash.put("12", "Oak Plank");
    	hash.put("13", "Teak Plank");
    	hash.put("14", "Mahogany Plank");
    	hash.put("15", "Nails");
    	hash.put("16", "Cloth");
    	hash.put("17", "Gold Leaf");
    	hash.put("18", "Bagged Dead Tree");
    	hash.put("19", "Bagged Nice Tree");
    	hash.put("20", "Bagged Oak Tree");
    	hash.put("21", "Bagged Willow Tree");
    	hash.put("22", "Bagged Maple Tree");
    	hash.put("23", "Bagged Yew Tree");
    	hash.put("24", "Bagged Magic Tree");
    	hash.put("25", "Watering Can");
    	hash.put("26", "Bagged Plant 1");
    	hash.put("27", "Bagged Plant 2");
    	hash.put("28", "Bagged Plant 3");
    	
    	return hash;
    }
    /**
     * Initialises the Material Hash with the different types of material
     */
    LinkedHashMap<Object, Materials> initMaterialHash(LinkedHashMap<Object, Materials> hash){
    	
    	hash.put("Iron Bar", new Materials(/*Type*/ "Iron Bar",
    			/*GEID*/ 2351,
    			/*GEMidPrice*/ 250,
    			/*shopPrice*/ 0,
    			/*Experience*/ 10,
    			/*imageURL*/ "iron_bar",
    			/*ID*/ "0"));
    	hash.put("Steel Bar", new Materials(/*Type*/ "Steel Bar",
    			/*GEID*/ 2353,
    			/*GEMidPrice*/ 720,
    			/*shopPrice*/ 0,
    			/*Experience*/ 20,
    			/*imageURL*/ "steel_bar",
    			/*ID*/ "1"));
    	hash.put("Cider", new Materials(/*Type*/ "Cider",
    			/*GEID*/ 5763,
    			/*GEMidPrice*/ 200,
    			/*shopPrice*/ 2,
    			/*Experience*/ 4,
    			/*imageURL*/ "cider",
    			/*ID*/ "2"));
    	hash.put("Asgarnian Ale", new Materials(/*Type*/ "Asgarnian Ale",
    			/*GEID*/ 1905,
    			/*GEMidPrice*/ 80,
    			/*shopPrice*/ 2,
    			/*Experience*/ 4,
    			/*imageURL*/ "asgarnian_ale",
    			/*ID*/ "3"));
    	hash.put("Greenmans Ale", new Materials(/*Type*/ "Greenmans Ale",
    			/*GEID*/ 1909,
    			/*GEMidPrice*/ 200,
    			/*shopPrice*/ 2,
    			/*Experience*/ 4,
    			/*imageURL*/ "greenmans_ale",
    			/*ID*/ "4"));
    	hash.put("Dragon Bitter", new Materials(/*Type*/ "Dragon Bitter",
    			/*GEID*/ 1911,
    			/*GEMidPrice*/ 450,
    			/*shopPrice*/ 2,
    			/*Experience*/ 4,
    			/*imageURL*/ "dragon_bitter",
    			/*ID*/ "5"));
    	hash.put("Chefs Delight", new Materials(/*Type*/ "Chefs Delight",
    			/*GEID*/ 5755,
    			/*GEMidPrice*/ 3400,
    			/*shopPrice*/ 0,
    			/*Experience*/ 4,
    			/*imageURL*/ "chefs_delight",
    			/*ID*/ "6"));
    	hash.put("Wool", new Materials(/*Type*/ "Wool",
    			/*GEID*/ 1737,
    			/*GEMidPrice*/ 140,
    			/*shopPrice*/ 0,
    			/*Experience*/ 0,
    			/*imageURL*/ "wool",
    			/*ID*/ "7"));
    	hash.put("Limestone Brick", new Materials(/*Type*/ "Limestone Brick",
    			/*GEID*/ 3420,
    			/*GEMidPrice*/ 100,
    			/*shopPrice*/ 21,
    			/*Experience*/ 20,
    			/*imageURL*/ "limestone_brick",
    			/*ID*/ "8"));
    	hash.put("Soft Clay", new Materials(/*Type*/ "Soft Clay",
    			/*GEID*/ 1761,
    			/*GEMidPrice*/ 170,
    			/*shopPrice*/ 0,
    			/*Experience*/ 10,
    			/*imageURL*/ "soft_clay",
    			/*ID*/ "9"));
    	hash.put("Marble Block", new Materials(/*Type*/ "Marble Block",
    			/*GEID*/ 8786,
    			/*GEMidPrice*/ 324500,
    			/*shopPrice*/ 325000,
    			/*Experience*/ 500,
    			/*imageURL*/ "marble_block",
    			/*ID*/ "10"));
    	hash.put("Plank", new Materials(/*Type*/ "Plank",
    			/*GEID*/ 960,
    			/*GEMidPrice*/ 200,
    			/*shopPrice*/ 100,
    			/*Experience*/ 30,
    			/*imageURL*/ "plankt",
    			/*ID*/ "11"));
    	hash.put("Oak Plank", new Materials(/*Type*/ "Oak Plank",
    			/*GEID*/ 8778,
    			/*GEMidPrice*/ 470,
    			/*shopPrice*/ 250,
    			/*Experience*/ 60,
    			/*imageURL*/ "oak_plank",
    			/*ID*/ "12"));
    	hash.put("Teak Plank", new Materials(/*Type*/ "Teak Plank",
    			/*GEID*/ 8780,
    			/*GEMidPrice*/ 800,
    			/*shopPrice*/ 500,
    			/*Experience*/ 90,
    			/*imageURL*/ "teak_plank",
    			/*ID*/ "13"));
    	hash.put("Mahogany Plank", new Materials(/*Type*/ "Mahogany Plank",
    			/*GEID*/ 8782,
    			/*GEMidPrice*/ 1790,
    			/*shopPrice*/ 1500,
    			/*Experience*/ 120,
    			/*imageURL*/ "mahogany_plank",
    			/*ID*/ "14"));
    	hash.put("Nails", new Materials(/*Type*/ "Nails",
    			/*GEID*/ 1539,
    			/*GEMidPrice*/ 25,
    			/*shopPrice*/ 52,
    			/*Experience*/ 3,
    			/*imageURL*/ "steel_nails",
    			/*ID*/ "15"));
    	hash.put("Cloth", new Materials(/*Type*/ "Bolt of Cloth",
    			/*GEID*/ 8790,
    			/*GEMidPrice*/ 830,
    			/*shopPrice*/ 650,
    			/*Experience*/ 15,
    			/*imageURL*/ "cloth",
    			/*ID*/ "16"));
    	hash.put("Gold Leaf", new Materials(/*Type*/ "Gold Leaf",
    			/*GEID*/ 8784,
    			/*GEMidPrice*/ 129900,
    			/*shopPrice*/ 130000,
    			/*Experience*/ 300,
    			/*imageURL*/ "gold_leaf",
    			/*ID*/ "17"));
    	hash.put("Bagged Dead Tree", new Materials(/*Type*/ "Bagged Dead Tree",
    			/*GEID*/ 8417,
    			/*GEMidPrice*/ 1070,
    			/*shopPrice*/ 1000,
    			/*Experience*/ 31,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "18"));
    	hash.put("Bagged Nice Tree", new Materials(/*Type*/ "Bagged Nice Tree",
    			/*GEID*/ 8419,
    			/*GEMidPrice*/ 2150,
    			/*shopPrice*/ 2000,
    			/*Experience*/ 44,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "19"));
    	hash.put("Bagged Oak Tree", new Materials(/*Type*/ "Bagged Oak Tree",
    			/*GEID*/ 8421,
    			/*GEMidPrice*/ 5170,
    			/*shopPrice*/ 5000,
    			/*Experience*/ 70,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "20"));
    	hash.put("Bagged Willow Tree", new Materials(/*Type*/ "Bagged Willow Tree",
    			/*GEID*/ 8423,
    			/*GEMidPrice*/ 10200,
    			/*shopPrice*/ 10000,
    			/*Experience*/ 100,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "21"));
    	hash.put("Bagged Maple Tree", new Materials(/*Type*/ "Bagged Maple Tree",
    			/*GEID*/ 8425,
    			/*GEMidPrice*/ 15100,
    			/*shopPrice*/ 15000,
    			/*Experience*/ 122,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "22"));
    	hash.put("Bagged Yew Tree", new Materials(/*Type*/ "Bagged Yew Tree",
    			/*GEID*/ 8427,
    			/*GEMidPrice*/ 20000,
    			/*shopPrice*/ 20000,
    			/*Experience*/ 141,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "23"));
    	hash.put("Bagged Magic Tree", new Materials(/*Type*/ "Bagged Magic Tree",
    			/*GEID*/ 8429,
    			/*GEMidPrice*/ 30400,
    			/*shopPrice*/ 50000,
    			/*Experience*/ 223,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "24"));
    	hash.put("Watering Can", new Materials(/*Type*/ "Watering Can",
    			/*GEID*/ 5331,
    			/*GEMidPrice*/ 86,
    			/*shopPrice*/ 25,
    			/*Experience*/ 0,
    			/*imageURL*/ "watering_can",
    			/*ID*/ "25"));
    	hash.put("Bagged Plant 1", new Materials(/*Type*/ "Bagged Plant 1",
    			/*GEID*/ 8431,
    			/*GEMidPrice*/ 1170,
    			/*shopPrice*/ 1000,
    			/*Experience*/ 31,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "26"));
    	hash.put("Bagged Plant 2", new Materials(/*Type*/ "Bagged Plant 2",
    			/*GEID*/ 8433,
    			/*GEMidPrice*/ 5200,
    			/*shopPrice*/ 5000,
    			/*Experience*/ 70,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "27"));
    	hash.put("Bagged Plant 3", new Materials(/*Type*/ "Bagged Plant 3",
    			/*GEID*/ 8435,
    			/*GEMidPrice*/ 10200,
    			/*shopPrice*/ 10000,
    			/*Experience*/ 100,
    			/*imageURL*/ "bagged_plant",
    			/*ID*/ "28"));
    	    	
    	return hash;
    }

}
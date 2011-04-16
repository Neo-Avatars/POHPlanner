

//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware: 
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware: 
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware:
//TODO comment out line 291 :aware:


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

//TODO add support for GE prices
//TODO save stats when closing the ViewStats Frame
//TODO add more rules and confirmations when building eg. stairs / costume room
//TODO optimise the sharing code to contain less 0s
//TODO get a CostumeRoom screenshot - Josh said he may do it
//001,Lostsamurai7,13403,1350200000000000000,1360204000000000000,1440201000000000000,14503,1460201000000000000,

/**
 * The main class file for the POH Planner.
 * 
 * @author Neo Avatars
 */
public class POHPlanner extends JApplet implements ActionListener, MouseListener, ItemListener {

	private static final long serialVersionUID = -4986474296742496209L;
	
	private JLabel powerImageLabel = new JLabel(new ImageIcon(this.getClass().getResource("poweredby.gif")));
	
	//loading things
	private JPanel loadingPanel = new JPanel();
	private JLabel loadingLabel = new JLabel();
	private JPanel almightyPanel = new JPanel();
	
	//main panels
	private JPanel corePanel = new JPanel();
	private JPanel overviewPanel = new JPanel();
	private JPanel roomPanel = new JPanel();
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
	private JMenu mainMenu = new JMenu( "Menu" );
	private JMenuItem menuItem = new JMenu( "Item" );
	private JTextField menuHouseCodeTextbox = new JTextField();
	private JLabel menuHouseCodeLabel = new JLabel();
	private JButton menuHouseCodeButton = new JButton();
	
	//colours
	private Color bgblue = new Color(83, 115, 191);
	private Color bgcream = new Color(217, 198, 163);
	private Color bgtransparent = new Color(0, 0, 0, 0);
	private Color txtred = new Color(127, 2, 1);
	private Color txtblue = new Color(1, 0, 254);
	private Color polycol = new Color(89,63,255,128);
	
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
    private LinkedHashMap<Object, Room> roomLoadHash = new LinkedHashMap<Object, Room>();
    private LinkedHashMap<Object, Room> roomHash = new LinkedHashMap<Object, Room>();
	private ArrayList<HiscoreData> hiscoreList = new ArrayList<HiscoreData>();
	private LinkedHashMap<Object, FurniBuildPanel> furniBuildHash = new LinkedHashMap<Object, FurniBuildPanel>();
	private LinkedHashMap<Object, FurniStatusLabel> furniStatusHash = new LinkedHashMap<Object, FurniStatusLabel>();
	private LinkedHashMap<Object, StatsPane> statsPanelHash = new LinkedHashMap<Object, StatsPane>();
	private LinkedHashMap<Object, Quest> questHash = new LinkedHashMap<Object, Quest>();
	
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
	
	//furniture building frame
	private JFrame furniBuildingFrame = new JFrame();
	private JPanel furniBuildingScrollPanel = new JPanel();
	private JScrollPane furniBuildingScrollPane = new JScrollPane(furniBuildingScrollPanel);
	private JLabel furniRemoveFromHotspotLabel = new JLabel();
	private JLabel furniTooHighLevelImgLabel = new JLabel(new ImageIcon("furniIcons/unavailable.gif"));
	
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
	private int selectedHotspotID = 0;
	private int currentRoomFurniNumber = 0;
	int overviewViewingLevel = 1;
	/**
	 * Lets you switch back to the last viewed room.
	 * @type String
	 */
	String lastViewedRoom;
	/**
	 * The room currently being viewed.
	 */
	String viewingRoom = "";
	/**
	 * Currently selected room. The hashKey to be used with roomHash.
	 */
	String overviewSelectedRoom = "";
	/**
	 * If True: ViewingGrid. If False: ViewingRoom.
	 */
	boolean viewingGrid;
	boolean ignoreLevelWarnings = false;
	
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
	private JButton viewStatsSaveButton = new JButton("Save and close Window");
	private JLabel viewStatsQuestTitleLabel = new JLabel("Important Quests", JLabel.CENTER);
	private JLabel viewStatsQuestHelpLabel = new JLabel("Select the Quests that you have completed.");
	private JButton viewStatsSelectAllQuestsButton = new JButton("Select all Quests");
	private JButton viewStatsDeselectAllQuestsButton = new JButton("Deselect all Quests");
	
	//error messages
	private JFrame rightDeleteRoomConfirmFrame = new JFrame();
	private JFrame ignoreLevelWarningsConfirmFrame = new JFrame();
	
	//used for testing and should probably exist in a
	//different form by the finished version
	private String username = "Salmoneus"; //set to "" - name is for easier testing Lostsamurai7
	private boolean[] fourTrues = new boolean[]{true, true, true, true}; //was used
	
	public void init() {
	    initMenuBar();
		initLoadingPanel();
		loadingLabel.setText("Loading . . . 7%");
		//needs to be done before the hiscore lookup so the stats can be inserted
		initStatsFrame();
		loadingLabel.setText("Loading . . . 18%");
		if(username != ""){
			new RuneScapeHiscore();
		}
		loadingLabel.setText("Loading . . . 19%");
		createAndShowGUI();
		loadingLabel.setText("Loading . . . 29%");
		initRoomBuildingFrame();
		loadingLabel.setText("Loading . . . 37%");
		initFurniBuildingFrame();
		loadingLabel.setText("Loading . . . 42%");
		initRoomArray();
		loadingLabel.setText("Loading . . . 55%");
		initRoomInfoPanel();
		loadingLabel.setText("Loading . . . 69%");
		initRightPanel();
		
		inducePointlessWait();
		
		loadingLabel.setText("Loading . . . 78%");
		initRightHouseOverviewPanel();
		loadingLabel.setText("Loading . . . 95%");
		createAndShowGrid(overviewViewingLevel);
		loadingPanel.setVisible(false);
		corePanel.setVisible(true);
		menuHouseCodeButton.setEnabled( true );
		calculateSharingCode();

	}

	/**
     * Initialises the Menu Bar
     */
	public void initMenuBar() {
		
		menuBar.add(mainMenu);
	    mainMenu.add(menuItem);
	    //add space between menu and House Code
	    menuBar.add(Box.createRigidArea(new Dimension(50, 0)));
	    //menuBar.add(Box.createHorizontalGlue());
	    
	    menuHouseCodeLabel.setText("House Plan: ");
	    menuHouseCodeButton.setText("Load");
	    menuHouseCodeButton.setEnabled( false );
	    menuHouseCodeButton.addMouseListener(this);
	    menuHouseCodeTextbox.addActionListener(this);
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
	    this.setSize(715, 549);
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
		
		viewStatsFrame.setBounds(200, 60, 350, 450);
		viewStatsFrame.setIconImage(new ImageIcon("statsicon.gif").getImage());
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
		viewStatsSaveButton.setToolTipText("Save your Stats and close this window.");
		
		//create the various skills
		statsPanelHash.put(0, new StatsPane("Attack", 1));
		statsPanelHash.put(1, new StatsPane("Defence", 2));
		statsPanelHash.put(2, new StatsPane("Strength", 3));
		statsPanelHash.put(3, new StatsPane("Hitpoints", 4));
		statsPanelHash.put(4, new StatsPane("Ranged", 5));
		statsPanelHash.put(5, new StatsPane("Prayer", 6));
		statsPanelHash.put(6, new StatsPane("Magic", 7));
		statsPanelHash.put(7, new StatsPane("Cooking", 8));
		statsPanelHash.put(8, new StatsPane("Woodcutting", 9));
		statsPanelHash.put(9, new StatsPane("Fletching", 10));
		statsPanelHash.put(10, new StatsPane("Fishing", 11));
		statsPanelHash.put(11, new StatsPane("Firemaking", 12));
		statsPanelHash.put(12, new StatsPane("Crafting", 13));
		statsPanelHash.put(13, new StatsPane("Smithing", 14));
		statsPanelHash.put(14, new StatsPane("Mining", 15));
		statsPanelHash.put(15, new StatsPane("Herblore", 16));
		statsPanelHash.put(16, new StatsPane("Agility", 17));
		statsPanelHash.put(17, new StatsPane("Thieving", 18));
		statsPanelHash.put(18, new StatsPane("Slayer", 19));
		statsPanelHash.put(19, new StatsPane("Farming", 20));
		statsPanelHash.put(20, new StatsPane("Runecrafting", 21));
		statsPanelHash.put(21, new StatsPane("Hunter", 22));
		statsPanelHash.put(22, new StatsPane("Construction", 23));
		statsPanelHash.put(23, new StatsPane("Summoning", 24));
		
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
		
		//add Quest title
		c.insets = bottomFiveTopThreeInsets;
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
		
		//add a save stats button
		c.insets = bottomFiveTopThreeInsets;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 4;
		viewStatsPanel.add(viewStatsSaveButton, c);
		viewStatsSaveButton.addMouseListener(this);
		
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
		roomBuildingFrame.setIconImage(new ImageIcon("hammer.gif").getImage());
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
		
		//roomBuildingFrame.setVisible(true);
		
	}

	/**
     * Initialises the Furniture Building Frame
     */
	public void initFurniBuildingFrame(){
		
		furniBuildingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		furniBuildingFrame.setBounds(50, 80, 600, 400);
		furniBuildingFrame.setIconImage(new ImageIcon("hammer.gif").getImage());
		furniBuildingFrame.setTitle("Build Furniture");
		
		furniRemoveFromHotspotLabel.setForeground(txtred);
		
		furniBuildingFrame.add(furniBuildingScrollPane);
		furniBuildingScrollPanel.setBackground( bgcream );
		
		//init furniBuildHash with 8 panels (I think that's the max furni available to 1 hotspot)
		for(int i = 0; i < 8; i++){
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

	}
	/**
     * Adds info to the Furniture Building Frame
     * 
     * @param hotspotType The type of hotspot to set up data for
     */
	public void createFurniBuildingFrameContent(String hotspotType){
		Hotspot hotspot = new Hotspot(hotspotType);
		workOuthotspotID(hotspotType);
		furniBuildingFrameHotspotType = hotspotType;
		
		int furniCost = 0;
		int furniXP = 0;
		int numberOfAddedPanels = 0;
		int i = 0;
		int scrollPaneGridRows;
		int remainder = hotspot.getFurniTypes().length % 2;

		String builtFurni = roomHash.get(viewingRoom).getHotspots()[0].getBuiltFurniture().getFurniType();
		
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
		furniBuildingFrame.setBounds(50, 80, 600, (scrollPaneGridRows*100));
		
		//add various information about what you're about to build
		for( ; i < hotspot.getFurniTypes().length; i++){
			furniBuildHash.get( i ).setFurni(hotspot.getFurniTypes()[i]);
			
			GridBagConstraints c = new GridBagConstraints();
			//set panels to display content in
			if(i == 0){
				//set text for label based on if anything is currently built or not
				if(builtFurni != "None"){
					furniRemoveFromHotspotLabel.setText("Remove " + builtFurni);
				} else {
					furniRemoveFromHotspotLabel.setText("Close Furniture Building Window");
				}
				furniBuildHash.get( i ).getBuildPanel().add(furniRemoveFromHotspotLabel, c);
				furniBuildHash.get( i ).getBuildPanel().setToolTipText("Remove " + builtFurni);

				//add it to furniBuildingScrollPanel
				furniBuildingScrollPanel.add(furniBuildHash.get( i ).getBuildPanel());
				numberOfAddedPanels++;
				continue;
			}

			furniCost = 0;
			furniXP = 0;
			
			JLabel furniTypeLabel = new JLabel(hotspot.getFurniTypes()[i].getFurniType());
			JLabel furniImageLabel = new JLabel(new ImageIcon("furniIcons/"+hotspot.getFurniTypes()[i].getFurniURL()+".gif" ));
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
				HiscoreData conLvl = hiscoreList.get( 23 );
				if(hotspot.getFurniTypes()[i].getLevel() > conLvl.getLevel()
						&& !ignoreLevelWarnings){
					furniBuildHash.get( i ).getBuildPanel().add(furniTooHighLevelImgLabel, c);
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

	}
	/**
	 * Works out the ID of the selected hotspot based on which room is being
	 * viewed and the type of hotspot that has been selected
	 * @param hotspotType The type of hotspot that has been selected
	 */
	public void workOuthotspotID(String hotspotType){
		//work out what type of room and hotspot you're looking at
		selectedHotspotID = 0;

		for(; selectedHotspotID < roomHash.get( viewingRoom ).getHotspots().length; selectedHotspotID++){
			if(roomHash.get( viewingRoom ).getHotspots()[selectedHotspotID].getHotspotType().equals(hotspotType)){
				break;
			}
		}
	}

	/**
     * Initialises and resets the room layout so there is just the basic Parlour and Garden
     */
	public void initRoomArray(){

		initialisingRoomAray = true;
		String hashKey;
		
		setEmptyRoomArray();
		roomHash = roomLoadHash;
		
		//add a parlour and garden as a default house layout
		hashKey = "overviewRoom" + Integer.toString(1) + Integer.toString(3) + Integer.toString(4);
		buildRoom(hashKey, "Parlour", 1);
		hashKey = "overviewRoom" + Integer.toString(1) + Integer.toString(4) + Integer.toString(4);
		buildRoom(hashKey, "Garden", 1);
		
		//add exit portal to Garden
		roomHash.get( hashKey ).getHotspots()[0].setBuiltFurniture(new Furniture("Exit Portal"));
		
		initialisingRoomAray = false;
	}
	/**
	 * Sets all the rooms in roomLoadHash to empty. By not going straight to
	 * the roomHash, it stops problems with a faulty Sharing Code, so you also
	 * need to put 'roomHash = roomLoadHash;' at some point.
	 */
	public void setEmptyRoomArray(){
		String hashKey;
		
		for(int i = 0;i < 3;i++){
			for(int j = 0;j < 9; j++){
				for(int k = 0;k < 9; k++){
					hashKey = "overviewRoom" + Integer.toString(i) + Integer.toString(j) + Integer.toString(k);
					
					if(i == 1){
						//buildRoom(hashKey, "EmptyFloor1", i);
						roomLoadHash.put( hashKey , new Room("EmptyFloor1", i));
						roomLoadHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						roomLoadHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 0) {
						//buildRoom(hashKey, "EmptyDungeon", 0);
						roomLoadHash.put( hashKey , new Room("EmptyDungeon", 0));
						roomLoadHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						roomLoadHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
					} else if(i == 2) {
						roomLoadHash.put( hashKey , new Room("EmptyUpstairs", 0));
						roomLoadHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
						roomLoadHash.get( hashKey ).getRoomOverviewLabel().setToolTipText("You need to build a Room downstairs to build one here");
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
		for(int i = 0; i < 8; i++){
			//create the panel
			furniStatusHash.put( i , new FurniStatusLabel());
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
	    comboSelectGridLevel.setSelectedIndex(1);
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
		rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]);
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
	    		overviewPanel.add(roomHash.get( hashKey ).getRoomOverviewLabel());
	    		//System.out.println(roomHash.get( hashKey ).getRoomOverviewLabel());
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
     * @param roomKey The key used to access data in roomHash
     */
	private void createAndShowRoom(String roomKey) {

		roomPanel.removeAll();
		refreshGrid();
		
		lastViewedRoom = roomKey;
		viewingRoom = roomKey;
		
	    roomPanel.add(roomHash.get(roomKey).getRoomImageLabel());
		//RoomBackground background = new RoomBackground("roomPics/" + roomHash.get(roomKey).getRoomImageURL() + ".png");
		//roomPanel.add(background);

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
	 * @param roomKey The key used to access data in roomHash
	 */
	public void selectRoom(String roomKey){
		
		//set a previously selected room to unselected
		if(overviewSelectedRoom != ""
			&& roomHash.get(roomKey).getRoomType() != " "){
				unselectRoom();
		}
		//set the clicked room as selected
		roomHash.get(roomKey).setIsSelected(true);
		
		//specify the new selected room
		overviewSelectedRoom = roomKey;
		
		//System.out.println(roomKey + " is selected " + roomHash.get(roomKey).getIsSelected());

		//check whether the selected room is empty or not
		if(roomHash.get(roomKey).getRoomType().equals("")
			|| roomHash.get(roomKey).getRoomType().equals(" ")){
			//show building pane
			roomBuildingFrame.setVisible(true);
		} else if(roomHash.get(roomKey).getRoomType().equals("  ")){
			JOptionPane.showMessageDialog( null, "You need to build a Room downstairs to build one here", 
                    "Oops!", JOptionPane.ERROR_MESSAGE );
		} else {
			//If not empty, update room info
			//show room info
			setRightRoomInfoPanelContent();
			//indicate that the room's selected by changing the text colour
			roomHash.get(overviewSelectedRoom).getRoomOverviewLabel().setForeground(txtred);
			//show delete room button
		}
	}
	/**
	 * Unselects a room
	 */
	public void unselectRoom(){
		if(overviewSelectedRoom != ""){
			roomHash.get(overviewSelectedRoom).setIsSelected(false);
			//indicate that the room's unselected by reverting the text colour
			roomHash.get(overviewSelectedRoom).getRoomOverviewLabel().setForeground(Color.WHITE);
			overviewSelectedRoom = "";
			resetRightRoomInfoPanelContent();
		}
	}
	
	/**
     * Switches from Grid View to Room View, checking that there has first
     * been a 'selection click'
     * @param roomKey The key used to access data in roomHash
     */
	public void goToRoomFromGrid(String roomKey){
		
		if(roomHash.get(roomKey).getIsSelected() == false){
			selectRoom(roomKey);
		} 
		
		else if(roomHash.get(roomKey).getIsSelected() == true) {
			//unselect room
			roomHash.get(roomKey).setIsSelected(false);
			resetRightRoomInfoPanelContent();
			//indicate that the room's unselected by reverting the text colour
			roomHash.get(overviewSelectedRoom).getRoomOverviewLabel().setForeground(Color.WHITE);
			overviewSelectedRoom = "";
			
			//show room
			if(roomHash.get(roomKey).getRoomType() != ""
				&& roomHash.get(roomKey).getRoomType() != " "
					&& roomHash.get(roomKey).getRoomType() != "  "){
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
		rightRoomInfoRoomTypeLabel.setText(roomHash.get(overviewSelectedRoom).getRoomType()  + " Stats");

		//work out the rest of the labels
		/*rightRoomInfoLevelLabel.setText("");
		rightRoomInfoRoomCostLabel.setText("");
		rightRoomInfoFurniCostLabel.setText("");
		rightRoomInfoFurniXPLabel.setText("");
		rightRoomInfoDeleteRoom.setText("");*/
		
		rightRoomInfoLevelLabel.setText("Required level: " + Integer.toString(roomHash.get(overviewSelectedRoom).getRoomLevel()));
		rightRoomInfoRoomCostLabel.setText("Room Cost: " + Integer.toString(roomHash.get(overviewSelectedRoom).getRoomCost()) + "gp");
		rightRoomInfoFurniCostLabel.setText("Furniture Cost: " + calculateRoomFurniCost(overviewSelectedRoom) + "gp");
		rightRoomInfoFurniXPLabel.setText("Furniture XP: " + calculateRoomFurniXP(overviewSelectedRoom) + "xp");
		rightRoomInfoDeleteRoom.setText("Demolish " + roomHash.get(overviewSelectedRoom).getRoomType());
		
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
		//if there are 4 doors, don't show the rotate buttons
		//doesn't look very efficient, but nothing else I've tried has worked properly
		if(roomHash.get(overviewSelectedRoom).getDoorLayout()[0] == true
				&& roomHash.get(overviewSelectedRoom).getDoorLayout()[1] == true
				&& roomHash.get(overviewSelectedRoom).getDoorLayout()[2] == true
				&& roomHash.get(overviewSelectedRoom).getDoorLayout()[3] == true){
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
			if(room){
				//add one to the number of rooms, then update the label
				rightOverviewLabelNumbers[0] += 1;
				rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]);
				
				//increase room cost by the cost of the new room, then update the label
				rightOverviewLabelNumbers[1] += roomHash.get(location).getRoomCost();
				rightOverviewRoomCostLabel.setText("Rooms Cost: "+rightOverviewLabelNumbers[1]+"gp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");

			} else {
				//add the furni cost to the total furni cost, then update the label
				rightOverviewLabelNumbers[2] += calculateSingleFurniCost(roomHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniCostLabel.setText("Cost of Furniture: "+rightOverviewLabelNumbers[2]+"gp");
				
				//add the furni xp to the total furni xp, then update the label
				rightOverviewLabelNumbers[3] += calculateSingleFurniXP(roomHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniNumberLabel.setText("Furniture Exp: "+rightOverviewLabelNumbers[3]+"xp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");
			}
		} //if demolishing
		else {
			if(room){
				//subtract one from the number of rooms, then update the label
				rightOverviewLabelNumbers[0] -= 1;
				rightOverviewRoomNumberLabel.setText("Number of Rooms: " + rightOverviewLabelNumbers[0]);
				
				//decrease room cost by the cost of the new room, then update the label
				rightOverviewLabelNumbers[1] -= roomHash.get(location).getRoomCost();
				rightOverviewRoomCostLabel.setText("Rooms Cost: "+rightOverviewLabelNumbers[1]+"gp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");
				
			} else {
				//subtract the furni cost from the total furni cost, then update the label
				rightOverviewLabelNumbers[2] -= calculateSingleFurniCost(roomHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniCostLabel.setText("Cost of Furniture: "+rightOverviewLabelNumbers[2]+"gp");
				
				//subtract the furni xp from the total furni xp, then update the label
				rightOverviewLabelNumbers[3] -= calculateSingleFurniXP(roomHash.get(location).getHotspots()[selectedHotspotID].getBuiltFurniture());
				rightOverviewFurniNumberLabel.setText("Furniture Exp: "+rightOverviewLabelNumbers[3]+"xp");
				
				//add the furni cost to get the total cost and update the label
				rightOverviewTotalCostLabel.setText("Total cost: "+(rightOverviewLabelNumbers[1]+rightOverviewLabelNumbers[2])+"gp");
			}
		}
	}
	
	/**
	 * Builds a room.
	 * @param roomKey The key used to access data in roomHash.
	 * @param roomType The type of room you want to build.
	 * @param level The floor you want to build on.
	 * 0 = Dungeon. 1 = Ground Floor. 2 = Upstairs.
	 */
	public void buildRoom(String roomKey, String roomType, int level){
		//Room checkRoom = new Room(roomType, level);
		//check that you have the level to actually build it
		if(initialisingRoomAray
				|| new Room(roomType, level).getRoomLevel() <= hiscoreList.get(23).getLevel()
					|| ignoreLevelWarnings){
			//build the room
			roomHash.put( roomKey , new Room(roomType, level));
			roomHash.get( roomKey ).getRoomOverviewLabel().addMouseListener(this);
			roomHash.get( roomKey ).getRoomOverviewLabel().setToolTipText(roomHash.get( roomKey ).getRoomType());
	
			//indicate upstairs
			if(level == 1 && roomHash.get(roomKey).getRoomType() != ""){
				String newKey = "overviewRoom2" + roomKey.substring(13);
				
				roomHash.put( newKey , new Room("AboveUpsairs", 2));
				roomHash.get( newKey ).getRoomOverviewLabel().addMouseListener(this);
				roomHash.get( newKey ).getRoomOverviewLabel().setToolTipText("Build a Room");
			}
			
			if(!initialisingRoomAray){
				updateRightHouseOverviewPanel(true, true, roomKey);
				calculateSharingCode();
			}
			
			//show the grid with the new shiny room
			//createAndShowRoom(overviewSelectedRoom);
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		} else {
			//show a confirmation dialogue
			Object[] confirmFurniBuildOptions = {"Yes",
	        "No"};
			int ignoreLevelWarningsPane = JOptionPane.showOptionDialog(ignoreLevelWarningsConfirmFrame,
			"Your Construction level is not high enough to build a " +
			roomType + ".\n" +
			"Do you want to ignore level requirements and build it anyway?",
			"Confirm Room Construction",
			JOptionPane.YES_NO_OPTION,
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

	        }
		}
	}
	/**
	 * Demolishes a room.
	 */
	public void demolishRoom(){
		//show a confirmation dialogue
		Object[] deleteRoomOptions = {"Demolish " + roomHash.get(overviewSelectedRoom).getRoomType(),
        "Cancel"};
		int demolitionPane = JOptionPane.showOptionDialog(rightDeleteRoomConfirmFrame,
		"Are you sure you want to demolish the "+roomHash.get(overviewSelectedRoom).getRoomType()+
			"?\nYou will lose any furniture built within.",
		"Confirm Room Demolition",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.WARNING_MESSAGE,
		null,     //do not use a custom Icon
		deleteRoomOptions,  //the titles of buttons
		deleteRoomOptions[1]); //default button title

		//if you want to delete, delete the room
		if (demolitionPane == JOptionPane.YES_OPTION) {
			updateRightHouseOverviewPanel(false, true, overviewSelectedRoom);
			calculateSharingCode();
            if(overviewViewingLevel == 0){
            	roomHash.put(overviewSelectedRoom, new Room("EmptyDungeon", 0));
            } else {
            	roomHash.put(overviewSelectedRoom, new Room("EmptyFloor1", overviewViewingLevel));
            }
            refreshGrid();
            createAndShowGrid(overviewViewingLevel);
            resetRightRoomInfoPanelContent();
            overviewSelectedRoom = "";
        } //if not, don't delete the room
		else if (demolitionPane == JOptionPane.NO_OPTION) {

        }
	}
	/**
	 * Removes furniture
	 * @param roomKey The key used to access data in roomHash
	 * @param hotspotID The ID of the Hotspot within the room
	 */
	public void removeFurni(String roomKey, int hotspotID){

		updateRightHouseOverviewPanel(false, false, viewingRoom);
		
		//delete anything that may be in the selected spot
		roomHash.get( roomKey ).getHotspots()[hotspotID].setBuiltFurniture(new Furniture("None"));
		//hide build panel
		furniBuildingFrame.setVisible(false);
		//update stats about the room in the room panel
		setRoomInfoPanelContent();
		calculateSharingCode();

	}
	/**
	 * Builds furniture
	 * @param roomKey The key used to access data in roomHash
	 * @param hotspotID The ID of the Hotspot within the room
	 * @param panelID The ID of the Panel that was clicked on
	 */
	public void buildFurni(String roomKey, int hotspotID, int panelID){
		//check that you have the level to actually build it
		if(furniBuildHash.get( panelID ).getFurni().getLevel() <= hiscoreList.get(23).getLevel()
				|| ignoreLevelWarnings){
			//remove anything already in the spot
			removeFurni(roomKey, hotspotID);
			
			//build the new things
			roomHash.get( roomKey ).getHotspots()[hotspotID].
			setBuiltFurniture(new Furniture(furniBuildHash.get( panelID ).getFurni().getFurniType()));
			//hide build panel
			furniBuildingFrame.setVisible(false);
			
			updateRightHouseOverviewPanel(true, false, viewingRoom);
			//update stats about the room in the room panel
			setRoomInfoPanelContent();
			calculateSharingCode();
		} else {
			//show a confirmation dialogue
			Object[] confirmFurniBuildOptions = {"Yes",
	        "No"};
			int ignoreLevelWarningsPane = JOptionPane.showOptionDialog(ignoreLevelWarningsConfirmFrame,
			"Your Construction level is not high enough to build a " +
			furniBuildHash.get( panelID ).getFurni().getFurniType() + ".\n" +
			"Do you want to ignore level requirements and build it anyway?",
			"Confirm Furniture Construction",
			JOptionPane.YES_NO_OPTION,
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

	        }
		}
	}

	/**
     * Adds info to the tabbed pane with stats and such when in Room View
     * 
     * @param roomKey The key used to access data in roomHash
     */
	public void workOutRoomInfoContent(String roomKey) {
		
		//roomInfoPanel.removeAll();
		roomInfoBuildFurniScrollPanel.removeAll();
		roomInfoBuildFurniScrollPanel.setLayout(new GridLayout(roomHash.get(roomKey).getFurniSpots(), 1, 0, 0));

		//add hotspot buttons
		Hotspot[] hotspotArray = roomHash.get(roomKey).getHotspots();
		for(int i = 0; i < roomHash.get(roomKey).getHotspots().length; i++){
			roomInfoBuildFurniScrollPanel.add(hotspotArray[i].getHotspotButton());
			hotspotArray[i].getHotspotButton().addMouseListener(this);
		}
		
		setRoomInfoPanelContent();
		
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
	/**
	 * Calculates and shows the info about a room when you're viewing it.
	 * (the stats, not the Furni Build Buttons)
	 */
	public void setRoomInfoPanelContent(){
		//set room name label text
		roomInfoRoomTypeLabel.setText(roomHash.get(viewingRoom).getRoomType() + " Stats");
	
		//work out the rest of the labels
		roomInfoLevelLabel.setText("Required level: " + Integer.toString(roomHash.get(viewingRoom).getRoomLevel()));
		roomInfoRoomCostLabel.setText("Room Cost: " + Integer.toString(roomHash.get(viewingRoom).getRoomCost()) + "gp");
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
		for(int i = 0; i < roomHash.get(viewingRoom).getHotspots().length; i++){
			//work out what it should say
			furniStatusHash.get( i ).getStatusLabel().setText(
					roomHash.get(viewingRoom).getHotspots()[i].getHotspotType() + ": " +
					roomHash.get(viewingRoom).getHotspots()[i].getBuiltFurniture().getFurniType());
			//set layout stuff
			c.gridy = (i+1);
			if(i == (roomHash.get(viewingRoom).getHotspots().length - 1)){
				c.weighty = 1.0;
			}
			//add the labels
			roomInfoOverviewPanel.add(furniStatusHash.get( i ).getStatusLabel(), c);
			//if there's furniture here in a spot, say so
			if(roomHash.get(viewingRoom).getHotspots()[i].getBuiltFurniture().getFurniType() != "None"){
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

					if(roomHash.get( hashKey ).getRoomType().equals("")
							||roomHash.get( hashKey ).getRoomType().equals(" ")
								||roomHash.get( hashKey ).getRoomType().equals("  ")){
						continue;
					}
					else {
						//increases number of rooms
						roomsAndCost[0]++;
						//increases room cost
						roomsAndCost[1] += roomHash.get( hashKey ).getRoomCost();
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
		
		for(int j = 0; j < furni.getMaterials().length; j++){
			cost += (furni.getMaterials()[j].getGeMidPrice() * furni.getMaterialNumbers()[j]);
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
	 * @param roomKey The key used to access data in roomHash
	 * @return xp (int) The xp gained by building the furniture in a room.
	 */
	public int calculateRoomFurniXP(String roomKey){
		int xp = 0;
		//get hotspot status
		Hotspot[] room = new Hotspot[roomHash.get(roomKey).getHotspots().length];
		room = roomHash.get(roomKey).getHotspots();
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
	 * @param roomKey The key used to access data in roomHash
	 * @return cost (int) The cost of the furniture within a room based on GE prices.
	 */
	public int calculateRoomFurniCost(String roomKey){
		int cost = 0;
		//get hotspot status
		Hotspot[] room = new Hotspot[roomHash.get(roomKey).getHotspots().length];
		room = roomHash.get(roomKey).getHotspots();
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
		
		String hashKey = "";
		String code = "";
		
		code += ("001,"+username+",");
		
		//loops through rooms
		for(int i = 0;i < 3;i++){
			for(int j = 0;j < 9; j++){
				for(int k = 0;k < 9; k++){
					//set key
					hashKey = "overviewRoom" + Integer.toString(i) + Integer.toString(j) + Integer.toString(k);

					//if there's no room, we don't care
					if(roomHash.get( hashKey ).getRoomType().equals("")
							||roomHash.get( hashKey ).getRoomType().equals(" ")
								||roomHash.get( hashKey ).getRoomType().equals("  ")){
						continue;
					}
					else {
						//add the ID of the room
						code += (Integer.toString(i) + Integer.toString(j) + Integer.toString(k));
						
						//add the type of room
						code += roomHash.get(hashKey).getRoomID();
						
						//loop through hotspots in the room and add the active furniture
						for(int l = 0; l < roomHash.get( hashKey ).getHotspots().length; l++){
							code += roomHash.get(hashKey).getHotspots()[l].getBuiltFurniture().getFurniID();
						}
						
						//add a separator to indicate the start of the next room
						code += ",";
					}
				}
			}
		}
		
		menuHouseCodeTextbox.setText(code);
	}
	/**
	 * Loads a house from the sharing code
	 */
	public void loadHouseFromSharingCode(){
		
		String code = menuHouseCodeTextbox.getText();
        String[] codePieces = code.split( "," );
        String hashKey = "";
        String roomType = "";
        int floor = 0;
        String furniType = "";
        
        //reset house so it's nice and empty
        setEmptyRoomArray();
        
        //set username
        setUsername(codePieces[1]);
        
        /*for(int i = 0; i < codePieces.length; i++){
        	System.out.println("code: " + codePieces[i]);
        }*/
        
        //go through the rest of the pieces and load the rooms
        for(int i = 2; i < codePieces.length; i++){
        	//work out the room location
        	hashKey = "overviewRoom" + codePieces[i].substring(0, 3);
        	floor = Integer.parseInt(codePieces[i].substring(0, 1));
        	
        	//work out the room type
        	roomType = codePieces[i].substring(3, 5);
        	
        	//System.out.println("roomtype: " + roomType + " floor: " + floor + " location: " + hashKey);
        	
        	//build a new room of specified type in specified spot
        	roomLoadHash.put(hashKey, new Room(roomType, floor));
        	roomLoadHash.get( hashKey ).getRoomOverviewLabel().addMouseListener(this);
			roomLoadHash.get( hashKey ).getRoomOverviewLabel().setToolTipText(roomLoadHash.get( hashKey ).getRoomType());
		
        	//loop through the furniture and build it
        	for(int j = 0; j < roomLoadHash.get(hashKey).getHotspots().length; j++){
        		//work out the furni type
        		furniType = codePieces[i].substring((5+(j*2)), (7+(j*2)));
        		
        		//System.out.println("furnitype: " + furniType);
        		
        		//build that type of furni
        		roomLoadHash.get(hashKey).getHotspots()[j].setBuiltFurniture(new Furniture(furniType));
        	}
        }
        
        roomHash = roomLoadHash;
        initRightHouseOverviewPanel();
        createAndShowGrid(1);
        refreshGrid();
        //test code
        //001,Lostsamurai7,13403,1350200000000000000,1360204000000000000,1440201000000000000,14503,1460201000000000000,
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
        } else if(e.getSource().equals(menuHouseCodeTextbox) || e.getSource().equals(menuHouseCodeButton)){
        	loadHouseFromSharingCode();
        }
	    
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
		if (e.getSource() == rightViewGridButton) {
			
			if(viewingGrid)createAndShowRoom(lastViewedRoom);
			else if(!viewingGrid) createAndShowGrid(1);
			
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
			
			//hide building pane
			roomBuildingFrame.setVisible(false);
			
			String roomType = calculatePressedBuildButtonBasedOnName(e.getSource().toString());

			//build the selected room
			buildRoom(overviewSelectedRoom, roomType, overviewViewingLevel);

			overviewSelectedRoom = "";
		} else if(e.getSource().toString().contains("Build Furniture:")) {
			String furniType = calculatePressedBuildButtonBasedOnName(e.getSource().toString());

			createFurniBuildingFrameContent(furniType);

		} //rotate room
		else if(e.getSource() == rightRoomInfoRotateAntiClockwise){
			roomHash.get( overviewSelectedRoom ).rotateRoomCcw();
			roomHash.get( overviewSelectedRoom ).getRoomOverviewLabel().addMouseListener(this);
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		} else if(e.getSource() == rightRoomInfoRotateClockwise){
			roomHash.get( overviewSelectedRoom ).rotateRoomCw();
			roomHash.get( overviewSelectedRoom ).getRoomOverviewLabel().addMouseListener(this);
			refreshGrid();
			createAndShowGrid(overviewViewingLevel);
		} else if(e.getSource() == rightRoomInfoDeleteRoom){
			demolishRoom();
		}//this can almost certainly be writen more effiently, but it's late and I can't think :P
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
		} else if(e.getSource() == viewStatsSaveButton){
			viewStatsFrame.setVisible(false);
		} else if(e.getSource() == menuHouseCodeButton){
			loadHouseFromSharingCode();
		}
		else {
			//System.out.println(e);
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
                    Integer.parseInt( linePieces[2] ) 
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
            if(username.substring(0, 3).equalsIgnoreCase("mod")){
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
	            //update with the new username
	            calculateSharingCode();
	            rightGeneralToolsUpdateUsername.setText( "Fetch My Exp!" );
	            rightGeneralToolsUpdateUsername.setEnabled( true );
	            
	            if( result != "ok" ) {
	                JOptionPane.showMessageDialog( null, result, "An Error Occurred", 
	                        JOptionPane.ERROR_MESSAGE );
	            }
	            else {            
	                //update various things to do with the highscores
	            	HiscoreData currentSkill = hiscoreList.get( 23 );
	            	
	            	//System.out.println("Username: " + username + " Con level: " + currentSkill.getLevel());
	            }
            }
        }
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
}

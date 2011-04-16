package pohplanner;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;

public class Room {
	private String type;
	private String labelText;
	private int level;
	private int cost;
	private boolean[] doorLayout;
	private int furniSpots;
	private Hotspot[] hotspots;
	private int floor;
	private boolean selected;
	private String imageURL;
	private String roomImageURL;
	private String roomImage;
	private JLabel roomLabel;
	private String ID;
	private Utilities util;
	
	public Room( Room room, int houseFloor ){
		this.floor = houseFloor;
		this.type = room.type;
		this.util = room.util;
		this.labelText = room.labelText;
        this.level = room.level;
        this.cost = room.cost;
        this.doorLayout = room.doorLayout;
        this.hotspots = room.hotspots;
        this.roomImageURL = room.roomImageURL;
        this.ID = room.ID;
        this.imageURL = calculateRoomDoors();
        this.selected = false;
		
        
        if(room.type.contains("EmptyFloor1") || room.type.contains("EmptyDungeon")){
			this.type = "";
		} else if(room.type.contains("AboveUpstairs")){
			this.type = " ";
		} else if(room.type.contains("EmptyUpstairs")){
			this.type = "  ";
		}      
        
        this.furniSpots = this.hotspots.length;
		createRoomLabel();
		
		if(this.type != ""){
			setRoomImage();
		}
	}
	
	public Room( String type, String labelText, /*int houseFloor,*/ int level, Utilities util,
			int cost, boolean[] doorLayout, Hotspot[] hotspots, String roomImageURL, String id ){
		this.type = type;
		//this.floor = houseFloor;
		this.util = util;
		this.labelText = labelText;
        this.level = level;
        this.cost = cost;
        this.doorLayout = doorLayout;
        this.hotspots = hotspots;
        this.roomImageURL = roomImageURL;
        this.ID = id;
        this.imageURL = calculateRoomDoors();
        this.selected = false;
        
        if(type.contains("EmptyFloor1") || type.contains("EmptyDungeon")){
			this.type = "";
		} else if(type.contains("AboveUpstairs")){
			this.type = " ";
		} else if(type.contains("EmptyUpstairs")){
			this.type = "  ";
		}      
        
        this.furniSpots = this.hotspots.length;
		//createRoomLabel();
		
		//if(this.type != ""){
		//	setRoomImage();
		//}
		
		/*if(type.contains("EmptyFloor1") || type.contains("00")){
			initEmptyGroundFloor();
		} else if(type.contains("EmptyDungeon") || type.contains("01")){
			initEmptyDungeon();
		} else if(type.contains("AboveUpstairs") || type.contains("02")){
			initAboveUpstairs();
		} else if(type.contains("EmptyUpstairs") || type.contains("03")){
			initEmptyUpstairs();
		} else if(type.contains("Formal Garden") || type.contains("18")){
			initFormalGarden();
		} else if(type.contains("Garden") || type.contains("04")){
			initGarden();
		} else if(type.contains("Parlour") || type.contains("05")){
			initParlour();
		} else if(type.contains("Kitchen") || type.contains("06")){
			initKitchen();
		} else if(type.contains("Dining Room") || type.contains("07")){
			initDiningRoom();
		} else if(type.contains("Workshop") || type.contains("08")){
			initWorkshop();
		} else if(type.contains("Bedroom") || type.contains("09")){
			initBedroom();
		} else if(type.contains("Skill Hall") || type.contains("10")){
			initSkillHall();
		} else if(type.contains("Games Room") || type.contains("11")){
			initGamesRoom();
		} else if(type.contains("Combat Room") || type.contains("12")){
			initCombatRoom();
		} else if(type.contains("Quest Hall") || type.contains("13")){
			initQuestHall();
		} else if(type.contains("Study") || type.contains("14")){
			initStudy();
		} else if(type.contains("Costume Room") || type.contains("15")){
			initCostumeRoom();
		} else if(type.contains("Chapel") || type.contains("16")){
			initChapel();
		} else if(type.contains("Portal Chamber") || type.contains("17")){
			initPortalChamber();
		} else if(type.contains("Throne Room") || type.contains("19")){
			initThroneRoom();
		} else if(type.contains("Oubliette") || type.contains("20")){
			initOubliette();
		} else if(type.contains("Dungeon Corridor") || type.contains("21")){
			initDungeonCorridor();
		} else if(type.contains("Dungeon Junction") || type.contains("22")){
			initDungeonJunction();
		} else if(type.contains("Dungeon Stairs") || type.contains("23")){
			initDungeonStairs();
		} else if(type.contains("Treasure Room") || type.contains("24")){
			initTreasureRoom();
		} else {
			
		}*/
		
		/*else if(type.contains("") || type.contains("")){
			init();
		} */

    }
	
	/*private void initEmptyGroundFloor(){
		this.type = "";
		this.labelText = "";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "00";
	}
	private void initEmptyDungeon(){
		this.type = "";
		this.labelText = "";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "01";
	}
	private void initAboveUpstairs(){
		this.type = " ";
		this.labelText = "";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "02";
	}
	private void initEmptyUpstairs(){
		this.type = "  ";
		this.labelText = "";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "03";
	}
	private void initGarden(){
		this.type = "Garden";
		this.labelText = "Garden";
        this.level = 1;
        this.cost = 1000;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[] {
        	new Hotspot("Centrepiece", 
        			new int[][]{ new int[]{ 268, 278, 278, 268, 268}, }, 
        			new int[][]{ new int[]{ 118, 118, 190, 190, 118}, }, 
        			new int[]{4} ),
        	new Hotspot("Tree", 
        			new int[][]{ new int[]{ 128, 190, 190, 128, 128}, }, 
        			new int[][]{ new int[]{ 10, 10, 90 , 90, 10}, }, 
        			new int[]{4} ),
        	new Hotspot("Big Tree", 
        			new int[][]{ new int[]{ 146, 222, 222, 146, 146}, }, 
        			new int[][]{ new int[]{ 168, 168, 262, 262, 168}, }, 
        			new int[]{4} ),
        	new Hotspot("Small Plant 1", 
        			new int[][]{ new int[]{ 196, 234, 234, 196, 196}, }, 
        			new int[][]{ new int[]{ 125, 125, 160, 160, 125}, }, 
        			new int[]{4} ),
        	new Hotspot("Small Plant 2", 
        			new int[][]{ new int[]{ 352, 392, 392, 352, 352}, }, 
        			new int[][]{ new int[]{ 158, 158, 195, 195, 158}, }, 
        			new int[]{4} ),
        	new Hotspot("Big Plant 1", 
        			new int[][]{ new int[]{ 370, 452, 452, 370, 370}, }, 
        			new int[][]{ new int[]{ 238, 238, 316, 316, 238}, }, 
        			new int[]{4} ),
        	new Hotspot("Big Plant 2", 
        			new int[][]{ new int[]{ 351, 406, 406, 351, 351}, }, 
        			new int[][]{ new int[]{ 24, 24, 89, 89, 24}, }, 
        			new int[]{4} ),
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "1Garden";
        this.ID = "04";
	}
	private void initParlour(){
		this.type = "Parlour";
		this.labelText = "Parlour";
        this.level = 1;
        this.cost = 1000;
        this.doorLayout = new boolean[]{false, true, true, true};
        this.hotspots = new Hotspot[]{
        	new Hotspot("Chairs", 
        			new int[][]{new int[]{ 182, 210, 215, 198, 182, 182},}, 
        			new int[][]{ new int[]{108, 125, 145, 160, 146, 108},}, 
        			new int[]{5}),
        	new Hotspot("Chairs", 
        			new int[][]{new int[]{ 277, 300, 300, 274, 274, 277},}, 
        			new int[][]{ new int[]{149, 149, 186, 186, 164, 149},}, 
        			new int[]{5}),
        	new Hotspot("Chairs", 
        			new int[][]{new int[]{ 346, 343, 329, 313, 318, 346},}, 
        			new int[][]{ new int[]{111, 149, 158, 147, 126, 111},}, 
        			new int[]{5}),
        	new Hotspot("Rugs", 
        			new int[][]{new int[]{ 202, 325, 338, 189, 202},}, 
        			new int[][]{ new int[]{115, 115, 216, 216, 115},}, 
        			new int[]{4}),
        	new Hotspot("Fireplace", 
        			new int[][]{new int[]{ 231, 295, 295, 231, 231},}, 
        			new int[][]{ new int[]{25, 25, 83, 83, 25},}, 
        			new int[]{4}),
        	new Hotspot("Curtains",
        			new int[][]{ new int[]{ 192, 218, 219, 196, 192},	new int[]{ 310, 336, 333, 310, 310},new int[]{ 435, 443, 432, 424, 435},new int[]{ 472, 484, 468, 457, 472},new int[]{ 344, 377, 374, 343, 344},new int[]{ 153, 185, 186, 156, 153},new int[]{ 57, 73, 62, 47, 57},		new int[]{ 95, 104, 98, 85, 95},},
        			new int[][]{ new int[]{ 4, 4, 38, 37, 4}, 			new int[]{ 4, 4, 37, 37, 4}, 		new int[]{ 50, 68, 95, 81, 50},		new int[]{ 144, 170, 194, 176, 144},new int[]{ 287, 287, 301, 300, 287},new int[]{ 285, 285, 299, 298, 285},new int[]{ 141, 176, 193, 168, 141},new int[]{ 49, 80, 98, 68, 49},},
        			new int[]{ 4, 4, 4, 4, 4, 4, 4, 4}),
        	new Hotspot("Bookcases",
                	new int[][]{ new int[]{ 46, 74, 94, 85, 48, 29, 46}, 		new int[]{ 456, 484, 502, 479, 443, 434, 456}, },
                	new int[][]{ new int[]{ 182, 182, 231, 264, 264, 227, 182}, new int[]{ 186, 186, 232, 267, 267, 232, 186}, },
                	new int[]{ 6, 6}),
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "1Parlour";
        this.ID = "05";
	}
	private void initKitchen(){
		this.type = "Kitchen";
		this.labelText = "Kitchen";
        this.level = 5;
        this.cost = 5000;
        this.doorLayout = new boolean[]{false, true, true, false};
        this.hotspots = new Hotspot[]{
        	
        	new Hotspot("Kitchen Table",
            		new int[][]{ new int[]{ 199, 276, 276, 194, 199}, },
            		new int[][]{ new int[]{ 82, 82, 157, 157, 82}, },
            		new int[]{ 4}),
            new Hotspot("Larder",
                   	new int[][]{ new int[]{ 382, 386, 372, 317, 314, 317, 382}, },
                   	new int[][]{ new int[]{ 0, 29, 71, 73, 15, 0, 0}, },
                   	new int[]{ 6}),
            new Hotspot("Sink",
                	new int[][]{ new int[]{ 275, 274, 209, 203, 226, 226, 275}, },
                	new int[][]{ new int[]{ 16, 45, 45, 0, 0, 16, 16}, },
                	new int[]{ 6}),
            new Hotspot("Shelf",
                	new int[][]{ new int[]{ 98, 113, 108, 92, 98},	new int[]{ 68, 86, 78, 60, 68}, },
                	new int[][]{ new int[]{ 6, 12, 43, 40, 6},		new int[]{ 170, 173, 214, 214, 170}, },
                	new int[]{ 4, 4}),
            new Hotspot("Stove",
                	new int[][]{ new int[]{ 95, 131, 131, 95, 95}, },
                	new int[][]{ new int[]{ 97, 97, 145, 145, 97}, },
                	new int[]{ 4}),
            new Hotspot("Barrel",
                	new int[][]{ new int[]{ 101, 152, 153, 101, 101}, },
                	new int[][]{ new int[]{ 213, 213, 245, 245, 213}, },
                	new int[]{ 4}),
            new Hotspot("Cat Basket",
            		new int[][]{ new int[]{ 351, 389, 389, 351, 351}, },
                	new int[][]{ new int[]{ 232, 232, 248, 248, 232}, },
                	new int[]{ 4}),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "5Kitchen";
        this.ID = "06";
	}
	private void initDiningRoom(){
		this.type = "Dining Room";
		this.labelText = "<html>Dining<br />Room</html>";
        this.level = 10;
        this.cost = 5000;
        this.doorLayout = new boolean[]{true, false, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "10DiningRoom";
        this.ID = "07";
	}
	private void initWorkshop(){
		this.type = "Workshop";
		this.labelText = "Workshop";
        this.level = 15;
        this.cost = 10000;
        this.doorLayout = new boolean[]{false, true, false, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "15Workshop";
        this.ID = "08";
	}
	private void initBedroom(){
		this.type = "Bedroom";
		this.labelText = "Bedroom";
        this.level = 20;
        this.cost = 10000;
        this.doorLayout = new boolean[]{true, true, false, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "20Bedroom";
        this.ID = "09";
	}
	private void initSkillHall(){
		this.type = "Skill Hall";
		this.labelText = "Skill Hall";
        this.level = 25;
        this.cost = 15000;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "25SkillHall";
        this.ID = "10";
	}
	private void initGamesRoom(){
		this.type = "Games Room";
		this.labelText = "<html>Games<br />Room</htm>";
        this.level = 30;
        this.cost = 25000;
        this.doorLayout = new boolean[]{false, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "30GamesRoom";
        this.ID = "11";
	}
	private void initCombatRoom(){
		this.type = "Combat Room";
		this.labelText = "<html>Combat<br />Room</html>";
        this.level = 32;
        this.cost = 25000;
        this.doorLayout = new boolean[]{false, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "32CombatRoom";
        this.ID = "12";
	}
	private void initQuestHall(){
		this.type = "Quest Hall";
		this.labelText = "<html>Quest<br/>Hall</html>";
        this.level = 35;
        this.cost = 25000;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "35QuestHall";
        this.ID = "13";
	}
	private void initStudy(){
		this.type = "Study";
		this.labelText = "Study";
        this.level = 40;
        this.cost = 50000;
        this.doorLayout = new boolean[]{false, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "40Study";
        this.ID = "14";
	}
	private void initCostumeRoom(){
		this.type = "Costume Room";
		this.labelText = "<html>Costume<br />Room</html>";
        this.level = 42;
        this.cost = 50000;
        this.doorLayout = new boolean[]{false, true, false, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "40Study";
        this.ID = "15";
	}
	private void initChapel(){
		this.type = "Chapel";
		this.labelText = "Chapel";
        this.level = 45;
        this.cost = 50000;
        this.doorLayout = new boolean[]{true, true, false, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "45Chapel";
        this.ID = "16";
	}
	private void initPortalChamber(){
		this.type = "Portal Chamber";
		this.labelText = "<html>Portal<br />Chamber</html>";
        this.level = 50;
        this.cost = 100000;
        this.doorLayout = new boolean[]{false, true, false, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "50PortalChamber";
        this.ID = "17";
	}
	private void initFormalGarden(){
		this.type = "Formal Garden";
		this.labelText = "<html>Formal<br />Garden</html>";
        this.level = 55;
        this.cost = 75000;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "55FormalGarden";
        this.ID = "18";
	}
	private void initThroneRoom(){
		this.type = "Throne Room";
		this.labelText = "<html>Throne<br />Room</html>";
        this.level = 60;
        this.cost = 150000;
        this.doorLayout = new boolean[]{false, false, true, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "60ThroneRoom";
        this.ID = "19";
	}
	private void initOubliette(){
		this.type = "Oubliette";
		this.labelText = "Oubliette";
        this.level = 65;
        this.cost = 150000;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "65Oubliette";
        this.ID = "20";
	}
	private void initDungeonCorridor(){
		this.type = "Dungeon Corridor";
		this.labelText = "<html>Dungeon<br /><br />Corridor</html>";
        this.level = 70;
        this.cost = 7500;
        this.doorLayout = new boolean[]{true, false, true, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "70DungeonCorridor";
        this.ID = "21";
	}
	private void initDungeonJunction(){
		this.type = "Dungeon Junction";
		this.labelText = "Junction"; //<html>Dungeon<br /><br />Junction</html>
        this.level = 70;
        this.cost = 7500;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "70DungeonJunction";
        this.ID = "22";
	}
	private void initDungeonStairs(){
		this.type = "Dungeon Stairs";
		this.labelText = "<html>Dungeon<br />Stairs</html>";
        this.level = 70;
        this.cost = 7500;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "70DungeonStairs";
        this.ID = "23";
	}
	private void initTreasureRoom(){
		this.type = "Treasure Room";
		this.labelText = "<html>Treasure<br />Room</html>";
        this.level = 75;
        this.cost = 250000;
        this.doorLayout = new boolean[]{false, false, true, false};
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot(""),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "75TreasureRoom";
        this.ID = "24";
	}*/
	/*private void init(){
		this.type = "";
		this.labelText = "";
        this.level = ;
        this.cost = ;
        this.doorLayout = new boolean[]{, , , };
        this.hotspots = new Hotspot[]{
        	
		//	new Hotspot("",
        			new int[][]{ new int[]{ }, },
        			new int[][]{ new int[]{ }, },
        			new int[]{ }),
        	
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "";
        this.ID = "";
	}*/
	
	private void createRoomLabel(){
		this.roomLabel = new JLabel(labelText, util.loadImage( "roomIcons/" + getImageURL() + ".png" ), JLabel.CENTER);
        this.roomLabel.setIconTextGap(-55);
        this.roomLabel.setForeground(Color.WHITE);
        //stops long roomnames breaking everything
        this.roomLabel.setPreferredSize(new Dimension(57,57));
	}
	
	private String calculateRoomDoors(){
		
		String imageURL = "";
		
		if(this.floor == 0 && this.type.equals("")
				|| this.type.contains("Dungeon")
					|| this.type.equals("Treasure Room")){
			imageURL += "d";
		} else if(this.floor == 2 && this.type.equals(" ")){
			imageURL += "u";
		}
		if(this.doorLayout[0] == true){
			imageURL += "n";
		}
		if(this.doorLayout[1] == true){
			imageURL += "e";
		}
		if(this.doorLayout[2] == true){
			imageURL += "s";
		}
		if(this.doorLayout[3] == true){
			imageURL += "w";
		}
		if(this.type.equals("") || this.type.equals(" ") || this.type.equals("  ")){
			if(imageURL.equals("d") || imageURL.equals("u")){
				imageURL += "empty";
			} else imageURL = "empty";
		} else if(this.type.contains("Garden")){
			imageURL += "garden";
		} else if(this.type.contains("Dungeon")){
			if(this.type.equals("Dungeon Junction")){
				imageURL += "j";
			} else if(this.type.equals("Dungeon Stairs")){
				imageURL += "s";
			}
		} else if(this.type.equals("Treasure Room")){
			imageURL += "t";
		}
		
		return imageURL;		
	}
	
	public String getRoomImage() {
	    return this.roomImage;
	}
	
	public void setRoomImage() {
	    this.roomImage = "roomPics/" + getRoomImageURL() + ".jpg";
	}

	
	public void printValues(){
		System.out.println("Type: "+type);
		System.out.println("Level: "+level);
		System.out.println("Cost: "+cost);
		if(doorLayout.length == 4){
			System.out.println("DoorLayout: "+doorLayout[0]+doorLayout[1]+doorLayout[2]+doorLayout[3]);
		}
		System.out.println("FurniSpots: "+furniSpots);
		System.out.println("Floor: "+floor);
		System.out.println("Selected: "+selected);
		System.out.println("imageURL: "+imageURL);
		System.out.println("roomImageURL: "+roomImageURL);
		System.out.println("roomLabel: "+roomLabel);
		//System.out.println("roomImageLabel: "+roomImageLabel);
	}
	
	public void rotateRoomCw(){
		//System.out.println("doorsLayout: " + doorLayout);
		//for(int i = 0; i < 4; i++){
		//	System.out.println(doorLayout[i]);
		//}
		boolean extra = this.doorLayout[3];
		this.doorLayout[3] = this.doorLayout[2];
		this.doorLayout[2] = this.doorLayout[1];
		this.doorLayout[1] = this.doorLayout[0];
		this.doorLayout[0] = extra;
		this.imageURL = calculateRoomDoors();
		//for(int i = 0; i < 4; i++){
		//	System.out.println(doorLayout[i]);
		//}
		createRoomLabel();
		this.roomLabel.setForeground(new Color(127, 2, 1));
	}
	public void rotateRoomCcw(){
		//System.out.println("doorsLayout: " + doorLayout);
		//for(int i = 0; i < 4; i++){
		//	System.out.println(doorLayout[i]);
		//}
		boolean extra = this.doorLayout[0];
		this.doorLayout[0] = this.doorLayout[1];
		this.doorLayout[1] = this.doorLayout[2];
		this.doorLayout[2] = this.doorLayout[3];
		this.doorLayout[3] = extra;
		this.imageURL = calculateRoomDoors();
		//for(int i = 0; i < 4; i++){
		//	System.out.println(doorLayout[i]);
		//}
		createRoomLabel();
		this.roomLabel.setForeground(new Color(127, 2, 1));
	}
	
	
	public String getRoomType(){
		return type;
	}
	public void setRoomType(String newRoomType){
		type = newRoomType;
	}
	public int getRoomLevel(){
		return level;
	}
	public void setRoomLevel(int newRoomLevel){
		level = newRoomLevel;
	}
	public int getRoomCost(){
		return cost;
	}
	public void setRoomCost(int newRoomCost){
		cost = newRoomCost;
	}
	public boolean[] getDoorLayout(){
		return doorLayout;
	}
	public void setDoorLayout(boolean[] newDoorLayout){
		this.doorLayout = newDoorLayout;
		this.imageURL = calculateRoomDoors();
		createRoomLabel();
	}
	public int getFurniSpots(){
		return furniSpots;
	}
	public void setFurniSpots(int newFurniSpots){
		furniSpots = newFurniSpots;
	}
	public Hotspot[] getHotspots(){
		return hotspots;
	}
	public void setHotspots(Hotspot[] newHotspots){
		hotspots = newHotspots;
	}
	public int getFloor(){
		return floor;
	}
	public void setFloor(int newFloor){
		floor = newFloor;
	}
	public boolean getIsSelected(){
		return selected;
	}
	public void setIsSelected(boolean newSelected){
		selected = newSelected;
		if(newSelected == false){
			this.roomLabel.setForeground(Color.WHITE);
		}
	}
	public String getImageURL(){
		return imageURL;
	}
	public void setImageURL(String newImageURL){
		imageURL = newImageURL;
	}
	public String getRoomImageURL(){
		return roomImageURL;
	}
	public void setRoomImageURL(String newRoomImageURL){
		roomImageURL = newRoomImageURL;
	}
	public JLabel getRoomOverviewLabel(){
		return roomLabel;
	}
	public void setRoomOverviewLabel(JLabel newRoomLabel){
		roomLabel = newRoomLabel;
	}
	/*public JLabel getRoomImageLabel(){
		return roomImageLabel;
	}
	public void setRoomImageLabel(JLabel newRoomImageLabel){
		roomImageLabel = newRoomImageLabel;
	}*/
	public String getRoomID(){
		return ID;
	}
	public void setRoomID(String newRoomID){
		ID = newRoomID;
	}
}
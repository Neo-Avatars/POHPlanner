package pohplanner;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Room {

	private String type;
	private int level;
	private int cost;
	private boolean[] doorLayout;
	private int furniSpots;
	private Hotspot[] hotspots;
	private int floor;
	private boolean selected;
	private String imageURL;
	private String roomImageURL;
	private JLabel roomLabel;
	private JLabel roomImageLabel;
	private String ID;
	
	public Room( String type, int houseFloor ) {
		
		this.floor = houseFloor;
		this.selected = false;
		
		if(type.contains("EmptyFloor1") || type.contains("00")){
			initEmptyGroundFloor();
		} else if(type.contains("EmptyDungeon") || type.contains("01")){
			initEmptyDungeon();
		} else if(type.contains("AboveUpsairs") || type.contains("02")){
			initAboveUpstairs();
		} else if(type.contains("EmptyUpstairs") || type.contains("03")){
			initEmptyUpstairs();
		} else if(type.contains("Garden") || type.contains("04")){
			initGarden();
		} else if(type.contains("Parlour") || type.contains("05")){
			initParlour();
		} else {
			
		}

		this.furniSpots = this.hotspots.length;
		createRoomLabel();
		
		if(this.type != ""){
			createRoomImageLabel();
		}
		
    }
	
	private void initEmptyGroundFloor(){
		this.type = "";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "00";
	}
	
	private void initEmptyDungeon(){
		this.type = "";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "01";
	}
	
	private void initAboveUpstairs(){
		this.type = " ";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "02";
	}
	
	private void initEmptyUpstairs(){
		this.type = "  ";
        this.level = 1;
        this.cost = 0;
        this.doorLayout = new boolean[]{false, false, false, false};
        this.hotspots = new Hotspot[0];
        this.imageURL = calculateRoomDoors();
        this.ID = "03";
	}
	
	private void initGarden(){
		this.type = "Garden";
        this.level = 1;
        this.cost = 1000;
        this.doorLayout = new boolean[]{true, true, true, true};
        this.hotspots = new Hotspot[] {
        	new Hotspot("Centrepiece"),
        	new Hotspot("Tree"),
        	new Hotspot("Big Tree"),
        	new Hotspot("Small Plant 1"),
        	new Hotspot("Small Plant 2"),
        	new Hotspot("Big Plant 1"),
        	new Hotspot("Big Plant 2"),
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "1Garden";
        this.ID = "04";
	}
	
	private void initParlour(){
		this.type = "Parlour";
        this.level = 1;
        this.cost = 1000;
        this.doorLayout = new boolean[]{false, true, true, true};
        this.hotspots = new Hotspot[]{
        	/*new Hotspot("Chairs"),
        	new Hotspot("Chairs"),
        	new Hotspot("Chairs"),
        	new Hotspot("Rugs"),
        	new Hotspot("Fireplace"),
        	new Hotspot("Curtains"),
        	new Hotspot("Bookcases"),*/
        };
        this.imageURL = calculateRoomDoors();
        this.roomImageURL = "1Parlour";
        this.ID = "05";
	}
	
	private void createRoomLabel(){
		this.roomLabel = new JLabel(type, new ImageIcon( "roomIcons/" + getImageURL() +".png" ) , JLabel.CENTER);
        this.roomLabel.setIconTextGap(-55);
        this.roomLabel.setForeground(Color.WHITE);
	}
	
	private void createRoomImageLabel(){
		this.roomImageLabel = new JLabel("", new ImageIcon( "roomPics/" + getRoomImageURL() + ".jpg" ) , JLabel.CENTER);
	}
	
	private String calculateRoomDoors(){
		
		String imageURL = "";
		
		if(this.floor == 0 && this.type.equals("")){
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
			imageURL += "empty";
		} else if(this.type.equals("Garden")){
			imageURL += "garden";
		}
		
		return imageURL;		
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
		System.out.println("roomImageLabel: "+roomImageLabel);
	}
	
	public void rotateRoomCw(){
		boolean extra = this.doorLayout[3];
		this.doorLayout[3] = this.doorLayout[2];
		this.doorLayout[2] = this.doorLayout[1];
		this.doorLayout[1] = this.doorLayout[0];
		this.doorLayout[0] = extra;
		this.imageURL = calculateRoomDoors();
		createRoomLabel();
		this.roomLabel.setForeground(new Color(127, 2, 1));
	}
	public void rotateRoomCcw(){
		boolean extra = this.doorLayout[0];
		this.doorLayout[0] = this.doorLayout[1];
		this.doorLayout[1] = this.doorLayout[2];
		this.doorLayout[2] = this.doorLayout[3];
		this.doorLayout[3] = extra;
		this.imageURL = calculateRoomDoors();
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
		doorLayout = newDoorLayout;
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
	public JLabel getRoomImageLabel(){
		return roomImageLabel;
	}
	public void setRoomImageLabel(JLabel newRoomImageLabel){
		roomImageLabel = newRoomImageLabel;
	}
	public String getRoomID(){
		return ID;
	}
	public void setRoomID(String newRoomID){
		ID = newRoomID;
	}
	
}
package pohplanner;

import javax.swing.JButton;

public class Hotspot {

	private String type;
	private Furniture[] furniTypes;
	private Furniture active;
	private JButton hotspotButton;
	private int[][] xCoords;
	private int[][] yCoords;
	private int[] numberOfPoints;
	
	public Hotspot( Hotspot hotspot, int[][] xCoords, int[][] yCoords, int[] points){
		this.type = hotspot.type;
		this.xCoords = xCoords;
		this.yCoords = yCoords;
		this.numberOfPoints = points;
		this.furniTypes = hotspot.furniTypes;
		this.active = hotspot.active;
		
		createHotspotButton();
	}
	
	public Hotspot( String type, Furniture[] furniTypes){
		this.type = type;
		this.furniTypes = furniTypes;
		this.active = new Furniture("None");
		
		//createHotspotButton();
	}
	    
		//make sure names later in the list do not contain something earlier on
		//eg. 'Big Tree' must come before 'Tree' otherwise it won't get there
		
		/*if(type.contains("Centrepiece")){
			initCentrepiece();
		} else if(type.contains("Big Tree")){
			initBigTree();
		} else if(type.contains("Tree")){
			initTree();
		} else if(type.contains("Small Plant 1")){
			initSmallPlant1();
		} else if(type.contains("Small Plant 2")){
			initSmallPlant2();
		} else if(type.contains("Big Plant 1")){
			initBigPlant1();
		} else if(type.contains("Big Plant 2")){
			initBigPlant2();
		} else if(type.contains("Chairs")){
			initChairs();
		} else if(type.contains("Rugs")){
			initRugs();
		} else if(type.contains("Fireplace")){
			initFireplace();
		} else if(type.contains("Curtains")){
			initCurtains();
		} else if(type.contains("Bookcases")){
			initBookcases();
		} else if(type.contains("Kitchen Table")){
			initKitchenTable();
		} else if(type.contains("Larder")){
			initLarder();
		} else if(type.contains("Sink")){
			initSink();
		} else if(type.contains("Shelf")){
			initShelf();
		} else if(type.contains("Stove")){
			initStove();
		} else if(type.contains("Barrel")){
			initBarrel();
		} else if(type.contains("Cat Basket")){
			initCatBasket();
		}*/

	
	/*private void initCentrepiece(){
		this.type = "Centrepiece";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Exit Portal"),
			new Furniture("Decorative Rock"),
			new Furniture("Pond"),
			new Furniture("Imp Statue"),
			new Furniture("Dungeon Entrance"),
		};
		this.active = new Furniture("None");
	}
	private void initTree(){
		this.type = "Tree";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Tree"),
			new Furniture("Nice Tree"),
			new Furniture("Oak Tree"),
			new Furniture("Willow Tree"),
			new Furniture("Maple Tree"),
			new Furniture("Yew Tree"),
			new Furniture("Magic Tree"),
		};
		this.active = new Furniture("None");
	}
	private void initBigTree(){
		this.type = "Big Tree";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Tree"),
			new Furniture("Nice Tree"),
			new Furniture("Oak Tree"),
			new Furniture("Willow Tree"),
			new Furniture("Maple Tree"),
			new Furniture("Yew Tree"),
			new Furniture("Magic Tree"),
		};
		this.active = new Furniture("None");
	}
	private void initSmallPlant1(){
		this.type = "Small Plant 1";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Plant"),
			new Furniture("Small Fern"),
			new Furniture("Fern"),
		};
		this.active = new Furniture("None");
	}
	private void initSmallPlant2(){
		this.type = "Small Plant 2";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Dock Leaf"),
			new Furniture("Thistle"),
			new Furniture("Reeds"),
		};
		this.active = new Furniture("None");
	}
	private void initBigPlant1(){
		this.type = "Big Plant 1";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Fern "), //space stops it getting confused with the other Fern
			new Furniture("Bush"),
			new Furniture("Tall Plant"),
		};
		this.active = new Furniture("None");
	}
	private void initBigPlant2(){
		this.type = "Big Plant 2";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Short Plant"),
			new Furniture("Large Leaf Bush"),
			new Furniture("Huge Plant"),
		};
		this.active = new Furniture("None");
	}
	
	private void initChairs(){
		this.type = "Chairs";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Crude Wooden Chair"),
			new Furniture("Wooden Chair"),
			new Furniture("Rocking Chair"),
			new Furniture("Oak Chair"),
			new Furniture("Oak Armchair"),
			new Furniture("Teak Armchair"),
			new Furniture("Mahogany Armchair"),
		};
		this.active = new Furniture("None");
	}
	
	private void initRugs(){
		this.type = "Rugs";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Brown Rug"),
			new Furniture("Rug"),
			new Furniture("Opulent Rug"),
		};
		this.active = new Furniture("None");
	}
	
	private void initFireplace(){
		this.type = "Fireplace";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Clay Fireplace"),
			new Furniture("Stone Fireplace"),
			new Furniture("Marble Fireplace"),
		};
		this.active = new Furniture("None");
	}
	
	private void initCurtains(){
		this.type = "Curtains";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Torn Curtains"),
			new Furniture("Curtains"),
			new Furniture("Opulent Curtains"),
		};
		this.active = new Furniture("None");
	}
	
	private void initBookcases(){
		this.type = "Bookcases";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Wooden Bookcase"),
			new Furniture("Oak Bookcase"),
			new Furniture("Mahogany Bookcase"),
		};
		this.active = new Furniture("None");
	}
	
	private void initKitchenTable(){
		this.type = "Kitchen Table";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Wood Table"),
			new Furniture("Oak Table"),
			new Furniture("Teak Table"),
		};
		this.active = new Furniture("None");
	}
	
	private void initLarder(){
		this.type = "Larder";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Wooden Larder"),
			new Furniture("Oak Larder"),
			new Furniture("Teak Larder"),
		};
		this.active = new Furniture("None");
	}
	
	private void initSink(){
		this.type = "Sink";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Pump and Drain"),
			new Furniture("Pump and Tub"),
			new Furniture("Sink"),
		};
		this.active = new Furniture("None");
	}
	
	private void initShelf(){
		this.type = "Shelf";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Wooden Shelves 1"),
			new Furniture("Wooden Shelves 2"),
			new Furniture("Wooden Shelves 3"),
			new Furniture("Oak Shelves 1"),
			new Furniture("Oak Shelves 2"),
			new Furniture("Teak Shelves 1"),
			new Furniture("Teak Shelves 2"),
		};
		this.active = new Furniture("None");
	}
	
	private void initStove(){
		this.type = "Stove";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Firepit"),
			new Furniture("Firepit with Hook"),
			new Furniture("Firepit with Pot"),
			new Furniture("Small Oven"),
			new Furniture("Large Oven"),
			new Furniture("Steel Range"),
			new Furniture("Fancy Range"),
		};
		this.active = new Furniture("None");
	}
	
	private void initBarrel(){
		this.type = "Barrel";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Beer Barrel"),
			new Furniture("Cider Barrel"),
			new Furniture("Asgarnian Ale"),
			new Furniture("Greenmans Ale"),
			new Furniture("Dragon Bitter"),
			new Furniture("Chefs Delight"),
		};
		this.active = new Furniture("None");
	}
	
	private void initCatBasket(){
		this.type = "Cat Basket";
		this.furniTypes = new Furniture[] {
			new Furniture("None"),
			new Furniture("Cat Blanket"),
			new Furniture("Cat Basket"),
			new Furniture("Cushioned Basket"),
		};
		this.active = new Furniture("None");
	}*/
	
	private void createHotspotButton(){
		this.hotspotButton = new JButton("Build Furniture: " + this.type);
	}
	
	public String getHotspotType(){
		return type;
	}
	public void setHotspotType(String newType){
		type = newType;
	}
	public Furniture[] getFurniTypes(){
		return furniTypes;
	}
	public void setFurniTypes(Furniture[] newFurniTypes){
		furniTypes = newFurniTypes;
	}
	public Furniture getBuiltFurniture(){
		return active;
	}
	public void setBuiltFurniture(Furniture newActive){
		active = newActive;
	}
	public JButton getHotspotButton(){
		return hotspotButton;
	}
	public void setHotspotButton(JButton newHotspotButton){
		hotspotButton = newHotspotButton;
	}
	
	public int[][] getXCoords() {
	    return this.xCoords;
	}
	
	public int[][] getYCoords() {
	    return this.yCoords;
	}
	
	public int[] getNumberOfPoints() {
	    return this.numberOfPoints;
	}
}

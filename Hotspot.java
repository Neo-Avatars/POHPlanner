import javax.swing.JButton;

public class Hotspot {

	private String type;
	private Object[] furniTypes;
	private Object active;
	private JButton hotspotButton;
	
	public Hotspot( String type ){
		
		if(type.contains("Centrepiece")){
			initCentrepiece();
		} else if(type.contains("Tree")){
			initTree();
		} else if(type.contains("Big Tree")){
			initBigTree();
		} else if(type.contains("Small Plant 1")){
			initSmallPlant1();
		} else if(type.contains("Small Plant 2")){
			initSmallPlant2();
		} else if(type.contains("Big Plant 1")){
			initBigPlant1();
		} else if(type.contains("Big Plant 2")){
			initBigPlant2();
		} 
		
		createHotspotButton();
	}
	
	private void initCentrepiece(){
		this.type = "Centrepiece";
		this.furniTypes = new Object[] {
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
		this.furniTypes = new Object[] {
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
		this.furniTypes = new Object[] {
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
		this.furniTypes = new Object[] {
			new Furniture("None"),
			new Furniture("Plant"),
			new Furniture("Small Fern"),
			new Furniture("Fern"),
		};
		this.active = new Furniture("None");
	}
	private void initSmallPlant2(){
		this.type = "Small Plant 2";
		this.furniTypes = new Object[] {
			new Furniture("None"),
			new Furniture("Dock Leaf"),
			new Furniture("Thistle"),
			new Furniture("Reeds"),
		};
		this.active = new Furniture("None");
	}
	private void initBigPlant1(){
		this.type = "Big Plant 1";
		this.furniTypes = new Object[] {
			new Furniture("None"),
			new Furniture("Fern"),
			new Furniture("Bush"),
			new Furniture("Tall Plant"),
		};
		this.active = new Furniture("None");
	}
	private void initBigPlant2(){
		this.type = "Big Plant 2";
		this.furniTypes = new Object[] {
			new Furniture("None"),
			new Furniture("Short Plant"),
			new Furniture("Large Leaf Bush"),
			new Furniture("Huge Plant"),
		};
		this.active = new Furniture("None");
	}
	
	private void createHotspotButton(){
		this.hotspotButton = new JButton("Build " + this.type);
		System.out.println("Active: " + active);
		System.out.println("Type: " + type);
	}
	
	public String getHotspotType(){
		return type;
	}
	public void setHotspotType(String newType){
		type = newType;
	}
	public Object[] getFurniTypes(){
		return furniTypes;
	}
	public void setFurniTypes(Object[] newFurniTypes){
		furniTypes = newFurniTypes;
	}
	public Object getBuiltFurniture(){
		return active;
	}
	public void setBuiltFurniture(Object newActive){
		active = newActive;
	}
	public JButton getHotspotButton(){
		return hotspotButton;
	}
	public void setHotspotButton(JButton newHotspotButton){
		hotspotButton = newHotspotButton;
	}
	
}

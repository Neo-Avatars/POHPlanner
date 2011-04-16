import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Furniture {

	private String type;
	private String furniURL;
	private Materials[] materials;
	private int[] materialNumbers;
	private JLabel imageLabel;
	private int level;
	
	public Furniture ( String type ) {
		
		if(type.contains("None")){
			initNone();
		} else if(type.contains("Exit Portal")){
			initExitPortal();
		} else if(type.contains("Decorative Rock")){
			initDecorativeRock();
		} else if(type.contains("Pond")){
			initPond();
		} else if(type.contains("Imp Statue")){
			initImpStatue();
		}  else if(type.contains("Dungeon Entrance")){
			initDungeonEntrance();
		}
		
		createImageLabel();
	}
	
	private void initNone(){
		this.type = "None";
		this.furniURL = "none/empty";
		this.materials = new Materials[0];
		this.materialNumbers = new int[0];
		this.level = 1;
	}
	private void initExitPortal(){
		this.type = "Exit Portal";
		this.furniURL = "centrepiece/exit_portal";
		this.materials = new Materials[]{
			new Materials("Iron Bar"),
		};
		this.materialNumbers = new int[]{ 10};
		this.level = 1;
	}
	private void initDecorativeRock(){
		this.type = "Decorative Rock";
		this.furniURL = "centrepiece/decorative_rock";
		this.materials = new Materials[]{
				new Materials("Limestone Brick"),
		};
		this.materialNumbers = new int[]{ 5};
		this.level = 5;
	}
	private void initPond(){
		this.type = "Pond";
		this.furniURL = "centrepiece/pond";
		this.materials = new Materials[]{
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 10};
		this.level = 10;
	}
	private void initImpStatue(){
		this.type = "Imp Statue";
		this.furniURL = "centrepiece/imp_statue";
		this.materials = new Materials[]{
				new Materials("Limestone Brick"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 5, 5};
		this.level = 15;
	}
	private void initDungeonEntrance(){
		this.type = "Dungeon Entrance";
		this.furniURL = "centrepiece/dungeon_entrance";
		this.materials = new Materials[]{
				new Materials("Marble Block"),
		};
		this.materialNumbers = new int[]{ 1};
		this.level = 70;
	}
	
	private void createImageLabel(){
		this.imageLabel = new JLabel(new ImageIcon( "furniIcons/" + getFurniURL() +".gif" ));
	}
	
	public String getFurniType(){
		return type;
	}
	public void setFurniType(String newType){
		type = newType;
	}
	public String getFurniURL(){
		return furniURL;
	}
	public void setFurniURL(String newFurniURL){
		furniURL = newFurniURL;
	}
	public Materials[] getMaterials(){
		return materials;
	}
	public void setMaterials(Materials[] newMaterials){
		materials = newMaterials;
	}
	public int[] getMaterialNumbers(){
		return materialNumbers;
	}
	public void setmaterialNumbers(int[] newmaterialNumbers){
		materialNumbers = newmaterialNumbers;
	}
	public JLabel getImageLabel(){
		return imageLabel;
	}
	public void setImageLabel(JLabel newImageLabel){
		imageLabel = newImageLabel;
	}
	public int getLevel(){
		return level;
	}
	public void setLevel(int newLevel){
		level = newLevel;
	}
}

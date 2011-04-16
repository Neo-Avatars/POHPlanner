import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Furniture {

	private String type;
	private String furniURL;
	private Object[] materials;
	private int[] materialNumbers;
	private JLabel imageLabel;
	
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
		this.materials = new Object[0];
		this.materialNumbers = new int[0];
	}
	private void initExitPortal(){
		this.type = "Exit Portal";
		this.furniURL = "centrepiece/exit_portal";
		this.materials = new Object[]{
			new Materials("Iron Bar"),
		};
		this.materialNumbers = new int[]{ 10};
	}
	private void initDecorativeRock(){
		this.type = "Decorative Rock";
		this.furniURL = "centrepiece/decorative_rock";
		this.materials = new Object[]{
				new Materials("Limestone Brick"),
		};
		this.materialNumbers = new int[]{ 5};
	}
	private void initPond(){
		this.type = "Pond";
		this.furniURL = "centrepiece/pond";
		this.materials = new Object[]{
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 10};
	}
	private void initImpStatue(){
		this.type = "Imp Statue";
		this.furniURL = "centrepiece/imp_statue";
		this.materials = new Object[]{
				new Materials("Limestone Brick"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 5, 5};
	}
	private void initDungeonEntrance(){
		this.type = "Dungeon Entrance";
		this.furniURL = "centrepiece/dungeon_entrance";
		this.materials = new Object[]{
				new Materials("Marble Block"),
		};
		this.materialNumbers = new int[]{ 1};
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
	public Object[] getMaterials(){
		return materials;
	}
	public void setMaterials(Object[] newMaterials){
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
	
}

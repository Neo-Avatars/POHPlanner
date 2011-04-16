import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Materials {
	
	private String type;
	private int geid;
	private int geMidPrice;
	private int experience;
	private String imageURL;
	private JLabel imageLabel;
	
	public Materials( String type ){
		
		if(type.contains("Iron Bar")){
			initIronBar();
		} else if(type.contains("Limestone Brick")){
			initLimestoneBrick();
		} else if(type.contains("Soft Clay")){
			initSoftClay();
		} else if(type.contains("Marble Block")){
			initMarbleBlock();
		}
		createImageLabel();
	}
	
	private void initIronBar(){
		this.type = "Iron Bar";
		this.geid = 2351;
		this.geMidPrice = 200;
		this.experience = 10;
		this.imageURL = "iron_bar";
	}
	private void initLimestoneBrick(){
		this.type = "Limestone Brick";
		this.geid = 3420;
		this.geMidPrice = 100;
		this.experience = 20;
		this.imageURL = "limestone_brick";
	}
	private void initSoftClay(){
		this.type = "Soft Clay";
		this.geid = 1761;
		this.geMidPrice = 170;
		this.experience = 10;
		this.imageURL = "soft_clay";
	}
	private void initMarbleBlock(){
		this.type = "Marble Block";
		this.geid = 8786;
		this.geMidPrice = 325000;
		this.experience = 500;
		this.imageURL = "marble_block";
	}
	
	private void createImageLabel(){
		this.imageLabel = new JLabel(new ImageIcon( "materialPics/" + getImageURL() +".gif" ));
	}
	
	public String getMaterialType(){
		return type;
	}
	public void setMaterialType(String newType){
		type = newType;
	}
	public int getGeid(){
		return geid;
	}
	public void setGeid(int newGeid){
		geid = newGeid;
	}
	public int getGeMidPrice(){
		return geMidPrice;
	}
	public void setGeMidPrice(int newGeMidPrice){
		geMidPrice = newGeMidPrice;
	}
	public int getExperience(){
		return experience;
	}
	public void setExperience(int newExperience){
		experience = newExperience;
	}
	public String getImageURL(){
		return imageURL;
	}
	public void setImageURL(String newImageURL){
		imageURL = newImageURL;
	}
	public JLabel getImageLabel(){
		return imageLabel;
	}
	public void setImageLabel(JLabel newImageLabel){
		imageLabel = newImageLabel;
	}
	
}

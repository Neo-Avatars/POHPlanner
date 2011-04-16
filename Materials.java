package pohplanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Materials {
	
	private String type;
	private int geid;
	private int geMidPrice;
	private int shopPrice;
	private int experience;
	private String imageURL;
	private String ID;
	private JLabel imageLabel;
	private int numberUsed;
	private JLabel materialLabel;
	
	public Materials( Materials material ) {
		
		this.type = material.type;
		this.geid = material.geid;
		this.geMidPrice = material.geMidPrice;
		this.shopPrice = material.shopPrice;
		this.experience = material.experience;
		this.imageURL = material.imageURL;
		this.ID = material.ID;
		this.imageLabel = material.imageLabel;
	}
	
	public Materials( String type, int geid, int geMidPrice, int shopPrice, int experience,
			String imageURL, String ID){
		
		this.type = type;
		this.geid = geid;
		this.geMidPrice = geMidPrice;
		this.shopPrice = shopPrice;
		this.experience = experience;
		this.imageURL = imageURL;
		this.ID = ID;
		createImageLabel();
		
		/*if(type.contains("Iron Bar")){
			initIronBar();
		} else if(type.contains("Limestone Brick")){
			initLimestoneBrick();
		} else if(type.contains("Soft Clay")){
			initSoftClay();
		} else if(type.contains("Marble Block")){
			initMarbleBlock();
		} else if(type.contains("Oak Plank")){
			initOakPlank();
		} else if(type.contains("Teak Plank")){
			initTeakPlank();
		} else if(type.contains("Mahogany Plank")){
			initMahoganyPlank();
		} else if(type.contains("Plank")){
			initPlank();
		} else if(type.contains("Nails")){
			initNails();
		} else if(type.contains("Cloth")){
			initCloth();
		} else if(type.contains("Gold Leaf")){
			initGoldLeaf();
		} else if(type.contains("Steel Bar")){
			initSteelBar();
		} else if(type.contains("Wool")){
			initWool();
		} 
		
		else if(type.contains("Cider")){
			initCider();
		} else if(type.contains("Asgarnian Ale")){
			initAsgarnianAle();
		} else if(type.contains("Greenmans Ale")){
			initGreenmansAle();
		} else if(type.contains("Dragon Bitter")){
			initDragonBitter();
		} else if(type.contains("Chefs Delight")){
			initChefsDelight();
		} else if(type.contains("Watering Can")){
			initWateringCan();
		} else if(type.contains("Bagged Dead Tree")){
			initBaggedDeadTree();
		} else if(type.contains("Bagged Nice Tree")){
			initBaggedNiceTree();
		} else if(type.contains("Bagged Oak Tree")){
			initBaggedOakTree();
		} else if(type.contains("Bagged Willow Tree")){
			initBaggedWillowTree();
		} else if(type.contains("Bagged Maple Tree")){
			initBaggedMapleTree();
		} else if(type.contains("Bagged Yew Tree")){
			initBaggedYewTree();
		} else if(type.contains("Bagged Magic Tree")){
			initBaggedMagicTree();
		} else if(type.contains("Bagged Plant 1")){
			initBaggedPlant1();
		} else if(type.contains("Bagged Plant 2")){
			initBaggedPlant2();
		} else if(type.contains("Bagged Plant 3")){
			initBaggedPlant3();
		}
		 else if(type.contains("")){
				init();
			}*/
	}
	
	/*private void initIronBar(){
		this.type = "Iron Bar";
		this.geid = 2351;
		this.geMidPrice = 250;
		this.shopPrice = 0;
		this.experience = 10;
		this.imageURL = "iron_bar";
	}
	private void initSteelBar(){
		this.type = "Steel Bar";
		this.geid = 2353;
		this.geMidPrice = 720;
		this.shopPrice = 0;
		this.experience = 20;
		this.imageURL = "steel_bar";
	}
	private void initCider(){
		this.type = "Cider";
		this.geid = 5763;
		this.geMidPrice = 200;
		this.shopPrice = 2;
		this.experience = 4;
		this.imageURL = "cider";
	}
	private void initAsgarnianAle(){
		this.type = "Asgarnian Ale";
		this.geid = 1905;
		this.geMidPrice = 80;
		this.shopPrice = 2;
		this.experience = 4;
		this.imageURL = "asgarnian_ale";
	}
	private void initGreenmansAle(){
		this.type = "Greenmans Ale";
		this.geid = 1909;
		this.geMidPrice = 200;
		this.shopPrice = 2;
		this.experience = 4;
		this.imageURL = "greenmans_ale";
	}
	private void initDragonBitter(){
		this.type = "Dragon Bitter";
		this.geid = 1911;
		this.geMidPrice = 450;
		this.shopPrice = 2;
		this.experience = 4;
		this.imageURL = "dragon_bitter";
	}
	private void initChefsDelight(){
		this.type = "Chefs Delight";
		this.geid = 5755;
		this.geMidPrice = 3400;
		this.shopPrice = 0;
		this.experience = 4;
		this.imageURL = "chefs_delight";
	}
	private void initWool(){
		this.type = "Wool";
		this.geid = 1737;
		this.geMidPrice = 140;
		this.shopPrice = 0;
		this.experience = 0;
		this.imageURL = "wool";
	}
	private void initLimestoneBrick(){
		this.type = "Limestone Brick";
		this.geid = 3420;
		this.geMidPrice = 100;
		this.shopPrice = 21;
		this.experience = 20;
		this.imageURL = "limestone_brick";
	}
	private void initSoftClay(){
		this.type = "Soft Clay";
		this.geid = 1761;
		this.geMidPrice = 170;
		this.shopPrice = 0;
		this.experience = 10;
		this.imageURL = "soft_clay";
	}
	private void initMarbleBlock(){
		this.type = "Marble Block";
		this.geid = 8786;
		this.geMidPrice = 324500;
		this.shopPrice = 325000;
		this.experience = 500;
		this.imageURL = "marble_block";
	}
	private void initPlank(){
		this.type = "Plank";
		this.geid = 960;
		this.geMidPrice = 200;
		this.shopPrice = 100;
		this.experience = 30;
		this.imageURL = "plankt";
	}
	private void initOakPlank(){
		this.type = "Oak Plank";
		this.geid = 8778;
		this.geMidPrice = 470;
		this.shopPrice = 250;
		this.experience = 60;
		this.imageURL = "oak_plank";
	}
	private void initTeakPlank(){
		this.type = "Teak Plank";
		this.geid = 8780;
		this.geMidPrice = 800;
		this.shopPrice = 500;
		this.experience = 90;
		this.imageURL = "teak_plank";
	}
	private void initMahoganyPlank(){
		this.type = "Mahogany Plank";
		this.geid = 8782;
		this.geMidPrice = 1790;
		this.shopPrice = 1500;
		this.experience = 120;
		this.imageURL = "mahogany_plank";
	}
	private void initNails(){
		this.type = "Nails";
		this.geid = 1539;
		this.geMidPrice = 25;
		this.shopPrice = 52;
		this.experience = 3;
		this.imageURL = "steel_nails";
	}
	private void initCloth(){
		this.type = "Bold of Cloth";
		this.geid = 8790;
		this.geMidPrice = 830;
		this.shopPrice = 650;
		this.experience = 15;
		this.imageURL = "cloth";
	}
	private void initGoldLeaf(){
		this.type = "Gold Leaf";
		this.geid = 8784;
		this.geMidPrice = 129900;
		this.shopPrice = 130000;
		this.experience = 300;
		this.imageURL = "gold_leaf";
	}
	
	
	private void initBaggedDeadTree(){
		this.type = "Bagged Dead Tree";
		this.geid = 8417;
		this.geMidPrice = 1070;
		this.shopPrice = 1000;
		this.experience = 31;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedNiceTree(){
		this.type = "Bagged Nice Tree";
		this.geid = 8419;
		this.geMidPrice = 2150;
		this.shopPrice = 2000;
		this.experience = 44;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedOakTree(){
		this.type = "Bagged Oak Tree";
		this.geid = 8421;
		this.geMidPrice = 5170;
		this.shopPrice = 5000;
		this.experience = 70;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedWillowTree(){
		this.type = "Bagged Willow Tree";
		this.geid = 8423;
		this.geMidPrice = 10200;
		this.shopPrice = 10000;
		this.experience = 100;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedMapleTree(){
		this.type = "Bagged Maple Tree";
		this.geid = 8425;
		this.geMidPrice = 15100;
		this.shopPrice = 15000;
		this.experience = 122;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedYewTree(){
		this.type = "Bagged Yew Tree";
		this.geid = 8427;
		this.geMidPrice = 20000;
		this.shopPrice = 20000;
		this.experience = 141;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedMagicTree(){
		this.type = "Bagged Magic Tree";
		this.geid = 8429;
		this.geMidPrice = 30400;
		this.shopPrice = 50000;
		this.experience = 223;
		this.imageURL = "bagged_plant";
	}
	private void initWateringCan(){
		this.type = "Watering Can";
		this.geid = 5331;
		this.geMidPrice = 86;
		this.shopPrice = 25;
		this.experience = 0;
		this.imageURL = "watering_can";
	}
	private void initBaggedPlant1(){
		this.type = "Bagged Plant 1";
		this.geid = 8431;
		this.geMidPrice = 1170;
		this.shopPrice = 1000;
		this.experience = 31;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedPlant2(){
		this.type = "Bagged Plant 2";
		this.geid = 8433;
		this.geMidPrice = 5200;
		this.shopPrice = 5000;
		this.experience = 70;
		this.imageURL = "bagged_plant";
	}
	private void initBaggedPlant3(){
		this.type = "Bagged Plant 3";
		this.geid = 8435;
		this.geMidPrice = 10200;
		this.shopPrice = 10000;
		this.experience = 100;
		this.imageURL = "bagged_plant";
	}
	
	private void init(){
		this.type = "";
		this.geid = ;
		this.geMidPrice = ;
		this.shopPrice = ;
		this.experience = ;
		this.imageURL = "";
	}*/
	
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
	public int getShopPrice(){
		return shopPrice;
	}
	public void setShopPrice(int newShopPrice){
		shopPrice = newShopPrice;
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
	public int getNumberUsed(){
		return numberUsed;
	}
	public void setNumberUsed(int newNumberUsed){
		numberUsed = newNumberUsed;
	}
	public JLabel getMaterialLabel(){
		return materialLabel;
	}
	public void setMaterialLabel(JLabel newMaterialLabel){
		materialLabel = newMaterialLabel;
	}
}

package pohplanner;

import javax.swing.JPanel;

public class FurniBuildPanel {

	private JPanel buildPanel = new JPanel();
	private Furniture furni;
	//private boolean builtInStatus;
	/*private JLabel furniTypeLabel = new JLabel();
	private JLabel furniImageLabel = new JLabel();
	private JLabel materialsLabel = new JLabel();
	private JLabel furniLevelReqLabel = new JLabel();*/
	
	public FurniBuildPanel(){

	}
	
	public JPanel getBuildPanel(){
		return buildPanel;
	}
	public void setBuildPanel(JPanel newBuildPanel){
		this.buildPanel = newBuildPanel;
	}
	public Furniture getFurni(){
		return furni;
	}
	public void setFurni(Furniture newFurni){
		this.furni = newFurni;
	}
	/*public boolean getBuiltInStatus(){
		return builtInStatus;
	}
	public void setBuiltInStatus(boolean newBuiltInStatus){
		this.builtInStatus = newBuiltInStatus;
	}*/
	/*public JLabel getFurniTypeLabel(){
		return furniTypeLabel;
	}
	public void setFurniTypeLabel(JLabel newFurniTypeLabel){
		this.furniTypeLabel = newFurniTypeLabel;
	}
	public JLabel getFurniImageLabel(){
		return furniImageLabel;
	}
	public void set(JLabel newFurniImageLabel){
		this.furniImageLabel = newFurniImageLabel;
	}
	public JLabel getMaterialsLabel(){
		return materialsLabel;
	}
	public void setMaterialsLabel(JLabel newMaterialsLabel){
		this.materialsLabel = newMaterialsLabel;
	}
	public JLabel getFurniLevelReqLabel(){
		return furniLevelReqLabel;
	}
	public void setFurniLevelReqLabel(JLabel newFurniLevelReqLabel){
		this.furniLevelReqLabel = newFurniLevelReqLabel;
	}*/
}

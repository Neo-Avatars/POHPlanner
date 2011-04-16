package pohplanner;

import javax.swing.JPanel;

public class FurniBuildPanel {

	private JPanel buildPanel = new JPanel();
	private Furniture furni;
	
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
}

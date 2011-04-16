package pohplanner;

import javax.swing.JButton;

public class FurniBuildButton {

	JButton buildButton;
	
	public FurniBuildButton(){
		this.buildButton = new JButton();
	}
	
	public JButton getBuildButton(){
		return buildButton;
	}
	public void setBuildButton(JButton newBuildButton){
		this.buildButton= newBuildButton;
	}
}

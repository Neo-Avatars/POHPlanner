package pohplanner;

import javax.swing.JLabel;

public class FurniStatusLabel {

	private JLabel statusLabel = new JLabel();

	public FurniStatusLabel(){

	}
	
	public JLabel getStatusLabel(){
		return statusLabel;
	}
	public void setStatusLabel(JLabel newStatusLabel){
		this.statusLabel = newStatusLabel;
	}

}

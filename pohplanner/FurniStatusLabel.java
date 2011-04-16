package pohplanner;

import javax.swing.JLabel;

public class FurniStatusLabel {

	private JLabel statusLabel = new JLabel();
	//private Furniture furni;

	public FurniStatusLabel(){

	}
	
	public JLabel getStatusLabel(){
		return statusLabel;
	}
	public void setStatusLabel(JLabel newStatusLabel){
		this.statusLabel = newStatusLabel;
	}
	/*public Furniture getFurni(){
		return furni;
	}
	public void setFurni(Furniture newFurni){
		this.furni = newFurni;
	}*/

}

package pohplanner;

import javax.swing.JCheckBox;



public class Quest {

	private JCheckBox questBox = new JCheckBox();
	
	public Quest(String questName){
		this.questBox.setText(questName);
	}
	
	public JCheckBox getQuestBox(){
		return questBox;
	}
	public void setQuestBox(JCheckBox newQuestBox){
		this.questBox = newQuestBox;
	}

}

package pohplanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class StatsPane {

	private JLabel skillLabel = new JLabel();
	private int skillID;
	private JTextField levelBox = new JTextField(3);

	public StatsPane(String skillName, int skillID){
		this.skillLabel.setText(skillName+": ");
		this.skillLabel.setIcon(new ImageIcon("skillIcons/" + skillName + ".gif"));
		this.skillLabel.setToolTipText("Set your " + skillName + " level");
		this.skillID = skillID;
		this.levelBox.setDocument(new TextFieldLimiter(2));
		this.levelBox.setText("1");
		this.levelBox.setToolTipText("Set your " + skillName + " level");
	}
	
	public JLabel getSkillLabel(){
		return skillLabel;
	}
	public void setSkillLabel(JLabel newSkillLabel){
		this.skillLabel = newSkillLabel;
	}
	public int getSkillID(){
		return skillID;
	}
	public void setSkillID(int newSkillID){
		this.skillID = newSkillID;
	}
	public JTextField getLevelBox(){
		return levelBox;
	}
	public void setLevelBox(JTextField newLevelBox){
		this.levelBox = newLevelBox;
	}
}

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;


public class codeWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] roomTypes = new String[] {"Garden", "Parlour", "Kitchen", "Dining Room", "Workshop", "Bedroom", "Skill Hall", "Games Room", "Combat Room", "Quest Hall", "Study", "Costume Room", "Chapel", "Portal Chamber", "Formal Garden", "Throne Room", "Oubliette", "Dungeon Corridor", "Dungeon Junction", "Dungeon Stairs", "Treasure Room"};
		String[] roomTypesNoSpaces = new String[] {"Garden", "Parlour", "Kitchen", "DiningRoom", "Workshop", "Bedroom", "SkillHall", "GamesRoom", "CombatRoom", "QuestHall", "Study", "CostumeRoom", "Chapel", "PortalChamber", "FormalGarden", "ThroneRoom", "Oubliette", "DungeonCorridor", "DungeonJunction", "DungeonStairs", "TreasureRoom"};
		int[] levels = new int[] {1, 1, 5, 10, 15, 20, 25, 30, 32, 35, 40, 42, 45, 50, 55, 60, 65, 70, 70, 70, 75};
		int[] costs = new int[] {1000, 1000, 5000, 5000, 10000, 10000, 15000, 25000, 25000, 25000, 50000, 50000, 50000, 100000, 75000, 150000, 150000, 7500, 7500, 7500, 250000 };
		String newWord = "";
		char c;
		String labeltxt;

		String[] roomTypesLower = new String[21];
		
		for(int i = 0; i < roomTypesNoSpaces.length; i++){
			for(int j = 0; j < roomTypesNoSpaces[i].length(); j++){
				c = roomTypesNoSpaces[i].charAt(j);
				c = Character.toLowerCase(c);
				newWord += c;
			}
			roomTypesLower[i] = newWord;
			newWord = "";
		}
		
		for(int i = 0; i < roomTypes.length; i++){
			System.out.println("private JButton buildButton" + roomTypesNoSpaces[i] + " = new JButton();");
		}
		System.out.println();
		//System.out.println("private int buildIconSpace = 20;");
		//System.out.println();
		for(int i = 0; i < 21; i++){
			labeltxt = "\"Cost: " + costs[i] + "gp  Requires Level: " + levels[i] + "\"";
			System.out.println("private JLabel buildIcon" + roomTypesNoSpaces[i] + " = new JLabel(" + labeltxt + ", new ImageIcon(\"buildIcons/" + roomTypesLower[i] + ".gif\"), JLabel.CENTER);");
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		for(int j = 0; j < roomTypes.length; j++){
			System.out.println("buildButton" + roomTypesNoSpaces[j] + ".setText(\"Build a " + roomTypes[j] + "\");");
		}
		System.out.println();
		for(int k = 0; k < roomTypes.length; k++){
			System.out.println("buildButton" + roomTypesNoSpaces[k] + ".addMouseListener(this);");
		}
		//for(int k = 0; k < roomTypes.length; k++){
		//	System.out.println("roomBuildingScrollPanel.add(buildButton" + roomTypesNoSpaces[k] + ");");
		//}
		System.out.println();
		System.out.println("int buildIconSpace = 20;");
		System.out.println();
		for(int i = 0; i < roomTypes.length; i++){
			System.out.println("buildIcon" + roomTypesNoSpaces[i] + ".setIconTextGap(buildIconSpace);");
		}
		
		System.out.println();
		for(int i = 0; i < 21; i++){
			System.out.println("roomBuildingScrollPanel.add(buildIcon" + roomTypesNoSpaces[i] + ");");
			System.out.println("roomBuildingScrollPanel.add(buildButton" + roomTypesNoSpaces[i] + ");");
			
		}
	}

}

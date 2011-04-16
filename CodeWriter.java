package pohplanner;

public class CodeWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String houses = "";
		
		for(int i = 0; i < 3; i++){
			houses += "["; 
			for(int j = 0; j < 9; j++){
				houses += "["; 
				for(int l = 0; l < 9; l++){
					houses += "new room(roomsArray[0].type,roomsArray[0].labelText,roomsArray[0].level,roomsArray[0].cost,roomsArray[0].doorLayout,roomsArray[0].imgURL)";
					if(l < 8){
						houses += ",";
					}
				}
				houses += "]";
			}
			houses += "]";
		}
		
		System.out.println(houses);
	}

}

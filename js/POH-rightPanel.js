/*-DROP-DOWN MENU */

/*Shows the floor that has been selected by the dropdown */
function showSelectedOverviewFloor(){
	$('#' + determineIDOfFloor($house.visibleOverviewFloor)).show();
}

/*Hides the floor that has been selected by the dropdown */
function hideSelectedOverviewFloor(){
	$('#' + determineIDOfFloor($house.visibleOverviewFloor)).hide();
}

/*-HOUSE STATS */

/*Updates the House Stats Array and visible representation of it when a room is built or demolished 
Build - true if building, false if demolishing */
function updateHouseStatsContent(build, i, j, k){
	if(build){
		$house.builtRooms++; //say that you've built it
		$house.totalRoomCost += houseArray[i][j][k].cost; //inc room cost
	} else {
		$house.builtRooms--; //say that you've demolished it
		$house.totalRoomCost -= houseArray[i][j][k].cost; //reduce room cost
	}

	updateNumberOfRooms();
	updateRoomsCost();
	updateTotalCost();
}

/*Updates the number of rooms */
function updateNumberOfRooms(){
	$('#houseStatsTable tr:eq(1) td:eq(1) span:eq(0)').html($house.builtRooms +' / '+ $house.maxRooms);
}

/*Updates the cost of rooms */
function updateRoomsCost(){
	$('#houseStatsTable tr:eq(2) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($house.totalRoomCost));
}

/*Updates the cost of furniture */
function updateFurnitureCost(){
	$('#houseStatsTable tr:eq(3) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($house.totalFurniCost));
}

/*Updates the experience given by furniture */
function updateFurnitureExp(){
	$('#houseStatsTable tr:eq(4) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($house.totalFurniXp));
}

/*Updates the total cost of furniture and rooms combined */
function updateTotalCost(){
	$('#houseStatsTable tr:eq(5) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers(($house.totalRoomCost + $house.totalFurniCost)));
}

/*-ROOM STATS */

/*Updates the Room Stats Panel when a room is selected*/
function updateRoomStatsContent(){
	var $room = houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]];

	updateRequiredLevel($room);
	updateRoomCost($room);
	updateSelectedRoomName($room);
	displayRoomRotateButtons($room);
}

/*Updates the name of the room */
function updateSelectedRoomName(room){
	$('#roomStatsTable tr:eq(0) th:eq(0) span:eq(0)').html(room.labelText);
	$('#demolishRoomButton').attr({
		value : "Demolish " + room.labelText,
		title : "Demolish the " + room.labelText
	});
	$('#rotateCwButton').attr("title", "Rotate the " + room.labelText + " Clockwise");
	$('#rotateCcwButton').attr("title", "Rotate the " + room.labelText + " Anti-Clockwise");
}

/*Updates the required level */
function updateRequiredLevel(room){
	$('#roomStatsTable tr:eq(1) td:eq(1) span:eq(0)').html(room.level);
}

/*Updates the cost */
function updateRoomCost(room){
	$('#roomStatsTable tr:eq(2) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers(room.cost));
}

/*Hides the 'rotate' buttons if the room has 4 doors and shows them otherwise */
function displayRoomRotateButtons(room){
	if(room.hasFourDoors){
		$('#roomRotateButtons').hide();
	} else {
		$('#roomRotateButtons').show();
	}
}
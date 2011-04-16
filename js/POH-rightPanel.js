
var POHRightPanel = function(){

	var rp = this;

	/*-DROP-DOWN MENU */

	/*Shows the floor that has been selected by the dropdown */
	this.showSelectedOverviewFloor = function(){
		$('#' + poh.determineIDOfFloor($house.visibleOverviewFloor)).show();
	};

	/*Hides the floor that has been selected by the dropdown */
	this.hideSelectedOverviewFloor = function(){
		$('#' + poh.determineIDOfFloor($house.visibleOverviewFloor)).hide();
	};

	/*-HOUSE STATS */

	/*Updates the House Stats Array and visible representation of it when a room is built or demolished 
	Build - true if building, false if demolishing */
	this.updateHouseStatsContent = function(build, i, j, k){
		if(build){
			$house.builtRooms++; //say that you've built it
			$house.totalRoomCost += poh.houseArray[i][j][k].cost; //inc room cost
		} else {
			$house.builtRooms--; //say that you've demolished it
			$house.totalRoomCost -= poh.houseArray[i][j][k].cost; //reduce room cost
		}
		if(!$house.loading){
			rp.updateNumberOfRooms();
			rp.updateRoomsCost();
			rp.updateTotalCost();
		}
	};

	/*Reduces the Furniture sections under the 'House Stats' heading by the values that the furniture in the specified room is worth */
	this.updateFurniValuesOnRoomDemolition = function( i, j, k ){
		if( !$house.transforming ){
			$house.totalFurniCost -= poh.pm.calculateRoomFurniCost( i, j, k );
			$house.totalFurniXp -= poh.pm.calculateRoomFurniXp( i, j, k );
			rp.updateFurnitureCost();
			rp.updateFurnitureXp();
		}
	};
	
	/*Updates the number of rooms */
	this.updateNumberOfRooms = function(){
		//$('#houseStatsTable tr:eq(1) td:eq(1) span:eq(0)').html($house.builtRooms +' / '+ $house.maxRooms);
		$('#statsBuiltRooms').html($house.builtRooms);
	};

	/*Updates the maximum bumber of rooms */
	this.updateMaxNumberOfRooms = function(){
		$('#statsMaxRooms').html($house.maxRooms);
	};

	/*Updates the cost of rooms */
	this.updateRoomsCost = function(){
		//$('#houseStatsTable tr:eq(2) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($house.totalRoomCost));
		$('#statsTotalRoomCost').html(poh.addCommasToLargeNumbers($house.totalRoomCost));
	};

	/*Updates the cost of furniture */
	this.updateFurnitureCost = function(){
		//$('#houseStatsTable tr:eq(3) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($house.totalFurniCost));
		$('#statsTotalFurniCost').html(poh.addCommasToLargeNumbers($house.totalFurniCost));
	}

	/*Updates the experience given by furniture */
	this.updateFurnitureXp = function(){
		//$('#houseStatsTable tr:eq(4) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($house.totalFurniXp));
		$('#statsTotalFurniXp').html(poh.addCommasToLargeNumbers($house.totalFurniXp));
	};

	/*Updates the total cost of furniture and rooms combined */
	this.updateTotalCost = function(){
		//$('#houseStatsTable tr:eq(5) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers(($house.totalRoomCost + $house.totalFurniCost)));
		$('#statsTotalHouseCost').html(poh.addCommasToLargeNumbers($house.totalRoomCost + $house.totalFurniCost));
	};

	/*-ROOM STATS */

	/*Updates the Room Stats Panel when a room is selected*/
	this.updateRoomStatsContent = function(){
		var $room = poh.houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]];

		rp.updateRequiredLevel($room);
		rp.updateRoomCost($room);
		rp.updateSelectedRoomName($room);
		rp.updateRoomFurniCost( $house.csr[0], $house.csr[1], $house.csr[2] );
		rp.updateRoomFurniXp( $house.csr[0], $house.csr[1], $house.csr[2] );
		rp.displayRoomRotateButtons($room);
	};

	/*Updates the name of the room */
	this.updateSelectedRoomName = function($room){
		//$('#roomStatsTable tr:eq(0) th:eq(0) span:eq(0)').html($room.labelText);
		$('#statsRoomNameHeading').html($room.labelText);
		$('#demolishRoomButton').attr({
			value : "Demolish " + $room.labelText
		}).data('tooltipText', "Demolish the " + $room.labelText);
		$('#rotateCwButton').data('tooltipText', "Rotate the " + $room.labelText + " Clockwise");
		$('#rotateCcwButton').data('tooltipText', "Rotate the " + $room.labelText + " Anti-Clockwise");
	};

	/*Updates the required level */
	this.updateRequiredLevel = function($room){
		//$('#roomStatsTable tr:eq(1) td:eq(1) span:eq(0)').html($room.level);
		$('#statsRoomLevel').html($room.level);
	};

	/*Updates the cost */
	this.updateRoomCost = function($room){
		//$('#roomStatsTable tr:eq(2) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($room.cost));
		$('#statsRoomCost').html(poh.addCommasToLargeNumbers($room.cost));
	};
	
	/*Updates the cost of furniture in the room */
	this.updateRoomFurniCost = function( i, j, k ){
		//$('#roomStatsTable tr:eq(2) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($room.cost));
		$('#statsRoomFurniCost').html( poh.addCommasToLargeNumbers( poh.pm.calculateRoomFurniCost( i, j, k ) ) );
	};
	
	/*Updates the xp provided by furniture in the room */
	this.updateRoomFurniXp = function( i, j, k ){
		//$('#roomStatsTable tr:eq(2) td:eq(1) span:eq(0)').html(addCommasToLargeNumbers($room.cost));
		$('#statsRoomFurniXp').html( poh.addCommasToLargeNumbers( poh.pm.calculateRoomFurniXp( i, j, k ) ) );
	};

	/*Hides the 'rotate' buttons if the room has 4 doors and shows them otherwise */
	this.displayRoomRotateButtons = function(room){
		if(room.hasFourDoors){
			$('#roomRotateButtons').hide();
		} else {
			$('#roomRotateButtons').show();
		}
	};
};
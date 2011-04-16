//the 'reveal' function of the facebox has been modified to allow the tablesorter to work when in the facebox
//the aToolTip plugin has been changed to make it work as wanted

var $house = {
	builtRooms : 0, //the number of rooms that have been built
	maxRooms : 20, //the maximum number of rooms that can be built based on the specified Construction level
	totalRoomCost : 0, //the total cost of all the rooms
	totalFurniCost : 0, //the total cost of all the furniture
	totalFurniXp : 0, //the total experience that building all the specified furniture will provide
	visibleOverviewFloor : 1, //the floor that is being viewed in Overview Mode
	initialised : false, //whether the house has fully initialised
	hasCostumeRoom : false, //the house has a costume room built (can only have one)
	hasMenagerie : false, //the house has a menagerie built (can only have one)
	ignoreLevelLimits : false //whether you should ignore level limits when building a room
};
var houseArray = new Array(3); //the array that makes up the house
var roomsArray; //an array of all the different types of room, set via initRooms() in rooms.js
var currentlySelectedRoom = [-1, -1, -1]; //the location [i, j, k] of the currently selected room. Points to a location in houseArray[i][j][k]
var savedTooltipTitle; //a variable to make the aToolTips work as wanted
var savedTooltipID; //another variable to make the aToolTips work as wanted
//var houseStatsArray = new Array(4); //an array to store the information that goes in the #houseStatsTable. Inited in initHouseStatsArray()
//var initialised; //whether everything has been initialised and the POHPlanner is ready to use. true or false. Set within $( document ).ready()

/*Data classes*/
/*Room*/
function room(in_type, in_labelText, in_level, in_cost, in_doorLayout, in_imgURL, in_buildIcon){
	this.type = in_type;
	this.labelText = in_labelText;
	this.level = in_level;
	this.cost = in_cost;
	this.doorLayout = in_doorLayout;
	this.hasFourDoors = (checkArrayContentsAllFalse(this.doorLayout)) ? true : false;
	this.doorImgURL = '';
	this.imgURL = in_imgURL;
	this.buildIcon = in_buildIcon;
}

/*Returns true if all elements of an array are 'false', otherwise returns false */
function checkArrayContentsAllFalse(array){
	for(var i = 0; i < array.length; i++){
		if(array[i] === true){
			return false;
		}
	}
	return true;
}

/*Returns the #roomID of the room located in position houseArray[i][j][k] */
function calculateRoomID(i, j, k){
	return "#room" + i + j + k;
}

/*Sets the ID (location) of the currently selected room in Overview mode*/
function setSelectedRoom(i, j, k){
	//remove the visible selection from the previous room
	var roomID = "#room" + currentlySelectedRoom[0] + currentlySelectedRoom[1] + currentlySelectedRoom[2];
	$(roomID).removeClass('selectedRoom');
	
	//select the new room
	currentlySelectedRoom[0] = i;
	currentlySelectedRoom[1] = j;
	currentlySelectedRoom[2] = k;

	//if unselecting all rooms (demolishing or switching floor)
	if(i == -1 || j == -1 || k == -1){
		//hide the room info panel
		$('#infoPanelRoomStats').hide();
	} else {
		//visibly select the new room
		roomID = calculateRoomID(i, j, k);
		$(roomID).addClass('selectedRoom');
		//console.log('selected: ' + currentlySelectedRoom[0] + " " + currentlySelectedRoom[1] + " " + currentlySelectedRoom[2]); 
		
		//if there's a room built there
		if(houseArray[currentlySelectedRoom[0]][currentlySelectedRoom[1]][currentlySelectedRoom[2]].labelText !== ' '){
			//update the stats about the selected room
			updateRoomStatsContent();
			//and show it
			$('#infoPanelRoomStats').show();
		} else { //otherwise hide the panel
			$('#infoPanelRoomStats').hide();
		}
	}
}

/*Deselects the current room by setting it to the default [-1, -1, -1] */
function deselectAllRooms(){
	setSelectedRoom(-1, -1, -1);
}

/*Generates a set of borders to fit around content found in #innerID.
#outerID allows you to provide a background colour for this section.
Returns a string containing a series of divs */
function generateBorders(outerID, innerID){
	var borderClasses = [	'pohBorderTop',
		'pohBorderBottom',
		'pohBorderLeft',
		'pohBorderRight',
		'pohBorderCornerTR',
		'pohBorderCornerTL',
		'pohBorderCornerBR',
		'pohBorderCornerBL'];
	var borders = '';
	
	//create the outer div
	borders += "<div id=\"" + outerID + "\">";
	//create the borders
	for(var i = 0; i < borderClasses.length; i++){
		borders += "<div class=\"" + borderClasses[i] + "\">";
	}
	//create the inner div
	borders += "<div id=\"" + innerID + "\">";
	//close divs
	for(var i = 0; i < (borderClasses.length) + 2; i++){
		borders += "</div>";
	}
	
	return borders;
}

/*Shows the floor that has been selected by the dropdown */
function showSelectedOverviewFloor(){
	$('#' + getIDOfFloor($house.visibleOverviewFloor)).show();
}

/*Hides the floor that has been selected by the dropdown */
function hideSelectedOverviewFloor(){
	$('#' + getIDOfFloor($house.visibleOverviewFloor)).hide();
}

/*Creates the drop-down menu to select which floor you're viewing.
The 'Ground Floor' is the default selection.
Returns the HTML for the dropdown menu */
function createInfoPanelFloorSelectDropDown(){
	var menu = '<select name="floorSelect" id="floorSelectDropDown" onchange="return false;">\
	<option value="dungeon" title="Dungeon">Dungeon</option>\
	<option value="groundFloor" title="Ground Floor" selected="selected">Ground Floor</option>\
	<option value="upperFloor" title="Upper Floor">Upper Floor</option>\
	</select>';
	
	return menu;
}

/*Creates the content for #infoPanelFloorAndCharStats.
Returns the HTML */
function createInfoPanelFloorAndCharStatsContent(){
	var content = '<table id="floorAndCharStatsTable">\
	<tr title="Choose which floor of the house to view" id="facsTableR1">\
		<td><span class="floorAndCharStatsHeading">View Floor: </span></td>\
		<td>' + createInfoPanelFloorSelectDropDown() + '</td>\
	</tr>\
	<tr title="Enter your RuneScape username" id="facsTableR2">\
		<td><span class="floorAndCharStatsHeading">Username: </span></td>\
		<td><input type="text" name="usernameInput" class="usernameInput" id="usernameInput"\
			maxlength="12" /></td>\
	</tr>\
	<tr>\
		<td><input type="submit" value="Stats" class="openCharStatsButton"\
			id="openCharStatsButton" title="View and edit your stats and completed quests" /></td>\
		<td><input type="submit" value="Fetch My Exp!" class="fetchCharStatsButton"\
			id="fetchCharStatsButton" title="Fetch your stats from the RuneScape Hiscores" /></td>\
	</tr>\
	</table>';
	
	return content;
}

/*Creates the different divs for various elements in the #infoPanel.
Returns the HTML for the divs */
function createInfoPanelCoreDivs(){
	var divs = '<div id="infoPanelFloorAndCharStats"></div>\
	<div id="infoPanelHouseStats"></div>\
	<div id="infoPanelRoomStats"></div>';
	
	return divs;
}

/*Generates a table for the House or Room Stats.
Returns the HTML for the table*/
function generateInfoPanelStatsTable(tableContent, id, heading){
	var content = '<table id="' + id + '">\
	<tr><th colspan="2" class="statsTableHeading">\
		<span class="statsTypeHeading">' + heading + '</span> Stats\
	</th></tr>';
	
	//loop through each of the pieces of info that wants displaying
	for(var i = 0; i < tableContent.length; i++){
		content += '<tr>\
		<td class="statsHeading">' + tableContent[i][0] + '</td>\
		<td class="statsInfo"><span class="statsNumericalData">' + tableContent[i][1] + '</span>'
			+ '<span class="statsUnit">' + tableContent[i][2] + '</span></td>\
		</tr>';
	}
	
	content += '</table>';

	//console.log(content);
	
	return content;
}

/*Initialises the values for the House Stats Array */
/*function initHouseStatsArray(){
	houseStatsArray[0] = new Array(2);
	houseStatsArray[0][0] = 0; //number of rooms
	houseStatsArray[0][1] = 20; //max rooms - 20 default, but changes
	houseStatsArray[1] = 0; //rooms cost
	houseStatsArray[2] = 0; //furniture cost
	houseStatsArray[3] = 0; //furniture xp
}*/

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

/*Initialises the House Stats Array content */
function initHouseStatsContent(){
	//initHouseStatsArray();
	
	//say what goes in each cell in the table
	var tableContent = [
		['Number of Rooms:', $house.builtRooms + ' / ' + $house.maxRooms, ''],
		['Rooms Cost:', $house.totalRoomCost, 'gp'],
		['Furniture Cost:', $house.totalFurniCost, 'gp'],
		['Furniture Exp:', $house.totalFurniXp, 'xp'],
		['Total Cost:', ($house.totalRoomCost + $house.totalFurniCost), 'gp']
	];
	
	$('#infoPanelHouseStats').html(generateInfoPanelStatsTable(tableContent, 'houseStatsTable', 'House'));
}

/*Updates the name of the room */
function updateSelectedRoomName(room){
	$('#roomStatsTable tr:eq(0) th:eq(0) span:eq(0)').html(room.labelText);
	$('#demolishButtonRoomName').html(room.labelText);
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

/*Updates the Room Stats Panel when a room is selected*/
function updateRoomStatsContent(){
	var $room = houseArray[currentlySelectedRoom[0]][currentlySelectedRoom[1]][currentlySelectedRoom[2]];

	updateRequiredLevel($room);
	updateRoomCost($room);
	updateSelectedRoomName($room);
	displayRoomRotateButtons($room);
}

/*Determines the class for the door layout background image for a room in Overview Mode by looking at the door locations and room type
Returns a string containing the class name for that background */
function determineRoomOverviewImage(i, j, k){
	var imageURL = 'ROI'; //a default (Room Overview Image) so that it's hopefully easier to make sense of the stylesheet
	
	//indicate a dungeon or upstairs room
	if(i === 0 && houseArray[i][j][k].labelText === ' '
		|| houseArray[i][j][k].labelText.indexOf("Dungeon") != -1
			|| houseArray[i][j][k].labelText === 'Treasure Room'){
		imageURL += "d";
	} else if(i == 2 && houseArray[1][j][k].labelText !== ' '
				&& houseArray[i][j][k].labelText === ' '){
		imageURL += "u";
	}
	
	//console.log(imageURL + ' ' + houseArray[i][j][k].labelText);
	/*console.log("dl " + houseArray[i][j][k].doorLayout[0] +
		houseArray[i][j][k].doorLayout[1] +
			houseArray[i][j][k].doorLayout[2] +
				houseArray[i][j][k].doorLayout[3]);*/
	
	//compass directions of the doors
	if(houseArray[i][j][k].doorLayout[0] == true){
		imageURL += "n";
	}
	if(houseArray[i][j][k].doorLayout[1] == true){
		imageURL += "e";
	}
	if(houseArray[i][j][k].doorLayout[2] == true){
		imageURL += "s";
	}
	if(houseArray[i][j][k].doorLayout[3] == true){
		imageURL += "w";
	}
	
	//console.log(imageURL);
	
	//special types
	if(houseArray[i][j][k].labelText === ' '){
		imageURL += "empty";
	} else if(houseArray[i][j][k].labelText.indexOf('Garden') != -1){
		imageURL += "garden";
	} else if(houseArray[i][j][k].labelText.indexOf('Dungeon') != -1){
		if(houseArray[i][j][k].labelText === 'Dungeon Junction'){
			imageURL += "j";
		} else if(houseArray[i][j][k].labelText === 'Dungeon Stairs'){
			imageURL += "s";
		}
	} else if(houseArray[i][j][k].labelText === 'Treasure Room'){
		imageURL += "t";
	}
	
	//it should have at least one door if you're going to be able to enter, so something has probably gone wrong
	if(imageURL === 'ROI'){
		imageURL += "probableError";
	}
	
	//console.log(imageURL);
	
	return imageURL;
}

/*Removes the class of the previous room type when building a new one */
function removeExistingRoomOverviewImage(i, j, k){
	//console.log(i + " " + houseArray[i][j][k].doorImageURL);
	$(calculateRoomID(i, j, k)).removeClass(houseArray[i][j][k].doorImageURL);
}

/*Sets the background image for a room in Overview Mode */
function setRoomOverviewImage(i, j, k){
	houseArray[i][j][k].doorImageURL = determineRoomOverviewImage(i, j, k);
	$(calculateRoomID(i, j, k)).addClass(houseArray[i][j][k].doorImageURL);
}

/*Updates the Room Overview Image when rotating a room so that only one thing needs calling */
function changeRoomOverviewImage(i, j, k){
	//console.log(i + " s " + houseArray[i][j][k].doorImageURL);
	removeExistingRoomOverviewImage(i, j, k);
	setRoomOverviewImage(i, j, k);
}

/*Rotates the selected room - true = Clockwise, false = Anti-Clockwise
The currently selected room is located since the Rotate buttons are only visible when you have a room selected */
function rotateRoom(clockwise){
	var $room = houseArray[currentlySelectedRoom[0]][currentlySelectedRoom[1]][currentlySelectedRoom[2]];
	var temp;
	if(clockwise){
		temp = $room.doorLayout[3];
		$room.doorLayout[3] = $room.doorLayout[2];
		$room.doorLayout[2] = $room.doorLayout[1];
		$room.doorLayout[1] = $room.doorLayout[0];
		$room.doorLayout[0] = temp;
	} else {
		temp = $room.doorLayout[0];
		$room.doorLayout[0] = $room.doorLayout[1];
		$room.doorLayout[1] = $room.doorLayout[2];
		$room.doorLayout[2] = $room.doorLayout[3];
		$room.doorLayout[3] = temp;
	}
	//update the room image to show that it's been rotated
	changeRoomOverviewImage(currentlySelectedRoom[0], currentlySelectedRoom[1], currentlySelectedRoom[2]);
}

/*Creates the buttons to go in #infoPanelRoomButtons
Returns the HTML for the buttons and their containing table */
function createRoomStatsButtons(){
	var buttons = '<div id="infoPanelRoomButtons">\
		<div id="demolishRoomDiv" class="overviewRoomButton" onclick="demolishRoom0();return false;">\
			Demolish <span id="demolishButtonRoomName">Room</span></div>\
		<div id="roomRotateButtons">\
			<div id="rotateCwDiv" class="overviewRoomButton" onclick="rotateRoom(true);return false;">\
				Rotate Clockwise</div>\
			<div id="rotateCcwDiv" class="overviewRoomButton" onclick="rotateRoom(false);return false;">\
				Rotate Anti-Clockwise</div>\
		</div>\
	</div>';
	
	return buttons;
}

/*Initialises the #infoPanelRoomButtons content */
function initRoomStatsButtons(){
	$('#infoPanelRoomStats').append(createRoomStatsButtons());
}

/*Initialises the Room Stats Array content */
function initRoomStatsContent(){
	//say what goes in each cell in the table
	var tableContent = [
		['Required Level:', '', ''],
		['Room Cost:', 0, 'gp'],
		['Furniture Cost:', 0, 'gp'],
		['Furniture Exp:', 0, 'xp']
	];
	
	$('#infoPanelRoomStats').html(generateInfoPanelStatsTable(tableContent, 'roomStatsTable', 'Room'));	
	
	initRoomStatsButtons();
}

/*Initilises the content for the #infoPanel */
function initInfoPanel(){
	//create sub-sections to put content in
	$('#infoPanel').html(createInfoPanelCoreDivs());

	//create content for the floor select and stats setting section
	$('#infoPanelFloorAndCharStats').html(createInfoPanelFloorAndCharStatsContent());
	$('#floorAndCharStatsTable tr').aToolTip();
	$('#floorAndCharStatsTable input').aToolTip();

	//create content for the House Stats section
	initHouseStatsContent();
	
	/*Detects when the drop-down list for the selected floor is changed and shows the newly slected floor */
	$("#floorSelectDropDown").change(function(){
		deselectAllRooms(); //so you can't demolish something you can't see and such
		hideSelectedOverviewFloor();
		var index = $("#floorSelectDropDown option").index($("#floorSelectDropDown option:selected"));
		$house.visibleOverviewFloor = index;
		showSelectedOverviewFloor();
	});
	
	//and the Room Stats
	initRoomStatsContent();
	//before hiding it
	$('#infoPanelRoomStats').hide();
}

/*Create the table that allows you to build furniture and whatnot */
function populateRoomBuildingTable() {

	var table = "";
	
	//create the headings
	var headings = [ "Icon", "Type", "Level", "Cost" ];
	table += "<table id=\"roomBuildingTable\" class=\"tablesorter\"><thead><tr>";
	
	for(var i = 0; i < headings.length; i++){
		table += "<th>" + headings[i] + "</th>";
	}
	table += "</tr></thead><tbody>";
	//buildRoom("+currentlySelectedRoom[0]+","+currentlySelectedRoom[1]+","+currentlySelectedRoom[2]+","+i+");return false;
	//and the rows themselves
	for(var i = 0; i < roomsArray.length; i++){
		if(roomsArray[i][1] === ' '){
			//don't add empty rooms to the list
		}
		else {
		table += "<tr onclick=\"buildRoom(currentlySelectedRoom[0],currentlySelectedRoom[1]\
			,currentlySelectedRoom[2],"+i+");$(document).trigger('close.facebox');return false;\">\
			<td class=\"tablesorterRoomIcon\"><img src=\"buildIcons/" +
				roomsArray[i][6] + ".gif\" alt=\"" + roomsArray[i][6] + "\" width=\"32\" height=\"32\" /></td>\
			<td>" + roomsArray[i][1] + "</td>\
			<td>" + roomsArray[i][2] + "</td>\
			<td>" + roomsArray[i][3] + "</td></tr>";
		}
	}
	
	table += "</tbody></table>";
	
	//console.log(table);
	
	$('#roomBuildingFrame').html(table);
}

/*Create a blank house array with empty rooms*/
function initHouseArray() {
	//console.log("house array " + houseArray);
	for (var i = 0; i < 3; i++) {
		houseArray[i] = new Array(9);
		for (var j = 0; j < 9; j++) {
			houseArray[i][j] = new Array(9);
			for(var k = 0; k < 9; k++){
				houseArray[i][j][k] = new room(roomsArray[0][0],roomsArray[0][1],roomsArray[0][2],roomsArray[0][3],roomsArray[0][4],roomsArray[0][5]);
			}
		}
	}
	//console.log("house array out " + houseArray);
}

/*Returns the #ID of the floor based on the number (0, 1, 2) */
function getIDOfFloor(i){
	if(i === 0){
		return "dungeon";
	} else if(i === 1){
		return "groundFloor";
	} else {
		return "upstairs";
	}
}

/*Initialise the house, creating the HTML for it*/
function initHouse() {

	//console.time("create house array");
	initHouseArray();
	
	//console.timeEnd("create house array");	

	//console.time("building house");

	var roomsStr = "";
	var floor = "";
	var inner;
	
	for(var i = 0; i < 3; i++){
		floor = getIDOfFloor(i);
		roomsStr += "<div id=\"" + floor + "\" class=\"overviewFloor\">";
		
		for(var j = 0; j < 9; j++){
			roomsStr += "<div id=\"roomColumn" + i + j + "\" class=\"roomColumn\">";
			
			for(var k = 0; k < 9; k++){
				houseArray[i][j][k].doorImageURL = determineRoomOverviewImage(i, j, k);
				//don't link things on the top floor since you can't build in the air
				inner = i === 2 ? " " : "<span><a href=\"#roomBuildingFrame\"\
					onclick=\"setSelectedRoom("+i+","+j+","+k+");return false;\"\
						class=\"overviewLink faceboxLink\">" +
							houseArray[i][j][k].labelText + "</a></span>";
				//console.log(houseArray[i][j][k].doorImageURL);
				roomsStr += "<div id=\"room" + i + j + k + "\" class=\"room " + houseArray[i][j][k].doorImageURL +
					"\" title=\"" + getRoomOverviewTooltipText(i, j, k) +"\">" + inner + "</div>";
			}
			
			roomsStr += "</div>";
		}
		roomsStr += "</div>";
	}
	
	//console.log(roomsStr);
	
	$('#houseOverview').html(roomsStr);
	showSelectedOverviewFloor();
	//$('#groundFloor').show();

	buildRoom(1, 2, 3, 1);
	//console.timeEnd("building house");
}

/*Updates the image of the room above and whether you're able to build anything there, so you can't build a room in mid air Upstairs */
function updateRoomAbove(i, j, k){
	//if you're building a room on the Ground Floor
	if(i === 1){
		//update the image of the room above
		changeRoomOverviewImage(i + 1, j, k);
		
		//console.log(setRoomOverviewImage(i + 1, j, k));
		//console.log(determineRoomOverviewImage(i + 1, j, k));
		
		//work out the ID of the room above
		var aboveID = calculateRoomID(i + 1, j, k);
		
		//if you're building something, make the room above clickable
		if(houseArray[i][j][k].labelText !== ' '){
			//create a link and such
			$(aboveID).html("<span><a href=\"#roomBuildingFrame\"\
				onclick=\"setSelectedRoom("+(i + 1)+","+j+","+k+");return false;\"\
					class=\"overviewLink faceboxLink\">" +
						houseArray[i + 1][j][k].labelText + "</a></span>")
				//set the title so it has a tooltip
				.attr("title", getRoomOverviewTooltipText(i + 1, j, k));
			//make it so you can build a room here
			$(aboveID + ' .faceboxLink').facebox();
		} else {
			//remove the ability to do anything useful
			$(aboveID).html(" ").unbind();
		}
	}
}	

/*Multiple functions are based on having different numbers of parameters so that it can be used in different situations
The digit on the end denotes the number of parameters and if it isn't there, problems with infinite recursion occur */
/*Demolishes a room, the parameters being the location of the room to demolish */
function demolishRoom3(i, j, k){
	var roomID = calculateRoomID(i, j, k);
	updateHouseStatsContent(false, i, j, k);
	
	//build an empty 'room' in it's place
	buildRoom(i, j, k, 0);
	deselectAllRooms();
	
	//make it posible to build a new room in it's place
	$(roomID + " a").addClass("faceboxLink").attr("href", "#roomBuildingFrame").facebox();
	
	//update the room above
	updateRoomAbove(i, j, k);
}

/*Demolishes the selected room */
function demolishRoom0(){
	demolishRoom3(currentlySelectedRoom[0], currentlySelectedRoom[1], currentlySelectedRoom[2]);
}

/*Builds a room*/
function buildRoom(i, j, k, typeID){
	var roomID = calculateRoomID(i, j, k);
	//removes existing type
	removeExistingRoomOverviewImage(i, j, k);
	
	//build the room
	houseArray[i][j][k] = new room(roomsArray[typeID][0],roomsArray[typeID][1],roomsArray[typeID][2],
		roomsArray[typeID][3],roomsArray[typeID][4],roomsArray[typeID][5],roomsArray[typeID][6]);

	//visibly show the change by switching the background image
	setRoomOverviewImage(i, j, k);
	//and changing the tooltip
	$(roomID).attr("title", getRoomOverviewTooltipText(i, j, k));
	
	//and updating the room above
	updateRoomAbove(i, j, k);
	
	//.addClass(houseArray[i][j][k].type)
	//console.log('new title: ' + $(roomID).attr('title'));
	//$(roomID).aToolTip({tipContent: ''});
	
	//then switching the text
	$(roomID  + ' a').html(houseArray[i][j][k].labelText)
	//and remove the facebox link
	.removeClass("faceboxLink")
	.attr("href", roomID + "PictureMode")
	.unbind();
	
	//and updating the House Stats
	//if not building a blank 'room'
	if(houseArray[i][j][k].labelText != ' '){
		updateHouseStatsContent(true, i, j, k);
	}
	if($house.initialised){
		//select the room
		setSelectedRoom(i, j, k);
	}
}

/*Returns the text that is used as the tooltip for rooms in Overview mode*/
function getRoomOverviewTooltipText(i, j, k){
	if (i === 2 && houseArray[i][j][k].doorImageURL === 'ROIempty'){
		return "";
	} else if (houseArray[i][j][k].labelText === ' '){
		return "Click to build a room";
	} else {
		return houseArray[i][j][k].labelText + " - Click to enter furniture build mode";
	}
}

/*Adds commas to numbers >= 1000 to make them easier to read
Doesn't work for negative numbers, although shouldn't need to, so it doesn't matter
About 15% faster (using console.time() with Firebug) than anything Google will find */
function addCommasToLargeNumbers(number){
	if(number >= 1000){
		var noStr = number + '';
		var decimalPointPosition = noStr.indexOf('.');
		var decimal = '';
		if(decimalPointPosition != -1){
			decimal = noStr.substr(decimalPointPosition, noStr.length - decimalPointPosition);
			noStr = noStr.substr(0, decimalPointPosition);
		}
		var numberOfCommas = Math.floor((noStr.length - 1) / 3);
		var output = noStr.substr(0, (noStr.length - (numberOfCommas * 3)));
		var lengthBeforeCommas = output.length;
		for(var i = 0; i < numberOfCommas; i++){
			output += ',' + noStr.substr(lengthBeforeCommas, 3);
			lengthBeforeCommas += 3;
		}
		return output + decimal;
	} else {
		return number;
	}
}

$( document ).ready( function() {
	//$('.map').maphilight();
	//$('area').aToolTip({inSpeed: 400, outSpeed: 100});

	console.time("document ready");

	//generate the core divs that everything will go in
	var core = generateBorders('overviewBorder', 'houseOverview');
	core += generateBorders('infoPanelBorder', 'infoPanel');
	$('#POHPlanner').html(core);
	
	//initialise the content for the Info Panel (the one on the right)
	initInfoPanel();

	//creating the initial house
	roomsArray = initRooms();
	initHouse();
	$('#houseOverview div').aToolTip();

	//creating the table so you can make more rooms
	populateRoomBuildingTable();
	
	//make clicking on rooms in Overview mode open up the table they link to in a facebox
	$('#houseOverview .faceboxLink').facebox();
	
	//console.log('Planner width: ' + $('#POHPlanner').width());
	//console.log('width: ' + ($('#houseOverview').width() + $('#infoPanel').width()));
	//console.log('height: ' + $('#houseOverview').height());

	console.timeEnd("document ready");
	
	//console.log(addCommas(123456789.123456));
	//console.log(addCommasToLargeNumbers(123456789.123456));

	$house.initialised = true;
	//var t = document.getElementById('POHPlanner').innerHTML;
	//console.log(t);
});